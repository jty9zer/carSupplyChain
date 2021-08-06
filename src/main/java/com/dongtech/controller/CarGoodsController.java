package com.dongtech.controller;

import com.dongtech.service.CarVGoodsService;
import com.dongtech.util.JSONResult;
import com.dongtech.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gzl
 * @date 2020-04-15
 * @program: springboot-jsp
 * @description: ${description}
 */
@Controller
@RequestMapping("cargoods")
public class CarGoodsController {


    @Resource
    private CarVGoodsService carVGoodsService;


    /**
     * @Author gzl
     * @Description：查询商品列表
     * @Exception
     */
    @RequestMapping("/queryList")
    public ModelAndView queryList(CarGoods carGoods) {
        List<CarGoods> list = new ArrayList<>();
        try {
            list = carVGoodsService.queryList(carGoods);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * 模型和视图
         * model模型: 模型对象中存放了返回给页面的数据
         * view视图: 视图对象中指定了返回的页面的位置
         */
        ModelAndView modelAndView = new ModelAndView();
        //将返回给页面的数据放入模型和视图对象中
        modelAndView.addObject("list", list);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/list");
        return modelAndView;
    }

    /**
     * 添加商品至购物车列表
     *
     * @param goodsId  商品ID
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException 异常抛出
     */
    @RequestMapping("/addGoodsToCart")
    public String addGoodsToCart(Long goodsId, HttpServletRequest request,
                                 HttpServletResponse response) throws UnsupportedEncodingException {
        // 从cookie中获取购物车列表
        List<Cart> cartVos = getCartInCookie(response, request);
        Cookie cookie_2st;
        CarGoods carGoods = new CarGoods();
        try {
            CarGoods carGoods1 = new CarGoods();
            carGoods1.setId(Long.parseLong(goodsId + ""));
            List<CarGoods> cList = carVGoodsService.queryList(carGoods1);
            carGoods = cList.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 如果购物车列表为空
        if (cartVos.size() <= 0) {
            //TODO 根据商品ID获取商品信息
            //测试用，实际应当根据id获取
            Cart cartVo = new Cart();
            cartVo.setNum(1);
            cartVo.setPrice(carGoods.getPrice().intValue());
            cartVo.setId(carGoods.getId());
            cartVo.setType(carGoods.getType());
            cartVo.setName(carGoods.getName());
            cartVo.setProduce(carGoods.getProduce());
            cartVo.setDescription(carGoods.getDescription());

            //将当前传来的商品添加到购物车列表
            cartVos.add(cartVo);
            if (getCookie(request) == null) {
                // 制作购物车cookie数据
                cookie_2st = new Cookie("cart", URLEncoder.encode(makeCookieValue(cartVos), "utf-8"));
                // 设置在该项目下都可以访问该cookie
                cookie_2st.setPath("/");
                // 设置cookie有效时间为30分钟
                cookie_2st.setMaxAge(60 * 30);
                cookie_2st.setValue(URLEncoder.encode(makeCookieValue(cartVos)));
                // 添加cookie
                response.addCookie(cookie_2st);
            }
        }
        // 当获取的购物列表不为空时
        else {
            int bj = 0;
            for (Cart cart : cartVos) {
                // 如果购物车中存在，该商品则数量+1
                if (cart.getId().equals(goodsId)) {
                    cart.setNum(cart.getNum() + 1);
                    bj = 1;
                    break;
                }
            }
            if (bj == 0) {
                // TODO 根据商品ID获取商品信息
                // 测试用，实际应当根据id获取
                Cart cartVo = new Cart();
                cartVo.setNum(1);
                cartVo.setPrice(carGoods.getPrice().intValue());
                cartVo.setId(carGoods.getId());
                cartVo.setType(carGoods.getType());
                cartVo.setName(carGoods.getName());
                cartVo.setProduce(carGoods.getProduce());
                cartVo.setDescription(carGoods.getDescription());
                // 将当前传来的商品添加到购物车列表
                cartVos.add(cartVo);
            }
            // 获取名为"cart"的cookie
            cookie_2st = getCookie(request);
            // 设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            // 设置cookie有效时间为30分钟
            cookie_2st.setMaxAge(60 * 30);
            // 设置value
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(cartVos)));
            // 添加cookie
            response.addCookie(cookie_2st);

        }
        return "redirect:/cargoods/getCart";
    }

