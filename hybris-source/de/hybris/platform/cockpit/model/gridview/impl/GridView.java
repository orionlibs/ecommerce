package de.hybris.platform.cockpit.model.gridview.impl;

import de.hybris.platform.cockpit.err.CockpitMarkAllException;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.AbstractItemView;
import de.hybris.platform.cockpit.model.general.impl.LoadingProgressContainer;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.gridview.GridViewListener;
import de.hybris.platform.cockpit.model.gridview.UIGridView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDraggedItem;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.impl.XulElement;

public class GridView extends AbstractItemView implements UIGridView
{
    private static final Logger LOG = LoggerFactory.getLogger(GridView.class);
    public static final String DEFAULT_GRIDVIEW_CONF = "gridView";
    public static final String ITEM_BOX_SIZE = "150px";
    protected MutableListModel model;
    protected boolean initialized;
    protected Div mainDiv;
    protected Checkbox focusComponent;
    private GridItemRenderer gridItemRenderer;
    private UIConfigurationService uiConfigurationService;
    private boolean scrollOnNextSelection = true;
    private TypeService typeService;
    private List<Integer> selectedIndexes = new ArrayList<>();
    private final List<Integer> activationIndexes = new ArrayList<>();
    private int lastSelectedIndex = -1;
    private final Set<GridViewListener> listeners = new HashSet<>();
    private String configContextCode = null;
    private ObjectTemplate rootTemplate;
    private final Map<TypedObject, Component> componentsMap = new HashMap<>();


    public String getConfigContextCode()
    {
        return StringUtils.isBlank(this.configContextCode) ? "gridView" : this.configContextCode;
    }


    public void setConfigContextCode(String code)
    {
        this.configContextCode = code;
    }


    public void addGridViewListener(GridViewListener listener)
    {
        this.listeners.add(listener);
    }


    public void removeGridViewListener(GridViewListener listener)
    {
        this.listeners.remove(listener);
    }


    public void setRootType(ObjectTemplate template)
    {
        this.rootTemplate = template;
    }


    public MutableListModel getModel()
    {
        return this.model;
    }


    public void setModel(MutableListModel model)
    {
        if(this.model != model)
        {
            this.model = model;
            if(this.model == null)
            {
                this.initialized = false;
            }
            else
            {
                initialize();
            }
        }
    }


    public void updateActivation()
    {
        if(this.initialized)
        {
            renderActivation(false);
            this.activationIndexes.clear();
            for(TypedObject item : getModel().getActiveItems())
            {
                Component comp = this.componentsMap.get(item);
                int index = this.mainDiv.getChildren().indexOf(comp);
                this.activationIndexes.add(Integer.valueOf(index));
            }
            renderActivation(true);
        }
    }


    public void updateActiveItems()
    {
        TypedObject activeItem = getModel().getActiveItem();
        rerenderItem(activeItem);
    }


    protected void rerenderItem(TypedObject item)
    {
        if(item != null)
        {
            Component outer = this.componentsMap.get(item);
            if(outer != null)
            {
                Component comp = outer.getFirstChild();
                if(comp != null && this.rootTemplate != null)
                {
                    comp.getChildren().clear();
                    GridViewConfiguration configuration = (GridViewConfiguration)getUIConfigurationService().getComponentConfiguration(
                                    getTypeService().getBestTemplate(item), getConfigContextCode(), GridViewConfiguration.class);
                    getGridItemRenderer().render(item, comp, configuration, (ListComponentModel)getModel(),
                                    getDDContext().getWrapper().getDraggedItem(comp), getDDContext().getWrapper());
                }
            }
        }
    }


    public int updateItem(TypedObject item, Set<PropertyDescriptor> propertyDescriptors)
    {
        int ret = updateItemInternal(item, propertyDescriptors);
        Collection<TypedObject> additionalItemsToUpdate = getModel().getAdditionalItemsToUpdate(item);
        for(TypedObject typedObject : additionalItemsToUpdate)
        {
            updateItemInternal(typedObject, null);
        }
        return ret;
    }


    private int updateItemInternal(TypedObject item, Set<PropertyDescriptor> propertyDescriptors)
    {
        rerenderItem(item);
        updateSelection();
        updateActivation();
        return 1;
    }


