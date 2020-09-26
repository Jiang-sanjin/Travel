package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 1.根据cid查询总记录数
     */
    public int findTotalCount(int cid,String rname);

    /**
     * 根据cid , start , pageSize 查询当前页的数据集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize,String rname);

    /**
     * 根据  rid  查询
     * @param rid
     * @return
     */
    public Route findOne(int rid);

    /**
     * 根据  uid  查询总记录数
     * @param uid
     * @return
     */
    public int findUidCount(int uid);

    /**
     * 根据  uid 查询 收藏的页码
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findUidPage(int uid, int start, int pageSize);
}
