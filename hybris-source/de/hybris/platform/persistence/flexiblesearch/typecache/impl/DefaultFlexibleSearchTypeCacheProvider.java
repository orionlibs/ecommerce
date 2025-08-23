package de.hybris.platform.persistence.flexiblesearch.typecache.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.flexiblesearch.AbstractQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.flexiblesearch.ParsedQuery;
import de.hybris.platform.persistence.flexiblesearch.typecache.CachedTypeData;
import de.hybris.platform.persistence.flexiblesearch.typecache.FlexibleSearchTypeCacheProvider;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.persistence.property.TypeInfoMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class DefaultFlexibleSearchTypeCacheProvider implements FlexibleSearchTypeCacheProvider
{
    private final PersistenceManager persistenceManager;
    private final TypeManager typeManager;
    private final C2LManager c2lManager;
    private final FlexibleSearch flexibleSearch;


    public DefaultFlexibleSearchTypeCacheProvider()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        this.persistenceManager = tenant.getPersistenceManager();
        this.c2lManager = tenant.getJaloConnection().getC2LManager();
        this.typeManager = tenant.getJaloConnection().getTypeManager();
        this.flexibleSearch = tenant.getJaloConnection().getFlexibleSearch();
    }


    public DefaultFlexibleSearchTypeCacheProvider(FlexibleSearch flexibleSearch)
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        this.persistenceManager = tenant.getPersistenceManager();
        this.c2lManager = tenant.getJaloConnection().getC2LManager();
        this.typeManager = tenant.getJaloConnection().getTypeManager();
        this.flexibleSearch = flexibleSearch;
    }


    public DefaultFlexibleSearchTypeCacheProvider(PersistenceManager persistenceManager, TypeManager typeManager, C2LManager c2lManager, FlexibleSearch flexibleSearch)
    {
        this.persistenceManager = persistenceManager;
        this.typeManager = typeManager;
        this.c2lManager = c2lManager;
        this.flexibleSearch = flexibleSearch;
    }


    public boolean isNonSearchableType(String code)
    {
        TypeInfoMap persistenceInfo = this.persistenceManager.getPersistenceInfo(code);
        return (persistenceInfo == null || persistenceInfo.isJaloOnly() || persistenceInfo.isViewType());
    }


    public Collection<String> getSearchableSubTypes(String type)
    {
        Collection<String> ret = null;
        Collection<ComposedType> subTypes = this.typeManager.getComposedType(type).getSubTypes();
        if(CollectionUtils.isNotEmpty(subTypes))
        {
            for(ComposedType ct : subTypes)
            {
                if(!(ct instanceof de.hybris.platform.jalo.type.ViewType) && !ct.isJaloOnly())
                {
                    if(ret == null)
                    {
                        ret = new ArrayList<>(subTypes.size());
                    }
                    ret.add(ct.getCode());
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public FlexibleSearchTypeCacheProvider.UnkownPropertyInfo checkForUnknownPropertyAttribute(String type, String qualifier)
    {
        try
        {
            AttributeDescriptor attributeDescriptor = this.typeManager.getComposedType(type).getAttributeDescriptorIncludingPrivate(qualifier);
            if(attributeDescriptor != null && attributeDescriptor.isProperty() && attributeDescriptor.getDatabaseColumn() == null)
            {
                return new FlexibleSearchTypeCacheProvider.UnkownPropertyInfo(attributeDescriptor.isLocalized());
            }
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        return null;
    }


    public boolean hasExternalTables(String typeCode)
    {
        Set<PK> externalTableTypes = getExternalTableTypes(typeCode);
        return (externalTableTypes != null && !externalTableTypes.isEmpty());
    }


    public Set<PK> getExternalTableTypes(String typeCode)
    {
        Set<PK> externalTableTypes = getPersistenceManager().getExternalTableTypes(
                        getPersistenceManager().getTypePK(typeCode));
        return (externalTableTypes != null) ? externalTableTypes : Collections.EMPTY_SET;
    }


    public boolean isAbstractRootTable(String typeCode)
    {
        CachedTypeData cachedTypeData = getCachedTypeData(typeCode);
        return (cachedTypeData.isAbstract() && cachedTypeData.getUnlocalizedTableName() != null && !hasExternalTables(typeCode));
    }


    public CachedTypeData getCachedTypeData(String typeCode)
    {
        try
        {
            TypeInfoMap typeInfoMap = getPersistenceManager().getPersistenceInfo(typeCode);
            return (CachedTypeData)new DefaultCachedTypeData(typeInfoMap);
        }
        catch(IllegalArgumentException e)
        {
            throw new FlexibleSearchException(e, e.getMessage(), 0);
        }
    }


    public PK getLanguagePkFromIsocode(String isoCode)
    {
        PK lpk = null;
        try
        {
            lpk = getC2LManager().getLanguageByIsoCode(isoCode).getPK();
        }
        catch(JaloItemNotFoundException e)
        {
            lpk = PK.parse(isoCode);
        }
        return lpk;
    }


    public Collection<AbstractQueryFilter> getQueryFilters(ParsedQuery query, String type, boolean includeSubtypes)
    {
        ComposedType composedType = this.typeManager.getComposedType(type);
        return getFlexibleSearch().getQueryFilters(query.getPrincipal(), composedType, !query.disablePrincipalGroupRestrictions(), true, includeSubtypes);
    }


    protected PersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        return this.flexibleSearch;
    }


    protected C2LManager getC2LManager()
    {
        return this.c2lManager;
    }
}
