<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product List</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>	
<a href="${pageContext.request.contextPath}/productList">
     Product List
   </a>
   	<fmt:setLocale value="en_US" scope="session"/>
    <div class="page-title">Shopping Cart</div>
    <c:forEach items="${list}" var="cartInfo">
        <div class="product-preview-container">
         <ul>
			<li><img src="<c:url value="/resources/${cartInfo.product.id}.jpg" />" alt="image" style="width:100%"/></li>
 			<li>ID: ${cartInfo.product.id}</li>
			<li>Name: ${cartInfo.product.name}</li>
			<li>Quantity: <fmt:formatNumber value="${cartInfo.quantity}"/></li>
			<li>Price: <fmt:formatNumber value="${cartInfo.product.price}" type="currency"/></li>
			<li>Total Price: <fmt:formatNumber value="${cartInfo.totalPrice}" type="currency"/></li>
			<li><a
                 href="${pageContext.request.contextPath}/removeFromCart?id=${cartInfo.product.id}">
                                Delete </a></li>
		</ul>
        </div>
    </c:forEach>
    <br/>
</body>
</html>