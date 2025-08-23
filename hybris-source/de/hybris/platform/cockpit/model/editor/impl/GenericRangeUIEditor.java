package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class GenericRangeUIEditor extends GenericCollectionUIEditor
{
    private UIEditor fromEditor;
    private HtmlBasedComponent fromEditorView;
    private List<Object> values;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Div div = new Div();
        div.setSclass("rangeEditorContainer");
        Object fromValue = null;
        Object toValue = null;
        this.values = new ArrayList((initialValue == null) ? Collections.EMPTY_LIST : (Collection)initialValue);
        if(this.values.size() < 2)
        {
            this.values.add(null);
        }
        if(this.values.size() < 2)
        {
            this.values.add(null);
        }
        fromValue = this.values.get(0);
        toValue = this.values.get(1);
        this.fromEditor = getSingleValueEditorDescriptor().createUIEditor();
        this.fromEditor.setEditable(isEditable());
        UIEditor toEditor = getSingleValueEditorDescriptor().createUIEditor();
        toEditor.setEditable(isEditable());
        this.fromEditorView = this.fromEditor.createViewComponent(fromValue, parameters, (EditorListener)new Object(this, listener));
        Div fromDiv = new Div();
        fromDiv.setSclass("localizedEditorContainer");
        div.appendChild((Component)fromDiv);
        Div labelFromContainer = new Div();
        labelFromContainer.setSclass("localizedEditorRowLabel");
        labelFromContainer.appendChild((Component)new Label(Labels.getLabel("rangeeditor.from")));
        fromDiv.appendChild((Component)labelFromContainer);
        fromDiv.appendChild((Component)this.fromEditorView);
        HtmlBasedComponent htmlBasedComponent = toEditor.createViewComponent(toValue, parameters, (EditorListener)new Object(this, listener));
        Div toDiv = new Div();
        toDiv.setSclass("localizedEditorContainer");
        div.appendChild((Component)toDiv);
        Div labelToContainer = new Div();
        labelToContainer.setSclass("localizedEditorRowLabel");
        labelToContainer.appendChild((Component)new Label(Labels.getLabel("rangeeditor.to")));
        toDiv.appendChild((Component)labelToContainer);
        toDiv.appendChild((Component)htmlBasedComponent);
        return (HtmlBasedComponent)div;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        this.fromEditor.setFocus(this.fromEditorView, selectAll);
    }


    private void changeValue(int index, Object value, EditorListener listener)
    {
        if(this.values != null && index >= 0 && index < this.values.size())
        {
            if(!ObjectUtils.equals(this.values.get(index), value))
            {
                this.values = new ArrayList(this.values);
            }
            this.values.set(index, value);
        }
        setValue(this.values);
        listener.valueChanged(getValue());
    }
}
