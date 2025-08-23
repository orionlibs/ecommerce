package de.hybris.platform.productcockpit.components.navigationarea.renderer;

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

public class CatalogSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final String CATALOG_SECTION_CONTAINER_CLASS = "catalog_section_container";
    private CatalogService productCockpitCatalogService;
    private SynchronizationService synchronizationService;


    public CatalogNavigationAreaModel getSectionPanelModel()
    {
        return (CatalogNavigationAreaModel)super.getSectionPanelModel();
    }


    protected Menupopup createContextMenu(Component parent, Listbox listbox)
    {
        Menupopup popupMenu = new Menupopup();
        Menuitem menuItem = new Menuitem(Labels.getLabel("sync.contextmenu.version"));
        UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new Object(this, parent, listbox), null, "busy.sync");
        menuItem.setParent((Component)popupMenu);
        return popupMenu;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div container = new Div();
        container.setSclass("catalog_section_container");
        parent.appendChild((Component)container);
        List<CatalogVersionModel> versions = getSectionPanelModel().getNavigationArea().getCatalogVersions();
        Listbox listbox = createList("navigation_catalogues", versions, (ListitemRenderer)new CatalogSectionSingleItemRenderer(this));
        listbox.setFixedLayout(true);
        Menupopup ctxMenu = createContextMenu((Component)container, listbox);
        parent.appendChild((Component)ctxMenu);
        listbox.setContext((Popup)ctxMenu);
        listbox.setParent((Component)container);
        UITools.addBusyListener((Component)listbox, "onSelect", (EventListener)new Object(this, listbox), null, null);
    }


    @Deprecated
    protected void selectionChanged(CatalogVersionModel catalogVersion, Listbox listbox)
    {
        if(catalogVersion == null)
        {
            return;
        }
        getSectionPanelModel().getNavigationArea().setSelectedCatalogItems(catalogVersion);
    }


    protected void selectionChanged(CatalogVersionModel catalogVersion)
    {
        if(catalogVersion == null)
        {
            return;
        }
        getSectionPanelModel().getNavigationArea().setSelectedCatalogItems(catalogVersion);
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public CatalogService getProductCockpitCatalogService()
    {
        return this.productCockpitCatalogService;
    }


    @Required
    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }


    public SynchronizationService getSynchronizationService()
    {
        return this.synchronizationService;
    }


    private void sendNotification(BaseUICockpitPerspective perspective, List<String> chosenRules)
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
}
