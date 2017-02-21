package de.hybris.merchandise.facades.populators;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class MerchandiseOrderPopulator extends MerchandiseAbstractOrderPopulator implements Populator<OrderModel, OrderData>
{

	@Override
	public void populate(final OrderModel orderModel, final OrderData orderData) throws ConversionException
	{
		populateOrder(orderModel, orderData);
	}

}
