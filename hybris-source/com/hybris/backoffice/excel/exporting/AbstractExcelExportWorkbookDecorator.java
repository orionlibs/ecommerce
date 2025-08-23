package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.exporting.data.ExcelCellValue;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExcelExportWorkbookDecorator implements ExcelExportWorkbookDecorator
{
    private ExcelCellService excelCellService;
    private AttributeNameFormatter<ExcelClassificationAttribute> attributeNameFormatter;
    private ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry;


    protected void decorate(Workbook workbook, Collection<ExcelClassificationAttribute> attributes, Collection<ItemModel> items)
    {
        Map<ItemModel, Optional<Row>> rowsCache = new HashMap<>();
        for(Iterator<ExcelClassificationAttribute> iterator = attributes.iterator(); iterator.hasNext(); )
        {
            ExcelClassificationAttribute attribute = iterator.next();
            String headerValue = getAttributeNameFormatter().format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
            Optional<ExcelAttributeTranslator<ExcelAttribute>> translator = getExcelAttributeTranslatorRegistry().findTranslator((ExcelAttribute)attribute);
            String referenceFormat = translator.isPresent() ? ((ExcelAttributeTranslator)translator.get()).referenceFormat((ExcelAttribute)attribute) : "";
            for(ItemModel item : items)
            {
                ((Optional)rowsCache.computeIfAbsent(item, key -> findRow(workbook, key))).ifPresent(row -> {
                    Cell headerCell = insertHeaderIfNecessary(row.getSheet(), headerValue);
                    Cell valueCell = createCellIfNecessary(row, headerCell.getColumnIndex());
                    insertReferenceFormatIfNecessary(valueCell, referenceFormat);
                    ExcelCellValue excelCellValue = new ExcelCellValue(valueCell, (ExcelAttribute)attribute, item);
                    translator.ifPresent(());
                });
            }
        }
    }


    protected void exportDataIntoCell(ExcelAttributeTranslator<ExcelAttribute> translator, ExcelCellValue excelCellValue)
    {
        translator.exportData(excelCellValue.getExcelAttribute(), excelCellValue.getItemModel())
                        .ifPresent(value -> getExcelCellService().insertAttributeValue(excelCellValue.getCell(), value));
    }


    protected void insertReferenceFormatIfNecessary(Cell excelCellValue, String referenceFormat)
    {
        if(StringUtils.isNotBlank(referenceFormat))
        {
            Sheet sheet = excelCellValue.getSheet();
            Row referencePatternRow = sheet.getRow(1);
            int columnIndex = excelCellValue.getColumnIndex();
            Cell referencePatternCell = referencePatternRow.getCell(columnIndex);
            if(referencePatternCell == null)
            {
                referencePatternCell = referencePatternRow.createCell(columnIndex);
            }
            getExcelCellService().insertAttributeValue(referencePatternCell, referenceFormat);
        }
    }


    protected Cell insertHeaderIfNecessary(Sheet sheet, String headerValue)
    {
        Row headerRow = sheet.getRow(0);
        int columnIndex = findColumnIndexByContentOrFirstEmptyCell(headerRow, headerValue);
        return createNewHeaderCell(headerRow, (columnIndex != -1) ? columnIndex : (headerRow.getLastCellNum() + 1), headerValue);
    }


    protected int findColumnIndexByContentOrFirstEmptyCell(Row row, String content)
    {
        int emptyCell = -1;
        for(int columnIndex = row.getFirstCellNum(); columnIndex <= row.getLastCellNum(); columnIndex++)
        {
            String cellValue = getExcelCellService().getCellValue(row.getCell(columnIndex));
            if(cellValue.equals(content))
            {
                return columnIndex;
            }
            if(emptyCell == -1 && StringUtils.isBlank(cellValue))
            {
                emptyCell = columnIndex;
            }
        }
        return emptyCell;
    }


    protected Cell createNewHeaderCell(Row headerRow, int columnIndex, String headerValue)
    {
        Cell cell = createCellIfNecessary(headerRow, columnIndex);
        getExcelCellService().insertAttributeValue(cell, headerValue);
        return cell;
    }


    protected Cell createCellIfNecessary(Row row, int columnIndex)
    {
        Cell cell = row.getCell(columnIndex);
        return (cell != null) ? cell : row.createCell(columnIndex);
    }


    protected Optional<Row> findRow(Workbook workbook, ItemModel item)
    {
        Sheet pkSheet = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.PK.getSheetName());
        for(int rowIndex = pkSheet.getFirstRowNum(); rowIndex <= pkSheet.getLastRowNum(); rowIndex++)
        {
            Row row = pkSheet.getRow(rowIndex);
            if(row != null)
            {
                Cell foundPkCell = row.getCell(0);
                String pkAsString = getExcelCellService().getCellValue(foundPkCell);
                if(item.getPk().getLongValueAsString().equals(pkAsString))
                {
                    String foundSheetName = getExcelCellService().getCellValue(row.getCell(1));
                    String foundRowIndex = getExcelCellService().getCellValue(row.getCell(2));
                    return findRowBySheetNameAndRowIndex(workbook, foundSheetName, foundRowIndex);
                }
            }
        }
        return Optional.empty();
    }


    private static Optional<Row> findRowBySheetNameAndRowIndex(Workbook workbook, String foundSheetName, String foundRowIndex)
    {
        Sheet sheet = workbook.getSheet(foundSheetName);
        return Optional.ofNullable(sheet.getRow(Integer.valueOf(foundRowIndex).intValue()));
    }


    public ExcelCellService getExcelCellService()
    {
        return this.excelCellService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    public AttributeNameFormatter<ExcelClassificationAttribute> getAttributeNameFormatter()
    {
        return this.attributeNameFormatter;
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelClassificationAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }


    public ExcelAttributeTranslatorRegistry getExcelAttributeTranslatorRegistry()
    {
        return this.excelAttributeTranslatorRegistry;
    }


    @Required
    public void setExcelAttributeTranslatorRegistry(ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry)
    {
        this.excelAttributeTranslatorRegistry = excelAttributeTranslatorRegistry;
    }
}
