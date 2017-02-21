package de.hybris.merchandise.order.impl;

import static java.math.BigDecimal.valueOf;

import de.hybris.merchandise.core.model.LPPaymentInfoModel;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePaymentAuthorizationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;


public class MerchandiseCommercePaymentAuthorizationStrategy extends DefaultCommercePaymentAuthorizationStrategy
{
	@Override
	public PaymentTransactionEntryModel authorizePaymentAmount(final CommerceCheckoutParameter parameter)
	{
		final LPPaymentInfoModel lpInfo = parameter.getCart().getLPPaymentInfo();
		if (lpInfo != null)
		{
			parameter.setAuthorizationAmount(parameter.getAuthorizationAmount().add(valueOf(-lpInfo.getLoyaltyCost())));
		}
		return super.authorizePaymentAmount(parameter);
	}
}
