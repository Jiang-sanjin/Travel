package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询总页码
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid,String rname) {

            //1.定义sql模板
            String sql = "select count(*) from tab_route where 1=1 ";
            StringBuilder sb = new StringBuilder(sql);

            List params = new ArrayList();//条件们
            //2.判断参数是否有值
            if(cid != 0){
                sb.append( " and cid = ? ");

                params.add(cid);//添加？对应的值
            }



//        System.out.println(rname);
            /**
             * 此处注意  rname 为空!"null".equals(rname)
             * 注意ranme 值为null的情况
             * rname != null  判断 rname是否为空引用
             * !"null".equals(rname)  判断rname值是否为null
             */
            if(rname != null && rname.length() > 0 && !"null".equals(rname)){
                sb.append(" and rname like ? ");

                params.add("%"+rname+"%");
            }

            sql = sb.toString();

            Integer integer = template.queryForObject(sql, Integer.class, params.toArray());
//            System.out.println(integer);
            return template.queryForObject(sql,Integer.class,params.toArray());


    }


    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname ) {
//        String sql = "select * from tab_route where cid = ? limit ?,?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname) ){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);


        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    /**
     * 根据  rid  进行查询
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        String sql = "Select * from tab_route where rid  = ? ";

        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    /**
     * 根据  uid  查询个数
     * @param uid
     * @return
     */
    @Override
    public int findUidCount(int uid) {
        String sql = "Select count(*) from tab_favorite where uid  = ? ";

        return template.queryForObject(sql,Integer.class,uid);
    }

    /**
     * 根据  uid 查询 收藏的页码
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findUidPage(int uid, int start, int pageSize) {
//        select * from tab_route where  rid in (select rid from tab_favorite where uid = 15)
        String sql = "select * from tab_route where (rid in (select rid from tab_favorite where uid = ?) ) limit ?,?";
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),uid,start,pageSize);
    }


}
