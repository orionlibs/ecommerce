package de.hybris.platform.cockpit.components.sectionpanel;

import com.google.common.base.Preconditions;
import de.hybris.platform.cockpit.components.dialog.DescriptionTooltip;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.session.impl.EditorPropertyRow;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkmax.event.PortalMoveEvent;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public final class SectionPanel extends HtmlMacroComponent implements DesktopRemovalAwareComponent
{
    private static final String SECTION_ROW_ATTRIBUTE = "sectionRow";
    private static final Logger LOG = LoggerFactory.getLogger(SectionPanel.class);
    public static final String CLASS_SECTION_SELECTED_ROW = "section_selected_row";
    public static final String FOCUSABLE_COMPONENT = "focusableComponent";
    private static final String INFO_CONTAINER = "infoContainer";
    private static final String LABEL_CONTAINER = "labelContainer";
    private static final String MESSAGE_CONTAINER = "messageContainer";
    private static final String SECTION_CONTAINER_DIV = "sectionContainerDiv";
    private static final String MAIN_BORDER_LAYOUT = "mainBorderlayout";
    public static final String ON_SECTION_REMOVED = "onSectionRemoved";
    public static final String ON_SECTION_MOVED = "onSectionMoved";
    public static final String ON_SECTION_ADDED = "onSectionAdded";
    public static final String ON_SECTION_HIDE = "onSectionHide";
    public static final String ON_SECTION_LABEL_CHANGE = "onSectionLabelChange";
    public static final String ON_SECTION_SHOW = "onSectionShow";
    public static final String ON_SECTION_OPEN = "onSectionOpen";
    public static final String ON_SECTION_CLOSED = "onSectionClosed";
    public static final String ON_SECTIONS_EXPANDED = "onSectionsExpanded";
    public static final String ON_SECTIONS_COLLAPSED = "onSectionsCollapsed";
    public static final String ON_ROW_HIDE = "onRowHide";
    public static final String ON_ROW_SHOW = "onRowShow";
    public static final String ON_ROW_MOVED = "onRowMoved";
    public static final String ON_ALL_ROWS_SHOW = "onAllRowsShow";
    public static final String ON_ALL_SECTIONS_SHOW = "onAllSectionsShow";
    public static final String ON_MESSAGE_CLICKED = "onMessageClicked";
    public static final String ON_MESSAGE_HIDE = "onMessageHide";
    public static final String ON_ALL_MESSAGE_SHOW = "onAllMessageShow";
    public static final String SECTION_ROW = "sectionRow";
    public static final String SECTION_NAME = "sectionName";
    protected static final String PERSP_TAG = "persp";
    protected static final String PERSP_ID = "admincockpit.perspective.admincockpit";
    protected static final String EVENT_TAG = "events";
    protected static final String NAVIGATION_EVENT = "activation";
    protected static final String PNAV_CONSTRAINT = "act-item";
    private SectionRenderer sectionRenderer = (SectionRenderer)new DefaultSectionRenderer();
    private SectionRowRenderer sectionRowRenderer;
    private MessageBoxRenderer messageBoxRenderer = (MessageBoxRenderer)new DefaultMessageBoxRenderer();
    private SectionPanelLabelRenderer sectionPanelLabelRenderer = null;
    private SectionPanelModel model;
    private boolean flatSectionLayout = false;
    private String rowLabelWidth = "10em";
    private boolean alternateOpen = false;
    private SectionRow selectedRow;
    private boolean lazyLoad = false;
    private final Map<Section, SectionComponent> sectionComponents = new HashMap<>();
    private final Map<SectionRow, SectionRowComponent> sectionRowComponents = new HashMap<>();
    private final Map<Section, Set<SectionRow>> section2RowsMap = new HashMap<>();
    private final SectionPanelListener modelListener = (SectionPanelListener)new MyModelListener(this);
    private Component sectionContainer;
    private boolean editMode = false;
    private boolean sectionsChangeAllowed = false;


    protected void registerSectionComponent(Section section, SectionComponent comp)
    {
        this.sectionComponents.put(section, comp);
    }


    public SectionComponent getSectionComponent(Section section)
    {
        return this.sectionComponents.get(section);
    }


    protected void registerRowComponent(Section section, SectionRow row, SectionRowComponent comp)
    {
        this.sectionRowComponents.put(row, comp);
        Set<SectionRow> rows = this.section2RowsMap.get(section);
        if(rows == null)
        {
            this.section2RowsMap.put(section, rows = new HashSet<>());
        }
        rows.add(row);
    }


    protected void unregisterRowComponents(Section section)
    {
        Set<SectionRow> rows = this.section2RowsMap.get(section);
        if(rows != null)
        {
            for(SectionRow sr : rows)
            {
                this.sectionRowComponents.remove(sr);
            }
        }
        this.section2RowsMap.remove(section);
    }


    protected SectionRowComponent getRowComponent(SectionRow row)
    {
        return this.sectionRowComponents.get(row);
    }


    protected Menupopup getContextMenu()
    {
        return (Menupopup)getFirstChild().getFirstChild();
    }


    protected Component getSectionContainer()
    {
        return this.sectionContainer;
    }


    protected Component getSectionContainerDiv()
    {
        return getFellowIfAny("sectionContainerDiv");
    }


    protected Component getMainBorderlayout()
    {
        return getFellow("mainBorderlayout");
    }


    protected Component getMessageContainer()
    {
        return getFellowIfAny("messageContainer");
    }


    protected Component getLabelContainer()
    {
        return getFellowIfAny("labelContainer");
    }


    protected Component getInfoContainer()
    {
        return getFellowIfAny("infoContainer");
    }


    public HtmlBasedComponent createRowComponent(Section section, SectionRow row, Component rowContainer, Component sectionContentContainer)
    {
        return (HtmlBasedComponent)createRowComponent(section, row, rowContainer, sectionContentContainer, getRowLabelWidth());
    }


    protected void renderRowValue(Component valueContainer, SectionRow row)
    {
        renderRowValue(valueContainer, row, true);
    }


    protected void renderRowValue(Component valueContainer, SectionRow row, boolean useRowRenderer)
    {
        SectionRowRenderer renderer = getSectionRowRenderer();
        if(useRowRenderer && renderer != null)
        {
            Section section = getSection(row);
            Map<String, Object> extendedContext = new HashMap<>();
            extendedContext.putAll(getModel().getContext());
            if(section != null)
            {
                extendedContext.put("sectionName", section.getLabel());
            }
            renderer.render(this, valueContainer, row, extendedContext);
        }
        else
        {
            Label naLabel = new Label(row.toString());
            naLabel.setStyle("color:red");
            naLabel.setParent(valueContainer);
        }
    }


    public SectionRowComponent createRowComponent(Section section, SectionRow row, Component rowContainer, Component sectionContentContainer, String labelWidth)
    {
        Hbox hbox;
        SectionRowComponent rowDiv = new SectionRowComponent(this);
        EditorRowConfiguration rowConfiguration = ((EditorPropertyRow)row).getRowConfiguration();
        PropertyDescriptor propertyDescriptor = rowConfiguration.getPropertyDescriptor();
        String attrDescription = propertyDescriptor.getDescription();
        DescriptionTooltip descTooltip = new DescriptionTooltip(attrDescription);
        String layoutParam = rowConfiguration.getParameter("layout");
        Box rowBox = null;
        if("wide".equalsIgnoreCase(layoutParam) || isCollectionReference(propertyDescriptor))
        {
            Vbox vbox = new Vbox();
            vbox.setStyle("table-layout:fixed;");
            vbox.setAlign("left");
            vbox.setWidth("100%");
        }
        else
        {
            hbox = new Hbox();
            String rWidths = descTooltip.isRender().booleanValue() ? (labelWidth + ",none,18px") : (labelWidth + ",none");
            hbox.setWidth("100%");
            hbox.setStyle("table-layout:fixed;");
            hbox.setWidths(rWidths);
        }
        rowDiv.getContainer().appendChild((Component)hbox);
        if(rowContainer != null)
        {
            rowDiv.setParent(rowContainer);
        }
        registerRowComponent(section, row, rowDiv);
        rowDiv.setSectionRow(row);
        Menupopup menuPopUp = createRowContextMenu(row, section);
        hbox.setContext((Popup)menuPopUp);
        Div labelContainer = new Div();
        labelContainer.setSclass("editorrowlabelcontainer");
        labelContainer.setParent((Component)hbox);
        if("wide".equalsIgnoreCase(layoutParam) || isCollectionReference(propertyDescriptor))
        {
            labelContainer.setWidth("100%");
        }
        Toolbarbutton toolbarButton = new Toolbarbutton("", "/cockpit/images/remove.png");
        toolbarButton.setSclass("sectionrow_remove_btn");
        toolbarButton.setTooltiptext(Labels.getLabel("section.button.remove.tooltip"));
        toolbarButton.addEventListener("onClick", (EventListener)new Object(this, section, row));
        toolbarButton.setParent((Component)labelContainer);
        Label lable = new Label(row.getLabel() + ": ");
        lable.setTooltiptext(row.getValueInfo());
        lable.setParent((Component)labelContainer);
        boolean isSelected = row.equals(getSelectedRow());
        if(isSelected)
        {
            rowDiv.setSclass("section_selected_row");
        }
        rowDiv.addEventListener("onClick", (EventListener)new Object(this, row));
        if(isEditMode())
        {
            menuPopUp.setParent((Component)labelContainer);
        }
        Div valueContainer = createRowValueContainer(row);
        if("wide".equalsIgnoreCase(layoutParam) || isCollectionReference(propertyDescriptor))
        {
            valueContainer.setStyle("padding-left:18px;");
        }
        rowDiv.setValueContainer((Component)valueContainer);
        valueContainer.setParent((Component)hbox);
        if(Boolean.TRUE.equals(descTooltip.isRender()))
        {
            descTooltip.setParent((Component)hbox);
        }
        renderRowValue((Component)valueContainer, row);
        if(isEditMode())
        {
            labelContainer.setDraggable(getId() + "_sectionrow");
            UITools.modifySClass((HtmlBasedComponent)labelContainer, "draggableRow", true);
        }
        labelContainer.setAttribute("sectionRow", row);
        rowDiv.setDroppable(getId() + "_sectionrow");
        rowDiv.addEventListener("onDrop", (EventListener)new Object(this, section, rowDiv));
        if(!row.isEditable())
        {
            UITools.modifySClass((HtmlBasedComponent)hbox, "write_protected_row", true);
        }
        return rowDiv;
    }


    protected Div createRowValueContainer(SectionRow row)
    {
        Div ret = new Div();
        ret.setSclass("section-row-value-container");
        return ret;
    }


    protected Menupopup createRowContextMenu(SectionRow row, Section section)
    {
        Menupopup ret = new Menupopup();
        ret.setSclass("sectionPanelMenu");
        ret.addEventListener("onOpen", (EventListener)new Object(this, section, row, ret));
        return ret;
    }


    protected void collapseAllSections(SectionComponent exclude)
    {
        if(getSectionContainer() != null)
        {
            for(Object o : getSectionContainer().getChildren())
            {
                SectionComponent sectionComponent = (SectionComponent)o;
                if(sectionComponent.isVisible() && sectionComponent.isOpen() && !sectionComponent.equals(exclude) &&
                                !sectionComponent.isAlwaysOpen())
                {
                    sectionComponent.setOpen(false);
                }
            }
        }
        else
        {
            LOG.error("Section container is null.");
        }
    }


    protected void expandAllSections()
    {
        if(getSectionContainer() != null)
        {
            for(Object o : getSectionContainer().getChildren())
            {
                SectionComponent sectionComponent = (SectionComponent)o;
                if(sectionComponent.isVisible() && !sectionComponent.isOpen())
                {
                    sectionComponent.setOpen(true);
                }
            }
        }
        else
        {
            LOG.error("Section container is null.");
        }
    }


    protected void appendPanelSpecificMenuItems(Menupopup menuPopUp)
    {
        Menuitem menuItem = new Menuitem(Labels.getLabel("general.expandall"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuPopUp.appendChild((Component)menuItem);
        menuItem = new Menuitem(Labels.getLabel("general.collapseall"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuPopUp.appendChild((Component)menuItem);
        if(isEditMode())
        {
            Menu menu = new Menu(Labels.getLabel("sectionmenu.showsections"));
            Menupopup nmp = new Menupopup();
            menu.appendChild((Component)nmp);
            menuPopUp.appendChild((Component)menu);
            menuPopUp.addEventListener("onOpen", (EventListener)new Object(this, menu, nmp));
            if(isSectionsChangeAllowed())
            {
                menuItem = new Menuitem(Labels.getLabel("sectionmenu.addsection"));
                menuItem.addEventListener("onClick", (EventListener)new Object(this));
                menuPopUp.appendChild((Component)menuItem);
            }
        }
        menuItem = new Menuitem(Labels.getLabel("section.editor.contextmenuedit"));
        menuItem.setSclass("menu-item-switch-chkbox");
        menuItem.setCheckmark(true);
        menuItem.setChecked(isEditMode());
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuPopUp.appendChild((Component)menuItem);
    }


    protected void appendSectionSpecificMenuItems(Menupopup menuPopUp, Section section)
    {
        if(!isEditMode())
        {
            return;
        }
        Menuitem menuItem = new Menuitem(Labels.getLabel("sectionmenu.hidesection"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this, section));
        menuPopUp.appendChild((Component)menuItem);
        if(section instanceof EditableSection && isSectionsChangeAllowed())
        {
            menuItem = new Menuitem(Labels.getLabel("sectionmenu.renamesection"));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, section));
            menuPopUp.appendChild((Component)menuItem);
            menuItem = new Menuitem(Labels.getLabel("sectionmenu.removesection"));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, section));
            if(getModel().getSections() != null && getModel().getSections().size() > 1)
            {
                menuPopUp.appendChild((Component)menuItem);
            }
        }
        if(getModel() instanceof RowlayoutSectionPanelModel)
        {
            menuPopUp.appendChild((Component)createRowsMenu(section));
        }
    }


    protected Menu createRowsMenu(Section section)
    {
        Menu menu = new Menu(Labels.getLabel("sectionmenu.showrows"));
        Menupopup nmp = new Menupopup();
        nmp.appendChild((Component)new Menuitem("-------------------"));
        nmp.addEventListener("onOpen", (EventListener)new Object(this, section, nmp));
        menu.appendChild((Component)nmp);
        return menu;
    }


    protected void resetRowsMenu(SectionPanel panel, Section section, Menupopup nmp)
    {
        nmp.getChildren().clear();
        Preconditions.checkArgument(panel.getModel() instanceof RowlayoutSectionPanelModel);
        RowlayoutSectionPanelModel model = (RowlayoutSectionPanelModel)panel.getModel();
        Menuitem showall = new Menuitem(Labels.getLabel("sectionmenu.showall"));
        showall.addEventListener("onClick", (EventListener)new Object(this, panel, section));
        showall.setParent((Component)nmp);
        nmp.appendChild((Component)new Menuseparator());
        for(SectionRow row : model.getRows(section))
        {
            if(!row.isVisible())
            {
                Menuitem menuItem = new Menuitem(row.getLabel());
                menuItem.addEventListener("onClick", (EventListener)new Object(this, panel, section, row));
                menuItem.setParent((Component)nmp);
            }
        }
        Menu otherSectionMenu = new Menu(Labels.getLabel("sectionmenu.fromothersections"));
        Menupopup otherMenuPopup = new Menupopup();
        otherMenuPopup.setParent((Component)otherSectionMenu);
        otherSectionMenu.setParent((Component)nmp);
        for(Section sec : panel.getModel().getSections())
        {
            if(sec.equals(section))
            {
                continue;
            }
            for(SectionRow row : model.getRows(sec))
            {
                if(row.isVisible())
                {
                    continue;
                }
                Menuitem menuItem = new Menuitem(sec.getLabel() + " : " + sec.getLabel());
                menuItem.addEventListener("onClick", (EventListener)new Object(this, panel, section, row));
                menuItem.setParent((Component)otherMenuPopup);
            }
        }
    }


    protected void appendViewSettingsMenu(Menupopup menuPopUp)
    {
        Menu menu = new Menu(Labels.getLabel("sectionmenu.viewsettings"));
        Menupopup nmp = new Menupopup();
        menu.appendChild((Component)nmp);
        Menuitem menuItem = new Menuitem(Labels.getLabel("sectionmenu.onlyoneopened"));
        menuItem.addEventListener("onClick", (EventListener)new Object(this));
        menuItem.setCheckmark(true);
        menuItem.setChecked(isAlternateOpen());
        menuItem.setAutocheck(true);
        nmp.appendChild((Component)menuItem);
        menuPopUp.appendChild((Component)menu);
    }


    protected Menupopup createSectionContextMenu(Section section)
    {
        Menupopup ret = new Menupopup();
        appendSectionSpecificMenuItems(ret, section);
        ret.appendChild((Component)new Menuseparator());
        appendPanelSpecificMenuItems(ret);
        return ret;
    }


    public String getLabel()
    {
        return (getModel() != null) ? getModel().getLabel() : null;
    }


    public String getImageUrl()
    {
        return (getModel() != null) ? getModel().getImageUrl() : null;
    }


    public List<Message> getMessages()
    {
        return (getModel() != null) ? getModel().getMessages() : null;
    }


    protected List<SectionComponent> getAllSectionComponents()
    {
        List<SectionComponent> ret = new ArrayList<>();
        if(getSectionContainer() != null)
        {
            ret.addAll(getSectionContainer().getChildren());
        }
        else
        {
            LOG.error("Section container is null.");
        }
        return ret;
    }


    public void setSectionRowRenderer(SectionRowRenderer sectionRowRenderer)
    {
        this.sectionRowRenderer = sectionRowRenderer;
    }


    public SectionRowRenderer getSectionRowRenderer()
    {
        return this.sectionRowRenderer;
    }


    public SectionRenderer getSectionRenderer()
    {
        return this.sectionRenderer;
    }


    public void setSectionRenderer(SectionRenderer sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
    }


    public void setModel(SectionPanelModel model)
    {
        if(this.model != model && (this.model == null || !this.model.equals(model)))
        {
            if(this.model != null)
            {
                this.model.removeModelListener(this.modelListener);
            }
            this.model = model;
            if(this.model != null)
            {
                this.model.addModelListener(this.modelListener);
            }
            resetView();
        }
    }


    protected void resetSectionMenu(Menu menu)
    {
        Menupopup ret = menu.getMenupopup();
        ret.getChildren().clear();
        Menuitem showall = new Menuitem(Labels.getLabel("sectionmenu.showall"));
        showall.addEventListener("onClick", (EventListener)new Object(this));
        showall.setParent((Component)ret);
        ret.appendChild((Component)new Menuseparator());
        for(Section section : getModel().getSections())
        {
            if(section.isVisible())
            {
                continue;
            }
            Menuitem menuItem = new Menuitem(section.getLabel());
            menuItem.addEventListener("onClick", (EventListener)new Object(this, section));
            menuItem.setParent((Component)ret);
        }
    }


    protected void resetInfoContainer()
    {
        if(getInfoContainer() == null)
        {
            return;
        }
        Component labelContainer = getLabelContainer();
        boolean showInfoContainer = false;
        if(labelContainer != null)
        {
            Div newLabelContainer = new Div();
            if(getLabel() != null && getLabel().trim().length() > 0)
            {
                showInfoContainer = true;
                getSectionPanelLabelRenderer().render(getLabel(), getImageUrl(), (Component)newLabelContainer);
            }
            UITools.detachChildren(labelContainer);
            for(Object o : newLabelContainer.getChildren().toArray())
            {
                ((Component)o).setParent(labelContainer);
            }
        }
        Component msgContainer = getMessageContainer();
        if(msgContainer != null)
        {
            msgContainer.getChildren().clear();
            if(getMessages() != null && getMessages().size() > 0 && getMessageBoxRenderer() != null)
            {
                showInfoContainer = true;
                getMessageBoxRenderer().render(this, msgContainer, getMessages());
            }
        }
        if(getMainBorderlayout() instanceof Borderlayout)
        {
            ((Borderlayout)getMainBorderlayout()).getNorth().setVisible(showInfoContainer);
            try
            {
                ((Borderlayout)getMainBorderlayout()).resize();
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        else
        {
            LOG.error("Main borderlayout is null.");
        }
    }


    protected void updateSectionPositions()
    {
        if(getSectionContainer() != null)
        {
            getSectionContainer().getChildren().clear();
            for(Section section : getModel().getSections())
            {
                SectionComponent comp = getSectionComponent(section);
                if(comp != null && section.isVisible())
                {
                    getSectionContainer().appendChild((Component)comp);
                }
            }
        }
        else
        {
            LOG.error("Section container is null.");
        }
    }


    public void onPortalMoved(PortalMoveEvent event)
    {
        Panel panel = event.getDragged();
        if(panel instanceof SectionComponent)
        {
            SectionEvent evt = new SectionEvent("onSectionMoved", (Component)this, ((SectionComponent)panel).getSection());
            evt.setIndex(getSectionContainer().getChildren().indexOf(panel));
            Events.postEvent((Event)evt);
        }
    }


    public void onDrop(DropEvent event)
    {
        Component dragged = event.getDragged();
        if(dragged instanceof SectionComponent)
        {
            SectionEvent evt = new SectionEvent("onSectionMoved", (Component)this, ((SectionComponent)dragged).getSection());
            evt.setIndex(-1);
            Events.postEvent((Event)evt);
        }
    }


    public void onShowAllMessages()
    {
        Events.postEvent((Event)new SectionPanelEvent("onAllMessageShow", (Component)this));
    }


    public void afterCompose()
    {
        super.afterCompose();
        if(Boolean.TRUE.equals(getDynamicProperty("resetPending")))
        {
            setDynamicProperty("resetPending", null);
            resetView();
        }
        if(isFlatSectionLayout() && getSectionContainer() != null)
        {
            UITools.modifySClass((HtmlBasedComponent)getSectionContainer(), "topflat", true);
        }
    }


    protected void resetView()
    {
        try
        {
            if(getSectionContainerDiv() != null)
            {
                updateMode(getSectionContainerDiv());
                this.sectionComponents.clear();
                this.sectionRowComponents.clear();
                this.section2RowsMap.clear();
                this.selectedRow = null;
                if(getSectionContainer() != null)
                {
                    getSectionContainer().getChildren().clear();
                    getContextMenu().getChildren().clear();
                    appendPanelSpecificMenuItems(getContextMenu());
                    getSectionContainer().addEventListener("onClick", (EventListener)new Object(this));
                }
                resetInfoContainer();
                if(getModel() != null)
                {
                    for(int i = 0; i < getModel().getSections().size(); i++)
                    {
                        Section section = getModel().getSections().get(i);
                        doSectionAdded(section);
                    }
                }
                Events.postEvent("onResetView", (Component)this, null);
            }
            else
            {
                setDynamicProperty("resetPending", Boolean.TRUE);
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    public SectionPanelModel getModel()
    {
        return this.model;
    }


    protected void doSectionRemoved(Section section)
    {
        unregisterRowComponents(section);
        SectionComponent sectionComponent = this.sectionComponents.remove(section);
        if(sectionComponent != null)
        {
            sectionComponent.detach();
        }
    }


    protected void doSectionHidden(Section section)
    {
        getSectionComponent(section).detach();
    }


    protected void doSectionShown(Section section)
    {
        SectionComponent sectionComponent = getSectionComponent(section);
        if(sectionComponent == null)
        {
            sectionComponent = new SectionComponent(this, section);
            sectionComponent.setInitialized(true);
            sectionComponent.render();
            Menupopup menupopup = createSectionContextMenu(section);
            sectionComponent.getCaption().appendChild((Component)menupopup);
            sectionComponent.getCaption().setContext((Popup)menupopup);
            this.sectionComponents.put(section, sectionComponent);
            sectionComponent.setOpen(section.isInitialOpen());
        }
        Component cont = getSectionContainer();
        if(cont != null)
        {
            int index = getModel().getSections().indexOf(section);
            Component after = (cont.getChildren().size() > index) ? cont.getChildren().get(index) : null;
            cont.insertBefore((Component)sectionComponent, after);
        }
        else
        {
            LOG.error("Section container is null.");
        }
    }


    protected void doSectionUpdated(Section section)
    {
        SectionComponent sectionComponent = getSectionComponent(section);
        if(sectionComponent != null)
        {
            sectionComponent.render();
        }
    }


    protected void doSectionAdded(Section section)
    {
        if(section.isVisible())
        {
            SectionComponent sectionComponent = new SectionComponent(this, section);
            if(getSectionContainer() != null)
            {
                getSectionContainer().appendChild((Component)sectionComponent);
            }
            else
            {
                LOG.error("Section container is null.");
            }
            Menupopup menupopup = createSectionContextMenu(section);
            sectionComponent.getCaption().appendChild((Component)menupopup);
            sectionComponent.getCaption().setContext((Popup)menupopup);
            this.sectionComponents.put(section, sectionComponent);
            sectionComponent.setOpen((section.isInitialOpen() || section.isOpen()));
        }
        else
        {
            this.sectionComponents.remove(section);
        }
    }


    protected Section getSection(SectionRow row)
    {
        for(Map.Entry<Section, Set<SectionRow>> entry : this.section2RowsMap.entrySet())
        {
            if(((Set)entry.getValue()).contains(row))
            {
                return entry.getKey();
            }
        }
        return null;
    }


    protected void doRowUpdated(SectionRow row)
    {
        SectionRowComponent rowComponent = getRowComponent(row);
        if(rowComponent == null)
        {
            Section section = getSection(row);
            if(section != null)
            {
                SectionComponent sectionComponent = getSectionComponent(section);
                if(sectionComponent != null && sectionComponent.isInitialized())
                {
                    LOG.error("Could not update row " + row + " because corresponding view component doesn't exist.");
                }
            }
        }
        else
        {
            Component valueContainer = rowComponent.getValueContainer();
            if(valueContainer != null && (!UITools.isFromOtherDesktop(valueContainer) || valueContainer.getDesktop() == null))
            {
                valueContainer.getChildren().clear();
                renderRowValue(valueContainer, row);
            }
        }
    }


    protected void doRowHidden(SectionRow row)
    {
        getRowComponent(row).setVisible(false);
    }


    protected void doRowShown(SectionRow row)
    {
        SectionRowComponent sectionRowComponent = getRowComponent(row);
        if(sectionRowComponent != null)
        {
            sectionRowComponent.setVisible(true);
        }
    }


    public void onCtrlKey(KeyEvent keyEvent)
    {
        int keycode = keyEvent.getKeyCode();
        if(keycode == 40)
        {
            handleKeyDown();
        }
    }


    public void handleKeyDown()
    {
    }


    protected void highlightRow(SectionRow sectionRow)
    {
    }


    public void setRowStatus(SectionRow row, int status, String localizedMsg)
    {
        SectionRowComponent rowComponent = getRowComponent(row);
        if(rowComponent != null)
        {
            rowComponent.setStatus(status, localizedMsg);
        }
    }


    public void setSectionHeaderStatus(Section section, int status)
    {
        SectionComponent sectionComponent = getSectionComponent(section);
        if(sectionComponent != null)
        {
            sectionComponent.markHeader(status);
        }
    }


    public void attacheValidationMenupopup(SectionRow row, Menupopup menuPopup)
    {
        SectionRowComponent rowComponent = getRowComponent(row);
        if(rowComponent != null)
        {
            rowComponent.getContainer().appendChild((Component)menuPopup);
            rowComponent.getValidationIcon().setPopup((Popup)menuPopup);
        }
    }


    public Set<SectionRow> getAllRows()
    {
        return Collections.unmodifiableSet(this.sectionRowComponents.keySet());
    }


    protected SectionRow getNextVisibleRow(SectionRow row)
    {
        SectionRow ret = null;
        if(getModel() instanceof RowlayoutSectionPanelModel)
        {
            ret = ((RowlayoutSectionPanelModel)getModel()).getNextVisibleRow(row);
        }
        return ret;
    }


    public void focusNext(SectionRow row, String nextIso)
    {
        SectionRow nextRow = row;
        if(nextIso == null)
        {
            nextRow = getNextVisibleRow(row);
        }
        if(nextRow != null)
        {
            SectionRow.FocusListener focusListener = nextRow.getFocusListener(nextIso);
            if(focusListener != null)
            {
                focusListener.onFocus();
            }
        }
    }


    public void setSelectedRow(SectionRow selectedRow)
    {
        setSelectedRow(selectedRow, true);
    }


    public void setSelectedRow(SectionRow selectedRow, boolean highlight)
    {
        if(this.selectedRow == null || !this.selectedRow.equals(selectedRow))
        {
            this.selectedRow = selectedRow;
            HtmlBasedComponent rowDiv = (HtmlBasedComponent)this.sectionRowComponents.get(selectedRow);
            if(rowDiv != null)
            {
                Clients.evalJavaScript("var item = document.getElementById('" + rowDiv.getUuid() + "'); ed_lastselected.className='sectionRowComponent'; item.className='section_selected_row'; ed_lastselected=item;");
            }
        }
    }


    public SectionRow getSelectedRow()
    {
        return this.selectedRow;
    }


    public void setMessageBoxRenderer(MessageBoxRenderer messageBoxRenderer)
    {
        this.messageBoxRenderer = messageBoxRenderer;
    }


    public MessageBoxRenderer getMessageBoxRenderer()
    {
        return this.messageBoxRenderer;
    }


    public void setAlternateOpen(boolean alternateOpen)
    {
        this.alternateOpen = alternateOpen;
    }


    public boolean isAlternateOpen()
    {
        return this.alternateOpen;
    }


    public void setFlatSectionLayout(boolean flatSectionLayout)
    {
        this.flatSectionLayout = flatSectionLayout;
        if(getSectionContainer() != null)
        {
            if(this.flatSectionLayout)
            {
                UITools.modifySClass((HtmlBasedComponent)getSectionContainer(), "topflat", true);
            }
            else
            {
                UITools.modifySClass((HtmlBasedComponent)getSectionContainer(), "topflat", false);
            }
        }
    }


    public boolean isFlatSectionLayout()
    {
        return this.flatSectionLayout;
    }


    public void detach()
    {
        super.detach();
        if(this.model != null)
        {
            this.model.removeModelListener(this.modelListener);
        }
    }


    protected void updateMode(Component parent)
    {
        parent.getChildren().clear();
        if(isEditMode())
        {
            Portallayout portallayout = new Portallayout();
            portallayout.setParent(parent);
            portallayout.setSclass("sectionPanelPortallayout");
            Portalchildren portalchildren = new Portalchildren();
            portalchildren.setParent((Component)portallayout);
            portalchildren.setId("sectionContainer");
            this.sectionContainer = (Component)portalchildren;
            portalchildren.setWidth("100%");
            portalchildren.setSclass("sectionPanelContainer");
            portallayout.addEventListener("onPortalMove", (EventListener)new Object(this));
        }
        else
        {
            Div panelContainer = new Div();
            panelContainer.setId("sectionContainer");
            this.sectionContainer = (Component)panelContainer;
            parent.appendChild((Component)panelContainer);
        }
        UITools.modifySClass((HtmlBasedComponent)getSectionContainer(), "topflat", this.flatSectionLayout);
    }


    protected boolean isCollectionReference(PropertyDescriptor propertyDescriptor)
    {
        return ("REFERENCE".equals(propertyDescriptor.getEditorType()) &&
                        !PropertyDescriptor.Multiplicity.SINGLE.equals(propertyDescriptor.getMultiplicity()));
    }


    public void desktopRemoved(Desktop desktop)
    {
        if(this.model != null)
        {
            this.model.removeModelListener(this.modelListener);
        }
    }


    public void onPageDetached(Page page)
    {
        super.onPageDetached(page);
    }


    public void setRowLabelWidth(String rowLabelWidth)
    {
        this.rowLabelWidth = rowLabelWidth;
    }


    public String getRowLabelWidth()
    {
        return this.rowLabelWidth;
    }


    public void setSectionPanelLabelRenderer(SectionPanelLabelRenderer sectionPanelLabelRenderer)
    {
        this.sectionPanelLabelRenderer = sectionPanelLabelRenderer;
    }


    public SectionPanelLabelRenderer getSectionPanelLabelRenderer()
    {
        return (this.sectionPanelLabelRenderer != null) ? this.sectionPanelLabelRenderer : (SectionPanelLabelRenderer)new MyLabelRenderer(this);
    }


    public void setLazyLoad(boolean lazyLoad)
    {
        this.lazyLoad = lazyLoad;
    }


    public boolean isLazyLoad()
    {
        return this.lazyLoad;
    }


    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }


    public boolean isEditMode()
    {
        return this.editMode;
    }


    public void setSectionsChangeAllowed(boolean sectionsChangeAllowed)
    {
        this.sectionsChangeAllowed = sectionsChangeAllowed;
    }


    public boolean isSectionsChangeAllowed()
    {
        return this.sectionsChangeAllowed;
    }
}
