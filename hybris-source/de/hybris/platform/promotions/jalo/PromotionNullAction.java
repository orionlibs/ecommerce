package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;

public class PromotionNullAction extends GeneratedPromotionNullAction
{
    public boolean apply(SessionContext ctx)
    {
        setMarkedApplied(ctx, true);
        return false;
    }


    public boolean undo(SessionContext ctx)
    {
        setMarkedApplied(ctx, false);
        return false;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        return true;
    }


    public double getValue(SessionContext ctx)
    {
        return 0.0D;
    }
}
