package de.hybris.merchandise.storefront.form.validation;

import de.hybris.merchandise.core.model.LoyaltyConfigModel;
import de.hybris.merchandise.loyalty.services.LoyaltyService;
import de.hybris.merchandise.storefront.form.LoyaltyPointsForm;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("loyaltyPointsValidator")
public class LoyaltyPointsValidator implements Validator
{

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private LoyaltyService loyaltyService;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return LoyaltyPointsForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object param, final Errors errors)
	{
		final LoyaltyPointsForm form = (LoyaltyPointsForm) param;

		if (form.getNumberOfPoints() == null)
		{
			return;
		}

		final int numberOfPoints = form.getNumberOfPoints().intValue();

		if (numberOfPoints < 1)
		{
			errors.rejectValue("numberOfPoints", "loyaltyPointsForm.min");
			return;
		}

		final CustomerModel customer = (CustomerModel) userService.getCurrentUser();

		if (customer.getNumberOfPoints() < numberOfPoints)
		{
			errors.rejectValue("numberOfPoints", "loayltyPointsForm.notEnough");
			return;
		}

		checkTresholdExceeded(numberOfPoints, errors);

	}

	private void checkTresholdExceeded(final int param, final Errors errors)
	{
		final LoyaltyConfigModel loyaltyConfig = loyaltyService.getLoyaltyConfig();

		final double total = cartService.getSessionCart().getTotalPrice().doubleValue();
		final double totalShare = loyaltyConfig.getMaxTotalShare();
		final double conversion = loyaltyConfig.getEuroConversion();

		if (total * totalShare < conversion * param)
		{
			// pass argument in a percentage format
			errors.rejectValue("numberOfPoints", "loayltyPointsForm.treshold", new Object[]
			{ Double.valueOf(totalShare * 100) }, null);
		}
	}

}