    /**
     * 获取购物车列表
     *
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException 抛出异常
     */
    @RequestMapping("/getCart")
    public ModelAndView getCart(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        List<Cart> cartInCookie = getCartInCookie(response, request);

        ModelAndView modelAndView = new ModelAndView();
        // 将返回给页面的数据放入模型和视图对象中
        modelAndView.addObject("list", cartInCookie);
        // 指定返回的页面位置
        modelAndView.setViewName("carGoods/carlist");
        return modelAndView;
    }

    /**
     * @Author gzl
     * @Description：查询下单列表
     * @Exception
     * @Date： 2020/4/19 11:59 PM
     */
    @RequestMapping("/queryorders")
    public ModelAndView QueryOrders() {
        List<CarOrders> list = carVGoodsService.queryOrders();
        /**
         * 模型和视图
         * model模型: 模型对象中存放了返回给页面的数据
         * view视图: 视图对象中指定了返回的页面的位置
         */
        ModelAndView modelAndView = new ModelAndView();
        //将返回给页面的数据放入模型和视图对象中
        modelAndView.addObject("list", list);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/orderlist");
        return modelAndView;
    }

    /**
     * @Author gzl
     * @Description：查询下单详情列表
     * @Exception
     * @Date： 2020/4/19 11:59 PM
     */
    @RequestMapping("/queryordersdetails")
    public ModelAndView QueryOrdersDetails(Integer id) {
        List<CarOrderDetails> list = carVGoodsService.queryOrdersDetails(id);
        /**
         * 模型和视图
         * model模型: 模型对象中存放了返回给页面的数据
         * view视图: 视图对象中指定了返回的页面的位置
         */
        ModelAndView modelAndView = new ModelAndView();
        //将返回给页面的数据放入模型和视图对象中
        modelAndView.addObject("list", list);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/orderdetailslist");
        return modelAndView;
    }

