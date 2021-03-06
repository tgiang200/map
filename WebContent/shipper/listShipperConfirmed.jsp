<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.i1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" type="image/x-icon" href="/Map/img/list.png" />
<link href="/Map/css/cssList.css" rel="stylesheet">
<style type="text/css">

</style>

<title>List shipper</title>
</head>
<body>
<%
	String userType = session.getAttribute("userType").toString();
	pageContext.setAttribute("userType", userType);
%>
<c:choose>
    <c:when test="${userType == 'center'}">
        <%@include file="../include/menu.jsp"%>
    </c:when>
    <c:when test="${userType == 'producer'}">
        <%@include file="../include/menuProducer.jsp"%>
    </c:when>
    <c:when test="${userType == 'shipper'}">
        <%@include file="../include/menuShipper.jsp"%>
    </c:when>
    <c:otherwise>
    	<%@include file="../include/menuShipper.jsp"%>
    </c:otherwise>
</c:choose>
<center>
<script>
	var info = ${listShipper};
	document.write("<div id=\"info\">");
	document.write("<table>");
	document.write("<th class=\"title\" colspan=\"7\"><center><h2>Shipper đang chờ duyệt</h2></center></th>");
	document.write("<h4>${message}</h4>");
	document.write("<tr><th><center>Họ tên</center></th>");
	document.write("<th><center>Địa chỉ</center></th>");
	document.write("<th><center>CMND</center></th>");
	document.write("<th><center>Điện thoại</center></th>");
	document.write("<th><center>Email</center></th>");
	document.write("<th><center>Phương tiện</center></th>");
	document.write("<th><center>Chi tiết</center></th></tr>");
	
	for (var i=0; i<info.length; i++){
			document.write("<tr>");
			document.write("<td>"+info[i].fullname+"</td>");
			document.write("<td>"+info[i].address+"</td>");
			document.write("<td>"+info[i].idCard+"</td>");
			document.write("<td>"+info[i].phone+"</td>");
			document.write("<td>"+info[i].email+"</td>");
			document.write("<td>"+info[i].vehicle+"</td>");
			document.write("<td><a href=\"/Map/shipper/shipperID="+info[i].phone+"\">Lựa chọn</a></td>");
			document.write("</tr>");
	}
	document.write("</table>");
	document.write("</div>");
</script>
<br>
<a href="/Map/shipper/listConfirm.html" style="font-size: 20px">Shipper chờ duyệt</a>
</center>
</body>
</html>