<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="orderBoxes clearfix">
    <order:deliveryAddressItem order="${orderData}"/>
    <order:deliveryMethodItem order="${orderData}"/>
    <div class="orderBox billing">
        <order:billingAddressItem order="${orderData}"/>
    </div>
    <c:if test="${not empty orderData.paymentInfo}">
        <div class="orderBox payment">
            <order:paymentDetailsItem order="${orderData}"/>
        </div>
    </c:if>
    <c:if test="${not empty orderData.LPPaymentInfo}">
        <div class="orderBox">
            <div class="headline"><spring:theme code="checkout.summary.loyaltyPoints.header"/></div>
            <ul>
                <li><spring:theme code="checkout.summary.loyaltyPoints.numberOfPoints"/>: 
                    ${orderData.LPPaymentInfo.numberOfPoints}
                </li>
                <li><spring:theme code="checkout.summary.loyaltyPoints.loyaltyCost"/>:
                    ${orderData.LPPaymentInfo.loyaltyCost.formattedValue}
                </li>
            </ul>
        </div>
    </c:if>
</div>