package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.Ordered;

public interface ExcelValueTranslator<T> extends Ordered
{
    boolean canHandle(AttributeDescriptorModel paramAttributeDescriptorModel);


    default boolean canHandle(AttributeDescriptorModel attributeDescriptor, ItemModel itemModel)
    {
        return canHandle(attributeDescriptor);
    }


    Optional<Object> exportData(T paramT);


    default Optional<Object> exportData(AttributeDescriptorModel attributeDescriptorModel, T objectToExport)
    {
        return exportData(objectToExport);
    }


    Impex importData(AttributeDescriptorModel paramAttributeDescriptorModel, ImportParameters paramImportParameters);


    default String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return "";
    }


    ExcelValidationResult validate(ImportParameters paramImportParameters, AttributeDescriptorModel paramAttributeDescriptorModel, Map<String, Object> paramMap);
}
