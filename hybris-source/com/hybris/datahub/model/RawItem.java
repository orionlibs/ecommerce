package com.hybris.datahub.model;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.hybris.datahub.domain.RawItemStatusType;
import com.hybris.datahub.runtime.domain.DataLoadingAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class RawItem extends BaseDataItem implements Cloneable
{
    protected static final Map<String, ImmutableMap<String, Boolean>> rawItemMetadataMap = new HashMap<>();
    protected static final Map<Pair<String, String>, DataItemAttribute> memoizedAttributeDefinition = new HashMap<>();
    protected static final Map<String, TypeAttributeDefinitions> memoizedTypeDefinitions = new HashMap<>();
    private String isoCode;
    private DataLoadingAction dataLoadingAction;
    private RawItemStatusType status;
    private RawItemDelegate rawItemDelegate;
    private boolean delete;
    private String batchId;
    private String traceId;
    private String uuid;


    protected RawItem(String type)
    {
        super(type);
    }


    public static boolean isValidType(String type)
    {
        return (rawItemMetadataMap.get(type) != null);
    }


    public static Set<String> getItemTypes()
    {
        return rawItemMetadataMap.keySet();
    }


    public static Optional<DataItemAttribute> getAttributeDefinition(String itemType, String attributeName)
    {
        BaseDataItemAttribute baseDataItemAttribute;
        ImmutablePair immutablePair = new ImmutablePair(itemType, attributeName);
        DataItemAttribute result = memoizedAttributeDefinition.get(immutablePair);
        if(result == null && rawItemMetadataMap.get(itemType) != null)
        {
            Boolean isSecured = (Boolean)((ImmutableMap)rawItemMetadataMap.get(itemType)).get(attributeName);
            if(isSecured != null)
            {
                baseDataItemAttribute = new BaseDataItemAttribute(false, String.class, attributeName, isSecured.booleanValue(), itemType, false);
                memoizedAttributeDefinition.put(immutablePair, baseDataItemAttribute);
            }
        }
        return Optional.fromNullable(baseDataItemAttribute);
    }


    public static TypeAttributeDefinitions getTypeAttributes(String itemType)
    {
        if(itemType == null)
        {
            return new TypeAttributeDefinitions(null, new ArrayList());
        }
        TypeAttributeDefinitions result = memoizedTypeDefinitions.get(itemType);
        if(result == null)
        {
            List<DataItemAttribute> attrList = new ArrayList<>();
            if(rawItemMetadataMap.get(itemType) != null)
            {
                attrList.addAll((Collection<? extends DataItemAttribute>)((ImmutableMap)rawItemMetadataMap.get(itemType)).entrySet().stream()
                                .filter(entry -> (entry.getValue() != null))
                                .map(entry -> new BaseDataItemAttribute(false, String.class, (String)entry.getKey(), ((Boolean)entry.getValue()).booleanValue(), itemType, false))
                                .collect(Collectors.toList()));
            }
            result = new TypeAttributeDefinitions(itemType, attrList);
            memoizedTypeDefinitions.put(itemType, result);
        }
        return result;
    }


    public static RawItem instantiate(String type)
    {
        if(!isValidType(type))
        {
            throw new IllegalArgumentException(type + " is not a valid RawItem type.");
        }
        return new RawItem(type);
    }


    public Optional<DataItemAttribute> getAttributeDefinition(String attributeName)
    {
        return getAttributeDefinition(getType(), attributeName);
    }


    public List<DataItemAttribute> getAttributeDefinitions()
    {
        return new ArrayList<>(getTypeAttributes().getAll());
    }


    public TypeAttributeDefinitions getTypeAttributes()
    {
        return getTypeAttributes(getType());
    }


    public Map<String, Object> getFields()
    {
        Map<String, Object> mappedFields = super.getFields();
        ((ImmutableMap)rawItemMetadataMap.get(getType())).keySet().stream()
                        .filter(attrName -> !mappedFields.containsKey(attrName))
                        .forEach(attrName -> mappedFields.put(attrName, null));
        return Collections.unmodifiableMap(mappedFields);
    }


    public RawItemStatusType getStatus()
    {
        return this.status;
    }


    public void setStatus(RawItemStatusType status)
    {
        this.status = status;
    }


    public DataLoadingAction getDataLoadingAction()
    {
        return this.dataLoadingAction;
    }


    public void setDataLoadingAction(DataLoadingAction dataLoadingAction)
    {
        if(this.dataLoadingAction != null && !this.dataLoadingAction.equals(dataLoadingAction))
        {
            throw new IllegalArgumentException("RawItem dataLoadingAction cannot be changed after it is defined");
        }
        this.dataLoadingAction = dataLoadingAction;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        if(this.isoCode != null && !this.isoCode.equals(isoCode))
        {
            throw new IllegalArgumentException("RawItem isoCode cannot be changed after it is defined");
        }
        this.isoCode = isoCode;
    }


    public String getBatchId()
    {
        return this.batchId;
    }


    public void setBatchId(String batchId)
    {
        this.batchId = batchId;
    }


    public String getTraceId()
    {
        return this.traceId;
    }


    public void setTraceId(String traceId)
    {
        this.traceId = traceId;
    }


    public boolean isDelete()
    {
        return this.delete;
    }


    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }


    public RawItemDelegate getRawItemDelegate()
    {
        return this.rawItemDelegate;
    }


    public void setRawItemDelegate(RawItemDelegate rawItemDelegate)
    {
        if(this.rawItemDelegate != null && !this.rawItemDelegate.equals(rawItemDelegate))
        {
            throw new IllegalArgumentException("RawItem rawItemDelegate cannot be changed after it is defined");
        }
        this.rawItemDelegate = rawItemDelegate;
    }


    public Set<CanonicalItem> getCanonicalItems()
    {
        if(this.rawItemDelegate != null)
        {
            return this.rawItemDelegate.getCanonicalItems();
        }
        return new HashSet<>();
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setUuid(String uuid)
    {
        if(this.uuid != null && !this.uuid.equals(uuid))
        {
            throw new IllegalArgumentException("RawItem uuid cannot be changed after it is defined");
        }
        this.uuid = uuid;
    }


    public RawItem cloneWithoutAttributes()
    {
        RawItem newItem = (RawItem)super.cloneWithoutAttributes();
        newItem.setDataPool(getDataPool());
        newItem.setIsoCode(getIsoCode());
        newItem.setStatus(getStatus());
        newItem.setDelete(isDelete());
        newItem.setBatchId(getBatchId());
        newItem.setTraceId(getTraceId());
        newItem.setUuid(this.uuid);
        newItem.setDataLoadingAction(this.dataLoadingAction);
        newItem.setRawItemDelegate(getRawItemDelegate());
        return newItem;
    }


    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }


    public String toString()
    {
        return "RawItem{type= '" + getType() + "', id=" +
                        getId() + (
                        (this.isoCode != null) ? (", isoCode='" + this.isoCode + "'") : "") + ", status=" + this.status + (
                        this.delete ? ", delete=true" : "") + (
                        (getDataPool() != null) ? (", dataPool=" + getDataPool()) : "") + ", batchId=" +
                        getBatchId() + ", traceId=" +
                        getTraceId() + ", uuid=" +
                        getUuid() + ", fields=" +
                        printFields() + "}";
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
        RawItem rawItem = (RawItem)o;
        if((this.dataLoadingAction != null) ? !this.dataLoadingAction.equals(rawItem.dataLoadingAction) : (rawItem.dataLoadingAction != null))
        {
            return false;
        }
        if((this.isoCode != null) ? !this.isoCode.equals(rawItem.isoCode) : (rawItem.isoCode != null))
        {
            return false;
        }
        if((this.batchId != null) ? !this.batchId.equals(rawItem.batchId) : (rawItem.batchId != null))
        {
            return false;
        }
        if((this.traceId != null) ? !this.traceId.equals(rawItem.traceId) : (rawItem.traceId != null))
        {
            return false;
        }
        if((this.uuid != null) ? !this.uuid.equals(rawItem.uuid) : (rawItem.uuid != null))
        {
            return false;
        }
        return (this.status == rawItem.status && this.delete == rawItem.delete);
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ((this.isoCode != null) ? this.isoCode.hashCode() : 0);
        result = 31 * result + ((this.dataLoadingAction != null) ? this.dataLoadingAction.hashCode() : 0);
        result = 31 * result + ((this.batchId != null) ? this.batchId.hashCode() : 0);
        result = 31 * result + ((this.traceId != null) ? this.traceId.hashCode() : 0);
        result = 31 * result + ((this.uuid != null) ? this.uuid.hashCode() : 0);
        result = 31 * result + ((this.status != null) ? this.status.hashCode() : 0);
        result = 31 * result + (this.delete ? 1 : 0);
        return result;
    }
}
