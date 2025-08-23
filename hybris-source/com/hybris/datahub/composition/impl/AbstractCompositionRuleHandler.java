package com.hybris.datahub.composition.impl;

import com.google.common.base.Optional;
import com.hybris.datahub.domain.CanonicalAttributeDefinition;
import com.hybris.datahub.domain.CompositionStatusType;
import com.hybris.datahub.model.BaseDataItem;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.CanonicalItemCompositionStatusDetail;
import com.hybris.datahub.model.DataItemAttribute;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.service.DataItemService;
import com.hybris.datahub.service.spel.TransformationExpressionException;
import com.hybris.datahub.service.spel.TransformationExpressionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCompositionRuleHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCompositionRuleHandler.class);
    protected TransformationExpressionProcessor transformationExpressionProcessor;
    private DataItemService dataItemService;
    private int order;


    public int getOrder()
    {
        return this.order;
    }


    @Required
    public void setOrder(int order)
    {
        this.order = order;
    }


    protected Optional<DataItemAttribute> getCanonicalDataItemAttribute(CanonicalAttributeDefinition attrDesc)
    {
        return CanonicalItem.getAttributeDefinition(attrDesc
                        .getCanonicalAttributeModelDefinition().getCanonicalItemMetadata().getItemType(), attrDesc
                        .getCanonicalAttributeModelDefinition().getAttributeName());
    }


    protected Object transformExpression(CanonicalAttributeDefinition attributeDefinition, CanonicalItem canonicalItem, RawItem rawItem)
    {
        try
        {
            return this.transformationExpressionProcessor.transform((BaseDataItem)rawItem, attributeDefinition.getReferenceAttribute());
        }
        catch(TransformationExpressionException e)
        {
            LOGGER.error("Error while evaluating reference or transformation " + attributeDefinition.getReferenceAttribute(), (Throwable)e);
            canonicalItem.setStatus(CompositionStatusType.ERROR);
            canonicalItem.setCompositionStatusDetail(CanonicalItemCompositionStatusDetail.FAILED_TRANSFORMATION.name());
            return null;
        }
    }


    @Required
    public void setDataItemService(DataItemService dataItemService)
    {
        this.dataItemService = dataItemService;
    }


    @Required
    public void setTransformationExpressionProcessor(TransformationExpressionProcessor transformationExpressionProcessor)
    {
        this.transformationExpressionProcessor = transformationExpressionProcessor;
    }
}
