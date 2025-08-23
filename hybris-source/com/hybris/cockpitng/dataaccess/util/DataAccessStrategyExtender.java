/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.util;

import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.impl.ObjectFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.permissions.impl.PermissionFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.permissions.impl.PermissionManagementFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.search.impl.FieldSearchFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.impl.TypeFacadeStrategyRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Allows for extending cockpit type-, object-, permission- and searchfacade functionality by adding additional
 * strategies. Should
 * be used as a spring bean to add additional properties to existing strategy registries without creating a
 * new registry bean. It adds the specified properties to the injected strategy registries after
 * the application context was initialized. It removes these properties before the application context is destroyed.
 */
public class DataAccessStrategyExtender
{
    private ObjectFacadeStrategyRegistry objectFacadeStrategyRegistry;
    private TypeFacadeStrategyRegistry typeFacadeStrategyRegistry;
    private FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry;
    private PermissionFacadeStrategyRegistry permissionFacadeStrategyRegistry;
    private PermissionManagementFacadeStrategyRegistry permissionManagementFacadeStrategyRegistry;
    private List<ObjectFacadeStrategy> originalObjectFacadeStrategies;
    private List<TypeFacadeStrategy> originalTypeFacadeStrategies;
    private List<FieldSearchFacadeStrategy<?>> originalFieldSearchFacadeStrategies;
    private List<PermissionFacadeStrategy> originalPermissionFacadeStrategies;
    private List<PermissionManagementFacadeStrategy> originalPermissionManagementFacadeStrategies;
    private List<ObjectFacadeStrategy> objectFacadeStrategies;
    private List<TypeFacadeStrategy> typeFacadeStrategies;
    private List<FieldSearchFacadeStrategy<?>> fieldSearchFacadeStrategies;
    private List<PermissionFacadeStrategy> permissionFacadeStrategies;
    private List<PermissionManagementFacadeStrategy> permissionManagementFacadeStrategies;
    private boolean addAfter;


    @PostConstruct
    public void addAll()
    {
        if(objectFacadeStrategyRegistry != null)
        {
            originalObjectFacadeStrategies = objectFacadeStrategyRegistry.getStrategies().orElse(Collections.emptyList());
            addStrategies(objectFacadeStrategyRegistry, originalObjectFacadeStrategies, objectFacadeStrategies);
        }
        if(typeFacadeStrategyRegistry != null)
        {
            originalTypeFacadeStrategies = typeFacadeStrategyRegistry.getStrategies().orElse(Collections.emptyList());
            addStrategies(typeFacadeStrategyRegistry, originalTypeFacadeStrategies, typeFacadeStrategies);
        }
        if(permissionFacadeStrategyRegistry != null)
        {
            originalPermissionFacadeStrategies = permissionFacadeStrategyRegistry.getStrategies().orElse(Collections.emptyList());
            addStrategies(permissionFacadeStrategyRegistry, originalPermissionFacadeStrategies, permissionFacadeStrategies);
        }
        if(fieldSearchFacadeStrategyRegistry != null)
        {
            originalFieldSearchFacadeStrategies = fieldSearchFacadeStrategyRegistry.getStrategies().orElse(Collections.emptyList());
            addStrategies(fieldSearchFacadeStrategyRegistry, originalFieldSearchFacadeStrategies, fieldSearchFacadeStrategies);
        }
        if(permissionManagementFacadeStrategyRegistry != null)
        {
            originalPermissionManagementFacadeStrategies = permissionManagementFacadeStrategyRegistry.getStrategies().orElse(
                            Collections.emptyList());
            addStrategies(permissionManagementFacadeStrategyRegistry, originalPermissionManagementFacadeStrategies,
                            permissionManagementFacadeStrategies);
        }
    }


    private <T> void addStrategies(final AbstractStrategyRegistry<T, ?> registry, final List<T> originalStrategies,
                    final List<T> strategies)
    {
        final List<T> newStrategyList = new ArrayList<T>();
        if(addAfter)
        {
            newStrategyList.addAll(originalStrategies);
            newStrategyList.addAll(strategies);
        }
        else
        {
            newStrategyList.addAll(strategies);
            newStrategyList.addAll(originalStrategies);
        }
        registry.setStrategies(newStrategyList);
    }


