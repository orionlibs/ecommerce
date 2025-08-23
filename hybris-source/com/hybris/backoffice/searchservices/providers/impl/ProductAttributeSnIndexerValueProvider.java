package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.util.ParameterUtils;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class ProductAttributeSnIndexerValueProvider extends AbstractProductSnIndexerValueProvider<ProductModel, ProductAttributeSnIndexerValueProvider.ProductData>
{
    public static final String ID = "productAttributeSnIndexerValueProvider";
    public static final String EXPRESSION_PARAM = "expression";
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    protected SnExpressionEvaluator snExpressionEvaluator;


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ProductModel source, ProductData data) throws SnIndexerException
    {
        try
        {
            String expression = resolveExpression(fieldWrapper);
            String productSelector = resolveProductSelector(fieldWrapper);
            Set<ProductModel> products = (Set<ProductModel>)data.getProducts().get(productSelector);
            if(CollectionUtils.isEmpty(products))
            {
                return null;
            }
            if(fieldWrapper.isLocalized())
            {
                List<Locale> locales = (List<Locale>)fieldWrapper.getQualifiers().stream().map(qualifier -> (Locale)qualifier.getAs(Locale.class)).collect(Collectors.toList());
                return this.snExpressionEvaluator.evaluate(products, expression, locales);
            }
            return this.snExpressionEvaluator.evaluate(products, expression);
        }
        catch(SnException e)
        {
            throw new SnIndexerException(e);
        }
    }


    protected ProductData loadData(SnIndexerContext indexerContext, Collection<SnIndexerFieldWrapper> fieldWrappers, ProductModel source) throws SnIndexerException
    {
        Map<String, Set<ProductModel>> products = collectProducts(fieldWrappers, source);
        ProductData data = new ProductData();
        data.setProducts(products);
        return data;
    }


    protected String resolveExpression(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "expression", fieldWrapper
                        .getField().getId());
    }


    public SnExpressionEvaluator getSnExpressionEvaluator()
    {
        return this.snExpressionEvaluator;
    }


    public void setSnExpressionEvaluator(SnExpressionEvaluator snExpressionEvaluator)
    {
        this.snExpressionEvaluator = snExpressionEvaluator;
    }
}
