package de.hybris.merchandise.loyalty.services.impl;

import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.exception.ModelAmbiguousException;
import de.hybris.merchandise.loyalty.daos.LoyaltyConfigDao;
import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("loyaltyService")
public class LoyaltyServiceImpl implements LoyaltyService
{

	@Autowired
	private LoyaltyConfigDao loyaltyDao;

	@Override
	public LoyaltyConfigModel getLoyaltyConfig()
	{
		final List<LoyaltyConfigModel> result = loyaltyDao.findLoyaltyConfigs();

		if (result.isEmpty())
		{
			throw new ModelNotFoundException("No loyalty config was found.");
		}
		else if (result.size() > 1)
		{
			throw new ModelAmbiguousException("Cannot exaclty determine loyalty config. Found " + result.size() + " results.");
		}

		return result.get(0);
	}

}
