package de.hybris.platform.solrfacetsearch.config.mapping;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import java.util.Map;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Filter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultFacetSearchConfigMapper extends ConfigurableMapper implements ApplicationContextAware
{
    protected MapperFactory mapperFactory;
    protected ApplicationContext applicationContext;


    DefaultFacetSearchConfigMapper()
    {
        super(false);
    }


    protected void configure(MapperFactory mapperFactory)
    {
        this.mapperFactory = mapperFactory;
        addAllSpringBeans();
    }


    protected void addAllSpringBeans()
    {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(FacetSearchConfigMapping.class);
        for(Object bean : beans.values())
        {
            if(bean instanceof Converter)
            {
                addConverter((Converter<?, ?>)bean);
                continue;
            }
            if(bean instanceof Mapper)
            {
                addMapper((Mapper<?, ?>)bean);
                continue;
            }
            if(bean instanceof Filter)
            {
                addFilter((Filter<?, ?>)bean);
            }
        }
    }


    public void addConverter(Converter<?, ?> converter)
    {
        this.mapperFactory.getConverterFactory().registerConverter(converter);
    }


    public void addMapper(Mapper<?, ?> mapper)
    {
        this.mapperFactory.classMap(mapper.getAType(), mapper.getBType()).byDefault(new ma.glasnost.orika.DefaultFieldMapper[0]).customize(mapper).register();
    }


    public void addFilter(Filter<?, ?> filter)
    {
        this.mapperFactory.registerFilter(filter);
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
        init();
        map(new FacetSearchConfig(), FacetSearchConfig.class);
    }
}
