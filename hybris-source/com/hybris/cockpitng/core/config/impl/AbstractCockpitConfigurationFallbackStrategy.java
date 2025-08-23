/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCockpitConfigurationFallbackStrategy<CONFIG> implements
                CockpitConfigurationFallbackStrategy<CONFIG>
{
    protected static final String MANDATORY = "mandatory";
    protected static final String UNIQUE = "unique";
    protected static final String INITIAL = "initial";
    protected static final String OTHER = "other";
    private final Map<String, Map<String, Set<String>>> cache = new ConcurrentHashMap<>();
    private TypeFacade typeFacade;


    /**
     * Method returns {@link DataType} evaluated by {@link TypeFacade}.
     *
     * @param type
     *           - type code.
     * @return {@link DataType}
     * @throws TypeNotFoundException
     */
    protected DataType loadType(final String type) throws TypeNotFoundException
    {
        return getTypeFacade().load(type);
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected Set<String> getMandatoryAttributes(final String typeCode) throws TypeNotFoundException
    {
        return getAttributes(typeCode, MANDATORY);
    }


    protected Set<String> getUniqueAttributes(final String typeCode) throws TypeNotFoundException
    {
        return getAttributes(typeCode, UNIQUE);
    }


    protected Set<String> getAttributes(final String typeCode, final String... modifiers) throws TypeNotFoundException
    {
        final Set<String> result = new HashSet<>();
        final Map<String, Set<String>> attributes = populateAttributes(typeCode);
        for(final String modifier : modifiers)
        {
            final Set<String> attr = attributes.get(modifier);
            if(CollectionUtils.isNotEmpty(attr))
            {
                result.addAll(attr);
            }
        }
        return result;
    }


    /**
     * @param typeCode
     * @throws TypeNotFoundException
     */
    private Map<String, Set<String>> populateAttributes(final String typeCode) throws TypeNotFoundException
    {
        Map<String, Set<String>> result = getValueFromCache(typeCode);
        if(result == null)
        {
            result = new HashMap<>();
            final Set<String> mandatoryAttributes = new LinkedHashSet<>();
            final Set<String> uniqueAttributes = new LinkedHashSet<>();
            final Set<String> initialAttributes = new LinkedHashSet<>();
            final Set<String> otherAttributes = new LinkedHashSet<>();
            final DataType loadedType = loadType(typeCode);
            if(loadedType != null)
            {
                final Collection<DataAttribute> attributes = loadedType.getAttributes();
                for(final DataAttribute attr : attributes)
                {
                    boolean other = true;
                    if(attr.isMandatory())
                    {
                        mandatoryAttributes.add(attr.getQualifier());
                        other = false;
                    }
                    if(attr.isUnique())
                    {
                        uniqueAttributes.add(attr.getQualifier());
                        other = false;
                    }
                    if(attr.isWritableOnCreation())
                    {
                        initialAttributes.add(attr.getQualifier());
                        other = false;
                    }
                    if(other)
                    {
                        otherAttributes.add(attr.getQualifier());
                    }
                }
                result.put(MANDATORY, mandatoryAttributes);
                result.put(UNIQUE, uniqueAttributes);
                result.put(INITIAL, initialAttributes);
                result.put(OTHER, otherAttributes);
                cacheResult(typeCode, result);
            }
        }
        return result;
    }


    private void cacheResult(final String typeCode, final Map<String, Set<String>> result)
    {
        cache.put(typeCode, result);
    }


    private Map<String, Set<String>> getValueFromCache(final String typeCode)
    {
        if(typeCode != null && cache.containsKey(typeCode))
        {
            return cache.get(typeCode);
        }
        return null;
    }
}
