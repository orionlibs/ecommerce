/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import com.hybris.cockpitng.web.js.ChainedDependencies;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Chained dependencies that are resolved on basis of regular expression. This dependencies check whether a url to
 * dependency matches configured regular expression. If so, then configured dependencies urls are returned.
 */
public abstract class AbstractRegexpChainedDependencies implements ChainedDependencies
{
    private transient Pattern pattern;
    private String regexp;
    private List<String> dependencies;


    public String getRegexp()
    {
        return regexp;
    }


    @Required
    public void setRegexp(final String regexp)
    {
        this.regexp = regexp;
        this.pattern = Pattern.compile(regexp);
    }


    public List<String> getDependencies()
    {
        return dependencies;
    }


    @Required
    public void setDependencies(final List<String> dependencies)
    {
        this.dependencies = dependencies;
    }


    protected abstract String getDiscriminator(final File moduleJar, final ResolvedDependency dependency,
                    final Set<String> dependingWidgets);


    @Override
    public Set<ResolvedDependency> getChainedDependencies(final File moduleJar, final ResolvedDependency dependency,
                    final Set<String> dependingWidgets)
    {
        final String discriminator = getDiscriminator(moduleJar, dependency, dependingWidgets);
        if(pattern.matcher(discriminator).matches())
        {
            return getDependencies()
                            .stream()
                            .map(url -> new ResolvedDependency("text/javascript", new DependencyDefinition(url, null), new ResourcePath(
                                            ResourcePath.REMOTE_RESOURCE, url, moduleJar.getName()))).filter(dep -> {
                                dep.addDependingWidgets(dependingWidgets);
                                dep.setInjectionPoint(ResolvedDependency.POINT_HEADER);
                                return true;
                            }).collect(Collectors.toSet());
        }
        else
        {
            return Collections.emptySet();
        }
    }
}
