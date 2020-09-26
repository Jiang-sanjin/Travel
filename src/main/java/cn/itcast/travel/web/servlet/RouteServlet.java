package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String uidStr = request.getParameter("uid");

        //接受rname 线路名称
        String rname = request.getParameter("rname");
        //解决乱码问题
        if (rname !=null && rname.length() > 0 && !"null".equals(rname)) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }
        int cid = 0;  //类别id
        //2.处理参数     cid为空时不进行转换
        if (cidStr !=null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        int uid = 0;  //类别id
        //2.处理参数     cid为空时不进行转换
        if (uidStr !=null && uidStr.length() > 0 && !"null".equals(uidStr)){
            uid = Integer.parseInt(uidStr);
        }
//        System.out.println("uid"+uid);
        int currentPage = 0;   //当前页码不传递   默认第一页
        if (currentPageStr !=null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }
        else {
            currentPage = 1;
        }

        int pageSize = 0;  //每页显示条数，如果不传递，默认显示5条
        if (pageSizeStr !=null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        else {
            pageSize = 5;
        }

        //3.调用service查询pageBean对象

        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname,uid);
        //4.将pageBean对象序列化为json
        writeValue(pb,response);

    }


    /**
     * 根据id查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route,response);
    }

    /**
     * 判断当前登录用户是否收藏线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取线路 rid
        String rid = request.getParameter("rid");
        //获取当前登录用户的 user
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;  //定义用户 uid
        if (user == null ){
            //用户尚未登录
            return;

        }
        else{
            //用户已登录
            uid = user.getUid();
        }

        //调用favoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //写回客户端
        writeValue(flag,response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取线路rid
        String rid = request.getParameter("rid");
        //获取当前登录用户
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;  //定义用户 uid
        if (user == null ){
            //用户尚未登录
            return ;
        }
        else{
            //用户已登录
            uid = user.getUid();
        }

        //调用service添加
        favoriteService.add(rid,uid);
    }


}
