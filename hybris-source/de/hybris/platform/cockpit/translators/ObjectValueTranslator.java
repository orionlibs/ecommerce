package de.hybris.platform.cockpit.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectValueTranslator extends AbstractValueTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(ObjectValueTranslator.class);


    public String exportValue(Object value) throws JaloInvalidParameterException
    {
        LOG.info("{}", value);
        return null;
    }


    public Object importValue(String valueExpr, Item toItem) throws JaloInvalidParameterException
    {
        if(toItem == null)
        {
            setError();
            return null;
        }
        AbstractDescriptor.DescriptorParams params = getColumnDescriptor().getDescriptorData();
        String classOfValue = params.getModifier("class");
        if(classOfValue == null || classOfValue.contains("String"))
        {
            return valueExpr;
        }
        if(classOfValue.contains("Media"))
        {
            Collection<Media> reports = MediaManager.getInstance().getMediaByCode(valueExpr);
            Media report = null;
            if(reports.isEmpty())
            {
                setError();
            }
            else
            {
                report = reports.iterator().next();
            }
            return report;
        }
        LOG.info("Can't import value of class: " + classOfValue + ". Extend " + getClass().getName() + " and write your own handling.");
        return null;
    }
}
