package de.hybris.merchandise.loyalty.daos.impl;

import static de.hybris.merchandise.core.model.LoyaltyConfigModel._TYPECODE;

import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.loyalty.daos.LoyaltyConfigDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("loyaltyConfigDao")
public class LoyaltyConfigDaoImpl implements LoyaltyConfigDao
{
	private static final String FIND_LOYALTY_CONFIGS = "SELECT {" + LoyaltyConfigModel.PK + "} FROM {" + _TYPECODE + "}";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<LoyaltyConfigModel> findLoyaltyConfigs()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_LOYALTY_CONFIGS);
		return flexibleSearchService.<LoyaltyConfigModel> search(query).getResult();
	}

}
