package de.hybris.merchandise.storefront.controllers.pages.checkout.steps;

import static de.hybris.merchandise.storefront.controllers.ControllerConstants.Views.Pages.MultiStepCheckout.LoyaltyPointsPage;
import static de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants.BREADCRUMBS_KEY;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import de.hybris.merchandise.facades.loyalty.LoyaltyFacade;
import de.hybris.merchandise.facades.order.data.LPPaymentInfoData;
import de.hybris.merchandise.storefront.form.LoyaltyPointsForm;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/checkout/multi/loyalty-points")
public class LoyaltyPointsCheckoutStepController extends AbstractCheckoutStepController
{

	public final static String LOYALTY_POINTS = "loyalty-points";

	@Autowired
	private Validator loyaltyPointsValidator;

	@Autowired
	private LoyaltyFacade loyaltyFacade;

	@Override
	@RequireHardLogIn
	@RequestMapping(method = GET)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		prepareDataForPage(model);
		model.addAttribute("loyaltyPointsForm", new LoyaltyPointsForm());
		return LoyaltyPointsPage;
	}

	@RequireHardLogIn
	@RequestMapping(method = POST)
	public String addPoints(@ModelAttribute final LoyaltyPointsForm loyaltyPointsForm, final Errors errors, final Model model)
			throws CMSItemNotFoundException
	{
		loyaltyPointsValidator.validate(loyaltyPointsForm, errors);

		if (errors.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "loylatyPointsForm.error");
			prepareDataForPage(model);
			return LoyaltyPointsPage;
		}

		LPPaymentInfoData lpPaymentInfo = null;
		final Integer numberOfPoints = loyaltyPointsForm.getNumberOfPoints();

		if (numberOfPoints != null)
		{
			lpPaymentInfo = new LPPaymentInfoData();
			lpPaymentInfo.setNumberOfPoints(numberOfPoints.intValue());

			final double costValue = loyaltyFacade.getLoyaltyCost(numberOfPoints.intValue());
			final PriceData price = new PriceData();
			price.setValue(BigDecimal.valueOf(costValue));

			lpPaymentInfo.setLoyaltyCost(price);
		}

		loyaltyFacade.setLPPaymentInfo(lpPaymentInfo);

		return getCheckoutStep().nextStep();
	}

	@Override
	@RequireHardLogIn
	@RequestMapping(value = "/back", method = GET)
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@Override
	@RequireHardLogIn
	@RequestMapping(value = "/next", method = GET)
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(LOYALTY_POINTS);
	}

	@Override
	protected void prepareDataForPage(final Model model) throws CMSItemNotFoundException
	{
		super.prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute("cartData", getCheckoutFacade().getCheckoutCart());
		model.addAttribute(BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		setCheckoutStepLinksForModel(model, getCheckoutStep());
	}
}
