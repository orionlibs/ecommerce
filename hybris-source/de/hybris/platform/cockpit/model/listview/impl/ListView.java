package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.ColumnLayoutComponent;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.impl.AbstractItemView;
import de.hybris.platform.cockpit.model.general.impl.LoadingProgressContainer;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ColumnModel;
import de.hybris.platform.cockpit.model.listview.ListViewMenuPopupBuilder;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDraggedItem;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.EditableComponent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.cockpit.util.testing.TestIdContext;
import de.hybris.platform.cockpit.util.testing.impl.ListViewCellTestIdContext;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.impl.XulElement;

public class ListView extends AbstractListView implements EditableComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(ListView.class);
    protected static final String COLUMN_SCLASS = "listColumn";
    protected static final String COLUMN_HEADER_SCLASS = "listColumnHeader";
    protected static final String COLUMN_HEADER_ASC_SCLASS = "listColumnHeaderAsc";
    protected static final String COLUMN_HEADER_DESC_SCLASS = "listColumnHeaderDesc";
    protected static final String DEFAULT_SCLASS = "listView";
    protected static final String COLUMN_CONTENT_SCLASS = "listColumnContent";
    protected static final String COLUMN_CONTENT_FIXED_SCLASS = "listColumnContentFixed";
    protected static final String SPLITTER_SCLASS = "listColumnSplitter";
    protected static final String PADDING_SCLASS = "listViewPadding";
    protected static final String MAIN_DIV_SCLASS = "listViewMainDiv";
    protected static final String DEFAULT_CELL_SCLASS = "defaultListViewCell";
    protected static final String DEFAULT_CELL_SCLASS_COMP = "dLVC";
    protected String defaultCellSclass = "defaultListViewCell";
    protected static final String EDITABLE_CELL_SCLASS = "editableListViewCell";
    protected static final String EDITABLE_CELL_SCLASS_COMP = "eLVC";
    protected String editableCellSclass = "editableListViewCell";
    protected static final String SELECTED_CELL_SCLASS = "selectedListViewCell";
    protected static final String SELECTED_CELL_SCLASS_COMP = "sLVC";
    protected String selectedCellSclass = "selectedListViewCell";
    protected static final String SELECTED_ROW_SCLASS_COMP = "sLVR";
    protected static final String SELECTED_ROW_SCLASS = "selectedListViewRow";
    protected String selectedRowSclass = "selectedListViewRow";
    protected static final String ACTIVATED_ROW_SCLASS = "activatedListViewRow";
    protected static final String ACTIVATED_ROW_SCLASS_COMP = "aLVR";
    protected String activatedRowSclass = "activatedListViewRow";
    protected static final String VERTICAL_SCROLL_SCLASS = "listViewVerticalScroll";
    protected static final String VERTICAL_SCROLL_DIV_SCLASS = "listViewVerticalScrollDiv";
    protected static final String COL_DRAG_N_DROP = "listViewColumnDnd";
    protected static final String WRAPPING_BOX = "listViewWrappingBox";
    protected static final String VERTICAL_SCROLL_UP_BTN_SCLASS = "listViewVerticalScrollUpBtn";
    protected static final String VERTICAL_SCROLL_DN_BTN_SCLASS = "listViewVerticalScrollDnBtn";
    protected static final String VERTICAL_SCROLL_DN_BTN_DISABLED_SCLASS = "listViewVerticalScrollDnDisabledBtn";
    protected static final String VERTICAL_SCROLL_UP_BTN_DISABLED_SCLASS = "listViewVerticalScrollUpDisabledBtn";
    private transient Div mainDiv = null;
    private transient Div menupopupContainer;
    private transient ColumnLayoutComponent mainColumnComponent = null;
    private final transient List<Div> columnContentBoxes = new ArrayList<>();
    private final transient List<Div> columnHeaderBoxes = new ArrayList<>();
    private final List<ColumnDescriptor> visibleColumnsInternal = new ArrayList<>();
    protected boolean showColumnHeaders = true;
    protected boolean editing = false;
    protected int editingColumn;
    protected int editingRow;
    protected boolean initialized;
    protected TableModel model;
    protected CellRenderer cellRenderer;
    protected Checkbox focusComponent;
    private final List<Integer> selectedRowIndexes = new ArrayList<>();
    private final List<Integer> selectedColIndexes = new ArrayList<>();
    private final List<Integer> activationIndexes = new ArrayList<>();
    private Boolean ddEnabled = null;
    private boolean doubleClickDisabled = false;
    private int lastSelectedRowIndex = -1;
    private TypedObject copyItem = null;
    private int lastRenderedStartIndex = -1;
    private boolean editMode = false;
    private List<ColumnDescriptor> copyColDescriptions = new ArrayList<>();
    private Object temporaryEditValue = null;
    private CockpitValidationService validationService;
    private ModelService modelService;
    private ValueService valueService;


    public ListView()
    {
        setSclass("listView");
        setupStyleClasses();
    }


    private void setupStyleClasses()
    {
        if(Boolean.parseBoolean(UITools.getCockpitParameter("listview.cell.classes.compressed", Executions.getCurrent())))
        {
            this.defaultCellSclass = "dLVC";
            this.editableCellSclass = "eLVC";
            this.selectedCellSclass = "sLVC";
            this.selectedRowSclass = "sLVR";
            this.activatedRowSclass = "aLVR";
        }
    }


    public void editCellAt(int columnIndex, int rowIndex)
    {
        editCellAt(columnIndex, rowIndex, null);
    }


    public void editCellAt(int columnIndex, int rowIndex, Object creationValue)
    {
        Object initialValue = creationValue;
        if(!isEditing() && getModel().isCellEditable(columnIndex, rowIndex))
        {
            UIEditor editor = getModel().getCellEditor(columnIndex, rowIndex);
            boolean select = false;
            if(initialValue instanceof String && " ".equals(initialValue))
            {
                select = true;
                initialValue = null;
            }
            PropertyDescriptor propertyDescriptor = getModel().getColumnComponentModel().getPropertyDescriptor(
                            getModel().getColumnComponentModel().getVisibleColumn(columnIndex));
            if(editor instanceof ListUIEditor)
            {
                List<Object> enumVals = UISessionUtils.getCurrentSession().getTypeService().getAvailableValues(propertyDescriptor);
                ((ListUIEditor)editor).setAvailableValues(EditorHelper.filterValues(propertyDescriptor, enumVals));
            }
            if(editor == null || getCellComponentAt(columnIndex, rowIndex) == null)
            {
                LOG.warn("Could not edit cell. No valid editor found.");
            }
            else
            {
                HtmlBasedComponent parent = getCellComponentAt(columnIndex, rowIndex);
                Object initialEditValue = null;
                initialEditValue = getModel().getValueAt(columnIndex, rowIndex);
                editor.setOptional(!PropertyDescriptor.Occurrence.REQUIRED.equals(propertyDescriptor.getOccurence()));
                List<Object> initialEditValueList = new ArrayList();
                if(this.selectedRowIndexes != null)
                {
                    for(Integer selectedRowIndex : new ArrayList(this.selectedRowIndexes))
                    {
                        Object initialEditValueRow = getModel().getValueAt(columnIndex, selectedRowIndex.intValue());
                        initialEditValueList.add(initialEditValueRow);
                    }
                }
                this.temporaryEditValue = initialEditValue;
                Map<String, Object> params = new HashMap<>();
                params.put("propertyDescriptor", propertyDescriptor);
                params.put("initialEditString", initialValue);
                Set<String> forceWritePks = new HashSet<>();
                HtmlBasedComponent editorViewComponent = editor.createViewComponent(initialEditValue, params, (EditorListener)new Object(this, initialEditValueList, columnIndex, forceWritePks, propertyDescriptor, editor, rowIndex, parent));
                if(editorViewComponent != null)
                {
                    setEditing(true);
                    this.editingColumn = columnIndex;
                    this.editingRow = rowIndex;
                    parent.getChildren().clear();
                    parent.appendChild((Component)editorViewComponent);
                    editor.setFocus(editorViewComponent, select);
                    parent.removeAttribute("validationIconAdded");
                }
            }
        }
    }


    protected void rollback(List<Object> initialEditValueList, int columnIndex)
    {
        int listPos = 0;
        for(Integer selectedRowIndex : new ArrayList(this.selectedRowIndexes))
        {
            if(initialEditValueList.size() > listPos)
            {
                fireChangeCellValue(columnIndex, selectedRowIndex.intValue(), initialEditValueList.get(listPos));
                listPos++;
            }
        }
    }


    public void validate(Set<String> forceWritePks, PropertyDescriptor propertyDescriptor, UIEditor editor, int rowIndex, int columnIndex, List<Object> initialEditValueList, HtmlBasedComponent parent)
    {
        TypedObject typedObject = (TypedObject)getModel().getListComponentModel().getListModel().elementAt(rowIndex);
        ValidationUIHelper validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
        int selectedCount = this.selectedRowIndexes.size();
        String languageIso = null;
        if(propertyDescriptor.isLocalized())
        {
            languageIso = ((ColumnDescriptor)getModel().getColumnComponentModel().getVisibleColumns().get(columnIndex)).getLanguage().getIsocode();
        }
        ObjectValueContainer currentObjectValues = validationUIHelper.createModelFromContainer(typedObject, languageIso, propertyDescriptor, editor);
        Set<CockpitValidationDescriptor> violations = validationUIHelper.sortCockpitValidationDescriptors(getValidationService().validateModel((ItemModel)currentObjectValues.getObject()));
        Set<PropertyDescriptor> descriptorsSet = new HashSet<>();
        descriptorsSet.add(propertyDescriptor);
        Set<PropertyDescriptor> omittedProps = TypeTools.getOmittedProperties(currentObjectValues, descriptorsSet, false);
        for(PropertyDescriptor descr : omittedProps)
        {
            StringBuilder msg = new StringBuilder();
            msg.append(Labels.getLabel("required_attribute_missing")).append(": '").append(descr.getQualifier()).append("'");
            violations.add(new CockpitValidationDescriptor(descr, 3, msg.toString(), null));
        }
        if(violations.isEmpty() || validationUIHelper.allWarningsForced(forceWritePks, violations))
        {
            try
            {
                if(selectedCount == 1)
                {
                    getValueService().setValues(typedObject, currentObjectValues);
                }
            }
            catch(ValueHandlerException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage());
                }
            }
            stopEditing();
            if(this.selectedRowIndexes != null)
            {
                if(selectedCount > 1)
                {
                    Clients.showBusy(null, true);
                    Object object = new Object(this, propertyDescriptor, columnIndex, validationUIHelper, editor, forceWritePks, initialEditValueList, selectedCount, parent);
                    parent.setAttribute("piggybagEL", object);
                    parent.addEventListener("onMultiEditLater", (EventListener)object);
                    Events.echoEvent("onMultiEditLater", (Component)parent, null);
                }
                else
                {
                    changeValues(selectedCount, columnIndex, editor);
                }
            }
        }
        else
        {
            validationUIHelper.createValidationMessages(parent, violations, typedObject, forceWritePks, this);
        }
    }


    public void focusFocusComponent()
    {
        Events.echoEvent("onFocusLater", (Component)this.focusComponent, null);
    }


    public ListViewMenuPopupBuilder getMenuPopupBuilder()
    {
        return getModel().getColumnComponentModel().getMenuPopupBuilder();
    }


    public TableModel getModel()
    {
        return this.model;
    }


    public Object getValueAt(int columnIndex, int rowIndex)
    {
        return this.model.getValueAt(columnIndex, rowIndex);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(assureModelLoaded())
        {
            getChildren().clear();
            this.selectedColIndexes.clear();
            this.selectedColIndexes.addAll(getModel().getColumnComponentModel().getSelectedIndexes());
            this.selectedRowIndexes.clear();
            this.selectedRowIndexes.addAll(getModel().getListComponentModel().getSelectedIndexes());
            this.activationIndexes.clear();
            this.activationIndexes.addAll(getActivationIndexes());
            Div conDiv = new Div();
            conDiv.setStyle("width: 100%; height: 100%; overflow-y: auto; position: relative;");
            this.mainDiv = new Div();
            this.mainDiv
                            .setAction("onclick: this.onselectstart=function(){ if(!isTextInputNode(document.activeElement)) { return false;} };");
            this.mainDiv.setSclass("listViewMainDiv");
            appendChild((Component)conDiv);
            conDiv.appendChild((Component)this.mainDiv);
            this.menupopupContainer = new Div();
            appendChild((Component)this.menupopupContainer);
            this.mainColumnComponent = new ColumnLayoutComponent();
            this.mainDiv.appendChild((Component)this.mainColumnComponent);
            this.mainColumnComponent.setWidth("100%");
            this.mainColumnComponent.setHeight("100%");
            this.mainDiv.addEventListener("onFocusLater", (EventListener)new Object(this));
        }
        this.focusComponent = new Checkbox();
        appendChild((Component)this.focusComponent);
        this.focusComponent.setSclass("plainBtn");
        this.focusComponent.setZindex(-10);
        this.focusComponent.setStyle("position:absolute;top:-10000px;z-index:-10");
        String focusAction = "onkeypress: listViewSendKeypress(this,event); onkeydown: return listViewSendKeydown(this,event);onfocus: this.onselectstart=function(){ if(!isTextInputNode(document.activeElement)) { return false;} };";
        this.focusComponent.setAction("onkeypress: listViewSendKeypress(this,event); onkeydown: return listViewSendKeydown(this,event);onfocus: this.onselectstart=function(){ if(!isTextInputNode(document.activeElement)) { return false;} };");
        this.focusComponent.addEventListener("onUser", (EventListener)new Object(this));
        this.focusComponent.addEventListener("onFocusLater", (EventListener)new Object(this));
        this.loadingProgressContainer = new LoadingProgressContainer(false);
        if(getModel() != null && !getModel().getColumnComponentModel().getVisibleColumns().isEmpty())
        {
            List<ColumnDescriptor> columns = getModel().getColumnComponentModel().getVisibleColumns();
            this.visibleColumnsInternal.clear();
            this.visibleColumnsInternal.addAll(columns);
            renderColumns(this.visibleColumnsInternal);
            renderRows();
            this.mainColumnComponent.setFillBackground(createListViewBackgroundColumn());
            this.mainColumnComponent.setWidths(getColumnWidths());
            this.mainColumnComponent.adjustColumnWidths(null);
            this.initialized = true;
        }
        this.mainDiv.appendChild((Component)this.loadingProgressContainer);
        this.loadingProgressContainer.addEventListener("onContinueLoading", (EventListener)new Object(this));
        return this.initialized;
    }


    public boolean isEditing()
    {
        return this.editing;
    }


    public Div renderColumnContentDiv()
    {
        Div columnContentDiv = new Div();
        this.columnContentBoxes.add(columnContentDiv);
        columnContentDiv.setSclass("listColumnContent");
        columnContentDiv.setHeight("100%");
        Div columnContent = new Div();
        columnContentDiv.appendChild((Component)columnContent);
        columnContent.setAction("onmouseup: return false;");
        columnContent.setSclass("listColumnContentFixed");
        return columnContentDiv;
    }


    public void setEditing(boolean editing)
    {
        this.editing = editing;
    }


    public void setModel(TableModel model)
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


    public void setShowColumnHeaders(boolean showColumnHeaders)
    {
        this.showColumnHeaders = showColumnHeaders;
    }


    public void setValueAt(int columnIndex, int rowIndex, Object data)
    {
        fireChangeCellValue(columnIndex, rowIndex, data);
    }


    public void stopEditing()
    {
        if(isEditing())
        {
            setEditing(false);
            updateCell(this.editingColumn, this.editingRow);
            focusFocusComponent();
        }
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateColumns();
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


    public void updateActivation()
    {
        if(this.initialized && !isEditing())
        {
            if(this.activationIndexes != null)
            {
                clear(null, this.activationIndexes);
            }
            this.activationIndexes.clear();
            this.activationIndexes.addAll(getActivationIndexes());
            showActivatedItems();
        }
    }


    public void updateActiveItems()
    {
        if(this.initialized && !isEditing())
        {
            Collection activeItems = getModel().getListComponentModel().getActiveItems();
            if(activeItems != null)
            {
                for(Object object : activeItems)
                {
                    if(object instanceof TypedObject)
                    {
                        updateItem((TypedObject)object, Collections.EMPTY_SET);
                    }
                }
            }
        }
    }


    public void updateCell(int columnIndex, int rowIndex)
    {
        if(this.initialized && !isEditing())
        {
            HtmlBasedComponent cell = getCellComponentAt(columnIndex, rowIndex);
            if(cell != null)
            {
                cell.getChildren().clear();
                updateCellState(columnIndex, rowIndex, cell);
                try
                {
                    getCellRenderer().render(getModel(), columnIndex, rowIndex, (Component)cell);
                }
                catch(Exception e)
                {
                    LOG.error("An error occurred while updating cell.", e);
                }
            }
        }
    }


    public void updateColumns()
    {
        if(this.initialized)
        {
            this.selectedColIndexes.clear();
            updateViewColumns(null);
        }
    }


    public void updateColumns(Integer colIndex)
    {
        if(this.initialized && !isEditing())
        {
            this.selectedColIndexes.clear();
            updateViewColumns(colIndex);
        }
    }


    public int updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        int ret = updateItemInternal(item, modifiedProperties);
        Collection<TypedObject> additionalItemsToUpdate = getModel().getListComponentModel().getAdditionalItemsToUpdate(item);
        for(TypedObject typedObject : additionalItemsToUpdate)
        {
            updateItemInternal(typedObject, Collections.EMPTY_SET);
        }
        return ret;
    }


    public void updateItems()
    {
        if(this.initialized && !isEditing())
        {
            this.selectedRowIndexes.clear();
            renderRows();
            this.mainColumnComponent.setFillBackground(createListViewBackgroundColumn());
        }
    }


    public void updateRow(int row)
    {
        if(this.initialized && !isEditing())
        {
            for(int i = 0; i < this.columnContentBoxes.size(); i++)
            {
                updateCell(i, row);
            }
        }
    }


    public void updateSelection()
    {
        if(this.initialized && !isEditing())
        {
            List<Integer> colsToUnselect = new ArrayList<>(this.selectedColIndexes);
            colsToUnselect.removeAll(getModel().getColumnComponentModel().getSelectedIndexes());
            List<Integer> rowsToUnselect = new ArrayList<>(this.selectedRowIndexes);
            rowsToUnselect.removeAll(getModel().getListComponentModel().getSelectedIndexes());
            List<Integer> colsToSelect = new ArrayList<>(getModel().getColumnComponentModel().getSelectedIndexes());
            colsToSelect.removeAll(this.selectedColIndexes);
            List<Integer> rowsToSelect = new ArrayList<>(getModel().getListComponentModel().getSelectedIndexes());
            rowsToSelect.removeAll(this.selectedRowIndexes);
            List<Integer> rowsAffected = new ArrayList<>(this.selectedRowIndexes);
            rowsAffected.addAll(getModel().getListComponentModel().getSelectedIndexes());
            List<Integer> colsAffected = new ArrayList<>(this.selectedColIndexes);
            colsAffected.addAll(getModel().getColumnComponentModel().getSelectedIndexes());
            if(!this.selectedColIndexes.isEmpty() && !this.selectedRowIndexes.isEmpty())
            {
                if(getModel().getListComponentModel().isForceRenderOnSelectionChanged())
                {
                    for(Integer rowIndex : rowsToUnselect)
                    {
                        updateRow(rowIndex.intValue());
                    }
                }
                clear(isSimpleSelectionMode() ? colsAffected : null, rowsAffected);
            }
            else if(!rowsToUnselect.isEmpty())
            {
                clear(isSimpleSelectionMode() ? colsAffected : null, rowsAffected);
            }
            this.selectedRowIndexes.clear();
            this.selectedRowIndexes.addAll(getModel().getListComponentModel().getSelectedIndexes());
            this.selectedColIndexes.clear();
            this.selectedColIndexes.addAll(getModel().getColumnComponentModel().getSelectedIndexes());
            if(getModel().getListComponentModel().isForceRenderOnSelectionChanged())
            {
                for(Integer rowIndex : this.selectedRowIndexes)
                {
                    updateRow(rowIndex.intValue());
                }
            }
            else if(!this.selectedRowIndexes.isEmpty())
            {
                if(!isSimpleSelectionMode())
                {
                    for(Integer rowIndex : this.selectedRowIndexes)
                    {
                        renderRowAsSelected(rowIndex);
                    }
                }
                renderCellsAsSelected(this.selectedColIndexes, this.selectedRowIndexes);
                Collection<Integer> activationIndexes = getActivationIndexes();
                if(!activationIndexes.isEmpty())
                {
                    boolean actAffected = false;
                    for(Integer actIndex : activationIndexes)
                    {
                        if(rowsAffected.contains(actIndex))
                        {
                            actAffected = true;
                            break;
                        }
                    }
                    if(actAffected)
                    {
                        List<Integer> colIndexList = new ArrayList<>();
                        for(int i = 0; i < this.columnContentBoxes.size(); i++)
                        {
                            colIndexList.add(Integer.valueOf(i));
                        }
                        renderCellsAsActivated(colIndexList, new ArrayList<>(activationIndexes));
                    }
                }
            }
        }
    }


    public void updateVisibleColumns()
    {
        if(this.initialized && !isEditing())
        {
            updateViewColumns();
        }
    }


    protected void appendScrollIntoViewComponent(HtmlBasedComponent cell)
    {
        AbstractItemView.LoadImage loadImage = new AbstractItemView.LoadImage((AbstractItemView)this);
        loadImage.setSrc("hack");
        loadImage.setSclass("autoscroll_hack_image");
        String mainDivId = this.mainColumnComponent.getUuid();
        loadImage.setAction("onerror: listViewScrollIntoViewIfNeeded(this, \"" + mainDivId + "\");");
        cell.appendChild((Component)loadImage);
    }


    protected boolean assureModelLoaded()
    {
        return (getModel() != null && getModel().getColumnComponentModel() != null &&
                        getModel().getListComponentModel() != null && getModel().getListComponentModel().getListModel() != null);
    }


    protected Menupopup createHeaderMenu(ColumnLayoutComponent columnLayoutComponent)
    {
        return adjustHeaderMenu(getMenuPopupBuilder().buildBackgroundColumnMenuPopup((UIListView)this, columnLayoutComponent));
    }


    protected Menupopup createHeaderMenu(ColumnDescriptor descriptor, int columnIndex)
    {
        return adjustHeaderMenu(getMenuPopupBuilder().buildStandardColumnMenuPopup((UIListView)this, descriptor, columnIndex));
    }


    protected Menupopup adjustHeaderMenu(Menupopup popup)
    {
        Menuitem mitem = new Menuitem(Labels.getLabel("section.editor.contextmenuedit"));
        mitem.setCheckmark(true);
        mitem.setChecked(isEditMode());
        mitem.setSclass("menu-item-switch-chkbox");
        if(!isEditMode())
        {
            popup.getChildren().clear();
        }
        mitem.addEventListener("onClick", (EventListener)new Object(this));
        popup.appendChild((Component)mitem);
        return popup;
    }


    protected Component createListViewBackgroundColumn()
    {
        Div dummyColumn = new Div();
        dummyColumn.setSclass("listColumn");
        Div headerDiv = new Div();
        headerDiv.setSclass("listColumnHeader");
        if(this.showColumnHeaders)
        {
            dummyColumn.appendChild((Component)headerDiv);
        }
        headerDiv.setDroppable("listViewColumnDnd");
        headerDiv.addEventListener("onDrop", (EventListener)new Object(this));
        headerDiv.addEventListener("onLater", (EventListener)new Object(this));
        Menupopup headerPopup = createHeaderMenu(this.mainColumnComponent);
        this.menupopupContainer.appendChild((Component)headerPopup);
        headerDiv.setContext((Popup)headerPopup);
        Div columnContentDiv = new Div();
        columnContentDiv.setSclass("listColumnContent");
        columnContentDiv.setHeight("100%");
        Div columnContent = new Div();
        columnContentDiv.appendChild((Component)columnContent);
        columnContent.setSclass("listColumnContentFixed");
        List<? extends Object> items = getModel().getListComponentModel().getListModel().getElements();
        for(int i = 0; i < items.size(); i++)
        {
            Div cell = new Div();
            cell.setSclass(this.defaultCellSclass + " " + this.defaultCellSclass);
            cell.setAction("onclick: clearSelection();");
            columnContent.appendChild((Component)cell);
            int index = i;
            cell.addEventListener("onClick", (EventListener)new Object(this, index));
            cell.addEventListener("onDoubleClick", (EventListener)new Object(this, index));
        }
        dummyColumn.appendChild((Component)columnContentDiv);
        return (Component)dummyColumn;
    }


    protected void doUserEvent(Object[] data)
    {
        if(isRendering())
        {
            return;
        }
        int colIndex = getModel().getColumnComponentModel().getSelectedIndex();
        if(colIndex == -1)
        {
            colIndex = 0;
        }
        if(this.lastSelectedRowIndex != -1)
        {
            if(getModel().getListComponentModel().getSelectedIndex() != null)
            {
                int rowIndex = getModel().getListComponentModel().getSelectedIndex().intValue();
                if("down".equals(data[0]) && getModel().getListComponentModel().isSelectable())
                {
                    if(rowIndex == this.lastSelectedRowIndex)
                    {
                        rowIndex = ((Integer)getModel().getListComponentModel().getSelectedIndexes().get(getModel().getListComponentModel().getSelectedIndexes().size() - 1)).intValue() + 1;
                    }
                    else
                    {
                        rowIndex++;
                    }
                    if(rowIndex <= getModel().getListComponentModel().getListModel().size() - 1 &&
                                    getModel().getListComponentModel().isSelectable())
                    {
                        this.lastSelectedRowIndex = rowIndex;
                        fireChangeSelection(Collections.singletonList(Integer.valueOf(colIndex)),
                                        Collections.singletonList(Integer.valueOf(rowIndex)));
                    }
                    focusFocusComponent();
                }
                else if("up".equals(data[0]) && getModel().getListComponentModel().isSelectable())
                {
                    if(rowIndex == this.lastSelectedRowIndex)
                    {
                        rowIndex = ((Integer)getModel().getListComponentModel().getSelectedIndexes().get(getModel().getListComponentModel().getSelectedIndexes().size() - 1)).intValue() - 1;
                    }
                    else
                    {
                        rowIndex--;
                    }
                    if(rowIndex >= 0)
                    {
                        this.lastSelectedRowIndex = rowIndex;
                        fireChangeSelection(Collections.singletonList(Integer.valueOf(colIndex)),
                                        Collections.singletonList(Integer.valueOf(rowIndex)));
                    }
                    focusFocusComponent();
                }
                else if("right".equals(data[0]))
                {
                    if(colIndex < getModel().getColumnComponentModel().getVisibleColumns().size() - 1)
                    {
                        for(int i = ++colIndex; i < getModel().getColumnComponentModel().getVisibleColumns().size(); i++)
                        {
                            if(getModel().getColumnComponentModel().getVisibleColumn(i).isSelectable())
                            {
                                fireChangeSelection(i, rowIndex);
                                break;
                            }
                        }
                    }
                    focusFocusComponent();
                }
                else if("left".equals(data[0]))
                {
                    if(colIndex > 0)
                    {
                        for(int i = --colIndex; i >= 0; i--)
                        {
                            if(getModel().getColumnComponentModel().getVisibleColumn(i).isSelectable())
                            {
                                fireChangeSelection(i, rowIndex);
                                break;
                            }
                        }
                    }
                    focusFocusComponent();
                }
                else if("enter".equals(data[0]) && getModel().getListComponentModel().isActivatable())
                {
                    if(isEditing())
                    {
                        stopEditing();
                    }
                    else
                    {
                        fireActivate(Collections.singletonList(Integer.valueOf(rowIndex)));
                    }
                }
                else if("edit".equals(data[0]) && getModel().isCellEditable(colIndex, rowIndex))
                {
                    String pressedChar = null;
                    if(data.length > 2)
                    {
                        Integer keycode = Integer.valueOf((String)data[1]);
                        pressedChar = String.valueOf((char)keycode.intValue());
                        if(!Boolean.parseBoolean((String)data[2]))
                        {
                            pressedChar = pressedChar.toLowerCase(Locale.getDefault());
                        }
                    }
                    editCellAt(colIndex, rowIndex, pressedChar);
                }
                else if("copy".equals(data[0]))
                {
                    if(this.selectedRowIndexes.size() == 1 && this.selectedColIndexes.size() > 0 &&
                                    getModel().isCellSelectable(colIndex, rowIndex))
                    {
                        this.copyItem = (TypedObject)getModel().getListComponentModel().getListModel().elementAt(rowIndex);
                        this.copyColDescriptions = Collections.singletonList(getModel().getColumnComponentModel()
                                        .getVisibleColumn(colIndex));
                    }
                }
                else if("paste".equals(data[0]))
                {
                    if(this.selectedRowIndexes.size() > 0 && this.selectedColIndexes.size() > 0)
                    {
                        if(this.selectedRowIndexes.size() > 1)
                        {
                            try
                            {
                                if(this.copyItem != null && this.copyColDescriptions.size() > 0)
                                {
                                    boolean checkColumn = true;
                                    for(int i = 0; i < this.selectedColIndexes.size(); i++)
                                    {
                                        if(!getModel().getColumnComponentModel().getVisibleColumn(((Integer)this.selectedColIndexes.get(i)).intValue()).equals(this.copyColDescriptions.get(i)))
                                        {
                                            checkColumn = false;
                                        }
                                    }
                                    if(checkColumn)
                                    {
                                        int answer = Messagebox.show(Labels.getLabel("listview.paste.question.message"),
                                                        Labels.getLabel("listview.paste.question.title"), 3, "z-msgbox z-msgbox-question");
                                        if(answer == 1)
                                        {
                                            paste();
                                        }
                                    }
                                    else
                                    {
                                        Messagebox.show(Labels.getLabel("listview.paste.samecolumn"));
                                    }
                                }
                            }
                            catch(InterruptedException e)
                            {
                                LOG.error(e.getMessage(), e);
                            }
                        }
                        else
                        {
                            paste();
                        }
                    }
                }
                else if("esc".equals(data[0]) && (this.selectedColIndexes.size() > 1 || this.selectedRowIndexes.size() > 0))
                {
                    if(!isEditing())
                    {
                        fireChangeSelection(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
                        focusFocusComponent();
                    }
                }
                else if("ctrl_right".equals(data[0]))
                {
                    firePageRequest(1);
                    Events.echoEvent("onFocusLater", (Component)this.mainDiv, null);
                }
                else if("ctrl_left".equals(data[0]))
                {
                    firePageRequest(-1);
                    Events.echoEvent("onFocusLater", (Component)this.mainDiv, null);
                }
                else if("page_up".equals(data[0]))
                {
                    rowIndex = 0;
                    fireChangeSelection(colIndex, rowIndex);
                    focusFocusComponent();
                }
                else if("page_down".equals(data[0]))
                {
                    rowIndex = getModel().getListComponentModel().getListModel().getElements().size() - 1;
                    fireChangeSelection(colIndex, rowIndex);
                    focusFocusComponent();
                }
                else if("del".equals(data[0]))
                {
                    List<Integer> sel = getModel().getListComponentModel().getSelectedIndexes();
                    if(!sel.isEmpty())
                    {
                        removeSelectedItems();
                    }
                }
                else if("markall".equals(data[0]))
                {
                    int realColIndex = getModel().getColumnComponentModel().getSelectedIndex();
                    List<Integer> realRowIndexes = new ArrayList<>();
                    for(int i = 0; i < getModel().getListComponentModel().getListModel().getElements().size(); i++)
                    {
                        realRowIndexes.add(Integer.valueOf(i));
                    }
                    Clients.evalJavaScript("clearSelection();");
                    focusFocusComponent();
                    fireMarkAllAsSelected(Collections.singletonList(Integer.valueOf(realColIndex)), realRowIndexes);
                }
                else if("shift_down".equals(data[0]) || "shift_up".equals(data[0]))
                {
                    int selectedRowIndex = rowIndex;
                    if("shift_up".equals(data[0]))
                    {
                        if(selectedRowIndex == this.lastSelectedRowIndex)
                        {
                            selectedRowIndex = ((Integer)getModel().getListComponentModel().getSelectedIndexes().get(getModel().getListComponentModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedRowIndex--;
                    }
                    else
                    {
                        if(selectedRowIndex == this.lastSelectedRowIndex)
                        {
                            selectedRowIndex = ((Integer)getModel().getListComponentModel().getSelectedIndexes().get(getModel().getListComponentModel().getSelectedIndexes().size() - 1)).intValue();
                        }
                        selectedRowIndex++;
                    }
                    if(this.lastSelectedRowIndex != -1 && selectedRowIndex >= 0 && selectedRowIndex <=
                                    getModel().getListComponentModel().getListModel()
                                                    .getElements().size() - 1)
                    {
                        UITools.clearBrowserTextSelection();
                        List<Integer> rowIndexes = new ArrayList<>();
                        int start = Math.min(this.lastSelectedRowIndex, selectedRowIndex);
                        int end = Math.max(this.lastSelectedRowIndex, selectedRowIndex);
                        for(int i = start; i <= end; i++)
                        {
                            rowIndexes.add(Integer.valueOf(i));
                        }
                        fireChangeSelection(Collections.singletonList(Integer.valueOf(colIndex)), rowIndexes);
                    }
                }
            }
        }
    }


    protected Collection<Integer> getActivationIndexes()
    {
        Collection<? extends Object> activeItems = getModel().getListComponentModel().getActiveItems();
        ArrayList<Integer> activationIndexes = new ArrayList<>();
        for(Object activeItem : activeItems)
        {
            int rowIndex = getModel().getListComponentModel().getListModel().getElements().indexOf(activeItem);
            if(rowIndex > -1)
            {
                for(int colIndex = 0; colIndex < this.columnContentBoxes.size(); colIndex++)
                {
                    activationIndexes.add(Integer.valueOf(rowIndex));
                }
            }
        }
        return activationIndexes;
    }


    protected CellRenderer getCellRenderer()
    {
        if(this.cellRenderer == null)
        {
            this.cellRenderer = (CellRenderer)new MyCellRenderer(this);
        }
        return this.cellRenderer;
    }


    private int getColumnIndexOfCellDiv(Component cellDiv)
    {
        int index = -1;
        List<Component> hboxChildren = this.mainColumnComponent.getColumns();
        int colIndex = 0;
        for(int hboxChildIndex = 0; hboxChildIndex < hboxChildren.size(); hboxChildIndex++)
        {
            Object child = hboxChildren.get(hboxChildIndex);
            if(((Component)child).getFirstChild().equals(cellDiv))
            {
                index = colIndex;
                break;
            }
            colIndex++;
        }
        return index;
    }


    protected void removeScrollIntoViewComponent(HtmlBasedComponent cell)
    {
        if(cell.getLastChild() instanceof AbstractItemView.LoadImage)
        {
            cell.getLastChild().detach();
        }
    }


    protected void updateCellState(int columnIndex, int rowIndex)
    {
        HtmlBasedComponent cell = getCellComponentAt(columnIndex, rowIndex);
        updateCellState(columnIndex, rowIndex, cell);
    }


    protected void updateCellState(int columnIndex, int rowIndex, HtmlBasedComponent cell)
    {
        Collection<Integer> activationIndexes = getActivationIndexes();
        List<Integer> rowIndexes = getModel().getListComponentModel().getSelectedIndexes();
        List<Integer> columnIndexes = getModel().getColumnComponentModel().getSelectedIndexes();
        if(cell != null)
        {
            if(activationIndexes.contains(Integer.valueOf(rowIndex)))
            {
                cell.setSclass(this.activatedRowSclass);
            }
            else if(rowIndexes.contains(Integer.valueOf(rowIndex)) && columnIndexes.contains(Integer.valueOf(columnIndex)))
            {
                cell.setSclass(this.selectedCellSclass);
            }
            else if(!isSimpleSelectionMode() && rowIndexes.contains(Integer.valueOf(rowIndex)))
            {
                cell.setSclass(this.selectedRowSclass);
            }
            else
            {
                cell.setSclass(this.defaultCellSclass);
            }
            if(getModel().isCellEditable(columnIndex, rowIndex))
            {
                UITools.modifySClass(cell, this.editableCellSclass, true);
            }
        }
    }


    protected void updateRowState(Integer rowIndex)
    {
        for(int colIndex = 0; colIndex < this.columnContentBoxes.size(); colIndex++)
        {
            updateCellState(colIndex, rowIndex.intValue());
        }
    }


    private void changeValues(int selectedCount, int columnIndex, UIEditor editor)
    {
        try
        {
            if(selectedCount > 1)
            {
                UndoTools.startUndoGrouping();
                fireMultiEdit(columnIndex, this.selectedRowIndexes, editor.getValue());
            }
            else if(this.selectedRowIndexes != null && !this.selectedRowIndexes.isEmpty())
            {
                fireChangeCellValue(columnIndex, ((Integer)this.selectedRowIndexes.iterator().next()).intValue(), editor.getValue());
            }
            else
            {
                LOG.warn("Can not change cell value. Reason: Invalid selection.");
            }
        }
        finally
        {
            UndoTools.stopUndoGrouping(UISessionUtils.getCurrentSession().getUndoManager(), this);
        }
    }


    private void clear(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        List<Integer> colIndexList = new ArrayList<>();
        if(colIndexes == null)
        {
            for(int i = 0; i < this.columnContentBoxes.size(); i++)
            {
                colIndexList.add(Integer.valueOf(i));
            }
        }
        else
        {
            colIndexList.addAll(colIndexes);
        }
        for(int j = 0; j < rowIndexes.size(); j++)
        {
            for(int i = 0; i < colIndexList.size(); i++)
            {
                if(rowIndexes.size() > j && colIndexList
                                .size() > i && this.columnContentBoxes
                                .size() > ((Integer)colIndexList.get(i)).intValue() && ((Div)this.columnContentBoxes
                                .get(((Integer)colIndexList.get(i)).intValue())).getFirstChild().getChildren().size() > ((Integer)rowIndexes
                                .get(j)).intValue())
                {
                    Object object = ((Div)this.columnContentBoxes.get(((Integer)colIndexList.get(i)).intValue())).getFirstChild().getChildren().get(((Integer)rowIndexes.get(j)).intValue());
                    if(object instanceof HtmlBasedComponent)
                    {
                        UITools.modifySClass((HtmlBasedComponent)object, this.activatedRowSclass, false);
                        UITools.modifySClass((HtmlBasedComponent)object, this.selectedCellSclass, false);
                        UITools.modifySClass((HtmlBasedComponent)object, this.selectedRowSclass, false);
                        UITools.modifySClass((HtmlBasedComponent)object, this.defaultCellSclass, true);
                        ((HtmlBasedComponent)object).setTooltiptext("");
                        removeScrollIntoViewComponent((HtmlBasedComponent)object);
                    }
                }
            }
        }
    }


    private void continueRenderRows(int index)
    {
        if(this.lastRenderedStartIndex < index)
        {
            this.lastRenderedStartIndex = index;
            delayLazyload();
            continueRenderRows(index, getLazyloadPackageSize());
        }
        else
        {
            this.loadingProgressContainer.setVisible(false);
        }
    }


    private void continueRenderRows(int index, int nrItems)
    {
        List<? extends Object> items = getModel().getListComponentModel().getListModel().getElements();
        if(getLazyloadPackageSize() <= 0)
        {
            for(int i = 0; i < items.size(); i++)
            {
                for(int j = 0; j < this.columnContentBoxes.size(); j++)
                {
                    renderCell(j, i);
                }
            }
        }
        else
        {
            int startIndex = index;
            int endIndex = startIndex + nrItems;
            boolean finishedLoading = false;
            if(endIndex >= items.size())
            {
                endIndex = items.size();
                finishedLoading = true;
            }
            int i;
            for(i = startIndex; i < endIndex; i++)
            {
                for(int j = 0; j < this.columnContentBoxes.size(); j++)
                {
                    renderCell(j, i);
                }
            }
            if(finishedLoading || !this.loadingProgressContainer.isVisible())
            {
                this.loadingProgressContainer.setVisible(false);
            }
            else
            {
                this.loadingProgressContainer.setValues(endIndex, items.size());
                if(this.loadingProgressContainer.getDesktop() == null)
                {
                    for(i = endIndex; i < items.size(); i++)
                    {
                        for(int j = 0; j < this.columnContentBoxes.size(); j++)
                        {
                            renderCell(j, i);
                        }
                    }
                    this.loadingProgressContainer.setVisible(false);
                }
                else
                {
                    Events.echoEvent("onContinueLoading", (Component)this.loadingProgressContainer, null);
                }
            }
        }
    }


    private HtmlBasedComponent getCellComponentAt(int colIndex, int rowIndex)
    {
        HtmlBasedComponent cell = null;
        if(this.columnContentBoxes != null && colIndex >= 0 && colIndex < this.columnContentBoxes.size() && rowIndex >= 0)
        {
            int rowSize = ((Div)this.columnContentBoxes.get(colIndex)).getFirstChild().getChildren().size();
            if(rowIndex < rowSize)
            {
                cell = ((Div)this.columnContentBoxes.get(colIndex)).getFirstChild().getChildren().get(rowIndex);
            }
        }
        return cell;
    }


    private List<String> getColumnWidths()
    {
        List<String> widths = new ArrayList<>();
        if(getModel() != null && !getModel().getColumnComponentModel().getVisibleColumns().isEmpty())
        {
            List<ColumnDescriptor> columns = getModel().getColumnComponentModel().getVisibleColumns();
            for(int i = 0; i < columns.size(); i++)
            {
                String columnWidth = getModel().getColumnComponentModel().getColumnWidth(columns.get(i), Boolean.FALSE);
                widths.add((columnWidth == null) ? "none" : columnWidth);
            }
        }
        return widths;
    }


    private String getRowBorderSclass(TypedObject typedObject)
    {
        String borderSclass = "lv-row";
        Object object = typedObject.getObject();
        if(object != null && UISessionUtils.getCurrentSession().getModelService().isNew(object))
        {
            borderSclass = borderSclass.concat("-new");
        }
        return borderSclass;
    }


    private int getRowIndexOfCellDiv(Component cellDiv, int columnIndex)
    {
        int index = -1;
        int tempIndex = 0;
        if(columnIndex >= this.columnContentBoxes.size())
        {
            return index;
        }
        if(this.columnContentBoxes.get(columnIndex) == null || ((Div)this.columnContentBoxes.get(columnIndex)).getFirstChild() == null)
        {
            return index;
        }
        for(Object currentWrapper : ((Div)this.columnContentBoxes.get(columnIndex)).getFirstChild().getChildren())
        {
            if(currentWrapper instanceof HtmlBasedComponent)
            {
                if(currentWrapper.equals(cellDiv))
                {
                    index = tempIndex;
                    break;
                }
                tempIndex++;
            }
        }
        return index;
    }


    private boolean isRendering()
    {
        return (this.loadingProgressContainer != null && this.loadingProgressContainer.isVisible());
    }


    private void paste()
    {
        if(this.copyItem != null && this.copyColDescriptions.size() > 0)
        {
            boolean checkColumn = true;
            for(int i = 0; i < this.selectedColIndexes.size(); i++)
            {
                if(!getModel().getColumnComponentModel().getVisibleColumn(((Integer)this.selectedColIndexes.get(i)).intValue()).equals(this.copyColDescriptions.get(i)))
                {
                    checkColumn = false;
                }
            }
            if(checkColumn)
            {
                try
                {
                    UndoTools.startUndoGrouping();
                    for(Integer rowIndex : new ArrayList(this.selectedRowIndexes))
                    {
                        for(ColumnDescriptor colDesc : this.copyColDescriptions)
                        {
                            int realColIndex = getModel().getColumnComponentModel().findColumn(colDesc);
                            if(getModel().isCellEditable(realColIndex, rowIndex.intValue()))
                            {
                                String text = "";
                                Object value = this.model.getColumnComponentModel().getValueAt(realColIndex, this.copyItem);
                                if(value != null)
                                {
                                    text = value.toString();
                                }
                                fireChangeCellValue(realColIndex, rowIndex.intValue(), text);
                            }
                        }
                        updateRow(rowIndex.intValue());
                        focusFocusComponent();
                    }
                }
                finally
                {
                    UndoTools.stopUndoGrouping(UISessionUtils.getCurrentSession().getUndoManager(), this);
                }
            }
        }
        focusFocusComponent();
    }


    public void setDoubleClickDisabled(boolean value)
    {
        this.doubleClickDisabled = value;
    }


    public boolean isDoubleClickDisabled()
    {
        return this.doubleClickDisabled;
    }


    public void removeSelectedItems()
    {
        fireRemove(getModel().getListComponentModel().getSelectedIndexes());
    }


    private void renderCell(int colIndex, int rowIndex)
    {
        TypedObject item = (TypedObject)getModel().getListComponentModel().getListModel().elementAt(rowIndex);
        ColumnDescriptor colDescr = getModel().getColumnComponentModel().getVisibleColumn(colIndex);
        if(colDescr == null)
        {
            return;
        }
        Div cell = new Div();
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)cell, (TestIdContext)new ListViewCellTestIdContext(getModel(), colIndex, rowIndex));
        }
        updateCellState(colIndex, rowIndex, (HtmlBasedComponent)cell);
        UITools.modifySClass((HtmlBasedComponent)cell, getRowBorderSclass(item), true);
        UITools.addMacCommandClickListener((XulElement)cell);
        cell.addEventListener("onClick", (EventListener)new Object(this, colDescr, item));
        if(!isDoubleClickDisabled())
        {
            cell.addEventListener("onDoubleClick", (EventListener)new Object(this, cell));
        }
        cell.addEventListener("onLaterDblClick", (EventListener)new Object(this, colDescr, item));
        ((Div)this.columnContentBoxes.get(colIndex)).getFirstChild().appendChild((Component)cell);
        try
        {
            getCellRenderer().render(getModel(), colIndex, rowIndex, (Component)cell);
        }
        catch(Exception e)
        {
            LOG.error("An error occurred while rendering cell.", e);
        }
        if(isDDEnabled() && getDDContext() != null)
        {
            cell.setDroppable("PerspectiveDND");
            cell.setDraggable("PerspectiveDND");
            getDDContext().getWrapper().attachDraggedItem((DraggedItem)new DefaultDraggedItem(item, getDDContext().getBrowser(), null), (Component)cell);
            cell.addEventListener("onDrop", (EventListener)new Object(this, colIndex));
            cell.addEventListener("onLater", (EventListener)new Object(this, rowIndex));
        }
    }


    private boolean isDDEnabled()
    {
        if(this.ddEnabled == null)
        {
            String cockpitParameter = UITools.getCockpitParameter("dnd.listview.enable", Executions.getCurrent().getDesktop());
            this.ddEnabled = Boolean.valueOf((cockpitParameter == null || Boolean.parseBoolean(cockpitParameter)));
        }
        return this.ddEnabled.booleanValue();
    }


    private void renderCellAsSelected(Integer colIndex, Integer rowIndex, boolean scrollIntoView)
    {
        if(colIndex.intValue() >= 0 && colIndex.intValue() < this.columnContentBoxes.size() && rowIndex.intValue() >= 0 && rowIndex
                        .intValue() < getModel().getListComponentModel().getListModel().size())
        {
            HtmlBasedComponent cell = getCellComponentAt(colIndex.intValue(), rowIndex.intValue());
            if(cell != null)
            {
                cell.setTooltiptext(Labels.getLabel("listview.tooltip.editcell"));
                Collection<Integer> activationIndexes = getActivationIndexes();
                if(activationIndexes.contains(rowIndex))
                {
                    UITools.modifySClass(cell, this.activatedRowSclass, true);
                }
                UITools.modifySClass(cell, this.selectedCellSclass, true);
                UITools.modifySClass(cell, this.defaultCellSclass, false);
                if(scrollIntoView)
                {
                    appendScrollIntoViewComponent(cell);
                }
                else
                {
                    removeScrollIntoViewComponent(cell);
                }
            }
        }
    }


    private void renderCellAsActivated(Integer colIndex, Integer rowIndex, boolean scrollIntoView)
    {
        if(colIndex.intValue() >= 0 && colIndex.intValue() < this.columnContentBoxes.size() && rowIndex.intValue() >= 0 && rowIndex
                        .intValue() < getModel().getListComponentModel().getListModel().size())
        {
            HtmlBasedComponent cell = getCellComponentAt(colIndex.intValue(), rowIndex.intValue());
            if(cell != null)
            {
                Collection<Integer> activationIndexes = getActivationIndexes();
                if(activationIndexes.contains(rowIndex))
                {
                    UITools.modifySClass(cell, this.activatedRowSclass, true);
                }
            }
        }
    }


    private void renderCellsAsSelected(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        boolean scroll = (rowIndexes.size() == 1);
        for(Integer colIndex : colIndexes)
        {
            for(Integer rowIndex : rowIndexes)
            {
                renderCellAsSelected(colIndex, rowIndex, scroll);
            }
        }
    }


    private void renderCellsAsActivated(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        boolean scroll = (rowIndexes.size() == 1);
        for(Integer colIndex : colIndexes)
        {
            for(Integer rowIndex : rowIndexes)
            {
                renderCellAsActivated(colIndex, rowIndex, scroll);
            }
        }
    }


    private void renderColumnHeaderAsDefault(Integer colIndex)
    {
        if(colIndex.intValue() >= 0 && colIndex.intValue() < this.columnHeaderBoxes.size())
        {
            HtmlBasedComponent cell = (HtmlBasedComponent)this.columnHeaderBoxes.get(colIndex.intValue());
            cell.setSclass("listColumnHeader");
        }
    }


    private void renderColumnHeaderAsSorted(Integer colIndex, boolean asc)
    {
        if(colIndex.intValue() >= 0 && colIndex.intValue() < this.columnHeaderBoxes.size())
        {
            HtmlBasedComponent cell = (HtmlBasedComponent)this.columnHeaderBoxes.get(colIndex.intValue());
            if(asc)
            {
                cell.setSclass("listColumnHeader listColumnHeaderAsc");
            }
            else
            {
                cell.setSclass("listColumnHeader listColumnHeaderDesc");
            }
        }
    }


    private void renderColumns(List<ColumnDescriptor> columns)
    {
        renderColumns(columns, null);
    }


    private void renderColumns(List<ColumnDescriptor> columns, List<Div> columnContentDivList)
    {
        this.columnContentBoxes.clear();
        this.menupopupContainer.getChildren().clear();
        for(int i = 0; i < columns.size(); i++)
        {
            int index = i;
            Div column = new Div();
            column.setDroppable("listViewColumnDnd");
            column.addEventListener("onDrop", (EventListener)new Object(this));
            column.addEventListener("onLater", (EventListener)new Object(this, index));
            this.mainColumnComponent.appendColumn((Component)column);
            column.setSclass("listColumn");
            if(column.getParent() instanceof HtmlBasedComponent)
            {
                UITools.modifySClass((HtmlBasedComponent)column.getParent(), "columnIndex_" + index, true);
            }
            if(this.showColumnHeaders)
            {
                ColumnDescriptor colDescr = columns.get(i);
                Div cellDiv = new Div();
                this.columnHeaderBoxes.add(cellDiv);
                column.appendChild((Component)cellDiv);
                cellDiv.setSclass("listColumnHeader");
                Component columnContainer = column.getParent();
                if(columnContainer != null)
                {
                    columnContainer.addEventListener("onUser", (EventListener)new Object(this, cellDiv));
                }
                if(isEditMode())
                {
                    cellDiv.setDraggable("listViewColumnDnd");
                }
                cellDiv.setDroppable("listViewColumnDnd");
                addStopEditListener((XulElement)cellDiv);
                cellDiv.addEventListener("onDrop", (EventListener)new Object(this));
                cellDiv.addEventListener("onLater", (EventListener)new Object(this, cellDiv));
                cellDiv.addEventListener("onClick", (EventListener)new Object(this, cellDiv));
                Label cell = new Label(colDescr.getName());
                cellDiv.appendChild((Component)cell);
                if(getMenuPopupBuilder() == null)
                {
                    String headerPopupBean = null;
                    try
                    {
                        ColumnModel columnComponentModel = getModel().getColumnComponentModel();
                        if(columnComponentModel instanceof DefaultColumnModel)
                        {
                            headerPopupBean = ((DefaultColumnModel)columnComponentModel).getConfiguration().getHeaderPopupBean();
                        }
                    }
                    catch(Exception e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.error("", e);
                        }
                    }
                    finally
                    {
                        LOG.error("Could not get menu popup builder, maybe headerPopupBean with ID '" + headerPopupBean + "' is not defined.");
                    }
                }
                else
                {
                    int columnIndex = getColumnIndexOfCellDiv((Component)cellDiv);
                    Menupopup headerPopup = createHeaderMenu(colDescr, columnIndex);
                    this.menupopupContainer.appendChild((Component)headerPopup);
                    String key = "_headerPopup" + columnIndex;
                    Object oldPopup = this.menupopupContainer.getAttribute(key);
                    if(oldPopup instanceof Menupopup)
                    {
                        ((Menupopup)oldPopup).detach();
                    }
                    this.menupopupContainer.setAttribute(key, headerPopup);
                    cellDiv.setContext((Popup)headerPopup);
                }
            }
            if(columnContentDivList == null)
            {
                Div columnContentDiv = renderColumnContentDiv();
                column.appendChild((Component)columnContentDiv);
            }
            else
            {
                column.appendChild((Component)columnContentDivList.get(i));
                this.columnContentBoxes.add(columnContentDivList.get(i));
            }
        }
    }


    protected void addStopEditListener(XulElement headerDiv)
    {
        headerDiv.setAction("onmouseup: comm.sendUser(#{self}, 'MOUSE_UP')");
        headerDiv.addEventListener("onUser", (EventListener)new Object(this));
    }


    private void renderRowAsSelected(Integer rowIndex)
    {
        for(int colIndex = 0; colIndex < this.columnContentBoxes.size(); colIndex++)
        {
            HtmlBasedComponent cell = getCellComponentAt(colIndex, rowIndex.intValue());
            if(cell != null)
            {
                Collection<Integer> activationIndexes = getActivationIndexes();
                if(activationIndexes.contains(rowIndex))
                {
                    UITools.modifySClass(cell, this.activatedRowSclass, true);
                }
                UITools.modifySClass(cell, this.selectedRowSclass, true);
                UITools.modifySClass(cell, this.defaultCellSclass, false);
            }
        }
    }


    private void renderRows()
    {
        for(Div columnContentBox : this.columnContentBoxes)
        {
            columnContentBox.getFirstChild().getChildren().clear();
        }
        if(this.loadingProgressContainer != null && getLazyloadPackageSize() > 0)
        {
            this.loadingProgressContainer.reset();
            this.lastRenderedStartIndex = -1;
            this.loadingProgressContainer.setVisible(true);
        }
        continueRenderRows(0, getInitialPackageSize());
        this.mainColumnComponent.adjustColumnWidths(null);
    }


    private void showActivatedItems()
    {
        for(Integer rowIndex : this.activationIndexes)
        {
            updateRowState(rowIndex);
        }
    }


    private int updateItemInternal(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        int ret = 0;
        if(this.initialized && !isEditing())
        {
            if(item != null)
            {
                int rowIndex = getModel().getListComponentModel().getListModel().getElements().indexOf(item);
                if(rowIndex > -1)
                {
                    updateRow(rowIndex);
                    ret++;
                }
            }
        }
        return ret;
    }


    private void updateViewColumns()
    {
        updateViewColumns(null);
    }


    private void updateViewColumns(Integer colIndex)
    {
        List<ColumnDescriptor> modelColumns = getModel().getColumnComponentModel().getVisibleColumns();
        List<Div> newModelColumns = new ArrayList<>();
        List<ColumnDescriptor> newModelColDescriptors = new ArrayList<>();
        for(int i = 0; i < modelColumns.size(); i++)
        {
            ColumnDescriptor visibleColumn = modelColumns.get(i);
            int columnIndex = this.visibleColumnsInternal.indexOf(visibleColumn);
            if(columnIndex >= 0)
            {
                renderColumnHeaderAsDefault(Integer.valueOf(columnIndex));
                newModelColDescriptors.add(visibleColumn);
                newModelColumns.add(this.columnContentBoxes.get(columnIndex));
            }
            else
            {
                Div columnContentDiv = renderColumnContentDiv();
                if(colIndex == null)
                {
                    newModelColumns.add(columnContentDiv);
                }
                else
                {
                    newModelColDescriptors.add(colIndex.intValue() + 1, modelColumns.get(i));
                    newModelColumns.add(colIndex.intValue() + 1, columnContentDiv);
                }
                for(int j = 0; j < getModel().getListComponentModel().getListModel().getElements().size(); j++)
                {
                    renderCell(i, j);
                }
            }
        }
        renderColumnHeaderAsSorted(Integer.valueOf(getModel().getColumnComponentModel().getSortedByColumnIndex()),
                        getModel().getColumnComponentModel().isSortAscending());
        this.mainColumnComponent.clear();
        renderColumns((colIndex == null) ? modelColumns : newModelColDescriptors, newModelColumns);
        this.mainColumnComponent.setWidths(getColumnWidths());
        this.mainColumnComponent.adjustColumnWidths(null);
        this.visibleColumnsInternal.clear();
        this.visibleColumnsInternal.addAll((colIndex == null) ? modelColumns : newModelColDescriptors);
        if(colIndex != null)
        {
            ColumnModel columnModel = getModel().getColumnComponentModel();
            if(columnModel instanceof MutableColumnModel)
            {
                ((MutableColumnModel)columnModel).setVisibleColumns(newModelColDescriptors, true);
            }
        }
    }


    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }


    public boolean isEditMode()
    {
        return this.editMode;
    }


    protected CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    protected ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }
}
