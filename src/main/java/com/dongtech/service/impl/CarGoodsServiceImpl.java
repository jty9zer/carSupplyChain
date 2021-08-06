package com.dongtech.service.impl;


import com.dongtech.dao.CarGoodsDao;
import com.dongtech.dao.impl.CarGoodsDaoImpl;
import com.dongtech.service.CarVGoodsService;
import com.dongtech.vo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CarGoodsServiceImpl implements CarVGoodsService {

    CarGoodsDao dao = new CarGoodsDaoImpl();


    @Override
    public List<CarGoods> queryList(CarGoods carGoods) throws SQLException {
        return dao.queryList(carGoods);
    }

    @Override
    public List<CarOrders> queryOrders() {
        return dao.queryOrders();
    }

    @Override
    public void saveOrders(List<Cart> cartInCookie) {
        int number = (int) (Math.random() * 1000000);
        int totalPrice = 0;
        for (Cart cart : cartInCookie) {
            totalPrice += cart.getPrice();
        }
        dao.saveOrder(String.valueOf(number), BigDecimal.valueOf(totalPrice));
        int id = dao.queryMaxOrderId();
        for (Cart cart : cartInCookie) {
            totalPrice += cart.getPrice();
            dao.saveOrdersDetails(cart.getName(), cart.getNum(), cart.getProduce(), BigDecimal.valueOf(cart.getPrice()), id);
        }
    }

    @Override
    public List<CarOrderDetails> queryOrdersDetails(Integer id) {
        return dao.queryOrdersDetails(id);
    }

    @Override
    public TearDownDetails queryOrdersTearDownDetails(CarOrderDetails c) {
        return dao.queryOrdersTearDownDetails(c);
    }

    @Override
    public void saveTearDownDetailsOrders(TearDownDetails t) {
        dao.saveTearDownDetailsOrders(t);
    }

    @Override
    public List<CartItem> queryCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        List<CarOrderDetails> carOrderDetailsList = new ArrayList<>();
        carOrderDetailsList = dao.queryOrdersDetailsAll();
        for (CarOrderDetails carOrderDetails : carOrderDetailsList) {
            // flag用于判断该订单中的商品是否已经被加入返回的列表中
            int flag = 0;
            for (CartItem cartItem : cartItemList) {
                //如果商品已经被加入返回的列表中，只更新数量
                if (cartItem.getCarGoods().getName().equals(carOrderDetails.getGoodsname())) {
                    cartItem.setCount(cartItem.getCount() + carOrderDetails.getNum());
                    flag = 1;
                    break;
                }
            }
            //如果商品没被加入过，flag为0，新建cartItem，加入返回的列表中
            if (flag == 0) {
                CarGoods carGoods = new CarGoods();
                carGoods.setName(carOrderDetails.getGoodsname());
                CartItem cartItem1 = new CartItem();
                cartItem1.setCarGoods(carGoods);
                cartItem1.setCount(carOrderDetails.getNum());
                cartItemList.add(cartItem1);
            }
        }
        return cartItemList;
    }


}
