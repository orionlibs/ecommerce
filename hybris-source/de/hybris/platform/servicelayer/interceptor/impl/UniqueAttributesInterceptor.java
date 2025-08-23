package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.ddl.DataBaseProvider;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.servicelayer.exceptions.ModelValidationException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class UniqueAttributesInterceptor extends LocalizedMessageAwareValidator implements ValidateInterceptor
{
    public static final String DB_INDEX_ENABLED = "unique.attribute.interceptor.db.index.enabled";
    private static final Logger LOG = Logger.getLogger(UniqueAttributesInterceptor.class.getName());
    private static final String CHECKED = "UniqueAttributesInterceptor.checked";
    private static final String SEARCHABLE_MSG = "searchable";
    private static final String UNIQUE_MANY_MSG = "uniquemany";
    private static final String UNIQUE_ATLEAST_ONE_MSG = "uniqueatleatone";
    private static final String INDEX_NAME = "INDEX_NAME";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private final Map<String, Boolean> uniqueIndexCache = new ConcurrentHashMap<>();
    private TypeService typeService;
    private Tenant myTenant;


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!Boolean.TRUE.equals(ctx.getAttribute("UniqueAttributesInterceptor.checked")))
        {
            Set<UniqueItem> uniqueOnes = checkNoDuplicatesInCtx(ctx);
            checkNonSearchableAttributes(uniqueOnes);
            checkNoDuplicatesInDB(ctx, uniqueOnes);
            ctx.setAttribute("UniqueAttributesInterceptor.checked", Boolean.TRUE);
        }
    }


    private Set<UniqueItem> checkNoDuplicatesInCtx(InterceptorContext ctx) throws InterceptorException
    {
        Set<UniqueItem> uniqueOnes = new HashSet<>();
        for(UniqueItem item : getModifiedUniqueItemModels(ctx))
        {
            if(!uniqueOnes.add(item))
            {
                throw new AmbiguousUniqueKeysException(getLocalizedMessage("uniqueatleatone", new Object[] {item.uniqueKeys, item.model}), this, item);
            }
        }
        return uniqueOnes;
    }


    private void checkNonSearchableAttributes(Set<UniqueItem> uniqueOnes) throws NonSearchableAttributeException
    {
        for(UniqueItem item : uniqueOnes)
        {
            for(String attributeKey : item.uniqueKeys.keySet())
            {
                AttributeDescriptorModel attributeDescrModel = this.typeService.getAttributeDescriptor(item.type, attributeKey);
                if(!attributeDescrModel.getSearch().booleanValue())
                {
                    throw new NonSearchableAttributeException(
                                    getLocalizedMessage("searchable", new Object[] {attributeKey, item.type}), this, item.model, attributeKey);
                }
            }
        }
    }


    private void checkNoDuplicatesInDB(InterceptorContext ctx, Set<UniqueItem> uniqueOnes) throws AmbiguousUniqueKeysException
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ModelService modelService = ctx.getModelService();
        boolean cacheEnabled = uniqueDbIndexesEnabled();
        for(UniqueItem item : uniqueOnes)
        {
            if(cacheEnabled && canUseDBIndex(item))
            {
                LOG.info("Using DB index for type: " + item.type);
                continue;
            }
            GenericQuery query = createGenericQuery(item);
            if(!populateGenericQuery(item, query, modelService))
            {
                continue;
            }
            List<Item> matches = findMatchingItems(jaloSession, query);
            if(!matches.isEmpty())
            {
                if(matches.size() > 1 || item.model.getPk() == null || !item.model.getPk().equals(((Item)matches.get(0)).getPK()))
                {
                    throw new AmbiguousUniqueKeysException(
                                    getLocalizedMessage("uniquemany", new Object[] {item.uniqueKeys, item.model, Integer.valueOf(matches.size())}), this, item);
                }
            }
        }
    }


    private boolean canUseDBIndex(UniqueItem item)
    {
        ComposedType type = (ComposedType)TypeManager.getInstance().getType(item.model.getItemtype());
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(type);
        return (PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(Registry.getCurrentTenant(), typeInfo) &&
                        hasUniqueIndex(item));
    }


    private boolean uniqueDbIndexesEnabled()
    {
        return Config.getBoolean("unique.attribute.interceptor.db.index.enabled", false);
    }


    private boolean hasUniqueIndex(UniqueItem item)
    {
        return ((Boolean)this.uniqueIndexCache.computeIfAbsent(item.type, type -> {
            boolean uniqueIndexExists = computeUniqueIndexCache(item);
            LOG.info("Adding " + item.type + " " + uniqueIndexExists + " to uniqueIndexCache");
            return Boolean.valueOf(uniqueIndexExists);
        })).booleanValue();
    }


    private boolean computeUniqueIndexCache(UniqueItem item)
    {
        Set<String> columns = getUniqueColumns(item);
        if(columns.isEmpty())
        {
            return false;
        }
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(item.type);
        if(hasSubDeployments(type))
        {
            return false;
        }
        Map<String, Set<String>> foundIndexes = findDBIndexes(type);
        Optional<Map.Entry<String, Set<String>>> index = filterUniqueColumns(columns, foundIndexes);
        return !index.isEmpty();
    }


    private Optional<Map.Entry<String, Set<String>>> filterUniqueColumns(Set<String> columns, Map<String, Set<String>> foundIndexes)
    {
        return foundIndexes.entrySet()
                        .stream()
                        .filter(e -> ((Set)e.getValue()).equals(columns))
                        .findAny();
    }


    private Set<String> getUniqueColumns(UniqueItem item)
    {
        Set<String> columns = new HashSet<>();
        for(String key : item.uniqueKeys.keySet())
        {
            AttributeDescriptorModel attributeDescriptorModel = this.typeService.getAttributeDescriptor(item.type, key);
            if(attributeDescriptorModel == null)
            {
                return Collections.emptySet();
            }
            String column = attributeDescriptorModel.getDatabaseColumn();
            if(column == null)
            {
                return Collections.emptySet();
            }
            columns.add(column.toLowerCase(Locale.ROOT));
        }
        return columns;
    }


    private Map<String, Set<String>> findDBIndexes(ComposedTypeModel item)
    {
        String table = item.getTable();
        if(item.getTable() == null)
        {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> foundIndexes = new HashMap<>();
        try
        {
            Connection connection = getConnection();
            try
            {
                String tableName = getTableName(table);
                ResultSet infos = connection.getMetaData().getIndexInfo(null, null, tableName, true, false);
                try
                {
                    while(infos.next())
                    {
                        String indexName = infos.getString("INDEX_NAME");
                        String columnName = infos.getString("COLUMN_NAME");
                        if(indexName == null || columnName == null)
                        {
                            LOG.warn("Failed to get index data, index name: " + indexName + ", column name: " + columnName + ", item: " + item);
                            continue;
                        }
                        Set<String> columns = foundIndexes.computeIfAbsent(indexName, name -> new HashSet());
                        columns.add(columnName.toLowerCase(Locale.ROOT));
                    }
                    if(infos != null)
                    {
                        infos.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(infos != null)
                    {
                        try
                        {
                            infos.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
                if(connection != null)
                {
                    connection.close();
                }
            }
            catch(Throwable throwable)
            {
                if(connection != null)
                {
                    try
                    {
                        connection.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            LOG.error(e);
        }
        return foundIndexes;
    }


    private String getTableName(String table)
    {
        String tableName = table.toUpperCase(Locale.ROOT);
        if(Registry.getCurrentTenant().getMasterDataSource().getDatabaseName().equals(DataBaseProvider.MYSQL.getDbName()))
        {
            tableName = table;
        }
        return tableName;
    }


    private Connection getConnection() throws SQLException
    {
        return Registry.getCurrentTenantNoFallback().getDataSource().getConnection();
    }


    private boolean hasSubDeployments(ComposedTypeModel type)
    {
        if(type.getAbstract().booleanValue())
        {
            return true;
        }
        String table = type.getTable();
        for(ComposedTypeModel subType : type.getAllSubTypes())
        {
            if(checkSubType(table, subType))
            {
                return true;
            }
        }
        return false;
    }


    private boolean checkSubType(String table, ComposedTypeModel type)
    {
        if(!type.getTable().equals(table))
        {
            return true;
        }
        for(ComposedTypeModel subType : type.getAllSubTypes())
        {
            if(checkSubType(table, subType))
            {
                return true;
            }
        }
        return false;
    }


    private List<Item> findMatchingItems(JaloSession jaloSession, GenericQuery query)
    {
        List<Item> matches;
        SessionContext jCtx = null;
        try
        {
            jCtx = jaloSession.createLocalSessionContext();
            jCtx.setAttribute("disableRestrictions", Boolean.TRUE);
            jCtx.setAttribute("disableCache", Boolean.TRUE);
            matches = jaloSession.search(query).getResult();
        }
        finally
        {
            if(jCtx != null)
            {
                jaloSession.removeLocalSessionContext();
            }
        }
        return matches;
    }


    private boolean populateGenericQuery(UniqueItem item, GenericQuery query, ModelService modelService)
    {
        for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)item.uniqueKeys.entrySet())
        {
            if(e.getValue() == null)
            {
                query.addCondition(GenericCondition.createIsNullCondition(new GenericSearchField(e.getKey())));
                continue;
            }
            if("itemtype".equalsIgnoreCase(e.getKey()))
            {
                query.addCondition(GenericCondition.equals(e.getKey(), modelService.getSource(
                                getTypeService().getComposedTypeForCode((String)e.getValue()))));
                continue;
            }
            try
            {
                query.addCondition(
                                GenericCondition.equals(e.getKey(), modelService.toPersistenceLayer(e.getValue())));
            }
            catch(IllegalStateException ise)
            {
                return false;
            }
        }
        return true;
    }


    protected GenericQuery createGenericQuery(UniqueItem item)
    {
        if("Link".equals(item.type))
        {
            return new GenericQuery(item.model.getItemtype());
        }
        return new GenericQuery(item.type);
    }


    protected Collection<UniqueItem> getModifiedUniqueItemModels(InterceptorContext ctx) throws InterceptorException
    {
        Collection<UniqueItem> ret = null;
        ModelService modelService = ctx.getModelService();
        TypeService typeService = getTypeService();
        for(Object model : ctx.getAllRegisteredElements())
        {
            String type = typeService.getUniqueModelRootType(modelService.getModelType(model));
            if(type == null || model instanceof de.hybris.platform.core.HybrisEnumValue)
            {
                continue;
            }
            Set<String> uniqueKeys = typeService.getUniqueAttributes(type);
            boolean modified = false;
            for(String k : uniqueKeys)
            {
                if(ctx.isModified(model, k))
                {
                    modified = true;
                    break;
                }
            }
            if(modified)
            {
                ItemModel item = (ItemModel)model;
                Map<String, Object> uniqueKeyValues = new HashMap<>(uniqueKeys.size());
                for(String k : uniqueKeys)
                {
                    try
                    {
                        uniqueKeyValues.put(k, modelService.getAttributeValue(model, k));
                    }
                    catch(Exception e)
                    {
                        throw new ModelValidationException("cannot read unique key attribute " + k + " due to " + e.getMessage());
                    }
                }
                if(ret == null)
                {
                    ret = new ArrayList<>();
                }
                ret.add(new UniqueItem(item, type, uniqueKeyValues));
            }
        }
        return (ret == null) ? Collections.<UniqueItem>emptySet() : ret;
    }


    protected void registerTenantListener()
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    @PostConstruct
    public void init()
    {
        this.myTenant = Registry.getCurrentTenant();
        registerTenantListener();
    }
}
