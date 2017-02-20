package de.hybris.merchandise.servicelayer.interceptor;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor that prepares {@link CustomerModel} instance before saving.<br>
 * It checks if customer is internal and adds it to hybrids group if he was not a member yet. If the customer is not
 * internal, it removes him from hybrids members.
 *
 */
public class HybridsCustomersPrepareInterceptor implements PrepareInterceptor
{

	private static final String HYBRIDS = "hybrids";
	private UserGroupDao userGroupDao;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) model;
			final UserGroupModel hybrids = userGroupDao.findUserGroupByUid(HYBRIDS);

			if (Boolean.TRUE.equals(customer.getIsInternal()))
			{
				if (!customer.getGroups().contains(hybrids))
				{
					final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(customer.getGroups());
					newGroups.add(hybrids);
					customer.setGroups(newGroups);
				}
			}
			else
			{
				if (customer.getGroups().contains(hybrids))
				{
					final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(customer.getGroups());
					newGroups.remove(hybrids);
					customer.setGroups(newGroups);
				}
			}
		}
	}

	@Required
	public void setUserGroupDao(final UserGroupDao userGroupDao)
	{
		this.userGroupDao = userGroupDao;
	}
}