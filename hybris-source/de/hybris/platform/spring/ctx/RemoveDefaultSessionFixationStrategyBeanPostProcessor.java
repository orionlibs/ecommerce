package de.hybris.platform.spring.ctx;

import de.hybris.platform.servicelayer.security.spring.HybrisSessionFixationProtectionStrategy;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.util.ReflectionUtils;

class RemoveDefaultSessionFixationStrategyBeanPostProcessor implements BeanPostProcessor
{
    private static final Log LOG = LogFactory.getLog(RemoveDefaultSessionFixationStrategyBeanPostProcessor.class.getName());


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if(bean instanceof CompositeSessionAuthenticationStrategy)
        {
            CompositeSessionAuthenticationStrategy compositeStrategy = (CompositeSessionAuthenticationStrategy)bean;
            try
            {
                List<SessionAuthenticationStrategy> strategies = getStrategiesFrom(compositeStrategy);
                Iterator<SessionAuthenticationStrategy> iterator = strategies.iterator();
                boolean hasHybrisSessionFixationStrategy = false;
                boolean removedDefaultSessionFixationStrategy = false;
                while(iterator.hasNext())
                {
                    SessionAuthenticationStrategy current = iterator.next();
                    if(current.getClass().equals(SessionFixationProtectionStrategy.class))
                    {
                        iterator.remove();
                        removedDefaultSessionFixationStrategy = true;
                    }
                    else if(current.getClass().equals(ChangeSessionIdAuthenticationStrategy.class))
                    {
                        iterator.remove();
                        removedDefaultSessionFixationStrategy = true;
                    }
                    if(current instanceof HybrisSessionFixationProtectionStrategy)
                    {
                        hasHybrisSessionFixationStrategy = true;
                    }
                }
                if(!hasHybrisSessionFixationStrategy)
                {
                    addHybrisStrategyTo(strategies);
                    if(removedDefaultSessionFixationStrategy)
                    {
                        LOG.info("Replaced Spring default SessionFixationProtectionStrategy with HybrisSessionFixationProtectionStrategy");
                    }
                    else
                    {
                        LOG.info("Spring default SessionFixationProtectionStrategy was not defined. Added HybrisSessionFixationProtectionStrategy");
                    }
                }
                else if(removedDefaultSessionFixationStrategy)
                {
                    LOG.info("Removed Spring default SessionFixationProtectionStrategy, since HybrisSessionFixationProtectionStrategy is already defined");
                }
                else
                {
                    LOG.info("HybrisSessionFixationProtectionStrategy is defined as the only SessionFixationProtectionStrategy");
                }
            }
            catch(IllegalAccessException e)
            {
                throw new RuntimeException("Error removing default session fixation strategy", e);
            }
        }
        return bean;
    }


    private void addHybrisStrategyTo(List<SessionAuthenticationStrategy> strategies)
    {
        HybrisSessionFixationProtectionStrategy hybrisSessionFixationProtectionStrategy = new HybrisSessionFixationProtectionStrategy();
        hybrisSessionFixationProtectionStrategy.setMigrateSessionAttributes(false);
        strategies.add(hybrisSessionFixationProtectionStrategy);
    }


    private List<SessionAuthenticationStrategy> getStrategiesFrom(CompositeSessionAuthenticationStrategy strategy) throws IllegalAccessException
    {
        Field field = ReflectionUtils.findField(CompositeSessionAuthenticationStrategy.class, "delegateStrategies");
        if(field == null)
        {
            throw new RuntimeException("Could not find field 'delegateStrategies' in CompositeSessionAuthenticationStrategy!");
        }
        field.setAccessible(true);
        return (List<SessionAuthenticationStrategy>)field.get(strategy);
    }
}
