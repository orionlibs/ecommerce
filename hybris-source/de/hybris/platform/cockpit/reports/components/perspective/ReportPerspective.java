package de.hybris.platform.cockpit.reports.components.perspective;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsCache;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Menupopup;

public class ReportPerspective extends BaseUICockpitPerspective
{
    private static final Logger LOG = LoggerFactory.getLogger(ReportPerspective.class);
    private ModelService modelService;
    private JasperReportsCache jasperReportsCache;


    public void setJasperReportsCache(JasperReportsCache jasperReportsCache)
    {
        this.jasperReportsCache = jasperReportsCache;
    }


    public void onShow()
    {
        getEditorArea().setManagingPerspective((UICockpitPerspective)this);
        createTemplateList("WidgetPreferences");
        BrowserModel browserModel = getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof SearchBrowserModel)
        {
            SearchBrowserModel searchBrowser = (SearchBrowserModel)browserModel;
            if(searchBrowser.getResult() == null)
            {
                searchBrowser.updateItems(0);
            }
        }
        try
        {
            if(getActiveItem() != null)
            {
                activateItemInEditor(getActiveItem());
            }
        }
        catch(Exception e)
        {
            LOG.warn("Error occurred when trying to load active item (Reason: '" + e.getMessage() + "').");
        }
        super.onShow();
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            if(itemChangedEvent.getChangeType().equals(ItemChangedEvent.ChangeType.CREATED))
            {
                List<ItemModel> items = UISessionUtils.getCurrentSession().getTypeService().unwrapItems(
                                Arrays.asList(new TypedObject[] {itemChangedEvent.getItem()}));
                if(items.get(0) instanceof WidgetPreferencesModel)
                {
                    WidgetPreferencesModel conf = (WidgetPreferencesModel)items.get(0);
                    conf.setOwnerUser(UISessionUtils.getCurrentSession().getUser());
                    this.modelService.save(conf);
                }
            }
            if(itemChangedEvent.getChangeType().equals(ItemChangedEvent.ChangeType.CHANGED))
            {
                List<ItemModel> items = UISessionUtils.getCurrentSession().getTypeService().unwrapItems(
                                Arrays.asList(new TypedObject[] {itemChangedEvent.getItem()}));
                if(items.get(0) instanceof WidgetPreferencesModel)
                {
                    this.jasperReportsCache.update((WidgetPreferencesModel)items.get(0));
                }
            }
            getBrowserArea().update();
        }
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public boolean generateCreateMenuitems(Menupopup menupopup, boolean expandEditorArea, CreateContext createContext, boolean popup, Map<String, Object> initValues, boolean loadDefaultValues)
    {
        super.generateCreateMenuitems(menupopup, expandEditorArea, createContext, popup, initValues, loadDefaultValues);
        return false;
    }


    public boolean canCreate(TemplateListEntry entry)
    {
        return false;
    }
}
