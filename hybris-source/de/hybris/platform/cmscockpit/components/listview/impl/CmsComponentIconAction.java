package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class CmsComponentIconAction extends AbstractListViewAction
{
    private static final String CONTENT_ELEMENT_CONFIG = "contentElement";
    private UIConfigurationService uiConfigurationService = null;
    private TypeService cockpitTypeService = null;


    public boolean isAlwaysEnabled()
    {
        return true;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        String ret = null;
        ObjectTemplate objectTemplate = getCockpitTypeService().getBestTemplate(context.getItem());
        BaseType type = context.getItem().getType();
        Map<ObjectType, ContentElementConfiguration> contentElements = getContentElementConfiguration(objectTemplate).getContentElements();
        ContentElementConfiguration contentElementConfiguration = null;
        if(MapUtils.isNotEmpty(contentElements))
        {
            contentElementConfiguration = contentElements.get(type);
        }
        if(contentElementConfiguration == null)
        {
            for(ObjectTemplate template : context.getItem().getAssignedTemplates())
            {
                contentElements = getContentElementConfiguration(template).getContentElements();
                contentElementConfiguration = contentElements.get(template);
                if(contentElementConfiguration != null)
                {
                    break;
                }
            }
        }
        if(contentElementConfiguration != null)
        {
            ret = contentElementConfiguration.getImageSmall();
        }
        if(StringUtils.isBlank(ret))
        {
            ret = "/cmscockpit/images/ContentElementOtherSmall.gif";
        }
        return ret;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return null;
    }


    protected ContentElementListConfiguration getContentElementConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentElementListConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "contentElement", ContentElementListConfiguration.class);
    }


    protected ContentElementListConfiguration getContentElementConfiguration()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
        return getContentElementConfiguration(objectTemplate);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected TypeService getCockpitTypeService()
    {
        if(this.cockpitTypeService == null)
        {
            this.cockpitTypeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.cockpitTypeService;
    }
}
