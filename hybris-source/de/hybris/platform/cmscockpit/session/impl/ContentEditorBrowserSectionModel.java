package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultBrowserSectionModel;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.util.Config;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class ContentEditorBrowserSectionModel extends DefaultBrowserSectionModel implements ViewStatePersistenceProvider
{
    private static final Logger LOG = Logger.getLogger(ContentEditorBrowserSectionModel.class);
    private ObjectValueContainer valueContainer = null;
    private HtmlBasedComponent preLabel = null;
    private Boolean readOnly = null;
    private Boolean previousReadOnly = null;
    private boolean enabled = true;
    Map<Object, Map<String, Object>> viewStates;


    public ContentEditorBrowserSectionModel(SectionBrowserModel browserModel)
    {
        this(browserModel, "");
    }


    public ContentEditorBrowserSectionModel(SectionBrowserModel browserModel, String label)
    {
        this(browserModel, label, null);
    }


    public ContentEditorBrowserSectionModel(SectionBrowserModel browserModel, String label, Object rootItem)
    {
        super(browserModel, label, rootItem);
        this.viewStates = new HashMap<>();
        this.enabled = Config.getBoolean("cmscockpit.contenteditor.enabled", true);
    }


    public boolean isEnabled()
    {
        return this.enabled;
    }


    public void setRootItem(Object rootItem)
    {
        super.setRootItem(rootItem);
        if(rootItem != null)
        {
            this.valueContainer = null;
        }
    }


    public ObjectValueContainer getValueContainer()
    {
        if(getRootItem() instanceof TypedObject)
        {
            TypedObject item = (TypedObject)getRootItem();
            this.valueContainer = TypeTools.createValueContainer(item, item.getType().getPropertyDescriptors(), UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
        }
        else
        {
            LOG.warn("No value container and no valid root item available.");
        }
        return this.valueContainer;
    }


    protected void fireEvent(SectionModelEvent event)
    {
        if("itemsChanged".equals(event.getName()) || "changed".equals(event.getName()))
        {
            super.fireEvent(event);
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[((ItemChangedEvent)event).getChangeType().ordinal()])
            {
                case 1:
                    setModified(Boolean.TRUE.booleanValue());
                    break;
                case 2:
                    if(getRootItem() != null && getRootItem().equals(((ItemChangedEvent)event).getItem()))
                    {
                        setRootItem(null);
                        setModified(Boolean.TRUE.booleanValue());
                    }
                    break;
            }
        }
    }


    public HtmlBasedComponent getPreLabel()
    {
        return this.preLabel;
    }


    public void setPreLabel(HtmlBasedComponent preLabel)
    {
        this.preLabel = preLabel;
    }


    public boolean hasReadOnlyChanged()
    {
        if(this.previousReadOnly != null)
        {
            return !this.previousReadOnly.equals(this.readOnly);
        }
        return false;
    }


    public boolean isReadOnly()
    {
        return BooleanUtils.toBoolean(this.readOnly);
    }


    public void setReadOnly(boolean readOnly)
    {
        this.previousReadOnly = this.readOnly;
        this.readOnly = BooleanUtils.toBooleanObject(readOnly);
    }


    public void clearViewStateForComponent(ViewStatePersistenceProvider.ViewStatePersisteable viewComponent)
    {
        Object viewId = viewComponent.getViewComponentId();
        if(this.viewStates.containsKey(viewId))
        {
            this.viewStates.remove(viewId);
        }
    }


    public Map<String, Object> getViewStateForComponent(ViewStatePersistenceProvider.ViewStatePersisteable viewComponent)
    {
        Object viewId = viewComponent.getViewComponentId();
        if(!this.viewStates.containsKey(viewId))
        {
            this.viewStates.put(viewId, new HashMap<>());
        }
        return this.viewStates.get(viewId);
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }


    protected CMSPageService getCmsPageService()
    {
        return (CMSPageService)SpringUtil.getBean("cmsPageService");
    }
}
