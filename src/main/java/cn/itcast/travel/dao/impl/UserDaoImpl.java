package cn.itcast.travel.dao.impl;


import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询用户名
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        //template查询不到会进行报错 需要抛出异常
        try{

            //1.定义sql
            String sql = "select * from tab_user where username = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 保存用户
     * @param user
     */
    @Override
    public void save(User user) {

        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";

        template.update(sql,user.getUsername(),user.getPassword(),
                user.getName(),user.getBirthday(),user.getSex(),
                user.getTelephone(),user.getEmail(),user.getStatus(),user.getCode());



    }

    /**
     * 查询用户激活码
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 修改用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = " update tab_user set status = 'Y' where uid=?";
        template.update(sql,user.getUid());


    }

    /**
     * 根据用户名和密码查询的方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        //template查询不到会进行报错 需要抛出异常
        try{

            //1.定义sql
            String sql = "select * from tab_user where username = ? and password = ? ";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        }
        catch (Exception e){
//            e.printStackTrace();
        }

        return user;
    }
}
