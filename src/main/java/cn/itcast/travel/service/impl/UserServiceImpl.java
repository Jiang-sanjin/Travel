package cn.itcast.travel.service.impl;


import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    //创建操作用户的对象
    private UserDao userdao = new UserDaoImpl();
    /**
     * 邮件注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {

        //根据用户名查询用户对象
        User u = userdao.findByUsername(user.getUsername());
        if (u!=null){
            //用户存在，注册失败
            return false;
        }

        //保存用户信息
        //设置唯一的激活码
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        userdao.save(user);

        //激活邮件发送

        String content = "<a href='http://localhost:8080/travel/active?code="+user.getCode()+"'>点击激活</a>" ;
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象

        User user =  userdao.findByCode(code);
        //2.调用修改激活状态方法
        if (user!=null){
            userdao.updateStatus(user);
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {

        return userdao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }


}
