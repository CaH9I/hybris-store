package de.hybris.merchandise.facades.loyalty.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.merchandise.core.model.LPPaymentInfoModel;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.facades.loyalty.LoyaltyFacade;
import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class LoyaltyFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	private static final int numberOfPoints = 100;
	private static final double loyaltyCost = 20;

	@Resource
	private LoyaltyFacade loyaltyFacade;

	@Resource
	private ModelService modelService;

	@Resource
	private CartService cartService;

	@Test
	public void getLoyaltyCost()
	{
		final LoyaltyConfigModel loyaltyConfig = new LoyaltyConfigModel();
		loyaltyConfig.setEuroConversion(0.01);
		modelService.save(loyaltyConfig);

		final double loyaltyCost = loyaltyFacade.getLoyaltyCost(numberOfPoints);
		assertEquals(numberOfPoints * loyaltyConfig.getEuroConversion(), loyaltyCost, 0);
	}

	@Test
	public void clearLPPaymentInfo()
	{
		final CartModel cart = cartService.getSessionCart();
		cart.setLPPaymentInfo(new LPPaymentInfoModel());
		modelService.save(cart);

		loyaltyFacade.clearLPPaymentInfo();

		assertNull(cartService.getSessionCart().getLPPaymentInfo());
	}

	@Test
	public void setLPPaymentInfo()
	{
		final LPPaymentInfoData lpData = new LPPaymentInfoData();
		lpData.setNumberOfPoints(numberOfPoints);
		final PriceData priceData = new PriceData();
		priceData.setValue(BigDecimal.valueOf(loyaltyCost));
		lpData.setLoyaltyCost(priceData);

		loyaltyFacade.setLPPaymentInfo(lpData);

		assertEquals(numberOfPoints, cartService.getSessionCart().getLPPaymentInfo().getNumberOfPoints());
		assertEquals(loyaltyCost, cartService.getSessionCart().getLPPaymentInfo().getLoyaltyCost(), 0);
	}
}
