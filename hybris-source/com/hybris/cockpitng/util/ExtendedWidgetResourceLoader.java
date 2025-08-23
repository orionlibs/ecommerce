/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.packaging.SimpleHybrisWidgetResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Resource loader that allows to preload a merged css files from all widget that are configured in a cockpit
 * application.
 */
public class ExtendedWidgetResourceLoader extends SimpleHybrisWidgetResourceLoader
{
    private static final long serialVersionUID = 5420046195345605067L;
    private static final Logger LOG = LoggerFactory.getLogger(ExtendedWidgetResourceLoader.class);
    private static final String FIXED_DEFAULT_CSS = "default.css";
    private static final String CSS_IMPORT_PREFIX = "@import url('../..";
    private static final String CSS_IMPORT_POSTFIX = "');";
    private static final String SLASH_DELIMITER = "/";


    @Override
    protected List<String> getCssFilesAsStrings(final String mainSlotId, final HttpServletRequest request)
    {
        try
        {
            final ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            if(applicationContext != null)
            {
                final WidgetPersistenceService widgetPersistenceService = applicationContext.getBean("widgetPersistenceService",
                                WidgetPersistenceService.class);
                final Widget rootWidget = widgetPersistenceService.loadWidgetTree(mainSlotId);
                final Set<AbstractCockpitComponentDefinition> definitions = new HashSet<>(getAllDefinitions(rootWidget, applicationContext));
                definitions.addAll(getAllNonWidgetDefinitions(applicationContext));
                final List<String> cssFilesToImport = new ArrayList<>(super.getCssFilesAsStrings(mainSlotId, request));
                definitions.stream().filter(abstractCockpitComponentDefinition -> abstractCockpitComponentDefinition != null)
                                .forEach(abstractCockpitComponentDefinition -> {
                                    final String path = getCssPathForDefinition(abstractCockpitComponentDefinition);
                                    if(StringUtils.isNotEmpty(path))
                                    {
                                        cssFilesToImport.add(createCssFileImportString(path));
                                    }
                                });
                return cssFilesToImport;
            }
            else
            {
                LOG.debug("A issue occurred while trying to fetch css files for root widget {}", mainSlotId);
                return super.getCssFilesAsStrings(mainSlotId, request);
            }
        }
        catch(final Exception e)
        {
            LOG.error("An error occurred while trying to fetch css files for root widget '" + mainSlotId + "': ", e);
            return super.getCssFilesAsStrings(mainSlotId, request);
        }
    }


    /**
     * Creates CSS import path ready to add to CSS stylesheet
     *
     * @param path of the CSS stylesheet
     * @return String with CSS import statement
     * @throws IllegalArgumentException when path is null or empty
     */
    protected String createCssFileImportString(final String path)
    {
        if(StringUtils.isEmpty(path))
        {
            throw new IllegalArgumentException("CSS file path cannot be null or empty");
        }
        final StringBuilder stringBuilder = new StringBuilder(CSS_IMPORT_PREFIX);
        final String strippedPath = path.replaceFirst(WidgetLibConstants.RESOURCES_SUBFOLDER, StringUtils.EMPTY);
        final String resourcesPathPrefix = SLASH_DELIMITER + WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX;
        final boolean isFileInJarResources = strippedPath.startsWith(resourcesPathPrefix);
        if(!isFileInJarResources)
        {
            stringBuilder.append(SLASH_DELIMITER);
            stringBuilder.append(WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX);
        }
        stringBuilder.append(strippedPath);
        stringBuilder.append(CSS_IMPORT_POSTFIX);
        return stringBuilder.toString();
    }


    private Set<AbstractCockpitComponentDefinition> getAllDefinitions(final Widget rootWidget, final ApplicationContext applicationContext)
    {
        if(rootWidget == null)
        {
            return Collections.emptySet();
        }
        final CockpitComponentDefinitionService componentDefinitionService = getComponentDefinitionService(applicationContext);
        final Set<AbstractCockpitComponentDefinition> ret = new LinkedHashSet<AbstractCockpitComponentDefinition>();
        ret.addAll(getChildDefinitionsRecursively(Collections.singletonList(rootWidget), componentDefinitionService));
        return ret;
    }


