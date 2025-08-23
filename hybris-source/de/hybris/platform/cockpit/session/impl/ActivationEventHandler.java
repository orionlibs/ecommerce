package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivationEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ActivationEventHandler.class);
    public static final String ITEM_KEY = "item";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective == null)
        {
            LOG.warn("Can not activate item since no perspective has been specified.");
        }
        else if(StringUtils.isBlank(getParameter(params, "item")))
        {
            LOG.warn("Can not activate item since no item has been specified.");
        }
        else
        {
            String itemParam = getParameter(params, "item");
            TypedObject item = null;
            try
            {
                item = UISessionUtils.getCurrentSession().getTypeService().wrapItem(PK.parse(itemParam));
            }
            catch(IllegalArgumentException iae)
            {
                LOG.warn("Can not activate item. Reason: No valid item specified.", iae);
            }
            catch(Exception e)
            {
                LOG.error("An error occurred while retrieving item.", e);
            }
            if(item != null)
            {
                perspective.activateItemInEditor(item);
            }
        }
    }
}
