package de.hybris.platform.cockpit.model.editor.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.ShortcutConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.SingleConditionValueContainer;
import de.hybris.platform.cockpit.model.editor.DateParametersEditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.ValuePair;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public abstract class AbstractSimpleInputConditionUIEditor extends AbstractExtensibleConditionUIEditor
{
    private static final String CURRENT_ENTRY = "currentEntry";
    private static final String CURRENT_VALUES = "currentValues";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSimpleInputConditionUIEditor.class);
    private static final String ALLOW_ARROW_NAVIGATION = "allowArrowNavigation";
    private static final String ON_FOCUS2 = "onFocus2";
    private static final String CONDITION_ROWS = "conditionRows";
    private static final String FOCUS_COMPONENT = "focusComponent";
    private EditorConditionEntry selectedEntry = null;
    private List<Object> values = null;


    public ConditionValueContainer getValue()
    {
        return (this.selectedEntry == null) ? (ConditionValueContainer)new DefaultConditionValueContainer() : (ConditionValueContainer)new SingleConditionValueContainer((ConditionValue)new DefaultConditionValue(new Operator(this.selectedEntry
                        .getOperator()), (this.values == null) ? null : this.values.toArray()));
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Div ret = new Div();
        AbstractExtensibleConditionUIEditor.ComboDiv mainDiv = new AbstractExtensibleConditionUIEditor.ComboDiv();
        Div combo = new Div();
        combo.setSclass("gradientDiv");
        UITools.modifySClass((HtmlBasedComponent)combo, "advSearchCond", true);
        try
        {
            filterConditionEntries(parameters);
        }
        catch(Exception e)
        {
            LOG.warn("Could not filter condition entries, error was ", e);
        }
        Object object = new Object(this, mainDiv, listener);
        combo.addEventListener("onClick", (EventListener)object);
        mainDiv.appendChild((Component)combo);
        Checkbox mainFocusComponent = createFocusComponent((AbstractTag)mainDiv);
        mainFocusComponent.addEventListener("onOK", (EventListener)new Object(this, listener));
        mainFocusComponent.addEventListener("onCheck", (EventListener)object);
        mainFocusComponent.setCtrlKeys("#del#up#down");
        mainFocusComponent.addEventListener("onCtrlKey", (EventListener)new Object(this, mainDiv, listener));
        ret.appendChild((Component)mainDiv);
        if(initialValue instanceof ConditionValueContainer)
        {
            Set<ConditionValue> conditionValues = ((ConditionValueContainer)initialValue).getConditionValues();
            Iterator<ConditionValue> iterator = conditionValues.iterator();
            if(iterator.hasNext())
            {
                ConditionValue conditionValue = iterator.next();
                this.selectedEntry = getConditionEntry(conditionValue.getOperator());
                this.values = conditionValue.getValues();
                updateComboDisplay(mainDiv, this.selectedEntry, this.values);
            }
        }
        return (HtmlBasedComponent)ret;
    }


    protected Popup createConditionPopup(AbstractExtensibleConditionUIEditor.ComboDiv parent, EditorListener listener)
    {
        Popup conditionPopup = new Popup();
        conditionPopup.setSclass("advSearchCondPopup");
        parent.appendChild((Component)conditionPopup);
        conditionPopup.addEventListener("onOpen", (EventListener)new Object(this, parent, conditionPopup));
        List<AbstractTag> conditionRows = new ArrayList<>();
        for(EditorConditionEntry conditionEntry : getConditionEntries())
        {
            AbstractTag conditionRow = createConditionRow(conditionEntry, listener, parent);
            conditionPopup.appendChild((Component)conditionRow);
            conditionRows.add(conditionRow);
        }
        conditionPopup.setAttribute("conditionRows", conditionRows);
        return conditionPopup;
    }


    protected AbstractTag createConditionRow(EditorConditionEntry editorConditionEntry, EditorListener listener, AbstractExtensibleConditionUIEditor.ComboDiv mainDiv)
    {
        String label;
        Div ret = new Div();
        ret.setSclass("conditionRow");
        Checkbox focusComponent = createFocusComponent((AbstractTag)ret);
        Object object1 = new Object(this, mainDiv);
        Object object2 = new Object(this, editorConditionEntry, mainDiv);
        focusComponent.addEventListener("onCancel", (EventListener)object1);
        focusComponent.setCtrlKeys("#pgup#pgdn#up#down");
        focusComponent.addEventListener("onCtrlKey", (EventListener)object2);
        Hbox hbox = new Hbox();
        if(editorConditionEntry instanceof ShortcutConditionEntry)
        {
            label = ((ShortcutConditionEntry)editorConditionEntry).getLabel(UISessionUtils.getCurrentSession().getLanguageIso());
        }
        else
        {
            label = Labels.getLabel(editorConditionEntry.getI3Label(), "[" + editorConditionEntry.getOperator() + "]");
        }
        Label labelCmp = new Label(label);
        labelCmp.setSclass("conditionRowLabel conditionRowLabel-first");
        hbox.appendChild((Component)labelCmp);
        List<Object> values = new ArrayList();
        if(editorConditionEntry.equals(this.selectedEntry) && this.values != null)
        {
            values.addAll(this.values);
        }
        InputElement firstInput = null;
        int currentConditionInputIndex = 0;
        for(String viewItem : editorConditionEntry.getViewComponentsAsList())
        {
            InputElement inputViewItemComp = createInputViewItem(viewItem);
            if(inputViewItemComp == null)
            {
                Label viewLabelCmp = createLabelViewItem(viewItem);
                if(viewLabelCmp != null)
                {
                    hbox.appendChild((Component)viewLabelCmp);
                }
                continue;
            }
            if(editorConditionEntry.equals(this.selectedEntry))
            {
                Object currentConditionInputValue = this.values.get(currentConditionInputIndex);
                if(currentConditionInputValue != null)
                {
                    if(inputViewItemComp instanceof Datebox && currentConditionInputValue instanceof Date)
                    {
                        ((Datebox)inputViewItemComp).setValue((Date)currentConditionInputValue);
                    }
                    else if(currentConditionInputValue instanceof String)
                    {
                        inputViewItemComp.setText((String)currentConditionInputValue);
                    }
                    else if(!currentConditionInputValue.toString().equals(""))
                    {
                        inputViewItemComp.setText(currentConditionInputValue.toString());
                    }
                }
            }
            if(values.size() <= currentConditionInputIndex)
            {
                values.add(null);
            }
            int valueIndex = currentConditionInputIndex;
            currentConditionInputIndex++;
            hbox.appendChild((Component)inputViewItemComp);
            if(firstInput == null)
            {
                firstInput = inputViewItemComp;
            }
            inputViewItemComp.addEventListener("onChange", (EventListener)new Object(this, values, valueIndex, inputViewItemComp, mainDiv, editorConditionEntry));
            inputViewItemComp.addEventListener("onFocus", (EventListener)new Object(this, mainDiv, editorConditionEntry, values));
            inputViewItemComp.addEventListener("onBlur", (EventListener)new Object(this, ret));
            inputViewItemComp.addEventListener("onOK", (EventListener)new Object(this, values, valueIndex, inputViewItemComp, mainDiv, editorConditionEntry, listener));
            inputViewItemComp.addEventListener("onCancel", (EventListener)object1);
            String ctrlKeys = "#pgup#pgdn";
            if(Boolean.TRUE.equals(inputViewItemComp.getAttribute("allowArrowNavigation")))
            {
                ctrlKeys = ctrlKeys + "#up#down";
            }
            inputViewItemComp.setCtrlKeys(ctrlKeys);
            inputViewItemComp.addEventListener("onCtrlKey", (EventListener)object2);
        }
        ret.addEventListener("onClick", (EventListener)new Object(this, values, ret, mainDiv, editorConditionEntry, listener));
        if(!values.isEmpty())
        {
            labelCmp.addEventListener("onClick", (EventListener)new Object(this, ret));
        }
        if(values.isEmpty())
        {
            UITools.modifySClass((AbstractTag)ret, "conditionNoInput", true);
            focusComponent.addEventListener("onOK", (EventListener)new Object(this, mainDiv, editorConditionEntry, listener));
        }
        else
        {
            InputElement _first = firstInput;
            focusComponent.addEventListener("onFocus2", (EventListener)new Object(this, _first));
        }
        ret.appendChild((Component)hbox);
        if(editorConditionEntry instanceof AbstractExtensibleConditionUIEditor.ClearConditionEntry)
        {
            UITools.modifySClass((HtmlBasedComponent)labelCmp, "clearCondition", true);
            Div applyDiv = new Div();
            Label applyLabel = new Label(Labels.getLabel("general.apply"));
            applyDiv.addEventListener("onClick", (EventListener)new Object(this, mainDiv, listener));
            applyDiv.appendChild((Component)applyLabel);
            applyDiv.setSclass("conditionApplyDiv");
            ret.appendChild((Component)applyDiv);
        }
        return (AbstractTag)ret;
    }


    protected void setSelectedEntry(AbstractExtensibleConditionUIEditor.ComboDiv mainDiv, EditorConditionEntry entry, List<Object> values)
    {
        if(entry instanceof AbstractExtensibleConditionUIEditor.ClearConditionEntry)
        {
            this.values = null;
            this.selectedEntry = null;
        }
        else if(entry instanceof ShortcutConditionEntry)
        {
            this.values = ((ShortcutConditionEntry)entry).getConditionValues();
            this.selectedEntry = entry;
        }
        else
        {
            this.values = values;
            this.selectedEntry = entry;
        }
        updateComboDisplay(mainDiv, this.selectedEntry, this.values);
        mainDiv.setOpen(false);
        setFocusInternal((AbstractTag)mainDiv, true, true);
    }


    protected void updateComboDisplay(AbstractExtensibleConditionUIEditor.ComboDiv mainDiv, EditorConditionEntry condEntry, List<Object> values)
    {
        Component comboDiv = mainDiv.getFirstChild();
        comboDiv.getChildren().clear();
        if(condEntry != null)
        {
            EditorConditionEntry entry;
            if(condEntry instanceof ShortcutConditionEntry)
            {
                entry = ((ShortcutConditionEntry)condEntry).getTargetEntry();
            }
            else
            {
                entry = condEntry;
            }
            Hbox hbox = new Hbox();
            hbox.setSclass("conditionValueRow");
            String label = Labels.getLabel(entry.getI3Label(), "[" + entry.getOperator() + "]");
            Label labelCmp = new Label(label);
            labelCmp.setSclass("conditionRowLabel");
            hbox.appendChild((Component)labelCmp);
            int inputIndex = 0;
            for(String viewItem : entry.getViewComponentsAsList())
            {
                if(viewItem.trim().startsWith("$") && !viewItem.trim().startsWith("$LABEL"))
                {
                    Object object = (values == null || values.size() <= inputIndex) ? null : values.get(inputIndex);
                    Label displayLabel = new Label(getDisplayLabel(object));
                    displayLabel.setSclass("conditionValue");
                    hbox.appendChild((Component)displayLabel);
                    inputIndex++;
                    continue;
                }
                String i3label = viewItem;
                if(viewItem.trim().startsWith("$LABEL"))
                {
                    i3label = parseLabel(viewItem);
                }
                String viewLabel = Labels.getLabel(i3label, "[" + i3label + "]");
                Label labelItem = new Label(viewLabel);
                labelItem.setSclass("conditionRowLabel");
                hbox.appendChild((Component)labelItem);
            }
            comboDiv.appendChild((Component)hbox);
        }
    }


    protected Label createLabelViewItem(String componentCode)
    {
        Label ret = null;
        ValuePair<String, String> typeAndWidth = getTypeAndWidth(componentCode);
        String type = (String)typeAndWidth.getFirst();
        String width = (String)typeAndWidth.getSecond();
        if(type != null && type.startsWith("$LABEL("))
        {
            String i3label = parseLabel(type);
            String viewLabel = Labels.getLabel(i3label, "[" + componentCode + "]");
            Label viewLabelCmp = new Label(viewLabel);
            viewLabelCmp.setSclass("conditionRowLabel");
            ret = viewLabelCmp;
        }
        if(width != null && ret != null)
        {
            ret.setWidth(width);
        }
        return ret;
    }


    private String parseLabel(String viewItemString)
    {
        String ret = null;
        try
        {
            String[] split = viewItemString.split("\\(");
            viewItemString = split[0];
            ret = split[1].split("\\)")[0];
        }
        catch(Exception e)
        {
            LOG.error("Wrong view component format '" + viewItemString + "', ignoring.");
        }
        return ret;
    }


    private ValuePair<String, String> getTypeAndWidth(String componentCode)
    {
        String qualifier = componentCode.trim();
        String width = null;
        String type = null;
        if(qualifier.contains("["))
        {
            try
            {
                String[] split = qualifier.split("\\[");
                type = split[0];
                width = split[1].split("]")[0];
            }
            catch(Exception e)
            {
                LOG.error("Wrong view component format '" + qualifier + "', using default text instead.");
            }
        }
        else
        {
            type = qualifier;
        }
        return new ValuePair(type, width);
    }


    protected InputElement createInputViewItem(String componentCode)
    {
        Decimalbox decimalbox;
        InputElement ret = null;
        ValuePair<String, String> typeAndWidth = getTypeAndWidth(componentCode);
        String type = (String)typeAndWidth.getFirst();
        String width = (String)typeAndWidth.getSecond();
        if("$_TYPE".equals(type))
        {
            type = "$" + getValueType();
        }
        if("$TEXT".equals(type))
        {
            Textbox textbox = new Textbox();
            textbox.setAttribute("allowArrowNavigation", Boolean.TRUE);
        }
        else if("$DATE".equals(type))
        {
            Datebox datebox = new Datebox();
            datebox.setFormat(DateParametersEditorHelper.getDateFormat(getParameters(), datebox));
            datebox.setLenient(DateParametersEditorHelper.getLenientFlag(getParameters()));
            Datebox datebox1 = datebox;
        }
        else if("$INTEGER".equals(type))
        {
            Intbox intbox = new Intbox();
            intbox.setAttribute("allowArrowNavigation", Boolean.TRUE);
        }
        else if("$LONG".equals(type))
        {
            Longbox longbox = new Longbox();
            longbox.setAttribute("allowArrowNavigation", Boolean.TRUE);
        }
        else if("$DECIMAL".equals(type))
        {
            decimalbox = new Decimalbox();
            decimalbox.setAttribute("allowArrowNavigation", Boolean.TRUE);
        }
        if(decimalbox != null)
        {
            if(width != null)
            {
                decimalbox.setWidth(width);
            }
            decimalbox.setSclass("conditionInput");
        }
        return (InputElement)decimalbox;
    }


    protected void setFocusedCondition(Popup conditionPopup, int index, boolean updateFocusComponent)
    {
        List<AbstractTag> conditionRows = (List<AbstractTag>)conditionPopup.getAttribute("conditionRows");
        if(conditionRows != null)
        {
            for(AbstractTag abstractTag : conditionRows)
            {
                setFocusInternal(abstractTag, false, false);
            }
            AbstractTag component = conditionRows.get(index);
            if(component != null)
            {
                setFocusInternal(component, true, updateFocusComponent);
            }
        }
    }


    protected Checkbox createFocusComponent(AbstractTag parent)
    {
        Checkbox focusChk = new Checkbox();
        focusChk.setStyle("position: absolute; left: -100px;");
        focusChk.addEventListener("onBlur", (EventListener)new Object(this, parent));
        focusChk.addEventListener("onFocus", (EventListener)new Object(this, parent));
        parent.appendChild((Component)focusChk);
        parent.setAttribute("focusComponent", focusChk);
        return focusChk;
    }


    protected void setFocusInternal(AbstractTag parent, boolean focus, boolean updateFocusComponent)
    {
        UITools.modifySClass(parent, "focusedComponent", focus);
        Object attribute = parent.getAttribute("focusComponent");
        if(updateFocusComponent && attribute instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)attribute).setFocus(focus);
        }
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        try
        {
            setFocusInternal((AbstractTag)rootEditorComponent.getFirstChild(), true, true);
        }
        catch(Exception e)
        {
            LOG.warn("Could not set focus", e);
        }
    }
}
