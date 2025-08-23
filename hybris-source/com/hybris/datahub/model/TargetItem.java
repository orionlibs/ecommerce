package com.hybris.datahub.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hybris.datahub.cache.ItemTypeCache;
import com.hybris.datahub.domain.TargetAttributeDefinition;
import com.hybris.datahub.domain.TargetItemMetadata;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TargetItem extends LocalizedBaseDataItem implements Cloneable
{
    protected static ItemTypeCache itemTypeCache;
    private CanonicalItem canonicalItem;
    private TargetSystemPublication targetSystemPublication;
    private String exportCode;
    private Map<String, String> exportCodeAttributeMap = Maps.newHashMap();
    private TargetItemDelegate targetItemDelegate;


    protected TargetItem(String type)
    {
        super(type);
    }


    public static void setItemTypeCache(ItemTypeCache itemTypeCache)
    {
        TargetItem.itemTypeCache = itemTypeCache;
    }


    public static boolean isValidType(String type)
    {
        return (type != null && itemTypeCache.typeExists(type));
    }


    public static Optional<DataItemAttribute> getAttributeDefinition(TargetAttributeDefinition def)
    {
        TargetItemMetadata itemMetadata = def.getTargetItemMetadata();
        return getAttributeDefinition(itemMetadata.getItemType(), def.getAttributeName(), itemMetadata.getTargetSystem().getTargetSystemName());
    }


    public static Optional<DataItemAttribute> getAttributeDefinition(String itemType, String attributeName, String targetSystemName)
    {
        if(isValidType(itemType))
        {
            return Optional.fromNullable(dataItemService.getDataItemAttribute(itemType, attributeName, targetSystemName));
        }
        return Optional.absent();
    }


    public static List<DataItemAttribute> getAttributeDefinitions(String itemType, String targetSystemName)
    {
        return new ArrayList<>(getTypeAttributes(itemType, targetSystemName).getAll());
    }


    public static TypeAttributeDefinitions getTypeAttributes(String itemType, String targetSystemName)
    {
        return dataItemService.getItemTypeAttributes(itemType, targetSystemName);
    }


    public static TargetItem instantiate(String type, TargetSystemPublication pub, CanonicalItem src)
    {
        Preconditions.checkArgument((type != null), "The target item type cannot be null");
        if(!isValidType(type))
        {
            throw new IllegalArgumentException(type + " is not a valid TargetItem type.");
        }
        TargetItem item = new TargetItem(type);
        return populateNewItem(item, pub, src);
    }


    protected static TargetItem populateNewItem(TargetItem item, TargetSystemPublication pub, CanonicalItem src)
    {
        item.setCanonicalItem(src);
        item.setDataPool((pub != null) ? pub.getPublicationAction().getPool() : null);
        item.setTargetSystemPublication(pub);
        return item;
    }


    public Optional<DataItemAttribute> getAttributeDefinition(String attributeName)
    {
        if(this.targetSystemPublication != null)
        {
            return getAttributeDefinition(attributeName, this.targetSystemPublication);
        }
        return Optional.absent();
    }


    private Optional<DataItemAttribute> getAttributeDefinition(String attrName, TargetSystemPublication publication)
    {
        return getAttributeDefinition(getType(), attrName, publication.getTargetSystem().getTargetSystemName());
    }


    public List<DataItemAttribute> getAttributeDefinitions()
    {
        TypeAttributeDefinitions definitions = (this.targetSystemPublication == null) ? new TypeAttributeDefinitions(getType(), Collections.emptyList()) : getTypeAttributes(getType(), this.targetSystemPublication.getTargetSystem().getTargetSystemName());
        return new ArrayList<>(definitions.getAll());
    }


    public TypeAttributeDefinitions getTypeAttributes()
    {
        return (this.targetSystemPublication == null) ?
                        new TypeAttributeDefinitions(getType(), Collections.emptyList()) :
                        getTypeAttributes(getType(), this.targetSystemPublication.getTargetSystem().getTargetSystemName());
    }


    public CanonicalItem getCanonicalItem()
    {
        if(this.canonicalItem == null)
        {
            if(getTargetItemDelegate() == null)
            {
                return null;
            }
            this.canonicalItem = getTargetItemDelegate().getCanonicalItem();
        }
        return this.canonicalItem;
    }


    public void setCanonicalItem(CanonicalItem canonicalItem)
    {
        if(this.canonicalItem != null && !this.canonicalItem.equals(canonicalItem))
        {
            throw new IllegalArgumentException("TargetItem field canonicalItem cannot be changed after it is defined");
        }
        this.canonicalItem = canonicalItem;
    }


    public TargetSystemPublication getTargetSystemPublication()
    {
        return this.targetSystemPublication;
    }


    public void setTargetSystemPublication(TargetSystemPublication targetSystemPublication)
    {
        if(this.targetSystemPublication != null && !this.targetSystemPublication.equals(targetSystemPublication))
        {
            throw new IllegalArgumentException("TargetItem field targetSystemPublication cannot be changed after it is defined");
        }
        this.targetSystemPublication = targetSystemPublication;
    }


    public String getExportCode()
    {
        return this.exportCode;
    }


    public void setExportCode(String exportCode)
    {
        this.exportCode = exportCode;
    }


    Map<String, String> getExportCodeAttributeMap()
    {
        if(getTargetItemDelegate() != null && this.exportCodeAttributeMap.isEmpty())
        {
            this.exportCodeAttributeMap = getTargetItemDelegate().getExportCodeAttributeMap();
        }
        return (Map<String, String>)ImmutableMap.copyOf(this.exportCodeAttributeMap);
    }


    void setExportCodeAttributeMap(Map<String, String> exportCodeAttributeMap)
    {
        this.exportCodeAttributeMap = exportCodeAttributeMap;
    }


    public Optional<String> getExportCodeAttributeByName(String attributeName)
    {
        Optional<String> expCode = Optional.absent();
        if(getExportCodeAttributeMap().get(attributeName) != null)
        {
            expCode = Optional.of(getExportCodeAttributeMap().get(attributeName));
        }
        else
        {
            DataItemAttribute dataItemAttribute = dataItemService.getDataItemAttribute(getType(), attributeName,
                            getTargetSystemPublication().getTargetSystem().getTargetSystemName());
            if(dataItemAttribute != null)
            {
                expCode = Optional.of(((TargetDataItemAttribute)dataItemAttribute).getExportCode());
            }
        }
        return expCode;
    }


    void addToExportCodeMap(String attributeName, String attributeValue)
    {
        this.exportCodeAttributeMap.put(attributeName, attributeValue);
    }


    public Object cloneWithoutAttributes()
    {
        TargetItem newItem = instantiate(getType(), getTargetSystemPublication(), getCanonicalItem());
        newItem.setTargetSystemPublication(getTargetSystemPublication());
        newItem.setId(getId());
        newItem.setExportCode(getExportCode());
        newItem.setExportCodeAttributeMap(Maps.newHashMap(getExportCodeAttributeMap()));
        newItem.setCanonicalItem(getCanonicalItem());
        return newItem;
    }


    public Object clone() throws CloneNotSupportedException
    {
        TargetItem newItem = (TargetItem)cloneWithoutAttributes();
        newItem.setFields(Maps.newHashMap(getFields()));
        return newItem;
    }


    protected TargetItemDelegate getTargetItemDelegate()
    {
        return this.targetItemDelegate;
    }


    public void setTargetItemDelegate(TargetItemDelegate targetItemDelegate)
    {
        if(this.targetItemDelegate != null && !this.targetItemDelegate.equals(targetItemDelegate))
        {
            throw new IllegalArgumentException("TargetItem targetItemDelegate cannot be changed after it is defined");
        }
        this.targetItemDelegate = targetItemDelegate;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        TargetItem that = (TargetItem)o;
        if((this.canonicalItem != null) ? !this.canonicalItem.equals(that.canonicalItem) : (that.canonicalItem != null))
        {
            return false;
        }
        if((this.exportCode != null) ? !this.exportCode.equals(that.exportCode) : (that.exportCode != null))
        {
            return false;
        }
        if((this.exportCodeAttributeMap != null) ? !this.exportCodeAttributeMap.equals(that.exportCodeAttributeMap) : (that.exportCodeAttributeMap != null))
        {
            return false;
        }
        if((this.targetSystemPublication != null) ? !this.targetSystemPublication.equals(that.targetSystemPublication) : (that.targetSystemPublication != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ((this.canonicalItem != null) ? this.canonicalItem.hashCode() : 0);
        result = 31 * result + ((this.targetSystemPublication != null) ? this.targetSystemPublication.hashCode() : 0);
        result = 31 * result + ((this.exportCode != null) ? this.exportCode.hashCode() : 0);
        result = 31 * result + ((this.exportCodeAttributeMap != null) ? this.exportCodeAttributeMap.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "TargetItem{" + super
                        .toString() + "canonicalItem=" + this.canonicalItem + ", targetSystemPublication=" + this.targetSystemPublication + ", exportCode='" + this.exportCode + "', exportCodeAttributeMap=" + this.exportCodeAttributeMap + "}";
    }
}
