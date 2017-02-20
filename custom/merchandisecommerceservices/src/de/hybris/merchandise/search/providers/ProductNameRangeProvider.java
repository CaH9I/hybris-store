/**
 *
 */
package de.hybris.merchandise.search.providers;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author User
 *
 */
public class ProductNameRangeProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;

	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			String productName = product.getName();
			if (productName.startsWith("hybris "))
			{
				productName = productName.substring(7);
			}
			final List<String> rangeNameList = rangeNameProvider.getRangeNameList(indexedProperty,
					new Integer(Character.toUpperCase(productName.charAt(0))));
			final String rangeName = !rangeNameList.isEmpty() ? rangeNameList.get(0) : "";

			final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, rangeName));
			}

			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate rating of non-product item");
		}
	}

}
