package de.hybris.platform.mediaconversion.model.interceptors;

import de.hybris.platform.mediaconversion.conversion.MediaConversionStrategy;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConversionStrategyValidateInterceptor implements ValidateInterceptor, ApplicationContextAware, InitializingBean
{
    private ApplicationContext applicationContext;
    private Set<String> availableConversionStrategies;


    public void afterPropertiesSet()
    {
        Map<String, MediaConversionStrategy> allBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)
                        getApplicationContext(), MediaConversionStrategy.class);
        this.availableConversionStrategies = Collections.unmodifiableSet(new TreeSet<>(allBeans.keySet()));
    }


    public void onValidate(Object model, InterceptorContext ctx) throws MediaConversionModelValidationException
    {
        if(model instanceof ConversionMediaFormatModel && ctx.isModified(model, "conversionStrategy"))
        {
            checkConversionStrategy(((ConversionMediaFormatModel)model).getConversionStrategy());
        }
    }


    private void checkConversionStrategy(String conversionStrategy) throws MediaConversionModelValidationException
    {
        if(conversionStrategy == null)
        {
            throw new MediaConversionModelValidationException("ConversionStrategy value must not be null.", null, this);
        }
        if(!this.availableConversionStrategies.contains(conversionStrategy))
        {
            throw new MediaConversionModelValidationException("Invalid conversionStrategy value '" + conversionStrategy + "'.", this);
        }
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
