package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.SelectIndexesEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskEventHandler.class);
    private final List<String> attachmentTypes = new ArrayList<>();
    public static final String ITEM_KEY = "item";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective == null)
        {
            LOG.warn("Request event ignored. Reason: No perspective available.");
        }
        else
        {
            TypedObject item = null;
            if(!StringUtils.isBlank(getParameter(params, "item")))
            {
                String itemParam = getParameter(params, "item");
                try
                {
                    item = UISessionUtils.getCurrentSession().getTypeService().wrapItem(PK.parse(itemParam));
                }
                catch(IllegalArgumentException iae)
                {
                    LOG.warn("Can not workflow action item. Reason: No valid item specified.", iae);
                }
                catch(Exception e)
                {
                    LOG.error("An error occurred while retrieving item.", e);
                }
            }
            openTaskBrowser(perspective.getBrowserArea(), item);
        }
    }


    private void openTaskBrowser(UIBrowserArea browserArea, TypedObject item)
    {
        TaskBrowserModel taskBrowser = null;
        TaskBrowserModel oldTaskBrowser = null;
        List<BrowserModel> browsers = browserArea.getBrowsers();
        for(BrowserModel browser : browsers)
        {
            if(browser instanceof TaskBrowserModel)
            {
                oldTaskBrowser = (TaskBrowserModel)browser;
                break;
            }
        }
        taskBrowser = new TaskBrowserModel(getAttachmentTypes());
        if(oldTaskBrowser == null)
        {
            browserArea.addVisibleBrowser(0, (BrowserModel)taskBrowser);
        }
        else
        {
            browserArea.replaceBrowser((BrowserModel)oldTaskBrowser, (BrowserModel)taskBrowser);
        }
        taskBrowser.updateItems();
        if(item != null && taskBrowser.getItems() != null)
        {
            for(TypedObject typedObject : taskBrowser.getItems())
            {
                if(typedObject.equals(item))
                {
                    int index = taskBrowser.getItems().indexOf(typedObject);
                    UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new SelectIndexesEvent(this,
                                    Collections.singletonList(Integer.valueOf(index)), TaskBrowserModel.class));
                    break;
                }
            }
        }
    }


    public List<String> getAttachmentTypes()
    {
        return Collections.unmodifiableList(this.attachmentTypes);
    }


    public void setAttachmentTypes(List<String> attachmentTypes)
    {
        this.attachmentTypes.clear();
        if(attachmentTypes != null && !attachmentTypes.isEmpty())
        {
            this.attachmentTypes.addAll(attachmentTypes);
        }
    }
}
