package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.TaskContentBrowser;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class TaskBrowserModel extends AbstractPageableBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskBrowserModel.class);
    private final List<String> attachmentTypes;
    private Set<WorkflowActionStatus> availableStatusFilters = null;
    private Set<WorkflowActionStatus> activeStatusFilters = null;
    List<TypedObject> tasks = null;
    private transient List<TypedObject> items = null;
    private WorkflowActionService workflowActionService;
    private TypeService typeService;


    public TaskBrowserModel(String attachmentType)
    {
        this.attachmentTypes = Collections.singletonList(attachmentType);
        this.items = new ArrayList<>();
        this.viewMode = "LIST";
        createDefaultFilers();
    }


    public TaskBrowserModel(List<String> attachmentTypes)
    {
        this.attachmentTypes = attachmentTypes;
        this.items = new ArrayList<>();
        this.viewMode = "LIST";
        createDefaultFilers();
    }


    private void createDefaultFilers()
    {
        if(this.availableStatusFilters == null)
        {
            this.availableStatusFilters = new LinkedHashSet<>();
            this.availableStatusFilters.add(WorkflowActionStatus.IN_PROGRESS);
            this.availableStatusFilters.add(WorkflowActionStatus.COMPLETED);
            this.availableStatusFilters.add(WorkflowActionStatus.TERMINATED);
        }
        if(this.activeStatusFilters == null)
        {
            this.activeStatusFilters = new LinkedHashSet<>();
            this.activeStatusFilters.add(WorkflowActionStatus.IN_PROGRESS);
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        TaskBrowserModel browserModel = new TaskBrowserModel(getAttachmentTypes());
        browserModel.setTotalCount(getTotalCount());
        browserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        browserModel.setBrowserFilter(getBrowserFilter());
        browserModel.setSimplePaging(isSimplePaging());
        browserModel.updateItems();
        return browserModel;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new TaskContentBrowser(getAttachmentTypes());
    }


    public TypedObject getItem(int index)
    {
        if(index < getCurrentPage() * getPageSize() || index >= (getCurrentPage() + 1) * getPageSize())
        {
            setCurrentPage(index / getPageSize());
            updateItems(getCurrentPage());
        }
        if(this.tasks != null)
        {
            return this.tasks.get(index);
        }
        return null;
    }


    public List<TypedObject> getItems()
    {
        return (this.items == null) ? Collections.EMPTY_LIST : this.items;
    }


    public void updateItems()
    {
        updateItems(getCurrentPage());
    }


    public void updateItems(int page)
    {
        setCurrentPage(page);
        clearSelection();
        refreshTasks();
        setTotalCount(this.tasks.size());
        if(this.tasks != null)
        {
            int fromIndex = Math.max(0, getOffset());
            int toIndex = Math.min(getPageSize() + fromIndex, this.tasks.size());
            this.items.clear();
            this.items.addAll(this.tasks.subList(fromIndex, toIndex));
            fireItemsChanged();
        }
    }


    public List<TypedObject> getTasks()
    {
        if(this.tasks == null)
        {
            this.tasks = new ArrayList<>();
            refreshTasks();
        }
        return this.tasks;
    }


    public void setTasks(List<TypedObject> tasks)
    {
        this.tasks = tasks;
    }


    public void refreshTasks()
    {
        setTasks(getTypeService().wrapItems(
                        getWorkflowActionService().getAllUserWorkflowActionsWithAttachments(getAttachmentTypes(), getActiveStatusFilters())));
    }


    public String getLabel()
    {
        return Labels.getLabel("navigationarea.infobox.tasks");
    }


    private WorkflowActionService getWorkflowActionService()
    {
        if(this.workflowActionService == null)
        {
            this.workflowActionService = (WorkflowActionService)Registry.getApplicationContext().getBean("workflowActionService");
        }
        return this.workflowActionService;
    }


    public void collapse()
    {
    }


    public boolean isCollapsed()
    {
        return true;
    }


    private List<String> getAttachmentTypes()
    {
        return this.attachmentTypes;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
        UINavigationArea navigationArea = getArea().getManagingPerspective().getNavigationArea();
        List<ObjectCollection> specialCollections = navigationArea.getObjectCollectionService().getSpecialCollections(UISessionUtils.getCurrentSession().getUser());
        UICollectionQuery collectionQuery = null;
        for(ObjectCollection oc : specialCollections)
        {
            if("Blacklist".equalsIgnoreCase(oc.getLabel()))
            {
                collectionQuery = new UICollectionQuery(oc);
                break;
            }
        }
        if(navigationArea instanceof BaseUICockpitNavigationArea)
        {
            Object[] indexesArray = indexes.toArray();
            for(int i = 0; i < indexesArray.length; i++)
            {
                ((BaseUICockpitNavigationArea)navigationArea).addToCollection(getItem(((Integer)indexesArray[i]).intValue()), collectionQuery, true);
            }
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
        UINavigationArea navigationArea = getArea().getManagingPerspective().getNavigationArea();
        if(navigationArea instanceof BaseUICockpitNavigationArea)
        {
            Object[] indexesArray = indexes.toArray();
            for(int i = 0; i < indexesArray.length; i++)
            {
                getItems().remove(((Integer)indexesArray[i]).intValue());
            }
        }
    }


    public List<TypedObject> getSelectedItems()
    {
        if(isAllMarked())
        {
            return this.tasks;
        }
        return super.getSelectedItems();
    }


    public boolean hasStatusBar()
    {
        return false;
    }


    public boolean isDuplicatable()
    {
        return false;
    }


    public Set<WorkflowActionStatus> getAvailableStatusFilters()
    {
        return this.availableStatusFilters;
    }


    public void setAvailableStatusFilters(Set<WorkflowActionStatus> availableStatusFilters)
    {
        this.availableStatusFilters = availableStatusFilters;
    }


    public Set<WorkflowActionStatus> getActiveStatusFilters()
    {
        return this.activeStatusFilters;
    }


    public void setActiveStatusFilters(Set<WorkflowActionStatus> activeStatusFilters)
    {
        this.activeStatusFilters = activeStatusFilters;
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
