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
	<a href="${pageContext.request.contextPath}/shoppingCart">
      My Cart
   </a>
    <fmt:setLocale value="en_US" scope="session"/>
    <div class="page-title">Product List</div>
    <c:forEach items="${list}" var="prodInfo">
        <div class="product-preview-container">
         <ul>
 			<li>ID: ${prodInfo.id}</li>
			<li>Name: ${prodInfo.name}</li>
			<li>Price: <fmt:formatNumber value="${prodInfo.price}" type="currency"/></li>
 			 <li><a 
  				href="${pageContext.request.contextPath}/addToCart?id=${prodInfo.id}">Add To Cart</a></li>
 		</ul>
        </div>
    </c:forEach>
    <br/>
</body>
</html> 