package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.impl.SectionTableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class CloneContentElementAction extends AbstractCmscockpitListViewAction
{
    private static final Logger LOG = Logger.getLogger(CloneContentElementAction.class);
    private static final String ABSTRACTCOMPONENT_UID_PREFIX = "comp";
    private static final String CLONE_CONTENT_ELEMENT_ACTION_URL = "/cmscockpit/images/cnt_elem_clone_action.png";
    private ModelService modelService;
    private GenericRandomNameProducer genericRandomNameProducer;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected ContentSlotModel getCurrentSlot(ListViewAction.Context context)
    {
        TableModel model = context.getModel();
        try
        {
            if(model instanceof SectionTableModel)
            {
                BrowserSectionModel sectionModel = ((SectionTableModel)model).getModel();
                if(sectionModel.getRootItem() instanceof TypedObject)
                {
                    return (ContentSlotModel)((TypedObject)sectionModel.getRootItem()).getObject();
                }
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        boolean canCreate = getSystemService().checkPermissionOn(context.getItem().getType().getCode(), "create");
        if(!isEnabled() || isComponentLockedForUser(context.getItem()) || !isWritableCatalog(context.getItem()) ||
                        !getSystemService().checkPermissionOn("AbstractPage", "change") || !canCreate)
        {
            return null;
        }
        return "/cmscockpit/images/cnt_elem_clone_action.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.clone");
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    protected GenericRandomNameProducer getGenericRandomNameProducer()
    {
        if(this.genericRandomNameProducer == null)
        {
            this.genericRandomNameProducer = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
        }
        return this.genericRandomNameProducer;
    }
}
