package de.hybris.merchandise.facades.loyalty.impl;

import de.hybris.merchandise.core.model.LPPaymentInfoModel;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.facades.loyalty.LoyaltyFacade;
import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;
import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("loyaltyFacade")
public class LoyaltyFacadeImpl implements LoyaltyFacade
{

	@Autowired
	private LoyaltyService loyaltyService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ModelService modelService;

	@Override
	public void setLPPaymentInfo(final LPPaymentInfoData lpInfoData)
	{
		LPPaymentInfoModel lpInfoModel = null;

		if (lpInfoData != null)
		{
			lpInfoModel = new LPPaymentInfoModel();
			lpInfoModel.setNumberOfPoints(lpInfoData.getNumberOfPoints());
			lpInfoModel.setLoyaltyCost(lpInfoData.getLoyaltyCost().getValue().doubleValue());
		}

		final CartModel cart = cartService.getSessionCart();
		cart.setLPPaymentInfo(lpInfoModel);

		modelService.save(cart);
	}

	@Override
	public double getLoyaltyCost(final int numberOfPoints)
	{
		final LoyaltyConfigModel loyaltyConfig = loyaltyService.getLoyaltyConfig();
		return loyaltyConfig.getEuroConversion() * numberOfPoints;
	}


	@Override
	public void clearLPPaymentInfo()
	{
		setLPPaymentInfo(null);
	}
}
