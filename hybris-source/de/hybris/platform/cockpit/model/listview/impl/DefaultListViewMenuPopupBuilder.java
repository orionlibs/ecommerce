package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.ColumnLayoutComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AdvanceMenupopup;
import de.hybris.platform.cockpit.components.menu.ManageMenuitem;
import de.hybris.platform.cockpit.components.menu.impl.ColumnsAssignedListViewRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ColumnGroup;
import de.hybris.platform.cockpit.model.listview.ListViewMenuPopupBuilder;
import de.hybris.platform.cockpit.model.listview.UIListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;

public class DefaultListViewMenuPopupBuilder implements ListViewMenuPopupBuilder
{
    public static final String ON_LATER = "onLater";
    protected static final String LISTVIEW_COLUMN_MENU_HIDECOLUMN = "listview.column.menu.hidecolumn";
    protected static final String LISTVIEW_COLUMN_MENU_OTHERLANGUAGES = "listview.column.menu.otherlanguages";
    protected static final String LISTVIEW_COLUMN_MENU_RESET = "listview.column.menu.reset";
    protected static final String LISTVIEW_COLUMN_MENU_SHOWCOLUMN = "listview.column.menu.showcolumn";
    protected static final String LISTVIEW_COLUMN_MENU_ALL_COLUMNS = "listview.column.menu.allcolumns";
    protected final Menupopup headerPopup;
    protected Comparator<ColumnDescriptor> colComparator = null;


    public DefaultListViewMenuPopupBuilder()
    {
        this.headerPopup = new Menupopup();
    }


    public void addHideColumnElement(UIListView listView, int columnIndex)
    {
        Menuitem hideColumnMenuItem = new Menuitem(Labels.getLabel("listview.column.menu.hidecolumn"));
        this.headerPopup.appendChild((Component)hideColumnMenuItem);
        hideColumnMenuItem.addEventListener("onClick", (EventListener)new Object(this, listView));
        hideColumnMenuItem.addEventListener("onLater", (EventListener)new Object(this, listView, columnIndex));
    }


    public void addLocalizedColumnElement(UIListView listView, ColumnDescriptor descriptor)
    {
        if(listView.getModel().getColumnComponentModel().isColumnLocalized(descriptor) &&
                        !listView.getModel().getColumnComponentModel().getHiddenLocalizedColumns(descriptor).isEmpty())
        {
            Menupopup hiddenLocColMenupopup = createNewMenuPopup(Labels.getLabel("listview.column.menu.otherlanguages"), this.headerPopup);
            hiddenLocColMenupopup.addEventListener("onOpen", (EventListener)new Object(this, hiddenLocColMenupopup, listView, descriptor));
        }
    }


    public void addResetColumnElement(UIListView listView)
    {
        Menuitem resetItem = new Menuitem(Labels.getLabel("listview.column.menu.reset"));
        resetItem.addEventListener("onClick", (EventListener)new Object(this, listView));
        this.headerPopup.appendChild((Component)resetItem);
    }


    public void addSeparator()
    {
        this.headerPopup.appendChild((Component)new Menuseparator());
    }


    public void addShowColumnElement(UIListView listView, ColumnLayoutComponent mainColumnComponent, boolean addResetItem)
    {
        Menupopup hiddenColumnMenuPopup = createNewMenuPopup(Labels.getLabel("listview.column.menu.showcolumn"), this.headerPopup);
        hiddenColumnMenuPopup.addEventListener("onOpen", (EventListener)new Object(this, listView, hiddenColumnMenuPopup, mainColumnComponent));
        if(addResetItem)
        {
            addSeparator();
            addResetColumnElement(listView);
        }
    }


    public void addShowColumnElement(UIListView listView, int colIndex, boolean addResetItem)
    {
        Menupopup hiddenColumnMenuPopup = createNewMenuPopup(Labels.getLabel("listview.column.menu.showcolumn"), this.headerPopup);
        hiddenColumnMenuPopup.addEventListener("onOpen", (EventListener)new Object(this, listView, hiddenColumnMenuPopup, colIndex));
    }


    public void addShowColumnLogic(UIListView listView, Menupopup hiddenColumnMenuPopup, int colIndex, boolean open)
    {
        if(open)
        {
            addAllColumnElement(listView, hiddenColumnMenuPopup, colIndex);
        }
        else
        {
            hiddenColumnMenuPopup.invalidate();
        }
    }


