package de.hybris.merchandise.placeorder.impl;

import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class MerchandiseCommercePlaceOrderStrategy extends DefaultCommercePlaceOrderStrategy
{

	@Autowired
	private LoyaltyService loyaltyService;

	@Override
	@Transactional
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CommerceOrderResult result = super.placeOrder(parameter);
		final OrderModel order = result.getOrder();

		// charge loyalty points to the customer
		final CustomerModel customer = (CustomerModel) order.getUser();
		final double pointsPerEuro = loyaltyService.getLoyaltyConfig().getChargePerEuro();
		final int chargedPoints = (int) (Math.ceil(order.getSubtotal().doubleValue() * pointsPerEuro));
		int debittedPoints = 0;
		if (order.getLPPaymentInfo() != null)
		{
			debittedPoints = order.getLPPaymentInfo().getNumberOfPoints();
		}

		customer.setNumberOfPoints(customer.getNumberOfPoints() + chargedPoints - debittedPoints);
		getModelService().save(customer);

		return result;
	}
}
