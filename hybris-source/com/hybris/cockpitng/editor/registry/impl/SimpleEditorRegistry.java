/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.registry.impl;

import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.editors.EditorDefinition;
import com.hybris.cockpitng.editors.EditorRegistry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEditorRegistry implements EditorRegistry, Resettable
{
    protected static final String NO_EDITOR_AVAILABLE = "";
    private static final Logger LOG = LoggerFactory.getLogger(SimpleEditorRegistry.class);
    protected final Map<String, String> typeToEditorCache = new ConcurrentHashMap<>();
    private final Map<String, String> defaultEditorMapping = new LinkedHashMap<>();
    private CockpitComponentDefinitionService componentDefinitionService;
    private String defaultEditorCode;


    @Override
    public EditorDefinition getEditorForType(final String editorType)
    {
        return getEditorForType(editorType, true);
    }


    protected EditorDefinition getEditorForType(final String editorType, final boolean scanSuperclasses)
    {
        if(editorType != null)
        {
            final String cachedEditorCode = typeToEditorCache.get(editorType);
            if(cachedEditorCode != null)
            {
                LOG.debug("Get editor for {} from cache: {}", editorType, cachedEditorCode);
                if(NO_EDITOR_AVAILABLE.equals(cachedEditorCode))
                {
                    return null;
                }
                else
                {
                    return getEditorForCode(cachedEditorCode);
                }
            }
        }
        EditorDefinition editor = null;
        // retrieve default Editor code (example: 'com.hybris.cockpitng.editor.defaulttext')
        // for given editorType (example: 'java.lang.String')
        final String defaultOne = getDefaultEditorCode(editorType);
        if(StringUtils.isNotBlank(defaultOne))
        {
            // retrieve the EditorDefinition for this code.
            final EditorDefinition definition = getComponentDefinitionService().getComponentDefinitionForCode(defaultOne,
                            EditorDefinition.class);
            if(definition == null)
            {
                LOG.warn("No editor definition found for definition code [{}]], type lookup", defaultOne);
            }
            else
            {
                // This editor definition is chosen
                editor = definition;
            }
        }
        final List<EditorDefinition> allDefinitions = getComponentDefinitionService()
                        .getComponentDefinitionsByClass(EditorDefinition.class);
        final List<EditorDefinition> candidates = new LinkedList<>();
        for(final EditorDefinition definition : allDefinitions)
        {
            // add REGEX handling for editorType
            if(definition.getType().equalsIgnoreCase(editorType) || editorType != null && editorType.matches(definition.getType()))
            {
                candidates.add(definition);
            }
        }
        // else if no default editorDefinition has been found, get the first candidate
        if(editor == null && (!candidates.isEmpty()))
        {
            editor = candidates.iterator().next();
        }
        // if no editor found, try to resolve class and get editor for super type
        if(editor == null && scanSuperclasses && editorType != null)
        {
            try
            {
                final Class<?> clazz = Class.forName(editorType, true, getClassLoader());
                final List<Class<?>> superClassesAndInterfaces = new ArrayList<>();
                superClassesAndInterfaces.addAll(ClassUtils.getAllSuperclasses(clazz));
                superClassesAndInterfaces.addAll(ClassUtils.getAllInterfaces(clazz));
                for(final Class<?> clsOrInterface : superClassesAndInterfaces)
                {
                    final EditorDefinition editorForType = getEditorForType(clsOrInterface.getName(), false);
                    if(editorForType != null)
                    {
                        editor = editorForType;
                        break;
                    }
                }
            }
            catch(final ClassNotFoundException e)
            {
                // OKAY
                LOG.debug(e.getMessage(), e);
            }
        }
        // if still no definition found, log error and return null
        if(editor == null)
        {
            if(scanSuperclasses)
            {
                LOG.debug("no editor found for editor type [{}]", editorType);
                // Add info to the cache that there is no editor available
                if(editorType != null)
                {
                    typeToEditorCache.put(editorType, NO_EDITOR_AVAILABLE);
                }
            }
        }
        else
        {
            if(editorType != null)
            {
                typeToEditorCache.put(editorType, editor.getCode());
            }
        }
        return editor;
    }


    protected ClassLoader getClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }


    @Override
    public EditorDefinition getEditorForCode(final String editorDefinitionCode)
    {
        final int indexOfParenthesis = editorDefinitionCode.indexOf('(');
        if(indexOfParenthesis != -1)
        {
            return getComponentDefinitionService()
                            .getComponentDefinitionForCode(editorDefinitionCode.substring(0, indexOfParenthesis), EditorDefinition.class);
        }
        return getComponentDefinitionService().getComponentDefinitionForCode(editorDefinitionCode, EditorDefinition.class);
    }


    private CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return componentDefinitionService;
    }


    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }


    /**
     * Return the default editor code (example: 'com.hybris.cockpitng.editor.default.text') for the given editorType
     * (example: 'TEXT')
     *
     * @return the default editor code for given editorType
     */
    public String getDefaultEditorCode(final String editorType)
    {
        String ret = defaultEditorMapping.get(editorType);
        if(editorType != null && ret == null)
        {
            for(final Entry<String, String> entry : defaultEditorMapping.entrySet())
            {
                if(StringUtils.lowerCase(editorType).matches(entry.getKey().toLowerCase()))
                {
                    ret = entry.getValue();
                }
            }
        }
        return ret;
    }


    @Override
    public EditorDefinition getFallbackEditor()
    {
        if(StringUtils.isNotBlank(getDefaultEditorCode()))
        {
            return getEditorForCode(getDefaultEditorCode());
        }
        return null;
    }


    public void setDefaultEditorMapping(final Map<String, String> defaultEditorMapping)
    {
        this.defaultEditorMapping.keySet().retainAll(defaultEditorMapping.keySet());
        this.defaultEditorMapping.putAll(defaultEditorMapping);
    }


    @Override
    public void reset()
    {
        typeToEditorCache.clear();
    }


    public String getDefaultEditorCode()
    {
        return defaultEditorCode;
    }


    public void setDefaultEditorCode(final String defaultEditorCode)
    {
        this.defaultEditorCode = defaultEditorCode;
    }


    public Map<String, String> getDefaultEditorMapping()
    {
        return defaultEditorMapping;
    }
}
