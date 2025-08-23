package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class JobLog extends GeneratedJobLog
{
    private static final Logger log = Logger.getLogger(JobLog.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("level", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("cronJob", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new JobLog", 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    protected void removeLinks()
    {
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getShortMessage(SessionContext ctx)
    {
        String message = getMessage(ctx);
        if(message != null && message.length() > 80)
        {
            message = message.substring(0, 79);
        }
        return message;
    }
}
