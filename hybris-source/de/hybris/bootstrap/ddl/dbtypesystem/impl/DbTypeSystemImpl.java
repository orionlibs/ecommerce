package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.ddl.dbtypesystem.AtomicType;
import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.CollectionType;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystemItem;
import de.hybris.bootstrap.ddl.dbtypesystem.Deployment;
import de.hybris.bootstrap.ddl.dbtypesystem.MapType;
import de.hybris.bootstrap.ddl.dbtypesystem.NumberSeries;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.jdbc.BadSqlGrammarException;

public class DbTypeSystemImpl implements DbTypeSystem
{
    private static final Logger LOG = Logger.getLogger(DbTypeSystemImpl.class);
    private final RowsProvider rowsProvider;
    private final LinkedList<DbDeployment> deployments = new LinkedList<>();
    private final LinkedList<DbType> types = new LinkedList<>();
    private final LinkedList<DbAttribute> attributes = new LinkedList<>();
    private final LinkedList<DbAtomicType> atomicTypes = new LinkedList<>();
    private final LinkedList<DbCollectionType> collectionTypes = new LinkedList<>();
    private final LinkedList<DbMapType> mapTypes = new LinkedList<>();
    private final LinkedList<DbEnumerationValue> enumerationValues = new LinkedList<>();
    private final LinkedList<NumberSeries> numberSeriesValues = new LinkedList<>();
    private final LinkedList<DbProps> propsValues = new LinkedList<>();
    private final Index<Long, DbType> pkToType = (Index<Long, DbType>)new MapBasedIndex();
    private final Index<String, DbType> codeToType = (Index<String, DbType>)new MapBasedIndex();
    private final Index<String, DbDeployment> fullNameToDeployment = (Index<String, DbDeployment>)new MapBasedIndex();
    private final Index<Integer, DbDeployment> typeCodeToDeployment = (Index<Integer, DbDeployment>)new MapBasedIndex();
    private final Index<Long, DbAttribute> pkToAttribute = (Index<Long, DbAttribute>)new MapBasedIndex();
    private final Index<Long, Set<DbAttribute>> enclosingTypePkToAttributes = (Index<Long, Set<DbAttribute>>)new MapBasedIndex();
    private final Index<Long, DbAtomicType> pkToAtomicType = (Index<Long, DbAtomicType>)new MapBasedIndex();
    private final Index<String, DbAtomicType> codeToAtomicType = (Index<String, DbAtomicType>)new MapBasedIndex();
    private final Index<Long, DbCollectionType> pkToCollectionType = (Index<Long, DbCollectionType>)new MapBasedIndex();
    private final Index<String, DbCollectionType> codeToCollectionType = (Index<String, DbCollectionType>)new MapBasedIndex();
    private final Index<Long, DbMapType> pkToMapType = (Index<Long, DbMapType>)new MapBasedIndex();
    private final Index<String, DbMapType> codeToMapType = (Index<String, DbMapType>)new MapBasedIndex();
    private final Index<Long, Set<DbEnumerationValue>> typePktoEnumValues = (Index<Long, Set<DbEnumerationValue>>)new MapBasedIndex();
    private final Index<UniqueIdentifier, DbTypeSystemItem> uidToTypeSystemItem = (Index<UniqueIdentifier, DbTypeSystemItem>)new MapBasedIndex();


    public DbTypeSystemImpl(RowsProvider rowsProvider)
    {
        this.rowsProvider = rowsProvider;
    }


    public Type findTypeByPK(long pk)
    {
        return (Type)findDbTypeByPk(pk);
    }


    public Type findTypeByCode(String code)
    {
        return (Type)findDbTypeByCode(code);
    }


    public Deployment findDeploymentByFullName(String fullName)
    {
        return (Deployment)findDbDeploymentByFullName(fullName);
    }


    public Deployment findDeploymentByTypeCode(int typeCode)
    {
        return (Deployment)this.typeCodeToDeployment.get(Integer.valueOf(typeCode));
    }


    public Attribute findAttributeByPk(long pk)
    {
        return (Attribute)findDbAttributeByPk(pk);
    }


    public AtomicType findAtomicTypeByPk(long pk)
    {
        return (AtomicType)findDbAtomicTypeByPk(pk);
    }


    public AtomicType findAtomicTypeByCode(String code)
    {
        return (AtomicType)findDbAtomicTypeByCode(code);
    }


    public CollectionType findCollectionTypeByPk(long pk)
    {
        return (CollectionType)findDbCollectionTypeByPk(pk);
    }


    public CollectionType findCollectionTypeByCode(String code)
    {
        return (CollectionType)findDbCollectionTypeByCode(code);
    }


    public MapType findMapTypeByPk(long pk)
    {
        return (MapType)findDbMapTypeByPk(pk);
    }


    public MapType findMapTypeByCode(String code)
    {
        return (MapType)findDbMapTypeByCode(code);
    }


    public Iterable<NumberSeries> getNumberSeries()
    {
        return Iterables.unmodifiableIterable(this.numberSeriesValues);
    }


