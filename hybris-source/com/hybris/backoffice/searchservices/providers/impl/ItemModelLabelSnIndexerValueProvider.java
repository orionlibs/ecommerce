package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ItemModelLabelSnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void>
{
    public static final String ID = "itemModelLabelSnIndexerValueProvider";
    public static final String EXPRESSION_PARAM = "expression";
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    private LabelServiceProxy labelServiceProxy;
    private SnExpressionEvaluator snExpressionEvaluator;


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        try
        {
            String expression = resolveExpression(fieldWrapper);
            Object object = this.snExpressionEvaluator.evaluate(source, expression);
            if(fieldWrapper.isLocalized())
            {
                Map<Locale, Object> localizedValue = new HashMap<>();
                fieldWrapper.getQualifiers().stream().forEach(qualifier -> {
                    Locale locale = (Locale)qualifier.getAs(Locale.class);
                    String label = getLabelServiceProxy().getObjectLabel(object, locale);
                    if(StringUtils.isNotBlank(label))
                    {
                        localizedValue.put(locale, label);
                    }
                });
                return localizedValue;
            }
            throw new SnIndexerException(
                            String.format("Indexed property must be localized and of type string/text to use %s while %s is not", new Object[] {ItemModelLabelSnIndexerValueProvider.class.getSimpleName(), fieldWrapper.getFieldId()}));
        }
        catch(SnException e)
        {
            throw new SnIndexerException(e);
        }
    }


    protected String resolveExpression(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "expression", "");
    }


    public SnExpressionEvaluator getSnExpressionEvaluator()
    {
        return this.snExpressionEvaluator;
    }


    public void setSnExpressionEvaluator(SnExpressionEvaluator snExpressionEvaluator)
    {
        this.snExpressionEvaluator = snExpressionEvaluator;
    }


    public LabelServiceProxy getLabelServiceProxy()
    {
        return this.labelServiceProxy;
    }


    public void setLabelServiceProxy(LabelServiceProxy labelServiceProxy)
    {
        this.labelServiceProxy = labelServiceProxy;
    }
}
