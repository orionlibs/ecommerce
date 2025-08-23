package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.WorkflowPageableContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class WorkflowItemsBrowserModel extends AbstractPageableBrowserModel implements CockpitEventAcceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowItemsBrowserModel.class);
    private transient TypedObject workflowObject = null;
    private transient List<TypedObject> pageItems = null;
    private transient List<TypedObject> items = null;
    private List<MainAreaComponentFactory> viewModes = null;
    private WorkflowService workflowService;


    public WorkflowItemsBrowserModel(TypedObject workflowObject)
    {
        setItemsMovable(false);
        setItemsRemovable(true);
        setContextItemsMovable(false);
        setContextItemsRemovable(false);
        this.viewMode = "LIST";
        this.workflowObject = workflowObject;
        this.items = new ArrayList<>();
        this.pageItems = new ArrayList<>();
    }


    public String getLabel()
    {
        if(this.workflowObject.getObject() instanceof WorkflowTemplateModel)
        {
            return ((WorkflowTemplateModel)this.workflowObject.getObject()).getName();
        }
        if(GeneratedWorkflowConstants.TC.WORKFLOW.equals(this.workflowObject.getType().getCode()))
        {
            if(this.workflowObject.getObject() == null)
            {
                return "";
            }
            return ((WorkflowModel)this.workflowObject.getObject()).getName();
        }
        return "";
    }


    public ObjectTemplate getRootType()
    {
        if(!this.items.isEmpty())
        {
            return UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(this.items.get(0));
        }
        return UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
    }


    public void updateItems()
    {
        super.updateItems();
    }


    public void updateItems(int page)
    {
        setCurrentPage(page);
        clearSelection();
        if(this.workflowObject != null && this.workflowObject.getObject() != null)
        {
            this.items.clear();
            if(this.workflowObject.getObject() instanceof WorkflowTemplateModel)
            {
                for(WorkflowModel workflow : getServiceLayerWorkflowService().getWorkflowsForTemplateAndUser((WorkflowTemplateModel)this.workflowObject
                                .getObject(), UISessionUtils.getCurrentSession().getUser()))
                {
                    this.items.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(workflow.getAttachments()));
                }
                setLabel(((WorkflowTemplateModel)this.workflowObject.getObject()).getName());
            }
            else if(GeneratedWorkflowConstants.TC.WORKFLOW.equals(this.workflowObject.getType().getCode()))
            {
                this.items.addAll(UISessionUtils.getCurrentSession().getTypeService()
                                .wrapItems(((WorkflowModel)this.workflowObject.getObject()).getAttachments()));
                setLabel(((WorkflowModel)this.workflowObject.getObject()).getName());
            }
            else
            {
                LOG.warn("Passed object is nor Workflow neither WorkflowTemplate, it is: " + this.workflowObject.getType());
            }
            int totalCount = this.items.size();
            int fromIndex = Math.max(0, getOffset());
            int toIndex = Math.min(getPageSize() + fromIndex, totalCount);
            this.pageItems.clear();
            this.pageItems.addAll(this.items.subList(fromIndex, toIndex));
            setTotalCount(totalCount);
            fireChanged();
        }
        else
        {
            switchBrowser();
        }
    }


    protected void switchBrowser()
    {
        UIBrowserArea area = getArea();
        if(area != null)
        {
            List<BrowserModel> browsers = area.getBrowsers();
            if(!CollectionUtils.isEmpty(browsers))
            {
                BrowserModel newBrwoserToShow = null;
                for(BrowserModel browserModel : browsers)
                {
                    if(!browserModel.equals(this))
                    {
                        newBrwoserToShow = browserModel;
                        break;
                    }
                }
                if(newBrwoserToShow == null)
                {
                    if(area instanceof AbstractBrowserArea)
                    {
                        newBrwoserToShow = ((AbstractBrowserArea)area).createNewDefaultBrowser();
                    }
                }
                ((AbstractBrowserArea)area).removeBrowser((BrowserModel)this);
                area.show(newBrwoserToShow);
            }
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        WorkflowItemsBrowserModel newBrowserModel = new WorkflowItemsBrowserModel(this.workflowObject);
        newBrowserModel.setOffset(getOffset());
        newBrowserModel.setPageSize(getPageSize());
        newBrowserModel.setTotalCount(getTotalCount());
        newBrowserModel.setBrowserFilter(getBrowserFilter());
        newBrowserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        newBrowserModel.updateItems(getCurrentPage());
        newBrowserModel.setSimplePaging(isSimplePaging());
        return newBrowserModel;
    }


    public TypedObject getItem(int index)
    {
        if(index < getCurrentPage() * getPageSize() || index >= (getCurrentPage() + 1) * getPageSize())
        {
            setCurrentPage(index / getPageSize());
            updateItems(getCurrentPage());
        }
        if(!this.items.isEmpty())
        {
            return this.items.get(index);
        }
        return null;
    }


    public List<TypedObject> getItems()
    {
        return (this.pageItems == null) ? Collections.EMPTY_LIST : this.pageItems;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void removeItems(Collection<Integer> indexes)
    {
        WorkflowModel workflow = (WorkflowModel)this.workflowObject.getObject();
        List<Integer> indicies = new ArrayList<>(indexes);
        Collections.reverse(indicies);
        Object[] indexesArray = indicies.toArray();
        if(getServiceLayerWorkflowService().isPlanned(workflow))
        {
            Collection<TypedObject> toRemoveTypedObjects = new ArrayList<>();
            Collection<Object> toRemoveModels = new ArrayList();
            for(int i = 0; i < indexesArray.length; i++)
            {
                TypedObject removed = getItems().remove(((Integer)indexesArray[i]).intValue());
                toRemoveTypedObjects.add(removed);
                toRemoveModels.add(removed.getObject());
            }
            if(!toRemoveModels.isEmpty())
            {
                UISessionUtils.getCurrentSession().getModelService().removeAll(toRemoveModels);
                UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().updateItems();
                UISessionUtils.getCurrentSession()
                                .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, this.workflowObject, Collections.EMPTY_LIST));
                for(TypedObject typedObject : toRemoveTypedObjects)
                {
                    UISessionUtils.getCurrentSession()
                                    .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedObject, Collections.EMPTY_LIST));
                }
            }
        }
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new WorkflowPageableContentBrowser();
    }


    public List<? extends MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new GridMainAreaComponentFactory((GridItemRenderer)new Object(this)));
            this.viewModes.add(new ListMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    public void setArea(UIBrowserArea area)
    {
        super.setArea(area);
        if(area != null)
        {
            area.getPerspective().addCockpitEventAcceptor(this);
        }
    }


    private WorkflowService getServiceLayerWorkflowService()
    {
        if(this.workflowService == null)
        {
            this
                            .workflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.workflowService;
    }


    public UIConfigurationService getUIConfigurationService()
    {
        return (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
    }


    public TypedObject getWorkflowObject()
    {
        return this.workflowObject;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(getArea() != null)
        {
            updateItems();
        }
    }
}
