package de.hybris.merchandise.jalo;

import de.hybris.merchandise.jalo.GeneratedProductQuantityThresholdFreeGiftPromotion;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;


public class ProductQuantityThresholdFreeGiftPromotion extends GeneratedProductQuantityThresholdFreeGiftPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ProductQuantityThresholdFreeGiftPromotion.class.getName());

	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		if (getProducts() != null && !getProducts().isEmpty())
		{
			// the base product is the first of related products
			final Product baseProduct = getProducts().iterator().next();

			final List<PromotionResult> results = new ArrayList<PromotionResult>();

			// Find the eligible products, and apply any restrictions
			final PromotionsManager.RestrictionSetResult restrictResult = findEligibleProductsInBasket(ctx, promoContext);

			// If the restrictions did not reject this promotion, and there are still products allowed after the restrictions
			if (restrictResult.isAllowedToContinue() && !restrictResult.getAllowedProducts().isEmpty())
			{
				final int qualifyingCount = this.getQualifyingCount(ctx).intValue();

				// Create a view of the order containing only the allowed products
				final PromotionOrderView orderView = promoContext.createView(ctx, this, restrictResult.getAllowedProducts());

				// Get the real quantity of the base product in the cart
				final long realQuantity = orderView.getQuantity(ctx, baseProduct);

				if (realQuantity >= qualifyingCount)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("(" + getPK() + ") evaluate: product quantity " + realQuantity + ">" + qualifyingCount
								+ ".  Creating a free gift action.");
					}
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
							promoContext.getOrder(), 1.0F);

					//Apply free gift promotion action
					final Product product = this.getGiftProduct(ctx);
					result.addAction(ctx, PromotionsManager.getInstance().createPromotionOrderAddFreeGiftAction(ctx, product, result));

					results.add(result);
				}
				else
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("(" + getPK() + ") evaluate: product quantity " + realQuantity + ">" + qualifyingCount
								+ ".  Creating a free gift action.");
					}
					final float certainty = (realQuantity / qualifyingCount);
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
							promoContext.getOrder(), certainty);
					results.add(result);
				}
				return results;
			}
		}
		return null;
	}

	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		final Object[] args =
		{ this.getQualifyingCount() };
		if (promotionResult.getFired(ctx))
		{
			return formatMessage(this.getMessageFired(ctx), args, locale);
		}
		else if (promotionResult.getCouldFire(ctx))
		{
			return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
		}
		return "";
	}

}
