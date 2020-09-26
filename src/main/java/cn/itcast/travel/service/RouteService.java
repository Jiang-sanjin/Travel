package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * 线路service
 */
public interface RouteService {
    /**
     * 根据类别进行分类查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname,int uid);

    /**、
     * 根据 id查询
     * @param rid
     * @return
     */
    public Route findOne(String rid);

}
