package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExportDataFactory implements ExportDataFactory
{
    private ModelService modelService;


    public Optional<String> create(RequiredAttribute rootUniqueAttribute, Object objectToExport)
    {
        List<String> exportedValues = new ArrayList<>();
        if(objectToExport instanceof java.util.Collection)
        {
            for(Object singleObject : objectToExport)
            {
                exportedValues.add(exportSingleValue(singleObject, rootUniqueAttribute));
            }
        }
        else
        {
            exportedValues.add(exportSingleValue(objectToExport, rootUniqueAttribute));
        }
        return Optional.of(String.join(",", (Iterable)exportedValues));
    }


    private String exportSingleValue(Object objectToExport, RequiredAttribute uniqueAttribute)
    {
        Object loadedValue = uniqueAttribute.isRoot() ? objectToExport : loadValue(objectToExport, uniqueAttribute.getQualifier());
        if(loadedValue == null)
        {
            return "";
        }
        if(uniqueAttribute.getChildren().isEmpty())
        {
            return Objects.toString(loadedValue, "");
        }
        String value = uniqueAttribute.getChildren().stream().map(children -> exportSingleValue(loadedValue, children)).collect(Collectors.joining(":"));
        return value.matches(":*") ? "" : value;
    }


    private Object loadValue(Object object, String qualifier)
    {
        return (object == null) ? null : this.modelService.getAttributeValue(object, qualifier);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
