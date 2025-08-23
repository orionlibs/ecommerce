package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UICockpitArea;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.HtmlBasedComponent;

public abstract class AbstractUIEditor implements UIEditor
{
    @Deprecated
    public static final String EVENT_SOURCE = "eventSource";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractUIEditor.class);
    public static final String CANCEL_BTN = "/cockpit/images/icon_func_undo.png";
    public static final String ATTRIBUTE_QUALIFIER_PARAM = "attributeQualifier";
    public static final String PROPERTY_DESCRIPTOR_PARAM = "propertyDescriptor";
    public static final String SEARCH_MODE_PARAM = "searchMode";
    private Object value = null;
    private boolean editable = true;
    protected Object initialEditValue = null;
    protected boolean inEditMode = true;
    protected String initialInputString = null;
    private boolean optional = true;


    @Deprecated
    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist) throws IllegalArgumentException
    {
        EditorHelper.createEditor(item, propDescr, parent, valueContainer, autoPersist);
    }


    @Deprecated
    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode) throws IllegalArgumentException
    {
        EditorHelper.createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, null);
    }


    @Deprecated
    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params) throws IllegalArgumentException
    {
        EditorHelper.createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, false);
    }


    @Deprecated
    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, boolean focus) throws IllegalArgumentException
    {
        EditorHelper.createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus);
    }


    @Deprecated
    public static CreateContext applyReferenceRelatedAttributes(ReferenceUIEditor referenceEditor, PropertyDescriptor propertyDescriptor, Map<String, ? extends Object> parameters, TypedObject currentObject, Object currentValue, String isoCode, UICockpitArea cockpitArea, UISession session)
    {
        return EditorHelper.applyReferenceRelatedAttributes(referenceEditor, propertyDescriptor, parameters, currentObject, currentValue, isoCode, cockpitArea, session);
    }


    @Deprecated
    public static <T> List<? extends T> filterValues(PropertyDescriptor propDescr, List<? extends T> values)
    {
        return EditorHelper.filterValues(propDescr, values);
    }


    @Deprecated
    public static SearchType getRootSearchType(PropertyDescriptor propDescr, UISession session)
    {
        return EditorHelper.getRootSearchType(propDescr, session);
    }


    @Deprecated
    public static UIEditor getUIEditor(PropertyDescriptor propDescr, String editorCode) throws IllegalArgumentException
    {
        return EditorHelper.getUIEditor(propDescr, editorCode);
    }


    @Deprecated
    public static boolean isEditable(PropertyDescriptor propDescr, boolean creationMode)
    {
        return EditorHelper.isEditable(propDescr, creationMode);
    }


    @Deprecated
    public static final Set<ObjectType> parseTemplateCodes(String input, TypeService typeService)
    {
        return EditorHelper.parseTemplateCodes(input, typeService);
    }


    @Deprecated
    public static void persistValues(TypedObject item, ObjectValueContainer valueContainer) throws ValueHandlerException
    {
        EditorHelper.persistValues(item, valueContainer);
    }


    @Deprecated
    public static void persistValues(TypedObject item, ObjectValueContainer valueContainer, Map<String, ? extends Object> params) throws ValueHandlerException
    {
        EditorHelper.persistValues(item, valueContainer, params);
    }


    protected void fireValueChanged(EditorListener listener)
    {
        if(this.inEditMode)
        {
            listener.valueChanged(getValue());
        }
    }


    public Object getValue()
    {
        return this.value;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public boolean isOptional()
    {
        return this.optional;
    }


    protected void parseInitialInputString(Map<String, ? extends Object> parameters)
    {
        try
        {
            this.initialInputString = (String)parameters.get("initialEditString");
        }
        catch(ClassCastException e)
        {
            LOG.error("Initial input string not of type String.");
        }
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        rootEditorComponent.setFocus(true);
    }


    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    protected boolean isSearchMode(Map<String, ? extends Object> parameters)
    {
        if(parameters != null)
        {
            Boolean value = (Boolean)parameters.get("searchMode");
            if(value != null && value.booleanValue())
            {
                return true;
            }
        }
        return false;
    }
}
