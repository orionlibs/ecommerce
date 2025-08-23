package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class GenericCollectionUIEditor extends AbstractUIEditor implements ListUIEditor
{
    private PropertyEditorDescriptor singleValueEditorDescriptor;
    private transient Listbox collectionItems;
    private transient List<Object> collectionValues;
    protected static final String WIDTH = "width";
    protected static final String HEIGHT = "height";
    protected static final String CELLPADDING = "cellpadding";
    protected static final String CELLSPACING = "cellspacing";
    protected static final String _100PERCENT = "100%";
    protected static final String SINGLE_VALUE_EDITOR_CODE = "singleValueEditorCode";
    private List<? extends Object> availableValues;


    public void setSingleValueEditorDescriptor(PropertyEditorDescriptor singleValueEditorDescriptor)
    {
        this.singleValueEditorDescriptor = singleValueEditorDescriptor;
    }


    public PropertyEditorDescriptor getSingleValueEditorDescriptor()
    {
        return this.singleValueEditorDescriptor;
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        this.collectionValues = createNewCollectionValuesList(initialValue);
        Div listContainer = new Div();
        this.collectionItems = new Listbox();
        this.collectionItems.setSclass("collectionUIEditorItems");
        this.collectionItems.setOddRowSclass("oddRowRowSclass");
        this.collectionItems.setDisabled(!isEditable());
        this.collectionItems.setModel((ListModel)getCollectionSimpleListModel(this.collectionValues));
        this.collectionItems.setFixedLayout(false);
        this.collectionItems.setItemRenderer(createCollectionItemListRenderer(parameters, listener));
        listContainer.appendChild((Component)this.collectionItems);
        return (HtmlBasedComponent)listContainer;
    }


    public void updateCollectionItems()
    {
        this.collectionValues = new ArrayList(this.collectionValues);
        this.collectionItems.setModel((ListModel)getCollectionSimpleListModel(this.collectionValues));
    }


    public String getEditorType()
    {
        PropertyEditorDescriptor desc = getSingleValueEditorDescriptor();
        return (desc == null) ? null : desc.getEditorType();
    }


    protected UIEditor createSingleValueEditor(Map<String, ? extends Object> parameters)
    {
        String editorCode = "single";
        if(parameters.containsKey("singleValueEditorCode"))
        {
            editorCode = ObjectUtils.toString(parameters.get("singleValueEditorCode"));
        }
        return getSingleValueEditorDescriptor().createUIEditor(editorCode);
    }


    public boolean isInline()
    {
        return false;
    }


    protected ListitemRenderer createCollectionItemListRenderer(Map<String, ? extends Object> parameters, EditorListener listener)
    {
        return (ListitemRenderer)new Object(this, parameters, listener);
    }


    private SimpleListModel getCollectionSimpleListModel(List<Object> collectionValues)
    {
        List<Object> newCollectionValues = new ArrayList((collectionValues == null) ? Collections.EMPTY_LIST : collectionValues);
        if((!newCollectionValues.isEmpty() && newCollectionValues.get(newCollectionValues.size() - 1) != null) || newCollectionValues
                        .isEmpty())
        {
            newCollectionValues.add(null);
        }
        return new SimpleListModel(newCollectionValues);
    }


    private void addCollectionElement(UIEditor editor, EditorListener listener)
    {
        this.collectionValues.add(editor.getValue());
        this.collectionValues = createNewCollectionValuesList(this.collectionValues);
        setValue(this.collectionValues);
        updateCollectionItems();
        listener.valueChanged(getValue());
    }


    public List<? extends Object> getAvailableValues()
    {
        return (this.availableValues == null) ? Collections.EMPTY_LIST : this.availableValues;
    }


    public void setAvailableValues(List<? extends Object> availableValues)
    {
        this.availableValues = availableValues;
    }


    protected List<Object> createNewCollectionValuesList(Object values)
    {
        return new ArrayList((values == null) ? Collections.EMPTY_LIST : (
                        (values instanceof String && StringUtils.isEmpty((String)values)) ? Collections.EMPTY_LIST : (
                                        (values instanceof Collection) ? (Collection)values : Collections.EMPTY_LIST)));
    }
}
