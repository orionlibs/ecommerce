package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;

public class MediaProcessCronJob extends GeneratedMediaProcessCronJob
{
    private static final Logger log = Logger.getLogger(MediaProcessCronJob.class.getName());


    @ForceJALO(reason = "something else")
    public void setNonInitialAttributes(SessionContext ctx, Item item, Item.ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap attr = new Item.ItemAttributeMap((Map)nonInitialAttributes);
        if(attr.get("jobMedia") == null)
        {
            attr.put("jobMedia", item.getSession().getTypeManager().getComposedType(JobMedia.class).newInstance(
                            Collections.singletonMap("code", "generated job media " + item.getPK().getLongValueAsString())));
        }
        super.setNonInitialAttributes(ctx, item, attr);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "MediaProcessCronJob [ Job: " + ((getJob() != null) ? getJob().getCode() : "null") + "; CurrentLine: " +
                        getCurrentLineAsPrimitive() + "; LastSuccessfulLine: " + getLastSuccessfulLineAsPrimitive() + "]";
    }
}
