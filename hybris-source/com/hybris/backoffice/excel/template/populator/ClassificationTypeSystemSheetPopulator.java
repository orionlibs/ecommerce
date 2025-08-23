package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationTypeSystemSheetPopulator implements ExcelSheetPopulator
{
    private ExcelCellService excelCellService;
    private Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, ExcelClassificationCellPopulator> cellValuePopulators;
    private Collection<ExcelFilter<ExcelAttribute>> filters = new LinkedList<>();
    private ClassificationTypeSystemSheetCompressor compressor;


    public void populate(@Nonnull ExcelExportResult excelExportResult)
    {
        Sheet typeSystemSheet = getOrCreateClassificationTypeSystemSheet(excelExportResult.getWorkbook());
        Collection<ExcelClassificationAttribute> classificationAttributes = extractClassificationAttributes(excelExportResult
                        .getSelectedAdditionalAttributes());
        populate(typeSystemSheet, classificationAttributes);
    }


    protected void populate(Sheet typeSystemSheet, Collection<ExcelClassificationAttribute> classificationAttributes)
    {
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> rows = (Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>>)classificationAttributes.stream().map(this::applyPopulatorsOnAttribute).collect(Collectors.toList());
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> compressedRows = this.compressor.compress(rows);
        compressedRows.forEach(compressedRow -> {
            Row lastRow = appendRow(typeSystemSheet);
            compressedRow.forEach(());
        });
    }


    private Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> applyPopulatorsOnAttribute(ExcelClassificationAttribute attribute)
    {
        return (Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>)this.cellValuePopulators.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> ((ExcelClassificationCellPopulator)entry.getValue()).apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute))));
    }


    protected List<ExcelClassificationAttribute> extractClassificationAttributes(Collection<ExcelAttribute> attributes)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return (List<ExcelClassificationAttribute>)attributes.stream().filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .filter(this::applyAllFilters)
                        .collect(Collectors.toList());
    }


    private boolean applyAllFilters(ExcelAttribute attribute)
    {
        return this.filters.stream().allMatch(filter -> filter.test(attribute));
    }


    protected Sheet getOrCreateClassificationTypeSystemSheet(@Nonnull Workbook workbook)
    {
        return Optional.<Sheet>ofNullable(workbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName()))
                        .orElseGet(() -> workbook.createSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName()));
    }


    private static Row appendRow(Sheet typeSystemSheet)
    {
        return typeSystemSheet.createRow(typeSystemSheet.getLastRowNum() + 1);
    }


    @Required
    public void setCellValuePopulators(Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, ExcelClassificationCellPopulator> cellValuePopulators)
    {
        this.cellValuePopulators = cellValuePopulators;
    }


    @Required
    public void setCompressor(ClassificationTypeSystemSheetCompressor compressor)
    {
        this.compressor = compressor;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    public void setFilters(Collection<ExcelFilter<ExcelAttribute>> filters)
    {
        this.filters = filters;
    }
}
