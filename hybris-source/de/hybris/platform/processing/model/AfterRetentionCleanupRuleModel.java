package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AfterRetentionCleanupRuleModel extends AbstractRetentionRuleModel
{
    public static final String _TYPECODE = "AfterRetentionCleanupRule";
    public static final String RETIREMENTITEMTYPE = "retirementItemType";
    public static final String RETIREMENTDATEATTRIBUTE = "retirementDateAttribute";
    public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";
    public static final String ITEMFILTEREXPRESSION = "itemFilterExpression";
    public static final String RETIREMENTDATEEXPRESSION = "retirementDateExpression";


    public AfterRetentionCleanupRuleModel()
    {
    }


    public AfterRetentionCleanupRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AfterRetentionCleanupRuleModel(String _actionReference, String _code, ComposedTypeModel _retirementItemType)
    {
        setActionReference(_actionReference);
        setCode(_code);
        setRetirementItemType(_retirementItemType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AfterRetentionCleanupRuleModel(String _actionReference, String _code, ItemModel _owner, ComposedTypeModel _retirementItemType)
    {
        setActionReference(_actionReference);
        setCode(_code);
        setOwner(_owner);
        setRetirementItemType(_retirementItemType);
    }


    @Accessor(qualifier = "itemFilterExpression", type = Accessor.Type.GETTER)
    public String getItemFilterExpression()
    {
        return (String)getPersistenceContext().getPropertyValue("itemFilterExpression");
    }


    @Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.GETTER)
    public Long getRetentionTimeSeconds()
    {
        return (Long)getPersistenceContext().getPropertyValue("retentionTimeSeconds");
    }


    @Accessor(qualifier = "retirementDateAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getRetirementDateAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("retirementDateAttribute");
    }


    @Accessor(qualifier = "retirementDateExpression", type = Accessor.Type.GETTER)
    public String getRetirementDateExpression()
    {
        return (String)getPersistenceContext().getPropertyValue("retirementDateExpression");
    }


    @Accessor(qualifier = "retirementItemType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getRetirementItemType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("retirementItemType");
    }


    @Accessor(qualifier = "itemFilterExpression", type = Accessor.Type.SETTER)
    public void setItemFilterExpression(String value)
    {
        getPersistenceContext().setPropertyValue("itemFilterExpression", value);
    }


    @Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.SETTER)
    public void setRetentionTimeSeconds(Long value)
    {
        getPersistenceContext().setPropertyValue("retentionTimeSeconds", value);
    }


    @Accessor(qualifier = "retirementDateAttribute", type = Accessor.Type.SETTER)
    public void setRetirementDateAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("retirementDateAttribute", value);
    }


    @Accessor(qualifier = "retirementDateExpression", type = Accessor.Type.SETTER)
    public void setRetirementDateExpression(String value)
    {
        getPersistenceContext().setPropertyValue("retirementDateExpression", value);
    }


    @Accessor(qualifier = "retirementItemType", type = Accessor.Type.SETTER)
    public void setRetirementItemType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("retirementItemType", value);
    }
}
