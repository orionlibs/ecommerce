package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Deprecated(since = "1808", forRemoval = true)
public interface ExcelTemplateService
{
    Workbook createWorkbook(InputStream paramInputStream);


    Sheet getTypeSystemSheet(Workbook paramWorkbook);


    List<String> getSheetsNames(Workbook paramWorkbook);


    List<Sheet> getSheets(Workbook paramWorkbook);


    String getCellValue(Cell paramCell);


    List<SelectedAttribute> getHeaders(Sheet paramSheet1, Sheet paramSheet2);


    List<SelectedAttributeQualifier> getSelectedAttributesQualifiers(Sheet paramSheet1, Sheet paramSheet2);


    int findColumnIndex(Sheet paramSheet1, Sheet paramSheet2, SelectedAttribute paramSelectedAttribute);


    Sheet createTypeSheet(String paramString, Workbook paramWorkbook);


    String findTypeCodeForSheetName(String paramString, Workbook paramWorkbook);


    String findSheetNameForTypeCode(String paramString, Workbook paramWorkbook);


    void addTypeSheet(String paramString, Workbook paramWorkbook);


    void insertAttributeHeader(Sheet paramSheet, SelectedAttribute paramSelectedAttribute, int paramInt);


    void insertAttributesHeader(Sheet paramSheet, Collection<SelectedAttribute> paramCollection);


    void insertAttributeValue(Cell paramCell, Object paramObject);


    Row createEmptyRow(Sheet paramSheet);


    String getAttributeDisplayName(AttributeDescriptorModel paramAttributeDescriptorModel, String paramString);
}
