package de.hybris.merchandise.facades.loyalty.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.merchandise.core.model.LPPaymentInfoModel;
import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.facades.loyalty.LoyaltyFacade;
import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;
import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LoyaltyFacadeUnitTest
{

	private static final int numberOfPoints = 100;
	private static final double loyaltyCost = 20;

	@InjectMocks
	private LoyaltyFacadeImpl loyaltyFacade;

	@Mock
	private LoyaltyService loyaltyService;

	@Mock
	private CartService cartService;

	@Mock
	private ModelService modelService;

	@Test
	public void getLoyaltyCost()
	{
		final LoyaltyConfigModel config = mock(LoyaltyConfigModel.class);
		when(config.getEuroConversion()).thenReturn(0.01);

		when(loyaltyService.getLoyaltyConfig()).thenReturn(config);

		final double loyaltyCost = loyaltyFacade.getLoyaltyCost(numberOfPoints);

		assertEquals(numberOfPoints * config.getEuroConversion(), loyaltyCost, 0);
	}

	@Test
	public void setLPPaymentInfo()
	{
		final PriceData priceData = mock(PriceData.class);
		when(priceData.getValue()).thenReturn(BigDecimal.valueOf(loyaltyCost));

		final LPPaymentInfoData lpData = mock(LPPaymentInfoData.class);
		when(lpData.getNumberOfPoints()).thenReturn(numberOfPoints);
		when(lpData.getLoyaltyCost()).thenReturn(priceData);

		when(cartService.getSessionCart()).thenReturn(new CartModel());

		loyaltyFacade.setLPPaymentInfo(lpData);

		verify(modelService).save(argThat(new ArgumentMatcher<CartModel>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				final CartModel cart = (CartModel) argument;
				final LPPaymentInfoModel lpModel = cart.getLPPaymentInfo();
				return (lpModel.getLoyaltyCost() == loyaltyCost) && (lpModel.getNumberOfPoints() == numberOfPoints);
			}
		}));
	}

	@Test
	public void clearLPPaymentInfo()
	{
		when(cartService.getSessionCart()).thenReturn(mock(CartModel.class));

		final LoyaltyFacade spyFacade = spy(loyaltyFacade);

		spyFacade.clearLPPaymentInfo();

		verify(spyFacade).setLPPaymentInfo(isNull(LPPaymentInfoData.class));
	}
}
