package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;

public class MapUIEditor extends GenericCollectionUIEditor
{
    private List<? extends Object> availableValues;
    private MapUIEditorRowRenderer rowRenderer;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Label label;
        Map<Object, Object> mapOfValues = new LinkedHashMap<>();
        if(initialValue instanceof Map)
        {
            mapOfValues.putAll((Map<?, ?>)initialValue);
        }
        if(isEditable())
        {
            ListModelMap listmodel = new ListModelMap(mapOfValues);
            createEmptyRow(listmodel);
            Div listContainer = new Div();
            Listbox listBox = new Listbox();
            listBox.setSclass("collectionUIEditorItems");
            listBox.setOddRowSclass("oddRowRowSclass");
            listBox.setDisabled(!isEditable());
            listBox.setModel((ListModel)listmodel);
            listBox.setFixedLayout(false);
            listBox.setItemRenderer(createListItemRenderer(parameters, listener, listBox));
            listContainer.appendChild((Component)listBox);
            setValue(mapOfValues);
            return (HtmlBasedComponent)listContainer;
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


    protected ListitemRenderer createCollectionItemListRenderer(Map<String, ? extends Object> parameters, EditorListener listener)
    {
        return null;
    }


    protected ListitemRenderer createListItemRenderer(Map<String, ? extends Object> parameters, EditorListener listener, Listbox listBox)
    {
        if(this.rowRenderer == null)
        {
            this.rowRenderer = (MapUIEditorRowRenderer)SpringUtil.getBean("mapUIEditorRowRenderer");
            this.rowRenderer.init(this, parameters, listener, listBox);
        }
        return (ListitemRenderer)this.rowRenderer;
    }


    public boolean isInline()
    {
        return false;
    }


    public String getEditorType()
    {
        PropertyEditorDescriptor desc = getSingleValueEditorDescriptor();
        return (desc == null) ? null : desc.getEditorType();
    }


    public List<? extends Object> getAvailableValues()
    {
        return (this.availableValues == null) ? Collections.EMPTY_LIST : this.availableValues;
    }


    public void setAvailableValues(List<? extends Object> availableValues)
    {
        this.availableValues = availableValues;
    }


    public void createEmptyRow(ListModelMap listmodel)
    {
        Map mapOfValues = listmodel.getInnerMap();
        if(MapUtils.isEmpty(mapOfValues) || (MapUtils.isNotEmpty(mapOfValues) && !mapOfValues.containsKey(null)))
        {
            listmodel.put(null, null);
        }
    }
}
