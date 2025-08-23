package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class WorkflowItemAttachmentModel extends ItemModel
{
    public static final String _TYPECODE = "WorkflowItemAttachment";
    public static final String _WORKFLOWITEMATTACHMENTRELATION = "WorkflowItemAttachmentRelation";
    public static final String _WORKFLOWACTIONITEMATTACHMENTRELATION = "WorkflowActionItemAttachmentRelation";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String ITEM = "item";
    public static final String TYPEOFITEM = "typeOfItem";
    public static final String ACTIONSTR = "actionStr";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String ACTIONS = "actions";


    public WorkflowItemAttachmentModel()
    {
    }


    public WorkflowItemAttachmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowItemAttachmentModel(ItemModel _item, WorkflowModel _workflow)
    {
        setItem(_item);
        setWorkflow(_workflow);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowItemAttachmentModel(String _code, ItemModel _item, ItemModel _owner, WorkflowModel _workflow)
    {
        setCode(_code);
        setItem(_item);
        setOwner(_owner);
        setWorkflow(_workflow);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public Collection<WorkflowActionModel> getActions()
    {
        return (Collection<WorkflowActionModel>)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "actionStr", type = Accessor.Type.GETTER)
    public String getActionStr()
    {
        return (String)getPersistenceContext().getPropertyValue("actionStr");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "item", type = Accessor.Type.GETTER)
    public ItemModel getItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("item");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "typeOfItem", type = Accessor.Type.GETTER)
    public ComposedTypeModel getTypeOfItem()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("typeOfItem");
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
    public WorkflowModel getWorkflow()
    {
        return (WorkflowModel)getPersistenceContext().getPropertyValue("workflow");
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(Collection<WorkflowActionModel> value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "item", type = Accessor.Type.SETTER)
    public void setItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("item", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
    public void setWorkflow(WorkflowModel value)
    {
        getPersistenceContext().setPropertyValue("workflow", value);
    }
}
