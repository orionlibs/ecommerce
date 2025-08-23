package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public abstract class AbstractSynchronizationAction extends AbstractListViewAction
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSynchronizationAction.class);
    protected TypeService typeService = null;
    protected SynchronizationService syncService = null;
    protected static final String SYNC_JOBS_KEY = "syncJobs";
    protected static final String SOURCE_CATALOG_VERSION_KEY = "sourceCatalogVersion";
    protected static final String CURRENT_STATUS_KEY = "currentStatus";
    protected static final String SOURCE_ITEMS_KEY = "sourceItems";


    protected abstract String getSyncOKImg();


    protected abstract String getSyncNotOKImg();


    protected abstract String getSyncInitImg();


    protected String getSyncBusyImg()
    {
        return "cockpit/images//synchronization_busy.gif";
    }


    protected String getTypeRestriction()
    {
        return null;
    }


    protected List<SyncItemJobModel>[] getSyncJobs(ListViewAction.Context context)
    {
        return (List<SyncItemJobModel>[])context.getMap().get("syncJobs");
    }


    protected int getCurrentStatus(ListViewAction.Context context)
    {
        Object object = context.getMap().get("currentStatus");
        if(object instanceof Integer)
        {
            return ((Integer)object).intValue();
        }
        return -1;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        String imgURI = null;
        if(checkTypeRestriction(context))
        {
            switch(getCurrentStatus(context))
            {
                case 0:
                    imgURI = getSyncOKImg();
                    break;
                case 1:
                    imgURI = getSyncNotOKImg();
                    break;
                case 2:
                    imgURI = getSyncInitImg();
                    break;
            }
        }
        return imgURI;
    }


    private boolean checkTypeRestriction(ListViewAction.Context context)
    {
        boolean ret = false;
        try
        {
            ret = (getTypeRestriction() == null || getTypeService().getBaseType(getTypeRestriction()).isAssignableFrom((ObjectType)context.getItem().getType()));
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.error(e.getMessage());
            }
        }
        return ret;
    }


    protected abstract void doCreateContext(ListViewAction.Context paramContext);


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected SynchronizationService getSynchronizationService()
    {
        if(this.syncService == null)
        {
            this.syncService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.syncService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected Set<TypedObject> getPossiblyAffectedObjects(Collection<TypedObject> sourceObjects)
    {
        Set<TypedObject> objectsToUpdate = new HashSet<>();
        for(TypedObject source : sourceObjects)
        {
            objectsToUpdate.add(source);
            objectsToUpdate.addAll(getSynchronizationService().getSyncTargets(source));
        }
        return objectsToUpdate;
    }


    protected void sendItemChangeEvents(Collection<TypedObject> updatedObjects)
    {
        if(containsBaseProduct(updatedObjects))
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, null, Collections.EMPTY_LIST));
        }
        else
        {
            for(TypedObject typedObject : updatedObjects)
            {
                Set<PropertyDescriptor> descriptors = typedObject.getType().getPropertyDescriptors();
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedObject, descriptors));
            }
        }
    }


    protected boolean containsBaseProduct(Collection<TypedObject> updatedObjects)
    {
        for(TypedObject updatedObject : updatedObjects)
        {
            if(isBaseProduct(updatedObject))
            {
                return true;
            }
        }
        return false;
    }


    private boolean isBaseProduct(TypedObject item)
    {
        return (item.getObject() instanceof ProductModel && ((ProductModel)item.getObject()).getVariantType() != null);
    }


    public String getTooltip(ListViewAction.Context context)
    {
        String tooltip = null;
        if(checkTypeRestriction(context))
        {
            switch(getCurrentStatus(context))
            {
                case 0:
                    tooltip = Labels.getLabel("gridview.item.synchronized.true");
                    break;
                case 1:
                    tooltip = Labels.getLabel("gridview.item.synchronized.false");
                    break;
                case 2:
                    tooltip = Labels.getLabel("gridview.item.synchronized.init");
                    break;
            }
            tooltip = Labels.getLabel("general.status") + ": " + Labels.getLabel("general.status");
        }
        else
        {
            tooltip = Labels.getLabel("sync.selecteditems");
        }
        return tooltip;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getStatusCode(ListViewAction.Context context)
    {
        String ret = null;
        if(checkTypeRestriction(context))
        {
            switch(getCurrentStatus(context))
            {
                case 0:
                    ret = "sync_ok";
                    break;
                case 1:
                    ret = "sync_notok";
                    break;
                case 2:
                    ret = "sync_init";
                    break;
            }
        }
        return ret;
    }
}