    protected void renderItemAsSelected(HtmlBasedComponent comp, boolean selected)
    {
        HtmlBasedComponent parent = (HtmlBasedComponent)comp.getParent();
        UITools.modifySClass(parent, "selectedGridItem", selected);
    }


    protected void renderItemAsActive(HtmlBasedComponent comp, boolean active)
    {
        HtmlBasedComponent parent = (HtmlBasedComponent)comp.getParent();
        UITools.modifySClass(parent, "activeGridItem", active);
    }


    protected void renderSelection(boolean selected)
    {
        List<HtmlBasedComponent> children = this.mainDiv.getChildren();
        for(Integer index : this.selectedIndexes)
        {
            if(children.size() > index.intValue() && index.intValue() >= 0)
            {
                HtmlBasedComponent comp = (HtmlBasedComponent)((HtmlBasedComponent)children.get(index.intValue())).getFirstChild();
                if(comp != null)
                {
                    if(this.selectedIndexes.size() > 1)
                    {
                        UITools.modifySClass(comp, "multiSelectedItem", true);
                    }
                    else
                    {
                        comp.setSclass("itemBox");
                    }
                    if(comp.getLastChild() instanceof AbstractItemView.LoadImage)
                    {
                        comp.getLastChild().detach();
                    }
                    renderItemAsSelected(comp, selected);
                }
            }
        }
    }


    protected void renderActivation(boolean selected)
    {
        List<HtmlBasedComponent> children = this.mainDiv.getChildren();
        for(Integer index : this.activationIndexes)
        {
            if(children.size() > index.intValue() && index.intValue() >= 0)
            {
                renderItemAsActive((HtmlBasedComponent)((HtmlBasedComponent)children.get(index.intValue())).getFirstChild(), selected);
            }
        }
    }


    protected void appendScrollIntoViewComponent(HtmlBasedComponent cell)
    {
        if(cell.getLastChild() instanceof AbstractItemView.LoadImage)
        {
            cell.getLastChild().detach();
        }
        AbstractItemView.LoadImage loadImage = new AbstractItemView.LoadImage(this);
        loadImage.setSrc("hack");
        loadImage.setSclass("autoscroll_hack_image");
        loadImage.setAction("onerror: scrollIntoViewIfNeeded(this);");
        cell.appendChild((Component)loadImage);
    }


    public void updateSelection()
    {
        renderSelection(false);
        this.selectedIndexes = getModel().getSelectedIndexes();
        renderSelection(true);
        if(this.selectedIndexes.size() == 1)
        {
            this.lastSelectedIndex = ((Integer)this.selectedIndexes.get(0)).intValue();
        }
        TypedObject currentSelectedObject = getItemAt(this.lastSelectedIndex);
        Component outer = this.componentsMap.get(currentSelectedObject);
        if(outer != null)
        {
            Component component = outer.getFirstChild();
            if(component != null && this.scrollOnNextSelection)
            {
                appendScrollIntoViewComponent((HtmlBasedComponent)component);
            }
        }
        this.scrollOnNextSelection = true;
    }


