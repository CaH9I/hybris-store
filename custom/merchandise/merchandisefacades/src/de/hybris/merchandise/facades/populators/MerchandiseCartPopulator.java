package de.hybris.merchandise.facades.populators;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class MerchandiseCartPopulator extends MerchandiseAbstractOrderPopulator implements Populator<CartModel, CartData>
{
	@Override
	public void populate(final CartModel cartModel, final CartData cartData) throws ConversionException
	{
		populateOrder(cartModel, cartData);
	}
}
