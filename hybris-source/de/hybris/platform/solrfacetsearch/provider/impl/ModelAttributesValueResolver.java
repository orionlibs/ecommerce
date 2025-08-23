package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ExpressionEvaluator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ModelAttributesValueResolver<T extends ItemModel> extends AbstractValueResolver<T, Object, Object>
{
    public static final String OPTIONAL_PARAM = "optional";
    public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;
    public static final String ATTRIBUTE_PARAM = "attribute";
    public static final String ATTRIBUTE_PARAM_DEFAULT_VALUE = null;
    public static final String EVALUATE_EXPRESSION_PARAM = "evaluateExpression";
    public static final boolean EVALUATE_EXPRESSION_PARAM_DEFAULT_VALUE = false;
    private ModelService modelService;
    private TypeService typeService;
    private ExpressionEvaluator expressionEvaluator;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public ExpressionEvaluator getExpressionEvaluator()
    {
        return this.expressionEvaluator;
    }


    @Required
    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator)
    {
        this.expressionEvaluator = expressionEvaluator;
    }


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, T model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        boolean hasValue = false;
        String attributeName = getAttributeName(indexedProperty);
        Object attributeValue = getAttributeValue(indexedProperty, model, attributeName);
        hasValue = filterAndAddFieldValues(document, batchContext, indexedProperty, attributeValue, resolverContext
                        .getFieldQualifier());
        if(!hasValue)
        {
            boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, "optional", true);
            if(!isOptional)
            {
                throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
            }
        }
    }


    protected String getAttributeName(IndexedProperty indexedProperty)
    {
        String attributeName = ValueProviderParameterUtils.getString(indexedProperty, "attribute", ATTRIBUTE_PARAM_DEFAULT_VALUE);
        if(attributeName == null)
        {
            attributeName = indexedProperty.getName();
        }
        return attributeName;
    }


    protected Object getAttributeValue(IndexedProperty indexedProperty, T model, String attributeName) throws FieldValueProviderException
    {
        Object value = null;
        if(StringUtils.isNotEmpty(attributeName))
        {
            boolean evaluateExpression = ValueProviderParameterUtils.getBoolean(indexedProperty, "evaluateExpression", false);
            if(evaluateExpression)
            {
                value = this.expressionEvaluator.evaluate(attributeName, model);
            }
            else
            {
                ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(model.getClass());
                if(this.typeService.hasAttribute(composedType, attributeName))
                {
                    value = this.modelService.getAttributeValue(model, attributeName);
                }
            }
        }
        return value;
    }
}
