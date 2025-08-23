package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomElement;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.EditorAreaParameterNames;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

@NotThreadSafe
public class NestedAttributePanelRenderer extends DefaultEditorAreaPanelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(NestedAttributePanelRenderer.class);
    protected static final String NESTED_OBJECT_IDENTIFIER = "InCurrentObject";
    protected String nestedObjectKey = "";
    private TypeFacade typeFacade;
    private NestedAttributeUtils nestedAttributeUtils;
    private LabelService labelService;


    public void render(Component component, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractPanelConfiguration instanceof Panel)
        {
            Panel panel = (Panel)abstractPanelConfiguration;
            panel.getAttributeOrCustom().stream()
                            .forEach(element -> renderAttributeOrCustom(component, object, dataType, widgetInstanceManager, (Positioned)element));
        }
    }


    protected void renderAttributeOrCustom(Component component, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager, Positioned element)
    {
        if(element instanceof Attribute)
        {
            Attribute attribute = (Attribute)element;
            renderNestedAttribute(component, attribute, object, dataType, widgetInstanceManager);
        }
        else if(element instanceof CustomElement)
        {
            CustomElement definition = (CustomElement)element;
            createCustomHtmlRenderer().render(component, definition, object, dataType, widgetInstanceManager);
        }
    }


    protected void renderNestedAttribute(Component component, Attribute attribute, Object rootObject, DataType rootDataType, WidgetInstanceManager widgetInstanceManager)
    {
        Object nestedObject = rootObject;
        String nestedObjectName = "";
        Attribute nestedPropertyAttribute = null;
        try
        {
            List<String> splitQualifier = getNestedAttributeUtils().splitQualifier(attribute.getQualifier());
            for(int i = 0; i < splitQualifier.size() - 1; i++)
            {
                nestedObjectName = splitQualifier.get(i);
                nestedObject = getNestedAttributeUtils().getNestedObject(nestedObject, nestedObjectName);
                nestedPropertyAttribute = generateAttributeForNestedProperty(attribute, splitQualifier.get(i + 1));
            }
            if(nestedObject == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.info(String.format("Property %s is null, skipping render of %s", new Object[] {nestedObjectName, attribute.getQualifier()}));
                }
                return;
            }
            this.nestedObjectKey = nestedObjectName + "InCurrentObject";
            widgetInstanceManager.getModel().put(this.nestedObjectKey, nestedObject);
            String nestedObjectClass = getNestedAttributeUtils().getNameOfClassWithoutModel(nestedObject);
            DataType nestedDataType = getTypeFacade().load(nestedObjectClass);
            boolean canReadNestedObject = getPermissionFacade().canReadInstanceProperty(rootObject, nestedObjectName);
            boolean canChangeNestedObject = getPermissionFacade().canChangeInstanceProperty(rootObject, nestedObjectName);
            if(canReadNestedObject && canChangeNestedObject)
            {
                createAttributeRenderer()
                                .render(component, nestedPropertyAttribute, nestedObject, nestedDataType, widgetInstanceManager);
            }
            else if(!canReadNestedObject)
            {
                Div attributeContainer = new Div();
                attributeContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
                renderNotReadableLabel((HtmlBasedComponent)attributeContainer, nestedPropertyAttribute, rootDataType,
                                getLabelService().getAccessDeniedLabel(nestedPropertyAttribute));
                attributeContainer.setParent(component);
            }
            else if(!canChangeNestedObject)
            {
                if(nestedPropertyAttribute != null)
                {
                    nestedPropertyAttribute.setReadonly(Boolean.TRUE);
                }
                createAttributeRenderer()
                                .render(component, nestedPropertyAttribute, nestedObject, nestedDataType, widgetInstanceManager);
            }
        }
        catch(TypeNotFoundException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | SecurityException | InvalidNestedAttributeException e)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.info(e.getMessage(), e);
            }
        }
    }


    protected Attribute generateAttributeForNestedProperty(Attribute attribute, String nestedQualifier)
    {
        Attribute nestedAttribute = new Attribute();
        nestedAttribute.setQualifier(nestedQualifier);
        nestedAttribute.setReadonly(Boolean.valueOf(attribute.isReadonly()));
        nestedAttribute.setDescription(attribute.getDescription());
        nestedAttribute.setEditor(attribute.getEditor());
        nestedAttribute.setLabel(attribute.getLabel());
        nestedAttribute.setVisible(Boolean.valueOf(attribute.isVisible()));
        nestedAttribute.setMergeMode(attribute.getMergeMode());
        nestedAttribute.setPosition(attribute.getPosition());
        return nestedAttribute;
    }


    protected Editor createEditor(DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if(genericAttribute == null)
        {
            return null;
        }
        String editorSClass = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-editor";
        String qualifier = genericAttribute.getQualifier();
        String referencedModelProperty = this.nestedObjectKey + "." + this.nestedObjectKey;
        Editor editor = createEditor(genericAttribute, widgetInstanceManager.getModel(), referencedModelProperty);
        processParameters(attribute.getEditorParameter(), editor);
        boolean editable = (!attribute.isReadonly() && canChangeProperty(genericAttribute, object));
        if(!editable)
        {
            editorSClass = "ye-default-editor-readonly";
        }
        editor.setReadOnly(!editable);
        editor.setLocalized(genericAttribute.isLocalized());
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setEditorLabel(resolveAttributeLabel(attribute, genericType));
        editor.setType(resolveEditorType(genericAttribute));
        editor.setOptional(!genericAttribute.isMandatory());
        YTestTools.modifyYTestId((Component)editor, "editor_" + this.nestedObjectKey + "." + qualifier);
        editor.setAttribute("parentObject", object);
        editor.setWritableLocales(getPermissionFacade().getWritableLocalesForInstance(object));
        editor.setReadableLocales(getPermissionFacade().getReadableLocalesForInstance(object));
        if(genericAttribute.isLocalized())
        {
            editor.addParameter("headerLabelTooltip", attribute.getQualifier());
            editor.addParameter("localizedEditor.attributeDescription", getAttributeDescription(genericType, attribute));
        }
        editor.setProperty(referencedModelProperty);
        if(StringUtils.isNotBlank(attribute.getEditor()))
        {
            editor.setDefaultEditor(attribute.getEditor());
        }
        editor.setPartOf(genericAttribute.isPartOf());
        editor.setOrdered(genericAttribute.isOrdered());
        editor.afterCompose();
        editor.setSclass(editorSClass);
        return editor;
    }


    protected void processParameters(List<Parameter> parameters, Editor editor)
    {
        for(Parameter parameter : parameters)
        {
            if(EditorAreaParameterNames.MULTILINE_EDITOR_ROWS.getName().equals(parameter.getName()) || EditorAreaParameterNames.ROWS
                            .getName().equals(parameter.getName()))
            {
                editor.addParameter("rows", parameter.getValue());
                continue;
            }
            if(EditorAreaParameterNames.NESTED_OBJECT_WIZARD_NON_PERSISTABLE_PROPERTIES_LIST.getName()
                            .equals(parameter.getName()))
            {
                List<String> nonPersistablePropertiesList = extractPropertiesList(parameter.getValue());
                editor.addParameter(EditorAreaParameterNames.NESTED_OBJECT_WIZARD_NON_PERSISTABLE_PROPERTIES_LIST.getName(), nonPersistablePropertiesList);
                continue;
            }
            editor.addParameter(parameter.getName(), parameter.getValue());
        }
    }


    protected Editor createEditor(DataAttribute genericAttribute, WidgetModel model, String referencedModelProperty)
    {
        if(isReferenceEditor(genericAttribute))
        {
            Object object = new Object(this);
            model.addObserver(referencedModelProperty, (ValueObserver)object);
            return (Editor)new Object(this, model, referencedModelProperty, (ModelObserver)object);
        }
        return new Editor();
    }


    protected boolean isReferenceEditor(DataAttribute genericAttribute)
    {
        return (genericAttribute.getValueType() != null && !genericAttribute.getValueType().isAtomic());
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected NestedAttributeUtils getNestedAttributeUtils()
    {
        return this.nestedAttributeUtils;
    }


    @Required
    public void setNestedAttributeUtils(NestedAttributeUtils nestedAttributeUtils)
    {
        this.nestedAttributeUtils = nestedAttributeUtils;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
