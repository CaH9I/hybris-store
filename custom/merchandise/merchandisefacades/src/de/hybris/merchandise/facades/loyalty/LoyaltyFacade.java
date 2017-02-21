package de.hybris.merchandise.facades.loyalty;

import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;


public interface LoyaltyFacade
{
	void setLPPaymentInfo(LPPaymentInfoData lpInfo);

	double getLoyaltyCost(int numberOfPoints);

	void clearLPPaymentInfo();
}
