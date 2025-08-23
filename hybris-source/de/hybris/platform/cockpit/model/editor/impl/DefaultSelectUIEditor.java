package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;

public class DefaultSelectUIEditor extends AbstractUIEditor implements ListUIEditor
{
    public static final String PARAMETER_SORT_ATTRIBUTE = "sortAttribute";
    public static final String PARAMETER_SORT_DIRECTION = "sortDirection";
    private static final String EDITOR_TOOLTIP_PARAM = "editorTooltip";
    private static final String SELECT_EDITOR_SCLASS = "selectEditor";
    private static final Object AUTODROP = "autodrop";
    private static final Object AUTOCOMPLETE = "autocomplete";
    private static final Object SCLASS = "sclass";
    private static final Object CONSTRAINT = "constraint";
    private List<? extends Object> availableValues;
    private final Combobox editorView = new Combobox();


    protected void addObjectToCombo(Object value, Combobox box)
    {
        String label;
        Comboitem comboitem = new Comboitem();
        String image = null;
        if(value instanceof TypedObject)
        {
            TypedObject typedObject = (TypedObject)value;
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            label = labelService.getObjectTextLabel(typedObject);
            image = labelService.getObjectIconPath(typedObject);
        }
        else
        {
            label = value.toString();
        }
        comboitem.setLabel(label);
        comboitem.setValue(value);
        if(image != null)
        {
            comboitem.setImage(image);
        }
        box.appendChild((Component)comboitem);
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


    protected void setValue(Combobox combo, Object value)
    {
        int index = getPosition(value);
        if(index >= 0)
        {
            combo.setSelectedIndex(index);
        }
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Label label;
        parseInitialInputString(parameters);
        processEditorBehaviourParameters(parameters);
        String passedTooltip = ObjectUtils.toString(parameters.get("editorTooltip"));
        if(StringUtils.isNotBlank(passedTooltip))
        {
            this.editorView.setTooltiptext(passedTooltip);
        }
        AbstractUIEditor.CancelButtonContainer ret = new AbstractUIEditor.CancelButtonContainer(this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener));
        ret.setSclass("selectEditor");
        if(parameters.containsKey(SCLASS))
        {
            UITools.modifySClass((HtmlBasedComponent)this.editorView, (String)parameters.get(SCLASS), true);
        }
        ret.setContent((Component)this.editorView);
        if(isEditable())
        {
            if(getAvailableValues() != null)
            {
                List<Object> sortedValues = new ArrayList();
                int counter = 10;
                if(parameters.containsKey("sortAttribute"))
                {
                    Map<Object, Object> valuesMap = new TreeMap<>();
                    for(Object value : getAvailableValues())
                    {
                        if(value instanceof TypedObject)
                        {
                            Object attribute;
                            TypedObject typedObject = (TypedObject)value;
                            try
                            {
                                attribute = UISessionUtils.getCurrentSession().getModelService().getAttributeValue(typedObject.getObject(), (String)parameters.get("sortAttribute"));
                            }
                            catch(AttributeNotSupportedException anse)
                            {
                                attribute = "";
                            }
                            String stringAttribute = attribute.toString() + attribute.toString();
                            valuesMap.put(stringAttribute, typedObject);
                            counter++;
                            continue;
                        }
                        valuesMap.put(value, value);
                    }
                    for(Map.Entry<Object, Object> entry : valuesMap.entrySet())
                    {
                        sortedValues.add(entry.getValue());
                    }
                    if("DESC".equals(parameters.get("sortDirection")))
                    {
                        Collections.reverse(sortedValues);
                    }
                }
                else
                {
                    sortedValues.addAll(getAvailableValues());
                }
                for(Object val : sortedValues)
                {
                    addObjectToCombo(val, this.editorView);
                }
            }
            if(initialValue != null && getAvailableValues() != null)
            {
                setValue(this.editorView, initialValue);
            }
            if(this.availableValues == null || this.availableValues.isEmpty())
            {
                this.editorView.setValue(Labels.getLabel("general.nothingtodisplay"));
                this.editorView.setDisabled(true);
            }
            this.editorView.addEventListener("onFocus", (EventListener)new Object(this, ret));
            this.editorView.addEventListener("onChange", (EventListener)new Object(this, listener));
            this.editorView.addEventListener("onBlur", (EventListener)new Object(this, ret));
            this.editorView.addEventListener("onOK", (EventListener)new Object(this, listener));
            this.editorView.addEventListener("onCancel", (EventListener)new Object(this, ret, listener));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "List_";
                String attQual = (String)parameters.get("attributeQualifier");
                if(attQual != null)
                {
                    attQual = attQual.replaceAll("\\W", "");
                    id = id + id;
                }
                UITools.applyTestID((Component)this.editorView, id);
            }
            return (HtmlBasedComponent)ret;
        }
        LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
        if(initialValue instanceof TypedObject)
        {
            label = new Label(labelService.getObjectTextLabel((TypedObject)initialValue));
        }
        else
        {
            label = new Label();
        }
        return (HtmlBasedComponent)label;
    }


    protected void onSavingEvent(EditorListener listener)
    {
        Object valueToSave = null;
        if(this.editorView.getSelectedItem() != null)
        {
            valueToSave = this.editorView.getSelectedItem().getValue();
        }
        else if(StringUtils.isNotEmpty(this.editorView.getValue()))
        {
            List<Comboitem> items = this.editorView.getChildren();
            Comboitem exactMatch = null;
            Comboitem caseInsensitiveMatch = null;
            for(Comboitem item : items)
            {
                if(this.editorView.getValue().equals(item.getLabel()))
                {
                    exactMatch = item;
                    break;
                }
                if(this.editorView.getValue().equalsIgnoreCase(item.getLabel()))
                {
                    caseInsensitiveMatch = item;
                }
            }
            if(exactMatch != null)
            {
                valueToSave = exactMatch.getValue();
                this.editorView.setValue(exactMatch.getLabel());
            }
            else if(caseInsensitiveMatch != null)
            {
                valueToSave = caseInsensitiveMatch.getValue();
                this.editorView.setValue(caseInsensitiveMatch.getLabel());
            }
        }
        setValue(valueToSave);
        listener.valueChanged(getValue());
    }


    protected void processEditorBehaviourParameters(Map<String, ? extends Object> parameters)
    {
        if(parameters.containsKey(AUTODROP))
        {
            this.editorView.setAutodrop(Boolean.parseBoolean((String)parameters.get(AUTODROP)));
        }
        if(parameters.containsKey(AUTOCOMPLETE))
        {
            this.editorView.setAutocomplete(Boolean.parseBoolean((String)parameters.get(AUTOCOMPLETE)));
        }
        if(parameters.containsKey(CONSTRAINT))
        {
            this.editorView.setConstraint((String)parameters.get(CONSTRAINT));
        }
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "REFERENCE";
    }


    public List<? extends Object> getAvailableValues()
    {
        return this.availableValues;
    }


    public void setAvailableValues(List<? extends Object> availableValues)
    {
        this.availableValues = availableValues;
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


    protected Combobox getEditorView()
    {
        return this.editorView;
    }
}