    private static Set<AbstractCockpitComponentDefinition> getChildDefinitionsRecursively(final List<Widget> widgets,
                    final CockpitComponentDefinitionService componentDefinitionService)
    {
        final Set<AbstractCockpitComponentDefinition> ret = new HashSet<>();
        for(final Widget widget : widgets)
        {
            final WidgetDefinition def = componentDefinitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId(),
                            WidgetDefinition.class);
            if(def != null)
            {
                ret.add(def);
                if(def.getComposedWidgetRoot() != null)
                {
                    ret.addAll(getChildDefinitionsRecursively(Collections.singletonList(def.getComposedWidgetRoot()),
                                    componentDefinitionService));
                }
            }
            else
            {
                LOG.error("Could not find definition for widget: {} {}", widget.getId(), widget.getWidgetDefinitionId());
            }
            final List<Widget> allChildrenIncludingGroups = new ArrayList<>(widget.getChildren());
            ret.addAll(getChildDefinitionsRecursively(allChildrenIncludingGroups, componentDefinitionService));
        }
        return ret;
    }


    private Set<AbstractCockpitComponentDefinition> getAllNonWidgetDefinitions(final ApplicationContext applicationContext)
    {
        final CockpitComponentDefinitionService componentDefinitionService = getComponentDefinitionService(applicationContext);
        final Set<AbstractCockpitComponentDefinition> ret = new LinkedHashSet<AbstractCockpitComponentDefinition>();
        final List<AbstractCockpitComponentDefinition> all = componentDefinitionService.getAllComponentDefinitions();
        for(final AbstractCockpitComponentDefinition definition : all)
        {
            if(!(definition instanceof WidgetDefinition))
            {
                ret.add(definition);
            }
        }
        return ret;
    }


    public String getCssPathForDefinition(final AbstractCockpitComponentDefinition def)
    {
        String path = getAdjustedUrl(def, FIXED_DEFAULT_CSS);
        LOG.debug("Trying to find css file for widget " + def.getCode());
        boolean fileExists = false;
        // first fallback
        InputStream cssAsStream = getResourceAsStream(path);
        if(cssAsStream == null)
        {
            // look in jars
            fileExists = isFileExistingInJar(path);
            if(!fileExists)
            {
                // second fallback, try definition name
                path = getAdjustedUrl(def, getIdWithoutPackage(def) + ".css");
                cssAsStream = getResourceAsStream(path);
                fileExists = (cssAsStream != null) || isFileExistingInJar(path);
            }
        }
        else
        {
            fileExists = true;
        }
        if(fileExists)
        {
            LOG.debug("Css file found: {}", path);
            try
            {
                if(cssAsStream != null)
                {
                    cssAsStream.close();
                }
            }
            catch(final IOException e)
            {
                LOG.error("Error while trying to close stream:", e);
            }
            return path;
        }
        return null;
    }


    public static String getIdWithoutPackage(final AbstractCockpitComponentDefinition definition)
    {
        String ret = null;
        if(definition != null)
        {
            final String fullyQualified = definition.getCode();
            if(fullyQualified.contains("."))
            {
                final String[] split = fullyQualified.split("\\.");
                if(split.length > 0)
                {
                    ret = split[split.length - 1];
                }
            }
            else
            {
                ret = fullyQualified;
            }
        }
        return ret;
    }


    public boolean isFileExistingInJar(final String pathArg)
    {
        try
        {
            return hasResourceInJar(pathArg);
        }
        catch(final Exception e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }


    public String getAdjustedUrl(final AbstractCockpitComponentDefinition def, final String filename)
    {
        return WidgetLibConstants.RESOURCES_SUBFOLDER
                        + StringUtils.remove(FilenameUtils.separatorsToUnix(FilenameUtils.normalize(def.getLocationPath() + "/" + filename)),
                        "/" + WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX);
    }


    private CockpitComponentDefinitionService getComponentDefinitionService(final ApplicationContext applicationContext)
    {
        return applicationContext.getBean("componentDefinitionService",
                        CockpitComponentDefinitionService.class);
    }
}
