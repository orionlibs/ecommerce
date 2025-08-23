package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.ExcelCellPopulator;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationIncludedHeaderPromptPopulator implements ExcelSheetPopulator
{
    private ExcelCellService excelCellService;
    private Map<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelAttributeDescriptorAttribute>> excelAttributeDescriptorPopulators;
    private Map<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelClassificationAttribute>> excelClassificationPopulators;


    public void populate(@Nonnull ExcelExportResult excelExportResult)
    {
        Objects.requireNonNull(ExcelAttributeDescriptorAttribute.class);
        Objects.requireNonNull(ExcelAttributeDescriptorAttribute.class);
        List<ExcelAttributeDescriptorAttribute> descriptorAttributes = (List<ExcelAttributeDescriptorAttribute>)excelExportResult.getAvailableAdditionalAttributes().stream().filter(ExcelAttributeDescriptorAttribute.class::isInstance).map(ExcelAttributeDescriptorAttribute.class::cast)
                        .sorted(Comparator.comparing(this::getAttributeDescriptorName)).collect(Collectors.toList());
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        List<ExcelClassificationAttribute> classificationAttributes = (List<ExcelClassificationAttribute>)excelExportResult.getSelectedAdditionalAttributes().stream().filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast).collect(Collectors.toList());
        Map<String, List<ExcelAttributeDescriptorAttribute>> attrs = (Map<String, List<ExcelAttributeDescriptorAttribute>>)descriptorAttributes.stream().collect(Collectors.groupingBy(e -> e.getAttributeDescriptorModel().getEnclosingType().getCode()));
        Sheet sheet = getSheet(excelExportResult.getWorkbook());
        attrs.forEach((type, descriptors) -> {
            String typeCode = populateAttributesBasedOnAttributeDescriptors(excelExportResult, descriptors);
            populateAttributesBasedOnClassification(sheet, classificationAttributes, typeCode);
        });
    }


    private String populateAttributesBasedOnAttributeDescriptors(ExcelExportResult excelExportResult, List<ExcelAttributeDescriptorAttribute> descriptors)
    {
        Sheet sheet = getSheet(excelExportResult.getWorkbook());
        String typeCode = "";
        for(ExcelAttributeDescriptorAttribute descriptor : descriptors)
        {
            Row row = appendRow(sheet);
            for(Map.Entry<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelAttributeDescriptorAttribute>> entry : this.excelAttributeDescriptorPopulators
                            .entrySet())
            {
                Map<String, Object> context = new HashMap<>();
                context.put("excelAttribute", descriptor);
                context.put(Workbook.class.getSimpleName(), excelExportResult.getWorkbook());
                String cellValue = (String)((ExcelCellPopulator)entry.getValue()).apply(DefaultExcelAttributeContext.ofMap((ExcelAttribute)descriptor, context));
                if(((ExcelTemplateConstants.HeaderPrompt)entry.getKey()).getIndex() == ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex())
                {
                    typeCode = cellValue;
                }
                row.createCell(((ExcelTemplateConstants.HeaderPrompt)entry.getKey()).getIndex()).setCellValue(cellValue);
            }
        }
        return typeCode;
    }


    private void populateAttributesBasedOnClassification(Sheet sheet, List<ExcelClassificationAttribute> classificationAttributes, String typeCode)
    {
        for(ExcelClassificationAttribute classification : classificationAttributes)
        {
            Row row = appendRow(sheet);
            row.createCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).setCellValue(typeCode);
            for(Map.Entry<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelClassificationAttribute>> entry : this.excelClassificationPopulators
                            .entrySet())
            {
                String cellValue = (String)((ExcelCellPopulator)entry.getValue()).apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)classification));
                this.excelCellService.insertAttributeValue(row.createCell(((ExcelTemplateConstants.HeaderPrompt)entry.getKey()).getIndex()), cellValue);
            }
        }
    }


    protected String getAttributeDescriptorName(ExcelAttributeDescriptorAttribute attributeDescriptorAttribute)
    {
        AttributeDescriptorModel attributeDescriptor = attributeDescriptorAttribute.getAttributeDescriptorModel();
        return StringUtils.isNotEmpty(attributeDescriptor.getName()) ? attributeDescriptor.getName() :
                        attributeDescriptor.getQualifier();
    }


    private static Row appendRow(Sheet typeSystemSheet)
    {
        return typeSystemSheet.createRow(typeSystemSheet.getLastRowNum() + 1);
    }


    private static Sheet getSheet(Workbook workbook)
    {
        return workbook.getSheet(ExcelTemplateConstants.UtilitySheet.HEADER_PROMPT.getSheetName());
    }


    @Required
    public void setExcelAttributeDescriptorPopulators(Map<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelAttributeDescriptorAttribute>> excelAttributeDescriptorPopulators)
    {
        this.excelAttributeDescriptorPopulators = excelAttributeDescriptorPopulators;
    }


    @Required
    public void setExcelClassificationPopulators(Map<ExcelTemplateConstants.HeaderPrompt, ExcelCellPopulator<ExcelClassificationAttribute>> excelClassificationPopulators)
    {
        this.excelClassificationPopulators = excelClassificationPopulators;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }
}
