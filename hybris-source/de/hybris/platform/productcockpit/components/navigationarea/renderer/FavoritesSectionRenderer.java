package de.hybris.platform.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.components.navigationarea.CatalogNavigationAreaModel;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class FavoritesSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private final String CATALOG_SECTION_CONTAINER_CLASS = "catalog_section_container";


    public CatalogNavigationAreaModel getSectionPanelModel()
    {
        return (CatalogNavigationAreaModel)super.getSectionPanelModel();
    }


    protected Menupopup createContextMenu(Listbox listbox)
    {
        Menupopup popupMenu = new Menupopup();
        Menuitem menuItem = new Menuitem("Remvove");
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuItem.setParent((Component)popupMenu);
        menuItem = new Menuitem(Labels.getLabel("general.collapseall"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuItem.setParent((Component)popupMenu);
        menuItem = new Menuitem(Labels.getLabel("general.clearselection"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuItem.setParent((Component)popupMenu);
        return popupMenu;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div container = new Div();
        container.setSclass("catalog_section_container");
        parent.appendChild((Component)container);
        Button button = new Button();
        button.setLabel(Labels.getLabel("favorites.create"));
        button.setTooltiptext(Labels.getLabel("favorites.create"));
        UserModel curent = UISessionUtils.getCurrentSession().getUser();
        button.addEventListener("onClick", (EventListener)new Object(this, curent));
        container.appendChild((Component)button);
        List<FavoriteCategory> favorities = getSectionPanelModel().getNavigationArea().getFavoriteCategoryService().getFavoriteCategories(curent);
        Listbox listbox = createList("navigation_catalogues", favorities, (ListitemRenderer)new FavoritesSectionSingleItemRenderer(this));
        if(listbox != null)
        {
            listbox.setFixedLayout(true);
            Menupopup ctxMenu = createContextMenu(listbox);
            parent.appendChild((Component)ctxMenu);
            listbox.setParent((Component)container);
            listbox.addEventListener("onSelect", (EventListener)new Object(this));
        }
    }


    protected Menupopup createFavoriteCategoryContext(FavoriteCategory favorite, Label label, Component parent)
    {
        Menupopup ret = new Menupopup();
        Menuitem mi = new Menuitem(Labels.getLabel("query.rename"));
        mi.addEventListener("onClick", (EventListener)new Object(this));
        mi.setParent((Component)ret);
        mi = new Menuitem(Labels.getLabel("query.delete"));
        mi.addEventListener("onClick", (EventListener)new Object(this, favorite));
        mi.setParent((Component)ret);
        return ret;
    }


    protected void selectionChanged(List<ItemModel> newSelected)
    {
        selectionChanged(newSelected, false);
    }


    protected void selectionChanged(List<ItemModel> newSelected, boolean doubleClicked)
    {
        if(newSelected != null && !newSelected.isEmpty())
        {
            Collection<CatalogVersionModel> versions = new LinkedHashSet<>();
            Collection<CategoryModel> categories = new LinkedHashSet<>();
            for(ItemModel i : newSelected)
            {
                if(i instanceof CatalogVersionModel)
                {
                    versions.add((CatalogVersionModel)i);
                    continue;
                }
                if(i instanceof CategoryModel)
                {
                    categories.add((CategoryModel)i);
                }
            }
        }
    }
}
