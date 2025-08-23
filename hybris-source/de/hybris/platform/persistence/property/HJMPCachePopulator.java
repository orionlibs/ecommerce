package de.hybris.platform.persistence.property;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.type.AttributeDescriptorHome;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeHome;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HJMPCachePopulator
{
    protected static final String POPULATOR_ENABLED = "hjmp.bulk.load.enabled";
    private static final String ATTRIBUTE_DESCRIPTOR_FIND_BY_ENCLOSING_TYPE_NAME = "de.hybris.platform.persistence.type.AttributeDescriptor_HJMPWrapper$FindByEnclosingType1FinderResult";
    private static final String COMPOSED_TYPE_FIND_BY_CODE_NAME = "de.hybris.platform.persistence.type.ComposedType_HJMPWrapper$FindByCodeExact1FinderResult";
    private static final Logger LOG = LoggerFactory.getLogger(HJMPCachePopulator.class);
    private final HJMPLpCachePopulator hjmpLpCachePopulator = new HJMPLpCachePopulator();


    public void populateCacheHintForHJMP()
    {
        if(preconditionsAreNotFulfilled())
        {
            return;
        }
        populateHintForComposedTypeFindByCode();
        populateHintForAttributeDescriptorFindByEnclosingType();
        populateLpCache();
    }


    private void populateLpCache()
    {
        this.hjmpLpCachePopulator.populateLpCache();
    }


    boolean preconditionsAreNotFulfilled()
    {
        return (!isHJMPCachePopulatorEnabled() || !isSystemInitialized());
    }


    private boolean isSystemInitialized()
    {
        return Utilities.isSystemInitialized(Registry.getCurrentTenant()
                        .getDataSource());
    }


    private boolean isHJMPCachePopulatorEnabled()
    {
        return Registry.getCurrentTenant().getConfig().getBoolean("hjmp.bulk.load.enabled", true);
    }


    private void populateHintForComposedTypeFindByCode()
    {
        Collection<ComposedTypeRemote> composedTypes = getAndCacheComposedTypes();
        Optional<Constructor<?>> constructor = getHintComposedTypeFindByCodeExact1FinderResultConstructor();
        if(composedTypes.isEmpty() || constructor.isEmpty())
        {
            LOG.warn("Could not use hint for composed types");
            return;
        }
        Constructor<?> hintConstructor = constructor.get();
        for(ComposedTypeRemote composedType : composedTypes)
        {
            String code = composedType.getCode().toLowerCase(Locale.ROOT);
            int itemTypeCode = composedType.getItemTypeCode();
            List<PK> pkList = wrapToArray(new PK[] {composedType.getPK()});
            try
            {
                hintConstructor.newInstance(new Object[] {getPersistencePool(), getItemDeployment(itemTypeCode), code, pkList});
            }
            catch(IllegalAccessException | InstantiationException | java.lang.reflect.InvocationTargetException e)
            {
                logException(String.format("Could not use hint for composed type(code = %s)", new Object[] {code}), e);
            }
        }
    }


    private void populateHintForAttributeDescriptorFindByEnclosingType()
    {
        Collection<AttributeDescriptorRemote> attributeDescriptors = getAndCacheAttributeDescriptors();
        Optional<Constructor<?>> constructor = getHintAttributeDescriptorFindByEnclosingType1FinderResultConstructor();
        if(attributeDescriptors.isEmpty() || constructor.isEmpty())
        {
            LOG.warn("Could not use hint for attribute descriptors");
            return;
        }
        Multimap<PK, PK> attributeDescriptorMap = createAttributeDescriptorMultimap(attributeDescriptors);
        Constructor<?> hintConstructor = constructor.get();
        for(PK enclosingType : attributeDescriptorMap.keySet())
        {
            try
            {
                hintConstructor.newInstance(new Object[] {getPersistencePool(), getItemDeployment(87), enclosingType, new ArrayList(attributeDescriptorMap
                                .get(enclosingType))});
            }
            catch(IllegalAccessException | InstantiationException | java.lang.reflect.InvocationTargetException e)
            {
                logException(String.format("Could not use hint for attribute descriptor(enclosing type = %s)", new Object[] {enclosingType
                                .toString()}), e);
            }
        }
    }


    private Multimap<PK, PK> createAttributeDescriptorMultimap(Collection<AttributeDescriptorRemote> attributeDescriptors)
    {
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        for(AttributeDescriptorRemote attributeDescriptor : attributeDescriptors)
        {
            PK enclosingType = attributeDescriptor.getEnclosingType().getPK();
            arrayListMultimap.put(enclosingType, attributeDescriptor.getPK());
        }
        return (Multimap<PK, PK>)arrayListMultimap;
    }


    private List<PK> wrapToArray(PK... pks)
    {
        return new ArrayList<>(Arrays.asList(pks));
    }


    private Optional<Constructor<?>> getHintAttributeDescriptorFindByEnclosingType1FinderResultConstructor()
    {
        try
        {
            Constructor<?> declaredConstructor = Class.forName("de.hybris.platform.persistence.type.AttributeDescriptor_HJMPWrapper$FindByEnclosingType1FinderResult").getDeclaredConstructor(new Class[] {PersistencePool.class, ItemDeployment.class, PK.class, Object.class});
            declaredConstructor.setAccessible(true);
            return Optional.of(declaredConstructor);
        }
        catch(NoSuchMethodException | ClassNotFoundException | SecurityException e)
        {
            logException(String.format("Could not load %s constructor", new Object[] {"de.hybris.platform.persistence.type.AttributeDescriptor_HJMPWrapper$FindByEnclosingType1FinderResult"}), e);
            return Optional.empty();
        }
    }


    private Optional<Constructor<?>> getHintComposedTypeFindByCodeExact1FinderResultConstructor()
    {
        try
        {
            Constructor<?> declaredConstructor = Class.forName("de.hybris.platform.persistence.type.ComposedType_HJMPWrapper$FindByCodeExact1FinderResult").getDeclaredConstructor(new Class[] {PersistencePool.class, ItemDeployment.class, String.class, Object.class});
            declaredConstructor.setAccessible(true);
            return Optional.of(declaredConstructor);
        }
        catch(NoSuchMethodException | ClassNotFoundException | SecurityException e)
        {
            logException(String.format("Could not load %s constructor", new Object[] {"de.hybris.platform.persistence.type.ComposedType_HJMPWrapper$FindByCodeExact1FinderResult"}), e);
            return Optional.empty();
        }
    }


    Collection<AttributeDescriptorRemote> getAndCacheAttributeDescriptors()
    {
        try
        {
            return getAttributeDescriptorHome().findAll();
        }
        catch(YFinderException e)
        {
            logException("Could not load all attribute descriptors", (Exception)e);
            return Collections.emptyList();
        }
    }


    Collection<ComposedTypeRemote> getAndCacheComposedTypes()
    {
        try
        {
            return getComposedTypeHome().findAll();
        }
        catch(YFinderException e)
        {
            logException("Could not load all composed types", (Exception)e);
            return Collections.emptyList();
        }
    }


    private void logException(String msg, Exception ex)
    {
        LOG.warn(msg);
        LOG.debug(msg, ex);
    }


    AttributeDescriptorHome getAttributeDescriptorHome()
    {
        return (AttributeDescriptorHome)getPersistencePool().getHomeProxy(87);
    }


    ComposedTypeHome getComposedTypeHome()
    {
        return (ComposedTypeHome)getPersistencePool().getHomeProxy(82);
    }


    private ItemDeployment getItemDeployment(int code)
    {
        return Registry.getPersistenceManager().getItemDeployment(code);
    }


    private PersistencePool getPersistencePool()
    {
        return Registry.getCurrentTenant().getPersistencePool();
    }
}
