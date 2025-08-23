package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Map;
import org.apache.log4j.Logger;

@Deprecated(since = "4.3", forRemoval = false)
public class URLCronJob extends GeneratedURLCronJob
{
    private static final Logger log = Logger.getLogger(URLCronJob.class.getName());


    @ForceJALO(reason = "something else")
    public void setNonInitialAttributes(SessionContext ctx, Item item, Item.ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap attr = new Item.ItemAttributeMap((Map)nonInitialAttributes);
        if(attr.get("URL") == null)
        {
            attr.put("URL", "");
        }
        super.setNonInitialAttributes(ctx, item, attr);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "URLCronJob [Job: " + ((getJob() != null) ? getJob().getCode() : "null") + "; URL: " + getURL() + "]";
    }
}
