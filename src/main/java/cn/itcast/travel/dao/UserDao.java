package cn.itcast.travel.dao;


import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 保存用户信息
     * @param user
     */
    public void save(User user);


    User findByCode(String code);

    void updateStatus(User user);

    /**
     * 根据用户名和密码查询
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