    @PreDestroy
    public void removeAll()
    {
        if(objectFacadeStrategyRegistry != null)
        {
            objectFacadeStrategyRegistry.setStrategies(originalObjectFacadeStrategies);
        }
        if(typeFacadeStrategyRegistry != null)
        {
            typeFacadeStrategyRegistry.setStrategies(originalTypeFacadeStrategies);
        }
        if(permissionFacadeStrategyRegistry != null)
        {
            permissionFacadeStrategyRegistry.setStrategies(originalPermissionFacadeStrategies);
        }
        if(fieldSearchFacadeStrategyRegistry != null)
        {
            fieldSearchFacadeStrategyRegistry.setStrategies(originalFieldSearchFacadeStrategies);
        }
        if(permissionManagementFacadeStrategyRegistry != null)
        {
            permissionManagementFacadeStrategyRegistry.setStrategies(originalPermissionManagementFacadeStrategies);
        }
    }


    /**
     * Sets the {@link ObjectFacadeStrategyRegistry} to which the {@link ObjectFacadeStrategy}s should be added.
     */
    public void setObjectFacadeStrategyRegistry(final ObjectFacadeStrategyRegistry objectFacadeStrategyRegistry)
    {
        this.objectFacadeStrategyRegistry = objectFacadeStrategyRegistry;
    }


    /**
     * Sets the {@link TypeFacadeStrategyRegistry} to which the {@link TypeFacadeStrategy}s should be added.
     */
    public void setTypeFacadeStrategyRegistry(final TypeFacadeStrategyRegistry typeFacadeStrategyRegistry)
    {
        this.typeFacadeStrategyRegistry = typeFacadeStrategyRegistry;
    }


    /**
     * Sets the {@link PermissionFacadeStrategyRegistry} to which the {@link PermissionFacadeStrategy}s should be added.
     */
    public void setPermissionFacadeStrategyRegistry(final PermissionFacadeStrategyRegistry permissionFacadeStrategyRegistry)
    {
        this.permissionFacadeStrategyRegistry = permissionFacadeStrategyRegistry;
    }


    /**
     * Sets the {@link FieldSearchFacadeStrategyRegistry} to which the {@link FieldSearchFacadeStrategy}s should be
     * added.
     */
    public void setFieldSearchFacadeStrategyRegistry(final FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry)
    {
        this.fieldSearchFacadeStrategyRegistry = fieldSearchFacadeStrategyRegistry;
    }


    public void setPermissionManagementFacadeStrategyRegistry(
                    final PermissionManagementFacadeStrategyRegistry permissionManagementFacadeStrategyRegistry)
    {
        this.permissionManagementFacadeStrategyRegistry = permissionManagementFacadeStrategyRegistry;
    }


    /**
     * Sets the {@link FieldSearchFacadeStrategy}s that should be added to the registry.
     */
    public void setFieldSearchFacadeStrategies(final List<FieldSearchFacadeStrategy<?>> fieldSearchFacadeStrategies)
    {
        this.fieldSearchFacadeStrategies = fieldSearchFacadeStrategies;
    }


    /**
     * Sets the {@link PermissionFacadeStrategy}s that should be added to the registry.
     */
    public void setPermissionFacadeStrategies(final List<PermissionFacadeStrategy> permissionFacadeStrategies)
    {
        this.permissionFacadeStrategies = permissionFacadeStrategies;
    }


    /**
     * Sets the {@link ObjectFacadeStrategy}s that should be added to the registry.
     */
    public void setObjectFacadeStrategies(final List<ObjectFacadeStrategy> objectFacadeStrategies)
    {
        this.objectFacadeStrategies = objectFacadeStrategies;
    }


    /**
     * Sets the {@link TypeFacadeStrategy}s that should be added to the registry.
     */
    public void setTypeFacadeStrategies(final List<TypeFacadeStrategy> typeFacadeStrategies)
    {
        this.typeFacadeStrategies = typeFacadeStrategies;
    }


    public void setPermissionManagementFacadeStrategies(
                    final List<PermissionManagementFacadeStrategy> permissionManagementFacadeStrategies)
    {
        this.permissionManagementFacadeStrategies = permissionManagementFacadeStrategies;
    }


    /**
     * If true, the strategies set at this bean will be added after the original list of strategies.
     * If false (default), strategies will be added before.
     */
    public void setAddAfter(final boolean addAfter)
    {
        this.addAfter = addAfter;
    }
}
