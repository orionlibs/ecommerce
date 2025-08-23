package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RetentionJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "RetentionJob";
    public static final String RETENTIONRULE = "retentionRule";
    public static final String BATCHSIZE = "batchSize";


    public RetentionJobModel()
    {
    }


    public RetentionJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RetentionJobModel(Integer _batchSize, String _code, AbstractRetentionRuleModel _retentionRule, String _springId)
    {
        setBatchSize(_batchSize);
        setCode(_code);
        setRetentionRule(_retentionRule);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RetentionJobModel(Integer _batchSize, String _code, Integer _nodeID, ItemModel _owner, AbstractRetentionRuleModel _retentionRule, String _springId)
    {
        setBatchSize(_batchSize);
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setRetentionRule(_retentionRule);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
    public Integer getBatchSize()
    {
        return (Integer)getPersistenceContext().getPropertyValue("batchSize");
    }


    @Accessor(qualifier = "retentionRule", type = Accessor.Type.GETTER)
    public AbstractRetentionRuleModel getRetentionRule()
    {
        return (AbstractRetentionRuleModel)getPersistenceContext().getPropertyValue("retentionRule");
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
    public void setBatchSize(Integer value)
    {
        getPersistenceContext().setPropertyValue("batchSize", value);
    }


    @Accessor(qualifier = "retentionRule", type = Accessor.Type.SETTER)
    public void setRetentionRule(AbstractRetentionRuleModel value)
    {
        getPersistenceContext().setPropertyValue("retentionRule", value);
    }
}
