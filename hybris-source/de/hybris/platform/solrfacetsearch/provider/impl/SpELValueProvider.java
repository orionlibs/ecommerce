package de.hybris.platform.solrfacetsearch.provider.impl;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.spel.support.DataBindingMethodResolver;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELValueProvider implements FieldValueProvider, ApplicationContextAware
{
    private ConfigurationService configurationService;
    private FieldNameProvider fieldNameProvider;
    private CommonI18NService commonI18NService;
    private ExpressionParser parser;
    private ApplicationContext applicationContext;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        String exprValue = getSpringExpression(indexedProperty);
        Expression parsedExpression = this.parser.parseExpression(exprValue);
        EvaluationContext evaluationContext = createEvaluationContext(model);
        evaluationContext.setVariable("item", model);
        List<FieldValue> resolvedFieldValues = new ArrayList<>();
        if(indexedProperty.isLocalized())
        {
            for(LanguageModel language : indexConfig.getLanguages())
            {
                Locale locale = this.commonI18NService.getLocaleForLanguage(language);
                evaluationContext.setVariable("lang", locale);
                Object value = parsedExpression.getValue(evaluationContext);
                resolvedFieldValues.addAll(resolve(indexedProperty, value, language.getIsocode()));
            }
        }
        else if(indexedProperty.isCurrency())
        {
            for(CurrencyModel currency : indexConfig.getCurrencies())
            {
                CurrencyModel sessionCurrency = this.commonI18NService.getCurrentCurrency();
                try
                {
                    this.commonI18NService.setCurrentCurrency(currency);
                    evaluationContext.setVariable("currency", currency);
                    Object value = parsedExpression.getValue(evaluationContext);
                    resolvedFieldValues.addAll(resolve(indexedProperty, value, currency.getIsocode()));
                }
                finally
                {
                    this.commonI18NService.setCurrentCurrency(sessionCurrency);
                }
            }
        }
        else
        {
            Object value = parsedExpression.getValue(evaluationContext);
            resolvedFieldValues.addAll(resolve(indexedProperty, value, null));
        }
        return resolvedFieldValues;
    }


    protected EvaluationContext createEvaluationContext(Object root)
    {
        boolean fullSpELSupport = this.configurationService.getConfiguration().getBoolean("solrfacetsearch.fullSpELSupport", false);
        if(fullSpELSupport)
        {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext(root);
            evaluationContext.setBeanResolver((BeanResolver)new BeanFactoryResolver((BeanFactory)this.applicationContext));
            return (EvaluationContext)evaluationContext;
        }
        return (EvaluationContext)SimpleEvaluationContext.forReadOnlyDataBinding().withRootObject(root)
                        .withMethodResolvers(new MethodResolver[] {(MethodResolver)DataBindingMethodResolver.forInstanceMethodInvocation()}).build();
    }


    protected Collection resolve(IndexedProperty indexedProperty, Object value, String qualifier)
    {
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, qualifier);
        if(value instanceof Collection)
        {
            return resolveValuesForCollection((Collection)value, fieldNames);
        }
        if(value == null)
        {
            return Collections.emptyList();
        }
        return getFieldValuesForFieldNames(fieldNames, value);
    }


    protected Collection<FieldValue> resolveValuesForCollection(Collection value, Collection<String> fieldNames)
    {
        Collection<Collection<FieldValue>> fieldValues = Collections2.transform(value, (Function)new Object(this, fieldNames));
        return Lists.newArrayList(Iterables.concat(fieldValues));
    }


    protected Collection<FieldValue> getFieldValuesForFieldNames(Collection<String> fieldNames, Object o)
    {
        return Collections2.transform(fieldNames, (Function)new Object(this, o));
    }


    protected String getSpringExpression(IndexedProperty indexedProperty)
    {
        String exprValue = indexedProperty.getValueProviderParameter();
        if(exprValue == null)
        {
            exprValue = indexedProperty.getName();
        }
        return exprValue;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setParser(ExpressionParser parser)
    {
        this.parser = parser;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
