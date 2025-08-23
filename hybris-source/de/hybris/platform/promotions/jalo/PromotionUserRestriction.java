package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class PromotionUserRestriction extends GeneratedPromotionUserRestriction
{
    private static final Logger LOG = Logger.getLogger(PromotionUserRestriction.class);


    public AbstractPromotionRestriction.RestrictionResult evaluate(SessionContext ctx, Collection<Product> products, Date date, AbstractOrder order)
    {
        User user;
        if(order != null)
        {
            user = order.getUser(ctx);
        }
        else
        {
            user = ctx.getUser();
        }
        boolean positive = isPositiveAsPrimitive(ctx);
        if(user != null && isInUserCollection(ctx, user))
        {
            return positive ? AbstractPromotionRestriction.RestrictionResult.ALLOW : AbstractPromotionRestriction.RestrictionResult.DENY;
        }
        return positive ? AbstractPromotionRestriction.RestrictionResult.DENY : AbstractPromotionRestriction.RestrictionResult.ALLOW;
    }


    protected boolean isInUserCollection(SessionContext ctx, User user)
    {
        Collection<Principal> restrictedUsers = getUsers(ctx);
        for(Principal p : restrictedUsers)
        {
            if(isInUserCollectionRecursive(ctx, p, user))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean isInUserCollectionRecursive(SessionContext ctx, Principal principal, User user)
    {
        if(principal instanceof PrincipalGroup)
        {
            PrincipalGroup principalGroup = (PrincipalGroup)principal;
            Collection<PrincipalGroup> superGroups = user.getGroups(ctx);
            return checkGroupForPrincipal(ctx, principalGroup, superGroups);
        }
        return principal.equals(user);
    }


    protected boolean checkGroupForPrincipal(SessionContext ctx, PrincipalGroup restrictedGroup, Collection<PrincipalGroup> groups)
    {
        if(groups.contains(restrictedGroup))
        {
            return true;
        }
        for(PrincipalGroup _group : groups)
        {
            if(checkGroupForPrincipal(ctx, restrictedGroup, _group.getGroups(ctx)))
            {
                return true;
            }
        }
        return false;
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(isPositiveAsPrimitive(ctx)).append('|');
        Collection<Principal> users = getUsers(ctx);
        if(users != null && !users.isEmpty())
        {
            for(Principal p : users)
            {
                builder.append(p.getUID()).append(',');
            }
        }
        builder.append('|');
    }


    protected Object[] getDescriptionPatternArguments(SessionContext ctx)
    {
        return new Object[] {getRestrictionType(ctx), Integer.valueOf(isPositiveAsPrimitive(ctx) ? 1 : 0), getUserNames(ctx)};
    }


    public String getUserNames(SessionContext ctx)
    {
        StringBuilder userNames = new StringBuilder();
        Collection<Principal> users = getUsers(ctx);
        if(users != null && !users.isEmpty())
        {
            for(Iterator<Principal> iterator = users.iterator(); iterator.hasNext(); )
            {
                Principal principal = iterator.next();
                if(principal instanceof User)
                {
                    userNames.append(((User)principal).getLogin(ctx));
                }
                else if(principal instanceof UserGroup)
                {
                    userNames.append(((UserGroup)principal).getUID());
                }
                if(iterator.hasNext())
                {
                    userNames.append(", ");
                }
            }
        }
        return userNames.toString();
    }
}
