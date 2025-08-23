/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;

/**
 * Sample implementation of {@link ReferenceEditorSearchFacade}
 * @param <T> type of returned search result
 */
public class SampleReferenceEditorSearchFacade<T> implements ReferenceEditorSearchFacade<T>
{
    private FieldSearchFacade<T> fieldSearchFacade;


    public void setFieldSearchFacade(final FieldSearchFacade<T> fieldSearchFacade)
    {
        this.fieldSearchFacade = fieldSearchFacade;
    }


    @Override
    public Pageable<T> search(final SearchQueryData searchQueryData)
    {
        return fieldSearchFacade.search(searchQueryData);
    }
}
