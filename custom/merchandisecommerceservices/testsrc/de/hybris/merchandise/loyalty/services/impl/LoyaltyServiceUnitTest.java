package de.hybris.merchandise.loyalty.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.exception.ModelAmbiguousException;
import de.hybris.merchandise.loyalty.daos.LoyaltyConfigDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LoyaltyServiceUnitTest
{
	private static final double chargePerEuro = 10;
	private static final double euroConversion = 0.01;
	private static final double maxTotalShare = 0.2;

	@InjectMocks
	private LoyaltyServiceImpl loyaltyService;

	@Mock
	private LoyaltyConfigDao loyaltyDao;

	@Test
	public void getLoyaltyConfig()
	{
		final LoyaltyConfigModel config = mock(LoyaltyConfigModel.class);
		when(config.getChargePerEuro()).thenReturn(chargePerEuro);
		when(config.getEuroConversion()).thenReturn(euroConversion);
		when(config.getMaxTotalShare()).thenReturn(maxTotalShare);

		final List configList = mock(List.class);
		when(configList.isEmpty()).thenReturn(false);
		when(configList.size()).thenReturn(1);
		when(configList.get(0)).thenReturn(config);
		when(loyaltyDao.findLoyaltyConfigs()).thenReturn(configList);

		final LoyaltyConfigModel result = loyaltyService.getLoyaltyConfig();

		assertEquals(chargePerEuro, result.getChargePerEuro(), 0);
		assertEquals(euroConversion, result.getEuroConversion(), 0);
		assertEquals(maxTotalShare, result.getMaxTotalShare(), 0);
	}

	@Test(expected = ModelNotFoundException.class)
	public void getLoyaltyConfigWhenNotExists()
	{
		final List configList = mock(List.class);
		when(configList.isEmpty()).thenReturn(true);
		when(loyaltyDao.findLoyaltyConfigs()).thenReturn(configList);

		loyaltyService.getLoyaltyConfig();
	}

	@Test(expected = ModelAmbiguousException.class)
	public void getLoyaltyConfigWhenTwoMore()
	{
		final List configList = mock(List.class);
		when(configList.size()).thenReturn(2);
		when(loyaltyDao.findLoyaltyConfigs()).thenReturn(configList);

		loyaltyService.getLoyaltyConfig();
	}
}