    public DbTypeSystemItem findDbTypeSystemItemById(UniqueIdentifier id)
    {
        return (DbTypeSystemItem)this.uidToTypeSystemItem.get(id);
    }


    void initialize()
    {
        try
        {
            Stopwatch stopwatch = Stopwatch.createStarted();
            fetchEntities();
            rebuildIndexes();
            linkDbTypeSystemEntities();
            LOG.info("Initialization of DbTypeSystem has taken " + stopwatch);
        }
        catch(BadSqlGrammarException e)
        {
            throw new DbTypeSystemException("Make sure you are trying to update already initialized type system. Cannot read existing DbTypeSystem properly due to : " + e
                            .getMessage(), e);
        }
    }


    private void fetchEntities()
    {
        String logPattern = "%d %s have been fetched in %s";
        LOG.info("Fetching type system related entities");
        Stopwatch stopwatch = Stopwatch.createStarted();
        fetchDeployments();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.deployments.size()), "deployments", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchTypes();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.types.size()), "composedtypes", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchAttributes();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.attributes.size()), "attributess", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchAtomicTypes();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.atomicTypes.size()), "atomictypes", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchCollectionTypes();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.collectionTypes.size()), "collectiontypes", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchMapTypes();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.mapTypes.size()), "maptypes", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchEnumerationValues();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.enumerationValues.size()), "enumerationvalues", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchNumberSeries();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.numberSeriesValues.size()), "numberseries", stopwatch.stop()}));
        stopwatch.reset().start();
        fetchProps();
        LOG.info(String.format("%d %s have been fetched in %s", new Object[] {new Integer(this.propsValues.size()), "props", stopwatch.stop()}));
    }


    private void rebuildIndexes()
    {
        this.uidToTypeSystemItem.clear();
        rebuildIndexesForTypes();
        rebuildIndexesForDeployments();
        rebuildIndexesForAttributes();
        rebuildIndexesForAtomicTypes();
        rebuildIndexesForCollectionTypes();
        rebuildIndexesForMapTypes();
        rebuildIndexesForEnumerationValues();
        rebuildIndexesForNumberSeries();
        rebuildIndexesForProps();
    }


    private void linkDbTypeSystemEntities()
    {
        for(DbType type : this.types)
        {
            Long superTypePk = type.getSuperTypePk();
            if(superTypePk != null)
            {
                type.setSuperType(findDbTypeByPk(superTypePk.longValue()));
            }
            type.setDeployment(findDbDeploymentByFullName(type.getItemJndiName()));
            type.setAttributes(findDbAttributesForTypePk(type.getPK()));
            type.setEnumerationValues(findEnumerationValuesforTypePk(type.getPK()));
        }
        for(DbAttribute attribute : this.attributes)
        {
            attribute.setEnclosingType(findDbTypeByPk(attribute.getEnclosingTypePk()));
        }
    }


    private void fetchDeployments()
    {
        for(DeploymentRow row : this.rowsProvider.getDeploymentRows())
        {
            this.deployments.add(new DbDeployment(row));
        }
    }


    private void fetchTypes()
    {
        for(TypeRow row : this.rowsProvider.getTypeRows())
        {
            this.types.add(new DbType(row));
        }
    }


    private void fetchAttributes()
    {
        for(AttributeRow row : this.rowsProvider.getAttributeRows())
        {
            this.attributes.add(new DbAttribute(row));
        }
    }


    private void fetchAtomicTypes()
    {
        for(AtomicTypeRow row : this.rowsProvider.getAtomicTypeRows())
        {
            this.atomicTypes.add(new DbAtomicType(row));
        }
    }


    private void fetchCollectionTypes()
    {
        for(CollectionTypeRow row : this.rowsProvider.getCollectionTypeRow())
        {
            this.collectionTypes.add(new DbCollectionType(row));
        }
    }


    private void fetchMapTypes()
    {
        for(MapTypeRow row : this.rowsProvider.getMapTypeRows())
        {
            this.mapTypes.add(new DbMapType(row));
        }
    }


    private void fetchEnumerationValues()
    {
        for(EnumerationValueRow row : this.rowsProvider.getEnumerationValueRows())
        {
            this.enumerationValues.add(new DbEnumerationValue(row));
        }
    }


    private void fetchNumberSeries()
    {
        for(NumberSeriesRow row : this.rowsProvider.getNumberSeriesRows())
        {
            this.numberSeriesValues.add(new DbNumberSeries(row));
        }
    }


    private void fetchProps()
    {
        for(PropsRow row : this.rowsProvider.getPropsRows())
        {
            this.propsValues.add(new DbProps(row));
        }
    }


    private void rebuildIndexesForTypes()
    {
        this.pkToType.clear();
        this.codeToType.clear();
        for(DbType type : this.types)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)type);
            this.pkToType.put(new Long(type.getPK()), type);
            this.codeToType.put(type.getInternalCodeLowerCase(), type);
        }
    }


    private void rebuildIndexesForDeployments()
    {
        this.fullNameToDeployment.clear();
        for(DbDeployment deployment : this.deployments)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)deployment);
            this.fullNameToDeployment.put(deployment.getFullName(), deployment);
            if(deployment.getTypeCode() != 0)
            {
                this.typeCodeToDeployment.put(Integer.valueOf(deployment.getTypeCode()), deployment);
            }
        }
    }


    private void rebuildIndexesForAttributes()
    {
        this.pkToAttribute.clear();
        this.enclosingTypePkToAttributes.clear();
        for(DbAttribute attribute : this.attributes)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)attribute);
            this.pkToAttribute.put(new Long(attribute.getPk()), attribute);
            Long attributeEnclosingTypePk = new Long(attribute.getEnclosingTypePk());
            if(!this.enclosingTypePkToAttributes.contains(attributeEnclosingTypePk))
            {
                this.enclosingTypePkToAttributes.put(attributeEnclosingTypePk, new HashSet());
            }
            ((Set<DbAttribute>)this.enclosingTypePkToAttributes.get(attributeEnclosingTypePk)).add(attribute);
        }
    }


    private void rebuildIndexesForAtomicTypes()
    {
        this.pkToAtomicType.clear();
        this.codeToAtomicType.clear();
        for(DbAtomicType atomicType : this.atomicTypes)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)atomicType);
            Long pk = new Long(atomicType.getPk());
            String code = atomicType.getInternalCodeLowerCase();
            this.pkToAtomicType.put(pk, atomicType);
            this.codeToAtomicType.put(code, atomicType);
        }
    }


    private void rebuildIndexesForCollectionTypes()
    {
        this.pkToCollectionType.clear();
        this.codeToCollectionType.clear();
        for(DbCollectionType collectionType : this.collectionTypes)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)collectionType);
            Long pk = new Long(collectionType.getPk());
            String code = collectionType.getInternalCodeLowerCase();
            this.pkToCollectionType.put(pk, collectionType);
            this.codeToCollectionType.put(code, collectionType);
        }
    }


    private void rebuildIndexesForMapTypes()
    {
        this.pkToMapType.clear();
        this.codeToMapType.clear();
        for(DbMapType mapType : this.mapTypes)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)mapType);
            Long pk = new Long(mapType.getPk());
            String code = mapType.getInternalCodeLowerCase();
            this.pkToMapType.put(pk, mapType);
            this.codeToMapType.put(code, mapType);
        }
    }


    private void rebuildIndexesForEnumerationValues()
    {
        this.typePktoEnumValues.clear();
        for(DbEnumerationValue enumValue : this.enumerationValues)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)enumValue);
            Long typePk = new Long(enumValue.getTypePk());
            if(!this.typePktoEnumValues.contains(typePk))
            {
                this.typePktoEnumValues.put(typePk, new HashSet());
            }
            ((Set<DbEnumerationValue>)this.typePktoEnumValues.get(typePk)).add(enumValue);
        }
    }


    private void rebuildIndexesForNumberSeries()
    {
        for(NumberSeries series : this.numberSeriesValues)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)series);
        }
    }


    private void rebuildIndexesForProps()
    {
        for(DbProps props : this.propsValues)
        {
            addDbTypeSystemItemToIndex((DbTypeSystemItem)props);
        }
    }


    private void addDbTypeSystemItemToIndex(DbTypeSystemItem item)
    {
        this.uidToTypeSystemItem.put(item.getUniqueIdentifier(), item);
    }


    private DbType findDbTypeByPk(long pk)
    {
        return (DbType)this.pkToType.get(new Long(pk));
    }


    private DbType findDbTypeByCode(String code)
    {
        return (DbType)this.codeToType.get(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private DbDeployment findDbDeploymentByFullName(String fullName)
    {
        return (DbDeployment)this.fullNameToDeployment.get(fullName);
    }


    private DbAttribute findDbAttributeByPk(long pk)
    {
        return (DbAttribute)this.pkToAttribute.get(new Long(pk));
    }


    private Set<DbAttribute> findDbAttributesForTypePk(long typePk)
    {
        return (Set<DbAttribute>)this.enclosingTypePkToAttributes.get(new Long(typePk));
    }


    private DbAtomicType findDbAtomicTypeByPk(long pk)
    {
        return (DbAtomicType)this.pkToAtomicType.get(new Long(pk));
    }


    private DbAtomicType findDbAtomicTypeByCode(String code)
    {
        return (DbAtomicType)this.codeToAtomicType.get(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private DbCollectionType findDbCollectionTypeByPk(long pk)
    {
        return (DbCollectionType)this.pkToCollectionType.get(new Long(pk));
    }


    private DbCollectionType findDbCollectionTypeByCode(String code)
    {
        return (DbCollectionType)this.codeToCollectionType.get(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private DbMapType findDbMapTypeByPk(long pk)
    {
        return (DbMapType)this.pkToMapType.get(new Long(pk));
    }


    private DbMapType findDbMapTypeByCode(String code)
    {
        return (DbMapType)this.codeToMapType.get(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private Set<DbEnumerationValue> findEnumerationValuesforTypePk(long pk)
    {
        Set<DbEnumerationValue> result = (Set<DbEnumerationValue>)this.typePktoEnumValues.get(new Long(pk));
        return (result == null) ? Collections.EMPTY_SET : result;
    }
}
