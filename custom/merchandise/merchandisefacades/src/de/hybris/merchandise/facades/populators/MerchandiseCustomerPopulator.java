package de.hybris.merchandise.facades.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class MerchandiseCustomerPopulator implements Populator<CustomerModel, CustomerData>
{
	@Override
	public void populate(final CustomerModel customer, final CustomerData data) throws ConversionException
	{
		data.setNumberOfPoints(customer.getNumberOfPoints());
	}
}
