package com.dongtech.dao.impl;


import com.dongtech.dao.CarGoodsDao;
import com.dongtech.util.JDBCUtil;
import com.dongtech.vo.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据层，只负责与数据库的数据交互，将数据进行存储读取操作
 */
public class CarGoodsDaoImpl implements CarGoodsDao {


    /**
     * 查询所有商品信息
     *
     * @param carGoods
     * @return
     */
    @Override
    public List<CarGoods> queryList(CarGoods carGoods) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarGoods> bookList = new ArrayList<CarGoods>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM cargoods where 1=1");
            if (!StringUtils.isEmpty(carGoods.getId())) {
                sql.append(" and id =").append(carGoods.getId());
            }
            if (!StringUtils.isEmpty(carGoods.getName())) {
                sql.append("  and name like '%").append(carGoods.getName()).append("%'");
            }
            if (!StringUtils.isEmpty(carGoods.getType())) {
                sql.append("  and type='").append(carGoods.getType()).append("'");
            }
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarGoods vo = new CarGoods(rs.getLong("id"),
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("produce"),
                        rs.getBigDecimal("price"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("num")

                );
                bookList.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return bookList;
    }

    /**
     * @Author gzl
     * @Description：查询订单信息
     * @Exception
     * @Date： 2020/4/20 12:04 AM
     */
    @Override
    public List<CarOrders> queryOrders() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrders> carOrdersList = new ArrayList<CarOrders>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM car_orders where 1=1");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrders vo = new CarOrders(rs.getLong("id"),
                        rs.getString("number"),
                        rs.getBigDecimal("price")

                );
                carOrdersList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrdersList;
    }

    /**
     * 保存订单信息
     *
     * @param number
     * @param price
     */
    @Override
    public void saveOrder(String number, BigDecimal price) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = 0;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            String sql = "INSERT INTO jk_pro_db.car_orders(number, price) values (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, number);
            ps.setBigDecimal(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
    }

    /**
     * 查询新增订单的ID，即最大的ID
     *
     * @return
     */
    @Override
    public int queryMaxOrderId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = 0;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT MAX(id) FROM car_orders");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return id;
    }

    /**
     * @Author gzl
     * @Description：查询订单详情
     * @Exception
     * @Date： 2020/4/20 12:17 AM
     */
    @Override
    public List<CarOrderDetails> queryOrdersDetails(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrderDetails> carOrderDetailsList = new ArrayList<CarOrderDetails>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM car_orders_details where 1=1");
            if (!StringUtils.isEmpty(id)) {
                sql.append(" and order_id =").append(id);
            }
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrderDetails vo = new CarOrderDetails(rs.getLong("id"),
                        rs.getString("goods_name"),
                        rs.getInt("num"),
                        rs.getString("produce"),
                        rs.getBigDecimal("price"),
                        rs.getInt("order_id")

                );
                carOrderDetailsList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrderDetailsList;
    }

    /**
     * 保存订单详情
     *
     * @param goods_name
     * @param num
     * @param produce
     * @param price
     * @param order_id
     */
    @Override
    public void saveOrdersDetails(String goods_name, int num, String produce, BigDecimal price, int order_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
            String sql = "INSERT INTO jk_pro_db.car_orders_details(goods_name, num,produce, price, order_id) values (?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            long randomNum = System.currentTimeMillis();
            ps.setString(1, goods_name);
            ps.setInt(2, num);
            ps.setString(3, produce);
            ps.setBigDecimal(4, price);
            ps.setInt(5, order_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
    }

    /**
     * 查询拆分订单的详情
     *
     * @param c
     * @return
     */
    @Override
    public TearDownDetails queryOrdersTearDownDetails(CarOrderDetails c) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TearDownDetails tearDownDetails = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM tear_down_details where 1=1");
            if (!StringUtils.isEmpty(c)) {
                sql.append(" and produce ='").append(c.getProduce()).append("'");
            }
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                tearDownDetails = new TearDownDetails(rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getString("produce"),
                        rs.getString("cargoods_name"),
                        rs.getInt("num")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
            return tearDownDetails;
        }
    }

    /**
     * 保存拆分的订单详情
     *
     * @param t
     */
    @Override
    public void saveTearDownDetailsOrders(TearDownDetails t) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
            String sql = "INSERT INTO jk_pro_db.tear_down_details(cargoods_name, num,produce, order_id) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getCargoods_name());
            ps.setInt(2, t.getNum());
            ps.setString(3, t.getProduce());
            ps.setLong(4, t.getOrderId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
    }

    /**
     * 查询所有的订单详情，用于生成统计数据
     *
     * @return
     */
    @Override
    public List<CarOrderDetails> queryOrdersDetailsAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrderDetails> carOrderDetailsList = new ArrayList<CarOrderDetails>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM car_orders_details");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrderDetails vo = new CarOrderDetails(rs.getLong("id"),
                        rs.getString("goods_name"),
                        rs.getInt("num"),
                        rs.getString("produce"),
                        rs.getBigDecimal("price"),
                        rs.getInt("order_id")

                );
                carOrderDetailsList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrderDetailsList;
    }
}

