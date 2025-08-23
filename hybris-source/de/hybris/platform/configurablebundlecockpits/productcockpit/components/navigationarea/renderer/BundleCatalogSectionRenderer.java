package de.hybris.platform.configurablebundlecockpits.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.productcockpit.components.navigationarea.CatalogNavigationAreaModel;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class BundleCatalogSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final String CATALOG_SECTION_CONTAINER_CLASS = "catalog_section_container";
    private CatalogService productCockpitCatalogService;
    private SynchronizationService synchronizationService;
    private CommonI18NService commonI18NService;


    public CatalogNavigationAreaModel getSectionPanelModel()
    {
        return (CatalogNavigationAreaModel)super.getSectionPanelModel();
    }


    protected Menupopup createContextMenu(Component parent, Listbox listbox)
    {
        Menupopup popupMenu = new Menupopup();
        Menuitem menuItem = new Menuitem(Labels.getLabel("sync.contextmenu.version"));
        UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new BusyEventListener(this, parent, listbox), null, "busy.sync");
        menuItem.setParent((Component)popupMenu);
        return popupMenu;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div container = new Div();
        container.setSclass("catalog_section_container");
        parent.appendChild((Component)container);
        List<CatalogVersionModel> versions = getSectionPanelModel().getNavigationArea().getCatalogVersions();
        Listbox listbox = createList("navigation_catalogues", versions, (ListitemRenderer)new CatalogSectionSingleItemRenderer(
                        getSectionPanelModel(), getCommonI18NService()));
        listbox.setFixedLayout(true);
        Menupopup ctxMenu = createContextMenu((Component)container, listbox);
        parent.appendChild((Component)ctxMenu);
        listbox.setContext((Popup)ctxMenu);
        listbox.setParent((Component)container);
        if(getSectionPanelModel().getNavigationArea().getSelectedCatalogVersion() == null)
        {
            selectionChanged(getNonActiveCatalogVersionModel(versions));
        }
        UITools.addBusyListener((Component)listbox, "onSelect", (EventListener)new Object(this, listbox), null, null);
    }


    protected void selectionChanged(CatalogVersionModel catalogVersion)
    {
        if(catalogVersion != null)
        {
            getSectionPanelModel().getNavigationArea().setSelectedCatalogItems(catalogVersion);
        }
    }


    protected void sendNotification(BaseUICockpitPerspective perspective, List<String> chosenRules)
    {
        StringBuilder detailInformation = new StringBuilder();
        for(String chosenRule : chosenRules)
        {
            detailInformation.append(", " + chosenRule);
        }
        detailInformation.append(" ");
        Notification notification = new Notification(Labels.getLabel("synchronization.finished.start") + Labels.getLabel("synchronization.finished.start") + detailInformation.substring(1));
        if(perspective.getNotifier() != null)
        {
            perspective.getNotifier().setNotification(notification);
        }
    }


    protected CatalogVersionModel getNonActiveCatalogVersionModel(List<CatalogVersionModel> versions)
    {
        CatalogVersionModel defaultModel = null;
        for(CatalogVersionModel model : versions)
        {
            if(!model.getActive().booleanValue())
            {
                defaultModel = model;
            }
        }
        return defaultModel;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    protected CatalogService getProductCockpitCatalogService()
    {
        return this.productCockpitCatalogService;
    }


    @Required
    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }


    protected SynchronizationService getSynchronizationService()
    {
        return this.synchronizationService;
    }
}
