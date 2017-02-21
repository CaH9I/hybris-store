package de.hybris.merchandise.loyalty.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.loyalty.daos.LoyaltyConfigDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class LoyaltyDaoIntegrationTest extends ServicelayerTransactionalTest
{

	private static final double chargePerEuro = 10;
	private static final double euroConversion = 0.01;
	private static final double maxTotalShare = 0.2;

	@Resource(name = "loyaltyConfigDao")
	private LoyaltyConfigDao loyaltyDao;

	@Resource
	private ModelService modelService;

	@Test
	public void findLoyaltyConfigs()
	{
		List<LoyaltyConfigModel> loyaltyConfigs = loyaltyDao.findLoyaltyConfigs();
		assertTrue(loyaltyConfigs.isEmpty());

		final LoyaltyConfigModel loyaltyConfig = new LoyaltyConfigModel();
		loyaltyConfig.setChargePerEuro(chargePerEuro);
		loyaltyConfig.setEuroConversion(euroConversion);
		loyaltyConfig.setMaxTotalShare(maxTotalShare);
		modelService.save(loyaltyConfig);

		loyaltyConfigs = loyaltyDao.findLoyaltyConfigs();

		assertEquals(1, loyaltyConfigs.size());
		assertEquals(chargePerEuro, loyaltyConfigs.get(0).getChargePerEuro(), 0);
		assertEquals(euroConversion, loyaltyConfigs.get(0).getEuroConversion(), 0);
		assertEquals(maxTotalShare, loyaltyConfigs.get(0).getMaxTotalShare(), 0);
	}
}
