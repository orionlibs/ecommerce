package de.hybris.platform.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.productcockpit.session.impl.NavigationArea;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class CatalogSelectorSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final String CATALOG_SECTION_CONTAINER_CLASS = "catalog_section_container";
    private static final String CTX_MENU_ARG = "ctx_menu";
    private static final String MENU_ITEM_SYNCH_CATALOGVERSION_ARG = "menuitemCatalogVersion";
    private static final String MENU_ITEM_SYNCH_CATEGORY_ARG = "menuitemCategory";
    private final Object rootDummy = new Treenode(this);
    private CatalogService productCockpitCatalogService;
    private SynchronizationService synchronizationService;


    public NavigationArea getNavigationArea()
    {
        return (NavigationArea)super.getNavigationArea();
    }


    protected List<Integer> getPathToRoot(Treeitem ti)
    {
        List<Integer> ret = new ArrayList<>();
        Treeitem currentItem = ti;
        while(currentItem != null)
        {
            ret.add(Integer.valueOf(currentItem.indexOf()));
            if(!currentItem.isOpen() || !currentItem.isVisible())
            {
                ret.clear();
                break;
            }
            currentItem = currentItem.getParentItem();
        }
        Collections.reverse(ret);
        return ret;
    }


    protected List<List<Integer>> getOpenedNodes(Tree tree, int depth)
    {
        List<List<Integer>> ret = new ArrayList<>();
        for(Object treeitem : tree.getItems())
        {
            Treeitem treeItem = (Treeitem)treeitem;
            if(treeItem.getLevel() <= depth && treeItem.isOpen() && treeItem.getTreechildren() != null && treeItem
                            .getTreechildren().getChildren().size() > 0)
            {
                List<Integer> path = getPathToRoot(treeItem);
                if(!path.isEmpty())
                {
                    ret.add(path);
                }
            }
        }
        return ret;
    }


    protected void openPath(Tree tree, List<Integer> path)
    {
        List<Treeitem> treeitems = tree.getTreechildren().getChildren();
        for(Integer integer : path)
        {
            Treeitem treeItem = treeitems.get(integer.intValue());
            treeItem.setOpen(true);
            if(treeItem.getTreechildren() == null)
            {
                break;
            }
            treeitems = treeItem.getTreechildren().getChildren();
        }
    }


    protected void restoreOpenedState(Tree tree, List<List<Integer>> openedNodes)
    {
        for(List<Integer> path : openedNodes)
        {
            openPath(tree, path);
        }
    }


    protected void expandAll(List<Object> treeitems)
    {
        if(treeitems == null || treeitems.isEmpty())
        {
            return;
        }
        for(Object treeitem : treeitems)
        {
            ((Treeitem)treeitem).setOpen(true);
            if(((Treeitem)treeitem).getTreechildren() != null)
            {
                expandAll(((Treeitem)treeitem).getTreechildren().getChildren());
            }
        }
    }


    protected Menupopup createContextMenu(Tree tree)
    {
        Menupopup ret = new Menupopup();
        Menuitem menuItem = new Menuitem(Labels.getLabel("general.expandall"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, tree));
        menuItem.setParent((Component)ret);
        menuItem = new Menuitem(Labels.getLabel("general.collapseall"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, tree));
        menuItem.setParent((Component)ret);
        menuItem = new Menuitem(Labels.getLabel("general.clearselection"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, tree));
        menuItem.setParent((Component)ret);
        Menu menu = new Menu(Labels.getLabel("navigationmenu.displaycategories"));
        Menupopup menuPopup = new Menupopup();
        Object object = new Object(this, tree);
        menuItem = new Menuitem(Labels.getLabel("navigationmenu.disabled"));
        menuItem.addEventListener("onClick", (EventListener)object);
        menuItem.setCheckmark(true);
        menuItem.setAttribute("max_level", "0");
        menuItem.setParent((Component)menuPopup);
        menuItem = new Menuitem(Labels.getLabel("navigationmenu.onelevel"));
        menuItem.addEventListener("onClick", (EventListener)object);
        menuItem.setCheckmark(true);
        menuItem.setAttribute("max_level", "1");
        menuItem.setParent((Component)menuPopup);
        menuItem = new Menuitem(Labels.getLabel("navigationmenu.twolevel"));
        menuItem.addEventListener("onClick", (EventListener)object);
        menuItem.setCheckmark(true);
        menuItem.setAttribute("max_level", "2");
        menuItem.setParent((Component)menuPopup);
        menuItem = new Menuitem(Labels.getLabel("navigationmenu.threelevel"));
        menuItem.addEventListener("onClick", (EventListener)object);
        menuItem.setCheckmark(true);
        menuItem.setAttribute("max_level", "3");
        menuItem.setParent((Component)menuPopup);
        menuItem = new Menuitem(Labels.getLabel("navigationmenu.unlimited"));
        menuItem.addEventListener("onClick", (EventListener)object);
        menuItem.setCheckmark(true);
        menuItem.setAttribute("max_level", UITools.getCockpitParameter("default.unlimited_tree_level", Executions.getCurrent()));
        menuItem.setParent((Component)menuPopup);
        int maxIndex = Math.min(((MyTreeModel)tree.getModel()).getCategoryLevel(), menuPopup.getChildren().size() - 1);
        ((Menuitem)menuPopup.getChildren().get(maxIndex)).setChecked(true);
        menuPopup.setParent((Component)menu);
        menu.setParent((Component)ret);
        return ret;
    }


    @Deprecated
    protected Menupopup createContextMenu(Tree tree, Div parent)
    {
        return createContextMenu(tree);
    }


    protected Menupopup adjustBaseMenupopup(Menupopup baseMenupopup, Tree currentTree, HtmlBasedComponent parent)
    {
        List<Object> componentsToDetach = new ArrayList();
        componentsToDetach.add(baseMenupopup.getAttribute("menuitemCatalogVersion"));
        componentsToDetach.add(baseMenupopup.getAttribute("menuitemCategory"));
        for(Object rawObject : componentsToDetach)
        {
            if(rawObject instanceof Component)
            {
                baseMenupopup.removeChild((Component)rawObject);
            }
        }
        Collection<CatalogVersionModel> selectedVersions = getNavigationArea().getSelectedCatalogVersions();
        if(CollectionUtils.isNotEmpty(selectedVersions) && CollectionUtils.isNotEmpty(currentTree.getSelectedItems()))
        {
            Menuitem menuitem = new Menuitem(Labels.getLabel("sync.contextmenu.versions"));
            UITools.addBusyListener((Component)menuitem, "onClick", (EventListener)new Object(this, parent, currentTree), null, "busy.sync");
            baseMenupopup.setAttribute("menuitemCatalogVersion", menuitem);
            menuitem.setParent((Component)baseMenupopup);
        }
        Collection<CategoryModel> selectedCategories = getNavigationArea().getSelectedCategories();
        if(!selectedCategories.isEmpty())
        {
            if(getSynchronizationService().isVersionSynchronizedAtLeastOnce(new ArrayList<>(selectedCategories)))
            {
                Menuitem menuitem = new Menuitem(Labels.getLabel("sync.contextmenu.categories"));
                UITools.addBusyListener((Component)menuitem, "onClick", (EventListener)new Object(this, parent), null, "busy.sync");
                menuitem.setParent((Component)baseMenupopup);
                baseMenupopup.setAttribute("menuitemCategory", menuitem);
            }
        }
        return baseMenupopup;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div container = new Div();
        container.setSclass("catalog_section_container");
        parent.appendChild((Component)container);
        Tree tree = new Tree();
        tree.setMultiple(true);
        tree.setZclass("z-dottree");
        MyTreeModel myTreeModel = new MyTreeModel(this, this.rootDummy);
        tree.setModel((TreeModel)myTreeModel);
        Menupopup ctxMenu = createContextMenu(tree, container);
        tree.setAttribute("ctx_menu", ctxMenu);
        tree.setTreeitemRenderer((TreeitemRenderer)new MyCatalogObjectRenderer(this));
        tree.setParent((Component)container);
        UITools.addBusyListener((Component)tree, "onSelect", (EventListener)new Object(this, tree), null, null);
        if(getNavigationArea().getSelectedCategories().isEmpty() &&
                        getNavigationArea().getSelectedCatalogVersions().size() == myTreeModel.getChildCount(this.rootDummy))
        {
            tree.setSelectedItem(null);
        }
    }


    protected void selectionChanged(List<ItemModel> newSelected)
    {
        selectionChanged(newSelected, false);
    }


    protected void selectionChanged(List<ItemModel> newSelected, boolean doubleClicked)
    {
        if(newSelected == null || newSelected.isEmpty())
        {
            getNavigationArea().setSelectedCatalogItems(Collections.EMPTY_SET, Collections.EMPTY_SET, doubleClicked);
        }
        else
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
            getNavigationArea().setSelectedCatalogItems(versions, categories, doubleClicked);
        }
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService catalogService)
    {
        this.productCockpitCatalogService = catalogService;
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
