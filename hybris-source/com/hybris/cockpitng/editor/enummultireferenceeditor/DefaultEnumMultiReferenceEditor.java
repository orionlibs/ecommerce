/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.enummultireferenceeditor;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.defaultenum.EnumValueFilterResolver;
import com.hybris.cockpitng.editor.defaultenum.EnumValueResolver;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * Multi-reference editor for collections of enumerations
 */
public class DefaultEnumMultiReferenceEditor<T> extends DefaultMultiReferenceEditor<T>
{
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern.compile("^EnumMultiReference-(COLLECTION|LIST|SET)\\((.*)\\)$");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMultiReferenceEditor.class);
    @Resource
    protected EnumValueResolver enumValueResolver;
    @Resource
    protected EnumValueFilterResolver enumValueFilterResolver;


    private static String getEnumValueType(final String typeCode)
    {
        return String.format("java.lang.Enum(%s)", typeCode);
    }


    @Override
    public void updateReferencesListBoxModel(final String textQuery)
    {
        final List<T> filteredValues = getFilteredEnumValues(textQuery);
        pageable = new PageableList<>(filteredValues, pageSize, getTypeCode());
    }


    /**
     * @return current enumeration's values filtered by textQuery
     */
    protected List<T> getFilteredEnumValues(final String textQuery)
    {
        final List<T> allValues;
        if(enumValueResolver != null)
        {
            final String valueType = getEnumValueType(getTypeCode());
            allValues = (List<T>)enumValueResolver.getAllValues(valueType, getEditorContext().getInitialValue());
        }
        else
        {
            final DataType dataType = getDataType();
            final T[] enumValues = (T[])dataType.getClazz().getEnumConstants();
            allValues = enumValues != null ? Lists.newArrayList(enumValues) : Collections.emptyList();
        }
        return filterEnumValues(allValues, textQuery);
    }


    protected DataType getDataType()
    {
        DataType dataType = null;
        try
        {
            dataType = getTypeFacade().load(getTypeCode());
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn(e.getLocalizedMessage(), e);
            }
            else
            {
                LOG.warn(e.getLocalizedMessage());
            }
        }
        return dataType;
    }


    /**
     * filters values by textQuery and returns a new list containing only matching elements
     *
     * @return filtered list of values
     */
    protected List<T> filterEnumValues(final List<T> values, final String textQuery)
    {
        if(enumValueFilterResolver == null)
        {
            return Collections.emptyList();
        }
        else
        {
            return enumValueFilterResolver.filterEnumValues(values, textQuery);
        }
    }


    @Override
    public boolean allowNestedObjectCreation()
    {
        return super.allowNestedObjectCreation() && !Enum.class.isAssignableFrom(getDataType().getClazz());
    }


    @Override
    public boolean isDisableDisplayingDetails()
    {
        return true;
    }


    @Override
    protected Pattern getRegexEditorPattern()
    {
        return REGEX_EDITOR_PATTERN;
    }


    @Override
    public void addSelectedObject(final T obj)
    {
        super.addSelectedObject(obj);
        final Listbox currentlySelectedList = getEditorLayout().getCurrentlySelectedList();
        final Listitem lastItem = currentlySelectedList.getItemAtIndex(currentlySelectedList.getItemCount() - 1);
        scrollIntoView(lastItem);
    }


    protected void scrollIntoView(final Component lastItem)
    {
        Clients.scrollIntoView(lastItem);
    }


    @Override
    public boolean isOnlyCreateMode()
    {
        return false;
    }


    @Override
    protected void setEditorLayout(final ReferenceEditorLayout<T> editorLayout)
    {
        super.setEditorLayout(editorLayout);
    }
}
