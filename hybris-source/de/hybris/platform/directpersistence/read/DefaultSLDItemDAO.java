package de.hybris.platform.directpersistence.read;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.servicelayer.event.events.AfterTenantRestartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.encryption.ValueEncryptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultSLDItemDAO extends AbstractEventListener<AfterTenantRestartEvent> implements SLDItemDAO
{
    private static final Logger LOG = Logger.getLogger(DefaultSLDItemDAO.class);
    private String tenantID;
    private JDBCValueMappings jdbcValueMappings;
    private Config.DatabaseName databaseName;
    private ValueEncryptor valueEncryptor;
    private JdbcTemplate jdbcTemplate;
    private PersistenceManager persistenceManager;
    private PersistencePool persistencePool;


    @PostConstruct
    public void init()
    {
        this.persistenceManager = Registry.getCurrentTenantNoFallback().getPersistenceManager();
        this.tenantID = Registry.getCurrentTenantNoFallback().getTenantID();
        this.jdbcValueMappings = JDBCValueMappings.getInstance();
        this.databaseName = Config.getDatabaseName();
        this.valueEncryptor = Registry.getMasterTenant().getValueEncryptor();
        this.persistencePool = Registry.getCurrentTenantNoFallback().getPersistencePool();
    }


    public SLDDataContainer load(PK entityPk)
    {
        Preconditions.checkNotNull(entityPk, "entityPk is required");
        SLDDataContainer.Builder dataContainerBuilder = null;
        RevertibleUpdate theUpdate = OperationInfo.updateThread(getOperationInfoForTypeCode(entityPk.getTypeCode()));
        try
        {
            QueryExecutor qExecutor = QueryExecutor.openConnection();
            try
            {
                ItemDeployment itemDeployment = this.persistenceManager.getItemDeployment(entityPk.getTypeCode());
                if(itemDeployment == null)
                {
                    SLDDataContainer sLDDataContainer = null;
                    if(qExecutor != null)
                    {
                        qExecutor.close();
                    }
                    if(theUpdate != null)
                    {
                        theUpdate.close();
                    }
                    return sLDDataContainer;
                }
                String query = getLoadAttributesStatementForType(itemDeployment.getDatabaseTableName());
                Object[] params = {Long.valueOf(entityPk.getLongValue())};
                AttributesExtractor extractor = new AttributesExtractor(this, true);
                Objects.requireNonNull(extractor);
                dataContainerBuilder = (SLDDataContainer.Builder)qExecutor.query(query, extractor::extractData, params);
                if(dataContainerBuilder == null)
                {
                    SLDDataContainer sLDDataContainer = null;
                    if(qExecutor != null)
                    {
                        qExecutor.close();
                    }
                    if(theUpdate != null)
                    {
                        theUpdate.close();
                    }
                    return sLDDataContainer;
                }
                if(dataContainerBuilder.isLocalized())
                {
                    LocalizedAttributesExtractor localizedExtractor = new LocalizedAttributesExtractor(this, dataContainerBuilder, true);
                    query = getLooadLocAttributesStatementForType(itemDeployment.getDatabaseTableName());
                    Objects.requireNonNull(localizedExtractor);
                    qExecutor.query(query, localizedExtractor::extractData, new Object[] {entityPk.getLong()});
                }
                query = getLoadPropertiesStatementForType(dataContainerBuilder);
                PropertiesExtractor propertiesExtractor = new PropertiesExtractor(this, dataContainerBuilder, true);
                Objects.requireNonNull(propertiesExtractor);
                qExecutor.query(query, propertiesExtractor::extractData, new Object[] {entityPk.getLong()});
                if(qExecutor != null)
                {
                    qExecutor.close();
                }
            }
            catch(Throwable throwable)
            {
                if(qExecutor != null)
                {
                    try
                    {
                        qExecutor.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
            if(theUpdate != null)
            {
                theUpdate.close();
            }
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
        return dataContainerBuilder.build();
    }


    public List<SLDDataContainer> load(List<PK> entityPKs)
    {
        Preconditions.checkNotNull(entityPKs, "entityPKs must not be null");
        if(entityPKs.isEmpty())
        {
            return Collections.emptyList();
        }
        List<SLDDataContainer> result = null;
        Map<Integer, List<PK>> pksByType = (Map<Integer, List<PK>>)entityPKs.stream().collect(
                        Collectors.groupingBy(pk -> Integer.valueOf(pk.getTypeCode())));
        for(Map.Entry<Integer, List<PK>> entry : pksByType.entrySet())
        {
            Integer typeCode = entry.getKey();
            List<PK> pks = entry.getValue();
            ItemDeployment itemDeployment = this.persistenceManager.getItemDeployment(typeCode.intValue());
            if(itemDeployment == null)
            {
                throw new IllegalStateException("no ItemDeployment object fount for typecode " + typeCode);
            }
            RevertibleUpdate theUpdate = OperationInfo.updateThread(getOperationInfoForTypeCode(typeCode.intValue()));
            try
            {
                String query = getLoadAttributesStatmentForTypes(itemDeployment.getDatabaseTableName(), pks);
                Object[] params = pks.stream().map(PK::getLong).toArray();
                AttributesExtractor extractor = new AttributesExtractor(this, false);
                List<SLDDataContainer.Builder> builders = this.jdbcTemplate.query(query, params, (rs, rowNum) -> extractor.extractData(rs));
                if(builders.isEmpty())
                {
                    List<?> list = Collections.emptyList();
                    if(theUpdate != null)
                    {
                        theUpdate.close();
                    }
                    return (List)list;
                }
                if(result == null)
                {
                    result = new ArrayList<>();
                }
                SLDDataContainer.Builder firstBuilder = builders.get(0);
                if(firstBuilder.isLocalized())
                {
                    this.jdbcTemplate.query(getLoadLocAttributesStatementForTypes(itemDeployment.getDatabaseTableName(), pks), params, (rs, rowNum) -> {
                        (new LocalizedAttributesBulkExtractor(this, builders)).fillData(rs);
                        return null;
                    });
                }
                String propsTableName = firstBuilder.getTypeInfoMap().getOldPropTableName();
                if(StringUtils.isNotBlank(propsTableName))
                {
                    this.jdbcTemplate.query(getLoadPropertiesStatementForTypes(propsTableName, pks), params, (rs, rowNum) -> {
                        (new BulkPropertiesExtractor(this, builders)).extractData(rs);
                        return null;
                    });
                }
                result.addAll((Collection<? extends SLDDataContainer>)builders.stream().map(SLDDataContainer.Builder::build).collect(Collectors.toList()));
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        return result;
    }


    protected String getLoadAttributesStatementForType(String tableName)
    {
        return FluentSqlBuilder.builder(this.databaseName).selectAll().from(tableName).where().field(ServiceCol.PK_STRING).isEqual()
                        .toSql();
    }


    String getLoadAttributesStatmentForTypes(String tableName, List<PK> PKs)
    {
        return FluentSqlBuilder.builder(this.databaseName).selectAll().from(tableName).where().field(ServiceCol.PK_STRING).in(PKs)
                        .toSql();
    }


    private String getLooadLocAttributesStatementForType(String tableName)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName);
        builder.selectAll().from(tableName + tableName);
        return builder.where().field(ServiceCol.ITEM_PK).isEqual().toSql();
    }


    private String getLoadLocAttributesStatementForTypes(String tableName, List<PK> PKs)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName);
        builder.selectAll().from(tableName + tableName);
        return builder.where().field(ServiceCol.ITEM_PK).in(PKs).toSql();
    }


    private String getLoadPropertiesStatementForType(SLDDataContainer.Builder dataContainerBuilder)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName);
        builder.selectAll().from(dataContainerBuilder.getTypeInfoMap().getOldPropTableName()).where().field(ServiceCol.ITEM_PK)
                        .isEqual();
        return builder.toSql();
    }


    private String getLoadPropertiesStatementForTypes(String tableName, List<PK> PKs)
    {
        FluentSqlBuilder builder = FluentSqlBuilder.builder(this.databaseName);
        return builder.selectAll().from(tableName).where().field(ServiceCol.ITEM_PK).in(PKs).toSql();
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    protected void onEvent(AfterTenantRestartEvent event)
    {
        if(event.getTenantId().equals(this.tenantID))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("got correct tenant id: " + event.getTenantId());
            }
            this.persistenceManager = Registry.getTenantByID(event.getTenantId()).getPersistenceManager();
            this.persistencePool = Registry.getTenantByID(event.getTenantId()).getPersistencePool();
        }
        else
        {
            LOG.error("ignoring restart event with invalid tenant id - expected: " + this.tenantID + " but got: " + event
                            .getTenantId());
        }
    }


    private OperationInfo getOperationInfoForTypeCode(int typeCode)
    {
        OperationInfo.OperationInfoBuilder builder = OperationInfo.builder();
        if(this.persistencePool.isSystemCriticalType(typeCode))
        {
            builder.withCategory(OperationInfo.Category.SYSTEM);
        }
        return builder.build();
    }
}
