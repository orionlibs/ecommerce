package de.hybris.platform.cms2.cmsitems.converter.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import de.hybris.platform.cms2.cmsitems.converter.AttributeContentConverter;
import de.hybris.platform.cms2.cmsitems.converter.AttributeStrategyConverterProvider;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class BaseAttributeStrategyConverterProvider<T> implements AttributeStrategyConverterProvider<T>, InitializingBean
{
    private List<AttributeContentConverter<T>> attributeContentConverters;
    private Deque<AttributeContentConverter<T>> internalAttributeContentConverters;


    public AttributeContentConverter<T> getContentConverter(T attributeDescriptor)
    {
        if(getInternalAttributeContentConverters().size() != getAttributeContentConverters().size())
        {
            this.internalAttributeContentConverters = new LinkedList<>(getAttributeContentConverters());
        }
        Preconditions.checkArgument(Objects.nonNull(attributeDescriptor), "AttributeDescriptor/Object Value should not be null");
        return ((Stream<AttributeContentConverter<T>>)getDescendingConverterStreamSupplier()
                        .get())
                        .filter(contentConverter -> contentConverter.getConstrainedBy().test(attributeDescriptor))
                        .findFirst()
                        .orElse(null);
    }


    public void afterPropertiesSet() throws Exception
    {
        this.internalAttributeContentConverters = new LinkedList<>(getAttributeContentConverters());
    }


    protected Supplier<Stream<AttributeContentConverter<T>>> getDescendingConverterStreamSupplier()
    {
        return () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(getInternalAttributeContentConverters().descendingIterator(), 16), false);
    }


    protected Deque<AttributeContentConverter<T>> getInternalAttributeContentConverters()
    {
        return this.internalAttributeContentConverters;
    }


    protected List<AttributeContentConverter<T>> getAttributeContentConverters()
    {
        return this.attributeContentConverters;
    }


    @Required
    public void setAttributeContentConverters(List<AttributeContentConverter<T>> attributeContentConverters)
    {
        this.attributeContentConverters = attributeContentConverters;
    }
}
