<%@ page import="com.dongtech.vo.CarGoods" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dongtech.vo.Cart" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/list.css">
    <title>维护图书</title>
</head>
<body>
<div class="w">
    <header>
        <a href="${pageContext.request.contextPath }/cargoods/addorders" >
            <input type="button" οnclick="javascrtpt:window.location.href='${pageContext.request.contextPath}/cargoods/addorders'" value="下单" class="btn">
        </a>
        <a href="${pageContext.request.contextPath }/cargoods/removeCartAll" >
            <input type="button" οnclick="javascrtpt:window.location.href='${pageContext.request.contextPath}/cargoods/removeCartAll'" value="清空" class="btn">
        </a>
        <%--<a href="${pageContext.request.contextPath }/cargoods/deleteAllCookie" >--%>
            <%--<input type="button" οnclick="javascrtpt:window.location.href='${pageContext.request.contextPath}/cargoods/deleteAllCookie'" value="清空购物车" class="btn">--%>
        <%--</a>--%>
    </header>
    <div class="list">
        <div class="list-bd">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <th width="5%"></th>
                    <th width="5%">ID</th>
                    <th width="18%">名称</th>
                    <th width="5%">价格</th>
                    <th width="10%">描述</th>
                    <th width="10%">数量</th>
                    <th width="10%">删除</th>
                </tr>
                <%
                    List<Cart> cartList = (List<Cart>) request.getAttribute("list");
                    if (cartList != null) {
                        for (Cart cart : cartList) {
                            out.write("<tr>");
                            out.write("<td>"+ cart.getId()+"</td>");
                            out.write("<td>" + cart.getName() + "</td>");
                            out.write("<td>" + cart.getPrice() + "</td>");
                            out.write("<td>" + cart.getDescription() + "</td>");
                            out.write("<td>" + cart.getNum() + "</td>");
                            out.write("<td> <a href=\"/cargoods/removeCartById?goodsId="+cart.getId()+"\" > <input type=\"button\" οnclick=\"javascrtpt:window.location.href='/cargoods/removeCartById?goodsId="+cart.getId() + "'\" value=\"删除\" class=\"btn\"></td> ");
                            out.write("</tr>");
                        }
                    }
                %>

            </table>
        </div>
    </div>
</div>
</body>
</html>