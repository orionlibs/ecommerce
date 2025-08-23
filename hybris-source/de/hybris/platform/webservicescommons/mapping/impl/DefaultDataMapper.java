package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.webservicescommons.mapping.TypeObjectFactory;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.platform.webservicescommons.mapping.config.FieldMapper;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Filter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingContextFactory;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.TypeFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultDataMapper extends ConfigurableMapper implements DataMapper, ApplicationContextAware
{
    private static final boolean DEFAULT_MAP_NULLS = false;
    private final MappingContextFactory mappingContextFactory = (MappingContextFactory)new MappingContext.Factory();
    private MapperFactory factory;
    private FieldSetBuilder fieldSetBuilder;
    private ApplicationContext applicationContext;


    public DefaultDataMapper()
    {
        super(false);
    }


    protected void configureFactoryBuilder(DefaultMapperFactory.Builder factoryBuilder)
    {
        factoryBuilder.mappingContextFactory(this.mappingContextFactory);
        factoryBuilder.captureFieldContext(true);
    }


    protected void configure(MapperFactory factory)
    {
        this.factory = factory;
        addAllSpringBeans();
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
        init();
    }


    protected void addAllSpringBeans()
    {
        Collection<Object> beans = getBeansForConfiguration();
        for(Object bean : beans)
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
                continue;
            }
            if(bean instanceof TypeObjectFactory)
            {
                registerObjectFactory((TypeObjectFactory<Object>)bean);
            }
        }
        Map<String, FieldMapper> fieldMappers = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, FieldMapper.class);
        for(FieldMapper mapper : fieldMappers.values())
        {
            addFieldMapper(mapper);
        }
    }


    protected Collection<Object> getBeansForConfiguration()
    {
        List<Object> beans = new ArrayList();
        ApplicationContext context = this.applicationContext;
        while(context != null)
        {
            beans.addAll(context.getBeansWithAnnotation(WsDTOMapping.class).values());
            context = context.getParent();
        }
        return beans;
    }


    protected void registerObjectFactory(TypeObjectFactory<Object> objectFactory)
    {
        this.factory.registerObjectFactory((ObjectFactory)objectFactory, TypeFactory.valueOf(objectFactory.getType()));
    }


    public void addConverter(Converter<?, ?> converter)
    {
        this.factory.getConverterFactory().registerConverter(converter);
    }


    public void addMapper(Mapper<?, ?> mapper)
    {
        this.factory.classMap(mapper.getAType(), mapper.getBType()).byDefault(new ma.glasnost.orika.DefaultFieldMapper[0]).customize(mapper).register();
    }


    public void addFilter(Filter<?, ?> filter)
    {
        this.factory.registerFilter(filter);
    }


    public void addFieldMapper(FieldMapper fieldMapper)
    {
        ClassMapBuilder mapBuilder = null;
        if((fieldMapper.getSourceClassArguments() != null && !fieldMapper.getSourceClassArguments().isEmpty()) || (fieldMapper
                        .getDestClassArguments() != null && !fieldMapper.getDestClassArguments().isEmpty()))
        {
            Type sourceType = TypeFactory.valueOf(fieldMapper.getSourceClass(), fieldMapper.getSourceActualTypeArguments());
            Type destType = TypeFactory.valueOf(fieldMapper.getDestClass(), fieldMapper.getDestActualTypeArguments());
            mapBuilder = this.factory.classMap(sourceType, destType);
        }
        else
        {
            mapBuilder = this.factory.classMap(fieldMapper.getSourceClass(), fieldMapper.getDestClass());
        }
        if(fieldMapper.getFieldMapping() != null && !fieldMapper.getFieldMapping().isEmpty())
        {
            for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)fieldMapper.getFieldMapping().entrySet())
            {
                mapBuilder.field(entry.getKey(), entry.getValue());
            }
        }
        this.factory.registerClassMap(mapBuilder.byDefault(new ma.glasnost.orika.DefaultFieldMapper[0]).toClassMap());
    }


    public <S, D> D map(S sourceObject, Class<D> destinationClass, String fields)
    {
        return (D)map(sourceObject, destinationClass, createMappingContext(destinationClass, fields));
    }


    public <S, D> D map(S sourceObject, Class<D> destinationClass, Set<String> fields)
    {
        return (D)map(sourceObject, destinationClass, createMappingContext(fields));
    }


    public <S, D> void map(S sourceObject, D destinationObject, String fields)
    {
        map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), fields));
    }


    public <S, D> void map(S sourceObject, D destinationObject, String fields, boolean mapNulls)
    {
        map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), fields, mapNulls));
    }


    public <S, D> void map(S sourceObject, D destinationObject, boolean mapNulls)
    {
        map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), null, mapNulls));
    }


    public <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass, String fields)
    {
        return mapAsList(source, destinationClass, createMappingContext(destinationClass, fields));
    }


    public <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass, String fields)
    {
        return mapAsSet(source, destinationClass, createMappingContext(destinationClass, fields));
    }


    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, String fields)
    {
        mapAsCollection(source, destination, destinationClass, createMappingContext(destinationClass, fields));
    }


    public <S, D> void mapGeneric(S sourceObject, D destObject, Type[] sourceActualTypeArguments, Type[] destActualTypeArguments, String fields, Map<String, Class<?>> destTypeVariableMap)
    {
        Type sourceType = TypeFactory.valueOf(sourceObject.getClass(), sourceActualTypeArguments);
        Type destType = TypeFactory.valueOf(destObject.getClass(), destActualTypeArguments);
        map(sourceObject, destObject, sourceType, destType,
                        createMappingContextForGeneric(destObject.getClass(), fields, destTypeVariableMap));
    }


    protected MappingContext createMappingContext(Set<String> fields)
    {
        MappingContext context = this.mappingContextFactory.getContext();
        if(fields != null)
        {
            context.setProperty("FIELD_SET_NAME", fields);
        }
        return context;
    }


    protected MappingContext createMappingContext(Class destinationClass, String fields)
    {
        return createMappingContext(destinationClass, fields, false);
    }


    protected MappingContext createMappingContext(Class destinationClass, String fields, boolean mapNulls)
    {
        MappingContext context = this.mappingContextFactory.getContext();
        if(fields != null)
        {
            Set<String> propertySet = this.fieldSetBuilder.createFieldSet(destinationClass, "destination", fields);
            context.setProperty("FIELD_SET_NAME", propertySet);
        }
        context.setProperty("MAP_NULLS", Boolean.valueOf(mapNulls));
        return context;
    }


    protected MappingContext createMappingContextForGeneric(Class destinationClass, String fields, Map<String, Class<?>> typeVariableMap)
    {
        MappingContext context = this.mappingContextFactory.getContext();
        if(fields != null)
        {
            FieldSetBuilderContext fieldSetBuilderContext = new FieldSetBuilderContext();
            fieldSetBuilderContext.setTypeVariableMap(typeVariableMap);
            Set<String> propertySet = this.fieldSetBuilder.createFieldSet(destinationClass, "destination", fields, fieldSetBuilderContext);
            context.setProperty("FIELD_SET_NAME", propertySet);
        }
        return context;
    }


    @Required
    public void setFieldSetBuilder(FieldSetBuilder fieldSetBuilder)
    {
        this.fieldSetBuilder = fieldSetBuilder;
    }
}
