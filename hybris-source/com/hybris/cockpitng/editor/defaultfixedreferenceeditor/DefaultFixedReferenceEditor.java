/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultfixedreferenceeditor;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fixed values reference editor
 */
public class DefaultFixedReferenceEditor<T> extends DefaultReferenceEditor<T>
{
    public static final String DATA_MODEL_PROPERTY = "dataModelProperty";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final String PAGE_SIZE = "pageSize";
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern.compile("^(FixedValues)?Reference\\((.*)\\)$");
    private static final Pageable EMPTY_PAGEABLE = new PageableList<>(new ArrayList<>(), DEFAULT_PAGE_SIZE);
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFixedReferenceEditor.class);


    @Override
    protected Pattern getRegexEditorPattern()
    {
        return REGEX_EDITOR_PATTERN;
    }


    @Override
    public String readTypeCode(final String valueType)
    {
        Validate.notNull("Value type may not be null", valueType);
        final Pattern pattern = getRegexEditorPattern();
        if(pattern == null)
        {
            throw new IllegalStateException("Provided Pattern may not be null");
        }
        final Matcher matcher = pattern.matcher(valueType);
        if(matcher.matches())
        {
            final int groupCount = matcher.groupCount();
            if(groupCount < 2)
            {
                throw new IllegalStateException("Could not capture group representing type code. Group count: " + groupCount);
            }
            return matcher.group(2);
        }
        else
        {
            throw new IllegalArgumentException("Improper value type: " + valueType);
        }
    }


    @Override
    public void updateReferencesListBoxModel()
    {
        updateReferencesListBoxModel(StringUtils.EMPTY);
    }


    @Override
    public void updateReferencesListBoxModel(final String textQuery)
    {
        pageable = createPageable(textQuery);
    }


    protected Pageable createPageable(final String textQuery)
    {
        final EditorContext editorContext = getEditorContext();
        final Object modelProperty = editorContext.getParameter(DATA_MODEL_PROPERTY);
        if(modelProperty instanceof String)
        {
            final List<?> data = getModel().getValue((String)modelProperty, List.class);
            if(data == null)
            {
                LOG.warn("Given data property: '{}' was resolved to null. Was the model initialised with an instance of {}?",
                                modelProperty, List.class.getName());
                return EMPTY_PAGEABLE;
            }
            if(StringUtils.isEmpty(textQuery))
            {
                return new PageableList(data, getPageSize());
            }
            else
            {
                final String trimmedQuery = textQuery.trim();
                final List filteredData = data.stream()
                                .filter(elem -> elem != null && StringUtils.containsIgnoreCase(Objects.toString(elem), trimmedQuery))
                                .collect(Collectors.toList());
                return new PageableList(filteredData, getPageSize());
            }
        }
        else
        {
            LOG.warn("Could not find model property setting: {}", DATA_MODEL_PROPERTY);
            return EMPTY_PAGEABLE;
        }
    }


    protected int getPageSize()
    {
        final Object pageSize = getModel().getValue(PAGE_SIZE, String.class);
        try
        {
            if(pageSize instanceof String)
            {
                return Integer.parseInt((String)pageSize);
            }
        }
        catch(final NumberFormatException nfe)
        {
            LOG.warn("Could not parse page sie: " + pageSize);
        }
        return DEFAULT_PAGE_SIZE;
    }


    @Override
    public void triggerReferenceSelected(final Object selected)
    {
        // Do nothing
    }


    protected WidgetModel getModel()
    {
        return getParentEditor().getWidgetInstanceManager().getModel();
    }
}
