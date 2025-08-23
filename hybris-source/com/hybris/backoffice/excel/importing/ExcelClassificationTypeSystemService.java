package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationTypeSystemService implements ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem>
{
    private static final String LANG_GROUP_NAME = "lang";
    private static final Pattern LANG_EXTRACT_PATTERN = Pattern.compile(".+\\[(?<lang>.+)].*");
    private ExcelCellService cellService;
    private CollectionFormatter collectionFormatter;


    public ExcelClassificationTypeSystem loadTypeSystem(Workbook workbook)
    {
        ExcelClassificationTypeSystem classificationTypeSystem = new ExcelClassificationTypeSystem();
        Sheet classificationSheet = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName());
        for(int rowIndex = 1; rowIndex <= classificationSheet.getLastRowNum(); rowIndex++)
        {
            Row row = classificationSheet.getRow(rowIndex);
            List<ClassificationTypeSystemRow> classificationTypeSystemRows = createClassificationTypeSystemRows(row);
            classificationTypeSystemRows
                            .forEach(typeSystemRow -> classificationTypeSystem.putRow(typeSystemRow.getFullName(), typeSystemRow));
        }
        return classificationTypeSystem;
    }


    private List<ClassificationTypeSystemRow> createClassificationTypeSystemRows(Row row)
    {
        List<ClassificationTypeSystemRow> typeSystemRows = new LinkedList<>();
        if(row != null)
        {
            for(String fullName : decompressCellValue(
                            getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME)))
            {
                typeSystemRows.add(createClassificationTypeSystemRow(row, fullName));
            }
        }
        return typeSystemRows;
    }


    private ClassificationTypeSystemRow createClassificationTypeSystemRow(Row row, String fullName)
    {
        ClassificationTypeSystemRow typeSystemRow = new ClassificationTypeSystemRow();
        typeSystemRow.setFullName(fullName);
        typeSystemRow.setClassificationSystem(
                        getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_SYSTEM));
        typeSystemRow.setClassificationVersion(
                        getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_VERSION));
        typeSystemRow.setClassificationClass(
                        getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS));
        typeSystemRow.setClassificationAttribute(
                        getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_ATTRIBUTE));
        typeSystemRow.setLocalized("true"
                        .equalsIgnoreCase(getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOCALIZED)));
        typeSystemRow.setIsoCode(extractIsoCode(fullName));
        typeSystemRow.setMandatory("true"
                        .equalsIgnoreCase(getCellValue(row, ExcelTemplateConstants.ClassificationTypeSystemColumns.MANDATORY)));
        return typeSystemRow;
    }


    private Collection<String> decompressCellValue(String value)
    {
        Set<String> decompressedValues = this.collectionFormatter.formatToCollection(value);
        if(decompressedValues.isEmpty())
        {
            decompressedValues.add(value);
        }
        return decompressedValues;
    }


    private static String extractIsoCode(String fullName)
    {
        Matcher matcher = LANG_EXTRACT_PATTERN.matcher(fullName);
        if(matcher.find())
        {
            return matcher.group("lang");
        }
        return "";
    }


    private String getCellValue(Row row, ExcelTemplateConstants.ClassificationTypeSystemColumns column)
    {
        return this.cellService.getCellValue(row.getCell(column.getIndex()));
    }


    @Required
    public void setCellService(ExcelCellService cellService)
    {
        this.cellService = cellService;
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }
}
