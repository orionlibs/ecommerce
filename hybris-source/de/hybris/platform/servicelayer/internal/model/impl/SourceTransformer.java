package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.directpersistence.read.SLDRelationDAO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObjectNotFoundException;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SourceTransformer
{
    private final SLDDataContainerProvider sldDataContainerProvider;
    private final SLDRelationDAO sldRelationDAO;
    private final JaloAccessorsService jaloAccesorsService;
    private final ConcurrentMap<String, Boolean> typeHierarchyBackedByJaloCache = new ConcurrentHashMap<>();
    private volatile ConverterRegistry converterRegistry;


    public SourceTransformer(SLDDataContainerProvider sldDataContainerProvider, SLDRelationDAO sldRelationDAO, JaloAccessorsService jaloAccessorsService)
    {
        Objects.requireNonNull(sldDataContainerProvider, "sldDataContainerProvider mustn't be null");
        Objects.requireNonNull(sldRelationDAO, "sldRelationDAO mustn't be null");
        Objects.requireNonNull(jaloAccessorsService, "jaloAccessorsService mustn't be null");
        this.sldDataContainerProvider = sldDataContainerProvider;
        this.sldRelationDAO = sldRelationDAO;
        this.jaloAccesorsService = jaloAccessorsService;
    }


    public PersistenceObject transformSource(Object source)
    {
        if(source == null)
        {
            return null;
        }
        PersistenceObject result = transformSourceOrReturnNull(source);
        if(result != null)
        {
            return result;
        }
        if(source instanceof PK)
        {
            return (PersistenceObject)getPersistenceObjectByPK((PK)source);
        }
        throw new UnsupportedOperationException("Unknown source type " + source);
    }


    public PersistenceObject transformSourceOrReturnNull(Object source)
    {
        if(source == null)
        {
            return null;
        }
        if(source instanceof PersistenceObject)
        {
            return (PersistenceObject)source;
        }
        if(source instanceof Item)
        {
            try
            {
                return (PersistenceObject)new JaloPersistenceObject((Item)source);
            }
            catch(JaloItemNotFoundException ex)
            {
                return null;
            }
        }
        if(source instanceof ItemPropertyValue)
        {
            try
            {
                return (PersistenceObject)getPersistenceObjectByPK(((ItemPropertyValue)source).getPK());
            }
            catch(PersistenceObjectNotFoundException ex)
            {
                return null;
            }
        }
        return null;
    }


    private ConverterRegistry getConverterRegistry()
    {
        if(this.converterRegistry == null)
        {
            this.converterRegistry = lookupConverterRegistry();
        }
        return this.converterRegistry;
    }


    protected ConverterRegistry lookupConverterRegistry()
    {
        throw new IllegalStateException("Must be overridden (Spring lookup method or manually)");
    }


    PersistenceObjectInternal getPersistenceObjectByPK(PK pk)
    {
        if(PersistenceUtils.isPersistenceLegacyModeEnabled())
        {
            return getJALOPersistenceObject(pk);
        }
        PersistenceObjectInternal sldObject = getSLDPersistenceObject(pk);
        if(testTypeHierarchyToBeBackedByJalo(sldObject.getTypeCode()))
        {
            return getJALOPersistenceObject(pk);
        }
        return (PersistenceObjectInternal)new SLDPersistenceObjectWithFallback(sldObject, this);
    }


    public final boolean mustBeBackedByJalo(PersistenceObject persistenceObject)
    {
        return mustBeBackedByJalo(persistenceObject.getTypeCode());
    }


    public final boolean mustBeBackedByJalo(String typeCode)
    {
        return (PersistenceUtils.isPersistenceLegacyModeEnabled() || testTypeHierarchyToBeBackedByJalo(typeCode));
    }


    protected boolean testTypeHierarchyToBeBackedByJalo(String typeCode)
    {
        Boolean cachedResult = this.typeHierarchyBackedByJaloCache.get(typeCode);
        if(cachedResult == null)
        {
            cachedResult = Boolean.FALSE;
            for(ComposedType type = TypeManager.getInstance().getComposedType(typeCode); type != null; type = type.getSuperType())
            {
                if(isJaloSLDSafeAndNotConfiguredForLegacyPersistence(type.getCode()))
                {
                    cachedResult = Boolean.TRUE;
                    break;
                }
            }
            this.typeHierarchyBackedByJaloCache.put(typeCode, cachedResult);
        }
        return cachedResult.booleanValue();
    }


    public void resetCache()
    {
        this.typeHierarchyBackedByJaloCache.clear();
    }


    private boolean isJaloSLDSafeAndNotConfiguredForLegacyPersistence(String typeCode)
    {
        boolean useJalo = true;
        if(this.jaloAccesorsService.isSLDSafeForRead(typeCode))
        {
            ModelConverter cnv = getConverterRegistry().getModelConverterBySourceType(typeCode);
            if(cnv instanceof ItemModelConverter)
            {
                useJalo = ((ItemModelConverter)cnv).isConfiguredForLegacyPersistence();
            }
        }
        return useJalo;
    }


    PersistenceObjectInternal getJALOPersistenceObject(PK pk)
    {
        try
        {
            Item item = JaloSession.getCurrentSession().getItem(pk);
            return (PersistenceObjectInternal)new JaloPersistenceObject(item);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new PersistenceObjectNotFoundException("Can't find item for pk " + pk, e);
        }
    }


    PersistenceObjectInternal getSLDPersistenceObject(PK pk)
    {
        SLDDataContainer container = getSLDContainerForPK(pk);
        if(container == null)
        {
            throw new PersistenceObjectNotFoundException("Can't find sld container for pk " + pk);
        }
        return (PersistenceObjectInternal)new SLDPersistenceObject(this, container, this.sldRelationDAO);
    }


    SLDDataContainer getSLDContainerForPK(PK pk)
    {
        return this.sldDataContainerProvider.get(pk);
    }


    public Item getItemFromPersistenceObject(PersistenceObject persistenceObject)
    {
        if(persistenceObject == null)
        {
            return null;
        }
        if(persistenceObject instanceof PersistenceObjectInternal)
        {
            return ((PersistenceObjectInternal)persistenceObject).getCorrespondingItem();
        }
        try
        {
            return JaloSession.getCurrentSession().getItem(persistenceObject.getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }
}
