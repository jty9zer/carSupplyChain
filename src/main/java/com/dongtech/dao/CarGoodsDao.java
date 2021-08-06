package com.dongtech.dao;


import com.dongtech.vo.*;

import java.math.BigDecimal;
import java.util.List;

public interface CarGoodsDao {
    List<CarGoods> queryList(CarGoods carGoods);

    List<CarOrders> queryOrders();

    void saveOrder(String number, BigDecimal price);

    List<CarOrderDetails> queryOrdersDetails(Integer id);

    int queryMaxOrderId();

    void saveOrdersDetails(String goods_name, int num, String produce, BigDecimal price, int order_id);

    TearDownDetails queryOrdersTearDownDetails(CarOrderDetails c);

    void saveTearDownDetailsOrders(TearDownDetails t);

    List<CarOrderDetails> queryOrdersDetailsAll();
}
