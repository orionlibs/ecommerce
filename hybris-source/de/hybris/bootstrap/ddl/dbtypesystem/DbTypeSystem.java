package de.hybris.bootstrap.ddl.dbtypesystem;

public interface DbTypeSystem
{
    Type findTypeByPK(long paramLong);


    Type findTypeByCode(String paramString);


    Deployment findDeploymentByFullName(String paramString);


    Deployment findDeploymentByTypeCode(int paramInt);


    Attribute findAttributeByPk(long paramLong);


    AtomicType findAtomicTypeByPk(long paramLong);


    AtomicType findAtomicTypeByCode(String paramString);


    CollectionType findCollectionTypeByPk(long paramLong);


    CollectionType findCollectionTypeByCode(String paramString);


    MapType findMapTypeByPk(long paramLong);


    MapType findMapTypeByCode(String paramString);


    Iterable<NumberSeries> getNumberSeries();


    DbTypeSystemItem findDbTypeSystemItemById(UniqueIdentifier paramUniqueIdentifier);
}
