package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class ExcelAttributeTranslatorRegistry
{
    private List<ExcelAttributeTranslator<ExcelAttribute>> translators;


    public Optional<ExcelAttributeTranslator<ExcelAttribute>> findTranslator(ExcelAttribute excelAttribute)
    {
        return this.translators.stream().filter(translator -> translator.canHandle(excelAttribute)).findFirst();
    }


    public Optional<ExcelAttributeTranslator<ExcelAttribute>> findTranslator(ExcelAttribute excelAttribute, Class<? extends ExcelAttributeTranslator>... exclude)
    {
        Collection<Class<? extends ExcelAttributeTranslator>> excludedTranslators = Optional.<Class<? extends ExcelAttributeTranslator>[]>ofNullable(exclude).map(Arrays::asList).orElseGet(Collections::emptyList);
        return this.translators.stream()
                        .filter(translator -> !excludedTranslators.contains(getTranslatorClass(translator)))
                        .filter(translator -> translator.canHandle(excelAttribute))
                        .findFirst();
    }


    protected Class<? extends ExcelAttributeTranslator> getTranslatorClass(ExcelAttributeTranslator<ExcelAttribute> translator)
    {
        return (Class)translator.getClass();
    }


    @PostConstruct
    public void init()
    {
        OrderComparator.sort(getTranslators());
    }


    public boolean canHandle(ExcelAttribute excelAttribute)
    {
        return this.translators.stream().anyMatch(translator -> translator.canHandle(excelAttribute));
    }


    public List<ExcelAttributeTranslator<ExcelAttribute>> getTranslators()
    {
        return this.translators;
    }


    @Required
    public void setTranslators(List<ExcelAttributeTranslator<ExcelAttribute>> translators)
    {
        this.translators = translators;
    }
}
