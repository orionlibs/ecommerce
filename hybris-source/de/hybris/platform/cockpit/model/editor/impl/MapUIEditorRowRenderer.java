package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class MapUIEditorRowRenderer implements ListitemRenderer
{
    public static final String BEAN_ID = "mapUIEditorRowRenderer";
    private MapUIEditor mapUIEditor;
    private Map<String, ? extends Object> parameters;
    private EditorListener listener;
    private Listbox listBox;
    private TypeModel keyType;
    private PropertyDescriptor propertyDescriptor;
    private AttributeDescriptorModel attributeDescriptor;
    private PropertyEditorDescriptor keyPropertyEditorDescriptor;
    private PropertyEditorDescriptor valuePropertyEditorDescriptor;
    @Resource(name = "cockpitTypeService")
    private TypeService cockpitTypeService;
    @Resource(name = "typeService")
    private TypeService coreTypeService;
    @Resource(name = "EditorFactory")
    private EditorFactory editorFactory;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ModelService modelService;


    public void init(MapUIEditor mapUIEditor, Map<String, ? extends Object> parameters, EditorListener listener, Listbox listBox)
    {
        this.mapUIEditor = mapUIEditor;
        this.parameters = parameters;
        this.listener = listener;
        this.listBox = listBox;
        this.propertyDescriptor = this.cockpitTypeService.getPropertyDescriptor((String)parameters.get("attributeQualifier"));
        this.attributeDescriptor = getAttributeDescriptor(this.propertyDescriptor);
        this.keyPropertyEditorDescriptor = getKeyPropertyEditorDescriptor();
        this.valuePropertyEditorDescriptor = getValuePropertyEditorDescriptor();
    }


    public void render(Listitem itemRow, Object entry) throws Exception
    {
        Div div = new Div();
        Object key = (entry == null) ? null : ((Map.Entry)entry).getKey();
        UIEditor keyUIEditor = createKeyUIEditor();
        div.appendChild((Component)keyUIEditor.createViewComponent(key, this.parameters, keyListener(itemRow)));
        Object value = (entry == null) ? null : ((Map.Entry)entry).getValue();
        UIEditor valueUIEditor = this.valuePropertyEditorDescriptor.createUIEditor();
        HtmlBasedComponent valueEditorComponent = valueUIEditor.createViewComponent(value, this.parameters, valueListener(itemRow));
        valueEditorComponent.setStyle("width: auto");
        div.appendChild((Component)valueEditorComponent);
        itemRow.appendChild((Component)createListCell((Component)div, createRemoveImage(itemRow)));
    }


    protected PropertyEditorDescriptor getKeyPropertyEditorDescriptor()
    {
        MapTypeModel attributeType = (MapTypeModel)this.attributeDescriptor.getAttributeType();
        this.keyType = attributeType.getArgumentType();
        String argumentCode = null;
        if(this.propertyDescriptor.isLocalized())
        {
            if(attributeType.getReturntype() instanceof MapTypeModel)
            {
                MapTypeModel returnType = (MapTypeModel)attributeType.getReturntype();
                this.keyType = returnType.getArgumentType();
                argumentCode = this.keyType.getCode();
            }
        }
        else
        {
            argumentCode = this.keyType.getCode();
        }
        String editorType = this.propertyService.getDefaultEditorType(argumentCode, false);
        Collection<PropertyEditorDescriptor> keyEditorDescriptors = this.editorFactory.getMatchingEditorDescriptors(editorType);
        if(CollectionUtils.isNotEmpty(keyEditorDescriptors))
        {
            return keyEditorDescriptors.iterator().next();
        }
        return null;
    }


    protected PropertyEditorDescriptor getValuePropertyEditorDescriptor()
    {
        TypeModel valueType = ((MapTypeModel)this.attributeDescriptor.getAttributeType()).getReturntype();
        String editorType = this.propertyService.getDefaultEditorType(valueType.getCode(), false);
        Collection<PropertyEditorDescriptor> valueEditorDescriptors = this.editorFactory.getMatchingEditorDescriptors(editorType);
        if(CollectionUtils.isNotEmpty(valueEditorDescriptors))
        {
            return valueEditorDescriptors.iterator().next();
        }
        return null;
    }


    private UIEditor createKeyUIEditor()
    {
        UIEditor keyUIEditor = this.keyPropertyEditorDescriptor.createUIEditor();
        if(keyUIEditor instanceof ListUIEditor)
        {
            List availableValues = getAvailableValues(this.keyType);
            ((ListUIEditor)keyUIEditor).setAvailableValues(availableValues);
            this.mapUIEditor.setAvailableValues(availableValues);
        }
        keyUIEditor.setEditable(this.mapUIEditor.isEditable());
        return keyUIEditor;
    }


    protected List getAvailableValues(TypeModel type)
    {
        List<Object> ret = new ArrayList();
        if(type instanceof EnumerationMetaTypeModel)
        {
            Collection<ItemModel> vals = ((EnumerationMetaTypeModel)type).getValues();
            for(ItemModel enumerationValue : vals)
            {
                if(enumerationValue instanceof EnumerationValueModel)
                {
                    ret.add(this.modelService.get(((EnumerationValueModel)enumerationValue).getPk()));
                    continue;
                }
                ret.add(this.cockpitTypeService.wrapItem(enumerationValue));
            }
        }
        return ret;
    }


    protected AttributeDescriptorModel getAttributeDescriptor(PropertyDescriptor propertyDescriptor)
    {
        ComposedTypeModel composedType = this.coreTypeService.getComposedTypeForCode(((ItemAttributePropertyDescriptor)propertyDescriptor).getTypeCode());
        AttributeDescriptorModel attributeDescriptor = this.coreTypeService.getAttributeDescriptor(composedType, ((ItemAttributePropertyDescriptor)propertyDescriptor)
                        .getAttributeQualifier());
        return attributeDescriptor;
    }


    protected Image createRemoveImage(Listitem itemRow)
    {
        Image removeImage = null;
        if(this.mapUIEditor.isEditable())
        {
            removeImage = new Image("/cockpit/images/remove.png");
            removeImage.setTooltiptext(Labels.getLabel("collectionEditor.button.remove.tooltip"));
            removeImage.addEventListener("onClick", (EventListener)new Object(this, itemRow));
        }
        return removeImage;
    }


    protected EditorListener keyListener(Listitem itemRow)
    {
        return (EditorListener)new Object(this, itemRow);
    }


    protected EditorListener valueListener(Listitem itemRow)
    {
        return (EditorListener)new Object(this, itemRow);
    }


    protected <T> Map<T, T> replaceKey(int index, T newKey, Map<T, T> map)
    {
        Object[] entries = map.entrySet().toArray();
        LinkedHashMap<T, T> newMap = new LinkedHashMap<>();
        int i;
        for(i = 0; i < index; i++)
        {
            Map.Entry<T, T> entry = (Map.Entry<T, T>)entries[i];
            newMap.put(entry.getKey(), entry.getValue());
        }
        newMap.put(newKey, ((Map.Entry<?, T>)entries[index]).getValue());
        for(i = index + 1; i < entries.length; i++)
        {
            Map.Entry<T, T> entry = (Map.Entry<T, T>)entries[i];
            newMap.put(entry.getKey(), entry.getValue());
        }
        return newMap;
    }


    protected <T> Map<T, T> replaceValue(int index, T newValue, Map<T, T> map)
    {
        Object[] entries = map.entrySet().toArray();
        LinkedHashMap<T, T> newMap = new LinkedHashMap<>();
        int i;
        for(i = 0; i < index; i++)
        {
            Map.Entry<T, T> entry = (Map.Entry<T, T>)entries[i];
            newMap.put(entry.getKey(), entry.getValue());
        }
        newMap.put(((Map.Entry<T, ?>)entries[index]).getKey(), newValue);
        for(i = index + 1; i < entries.length; i++)
        {
            Map.Entry<T, T> entry = (Map.Entry<T, T>)entries[i];
            newMap.put(entry.getKey(), entry.getValue());
        }
        return newMap;
    }


    protected boolean valueIsEmpty(Object value)
    {
        if(value == null)
        {
            return true;
        }
        if(value instanceof Map.Entry)
        {
            return (((Map.Entry)value).getKey() == null);
        }
        return false;
    }


    protected Listcell createListCell(Component editorView, Image image)
    {
        Listcell cellItem = new Listcell();
        Div cellContainerDiv = new Div();
        cellContainerDiv.setSclass("collectionUIEditorItem");
        Div firstCellDiv = new Div();
        firstCellDiv.appendChild(editorView);
        cellContainerDiv.appendChild((Component)firstCellDiv);
        if(image != null)
        {
            firstCellDiv.setSclass("editor");
            Div secondCellDiv = new Div();
            secondCellDiv.appendChild((Component)image);
            secondCellDiv.setSclass("image");
            cellContainerDiv.appendChild((Component)secondCellDiv);
        }
        cellItem.appendChild((Component)cellContainerDiv);
        return cellItem;
    }
}
