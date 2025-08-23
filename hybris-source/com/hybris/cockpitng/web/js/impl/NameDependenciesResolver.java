/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.web.js.DependenciesResolver;
import java.lang.reflect.Field;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Updates all required informations about dependencies on basisi of its name.
 */
public class NameDependenciesResolver implements DependenciesResolver
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NameDependenciesResolver.class);
    private transient Pattern namePattern;
    private String name;
    private String url;
    private String version;
    private String protocol;
    private String point;


    @Override
    public ResolvedDependency resolveDependency(final ResolvedDependency dependency)
    {
        if(getNamePattern().matcher(dependency.getDefinition().getName()).matches())
        {
            DependencyDefinition definition = dependency.getDefinition();
            Byte pathType = dependency.getPath().getType();
            byte injectionPoint = dependency.getInjectionPoint();
            if(!StringUtils.isEmpty(getVersion()) && StringUtils.isEmpty(definition.getVersion()))
            {
                definition = new DependencyDefinition(dependency.getDefinition().getName(), getVersion());
            }
            if(!StringUtils.isEmpty(getProtocol()) && !dependency.getPath().isTypeSet())
            {
                try
                {
                    pathType = Byte.valueOf(getProtocol());
                }
                catch(final NumberFormatException ex)
                {
                    try
                    {
                        final Field field = ResourcePath.class.getDeclaredField(getProtocol() + "_RESOURCE");
                        pathType = ReflectionUtils.getField(field, null);
                    }
                    catch(final NoSuchFieldException e)
                    {
                        LOGGER.error("Unable to resolve path type: " + getProtocol(), e);
                    }
                }
            }
            if(!StringUtils.isEmpty(getPoint()) && !dependency.isInjectionPointSet())
            {
                try
                {
                    injectionPoint = Byte.valueOf(getPoint());
                }
                catch(final NumberFormatException ex)
                {
                    try
                    {
                        final Field field = ResolvedDependency.class.getDeclaredField("POINT_" + injectionPoint);
                        injectionPoint = ReflectionUtils.getField(field, null);
                    }
                    catch(final NoSuchFieldException e)
                    {
                        LOGGER.error("Unable to resolve injection point: " + injectionPoint, e);
                    }
                }
            }
            final ResolvedDependency result = new ResolvedDependency(dependency.getType(), definition,
                            new ResourcePath(pathType, dependency.getPath().getPath() == null ? getUrl() : dependency.getPath().getPath(),
                                            dependency.getPath().getModule()));
            result.setInjectionPoint(injectionPoint);
            result.addDependingWidgets(dependency.getDependingWidgets());
            return result;
        }
        else
        {
            return dependency;
        }
    }


    protected Pattern getNamePattern()
    {
        return namePattern;
    }


    public String getName()
    {
        return name;
    }


    @Required
    public void setName(final String name)
    {
        this.name = name;
        this.namePattern = Pattern.compile(name);
    }


    public String getUrl()
    {
        return url;
    }


    public void setUrl(final String url)
    {
        this.url = url;
    }


    public String getVersion()
    {
        return version;
    }


    public void setVersion(final String version)
    {
        this.version = version;
    }


    public String getProtocol()
    {
        return protocol;
    }


    public void setProtocol(final String protocol)
    {
        this.protocol = protocol;
    }


    public String getPoint()
    {
        return point;
    }


    public void setPoint(final String point)
    {
        this.point = point;
    }
}
