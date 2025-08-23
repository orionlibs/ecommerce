/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import static java.util.Objects.nonNull;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditAvailabilityProvider;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditAvailabilityProviderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link EditAvailabilityProviderFactory}.
 *
 */
public class DefaultEditAvailabilityProviderFactory implements EditAvailabilityProviderFactory
{
    private Map<Class, EditAvailabilityProvider> editAvailabilityProviders;


    @Override
    public EditAvailabilityProvider getProvider(/*@SuppressWarnings("unused")*/ final DataAttribute attribute, final Object instance)
    {
        return getInstanceClasses(instance).stream().filter(getEditAvailabilityProviders()::containsKey)
                        .map(getEditAvailabilityProviders()::get).findFirst().orElse(getDefaultProvider());
    }


    protected EditAvailabilityProvider getDefaultProvider()
    {
        return (attribute, instance) -> nonNull(instance);
    }


    protected List<Class> getInstanceClasses(final Object obj)
    {
        final List<Class> result = new ArrayList<>();
        Class clazz = nonNull(obj) ? obj.getClass() : null;
        while(clazz != null)
        {
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return result;
    }


    protected Map<Class, EditAvailabilityProvider> getEditAvailabilityProviders()
    {
        return editAvailabilityProviders;
    }


    @Required
    public void setEditAvailabilityProviders(final Map<Class, EditAvailabilityProvider> editAvailabilityProviders)
    {
        this.editAvailabilityProviders = editAvailabilityProviders;
    }
}
