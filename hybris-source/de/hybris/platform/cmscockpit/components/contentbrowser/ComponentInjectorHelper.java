package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cmscockpit.components.contentbrowser.message.ContentEditorMessageRenderer;
import de.hybris.platform.cmscockpit.components.contentbrowser.message.MessageRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.core.model.ItemModel;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class ComponentInjectorHelper
{
    private static final Logger LOG = Logger.getLogger(ComponentInjectorHelper.class);
    public static final String COMPONENT_EDITOR_CONF = "contentEditor";
    public static final String COMPONENT_EDITOR_GRP_SCLASS = "contentEditorGroupbox";
    public static final String COMPONENT_EDITOR_GRP_SCLASS_NOT_EXPANDABLE = "contentEditorGroupboxNotExpandable";
    public static final String LOAD_ON_DEMAND_FLAG = "ondemandinit";
    public static final String EDITOR_ENTRY = "contentEditorEntry";
    public static final String EDITOR_ENTRY_READ_ONLY = "contentEditorEntryReadOnly";
    public static final String EDITOR_ENTRY_WIDTHS = "120px, 600px";
    public static final String EDITOR_ENTRY_WIDTHS_TOOLTIP = "120px,none,600px";
    public static final String EMPTY_TEXT = "Nothing to display.";
    public static final String CONTENT_EDITOR_ROW_ERROR = "contentEditorRowError";
    public static final String CONTENT_EDITOR_ROW = "contentEditorRow";
    public static final String UPDATE_COMPONENT_KEY = "cp_update_cmp";
    private static final transient MessageRenderer messageRenderer = (MessageRenderer)new ContentEditorMessageRenderer();


    public static HtmlBasedComponent renderEditorRow(Component parent, Map<String, Object> editorParameters, boolean editable, String label, String description)
    {
        Div entryDiv = new Div();
        parent.appendChild((Component)entryDiv);
        entryDiv.setSclass("contentEditorEntry" + (editable ? "" : " contentEditorEntryReadOnly"));
        Hbox hbox = new Hbox();
        entryDiv.appendChild((Component)hbox);
        hbox.setWidth("100%");
        hbox.setStyle("margin-top: 3px;");
        hbox.setWidths(getEditorWiths(editorParameters));
        Label labelComponent = new Label(label + ":");
        hbox.appendChild((Component)labelComponent);
        if(createLocalizedTooltip(description, (Component)hbox, labelComponent) != null)
        {
            hbox.setWidths(getEditorWithsTooltip(editorParameters));
        }
        return (HtmlBasedComponent)hbox;
    }


    public static Popup createLocalizedTooltip(String message, Component parent, Label target)
    {
        Popup ret = null;
        if(StringUtils.isNotEmpty(message))
        {
            Popup toolTipPopup = new Popup();
            toolTipPopup.setParent(parent);
            Label toolTipLabel = new Label(message);
            toolTipPopup.appendChild((Component)toolTipLabel);
            target.setTooltip(toolTipPopup);
            ret = toolTipPopup;
        }
        return ret;
    }


    public static String getEditorWiths(Map<String, Object> configParams)
    {
        if(configParams.get("labelWidth") != null && configParams.get("editorWidth") != null)
        {
            return (String)configParams.get("labelWidth") + "," + (String)configParams.get("labelWidth");
        }
        return "120px, 600px";
    }


    public static String getEditorWithsTooltip(Map<String, Object> configParams)
    {
        if(configParams.get("labelWidth") != null && configParams.get("editorWidth") != null)
        {
            return (String)configParams.get("labelWidth") + ",none," + (String)configParams.get("labelWidth");
        }
        return "120px,none,600px";
    }


    public static String getPropertyLabel(PropertyDescriptor propDescr)
    {
        return StringUtils.isBlank(propDescr.getName()) ? ("[" + propDescr.getQualifier() + "]") : propDescr.getName();
    }


    public static boolean hasEditableProperties(TypedObject referenceValue)
    {
        boolean ret = false;
        Set<PropertyDescriptor> descriptors = referenceValue.getType().getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor : descriptors)
        {
            if(EditorHelper.isEditable(propertyDescriptor, false))
            {
                ret = true;
                break;
            }
        }
        return ret;
    }


    public static Map<PropertyDescriptor, EditorRowConfiguration> getAllEditorRowConfigurations(EditorConfiguration config)
    {
        Map<PropertyDescriptor, EditorRowConfiguration> ret = new HashMap<>();
        for(EditorSectionConfiguration sectionConfig : config.getSections())
        {
            for(EditorRowConfiguration rowConfig : sectionConfig.getSectionRows())
            {
                ret.put(rowConfig.getPropertyDescriptor(), rowConfig);
            }
        }
        return ret;
    }


    public static List<PropertyDescriptor> getEditorPropertyDescriptors(TypedObject item, TypeService typeService, CMSAdminComponentService compService)
    {
        List<PropertyDescriptor> descriptors = null;
        ItemModel itemModel = (ItemModel)item.getObject();
        Collection<String> attrQualifiers = compService.getEditorProperties(itemModel);
        if(attrQualifiers != null && !attrQualifiers.isEmpty())
        {
            descriptors = new ArrayList<>(attrQualifiers.size());
            for(String attrQualifer : attrQualifiers)
            {
                PropertyDescriptor propDescr = typeService.getPropertyDescriptor((ObjectType)item.getType(), item
                                .getType().getCode() + "." + item.getType().getCode());
                if(propDescr != null)
                {
                    descriptors.add(propDescr);
                }
            }
        }
        return (descriptors == null) ? Collections.EMPTY_LIST : descriptors;
    }


    public static VelocityContext createVelocityContext(Object item)
    {
        VelocityContext velCtx = new VelocityContext();
        String label = (item instanceof TypedObject) ? UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel((TypedObject)item) : "";
        velCtx.put("label", StringEscapeUtils.escapeXml(label));
        return velCtx;
    }


    public static boolean checkVelocityParamsChanged(Object item, VelocityContext lastVelocityCtx)
    {
        if(lastVelocityCtx == null)
        {
            return true;
        }
        VelocityContext currentVelocityContext = createVelocityContext(item);
        for(String key : currentVelocityContext.getKeys())
        {
            if(key instanceof String)
            {
                Object lastParamValue = lastVelocityCtx.get(key);
                Object currentParamValue = currentVelocityContext.get(key);
                return (lastParamValue != currentParamValue && (lastParamValue == null || !lastParamValue.equals(currentParamValue)));
            }
        }
        return false;
    }


    public static String getParsedVelocityTemplateString(Object item, String velocityTemplate)
    {
        String templateString = null;
        try
        {
            StringWriter writer = new StringWriter();
            Properties velocityEngineProperties = new Properties();
            VelocityEngine velEngine = CommonsManager.getInstance().getVelocityEngine(velocityEngineProperties);
            VelocityContext velCtx = createVelocityContext(item);
            velEngine.evaluate((Context)velCtx, writer, "CMS Content editor section component", velocityTemplate);
            templateString = writer.toString();
        }
        catch(Exception e)
        {
            LOG.error("Velocity template MELTDOWN!!!", e);
        }
        return templateString;
    }


    protected static ObjectValueContainer.ObjectValueHolder getObjectValueHolder(ObjectValueContainer valueContainer, PropertyDescriptor descr, String langIso)
    {
        ObjectValueContainer.ObjectValueHolder valueHolder = null;
        if(descr.isLocalized())
        {
            valueHolder = valueContainer.getValue(descr,
                            (langIso == null) ? UISessionUtils.getCurrentSession().getGlobalDataLanguageIso() : langIso);
        }
        else
        {
            valueHolder = valueContainer.getValue(descr, null);
        }
        return valueHolder;
    }


    public static EditorListener createEditorListener(TypedObject item, ObjectValueContainer objectValueContainer, Map<String, ? extends Object> params, PropertyDescriptor propDescr, String editorCode, HtmlBasedComponent parent, HtmlBasedComponent rootComponent, boolean autoPersist)
    {
        UIEditor editor = EditorHelper.getUIEditor(propDescr, editorCode);
        return (EditorListener)new ContentEditorListener(editor, objectValueContainer, propDescr, params, item, parent, rootComponent, autoPersist);
    }


    public static Message createAnErrorMessage(Exception exception)
    {
        Message msg = new Message(exception.getMessage(), 3, true);
        if(exception instanceof ValueHandlerException)
        {
            Set<CockpitValidationDescriptor> relatedProperties = ((ValueHandlerException)exception).getProperties();
            if(!relatedProperties.isEmpty())
            {
                String propString = "";
                for(CockpitValidationDescriptor validationDescriptor : relatedProperties)
                {
                    propString = propString + "'" + propString + "', ";
                }
                propString = propString.trim().substring(0, propString.length() - 2);
                msg.setText(Labels.getLabel("editorarea.persist.error", (Object[])new String[] {propString}));
                msg.setTooltip(exception.getMessage());
            }
        }
        return msg;
    }


    protected static MessageRenderer getMessageRenderer()
    {
        return messageRenderer;
    }
}
