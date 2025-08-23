package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;

@Deprecated(since = "4.3", forRemoval = false)
public class FlexibleSearchCronJob extends GeneratedFlexibleSearchCronJob
{
    private static final Logger log = Logger.getLogger(FlexibleSearchCronJob.class.getName());


    @ForceJALO(reason = "something else")
    public void setNonInitialAttributes(SessionContext ctx, Item item, Item.ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)nonInitialAttributes);
        if(nonInitialAttributes.get("query") == null)
        {
            myMap.put("query", String.valueOf("SELECT {p:Code} FROM {Product AS p}"));
        }
        if(nonInitialAttributes.get("failOnUnknown") == null)
        {
            myMap.put("failOnUnknown", Boolean.TRUE);
        }
        if(nonInitialAttributes.get("dontNeedTotal") == null)
        {
            myMap.put("dontNeedTotal", Boolean.TRUE);
        }
        if(nonInitialAttributes.get("rangeStart") == null)
        {
            myMap.put("rangeStart", Integer.valueOf(0));
        }
        if(nonInitialAttributes.get("count") == null)
        {
            myMap.put("count", Integer.valueOf(-1));
        }
        super.setNonInitialAttributes(ctx, item, myMap);
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<String> getSearchResult(SessionContext ctx)
    {
        if(getQuery() != null && !getQuery().equals(""))
        {
            return getSession().getFlexibleSearch().search(ctx, getQuery(), Collections.EMPTY_MAP,
                            Collections.singletonList(String.class), isFailOnUnknownAsPrimitive(),
                            isDontNeedTotalAsPrimitive(),
                            getRangeStartAsPrimitive(), getCountAsPrimitive()).getResult();
        }
        return Collections.EMPTY_LIST;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "FlexibleSearchCronJob [ Job: " + ((getJob() != null) ? getJob().getCode() : "null") + "; Query: " + getQuery() + "]";
    }
}
