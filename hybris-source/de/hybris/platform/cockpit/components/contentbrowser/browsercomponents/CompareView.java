package de.hybris.platform.cockpit.components.contentbrowser.browsercomponents;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.PropertyComparisonInfo;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.services.values.impl.ObjectValuePair;
import de.hybris.platform.cockpit.session.EditableComponent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.ListActionHelper;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.model.ModelService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class CompareView extends Div implements EditableComponent
{
    private static final String COMPARE_ITEM = "compareItem";
    private static final Logger LOG = LoggerFactory.getLogger(CompareView.class);
    private static final String COMPARE_VALUE_LABEL = "compareValueLabel";
    private static final String COMPARE_VIEW_CELL = "compareViewCell";
    private static final String COMPARE_ROW_LABEL = "compareRowLabel";
    private static final String PRODUCT_REFERENCES = "Product.productReferences";
    private static final String PRODUCT_PRICES = "prices";
    private static final String PRODUCT_DISCOUNTS = "discounts";
    private static final String PRODUCT_VARIANTS = "Product.variants";
    private static final String PRODUCT_KEYWORDS = "Product.keywords";
    private static final String PRODUCT_DESCRIPTION = "Product.description";
    private static final String OPEN_BROWSER_EDITOR_IMG = "cockpit/images/icon_func_open_list.png";
    private static final String OPEN_REFERENCE_WIZARD_IMG = "cockpit/images/icon_func_new_available.png";
    private TypedObject referenceObject = null;
    private List<TypedObject> allItems = null;
    private Map<PropertyDescriptor, PropertyComparisonInfo> comparedAttributes = null;
    private EditorConfiguration configuration = null;
    private final Map<EditorSectionConfiguration, Boolean> sectionStatusMap = new HashMap<>();
    private final Map<PropertyDescriptor, Boolean> locPropertyStatusMap = new HashMap<>();
    private CompareViewListener listener = null;
    private final Map<ObjectValuePair, Component> cellMap = new HashMap<>();
    private final Map<PropertyDescriptor, Component> rowMap = new HashMap<>();
    private ObjectValuePair currentlyEditedValue = null;
    private CockpitValidationService validationService;
    private ValueService valueService;
    private ModelService modelService;
    private TypeService typeService;
    private static final Set<String> ATOMIC_EDITABLE_EDITOR_TYPES = new HashSet<>(Arrays.asList(new String[] {"BOOLEAN", "DATE", "DECIMAL", "INTEGER", "LONG", "TEXT"}));
    protected TypedObject rootObject;
    protected SearchType rootType;
    protected PropertyDescriptor propertyDescriptor;
    protected Collection<Object> editedObjectCollection;


    public CompareView(TypedObject sourceObject, List<TypedObject> allItems, Map<PropertyDescriptor, PropertyComparisonInfo> comparedAttributes, CompareViewListener listener)
    {
        this.referenceObject = sourceObject;
        this.allItems = new ArrayList<>(allItems);
        this.comparedAttributes = comparedAttributes;
        this.listener = listener;
    }


    public void setWidth(String width)
    {
        LOG.warn("Explicitly setting width is not supported by this component.");
    }


    public void initialize()
    {
        this.cellMap.clear();
        this.rowMap.clear();
        this.currentlyEditedValue = null;
        this.sectionStatusMap.clear();
        this.locPropertyStatusMap.clear();
        render();
    }


    protected void render()
    {
        getChildren().clear();
        setStyle("position: relative;");
        setSclass("compareView");
        renderHeader((Component)this);
        if(getConfiguration() == null)
        {
            appendChild((Component)createSection(Labels.getLabel("browser.viewmode.compare.diffsection.label"), new ArrayList<>(this.comparedAttributes
                            .keySet()), true, true));
        }
        else
        {
            for(EditorSectionConfiguration sectionConf : getConfiguration().getSections())
            {
                if(sectionConf instanceof DefaultEditorSectionConfiguration)
                {
                    List<EditorRowConfiguration> sectionRows = sectionConf.getSectionRows();
                    List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
                    for(EditorRowConfiguration editorRowConfiguration : sectionRows)
                    {
                        propertyDescriptors.add(editorRowConfiguration.getPropertyDescriptor());
                    }
                    if(!propertyDescriptors.isEmpty())
                    {
                        boolean open = false;
                        Boolean sectionStatus = this.sectionStatusMap.get(sectionConf);
                        if(sectionStatus == null)
                        {
                            open = sectionConf.isInitiallyOpened();
                            this.sectionStatusMap.put(sectionConf, Boolean.valueOf(open));
                        }
                        else
                        {
                            open = sectionStatus.booleanValue();
                        }
                        String label = (sectionConf instanceof DefaultEditorSectionConfiguration) ? ((DefaultEditorSectionConfiguration)sectionConf).getLabelWithFallback() : sectionConf.getLabel();
                        Groupbox groupbox = createSection(label, propertyDescriptors, false, open);
                        groupbox.addEventListener("onOpen", (EventListener)new Object(this, sectionConf));
                        appendChild((Component)groupbox);
                    }
                }
            }
        }
    }


    private Groupbox createSection(String label, List<PropertyDescriptor> descriptors, boolean hideSameValuedProperties, boolean open)
    {
        CompareGroupbox groupbox = new CompareGroupbox(this, label, getAllItems().size());
        groupbox.setWidth("100%");
        groupbox.setOpen(open);
        groupbox.addEventListener("onOpen", (EventListener)new Object(this, groupbox));
        for(PropertyDescriptor propertyDescriptor : descriptors)
        {
            PropertyComparisonInfo propCmpInfo = this.comparedAttributes.get(propertyDescriptor);
            if(propCmpInfo == null)
            {
                LOG.error("No property comparison info found for property descriptor " + propertyDescriptor.getQualifier());
                continue;
            }
            if(!hideSameValuedProperties || propCmpInfo.hasDifferences())
            {
                BitSet changedItemsInRow = new BitSet(getAllItems().size());
                Div compareRowCnt = new Div();
                HtmlBasedComponent compareRow = createCompareRow(propertyDescriptor, propCmpInfo, changedItemsInRow);
                compareRowCnt.appendChild((Component)compareRow);
                registerRowComponent((Component)compareRowCnt, propertyDescriptor);
                groupbox.appendLazyloadChild((Component)compareRowCnt);
                groupbox.addDiffInfo(propertyDescriptor, changedItemsInRow);
            }
        }
        groupbox.updateHeader();
        if(open)
        {
            groupbox.createLazyloadContent();
        }
        return (Groupbox)groupbox;
    }


    private void renderValue(Component parent, PropertyDescriptor propertyDescriptor, ObjectValuePair valueInfo, boolean different, Set<String> writeableLangIsos, String langiso)
    {
        ObjectValueContainer.ObjectValueHolder valueHolder = valueInfo.getValueHolder();
        if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            String attrQuali;
            ItemAttributePropertyDescriptor itemAttPd = (ItemAttributePropertyDescriptor)propertyDescriptor;
            if(itemAttPd.isSingleAttribute())
            {
                attrQuali = itemAttPd.getAttributeQualifier();
            }
            else
            {
                AttributeDescriptorModel attDm = ((ItemAttributePropertyDescriptor)propertyDescriptor).getLastAttributeDescriptor();
                attrQuali = attDm.getQualifier();
            }
            ComposedTypeModel composedTypeModel = ((ItemType)valueInfo.getObject().getType()).getComposedType();
            ComposedType composedType = (ComposedType)getModelService().getSource(composedTypeModel);
            if(!composedType.hasAttribute(attrQuali))
            {
                return;
            }
        }
        boolean writable = UISessionUtils.getCurrentSession().getUiAccessRightService().isWritable((ObjectType)valueInfo.getObject().getType(), valueInfo.getObject(), propertyDescriptor, false);
        boolean editable = writable;
        if(editable && propertyDescriptor.isLocalized() && langiso != null)
        {
            editable = (writeableLangIsos != null && writeableLangIsos.contains(langiso));
        }
        boolean readonly = (valueHolder == null || !editable);
        parent.getChildren().clear();
        if(valueInfo.equals(this.currentlyEditedValue))
        {
            this.currentlyEditedValue = null;
        }
        StringBuffer sclass = new StringBuffer(100);
        if(different)
        {
            sclass.append("cmp_diff_cell");
        }
        if(valueInfo.getObject().equals(getReferenceObject()))
        {
            sclass.append(" cmp_ref_cell");
        }
        if(readonly)
        {
            sclass.append(" cmp_readonly_cell");
        }
        if(valueHolder == null)
        {
            Div naDiv = new Div();
            naDiv.setSclass("compareViewCell");
            naDiv.appendChild((Component)new Label(Labels.getLabel("browser.viewmode.compare.propertynotavailable")));
            parent.appendChild((Component)naDiv);
            sclass.append(" cmp_attr_unavailable");
        }
        else
        {
            String text = null;
            Object value = valueHolder.getCurrentValue();
            Div div = new Div();
            div.setSclass("compareViewCell");
            if("REFERENCE".equals(propertyDescriptor.getEditorType()) || "FEATURE"
                            .equals(propertyDescriptor.getEditorType()) || "ENUM"
                            .equals(propertyDescriptor.getEditorType()))
            {
                if(ValueHandler.NOT_READABLE_VALUE.equals(value))
                {
                    UITools.modifySClass((HtmlBasedComponent)div, "listview_notreadable_cell", true);
                    text = Labels.getLabel("listview.cell.readprotected");
                }
                else if(value != null)
                {
                    LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
                    text = TypeTools.getValueAsString(labelService, value);
                }
            }
            else
            {
                text = (value == null) ? "" : value.toString();
            }
            Label label = new Label(text);
            label.setSclass("compareValueLabel");
            div.appendChild((Component)label);
            parent.appendChild((Component)div);
            StringBuilder tooltip = new StringBuilder();
            if(StringUtils.isNotBlank(propertyDescriptor.getName()))
            {
                tooltip.append(propertyDescriptor.getName());
            }
            if(StringUtils.isNotBlank(valueHolder.getLanguageIso()))
            {
                tooltip.append(" [" + valueHolder.getLanguageIso() + "]");
            }
            UITools.setTooltipText(parent, tooltip.toString());
            registerCellComponent(parent, valueInfo);
            if(!readonly)
            {
                parent.addEventListener("onClick", (EventListener)new Object(this, valueInfo));
            }
        }
        if(parent instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)parent).setSclass(sclass.toString());
        }
        else if(parent instanceof AbstractTag)
        {
            ((AbstractTag)parent).setSclass(sclass.toString());
        }
    }


    private void registerCellComponent(Component comp, ObjectValuePair valueInfo)
    {
        this.cellMap.put(valueInfo, comp);
    }


    private void registerRowComponent(Component comp, PropertyDescriptor propertyDescriptor)
    {
        this.rowMap.put(propertyDescriptor, comp);
    }


    private EditorRowConfiguration getConfigForProperty(PropertyDescriptor descriptor)
    {
        if(getConfiguration() != null)
        {
            for(EditorSectionConfiguration secConf : getConfiguration().getSections())
            {
                for(EditorRowConfiguration row : secConf.getSectionRows())
                {
                    if(row.getPropertyDescriptor().equals(descriptor))
                    {
                        return row;
                    }
                }
            }
        }
        return null;
    }


    private void updateRow(PropertyDescriptor descriptor)
    {
        Component component = this.rowMap.get(descriptor);
        PropertyComparisonInfo propertyComparisonInfo = this.comparedAttributes.get(descriptor);
        if(component != null)
        {
            component.getChildren().clear();
            BitSet changeSet = new BitSet();
            component.appendChild((Component)createCompareRow(descriptor, propertyComparisonInfo, changeSet));
            CompareGroupbox groupbox = (CompareGroupbox)UITools.getNextParentOfType(CompareGroupbox.class, component, 20);
            if(groupbox != null)
            {
                groupbox.updateHeader();
            }
        }
    }


    public void stopEditing()
    {
        if(this.currentlyEditedValue != null)
        {
            updateRow(this.currentlyEditedValue.getValueHolder().getPropertyDescriptor());
            this.currentlyEditedValue = null;
        }
    }


    private void editValue(ObjectValuePair valueInfo)
    {
        stopEditing();
        Component component = this.cellMap.get(valueInfo);
        if(component == null)
        {
            LOG.error("Could not find component for valueInfo (" + valueInfo.getObject() + "," + valueInfo.getValueHolder() + ")");
        }
        else
        {
            Map<String, ? extends Object> parameters;
            component.getChildren().clear();
            String editorCode = null;
            Object initValue = valueInfo.getValueHolder().getCurrentValue();
            EditorRowConfiguration configForProperty = getConfigForProperty(valueInfo.getValueHolder().getPropertyDescriptor());
            if(configForProperty == null)
            {
                parameters = Collections.EMPTY_MAP;
            }
            else
            {
                editorCode = configForProperty.getEditor();
                parameters = configForProperty.getParameters();
            }
            if(StringUtils.isBlank(editorCode) && initValue instanceof Map)
            {
                editorCode = "mapEditor";
            }
            this.propertyDescriptor = valueInfo.getValueHolder().getPropertyDescriptor();
            UIEditor uiEditor = EditorHelper.getUIEditor(this.propertyDescriptor, editorCode);
            if(uiEditor instanceof ListUIEditor)
            {
                EditorHelper.setAvailableValuesInListEditor((ListUIEditor)uiEditor, this.propertyDescriptor, parameters);
            }
            else if(uiEditor instanceof ReferenceUIEditor)
            {
                EditorHelper.applyReferenceRelatedAttributes((ReferenceUIEditor)uiEditor, this.propertyDescriptor, parameters, valueInfo
                                                .getObject(), valueInfo.getValueHolder().getCurrentValue(), valueInfo.getValueHolder().getLanguageIso(), null,
                                UISessionUtils.getCurrentSession());
            }
            Map<String, Object> params = new HashMap<>(parameters);
            params.put("propertyDescriptor", this.propertyDescriptor);
            params.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
            params.put("attributeQualifier", this.propertyDescriptor.getQualifier());
            boolean setRootSearchType = true;
            if(this.propertyDescriptor instanceof ItemAttributePropertyDescriptor)
            {
                AttributeDescriptorModel attrDescr = ((ItemAttributePropertyDescriptor)this.propertyDescriptor).getLastAttributeDescriptor();
                if(attrDescr != null && attrDescr.getAttributeType() instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
                {
                    setRootSearchType = false;
                }
                else if(attrDescr != null && attrDescr.getAttributeType() instanceof MapTypeModel)
                {
                    MapTypeModel mapTypeModel = (MapTypeModel)attrDescr.getAttributeType();
                    if(mapTypeModel.getReturntype() instanceof de.hybris.platform.core.model.type.AtomicTypeModel || (mapTypeModel
                                    .getReturntype() instanceof MapTypeModel && ((MapTypeModel)mapTypeModel.getReturntype())
                                    .getReturntype() instanceof de.hybris.platform.core.model.type.AtomicTypeModel))
                    {
                        setRootSearchType = false;
                    }
                }
            }
            this.rootObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(valueInfo.getObject());
            this
                            .rootType = setRootSearchType ? EditorHelper.getRootSearchType(valueInfo.getValueHolder().getPropertyDescriptor(), UISessionUtils.getCurrentSession()) : null;
            CreateContext createContext = new CreateContext((ObjectType)this.rootType, this.rootObject, this.propertyDescriptor, null);
            params.put("createContext", createContext);
            params.put("disableOnBlur", Boolean.TRUE);
            if(uiEditor instanceof de.hybris.platform.cockpit.model.editor.WysiwygUIEditor)
            {
                params.put("rows", "8");
            }
            Set<String> forceWritePks = new HashSet<>();
            Div editorContainer = new Div();
            if(initValue instanceof Collection)
            {
                this.editedObjectCollection = (Collection<Object>)initValue;
            }
            else
            {
                this.editedObjectCollection = null;
            }
            Object object1 = new Object(this, initValue, valueInfo, uiEditor, editorContainer, forceWritePks, params);
            HtmlBasedComponent viewComponent = uiEditor.createViewComponent(initValue, params, (EditorListener)object1);
            editorContainer.setSclass("cmp_editor_cnt");
            if(uiEditor instanceof ReferenceUIEditor && (this.propertyDescriptor
                            .getQualifier().equals("Product.productReferences") || this.propertyDescriptor
                            .getQualifier().toLowerCase().contains("prices") || this.propertyDescriptor
                            .getQualifier().toLowerCase().contains("discounts") || this.propertyDescriptor
                            .getQualifier().equals("Product.variants") || this.propertyDescriptor.getQualifier().equals("Product.keywords")))
            {
                Object object = new Object(this, (EditorListener)object1);
                Hbox hbox = new Hbox();
                hbox.appendChild((Component)viewComponent);
                hbox.setStyle("table-layout:fixed;");
                hbox.setWidth("100%");
                hbox.setSpacing("0");
                hbox.setWidths("none, 18px, 18px");
                Image imgOpenNewReferenceWizard = new Image("cockpit/images/icon_func_new_available.png");
                imgOpenNewReferenceWizard.setTooltiptext(Labels.getLabel("editorarea.button.createnewitem"));
                imgOpenNewReferenceWizard.setStyle("width:18px; height: 20px");
                UITools.addBusyListener((Component)imgOpenNewReferenceWizard, "onClick", (EventListener)new NewReferenceItemActionEventListener(this), null, "editor.openincontext.busy");
                hbox.appendChild((Component)imgOpenNewReferenceWizard);
                Image imgOpenExternal = new Image("cockpit/images/icon_func_open_list.png");
                imgOpenExternal.setTooltiptext(Labels.getLabel("editor.button.referenceeditor.open.tooltip"));
                imgOpenExternal.setStyle("width:18px; height: 20px");
                UITools.addBusyListener((Component)imgOpenExternal, "onClick", (EventListener)object, null, "editor.openincontext.busy");
                hbox.appendChild((Component)imgOpenExternal);
                Div wrapperDiv = new Div();
                wrapperDiv.appendChild((Component)hbox);
                editorContainer.appendChild((Component)wrapperDiv);
            }
            else
            {
                editorContainer.appendChild((Component)viewComponent);
            }
            Div cellDiv = new Div();
            cellDiv.addEventListener("onClick", (EventListener)new Object(this));
            cellDiv.setSclass("compareViewCell");
            component.appendChild((Component)cellDiv);
            cellDiv.appendChild((Component)editorContainer);
            this.currentlyEditedValue = valueInfo;
            uiEditor.setFocus(viewComponent, false);
        }
    }


    private HtmlBasedComponent createCompareRow(PropertyDescriptor propertyDescriptor, PropertyComparisonInfo propCmpInfo, BitSet changeSet)
    {
        CompareLocalizedAttributeDiv ret = new CompareLocalizedAttributeDiv(this, (OpenChangeListener)new Object(this, propertyDescriptor));
        ret.setStyle("position: relative;");
        Set<String> writeableLangIsos = UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguageIsos();
        List<String> langisos = new ArrayList<>();
        if(propertyDescriptor.isLocalized())
        {
            langisos.addAll(UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
        }
        else
        {
            langisos.add(null);
        }
        for(String langiso : langisos)
        {
            List<ObjectValuePair> allValueHolders = new ArrayList<>();
            Set<ObjectValuePair> diffValueHolders = new HashSet<>();
            for(TypedObject item : getAllItems())
            {
                if(item.equals(getReferenceObject()))
                {
                    allValueHolders.add(new ObjectValuePair(item, propCmpInfo.getReferenceValueHolder(langiso)));
                    continue;
                }
                ObjectValueContainer.ObjectValueHolder compareValueHolder = propCmpInfo.getCompareValueHolder(item, langiso);
                ObjectValuePair objectValuePair = new ObjectValuePair(item, compareValueHolder);
                allValueHolders.add(objectValuePair);
                if(propCmpInfo.getItemsWithDifferences(langiso).contains(item))
                {
                    diffValueHolders.add(objectValuePair);
                }
            }
            CompareRowComponent cmpRow = new CompareRowComponent(this, getAllItems().size());
            Div labelDiv = new Div();
            labelDiv.setSclass("compareViewCell");
            Hbox labelPosBox = new Hbox();
            labelPosBox.setSclass("compareViewCell_box");
            String rowLabel = (propertyDescriptor.getName() == null) ? propertyDescriptor.getQualifier() : propertyDescriptor.getName();
            Label label = new Label(rowLabel);
            label.setSclass("compareRowLabel");
            labelPosBox.appendChild((Component)label);
            labelPosBox.setWidths("none, 55px");
            if(langiso != null)
            {
                Label isoLabel = new Label("[" + langiso + "]");
                isoLabel.setSclass("cmpIsoLabel");
                labelPosBox.appendChild((Component)isoLabel);
            }
            labelDiv.appendChild((Component)labelPosBox);
            labelDiv.setTooltiptext(propertyDescriptor.getQualifier());
            cmpRow.getLabelCell().appendChild((Component)labelDiv);
            cmpRow.getFirstSep().appendChild((Component)new Div());
            Iterator<ObjectValuePair> iterator = allValueHolders.iterator();
            ObjectValuePair lastValue = iterator.next();
            boolean valueDifferent = diffValueHolders.contains(lastValue);
            if(valueDifferent)
            {
                changeSet.set(0);
            }
            renderValue(cmpRow.getValueCell(0), propertyDescriptor, lastValue, valueDifferent, writeableLangIsos, langiso);
            int index = 1;
            while(iterator.hasNext())
            {
                ObjectValuePair objectValue = iterator.next();
                cmpRow.getActionCell(index - 1).appendChild((Component)new Div());
                valueDifferent = diffValueHolders.contains(objectValue);
                if(valueDifferent)
                {
                    changeSet.set(index);
                }
                renderValue(cmpRow.getValueCell(index), propertyDescriptor, objectValue, valueDifferent, writeableLangIsos, langiso);
                lastValue = objectValue;
                index++;
            }
            ret.addCompareRow(cmpRow, langiso);
            if(propCmpInfo.hasDifferences())
            {
                UITools.modifySClass((HtmlBasedComponent)ret, "compareRowDifferent", true);
                for(int i = 0; i < allValueHolders.size(); i++)
                {
                    Component valueCell = cmpRow.getValueCell(i).getFirstChild();
                    if(valueCell instanceof HtmlBasedComponent)
                    {
                        String dd_id;
                        boolean editable = true;
                        ObjectValuePair objectValuePair = allValueHolders.get(i);
                        if(ATOMIC_EDITABLE_EDITOR_TYPES.contains(propertyDescriptor.getEditorType()))
                        {
                            dd_id = propertyDescriptor.getEditorType();
                        }
                        else
                        {
                            dd_id = propertyDescriptor.getEditorType() + "_" + propertyDescriptor.getEditorType();
                            if(TypeTools.isPartof(propertyDescriptor))
                            {
                                editable = false;
                            }
                        }
                        if(objectValuePair.getValueHolder() != null)
                        {
                            if(UISessionUtils.getCurrentSession().getUiAccessRightService()
                                            .isReadable((ObjectType)objectValuePair.getObject().getType(), propertyDescriptor, false))
                            {
                                ((HtmlBasedComponent)valueCell).setDraggable(dd_id);
                            }
                            if(editable && propertyDescriptor.isLocalized() && langiso != null)
                            {
                                editable = (writeableLangIsos != null && writeableLangIsos.contains(langiso));
                            }
                            if(editable)
                            {
                                if(UISessionUtils.getCurrentSession()
                                                .getUiAccessRightService()
                                                .isWritable((ObjectType)objectValuePair.getObject().getType(), objectValuePair.getObject(), propertyDescriptor, false))
                                {
                                    ((HtmlBasedComponent)valueCell).setDroppable(dd_id);
                                }
                            }
                        }
                        ObjectValueContainer.ObjectValueHolder valueHolder = objectValuePair.getValueHolder();
                        if(valueHolder != null)
                        {
                            ((HtmlBasedComponent)valueCell).setAttribute("value", valueHolder.getCurrentValue());
                        }
                        ((HtmlBasedComponent)valueCell).addEventListener("onDrop", (EventListener)new Object(this, valueHolder, objectValuePair));
                    }
                }
            }
        }
        if(Boolean.TRUE.equals(this.locPropertyStatusMap.get(propertyDescriptor)))
        {
            ret.open();
        }
        else
        {
            ret.close();
        }
        return (HtmlBasedComponent)ret;
    }


    private void addDDFunctionality(HtmlBasedComponent cmp, TypedObject item)
    {
        cmp.setDraggable("compareItem");
        cmp.setDroppable("compareItem");
        cmp.setAttribute("compareItem", item);
        cmp.addEventListener("onDrop", (EventListener)new Object(this, item));
    }


    private void renderLabelRepresentation(Component parent, TypedObject item)
    {
        Div labelCnt = new Div();
        labelCnt.setStyle("position: relative;");
        Hbox hbox = new Hbox();
        hbox.setWidths("65px");
        hbox.setSclass("compareItemHeader");
        addDDFunctionality((HtmlBasedComponent)labelCnt, item);
        labelCnt.appendChild((Component)hbox);
        parent.appendChild((Component)labelCnt);
        if(item.equals(getReferenceObject()))
        {
            UITools.modifySClass((HtmlBasedComponent)hbox, "compareReference", true);
        }
        Toolbarbutton removeBtn = new Toolbarbutton("", "/cockpit/images/remove.png");
        removeBtn.setSclass("compareRemove");
        removeBtn.setTooltiptext(Labels.getLabel("general.remove"));
        removeBtn.addEventListener("onClick", (EventListener)new Object(this, item));
        labelCnt.appendChild((Component)removeBtn);
        String imageUrl = UISessionUtils.getCurrentSession().getLabelService().getObjectIconPath(item);
        String displayImgUrl = "cockpit/images/stop_klein.jpg";
        if(imageUrl != null && !imageUrl.isEmpty())
        {
            boolean absolute = false;
            try
            {
                URI uri = new URI(imageUrl);
                absolute = uri.isAbsolute();
                displayImgUrl = absolute ? imageUrl : ("~" + imageUrl);
                displayImgUrl = StringUtils.contains(displayImgUrl, "?") ? displayImgUrl.concat("&") : displayImgUrl.concat("?");
                displayImgUrl = displayImgUrl.concat(RandomStringUtils.randomAlphanumeric(5));
            }
            catch(URISyntaxException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        Div imgDiv = new Div();
        imgDiv.setSclass("section-label-image");
        Image img = new Image(displayImgUrl);
        img.setSclass("section-label-image");
        imgDiv.appendChild((Component)img);
        String labelText = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(item);
        hbox.appendChild((Component)imgDiv);
        Div labelAndToolbarContainer = new Div();
        labelAndToolbarContainer.setSclass("compareLabelCnt");
        hbox.appendChild((Component)labelAndToolbarContainer);
        Div actionToolbarComponent = new Div();
        actionToolbarComponent.setSclass("cmpActionSlot");
        labelAndToolbarContainer.appendChild((Component)actionToolbarComponent);
        Label label = new Label(labelText);
        label.setSclass("compareLabel");
        labelAndToolbarContainer.appendChild((Component)label);
        renderActions((Component)actionToolbarComponent, item);
    }


    protected void renderActions(Component parent, TypedObject item)
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(item);
        GridViewConfiguration config = (GridViewConfiguration)UISessionUtils.getCurrentSession().getUiConfigurationService().getComponentConfiguration(template, "gridView", GridViewConfiguration.class);
        ActionColumnConfiguration statusConfiguration = (config.getSpecialactionSpringBeanID() == null) ? null : (ActionColumnConfiguration)Registry.getApplicationContext().getBean(config.getSpecialactionSpringBeanID(), ActionColumnConfiguration.class);
        ListActionHelper.renderActions(parent, item, statusConfiguration, "cmpActionImg");
        if(!item.equals(getReferenceObject()))
        {
            Toolbarbutton setRefBtn = new Toolbarbutton("", "/cockpit/images/icon_func_compare_reference.png");
            setRefBtn.setSclass("compareSetRef");
            setRefBtn.setTooltiptext(Labels.getLabel("browser.viewmode.compare.setreference.tooltip"));
            setRefBtn.addEventListener("onClick", (EventListener)new Object(this, item));
            parent.appendChild((Component)setRefBtn);
        }
    }


    private void renderHeader(Component parent)
    {
        CompareRowComponent cmpRow = new CompareRowComponent(this, getAllItems().size());
        cmpRow.setSclass("compareHeaderHbox");
        cmpRow.getLabelCell().appendChild((Component)new Div());
        cmpRow.getFirstSep().appendChild((Component)new Div());
        if(!getAllItems().isEmpty())
        {
            Iterator<TypedObject> iterator = getAllItems().iterator();
            Div labelCnt = new Div();
            labelCnt.setSclass("cmpHeaderLabelCnt");
            cmpRow.getValueCell(0).appendChild((Component)labelCnt);
            renderLabelRepresentation((Component)labelCnt, iterator.next());
            int index = 1;
            while(iterator.hasNext())
            {
                Div actionDiv = new Div();
                cmpRow.getActionCell(index - 1).appendChild((Component)actionDiv);
                labelCnt = new Div();
                labelCnt.setSclass("cmpHeaderLabelCnt");
                cmpRow.getValueCell(index).appendChild((Component)labelCnt);
                renderLabelRepresentation((Component)labelCnt, iterator.next());
                index++;
            }
            if(getAllItems().size() < 3)
            {
                UITools.modifySClass((AbstractTag)cmpRow, "twoCompareItems", true);
            }
        }
        parent.appendChild((Component)cmpRow);
    }


    public void update()
    {
        render();
    }


    public TypedObject getReferenceObject()
    {
        return this.referenceObject;
    }


    public Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes()
    {
        return this.comparedAttributes;
    }


    public void setReferenceObject(TypedObject referenceObject)
    {
        this.referenceObject = referenceObject;
    }


    protected List<TypedObject> getAllItems()
    {
        return (this.allItems == null) ? Collections.EMPTY_LIST : this.allItems;
    }


    public void setItems(List<TypedObject> items)
    {
        this.allItems = items;
    }


    public void setComparedAttributes(Map<PropertyDescriptor, PropertyComparisonInfo> comparedAttributes)
    {
        this.comparedAttributes = comparedAttributes;
    }


    public void setConfiguration(EditorConfiguration configuration)
    {
        if(this.configuration != null && !this.configuration.equals(configuration))
        {
            this.sectionStatusMap.clear();
            this.locPropertyStatusMap.clear();
        }
        this.configuration = configuration;
    }


    public EditorConfiguration getConfiguration()
    {
        return this.configuration;
    }


    protected void fireValueChanged(ObjectValuePair value)
    {
        if(this.listener != null)
        {
            this.listener.valueChanged(value.getObject(), value.getValueHolder());
        }
    }


    protected void fireReferenceItemChanged(TypedObject item)
    {
        if(this.listener != null)
        {
            this.listener.referenceItemChanged(item);
        }
    }


    protected void fireItemDropped(TypedObject dragged, TypedObject target)
    {
        if(this.listener != null)
        {
            this.listener.itemDropped(dragged, target);
        }
    }


    protected void fireItemRemoved(TypedObject item)
    {
        if(this.listener != null)
        {
            this.listener.itemRemoved(item);
        }
    }


    public void setListener(CompareViewListener listener)
    {
        this.listener = listener;
    }


    CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }


    public boolean isEditing()
    {
        return false;
    }


    public void validate(ObjectValuePair valueInfo, PropertyDescriptor propertyDescriptor, UIEditor uiEditor, Object value, Div editorContainer, Set<String> forceWritePks)
    {
        ValidationUIHelper validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
        TypedObject typedObject = valueInfo.getObject();
        String languageIso = valueInfo.getValueHolder().getLanguageIso();
        ObjectValueContainer currentObjectValues = validationUIHelper.createModelFromContainer(typedObject, languageIso, propertyDescriptor, uiEditor);
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
                getValueService().setValues(valueInfo.getObject(), currentObjectValues);
            }
            catch(ValueHandlerException e)
            {
                LOG.debug(e.getMessage(), (Throwable)e);
            }
            valueInfo.getValueHolder().setLocalValue(value);
            fireValueChanged(valueInfo);
        }
        else
        {
            validationUIHelper.createValidationMessages((HtmlBasedComponent)editorContainer, violations, valueInfo.getObject(), forceWritePks, this);
        }
    }


    protected void activateItemInPopupEditor(TypedObject item)
    {
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(allowOverlap())
        {
            currentPerspective.activateItemInPopupEditor(item);
        }
        else if(currentPerspective instanceof BaseUICockpitPerspective &&
                        !((BaseUICockpitPerspective)currentPerspective).isPopupEditorOpen())
        {
            currentPerspective.activateItemInPopupEditor(item);
        }
    }


    private boolean allowOverlap()
    {
        boolean allowOverlap = false;
        try
        {
            allowOverlap = Boolean.parseBoolean(UITools.getCockpitParameter("default.popUpEditor.allowOverlap",
                            Executions.getCurrent()));
        }
        catch(Exception e)
        {
            LOG.warn("Cannot read popupEditorOverlapping property");
        }
        return allowOverlap;
    }


    ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }
}
