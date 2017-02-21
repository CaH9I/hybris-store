package de.hybris.merchandise.loyalty.daos;

import de.hybris.merchandise.core.model.LoyaltyConfigModel;

import java.util.List;


public interface LoyaltyConfigDao
{
	List<LoyaltyConfigModel> findLoyaltyConfigs();
}
