package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.data.ExcelColumn;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelWorkbookEntriesService implements ExcelWorkbookEntriesService
{
    private RequiredAttributesFactory requiredAttributesFactory;
    private TypeService typeService;


    public Collection<WorksheetEntryKey> generateEntryKeys(ExcelWorksheet worksheet)
    {
        ComposedTypeModel composedTypeForSheet = this.typeService.getComposedTypeForCode(worksheet.getSheetName());
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(composedTypeForSheet);
        Collection<Map<ExcelColumn, ImportParameters>> rows = worksheet.getTable().rowMap().values();
        List<WorksheetEntryKey> result = new ArrayList<>();
        for(Map<ExcelColumn, ImportParameters> row : rows)
        {
            result.add(generateEntryKey(requiredAttribute, row));
        }
        return result;
    }


    protected WorksheetEntryKey generateEntryKey(RequiredAttribute requiredAttribute, Map<ExcelColumn, ImportParameters> row)
    {
        Map<String, String> params = new HashMap<>();
        for(Map.Entry<ExcelColumn, ImportParameters> cell : row.entrySet())
        {
            AttributeDescriptorModel attributeDescriptor = ((ExcelColumn)cell.getKey()).getSelectedAttribute().getAttributeDescriptor();
            if(BooleanUtils.isTrue(attributeDescriptor.getUnique()))
            {
                params.putAll(prepareParamsRecursively(attributeDescriptor, cell.getValue()));
            }
        }
        return new WorksheetEntryKey(requiredAttribute, params);
    }


    private Map<String, String> prepareParamsRecursively(AttributeDescriptorModel attributeDescriptorModel, ImportParameters importParameters)
    {
        Map<String, String> params = new HashMap<>();
        TypeModel attributeType = attributeDescriptorModel.getAttributeType();
        if(attributeType instanceof ComposedTypeModel)
        {
            List<AttributeDescriptorModel> uniqueAttributes = findUniqueAttributes((ComposedTypeModel)attributeType);
            for(AttributeDescriptorModel uniqueAttribute : uniqueAttributes)
            {
                params.putAll(prepareParamsRecursively(uniqueAttribute, importParameters));
            }
        }
        if(attributeType instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
        {
            Pair<String, String> keyAndValue = findKeyAndValue(attributeDescriptorModel, importParameters);
            params.put((String)keyAndValue.getKey(), (String)keyAndValue.getValue());
        }
        return params;
    }


    protected List<AttributeDescriptorModel> findUniqueAttributes(ComposedTypeModel attributeType)
    {
        List<AttributeDescriptorModel> allAttributes = new ArrayList<>();
        allAttributes.addAll(attributeType.getDeclaredattributedescriptors());
        allAttributes.addAll(attributeType.getInheritedattributedescriptors());
        return (List<AttributeDescriptorModel>)allAttributes.stream().filter(attr -> BooleanUtils.isTrue(attr.getUnique())).collect(Collectors.toList());
    }


    protected Pair<String, String> findKeyAndValue(AttributeDescriptorModel attributeDescriptorModel, ImportParameters importParameters)
    {
        String key = String.format("%s.%s", new Object[] {attributeDescriptorModel.getEnclosingType().getCode(), attributeDescriptorModel
                        .getQualifier()});
        Map<String, String> singleValueParameters = importParameters.getSingleValueParameters();
        if(singleValueParameters.containsKey(key))
        {
            return Pair.of(key, singleValueParameters.get(key));
        }
        for(ComposedTypeModel superType : attributeDescriptorModel.getEnclosingType().getAllSuperTypes())
        {
            String parentKey = String.format("%s.%s", new Object[] {superType.getCode(), attributeDescriptorModel.getQualifier()});
            if(singleValueParameters.containsKey(parentKey))
            {
                return Pair.of(parentKey, singleValueParameters.get(parentKey));
            }
        }
        return Pair.of(key, singleValueParameters.get("rawValue"));
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
