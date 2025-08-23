package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.provider.ExpressionEvaluator;
import org.apache.commons.lang.StringUtils;
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

public class DefaultExpressionEvaluator implements ExpressionEvaluator, ApplicationContextAware
{
    private ConfigurationService configurationService;
    private ExpressionParser parser;
    private ApplicationContext applicationContext;


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public ExpressionParser getParser()
    {
        return this.parser;
    }


    @Required
    public void setParser(ExpressionParser parser)
    {
        this.parser = parser;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public Object evaluate(String expression, Object root)
    {
        if(StringUtils.isNotEmpty(expression))
        {
            Expression parsedExpression = this.parser.parseExpression(expression);
            EvaluationContext evaluationContext = createEvaluationContext(root);
            return parsedExpression.getValue(evaluationContext);
        }
        return null;
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
}
