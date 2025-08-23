package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.PropertyValueHolder;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRowRenderer;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UICockpitArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class EditorRowRenderer implements SectionRowRenderer
{
    public static final String ATTRIBUTE_QUALIFIER = "attributeQualifier";
    public static final String CREATE_CONTEXT = "createContext";
    public static final String VALUE_CONTAINER = "valueContainer";
    public static final String CURRENT_OBJECT = "currentObject";
    public static final String EDITOR_AREA = "editorArea";
    public static final String SECTION_NAME = "sectionName";
    private static final Logger LOG = LoggerFactory.getLogger(EditorRowRenderer.class);
    private SystemService systemService;
    private TypeService typeService;
    private EditorFactory editorFactory;
    private ModelService modelService;


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    private SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public void setEditorFactory(EditorFactory editorFactory)
    {
        this.editorFactory = editorFactory;
    }


    public EditorFactory getEditorFactory()
    {
        return this.editorFactory;
    }


    public void render(SectionPanel panel, Component parent, SectionRow row, Map<String, Object> ctx)
    {
        if(row instanceof PropertyValueHolder && ((PropertyValueHolder)row).isLocalized())
        {
            renderLocalizedWrapper(panel, (PropertyValueHolder)row, parent, ctx);
        }
        else
        {
            renderSingleEditor(panel, row, parent, null, ctx, null);
        }
    }


    protected void renderSingleEditor(SectionPanel panel, SectionRow sectionRow, Component parent, String isoCode, Map<String, Object> ctx, List<String> availableLangIsos)
    {
        renderSingleEditor(panel, sectionRow, parent, isoCode, ctx, availableLangIsos, availableLangIsos);
    }


    protected void renderSingleEditor(SectionPanel panel, SectionRow sectionRow, Component parent, String isoCode, Map<String, Object> ctx, List<String> readableLangIsos, List<String> writeableLangIsos)
    {
        if(sectionRow instanceof EditorPropertyRow)
        {
            if(readableLangIsos != null && !readableLangIsos.contains(isoCode))
            {
                Label label = new Label(Labels.getLabel("editor.cataloglanguage.hidden", (Object[])new String[] {isoCode}));
                label.setStyle("color:#ccc;");
                label.setParent(parent);
                return;
            }
            Map<String, ? extends Object> parameters = ((EditorPropertyRow)sectionRow).getRowConfiguration().getParameters();
            UIEditorArea editorArea = (UIEditorArea)ctx.get("editorArea");
            TypedObject currentObject = (TypedObject)ctx.get("currentObject");
            ObjectValueContainer currentObjectValues = (ObjectValueContainer)ctx.get("valueContainer");
            if(currentObjectValues == null)
            {
                LOG.error("No value container set in context.");
                return;
            }
            EditorPropertyRow editorPropertyRow = (EditorPropertyRow)sectionRow;
            PropertyDescriptor propertyDescriptor = editorPropertyRow.getRowConfiguration().getPropertyDescriptor();
            PropertyEditorDescriptor edDescr = editorPropertyRow.getRowConfiguration().getEditorDescriptor();
            if(edDescr == null)
            {
                Collection<PropertyEditorDescriptor> matching = getEditorFactory().getMatchingEditorDescriptors(propertyDescriptor
                                .getEditorType());
                edDescr = matching.isEmpty() ? null : matching.iterator().next();
            }
            if(edDescr != null)
            {
                String preferredEditor;
                ObjectValueContainer valueContainer = currentObjectValues;
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(propertyDescriptor, isoCode);
                Object currentValue = valueHolder.getCurrentValue();
                if(panel.isEditMode())
                {
                    preferredEditor = "readonly";
                }
                else
                {
                    preferredEditor = editorPropertyRow.getRowConfiguration().getEditor();
                }
                UIEditor _editor = null;
                if(preferredEditor != null && !preferredEditor.isEmpty())
                {
                    _editor = edDescr.createUIEditor(preferredEditor);
                }
                if(_editor == null)
                {
                    _editor = checkMap(currentValue, propertyDescriptor) ? edDescr.createUIEditor("mapEditor") : (checkSingle(propertyDescriptor) ? edDescr.createUIEditor("single") : edDescr.createUIEditor(PropertyDescriptor.Multiplicity.RANGE.equals(propertyDescriptor
                                    .getMultiplicity()) ? "range" : "multi"));
                }
                UIEditor editor = _editor;
                if(editor != null)
                {
                    editor.setOptional(!PropertyDescriptor.Occurrence.REQUIRED.equals(propertyDescriptor.getOccurence()));
                    editor.setEditable((sectionRow.isEditable() && (writeableLangIsos == null || writeableLangIsos.contains(isoCode))));
                    if(editor instanceof ListUIEditor)
                    {
                        ((ListUIEditor)editor).setAvailableValues(EditorHelper.filterValues(propertyDescriptor, getTypeService()
                                        .getAvailableValues(propertyDescriptor, currentObject)));
                    }
                    Object object1 = new Object(this, editor, propertyDescriptor, currentObject, parameters, valueHolder, writeableLangIsos, isoCode, panel, sectionRow, editorArea);
                    CreateContext createContext = null;
                    if(editor instanceof ReferenceUIEditor)
                    {
                        ReferenceUIEditor referenceEditor = (ReferenceUIEditor)editor;
                        createContext = EditorHelper.applyReferenceRelatedAttributes(referenceEditor, propertyDescriptor, parameters, currentObject, currentValue, isoCode, (UICockpitArea)editorArea,
                                        UISessionUtils.getCurrentSession());
                    }
                    Map<String, Object> customParameters = new HashMap<>(parameters);
                    customParameters.put("attributeQualifier", propertyDescriptor.getQualifier());
                    customParameters.put("languageIso", isoCode);
                    if(!allowOverlap() && !parameters.containsKey("showEditButton"))
                    {
                        customParameters.put("showEditButton",
                                        String.valueOf((editorArea != null &&
                                                        !(editorArea.getEditorAreaController() instanceof de.hybris.platform.cockpit.session.PopupEditorAreaController))));
                    }
                    customParameters.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
                    if(createContext != null)
                    {
                        try
                        {
                            customParameters.put("createContext", createContext.clone());
                        }
                        catch(CloneNotSupportedException e)
                        {
                            LOG.error("Error while cloning create context: ", e);
                        }
                    }
                    if(propertyDescriptor instanceof de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor)
                    {
                        customParameters.put("propertyDescriptor", propertyDescriptor);
                    }
                    customParameters.put("propInfo", propertyDescriptor.getQualifier());
                    customParameters.put("currentObject", ctx.get("currentObject"));
                    HtmlBasedComponent htmlBasedComponent = editor.createViewComponent(currentValue, customParameters, (EditorListener)object1);
                    if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                    {
                        String editorType = "ENUM".equals(editor.getEditorType()) ? "dropdown" : "input";
                        String sectionName = ctx.containsKey("sectionName") ? (String)ctx.get("sectionName") : "unknown";
                        String cockpitID = "EditorArea_" + sectionName + "_" + ((propertyDescriptor.getName() == null) ? "" : propertyDescriptor.getName().replaceAll(" ", "")) + "_" + editorType;
                        UITools.applyTestID((Component)htmlBasedComponent, cockpitID);
                    }
                    parent.appendChild((Component)htmlBasedComponent);
                    Object object2 = new Object(this, editor, (Component)htmlBasedComponent, panel, sectionRow);
                    sectionRow.setFocusListener((SectionRow.FocusListener)object2, (writeableLangIsos != null) ? isoCode : null);
                    if(writeableLangIsos != null && writeableLangIsos.indexOf(isoCode) == 0)
                    {
                        sectionRow.setFocusListener((SectionRow.FocusListener)object2, null);
                    }
                }
            }
            else
            {
                Label label = new Label("no editor for " + editorPropertyRow.getRowConfiguration().getPropertyDescriptor().getEditorType());
                label.setStyle("color:red");
                label.setParent(parent);
            }
        }
    }


    private boolean checkMap(Object currentValue, PropertyDescriptor propertyDescriptor)
    {
        if(!(propertyDescriptor instanceof ItemAttributePropertyDescriptor) || !(currentValue instanceof Map))
        {
            return false;
        }
        AttributeDescriptorModel firstAttributeDescriptor = ((ItemAttributePropertyDescriptor)propertyDescriptor).getFirstAttributeDescriptor();
        return (firstAttributeDescriptor != null && firstAttributeDescriptor.getAttributeType() instanceof de.hybris.platform.core.model.type.MapTypeModel);
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


    protected void renderLocalizedWrapper(SectionPanel panel, PropertyValueHolder propertyRow, Component parent, Map<String, Object> ctx)
    {
        TypedObject currentObject = (TypedObject)ctx.get("currentObject");
        List<String> readableLangs = EditorHelper.removeHiddenLanguages(currentObject, getSystemService()
                        .getAllReadableLanguageIsos());
        List<String> writeableLangs = new ArrayList<>(getSystemService().getAllWriteableLanguageIsos());
        renderLocalizedStructure(parent, readableLangs, writeableLangs, (SingleEditorRenderer)new Object(this, panel, propertyRow, ctx, readableLangs, writeableLangs));
    }


    private boolean checkSingle(PropertyDescriptor propertyDescriptor)
    {
        PropertyDescriptor.Multiplicity multi = propertyDescriptor.getMultiplicity();
        return (multi == null || multi.equals(PropertyDescriptor.Multiplicity.SINGLE));
    }


    public static void renderLocalizedStructure(Component parent, List<String> readableLangs, List<String> writeableLangs, SingleEditorRenderer singleRenderer)
    {
        Div container = new Div();
        container.setParent(parent);
        container.setSclass("localized_container");
        Toolbarbutton langButton = new Toolbarbutton();
        langButton.setSclass("lang_button");
        langButton.setWidth("20px");
        langButton.setImage("cockpit/images/language_logo.gif");
        langButton.setTooltiptext(Labels.getLabel("editor.button.language.tooltip"));
        Div div = new Div();
        div.setStyle("margin-right:22px");
        div.setHeight("100%");
        div.setParent((Component)container);
        singleRenderer.render((Component)div, UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
        langButton.setParent((Component)container);
        langButton.addEventListener("onClick", (EventListener)new Object(parent, readableLangs, writeableLangs, singleRenderer, container));
    }
}
