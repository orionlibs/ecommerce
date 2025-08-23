package de.hybris.platform.cockpit.model.editor.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.impl.SimpleConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.impl.SingleConditionValueContainer;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.search.Operator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class HmcLikeTextConditionUIEditor extends AbstractExtensibleConditionUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(HmcLikeTextConditionUIEditor.class);
    private ConditionValueEntry currentSelectedOperator = null;
    private String inputValue = null;
    private String inputValue2 = null;


    protected String getValueType()
    {
        return "TEXT";
    }


    protected boolean setComboSelection(Combobox combo, String value)
    {
        List children = combo.getChildren();
        for(Object object : children)
        {
            if(object instanceof Comboitem && value != null && value
                            .equals(((ConditionValueEntry)((Comboitem)object).getValue()).getOperatorQualifier()))
            {
                combo.setSelectedItem((Comboitem)object);
                this.currentSelectedOperator = (ConditionValueEntry)((Comboitem)object).getValue();
                return true;
            }
        }
        return false;
    }


    private Comboitem createAndAppendComboitem(Combobox parent, String value, String label, int nrInputs)
    {
        Comboitem comboItem = new Comboitem((label == null) ? value : label);
        comboItem.setValue(new ConditionValueEntry(this, value, nrInputs));
        parent.appendChild((Component)comboItem);
        return comboItem;
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Div ret = new Div();
        Hbox hbox = new Hbox();
        ret.appendChild((Component)hbox);
        Combobox combo = new Combobox();
        Textbox textbox = new Textbox();
        Textbox textbox2 = new Textbox();
        Label andLabel = new Label("and");
        Comboitem containsItem = createAndAppendComboitem(combo, "contains", null, 1);
        createAndAppendComboitem(combo, "equals", null, 1);
        createAndAppendComboitem(combo, "startswith", null, 1);
        createAndAppendComboitem(combo, "endswith", null, 1);
        createAndAppendComboitem(combo, "greater", null, 1);
        createAndAppendComboitem(combo, "less", null, 1);
        createAndAppendComboitem(combo, "greaterOrEquals", null, 1);
        createAndAppendComboitem(combo, "lessOrEquals", null, 1);
        createAndAppendComboitem(combo, "isEmpty", null, 0);
        createAndAppendComboitem(combo, "between", null, 2);
        String opQuali = parseInitialValues(initialValue);
        if(opQuali == null || !setComboSelection(combo, opQuali))
        {
            combo.setSelectedItem(containsItem);
            this.currentSelectedOperator = (ConditionValueEntry)containsItem.getValue();
        }
        combo.addEventListener("onChange", (EventListener)new Object(this, combo, textbox, textbox2, andLabel, listener));
        hbox.appendChild((Component)combo);
        hbox.appendChild((Component)textbox);
        textbox.setValue(this.inputValue);
        Object object1 = new Object(this, textbox, listener);
        textbox.addEventListener("onChange", (EventListener)object1);
        textbox.addEventListener("onOK", (EventListener)object1);
        hbox.appendChild((Component)andLabel);
        hbox.appendChild((Component)textbox2);
        textbox2.setValue(this.inputValue2);
        Object object2 = new Object(this, textbox2, listener);
        textbox2.addEventListener("onChange", (EventListener)object2);
        textbox2.addEventListener("onOK", (EventListener)object2);
        updateTextboxVisibility(textbox, textbox2, (Component)andLabel);
        return (HtmlBasedComponent)ret;
    }


    private void updateTextboxVisibility(Textbox textbox1, Textbox textbox2, Component connector)
    {
        if(this.currentSelectedOperator == null || this.currentSelectedOperator.getNrInputs() == 0)
        {
            textbox1.setVisible(false);
            textbox2.setVisible(false);
            connector.setVisible(false);
        }
        else if(this.currentSelectedOperator.getNrInputs() == 1)
        {
            textbox1.setVisible(true);
            textbox2.setVisible(false);
            connector.setVisible(false);
        }
        else
        {
            textbox1.setVisible(true);
            textbox2.setVisible(true);
            connector.setVisible(true);
        }
    }


    private String parseInitialValues(Object initValue)
    {
        if(initValue instanceof SingleConditionValueContainer)
        {
            Set<ConditionValue> conditionValues = ((SingleConditionValueContainer)initValue).getConditionValues();
            try
            {
                ConditionValue next = conditionValues.iterator().next();
                Iterator<Object> iterator = next.getValues().iterator();
                if(iterator.hasNext())
                {
                    this.inputValue = (String)iterator.next();
                }
                if(iterator.hasNext())
                {
                    this.inputValue2 = (String)iterator.next();
                }
                return next.getOperator().getQualifier();
            }
            catch(Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.warn("Could not parse initial values", e);
                }
            }
        }
        return null;
    }


    private void clearIfBlank()
    {
        if(StringUtils.isBlank(this.inputValue))
        {
            this.inputValue = null;
        }
        if(StringUtils.isBlank(this.inputValue2))
        {
            this.inputValue2 = null;
        }
    }


    public ConditionValueContainer getValue()
    {
        clearIfBlank();
        if(this.currentSelectedOperator == null)
        {
            return null;
        }
        if(this.currentSelectedOperator.getNrInputs() < 2)
        {
            return (ConditionValueContainer)new SingleConditionValueContainer((ConditionValue)new SimpleConditionValue(this.inputValue, new Operator(this.currentSelectedOperator
                            .getOperatorQualifier())));
        }
        return (ConditionValueContainer)new SingleConditionValueContainer((ConditionValue)new DefaultConditionValue(new Operator(this.currentSelectedOperator
                        .getOperatorQualifier()), new Object[] {this.inputValue, this.inputValue2}));
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        try
        {
            ((Combobox)rootEditorComponent.getFirstChild().getFirstChild()).setFocus(true);
        }
        catch(Exception e)
        {
            LOG.error("Could not set focus to editor " + this);
        }
    }
}
