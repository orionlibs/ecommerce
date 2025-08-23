package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.util.Config;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractEntityStatementsBuilder extends AbstractStoreStatementsBuilder
{
    protected final PK itemPk;
    protected AtomicLong currentOptimisticLockCounter;
    protected final String typeCode;
    protected final Config.DatabaseName databaseName;


    public AbstractEntityStatementsBuilder(EntityRecord record, Config.DatabaseName databaseName)
    {
        Preconditions.checkNotNull(record, "EntityRecord is required");
        Preconditions.checkNotNull(databaseName, "Current databaseName is required");
        this.infoMap = DirectPersistenceUtils.getInfoMapForType(record.getType());
        Preconditions.checkState(!this.infoMap.isJaloOnly(), "JaloOnly items not supported by DirectPersistence!");
        this.databaseName = databaseName;
        this.itemPk = record.getPK();
        this.currentOptimisticLockCounter = new AtomicLong(0L);
        this.typeCode = record.getType();
    }


    protected Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> prepareDataBasePayload(Set<PropertyHolder> payload, TypeInfoMap infoMap)
    {
        Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> result = new HashMap<>();
        result.put(ColumnPayload.TargetTableType.ITEM, new LinkedHashSet<>());
        result.put(ColumnPayload.TargetTableType.LP, new LinkedHashSet<>());
        result.put(ColumnPayload.TargetTableType.PROPS, new LinkedHashSet<>());
        for(PropertyHolder propertyHolder : payload)
        {
            ColumnPayload columnPayload = DirectPersistenceUtils.createColumnPayloadForProperty(propertyHolder.getName(), propertyHolder
                            .getValue(), infoMap);
            if(columnPayload != null)
            {
                Set<ColumnPayload> columnsForTableType = result.get(columnPayload.getTargetTableType());
                columnsForTableType.add(columnPayload);
            }
        }
        return (Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>)ImmutableMap.copyOf(result);
    }


    protected Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> mergeDataBasePayload(Map<ColumnPayload.TargetTableType, Set<ColumnPayload>> result, Set<PropertyHolder> payload, TypeInfoMap infoMap)
    {
        for(PropertyHolder propertyHolder : payload)
        {
            ColumnPayload columnPayload = DirectPersistenceUtils.createColumnPayloadForProperty(propertyHolder.getName(), propertyHolder
                            .getValue(), infoMap);
            if(columnPayload != null)
            {
                ((Set<ColumnPayload>)result.get(columnPayload.getTargetTableType())).add(columnPayload);
            }
        }
        return (Map<ColumnPayload.TargetTableType, Set<ColumnPayload>>)ImmutableMap.copyOf(result);
    }


    protected Long getCurrentOptimistiLockCounter()
    {
        return Long.valueOf(this.currentOptimisticLockCounter.get());
    }


    protected PK getItemPk()
    {
        return this.itemPk;
    }


    protected String getTypeCode()
    {
        return this.typeCode;
    }
}
