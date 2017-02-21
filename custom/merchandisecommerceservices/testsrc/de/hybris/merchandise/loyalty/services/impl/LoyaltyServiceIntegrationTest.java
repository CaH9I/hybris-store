package de.hybris.merchandise.loyalty.services.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class LoyaltyServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final double chargePerEuro = 10;
	private static final double euroConversion = 0.01;
	private static final double maxTotalShare = 0.2;

	@Resource
	private LoyaltyService loyaltyService;

	@Resource
	private ModelService modelService;

	@Test
	public void getLoyaltyConfig()
	{
		final LoyaltyConfigModel loyaltyConfig = new LoyaltyConfigModel();
		loyaltyConfig.setChargePerEuro(chargePerEuro);
		loyaltyConfig.setEuroConversion(euroConversion);
		loyaltyConfig.setMaxTotalShare(maxTotalShare);
		modelService.save(loyaltyConfig);

		final LoyaltyConfigModel result = loyaltyService.getLoyaltyConfig();

		assertEquals(chargePerEuro, result.getChargePerEuro(), 0);
		assertEquals(euroConversion, result.getEuroConversion(), 0);
		assertEquals(maxTotalShare, result.getMaxTotalShare(), 0);
	}
}
