/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import com.hybris.cockpitng.core.persistence.ConfigurationImportSupport;
import com.hybris.cockpitng.core.persistence.ConfigurationInterpreter;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Import;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Requirement;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.template.TemplateEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A default tooling class to support importing configuration among different files
 */
public class DefaultConfigurationImportSupport implements ConfigurationImportSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurationImportSupport.class);
    private TemplateEngine templateEngine;
    private File importRootFile = new File(".");


    @Override
    public <C> C resolveImports(final C root, final ConfigurationInterpreter<C> interpreter) throws IOException
    {
        return loadImports(getImportRootFile(), root, interpreter, new HashSet<>());
    }


    protected <C> C loadImports(final File currentPath, final C root, final ConfigurationInterpreter<C> interpreter,
                    final Set<String> resolvedResources) throws IOException
    {
        final List<Import> imports = interpreter.getImports(root);
        if(imports == null)
        {
            return root;
        }
        C result = root;
        for(final Import resourceImport : imports)
        {
            result = applyImport(resourceImport, currentPath, result, interpreter, resolvedResources);
        }
        return result;
    }


    protected <C> C applyImport(final Import resourceImport, final File currentPath, final C root,
                    final ConfigurationInterpreter<C> interpreter, final Set<String> resolvedResources) throws IOException
    {
        final File resource;
        if(resourceImport.getResource().startsWith("/"))
        {
            resource = new File(resourceImport.getResource().substring(1));
        }
        else
        {
            resource = new File(currentPath, resourceImport.getResource());
        }
        final String resourcePath = resolveResourcePath(resource);
        final Map<String, Object> values = resourceImport.getOtherAttributes().entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().getLocalPart(), Map.Entry::getValue));
        try(final InputStream inputStream = resolveResource(resourcePath, values))
        {
            if(inputStream == null)
            {
                LOG.error("Could not find configuration import '{}'", resourceImport.getResource());
                return root;
            }
            resolvedResources.add(resourcePath);
            final C importedConfig = interpreter.load(resourceImport, inputStream);
            final Set<String> undefinedParameters = getUndefinedParameters(importedConfig, interpreter, values.keySet());
            if(CollectionUtils.isNotEmpty(undefinedParameters))
            {
                LOG.error("Required parameters not provided for {} in {}: {}", resourceImport.getResource(), currentPath,
                                undefinedParameters);
                return root;
            }
            else if(!checkRequirements(resource.getParentFile(), importedConfig, interpreter, resolvedResources))
            {
                return root;
            }
            else
            {
                final C loadedImports = loadImports(resource.getParentFile(), importedConfig, interpreter, resolvedResources);
                return interpreter.merge(resourceImport, root, loadedImports);
            }
        }
    }


    protected String resolveResourcePath(final File resource) throws IOException
    {
        final Path rootPath = Paths.get(getImportRootFile().getCanonicalPath());
        final Path resourcePath = Paths.get(resource.getCanonicalPath());
        final Path relativePath = resourcePath.startsWith(rootPath)
                        ? resourcePath.subpath(rootPath.getNameCount(), resourcePath.getNameCount())
                        : resourcePath;
        return relativePath.toString();
    }


    protected <C> boolean checkRequirements(final File currentPath, final C root, final ConfigurationInterpreter<C> interpreter,
                    final Set<String> resolvedResources)
    {
        final List<Requirement> requirements = interpreter.getRequirements(root);
        final Set<String> notMet = requirements.stream().filter(requirement -> {
            try
            {
                final File file = new File(currentPath, requirement.getResource());
                final String url = resolveResourcePath(file);
                return url == null || !resolvedResources.contains(url);
            }
            catch(IOException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
                return true;
            }
        }).map(Requirement::getResource).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(notMet))
        {
            LOG.error("Could not meet all requirements in {}: {}", currentPath, notMet);
            return false;
        }
        else
        {
            return true;
        }
    }


    protected InputStream resolveResource(final String resourcePath, final Map<String, Object> resourceParameters)
                    throws IOException
    {
        if(MapUtils.isNotEmpty(resourceParameters))
        {
            return getTemplateEngine().applyTemplate(null, resourcePath, resourceParameters);
        }
        else
        {
            final InputStream inputStream = ClassLoaderUtils.getCurrentClassLoader(this.getClass())
                            .getResourceAsStream(resourcePath);
            if(inputStream == null)
            {
                throw new FileNotFoundException(resourcePath);
            }
            return inputStream;
        }
    }


    protected <C> Set<String> getUndefinedParameters(final C root, final ConfigurationInterpreter<C> interpreter,
                    final Set<String> definedValues)
    {
        return new HashSet<>(CollectionUtils.disjunction(interpreter.getRequiredParameters(root), definedValues));
    }


    protected File getImportRootFile()
    {
        return importRootFile;
    }


    public void setImportRootFile(final String importRootFile)
    {
        this.importRootFile = new File(importRootFile);
    }


    @Required
    public void setTemplateEngine(final TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }


    protected TemplateEngine getTemplateEngine()
    {
        return templateEngine;
    }
}
