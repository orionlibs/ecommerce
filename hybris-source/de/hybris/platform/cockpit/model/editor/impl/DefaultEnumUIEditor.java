package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.EnumUIEditor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;

public class DefaultEnumUIEditor extends AbstractUIEditor implements EnumUIEditor
{
    private static final String EDITOR_TOOLTIP_PARAM = "editorTooltip";
    private static final String ENUM_EDITOR_SCLASS = "enumEditor";
    private List<? extends Object> availableValues;
    private List<? extends Object> originalAvailableValues;
    private final Combobox editorView = new Combobox();


    protected void addEnumToCombo(Object value, Combobox box)
    {
        Object val = (value instanceof TypedObject) ? ((TypedObject)value).getObject() : value;
        Comboitem comboitem = new Comboitem();
        String label = null;
        if(val instanceof HybrisEnumValue)
        {
            label = TypeTools.getEnumName((HybrisEnumValue)val);
        }
        else if(val instanceof ClassificationAttributeValueModel)
        {
            label = ((ClassificationAttributeValueModel)val).getName(UISessionUtils.getCurrentSession().getGlobalDataLocale());
            if(label == null)
            {
                label = ((ClassificationAttributeValueModel)val).getCode();
            }
        }
        else
        {
            label = "";
        }
        if(label == null)
        {
            label = val.toString();
        }
        comboitem.setLabel(label);
        if(label.isEmpty())
        {
            comboitem.setLabel(buildNullValueLabel());
            comboitem.setSclass("comboNullValue");
        }
        comboitem.setValue(value);
        box.appendChild((Component)comboitem);
    }


    private String buildNullValueLabel()
    {
        return "";
    }


    protected int getPosition(Object item)
    {
        int index = -1;
        if(this.availableValues != null)
        {
            index = getAvailableValues().indexOf(item);
        }
        return index;
    }


    protected void setEnumValue(Combobox combo, Object value)
    {
        int index = getPosition(value);
        if(index >= 0)
        {
            combo.setSelectedIndex(index);
        }
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Label ret;
        parseInitialInputString(parameters);
        this.editorView.setConstraint("strict");
        String passedTooltip = ObjectUtils.toString(parameters.get("editorTooltip"));
        if(StringUtils.isNotBlank(passedTooltip))
        {
            this.editorView.setTooltiptext(passedTooltip);
        }
        if(isEditable())
        {
            AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer(this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener));
            cancelButtonContainer.setSclass("enumEditor");
            cancelButtonContainer.setContent((Component)this.editorView);
            if(getAvailableValues() != null)
            {
                for(Object val : getAvailableValues())
                {
                    addEnumToCombo(val, this.editorView);
                }
            }
            if(initialValue != null && getAvailableValues() != null)
            {
                setEnumValue(this.editorView, initialValue);
            }
            this.editorView.addEventListener("onFocus", (EventListener)new Object(this, cancelButtonContainer));
            this.editorView.addEventListener("onChange", (EventListener)new Object(this, listener));
            this.editorView.addEventListener("onBlur", (EventListener)new Object(this, cancelButtonContainer));
            this.editorView.addEventListener("onOK", (EventListener)new Object(this, listener));
            this.editorView.addEventListener("onCancel", (EventListener)new Object(this, cancelButtonContainer, listener));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "Enum_";
                String attributeQualifier = (String)parameters.get("attributeQualifier");
                if(attributeQualifier != null)
                {
                    attributeQualifier = attributeQualifier.replaceAll("\\W", "");
                    id = id + id;
                }
                UITools.applyTestID((Component)this.editorView, id);
            }
            return (HtmlBasedComponent)cancelButtonContainer;
        }
        if(initialValue instanceof HybrisEnumValue)
        {
            ret = new Label(((HybrisEnumValue)initialValue).getCode());
        }
        else if(initialValue instanceof TypedObject)
        {
            Object value = ((TypedObject)initialValue).getObject();
            if(value instanceof ClassificationAttributeValueModel)
            {
                ret = new Label(((ClassificationAttributeValueModel)value).getName());
            }
            else
            {
                ret = new Label("-");
            }
        }
        else
        {
            ret = new Label("-");
        }
        return (HtmlBasedComponent)ret;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "ENUM";
    }


    public List<? extends Object> getAvailableValues()
    {
        return this.availableValues;
    }


    public void setAvailableValues(List<? extends Object> availableValues)
    {
        if(availableValues == null || availableValues.isEmpty())
        {
            this.editorView.setValue(Labels.getLabel("general.nothingtodisplay"));
            this.editorView.setDisabled(true);
            this.availableValues = null;
            this.originalAvailableValues = null;
        }
        else
        {
            this.availableValues = new ArrayList(availableValues);
            if(isOptional())
            {
                this.availableValues.add(0, null);
            }
            this.originalAvailableValues = new ArrayList(availableValues);
        }
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        Combobox element = (Combobox)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent();
        element.setFocus(true);
        if(this.initialInputString != null)
        {
            element.setText(this.initialInputString);
        }
    }


    public void setOptional(boolean optional)
    {
        if(!optional)
        {
            this.availableValues = this.originalAvailableValues;
        }
        super.setOptional(optional);
    }


    protected void validateAndFireEvent(EditorListener listener)
    {
        if(this.editorView.getSelectedItem() == null)
        {
            setEnumValue(this.editorView, this.initialEditValue);
        }
        else
        {
            setValue(this.editorView.getSelectedItem().getValue());
            listener.valueChanged(getValue());
        }
    }


    protected Combobox getEditorView()
    {
        return this.editorView;
    }
}
