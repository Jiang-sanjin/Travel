package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //声明一个userservice业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //验证码校验   获取前台输入的验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次

        //比较
        if (checkcode_server == null || !checkcode_server.equals(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);

            //设置content-type
            response.setContentType("applaction/json; chatset=utf-8");
            response.getWriter().write(json);

            return;

        }

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user  = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
//        UserService service = new UserServiceImpl();
        boolean flag =  service.regist(user);
        //4.响应结果
        ResultInfo info = new ResultInfo();
        if (flag){
            //注册成功
            info.setFlag(true);
        }
        else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }



        //将info对象序列化为json
        String json = writeValueAsString(info);
/*        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);*/

        //将json数据写回客户端
        //设置content-type
        response.setContentType("applaction/json;charset = utf-8");
        response.getWriter().write(json);

    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验   获取前台输入的验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次

        //比较
        if (checkcode_server == null || !checkcode_server.equals(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //登录失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);

            //设置content-type
            response.setContentType("applaction/json; chatset=utf-8");
            response.getWriter().write(json);

            return;

        }
        //获取用户名密码
        Map<String, String[]> map = request.getParameterMap();
        //封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用Service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();
        //判断u 是否为null
        if (u==null){
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名密码错误");
        }
//5.判断用户是否激活
        if(u != null && !"Y".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        //6.判断登录成功
        if(u != null && "Y".equals(u.getStatus())){
            request.getSession().setAttribute("user",u);//登录成功标记

            //登录成功
            info.setFlag(true);
        }
        //响应数据
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json; charset = utf-8");
        mapper.writeValue(response.getOutputStream() ,info);
    }

    /**
     * 查找单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将用户名写回客户端
        writeValue(user,response);
/*        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("applaction/json; charset = utf-8");
        mapper.writeValue(response.getOutputStream(),user);*/
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        // 1.销毁session
        request.getSession().invalidate();

        //2.重定向  跳转页面  需要或取虚拟路径
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.激活码

        String code = request.getParameter("code");

        if (code!=null){
            //2.调用service完成激活
//            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断标记
            String msg = null;
            if (flag){
                //激活成功
                // msg = "激活成功，请<a href='http://localhost:8080/travel/login.html'>登录</a>";
                msg = "激活成功，请<a href='http://sanjin.work/login.html'>登录</a>";
            }
            else {
                msg = "激活失败";
            }
            response.setContentType("text/html;charset = utf-8");
            response.getWriter().write(msg);
        }
    }
}
