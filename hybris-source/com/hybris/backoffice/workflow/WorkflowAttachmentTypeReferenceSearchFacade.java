/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Search provider for supported workflow attachment type. Given search text
 * {@link SearchQueryData#getSearchQueryText()} will be used to filter types by name {@link ComposedTypeModel#getName()}
 */
public class WorkflowAttachmentTypeReferenceSearchFacade implements ReferenceEditorSearchFacade<ComposedTypeModel>
{
    private WorkflowsTypeFacade workflowsTypeFacade;


    @Override
    public Pageable<ComposedTypeModel> search(final SearchQueryData searchQueryData)
    {
        final List<ComposedTypeModel> composedTypes = getWorkflowsTypeFacade().getSupportedAttachmentTypes();
        return new PageableList<>(filterTypesByNames(composedTypes, searchQueryData.getSearchQueryText()),
                        searchQueryData.getPageSize(), WorkflowTemplateModel._TYPECODE);
    }


    protected List<ComposedTypeModel> filterTypesByNames(final List<ComposedTypeModel> types, final String searchText)
    {
        if(StringUtils.isNotEmpty(searchText))
        {
            return types.stream().filter(type -> StringUtils.containsIgnoreCase(type.getName(), searchText))
                            .collect(Collectors.toList());
        }
        return types;
    }


    public WorkflowsTypeFacade getWorkflowsTypeFacade()
    {
        return workflowsTypeFacade;
    }


    @Required
    public void setWorkflowsTypeFacade(final WorkflowsTypeFacade workflowsTypeFacade)
    {
        this.workflowsTypeFacade = workflowsTypeFacade;
    }
}
