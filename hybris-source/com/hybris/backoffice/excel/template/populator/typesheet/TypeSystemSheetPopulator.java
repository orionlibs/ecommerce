package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Required;

public class TypeSystemSheetPopulator implements ExcelSheetPopulator
{
    private ExcelMapper<ExcelExportResult, AttributeDescriptorModel> mapper;
    private ExcelCellService excelCellService;
    private TypeSystemRowFactory typeSystemRowFactory;


    public void populate(@Nonnull ExcelExportResult excelExportResult)
    {
        Sheet typeSystemSheet = excelExportResult.getWorkbook().getSheet(ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM.getSheetName());
        populate(typeSystemSheet, (Collection<AttributeDescriptorModel>)this.mapper.apply(excelExportResult));
    }


    protected void populate(Sheet typeSystemSheet, Collection<AttributeDescriptorModel> attributeDescriptors)
    {
        Map<String, TypeSystemRow> typeSystemRows = mergeAttributesByQualifier(attributeDescriptors);
        populateTypeSystemSheet(typeSystemSheet, typeSystemRows.values());
    }


    protected Map<String, TypeSystemRow> mergeAttributesByQualifier(Collection<AttributeDescriptorModel> attributes)
    {
        Function<AttributeDescriptorModel, String> attributeDescriptorToKey = attributeDescriptorModel -> String.format("%s:%s:%s",
                        new Object[] {attributeDescriptorModel.getQualifier(), attributeDescriptorModel.getName(), this.typeSystemRowFactory.getAttributeDisplayName(attributeDescriptorModel)});
        Objects.requireNonNull(this.typeSystemRowFactory);
        Objects.requireNonNull(this.typeSystemRowFactory);
        return (Map<String, TypeSystemRow>)attributes.stream().collect(Collectors.toMap(attributeDescriptorToKey, this.typeSystemRowFactory::create, this.typeSystemRowFactory::merge));
    }


    protected void populateTypeSystemSheet(Sheet typeSystemSheet, Collection<TypeSystemRow> typeSystemRows)
    {
        typeSystemRows.forEach(typeSystemRow -> appendTypeSystemRowToSheet(typeSystemSheet, typeSystemRow));
    }


    private void appendTypeSystemRowToSheet(Sheet typeSystemSheet, TypeSystemRow typeSystemRow)
    {
        Row row = appendRow(typeSystemSheet);
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex()), typeSystemRow
                        .getTypeCode());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.TYPE_NAME.getIndex()), typeSystemRow
                        .getTypeName());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex()), typeSystemRow
                        .getAttrQualifier());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_NAME.getIndex()), typeSystemRow
                        .getAttrName());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_OPTIONAL.getIndex()), typeSystemRow
                        .getAttrOptional());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_TYPE_CODE.getIndex()), typeSystemRow
                        .getAttrTypeCode());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_TYPE_ITEMTYPE.getIndex()), typeSystemRow
                        .getAttrTypeItemType());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_LOCALIZED.getIndex()), typeSystemRow
                        .getAttrLocalized());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG.getIndex()), typeSystemRow
                        .getAttrLocLang());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex()), typeSystemRow
                        .getAttrDisplayName());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.ATTR_UNIQUE.getIndex()), typeSystemRow
                        .getAttrUnique());
        this.excelCellService.insertAttributeValue(row.createCell(ExcelTemplateConstants.TypeSystem.REFERENCE_FORMAT.getIndex()), typeSystemRow
                        .getAttrReferenceFormat());
    }


    private static Row appendRow(Sheet sheet)
    {
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }


    @Required
    public void setMapper(ExcelMapper<ExcelExportResult, AttributeDescriptorModel> mapper)
    {
        this.mapper = mapper;
    }


    @Required
    public void setTypeSystemRowFactory(TypeSystemRowFactory typeSystemRowFactory)
    {
        this.typeSystemRowFactory = typeSystemRowFactory;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }
}
