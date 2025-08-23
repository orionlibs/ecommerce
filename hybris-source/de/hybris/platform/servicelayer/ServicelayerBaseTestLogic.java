package de.hybris.platform.servicelayer;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.AppendSpringConfiguration;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ReflectionUtils;

public class ServicelayerBaseTestLogic
{
    private static final Logger LOG = Logger.getLogger(ServicelayerBaseTestLogic.class);
    private ApplicationContext applicationContext;


    public void prepareApplicationContextAndSession(Object test) throws Exception
    {
        ApplicationContext parentContext = Registry.getApplicationContext();
        if(test.getClass().isAnnotationPresent((Class)AppendSpringConfiguration.class))
        {
            String[] springConfiguration = ((AppendSpringConfiguration)test.getClass().<AppendSpringConfiguration>getAnnotation(AppendSpringConfiguration.class)).value();
            this.applicationContext = (ApplicationContext)new ClassPathXmlApplicationContext(springConfiguration, parentContext);
        }
        else
        {
            this.applicationContext = parentContext;
        }
        autowireProperties(this.applicationContext, test);
    }


    protected void autowireProperties(ApplicationContext applicationContext, Object test)
    {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        Set<String> missing = new LinkedHashSet<>();
        ReflectionUtils.doWithFields(test.getClass(), (ReflectionUtils.FieldCallback)new Object(this, test, beanFactory, missing));
        if(!missing.isEmpty())
        {
            throw new IllegalStateException("test " + test
                            .getClass()
                            .getSimpleName() + " is not properly initialized - missing bean references " + missing);
        }
    }


    protected String getBeanName(Resource resource, Field field)
    {
        if(resource.mappedName() != null && resource.mappedName().length() > 0)
        {
            return resource.mappedName();
        }
        if(resource.name() != null && resource.name().length() > 0)
        {
            return resource.name();
        }
        return field.getName();
    }


    public ApplicationContext getApplicationContext()
    {
        if(this.applicationContext != null)
        {
            return this.applicationContext;
        }
        return Registry.getApplicationContext();
    }
}
