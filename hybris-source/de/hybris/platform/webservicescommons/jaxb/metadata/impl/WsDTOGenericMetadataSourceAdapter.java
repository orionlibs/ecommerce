package de.hybris.platform.webservicescommons.jaxb.metadata.impl;

import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataNameProvider;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.persistence.jaxb.metadata.MetadataSourceAdapter;
import org.eclipse.persistence.jaxb.xmlmodel.JavaType;
import org.eclipse.persistence.jaxb.xmlmodel.ObjectFactory;
import org.eclipse.persistence.jaxb.xmlmodel.XmlBindings;
import org.eclipse.persistence.jaxb.xmlmodel.XmlElement;
import org.eclipse.persistence.jaxb.xmlmodel.XmlElementWrapper;
import org.eclipse.persistence.jaxb.xmlmodel.XmlJavaTypeAdapter;
import org.eclipse.persistence.jaxb.xmlmodel.XmlJavaTypeAdapters;
import org.eclipse.persistence.jaxb.xmlmodel.XmlRootElement;

public class WsDTOGenericMetadataSourceAdapter extends MetadataSourceAdapter
{
    private final Class<?> clazz;
    private final Boolean wrapCollections;
    private final Collection<Class<?>> typeAdapters;
    private final MetadataNameProvider nameProvider;
    private static final int EXPECTED_NUMBER_OF_TYPE_ARGUMENTS = 2;


    public WsDTOGenericMetadataSourceAdapter(Class<?> clazz, Collection<Class<?>> typeAdapters, Boolean wrapCollections, MetadataNameProvider nameProvider)
    {
        this.clazz = clazz;
        this.wrapCollections = wrapCollections;
        this.typeAdapters = typeAdapters;
        this.nameProvider = nameProvider;
    }


    public XmlBindings getXmlBindings(Map<String, ?> properties, ClassLoader classLoader)
    {
        ObjectFactory factory = createObjectFactory();
        JavaType javaType = createJavaType();
        XmlBindings xmlBindings = createXmlBindings();
        processXmlType(javaType, factory);
        xmlBindings.getJavaTypes().getJavaType().add(javaType);
        return xmlBindings;
    }


    protected ObjectFactory createObjectFactory()
    {
        return new ObjectFactory();
    }


    protected JavaType createJavaType()
    {
        JavaType javaType = new JavaType();
        javaType.setName(getClazz().getName());
        javaType.setJavaAttributes(new JavaType.JavaAttributes());
        javaType.setXmlRootElement(createRootElement());
        return javaType;
    }


    protected XmlRootElement createRootElement()
    {
        XmlRootElement xre = new XmlRootElement();
        xre.setName(getNameProvider().createNodeNameFromClass(getClazz()));
        return xre;
    }


    protected XmlBindings createXmlBindings()
    {
        XmlBindings xmlBindings = new XmlBindings();
        String packageName = getClazz().getPackage().getName();
        xmlBindings.setPackageName(packageName);
        xmlBindings.setJavaTypes(new XmlBindings.JavaTypes());
        xmlBindings.setXmlJavaTypeAdapters(new XmlJavaTypeAdapters());
        return xmlBindings;
    }


    protected void processXmlType(JavaType javaType, ObjectFactory factory)
    {
        boolean wrap = isWrapCollections();
        boolean adapt = isTypeAdapters();
        if(wrap || adapt)
        {
            Map<String, Class<?>> adapterMap = new HashMap<>();
            if(adapt)
            {
                adapterMap = (Map<String, Class<?>>)getTypeAdapters().stream().collect(Collectors.toMap(this::getAdapterType, Function.identity()));
            }
            Field[] fields = getClazz().getDeclaredFields();
            for(Field field : fields)
            {
                processBindingInner(javaType, factory, wrap, adapt, adapterMap, field);
            }
        }
    }


    protected String getAdapterType(Class<?> paramClass)
    {
        if(!XmlAdapter.class.isAssignableFrom(paramClass))
        {
            return null;
        }
        ParameterizedType pt = (ParameterizedType)paramClass.getGenericSuperclass();
        if((pt.getActualTypeArguments()).length == 2)
        {
            return pt.getActualTypeArguments()[1].getTypeName();
        }
        return Object.class.getCanonicalName();
    }


    protected void processBindingInner(JavaType javaType, ObjectFactory factory, boolean wrap, boolean adapt, Map<String, Class<?>> adapterMap, Field field)
    {
        boolean add = false;
        XmlElement element = createXmlElement(field);
        if(wrap && shouldWrapField(field))
        {
            add = true;
            wrapField(element, field);
        }
        if(adapt && shouldAdaptField(field, adapterMap))
        {
            add = true;
            adaptField(element, field, adapterMap);
        }
        if(add)
        {
            javaType.getJavaAttributes().getJavaAttribute().add(factory.createXmlElement(element));
        }
    }


    protected XmlElement createXmlElement(Field field)
    {
        return new XmlElement();
    }


    protected boolean shouldWrapField(Field field)
    {
        Class<?> fieldClass = field.getType();
        return Collection.class.isAssignableFrom(fieldClass);
    }


    protected void wrapField(XmlElement xe, Field field)
    {
        XmlElementWrapper xew = new XmlElementWrapper();
        xew.setName(field.getName());
        xe.setJavaAttribute(field.getName());
        xe.setName(getNameProvider().createCollectionItemNameFromField(field));
        xe.setXmlElementWrapper(xew);
    }


    protected boolean shouldAdaptField(Field field, Map<String, Class<?>> adapterMap)
    {
        String typeName = field.getGenericType().getTypeName();
        return adapterMap.containsKey(typeName);
    }


    protected void adaptField(XmlElement element, Field field, Map<String, Class<?>> adapterMap)
    {
        String typeName = field.getGenericType().getTypeName();
        Class adapterClass = adapterMap.get(typeName);
        XmlJavaTypeAdapter xjta = new XmlJavaTypeAdapter();
        xjta.setValue(adapterClass.getCanonicalName());
        element.setXmlJavaTypeAdapter(xjta);
        element.setJavaAttribute(field.getName());
    }


    protected boolean isWrapCollections()
    {
        return (getWrapCollections() != null && Boolean.TRUE.equals(getWrapCollections()));
    }


    protected boolean isTypeAdapters()
    {
        return CollectionUtils.isNotEmpty(getTypeAdapters());
    }


    protected Class<?> getClazz()
    {
        return this.clazz;
    }


    protected MetadataNameProvider getNameProvider()
    {
        return this.nameProvider;
    }


    protected Collection<Class<?>> getTypeAdapters()
    {
        return this.typeAdapters;
    }


    protected Boolean getWrapCollections()
    {
        return this.wrapCollections;
    }
}
