package de.hybris.merchandise.facades.populators;

import static de.hybris.platform.commercefacades.product.data.PriceDataType.BUY;

import de.hybris.merchandise.core.model.LPPaymentInfoModel;
import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;


public abstract class MerchandiseAbstractOrderPopulator
{

	@Autowired
	private PriceDataFactory priceDataFactory;

	protected void populateOrder(final AbstractOrderModel orderModel, final AbstractOrderData orderData)
	{
		final LPPaymentInfoModel lpInfoModel = orderModel.getLPPaymentInfo();

		if (lpInfoModel != null)
		{
			final LPPaymentInfoData lpInfoData = new LPPaymentInfoData();
			lpInfoData.setNumberOfPoints(lpInfoModel.getNumberOfPoints());

			final BigDecimal costValue = BigDecimal.valueOf(lpInfoModel.getLoyaltyCost());
			final PriceData priceData = priceDataFactory.create(BUY, costValue, orderModel.getCurrency());

			lpInfoData.setLoyaltyCost(priceData);

			orderData.setLPPaymentInfo(lpInfoData);
		}
	}
}
