/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;

/**
 * Search Facade used by {@link DefaultReferenceEditor} and {@link DefaultMultiReferenceEditor}
 * @param <T> type of returned search result
 */
public interface ReferenceEditorSearchFacade<T>
{
    /**
     * Takes the search query data (type, attributes, sort, sort order, search text ) and returns a {@link Pageable}
     * object that allows paging the resulting objects.
     *
     * @param searchQueryData query necessary for search criteria
     * @return pageable collection
     */
    Pageable<T> search(SearchQueryData searchQueryData);
}
