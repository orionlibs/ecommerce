package com.hybris.datahub.model;

import com.google.common.base.Optional;
import com.hybris.datahub.cache.ItemTypeCache;
import com.hybris.datahub.domain.CompositionStatusType;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatusType;
import com.hybris.datahub.runtime.domain.CompositionAction;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanonicalItem extends LocalizedBaseDataItem implements Cloneable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CanonicalItem.class);
    protected static ItemTypeCache itemTypeCache;
    private String integrationKey;
    private CompositionStatusType status;
    private String compositionStatusDetail;
    private CompositionAction compositionAction;
    private CanonicalItemDelegate canonicalItemDelegate;
    private String batchId;
    private String traceId;
    private String uuid;
    private String documentId;


    protected CanonicalItem(String type)
    {
        super(type);
    }


    public static void setItemTypeCache(ItemTypeCache itemTypeCache)
    {
        CanonicalItem.itemTypeCache = itemTypeCache;
    }


    public static Set<String> getItemTypes()
    {
        return itemTypeCache.getTypes();
    }


    public static boolean isValidType(String itemType)
    {
        return (itemType != null && itemTypeCache.typeExists(itemType));
    }


    public static Optional<DataItemAttribute> getAttributeDefinition(String itemType, String attributeName)
    {
        if(!isValidType(itemType))
        {
            LOGGER.debug("Definition of attribute {} is requested from invalid type {}", attributeName, itemType);
            return Optional.absent();
        }
        return Optional.fromNullable(dataItemService.getDataItemAttribute(itemType, attributeName));
    }


    public static TypeAttributeDefinitions getTypeAttributes(String itemType)
    {
        return dataItemService.getItemTypeAttributes(itemType);
    }


    public static CanonicalItem instantiate(String type)
    {
        if(!isValidType(type))
        {
            throw new IllegalArgumentException(type + " is not a valid CanonicalItem type.");
        }
        return new CanonicalItem(type);
    }


    public static <T extends CanonicalItem> T copyOf(T source)
    {
        try
        {
            return (T)source.clone();
        }
        catch(CloneNotSupportedException e)
        {
            if(LOGGER.isTraceEnabled())
            {
                LOGGER.trace("Clone is not supported for {}.", source, e);
            }
            return null;
        }
    }


    public Optional<DataItemAttribute> getAttributeDefinition(String attributeName)
    {
        return getAttributeDefinition(getType(), attributeName);
    }


    public List<DataItemAttribute> getAttributeDefinitions()
    {
        return new ArrayList<>(getTypeAttributes(getType()).getAll());
    }


    public TypeAttributeDefinitions getTypeAttributes()
    {
        return getTypeAttributes(getType());
    }


    public CompositionStatusType getStatus()
    {
        return this.status;
    }


    public void setStatus(CompositionStatusType status)
    {
        this.status = status;
    }


    public Set<RawItem> getRawItems()
    {
        if(this.canonicalItemDelegate != null)
        {
            return this.canonicalItemDelegate.getRawItems();
        }
        return new HashSet<>();
    }


    public String getCompositionStatusDetail()
    {
        return this.compositionStatusDetail;
    }


    public void setCompositionStatusDetail(String compositionStatusDetail)
    {
        this.compositionStatusDetail = compositionStatusDetail;
    }


    public void invalidate(CanonicalItemCompositionStatusDetail detail)
    {
        setStatus(CompositionStatusType.ERROR);
        setCompositionStatusDetail(detail.name());
    }


    public CompositionAction getCompositionAction()
    {
        return this.compositionAction;
    }


    public void setCompositionAction(CompositionAction compositionAction)
    {
        if(this.compositionAction != null && !this.compositionAction.equals(compositionAction))
        {
            throw new IllegalArgumentException("CanonicalItem compositionAction cannot be changed after it is defined");
        }
        this.compositionAction = compositionAction;
    }


    public String getIntegrationKey()
    {
        return this.integrationKey;
    }


    public void setIntegrationKey(String integrationKey)
    {
        if(this.integrationKey != null && !this.integrationKey.equals(integrationKey))
        {
            throw new IllegalArgumentException("CanonicalItem integrationKey cannot be changed after it is defined");
        }
        this.integrationKey = integrationKey;
    }


    public Set<CanonicalItemPublicationStatus> getPublicationStatuses()
    {
        if(this.canonicalItemDelegate != null)
        {
            return this.canonicalItemDelegate.getPublicationStatuses();
        }
        return new HashSet<>();
    }


    protected void overrideCanonicalItemId(long id)
    {
        overrideId(Long.valueOf(id));
    }


    protected CanonicalItemDelegate getCanonicalItemDelegate()
    {
        return this.canonicalItemDelegate;
    }


    public void setCanonicalItemDelegate(CanonicalItemDelegate canonicalItemDelegate)
    {
        if(this.canonicalItemDelegate != null && !this.canonicalItemDelegate.equals(canonicalItemDelegate))
        {
            throw new IllegalArgumentException("CanonicalItem canonicalItemDelegate cannot be changed after it is defined");
        }
        this.canonicalItemDelegate = canonicalItemDelegate;
    }


    protected DataItemAttribute getDataItemAttribute(String fieldName)
    {
        assert dataItemService != null;
        DataItemAttribute attribute = dataItemService.getDataItemAttribute(getType(), fieldName);
        if(attribute == null)
        {
            throw new InvalidAttributeException("Attribute " + fieldName + " does not exist in " + getClass().getSimpleName());
        }
        return attribute;
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


    public String getDocumentId()
    {
        return this.documentId;
    }


    public void setDocumentId(String documentId)
    {
        this.documentId = documentId;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setUuid(String uuid)
    {
        if(this.uuid != null && !this.uuid.equals(uuid))
        {
            throw new IllegalArgumentException("CanonicalItem uuid cannot be changed after it is defined");
        }
        this.uuid = uuid;
    }


    public CanonicalItem cloneWithoutAttributes()
    {
        CanonicalItem newItem = (CanonicalItem)super.cloneWithoutAttributes();
        newItem.setDataPool(getDataPool());
        newItem.setStatus(getStatus());
        newItem.setIntegrationKey(getIntegrationKey());
        newItem.setCompositionStatusDetail(getCompositionStatusDetail());
        newItem.setCompositionAction(getCompositionAction());
        newItem.setCanonicalItemDelegate(this.canonicalItemDelegate);
        newItem.setTraceId(this.traceId);
        newItem.setBatchId(this.batchId);
        newItem.setUuid(this.uuid);
        newItem.setDocumentId(this.documentId);
        return newItem;
    }


    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }


    public CanonicalItemPublicationStatusType getPublicationStatus(TargetSystemPublication pub)
    {
        return getPublicationStatuses().stream()
                        .filter(st -> st.getTargetSystemPublication().equals(pub))
                        .map(CanonicalItemPublicationStatus::getStatus)
                        .findAny()
                        .orElse(null);
    }


    public String toString()
    {
        String idMsg = (getId() != null) ? ("id='" + getId() + "', ") : "";
        String integrationKeyMsg = (this.integrationKey != null) ? ("integrationKey='" + this.integrationKey + "', ") : "";
        String statusMsg = (this.status != null) ? ("status=" + this.status + ", ") : "";
        String dataPoolMsg = (getDataPool() != null) ? ("dataPool=" + getDataPool() + ", ") : "";
        String batchIdMsg = (getBatchId() != null) ? ("batchId=" + getBatchId() + ", ") : "";
        String traceIdMsg = (getTraceId() != null) ? ("traceId=" + getTraceId() + ", ") : "";
        String uuidMsg = (getUuid() != null) ? ("uuid=" + getUuid() + ", ") : "";
        String documentIdMsg = (getDocumentId() != null) ? ("documentId=" + getDocumentId() + ", ") : "";
        return "CanonicalItem{" + idMsg + integrationKeyMsg + statusMsg + dataPoolMsg + batchIdMsg + traceIdMsg + uuidMsg + documentIdMsg + "fields=" +
                        printFields() + "}";
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o != null && getClass() == o.getClass())
        {
            CanonicalItem that = (CanonicalItem)o;
            return (super.equals(o) &&
                            Objects.equals(this.canonicalItemDelegate, that.canonicalItemDelegate) &&
                            Objects.equals(this.compositionStatusDetail, that.compositionStatusDetail) &&
                            Objects.equals(this.batchId, that.batchId) &&
                            Objects.equals(this.traceId, that.traceId) &&
                            Objects.equals(this.uuid, that.uuid) &&
                            Objects.equals(this.documentId, that.documentId) &&
                            Objects.equals(this.integrationKey, that.integrationKey) && this.status == that.status);
        }
        return false;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ((this.integrationKey != null) ? this.integrationKey.hashCode() : 0);
        result = 31 * result + ((this.status != null) ? this.status.hashCode() : 0);
        result = 31 * result + ((this.batchId != null) ? this.batchId.hashCode() : 0);
        result = 31 * result + ((this.traceId != null) ? this.traceId.hashCode() : 0);
        result = 31 * result + ((this.uuid != null) ? this.uuid.hashCode() : 0);
        result = 31 * result + ((this.documentId != null) ? this.documentId.hashCode() : 0);
        result = 31 * result + ((this.compositionStatusDetail != null) ? this.compositionStatusDetail.hashCode() : 0);
        result = 31 * result + ((this.canonicalItemDelegate != null) ? this.canonicalItemDelegate.hashCode() : 0);
        return result;
    }
}
