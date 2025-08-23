package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerAwareModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerContext;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.CommentModel;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;

public class CommentLayerUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentLayerUtils.class);


    public static String numericSizeToString(double value, UNIT unit)
    {
        String strValue = Double.toString(value);
        return strValue + unit.toString().toLowerCase();
    }


    public static String numericSizeToString(double value)
    {
        return numericSizeToString(value, UNIT.PX);
    }


    public static double getSizeNumberValue(String stringValue, UNIT unit)
    {
        if(!StringUtils.isEmpty(stringValue) && unit != null)
        {
            try
            {
                String unitString = unit.toString().toLowerCase();
                stringValue = stringValue.replaceAll(unitString, "");
                return Double.parseDouble(stringValue);
            }
            catch(NumberFormatException nfe)
            {
                LOG.error("Could not resolve HTML component size", nfe);
                return -1.0D;
            }
        }
        throw new IllegalArgumentException("String value and unit must not be null");
    }


    public static double getSizeNumberValue(String stringValue)
    {
        return getSizeNumberValue(stringValue, UNIT.PX);
    }


    public static void clearEventListeners(Component component, String... eventNames)
    {
        for(String eventName : eventNames)
        {
            clearEventListeners(component, eventName);
        }
    }


    private static void clearEventListeners(Component component, String eventName)
    {
        for(Iterator<EventListener> elIter = component.getListenerIterator(eventName); elIter.hasNext(); )
        {
            component.removeEventListener(eventName, elIter.next());
        }
    }


    public static CommentLayerComponent getOwningCommentLayer(CommentLayerAwareModel model, CommentModel commentItemModel)
    {
        CommentLayerContext context = model.getCommentLayerContext();
        if(context != null)
        {
            for(CommentLayerComponent clComponent : context.getCommentLayerComponents())
            {
                if(clComponent.containsComment(commentItemModel))
                {
                    return clComponent;
                }
            }
        }
        return null;
    }


    public static boolean isValidCommentText(String text)
    {
        if(text == null)
        {
            return false;
        }
        String processedText = text.replaceAll("&nbsp;", "");
        processedText = UITools.removeHtml(processedText);
        return !StringUtils.isEmpty(processedText.trim());
    }
}
