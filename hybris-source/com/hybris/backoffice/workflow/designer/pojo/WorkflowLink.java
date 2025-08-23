/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import com.hybris.backoffice.workflow.designer.persistence.LinkAttributeAccessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Objects;

/**
 * Represents Workflow Link
 */
public class WorkflowLink implements WorkflowEntity
{
    private final LinkModel linkModel;
    private final boolean andConnection;


    /**
     * Creates instance based on the saved {@link LinkModel}. The {@link #isAndConnection()} property will be resolved
     * automatically from the model
     *
     * @param linkModel
     *           model for which the object will be created
     * @return created object.
     */
    public static WorkflowLink ofSavedModel(final LinkModel linkModel)
    {
        final boolean andAttribute = LinkAttributeAccessor.getAndConnectionAttribute(linkModel);
        return new WorkflowLink(linkModel, andAttribute);
    }


    /**
     * Creates instance based on the un-saved {@link LinkModel}. The {@link #isAndConnection()} property has to be passed as
     * the model is not persisted and does not have access to {@link Link#setAttribute(String, Object)}.
     *
     * @param linkModel
     *           model for which the object will be created
     * @param andConnection
     *           if the model should be treated as 'andConnection' link
     * @return created object.
     */
    public static WorkflowLink ofUnsavedModel(final LinkModel linkModel, final boolean andConnection)
    {
        return new WorkflowLink(linkModel, andConnection);
    }


    private WorkflowLink(final LinkModel linkModel, final boolean andConnection)
    {
        this.linkModel = linkModel;
        this.andConnection = andConnection;
    }


    @Override
    public LinkModel getModel()
    {
        return linkModel;
    }


    public boolean isAndConnection()
    {
        return andConnection;
    }


    /**
     * @return source of the link
     */
    public WorkflowEntity getSource()
    {
        return mapItemToWorkflowEntity(linkModel.getSource());
    }


    /**
     * @return target of the link
     */
    public WorkflowEntity getTarget()
    {
        return mapItemToWorkflowEntity(linkModel.getTarget());
    }


    private WorkflowEntity mapItemToWorkflowEntity(final ItemModel item)
    {
        if(item instanceof WorkflowActionModel)
        {
            return new WorkflowActionInstance((WorkflowActionModel)item);
        }
        if(item instanceof WorkflowActionTemplateModel)
        {
            return new WorkflowActionTemplate((WorkflowActionTemplateModel)item);
        }
        if(item instanceof WorkflowDecisionModel)
        {
            return new WorkflowDecisionInstance((WorkflowDecisionModel)item);
        }
        if(item instanceof WorkflowDecisionTemplateModel)
        {
            return new WorkflowDecisionTemplate((WorkflowDecisionTemplateModel)item);
        }
        return null;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final WorkflowLink that = (WorkflowLink)o;
        return Objects.equals(linkModel, that.linkModel) && andConnection == that.andConnection;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(linkModel, andConnection);
    }
}
