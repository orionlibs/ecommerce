package de.hybris.platform.platformbackoffice.services;

import com.hybris.cockpitng.core.model.impl.FixedBeanResolver;
import de.hybris.platform.util.Config;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

public class PlatformbackofficeService implements InitializingBean, ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(PlatformbackofficeService.class.getName());
    private ApplicationContext springContext;


    public void afterPropertiesSet() throws Exception
    {
        try
        {
            patchFixedBeanResolver();
        }
        catch(Exception e)
        {
            String message = String.format("Couldn't patch 'fixedBeanResolver' due to %s.", new Object[] {e.getMessage()});
            if(LOG.isDebugEnabled())
            {
                LOG.warn(message, e);
            }
            else
            {
                LOG.warn(message);
            }
        }
    }


    private void patchFixedBeanResolver() throws IllegalAccessException
    {
        List<String> newBeans = new ArrayList<>();
        String accessibleBeanNamesForSpEL = Config.getString("platformbackoffice.available.bean.names.for.spel", "labelService");
        if(StringUtils.isNotBlank(accessibleBeanNamesForSpEL))
        {
            for(String bean : accessibleBeanNamesForSpEL.split("[, ;\\t]"))
            {
                if(StringUtils.isNotBlank(bean) && !newBeans.contains(bean))
                {
                    newBeans.add(bean);
                    LOG.debug("added bean '{}' for patching fixedBeanResolver", bean);
                }
            }
        }
        FixedBeanResolver fbr = (FixedBeanResolver)this.springContext.getBean("fixedBeanResolver", FixedBeanResolver.class);
        List<String> currentBeans = getAvailableBeanNamesViaReflection(fbr);
        if(CollectionUtils.isNotEmpty(currentBeans))
        {
            for(String bean : currentBeans)
            {
                if(!newBeans.contains(bean))
                {
                    newBeans.add(bean);
                    LOG.debug("added previously existing bean '{}' for patching fixedBeanResolver", bean);
                }
            }
        }
        fbr.setAvailableBeanNames(newBeans);
        LOG.debug("patched fixedBeanResolver - beans are now {}", getAvailableBeanNamesViaReflection(fbr));
    }


    @Deprecated(since = "6.5", forRemoval = true)
    protected List<String> getAvailableBeanNamesViaRefelction(FixedBeanResolver fbr) throws IllegalAccessException
    {
        return getAvailableBeanNamesViaReflection(fbr);
    }


    protected List<String> getAvailableBeanNamesViaReflection(FixedBeanResolver fbr) throws IllegalAccessException
    {
        Class<?> clazz = fbr.getClass();
        Field field = ReflectionUtils.findField(clazz, "availableBeanNames");
        ReflectionUtils.makeAccessible(field);
        return (List<String>)field.get(fbr);
    }


    public void setApplicationContext(ApplicationContext ctx) throws BeansException
    {
        this.springContext = ctx;
    }
}
