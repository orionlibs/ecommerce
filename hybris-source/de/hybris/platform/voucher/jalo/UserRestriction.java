package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class UserRestriction extends GeneratedUserRestriction
{
    private static final Logger LOG = Logger.getLogger(UserRestriction.class.getName());


    protected String[] getMessageAttributeValues()
    {
        StringBuilder logins = new StringBuilder();
        for(Iterator<Principal> iterator = getUsers().iterator(); iterator.hasNext(); )
        {
            Principal nextPrincipal = iterator.next();
            if(nextPrincipal instanceof User)
            {
                logins.append(((User)nextPrincipal).getLogin());
            }
            if(nextPrincipal instanceof UserGroup)
            {
                logins.append(((UserGroup)nextPrincipal).getUID());
            }
            if(iterator.hasNext())
            {
                logins.append(", ");
            }
        }
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), logins.toString()};
    }


    public Collection getUsers(SessionContext ctx)
    {
        Collection principals = super.getUsers(ctx);
        return (principals != null) ? principals : Collections.emptyList();
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        if(anOrder == null)
        {
            return false;
        }
        if(isPositiveAsPrimitive())
        {
            return isPartOfConfiguredPrincipals(anOrder.getUser());
        }
        return !isPartOfConfiguredPrincipals(anOrder.getUser());
    }


    protected boolean isPartOfConfiguredPrincipals(User user)
    {
        for(Iterator<Principal> iterator = getUsers().iterator(); iterator.hasNext(); )
        {
            Principal nextPrincipal = iterator.next();
            if((nextPrincipal instanceof User && nextPrincipal.getPK().equals(user.getPK())) || (nextPrincipal instanceof UserGroup && ((UserGroup)nextPrincipal)
                            .containsMember((Principal)user)))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
