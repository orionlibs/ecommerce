package de.hybris.bootstrap.ddl.pk.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.NumberSeries;
import de.hybris.bootstrap.ddl.pk.PkFactory;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.platform.core.PK;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultPkFactory implements PkFactory
{
    private static final Logger LOG = Logger.getLogger(DefaultPkFactory.class);
    private final InitializationPKCounterGenerator pkCounterGenerator;
    private final Map<String, PK> yAtomicTypePks = new HashMap<>();
    private final Map<String, PK> yComposedTypePks = new HashMap<>();
    private final Map<String, PK> yAttributeDescriptorPKs = new HashMap<>();
    private final Map<String, PK> yMapTypePKs = new HashMap<>();
    private final Map<String, PK> yCollectionTypePKs = new HashMap<>();
    private final Map<String, PK> yEnumValuePKs = new HashMap<>();


    public DefaultPkFactory(Iterable<NumberSeries> currentNumberSeries, int clusterId)
    {
        System.setProperty("counter.pk.generator.class", InitializationPKCounterGenerator.class.getName());
        this.pkCounterGenerator = new InitializationPKCounterGenerator(currentNumberSeries, clusterId);
    }


    public Map<String, Long> getCurrentNumberSeries()
    {
        return this.pkCounterGenerator.getAllCounters();
    }


    public PK createNewPK(int tc)
    {
        Preconditions.checkArgument((tc >= 0), "Negative type code. Are you trying to instantiate abstract type?");
        return PK.createFixedCounterPK(tc, this.pkCounterGenerator.fetchNextCounter(tc));
    }


    public PK createNewPK(YComposedType type)
    {
        return createNewPK(getItemTypeCode(type));
    }


    private int getItemTypeCode(YComposedType yComposedType)
    {
        Preconditions.checkNotNull(yComposedType, "yComposedType cannot be null");
        Preconditions.checkNotNull(yComposedType.getDeployment(), "deployment for yComposedType cannot be null");
        return yComposedType.getDeployment().getItemTypeCode();
    }


    public PK getOrCreatePK(YComposedType yComposedType, YAttributeDescriptor yAttributeDescriptor)
    {
        Preconditions.checkNotNull(yComposedType, "Composed type cannot be null");
        Preconditions.checkNotNull(yAttributeDescriptor, "Attribute descriptor cannot be null");
        String key = getDescriptorKey(yComposedType, yAttributeDescriptor);
        PK ret = this.yAttributeDescriptorPKs.get(key);
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Attribute " + key + " does not exists. creating it ...");
            }
            ret = createNewPK(yAttributeDescriptor.getMetaType());
            this.yAttributeDescriptorPKs.put(key, ret);
        }
        return ret;
    }


    private String getDescriptorKey(YComposedType enclosingType, YAttributeDescriptor yAttributeDescriptor)
    {
        return enclosingType.getCode() + "." + enclosingType.getCode();
    }


    public PK getOrCreatePK(YComposedType yComposedType)
    {
        Preconditions.checkNotNull(yComposedType, "Composed type cannot be null");
        Preconditions.checkNotNull(yComposedType.getCode(), "Code for composed cannot be null");
        PK ret = this.yComposedTypePks.get(yComposedType.getCode());
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Composed type " + yComposedType.getCode() + " does not exists. creating it ...");
            }
            ret = createNewPK(yComposedType.getMetaType());
            this.yComposedTypePks.put(yComposedType.getCode(), ret);
        }
        return ret;
    }


    public PK getOrCreatePK(YEnumValue yEnumValue)
    {
        Preconditions.checkNotNull(yEnumValue, "YEnumValue can not be null");
        String key = getKey(yEnumValue);
        PK ret = this.yEnumValuePKs.get(key);
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Enum " + key + " does not exists. creating it ...");
            }
            ret = createNewPK((YComposedType)yEnumValue.getEnumType());
            this.yEnumValuePKs.put(key, ret);
        }
        return ret;
    }


    protected String getKey(YEnumValue yEnumValue)
    {
        return yEnumValue.getEnumTypeCode() + "." + yEnumValue.getEnumTypeCode();
    }


    public PK getOrCreatePK(YAtomicType yAtomicType)
    {
        Preconditions.checkNotNull(yAtomicType, "YAtomicType can not be null");
        String key = yAtomicType.getCode();
        PK ret = this.yAtomicTypePks.get(key);
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Atomic type " + key + " does not exists. creating it ...");
            }
            ret = createNewPK(yAtomicType.getMetaType());
            this.yAtomicTypePks.put(key, ret);
        }
        return ret;
    }


    public PK getOrCreatePK(YMapType yMapType)
    {
        Preconditions.checkNotNull(yMapType, "YMapType can not be null");
        String key = yMapType.getCode();
        PK ret = this.yMapTypePKs.get(key);
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Map type " + key + " does not exists. creating it ...");
            }
            ret = createNewPK(yMapType.getMetaType());
            this.yMapTypePKs.put(key, ret);
        }
        return ret;
    }


    public PK getOrCreatePK(YCollectionType yCollectionType)
    {
        Preconditions.checkNotNull(yCollectionType, "CollectionType can  not be null");
        String key = yCollectionType.getCode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Looking up  PK of " + key);
        }
        PK ret = this.yCollectionTypePKs.get(key);
        if(ret == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Collection type " + key + " does not exists. creating it ...");
            }
            ret = createNewPK(yCollectionType.getMetaType());
            this.yCollectionTypePKs.put(key, ret);
        }
        return ret;
    }
}
