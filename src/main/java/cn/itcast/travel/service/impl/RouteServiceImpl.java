package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();

    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize ,String rname, int uid) {
        //封装pafeBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentpage(currentPage);

        //设置每页显示条数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount = 0;
        //设置当前页显示的数据集合
        int start = (currentPage-1)*pageSize;  //开始的记录数
        List<Route> list = null;
        if (cid!=0){
            totalCount=routeDao.findTotalCount(cid,rname);
            list = routeDao.findByPage(cid,start,pageSize,rname);
        }
        else if (uid!=0){
            totalCount=routeDao.findUidCount(uid);
            list = routeDao.findUidPage(uid,start,pageSize);
         
        }


        pb.setTotalCount(totalCount);



        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize  : (totalCount / pageSize) +1;
        pb.setTotalPage(totalPage);
         return pb;

    }

    @Override
    public Route findOne(String rid) {
        //根据 id 查询route表的route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //根据 route的 id查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //集合设置到route对象
        route.setRouteImgList(routeImgList);
        //根据route 的 sid（商家id） 查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //4.查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());

        route.setCount(count);
        return route;
    }




}