    public void addAllColumnElement(UIListView listView, Menupopup columnMenuPopup, int colIndex)
    {
        columnMenuPopup.getChildren().clear();
        List<ColumnDescriptor> visibleColumns = listView.getModel().getColumnComponentModel().getVisibleColumns();
        Object object1 = new Object(this, listView);
        ColumnsAssignedListViewRenderer columnsAssignedListViewRenderer = new ColumnsAssignedListViewRenderer(visibleColumns, listView, (EventListener)object1);
        columnMenuPopup.appendChild((Component)columnsAssignedListViewRenderer.createMenuListViewComponent());
        Object object2 = new Object(this, listView, (EventListener)object1, colIndex, visibleColumns);
        ManageMenuitem manageMenuitem = new ManageMenuitem(Labels.getLabel("listview.column.manage"), (EventListener)object2);
        columnMenuPopup.appendChild((Component)manageMenuitem);
        columnMenuPopup.appendChild((Component)new Menuseparator());
        Menupopup allColumnsMenuPopup = createNewMenuPopup(Labels.getLabel("listview.column.menu.allcolumns"), columnMenuPopup);
        allColumnsMenuPopup.addEventListener("onOpen", (EventListener)new Object(this, listView, allColumnsMenuPopup, colIndex));
    }


    public void addAllColumnLogic(UIListView listView, Menupopup allColumnsMenuPopup, int colIndex)
    {
        allColumnsMenuPopup.getChildren().clear();
        ColumnGroup group = listView.getModel().getColumnComponentModel().getRootColumnGroup();
        int size = buildHiddenColumnMenupopup(listView, group, allColumnsMenuPopup, colIndex);
        allColumnsMenuPopup.setWidth("" + size + 2 + "em");
    }


    public Menupopup buildBackgroundColumnMenuPopup(UIListView listView, ColumnLayoutComponent mainColumnComponent)
    {
        addShowColumnElement(listView, mainColumnComponent, true);
        return this.headerPopup;
    }


    public int buildHiddenColumnMenupopup(UIListView listView, ColumnGroup group, Menupopup menuPopup, int colIndex)
    {
        int popupWidth = 0;
        menuPopup.getChildren().clear();
        List<ColumnDescriptor> hiddenColumns = new ArrayList<>();
        hiddenColumns.addAll(group.getHiddenColumns());
        Collections.sort(hiddenColumns, getColumnComparator());
        for(ColumnDescriptor hiddenColDescr : hiddenColumns)
        {
            String name = hiddenColDescr.getName();
            if(name != null)
            {
                popupWidth = Math.max(popupWidth, name.length());
            }
            Menuitem hiddenColMenuItem = new Menuitem(name);
            menuPopup.appendChild((Component)hiddenColMenuItem);
            String evtnm = "onClick";
            hiddenColMenuItem.addEventListener("onClick", (EventListener)new Object(this, listView, hiddenColDescr, colIndex));
        }
        for(ColumnGroup subGroup : group.getColumnGroups())
        {
            if(!CollectionUtils.isEmpty(subGroup.getAllColumns()))
            {
                String name = subGroup.getName();
                if(name != null)
                {
                    popupWidth = Math.max(popupWidth, name.length());
                }
                Menu subGroupMenu = new Menu(name);
                menuPopup.appendChild((Component)subGroupMenu);
                Menupopup subGroupMenupopup = new Menupopup();
                subGroupMenu.appendChild((Component)subGroupMenupopup);
                buildHiddenColumnMenupopup(listView, subGroup, subGroupMenupopup, colIndex);
            }
        }
        return popupWidth;
    }


    public Menupopup buildStandardColumnMenuPopup(UIListView listView, ColumnDescriptor descriptor, int columnIndex)
    {
        addResetColumnElement(listView);
        addHideColumnElement(listView, columnIndex);
        addSeparator();
        addLocalizedColumnElement(listView, descriptor);
        addShowColumnElement(listView, columnIndex, true);
        addSeparator();
        return this.headerPopup;
    }


    protected Menupopup createNewMenuPopup(String menuLabel, Menupopup menuPopup)
    {
        Menu menu = new Menu(menuLabel);
        AdvanceMenupopup popup = new AdvanceMenupopup();
        menu.appendChild((Component)popup);
        menuPopup.appendChild((Component)menu);
        return (Menupopup)popup;
    }


    protected Comparator<ColumnDescriptor> getColumnComparator()
    {
        if(this.colComparator == null)
        {
            this.colComparator = (Comparator<ColumnDescriptor>)new Object(this);
        }
        return this.colComparator;
    }
}
