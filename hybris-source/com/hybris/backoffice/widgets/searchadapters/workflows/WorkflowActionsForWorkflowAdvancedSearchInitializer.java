/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.workflows;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Optional;

/**
 * Advanced search initializer for Workflow Actions. Adds a condition for given Workflow.
 */
public class WorkflowActionsForWorkflowAdvancedSearchInitializer implements AdvancedSearchInitializer
{
    @Override
    public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> node)
    {
        if(node.isPresent())
        {
            node.map(NavigationNode::getData).map(WorkflowModel.class::cast)
                            .ifPresent(workflow -> addWorkflowCondition(searchData, workflow));
        }
    }


    protected void addWorkflowCondition(final AdvancedSearchData searchData, final WorkflowModel workflow)
    {
        final FieldType fieldType = new FieldType();
        fieldType.setDisabled(Boolean.TRUE);
        fieldType.setSelected(Boolean.TRUE);
        fieldType.setName(WorkflowActionModel.WORKFLOW);
        searchData.addFilterQueryRawCondition(fieldType, ValueComparisonOperator.EQUALS, workflow);
    }
}
