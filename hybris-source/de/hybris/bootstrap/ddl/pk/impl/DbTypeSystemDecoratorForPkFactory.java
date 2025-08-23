package de.hybris.bootstrap.ddl.pk.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.AtomicType;
import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.CollectionType;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.EnumerationValue;
import de.hybris.bootstrap.ddl.dbtypesystem.MapType;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import de.hybris.bootstrap.ddl.pk.PkFactory;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.platform.core.PK;
import java.util.Map;
import java.util.Objects;

public class DbTypeSystemDecoratorForPkFactory implements PkFactory
{
    private final PkFactory target;
    private final DbTypeSystem dbTypeSystem;


    public DbTypeSystemDecoratorForPkFactory(PkFactory target, DbTypeSystem dbTypeSystem)
    {
        Objects.requireNonNull(target);
        Objects.requireNonNull(dbTypeSystem);
        this.target = target;
        this.dbTypeSystem = dbTypeSystem;
    }


    public Map<String, Long> getCurrentNumberSeries()
    {
        return this.target.getCurrentNumberSeries();
    }


    public PK createNewPK(int tc)
    {
        return this.target.createNewPK(tc);
    }


    public PK createNewPK(YComposedType type)
    {
        return this.target.createNewPK(type);
    }


    public PK getOrCreatePK(YComposedType yComposedType, YAttributeDescriptor yAttributeDescriptor)
    {
        Type type = this.dbTypeSystem.findTypeByCode(yComposedType.getCode());
        if(type != null)
        {
            Attribute attribute = type.getAttribute(yAttributeDescriptor.getQualifier());
            if(attribute != null)
            {
                return PK.fromLong(attribute.getPk());
            }
        }
        return this.target.getOrCreatePK(yComposedType, yAttributeDescriptor);
    }


    public PK getOrCreatePK(YComposedType yComposedType)
    {
        Type type = this.dbTypeSystem.findTypeByCode(yComposedType.getCode());
        if(type != null)
        {
            return PK.fromLong(type.getPK());
        }
        return this.target.getOrCreatePK(yComposedType);
    }


    public PK getOrCreatePK(YEnumValue yEnumValue)
    {
        Type type = this.dbTypeSystem.findTypeByCode(yEnumValue.getEnumTypeCode());
        if(type != null)
        {
            EnumerationValue enumerationValue = type.getEnumerationValue(yEnumValue.getCode());
            if(enumerationValue != null)
            {
                return PK.fromLong(enumerationValue.getPk());
            }
        }
        return this.target.getOrCreatePK(yEnumValue);
    }


    public PK getOrCreatePK(YAtomicType yAtomicType)
    {
        AtomicType atomicType = this.dbTypeSystem.findAtomicTypeByCode(yAtomicType.getCode());
        if(atomicType != null)
        {
            return PK.fromLong(atomicType.getPk());
        }
        return this.target.getOrCreatePK(yAtomicType);
    }


    public PK getOrCreatePK(YMapType yMapType)
    {
        MapType mapType = this.dbTypeSystem.findMapTypeByCode(yMapType.getCode());
        if(mapType != null)
        {
            return PK.fromLong(mapType.getPk());
        }
        return this.target.getOrCreatePK(yMapType);
    }


    public PK getOrCreatePK(YCollectionType yCollectionType)
    {
        CollectionType collectionType = this.dbTypeSystem.findCollectionTypeByCode(yCollectionType.getCode());
        if(collectionType != null)
        {
            return PK.fromLong(collectionType.getPk());
        }
        return this.target.getOrCreatePK(yCollectionType);
    }
}