    /**
     * 下单
     *
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/addorders")
    public ModelAndView addOrders(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> cartInCookie = getCartInCookie(response, request);
        carVGoodsService.saveOrders(cartInCookie);
        ModelAndView modelAndView = new ModelAndView();
        //将返回的页面数据放入模型和视图对象中
        List<CarGoods> list = new ArrayList<>();
        try {
            CarGoods carGoods = new CarGoods();
            list = carVGoodsService.queryList(carGoods);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("list", list);
        //指定返回的位置页面
        modelAndView.setViewName("carGoods/list");
        return modelAndView;
    }

    /**
     * 清空购物车列表
     *
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/removeCartAll")
    public String removeCartAll(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie cookie = getCookie(request);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/cargoods/getCart";
    }

    /**
     * 通过ID删除购物车中商品
     *
     * @param request
     * @param response
     * @param goodsId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/removeCartById")
    public String removeCartById(HttpServletRequest request, HttpServletResponse response, Long goodsId) throws UnsupportedEncodingException {
        // 从cookie中获取购物车列表
        List<Cart> cartVos = getCartInCookie(response, request);
        Cookie cookie_2st;
        for (Cart cart : cartVos) {
            // 如果购物车中存在，该商品则数量-1
            if (cart.getId().equals(goodsId) && cart.getNum() >= 2) {
                cart.setNum(cart.getNum() - 1);
                break;
            }
            // 如果购物车中不存在直接删除
            if (cart.getId().equals(goodsId) && cart.getNum() == 1) {
                cartVos.remove(cart);
                break;
            }
        }
        // 如果购物车中还存在商品
        if (cartVos.size() != 0) {
            // 获取名为"cart"的cookie
            cookie_2st = getCookie(request);
            // 设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            // 设置cookie有效时间为30分钟
            cookie_2st.setMaxAge(60 * 30);
            // 设置value
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(cartVos)));
            response.addCookie(cookie_2st);
            return "redirect:/cargoods/getCart";
        } else {
            // 如果购物车中不存在商品
            return "redirect:/cargoods/removeCartAll";
        }
    }

    /**
     * 根据供应商拆分订单
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/teardowndetails")
    public ModelAndView teardowndetails(HttpServletRequest request, HttpServletResponse response, Integer orderId)
            throws UnsupportedEncodingException {
        List<CarOrderDetails> list = carVGoodsService.queryOrdersDetails(orderId);
        List<TearDownDetails> tearList = new ArrayList<>();
        //筛选出不同的
        for (CarOrderDetails c : list) {
            TearDownDetails tdd = carVGoodsService.queryOrdersTearDownDetails(c);
            if (tdd != null) {
                tdd.setNum(tdd.getNum() + c.getNum());
                tearList.add(tdd);
            } else {
                TearDownDetails t = new TearDownDetails();
                t.setNum(c.getNum());
                t.setCargoods_name(c.getGoodsname());
                t.setOrderId((long) c.getOrderId());
                t.setProduce(c.getProduce());
                carVGoodsService.saveTearDownDetailsOrders(t);
                tearList.add(t);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", tearList);
        //指定返回的位置页面
        modelAndView.setViewName("carGoods/teardetailslist");
        return modelAndView;
    }

    /**
     * 返回当前订单数据，用于柱状图显示
     *
     * @return
     */
    @GetMapping("/getRepertoryData")
    @ResponseBody
    public JSONResult getRepertoryData() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList = carVGoodsService.queryCartItems();
        List<String> carGoodsNameList = new ArrayList<>();
        List<Integer> carGoodsCountList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            carGoodsNameList.add(cartItem.getCarGoods().getName());
            carGoodsCountList.add(cartItem.getCount());
        }
        List responseData = new ArrayList();
        responseData.add(carGoodsNameList);
        responseData.add(carGoodsCountList);
        return JSONResult.ok(responseData);
    }

    /**
     * 返回当前订单数据，用于饼状图显示
     *
     * @return
     */
    @GetMapping("/getRepertoryData2")
    @ResponseBody
    public JSONResult getRepertoryData2() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList = carVGoodsService.queryCartItems();
        List carGoodsNameAndCountList = new ArrayList<>();
        List<String> carGoodsNameList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            Map<String, Object> repertoryData = new HashMap<>();
            repertoryData.put("name", cartItem.getCarGoods().getName());
            repertoryData.put("value", cartItem.getCount());
            carGoodsNameAndCountList.add(repertoryData);
            carGoodsNameList.add(cartItem.getCarGoods().getName());
        }

        List responseList = new ArrayList();
        responseList.add(carGoodsNameAndCountList);
        responseList.add(carGoodsNameList);
        return JSONResult.ok(responseList);
    }

    /**
     * 获取cookie中的购物车列表
     *
     * @param response
     * @param request
     * @return 购物车列表
     * @throws UnsupportedEncodingException 抛出异常
     */
    public List<Cart> getCartInCookie(HttpServletResponse response, HttpServletRequest request) throws
            UnsupportedEncodingException {
        // 定义空的购物车列表
        List<Cart> items = new ArrayList<>();
        String value_1st;
        // 购物cookie
        Cookie cart_cookie = getCookie(request);
        // 判断cookie是否为空
        if (cart_cookie != null && cart_cookie.getMaxAge() != 0) {
            // 获取cookie中String类型的value,从cookie获取购物车
            value_1st = URLDecoder.decode(cart_cookie.getValue(), "utf-8");
            // 判断value是否为空或者""字符串
            if (value_1st != null && !"".equals(value_1st)) {
                // 解析字符串中的数据为对象并封装至list中返回给上一级
                String[] arr_1st = value_1st.split("==");
                for (String value_2st : arr_1st) {
                    String[] arr_2st = value_2st.split("=");
                    Cart item = new Cart();
                    item.setId(Long.parseLong(arr_2st[0])); //商品id
                    item.setType(arr_2st[1]); //商品类型ID
                    item.setName(arr_2st[2]); //商品名
                    item.setDescription(arr_2st[4]);//商品详情
                    item.setPrice(Integer.parseInt(arr_2st[3])); //商品市场价格
                    item.setNum(Integer.parseInt(arr_2st[5]));//生产商
                    item.setProduce(arr_2st[6]);
                    items.add(item);
                }
            }
        }
        return items;

    }

    /**
     * 获取名为"cart"的cookie
     *
     * @param request
     * @return cookie
     */
    public Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cart_cookie = null;
        for (Cookie cookie : cookies) {
            //获取购物车cookie
            if ("cart".equals(cookie.getName())) {
                cart_cookie = cookie;
            }
        }
        return cart_cookie;
    }

    /**
     * 制作cookie所需value
     *
     * @param cartVos 购物车列表
     * @return 解析为字符串的购物车列表，属性间使用"="相隔，对象间使用"=="相隔
     */
    public String makeCookieValue(List<Cart> cartVos) {
        StringBuffer buffer_2st = new StringBuffer();
        for (Cart item : cartVos) {
            buffer_2st.append(item.getId() + "=" + item.getType() + "=" + item.getName() + "="
                    + item.getPrice() + "=" + item.getDescription() + "=" + item.getNum() + "=" + item.getProduce() + "==");
        }
        return buffer_2st.toString().substring(0, buffer_2st.toString().length() - 2);
    }

}
