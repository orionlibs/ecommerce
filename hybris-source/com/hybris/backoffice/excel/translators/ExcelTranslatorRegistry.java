package com.hybris.backoffice.excel.translators;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class ExcelTranslatorRegistry
{
    private List<ExcelValueTranslator<Object>> translators;


    public Optional<ExcelValueTranslator<Object>> getTranslator(AttributeDescriptorModel attributeDescriptor)
    {
        return getTranslators().stream().filter(translator -> translator.canHandle(attributeDescriptor)).findFirst();
    }


    public Optional<ExcelValueTranslator<Object>> getTranslator(AttributeDescriptorModel attributeDescriptor, ItemModel itemModel)
    {
        return getTranslators().stream().filter(translator -> translator.canHandle(attributeDescriptor, itemModel)).findFirst();
    }


    public Optional<ExcelValueTranslator<Object>> getTranslator(AttributeDescriptorModel attributeDescriptor, Class<? extends ExcelValueTranslator>... exclude)
    {
        Collection<Class<? extends ExcelValueTranslator>> excludedTranslators = Optional.<Class<? extends ExcelValueTranslator>[]>ofNullable(exclude).map(Arrays::asList).orElseGet(Collections::emptyList);
        return getTranslators().stream()
                        .filter(translator -> !excludedTranslators.contains(getTranslatorClass(translator)))
                        .filter(translator -> translator.canHandle(attributeDescriptor)).findFirst();
    }


    protected Class<? extends ExcelValueTranslator> getTranslatorClass(ExcelValueTranslator<Object> translator)
    {
        return (Class)translator.getClass();
    }


    @Deprecated(since = "1811", forRemoval = true)
    @PostConstruct
    public void init()
    {
    }


    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return getTranslators().stream().anyMatch(translator -> translator.canHandle(attributeDescriptor));
    }


    public List<ExcelValueTranslator<Object>> getTranslators()
    {
        List<ExcelValueTranslator<Object>> copy = new ArrayList<>(this.translators);
        OrderComparator.sort(copy);
        return copy;
    }


    @Required
    public void setTranslators(List<ExcelValueTranslator<Object>> translators)
    {
        this.translators = translators;
    }
}