    public boolean initialize()
    {
        this.initialized = false;
        this.loadingProgressContainer = new LoadingProgressContainer(false);
        if(getModel() != null)
        {
            getChildren().clear();
            Div conDiv = new Div();
            conDiv.setStyle("width: 100%; height: 100%; overflow-y: auto; position: relative;");
            this.mainDiv = new Div();
            this.mainDiv.setStyle("text-align: left; position: relative; padding-left: 3px; ");
            this.mainDiv.setSclass("clearfix gridviewMain");
            appendChild((Component)conDiv);
            conDiv.appendChild((Component)this.mainDiv);
            this.focusComponent = new Checkbox();
            appendChild((Component)this.focusComponent);
            this.focusComponent.setSclass("plainBtn");
            this.focusComponent.setZindex(-10);
            this.focusComponent.setStyle("position:absolute;top:-10000px;z-index:-10");
            String focusAction = "onkeydown: return listEditorSendKeypress(this,event);";
            this.focusComponent.setAction("onkeydown: return listEditorSendKeypress(this,event);");
            this.focusComponent.addEventListener("onUser", (EventListener)new Object(this));
            this.focusComponent.addEventListener("onFocusLater", (EventListener)new Object(this));
            conDiv.addEventListener("onClick", (EventListener)new Object(this));
            renderItems((Component)this.mainDiv);
            this.initialized = true;
            focusFocusComponent();
            updateActivation();
        }
        this.loadingProgressContainer.addEventListener("onContinueLoading", (EventListener)new Object(this));
        appendChild((Component)this.loadingProgressContainer);
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateItems();
            updateSelection();
            updateActivation();
            updateActiveItems();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void updateItems()
    {
        this.mainDiv.getChildren().clear();
        renderItems((Component)this.mainDiv);
        focusFocusComponent();
    }


    protected void focusFocusComponent()
    {
        Events.echoEvent("onFocusLater", (Component)this.focusComponent, null);
    }


    protected void renderItems(Component parent)
    {
        this.componentsMap.clear();
        if(this.loadingProgressContainer != null && getLazyloadPackageSize() > 0)
        {
            this.loadingProgressContainer.reset();
            this.loadingProgressContainer.setVisible(true);
        }
        continueRenderItems(parent, 0, getInitialPackageSize());
    }


    protected void continueRenderItems(Component parent, int index)
    {
        delayLazyload();
        continueRenderItems(parent, index, getLazyloadPackageSize());
    }


    protected void continueRenderItems(Component parent, int index, int numberOfItemsToRender)
    {
        List<? extends TypedObject> elements = getModel().getListModel().getElements();
        if(!elements.isEmpty())
        {
            if(this.rootTemplate == null)
            {
                LOG.warn("no template given");
            }
            else
            {
                Map<ObjectTemplate, GridViewConfiguration> configCache = new HashMap<>();
                if(getLazyloadPackageSize() <= 0)
                {
                    for(TypedObject element : elements)
                    {
                        ObjectTemplate bestTemplate = getTypeService().getBestTemplate(element);
                        GridViewConfiguration configuration = configCache.get(bestTemplate);
                        if(configuration == null)
                        {
                            configuration = (GridViewConfiguration)getUIConfigurationService().getComponentConfiguration(bestTemplate, getConfigContextCode(), GridViewConfiguration.class);
                            configCache.put(bestTemplate, configuration);
                        }
                        parent.appendChild((Component)createItemBox(element, configuration));
                    }
                }
                else
                {
                    int startIndex = index;
                    int endIndex = startIndex + numberOfItemsToRender;
                    boolean finishedLoading = false;
                    if(endIndex >= elements.size())
                    {
                        endIndex = elements.size();
                        finishedLoading = true;
                    }
                    for(int i = index; i < endIndex; i++)
                    {
                        TypedObject element = elements.get(i);
                        ObjectTemplate bestTemplate = getTypeService().getBestTemplate(element);
                        GridViewConfiguration configuration = configCache.get(bestTemplate);
                        if(configuration == null)
                        {
                            configuration = (GridViewConfiguration)getUIConfigurationService().getComponentConfiguration(bestTemplate, getConfigContextCode(), GridViewConfiguration.class);
                            configCache.put(bestTemplate, configuration);
                        }
                        parent.appendChild((Component)createItemBox(element, configuration));
                        if(element.equals(getModel().getActiveItem()))
                        {
                            updateActivation();
                        }
                    }
                    if(finishedLoading || !this.loadingProgressContainer.isVisible())
                    {
                        this.loadingProgressContainer.setVisible(false);
                        String invPref = UITools.getZKPreference("de.hybris.platform.cockpit.browserview.invalidateGridAfterLoading");
                        if(invPref != null)
                        {
                            boolean inv = false;
                            if(invPref.trim().equalsIgnoreCase("auto"))
                            {
                                inv = Executions.getCurrent().getUserAgent().toLowerCase().contains("msie");
                            }
                            else
                            {
                                inv = invPref.trim().equalsIgnoreCase("true");
                            }
                            if(inv)
                            {
                                this.mainDiv.invalidate();
                            }
                        }
                    }
                    else
                    {
                        this.loadingProgressContainer.setValues(endIndex, elements.size());
                        Events.echoEvent("onContinueLoading", (Component)this.loadingProgressContainer, null);
                    }
                }
            }
        }
        else
        {
            this.loadingProgressContainer.setVisible(false);
        }
    }


    protected void rangeSelect(int index)
    {
        List<Integer> selectedIndexes = new ArrayList<>();
        int startIndex = index;
        int endIndex = index;
        if(this.lastSelectedIndex >= 0)
        {
            startIndex = Math.min(this.lastSelectedIndex, index);
            endIndex = Math.max(this.lastSelectedIndex, index);
        }
        for(int i = startIndex; i <= endIndex; i++)
        {
            selectedIndexes.add(Integer.valueOf(i));
        }
        this.scrollOnNextSelection = false;
        fireChangeSelection(selectedIndexes);
    }


    protected HtmlBasedComponent createItemBox(TypedObject item, GridViewConfiguration config)
    {
        Div ret = new Div();
        ret.setSclass("itemOuter");
        Div innerDiv = new Div();
        innerDiv.setSclass("itemBox");
        UITools.addMacCommandClickListener((XulElement)ret);
        ret.addEventListener("onClick", (EventListener)new Object(this, ret));
        ret.addEventListener("onDoubleClick", (EventListener)new Object(this));
        ret.addEventListener("onLaterDblClick", (EventListener)new Object(this, item));
        DefaultDraggedItem defaultDraggedItem = null;
        if(getDDContext() != null)
        {
            innerDiv.setDraggable("PerspectiveDND");
            defaultDraggedItem = new DefaultDraggedItem(item, getDDContext().getBrowser(), null);
            getDDContext().getWrapper().attachDraggedItem((DraggedItem)defaultDraggedItem, (Component)innerDiv);
        }
        getGridItemRenderer().render(item, (Component)innerDiv, config, (ListComponentModel)getModel(), (DraggedItem)defaultDraggedItem, getDDContext().getWrapper());
        ret.appendChild((Component)innerDiv);
        this.componentsMap.put(item, ret);
        return (HtmlBasedComponent)ret;
    }


    protected TypedObject getItemAt(int index)
    {
        TypedObject ret = null;
        Component comp = null;
        try
        {
            if(index >= 0 && this.mainDiv.getChildren().size() > index &&
                            CollectionUtils.isNotEmpty(((Component)this.mainDiv.getChildren().get(index)).getChildren()))
            {
                comp = ((Component)this.mainDiv.getChildren().get(index)).getFirstChild();
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        if(comp != null)
        {
            ret = getDDContext().getWrapper().getDraggedItem(comp).getSingleTypedObject();
        }
        return ret;
    }


    protected void removeSelectedItems()
    {
        fireRemove(getModel().getSelectedIndexes());
    }


    protected void fireRemove(Collection<Integer> indexes)
    {
        for(GridViewListener listener : this.listeners)
        {
            listener.remove(indexes);
        }
    }


    protected void fireChangeSelection(List<Integer> selectedIndexes)
    {
        fireMarkAllAsSelected(false);
        getModel().setSelectedIndexes(selectedIndexes);
    }


    protected void fireChangeSelection(int selectedIndex)
    {
        fireMarkAllAsSelected(false);
        getModel().setSelectedIndexes(Collections.singletonList(Integer.valueOf(selectedIndex)));
    }


    protected void doUserEvent(Object[] data)
    {
        if(data.length > 1)
        {
            if(getModel().getSelectedIndex() != null)
            {
                if("left".equals(data[0]))
                {
                    increaseSelectedIndex(-1);
                }
                else if("right".equals(data[0]))
                {
                    increaseSelectedIndex(1);
                }
                else if("down".equals(data[0]) && data[1] instanceof String)
                {
                    int itemsPerRow = Integer.parseInt((String)data[1]);
                    increaseSelectedIndex(itemsPerRow);
                }
                else if("up".equals(data[0]) && data[1] instanceof String)
                {
                    int itemsPerRow = Integer.parseInt((String)data[1]);
                    increaseSelectedIndex(-itemsPerRow);
                }
                else if("enter".equals(data[0]))
                {
                    Clients.showBusy(Labels.getLabel("gridview.activation.busy"), true);
                    Component comp = this.mainDiv.getChildren().get(this.lastSelectedIndex);
                    Events.echoEvent("onLaterDblClick", comp, null);
                }
                else if("del".equals(data[0]))
                {
                    removeSelectedItems();
                }
                else if("markall".equals(data[0]))
                {
                    Clients.evalJavaScript("clearSelection();");
                    focusFocusComponent();
                    fireMarkAllAsSelected(true);
                }
                else if("shift_right".equals(data[0]) || "shift_left".equals(data[0]) || "shift_up".equals(data[0]) || "shift_down"
                                .equals(data[0]))
                {
                    int selectedIndex = getModel().getSelectedIndex().intValue();
                    if("shift_left".equals(data[0]))
                    {
                        if(selectedIndex == this.lastSelectedIndex)
                        {
                            selectedIndex = ((Integer)getModel().getSelectedIndexes().get(getModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedIndex--;
                    }
                    else if("shift_right".equals(data[0]))
                    {
                        if(selectedIndex == this.lastSelectedIndex)
                        {
                            selectedIndex = ((Integer)getModel().getSelectedIndexes().get(getModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedIndex++;
                    }
                    else if("shift_up".equals(data[0]))
                    {
                        int itemsPerRow = Integer.parseInt((String)data[1]);
                        if(selectedIndex == this.lastSelectedIndex)
                        {
                            selectedIndex = ((Integer)getModel().getSelectedIndexes().get(getModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedIndex -= itemsPerRow;
                        if(selectedIndex < 0)
                        {
                            selectedIndex = 0;
                        }
                    }
                    else if("shift_down".equals(data[0]))
                    {
                        int itemsPerRow = Integer.parseInt((String)data[1]);
                        if(selectedIndex == this.lastSelectedIndex)
                        {
                            selectedIndex = ((Integer)getModel().getSelectedIndexes().get(getModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedIndex += itemsPerRow;
                        if(selectedIndex >= getModel().getListModel().getElements().size())
                        {
                            selectedIndex = getModel().getListModel().getElements().size() - 1;
                        }
                    }
                    if(this.lastSelectedIndex != -1 && selectedIndex >= 0 && selectedIndex <=
                                    getModel().getListModel().getElements().size() - 1)
                    {
                        UITools.clearBrowserTextSelection();
                        List<Integer> selectedIndexes = new ArrayList<>();
                        int start = Math.min(this.lastSelectedIndex, selectedIndex);
                        int end = Math.max(this.lastSelectedIndex, selectedIndex);
                        for(int i = start; i <= end; i++)
                        {
                            selectedIndexes.add(Integer.valueOf(i));
                        }
                        fireChangeSelection(selectedIndexes);
                    }
                }
            }
        }
    }


    private void increaseSelectedIndex(int amount)
    {
        int itemIndex = getModel().getSelectedIndex().intValue();
        if(itemIndex == this.lastSelectedIndex)
        {
            itemIndex = ((Integer)getModel().getSelectedIndexes().get(getModel().getSelectedIndexes().size() - 1)).intValue() + amount;
        }
        else
        {
            itemIndex += amount;
        }
        this.lastSelectedIndex = itemIndex;
        if(this.lastSelectedIndex >= getModel().getListModel().size())
        {
            this.lastSelectedIndex = getModel().getListModel().size() - 1;
        }
        else if(this.lastSelectedIndex < 0)
        {
            this.lastSelectedIndex = 0;
        }
        fireChangeSelection(Collections.singletonList(Integer.valueOf(this.lastSelectedIndex)));
    }


    private void fireMarkAllAsSelected(boolean marked)
    {
        boolean markedThroughListener = true;
        for(GridViewListener listener : this.listeners)
        {
            try
            {
                listener.markAll(marked);
            }
            catch(CockpitMarkAllException e)
            {
                LOG.info(e.getMessage());
                markedThroughListener = false;
            }
        }
        if(marked && markedThroughListener)
        {
            List<Integer> selectedIndexes = new ArrayList<>();
            for(int i = 0; i < getModel().getListModel().getElements().size(); i++)
            {
                selectedIndexes.add(Integer.valueOf(i));
            }
            getModel().setSelectedIndexes(selectedIndexes);
        }
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public void setGridItemRenderer(GridItemRenderer gridItemRenderer)
    {
        this.gridItemRenderer = gridItemRenderer;
    }


    public GridItemRenderer getGridItemRenderer()
    {
        if(this.gridItemRenderer == null)
        {
            this.gridItemRenderer = (GridItemRenderer)new DefaultGridItemRenderer();
        }
        return this.gridItemRenderer;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
