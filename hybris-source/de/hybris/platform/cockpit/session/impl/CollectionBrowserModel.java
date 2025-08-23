package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultPageableContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MultiTypeListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.PageableObjectCollection;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class CollectionBrowserModel extends AbstractPageableBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(CollectionBrowserModel.class);
    private transient ObjectCollection collection = null;
    protected transient List<TypedObject> items = null;
    protected List<MainAreaComponentFactory> viewModes = null;
    private ObjectCollectionService objectCollectionService = null;


    public CollectionBrowserModel()
    {
        setItemsMovable(true);
        setItemsRemovable(true);
        setContextItemsMovable(false);
        setContextItemsRemovable(false);
        this.viewMode = "GRID";
        this.items = new ArrayList<>();
    }


    public ObjectTemplate getRootType()
    {
        ObjectCollection coll = getCollection();
        if(coll != null && coll.getTotalCount() > 0)
        {
            if(coll instanceof PageableObjectCollection)
            {
                List<TypedObject> elements = ((PageableObjectCollection)coll).getElements(0, 1);
                if(!elements.isEmpty())
                {
                    return UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(elements.iterator().next());
                }
            }
            else
            {
                return UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(coll.getElements().get(0));
            }
        }
        return UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
    }


    public String getLabel()
    {
        String ret = (this.collection != null) ? this.collection.getLabel() : "";
        if(getBrowserFilterFixed() != null)
        {
            ret = ret + " - " + ret;
        }
        return ret;
    }


    public Object clone() throws CloneNotSupportedException
    {
        CollectionBrowserModel collectionBrowserModel = new CollectionBrowserModel();
        if(getObjectCollectionService() != null && getCollection() != null)
        {
            collectionBrowserModel.setCollection(getCollection());
        }
        collectionBrowserModel.setOffset(getOffset());
        collectionBrowserModel.setPageSize(getPageSize());
        collectionBrowserModel.setTotalCount(getTotalCount());
        collectionBrowserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        collectionBrowserModel.setBrowserFilter(getBrowserFilter());
        collectionBrowserModel.setSimplePaging(isSimplePaging());
        collectionBrowserModel.updateItems(getCurrentPage());
        return collectionBrowserModel;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new DefaultPageableContentBrowser();
    }


    public List<TypedObject> getItems()
    {
        List<TypedObject> result = new ArrayList<>();
        if(this.items != null)
        {
            TypeTools.filterOutRemovedItems(this.items);
            for(TypedObject typedObject : this.items)
            {
                result.add(typedObject);
            }
        }
        return result;
    }


    public void updateItems()
    {
        updateItems(getCurrentPage());
    }


    public void updateItems(int activePage)
    {
        if(this.collection != null)
        {
            int totalCount = this.collection.getTotalCount();
            int removedElements = getObjectCollectionService().removeNullItemReferences(this.collection);
            if(removedElements > 0)
            {
                totalCount -= removedElements;
                BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
                if(basePerspective.getNotifier() != null)
                {
                    Notification notification = new Notification(Labels.getLabel("specialcollection.itemsremoved", (Object[])new String[] {String.valueOf(removedElements)}));
                    basePerspective.getNotifier().setNotification(notification);
                }
                UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
            }
            setCurrentPage(activePage);
            if(getOffset() >= totalCount)
            {
                setCurrentPage(Math.max(0, totalCount - 1) / getPageSize());
            }
            clearSelection();
            int fromIndex = Math.max(0, getOffset());
            int toIndex = Math.min(getPageSize() + fromIndex, totalCount);
            this.items.clear();
            if(this.collection instanceof PageableObjectCollection)
            {
                this.items.addAll(((PageableObjectCollection)this.collection).getElements(fromIndex, getPageSize()));
            }
            else
            {
                List<TypedObject> elements = this.collection.getElements();
                this.items.addAll(elements.subList(fromIndex, toIndex));
            }
            setTotalCount(totalCount);
            fireChanged();
        }
        else
        {
            setCurrentPage(0);
            setOffset(0);
            clearSelection();
            setTotalCount(0);
        }
    }


    public void setCollection(ObjectCollection collection)
    {
        if(this.collection != collection)
        {
            this.collection = collection;
        }
    }


    public ObjectCollection getCollection()
    {
        return this.collection;
    }


    @Required
    public void setObjectCollectionService(ObjectCollectionService service)
    {
        this.objectCollectionService = service;
    }


    public ObjectCollectionService getObjectCollectionService()
    {
        if(this.objectCollectionService == null)
        {
            this.objectCollectionService = (ObjectCollectionService)SpringUtil.getBean("objectCollectionService");
        }
        return this.objectCollectionService;
    }


    public TypedObject getItem(int index)
    {
        if(index < getCurrentPage() * getPageSize() || index >= (getCurrentPage() + 1) * getPageSize())
        {
            setCurrentPage(index / getPageSize());
            updateItems(getCurrentPage());
        }
        if(this.collection != null)
        {
            if(this.collection instanceof PageableObjectCollection)
            {
                List<TypedObject> list = ((PageableObjectCollection)this.collection).getElements(index, 1);
                return list.isEmpty() ? null : list.iterator().next();
            }
            List<TypedObject> elements = this.collection.getElements();
            return (elements.size() > index) ? elements.get(index) : null;
        }
        return null;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
        List<TypedObject> elements = new ArrayList<>();
        if(isAllMarked())
        {
            elements.addAll(getSelectedItems());
        }
        else
        {
            List<TypedObject> items = getItems();
            for(Integer i : indexes)
            {
                elements.add(items.get(i.intValue()));
            }
        }
        UINavigationArea navigationArea = getArea().getManagingPerspective().getNavigationArea();
        List<ObjectCollection> specialCollections = navigationArea.getObjectCollectionService().getSpecialCollections(
                        UISessionUtils.getCurrentSession().getUser());
        ObjectCollection blacklist = null;
        for(ObjectCollection oc : specialCollections)
        {
            if("Blacklist".equalsIgnoreCase(oc.getLabel()))
            {
                blacklist = oc;
                break;
            }
        }
        if(blacklist != null)
        {
            getObjectCollectionService().removeFromCollection(blacklist, elements);
            updateItems();
            getArea().getManagingPerspective().getNavigationArea().update();
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
        List<TypedObject> elements = new ArrayList<>();
        if(isAllMarked())
        {
            elements.addAll(getSelectedItems());
        }
        else
        {
            List<TypedObject> items = getItems();
            for(Integer i : indexes)
            {
                elements.add(items.get(i.intValue()));
            }
        }
        if(getObjectCollectionService().hasWriteCollectionRight((PrincipalModel)UISessionUtils.getCurrentSession().getUser(), getCollection())
                        .booleanValue())
        {
            getObjectCollectionService().removeFromCollection(getCollection(), elements);
            updateItems();
            getArea().getManagingPerspective().getNavigationArea().update();
        }
        else
        {
            UISessionUtils.getCurrentSession()
                            .getCurrentPerspective()
                            .getNotifier()
                            .setNotification(new Notification(
                                            Labels.getLabel("general.permission.denied"),
                                            Labels.getLabel("collection.cannot.modify.no.accees.rights")));
        }
    }


    public List<TypedObject> getSelectedItems()
    {
        if(isAllMarked())
        {
            if(this instanceof DynamicQueryBrowserModel)
            {
                return getItems();
            }
            return this.collection.getElements();
        }
        return super.getSelectedItems();
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new GridMainAreaComponentFactory());
            this.viewModes.add(new MultiTypeListMainAreaComponentFactory());
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }
}
