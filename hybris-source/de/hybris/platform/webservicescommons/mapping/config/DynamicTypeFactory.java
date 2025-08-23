package de.hybris.platform.webservicescommons.mapping.config;

import de.hybris.platform.webservicescommons.mapping.TypeObjectFactory;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import ma.glasnost.orika.MappingContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@WsDTOMapping
public class DynamicTypeFactory<D> implements TypeObjectFactory<D>, ApplicationContextAware
{
    private static final Logger LOG = Logger.getLogger(DynamicTypeFactory.class);
    private Class<D> baseType;
    private ApplicationContext applicationContext;
    private final Map<Class, Class> factoryMap = (Map)new ConcurrentHashMap<>();


    @PostConstruct
    public void init()
    {
        Map<String, FieldMapper> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, FieldMapper.class);
        for(FieldMapper mapper : beans.values())
        {
            if(this.baseType.isAssignableFrom(mapper.getSourceClass()))
            {
                this.factoryMap.put(mapper.getSourceClass(), mapper.getDestClass());
            }
        }
    }


    public D create(Object source, MappingContext mappingContext)
    {
        Class<D> destinationClass = this.factoryMap.get(source.getClass());
        if(destinationClass != null)
        {
            try
            {
                return destinationClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch(InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e)
            {
                LOG.warn("Failed to create instance of class from source - using default value", e);
                return getDefault(source.getClass());
            }
        }
        return getDefault(source.getClass());
    }


    protected D getDefault(Class source)
    {
        try
        {
            return this.baseType.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch(InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e)
        {
            throw new RuntimeException("Failed to create instance of type " + this.baseType + " as fallback object for " + source + " type", e);
        }
    }


    public Class<D> getType()
    {
        return this.baseType;
    }


    @Required
    public void setBaseType(Class<D> baseType)
    {
        this.baseType = baseType;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
