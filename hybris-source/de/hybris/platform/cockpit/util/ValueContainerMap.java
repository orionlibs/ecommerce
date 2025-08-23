package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;

public class ValueContainerMap implements Map
{
    private final ObjectValueContainer container;
    private Map<String, ItemAttributePropertyDescriptor> attributeMap;
    private Map<ItemAttributePropertyDescriptor, Object> valueMap;
    private final boolean allValues;
    private final PropertyFilter filter;


    public ValueContainerMap(ObjectValueContainer container, boolean allValues, PropertyFilter filter)
    {
        this.container = container;
        this.allValues = allValues;
        this.filter = filter;
        buildKeyMap();
        collectValues();
    }


    public boolean containsKey(Object arg0)
    {
        return this.attributeMap.containsKey(arg0);
    }


    public boolean containsValue(Object arg0)
    {
        return this.valueMap.containsValue(arg0);
    }


    public Set<Map.Entry> entrySet()
    {
        Set<Map.Entry> set = new HashSet<>();
        for(String key : this.attributeMap.keySet())
        {
            set.add(new MapEntry(this, key, this.valueMap.get(this.attributeMap.get(key))));
        }
        return set;
    }


    public Object get(Object arg0)
    {
        ItemAttributePropertyDescriptor propertyDescriptor = this.attributeMap.get(arg0);
        return (propertyDescriptor == null) ? null : this.valueMap.get(propertyDescriptor);
    }


    public boolean isEmpty()
    {
        return this.attributeMap.isEmpty();
    }


    public Set keySet()
    {
        return this.attributeMap.keySet();
    }


    public int size()
    {
        return this.attributeMap.size();
    }


    public Collection values()
    {
        Collection values = new ArrayList();
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> entry : this.valueMap.entrySet())
        {
            ItemAttributePropertyDescriptor iapd = entry.getKey();
            if(iapd.isSingleAttribute())
            {
                values.add(this.valueMap.get(entry.getKey()));
            }
        }
        return values;
    }


    public void clear()
    {
        throw new UnsupportedOperationException();
    }


    public Object put(Object arg0, Object arg1)
    {
        throw new UnsupportedOperationException();
    }


    public void putAll(Map arg0)
    {
        throw new UnsupportedOperationException();
    }


    public Object remove(Object arg0)
    {
        throw new UnsupportedOperationException();
    }


    private void buildKeyMap()
    {
        this.attributeMap = new HashMap<>();
        for(PropertyDescriptor propertyDescriptor : this.container.getPropertyDescriptors())
        {
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
            {
                this.attributeMap.put(((ItemAttributePropertyDescriptor)propertyDescriptor).getAttributeQualifier().toLowerCase(), (ItemAttributePropertyDescriptor)propertyDescriptor);
            }
        }
    }


    private void collectValues()
    {
        this.valueMap = new HashMap<>();
        for(ObjectValueContainer.ObjectValueHolder valueHolder : this.container.getAllValues())
        {
            if(this.allValues || valueHolder.isModified())
            {
                PropertyDescriptor propertyDescriptor = valueHolder.getPropertyDescriptor();
                if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
                {
                    ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)valueHolder.getPropertyDescriptor();
                    if(BooleanUtils.toBoolean(iapd.getLastAttributeDescriptor().getWritable()) && this.filter != null && this.filter
                                    .processProperty(iapd.getLastAttributeDescriptor()))
                    {
                        if(propertyDescriptor.isLocalized())
                        {
                            addLocalizedValue(this.valueMap, iapd, valueHolder.getLanguageIso(), valueHolder.getLocalValue());
                            continue;
                        }
                        addValue(this.valueMap, iapd, valueHolder.getLocalValue());
                    }
                }
            }
        }
    }


    private void addValue(Map<ItemAttributePropertyDescriptor, Object> valueMap, ItemAttributePropertyDescriptor propertyDescriptor, Object value)
    {
        valueMap.put(propertyDescriptor, TypeTools.container2Item(getCockpitTypeService(), value));
    }


    private void addLocalizedValue(Map<ItemAttributePropertyDescriptor, Object> valueMap, ItemAttributePropertyDescriptor propertyDescriptor, String languageIso, Object value)
    {
        Map<String, Object> locMap = (Map<String, Object>)valueMap.get(propertyDescriptor);
        if(locMap == null)
        {
            valueMap.put(propertyDescriptor, locMap = new HashMap<>());
        }
        locMap.put(languageIso, TypeTools.container2Item(getCockpitTypeService(), value));
    }


    protected TypeService getCockpitTypeService()
    {
        return (TypeService)Registry.getApplicationContext().getBean("cockpitTypeService");
    }
}
