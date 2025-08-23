package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Collections;

@Deprecated(since = "ages", forRemoval = false)
public class CleanUpCronJob extends GeneratedCleanUpCronJob
{
    @Deprecated(since = "ages", forRemoval = false)
    public static Integer X_DAYS_OLD_DEFAULTVALUE = Integer.valueOf(14);


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Integer xDaysOld = (Integer)allAttributes.get("xDaysOld");
        if(xDaysOld == null || xDaysOld.intValue() < 0)
        {
            allAttributes.put("xDaysOld", X_DAYS_OLD_DEFAULTVALUE);
        }
        Collection<EnumerationValue> statuscoll = (Collection<EnumerationValue>)allAttributes.get("statuscoll");
        Collection<EnumerationValue> resultcoll = (Collection<EnumerationValue>)allAttributes.get("resultcoll");
        if(statuscoll == null || statuscoll.isEmpty())
        {
            allAttributes.put("statuscoll", Collections.singleton(getFinishedStatus()));
        }
        if(resultcoll == null || resultcoll.isEmpty())
        {
            allAttributes.put("resultcoll", Collections.singleton(getSuccessResult()));
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void setXDaysOld(SessionContext ctx, Integer value)
    {
        if(value == null || value.intValue() <= 0)
        {
            super.setXDaysOld(ctx, X_DAYS_OLD_DEFAULTVALUE);
        }
        else
        {
            super.setXDaysOld(ctx, value);
        }
    }
}
