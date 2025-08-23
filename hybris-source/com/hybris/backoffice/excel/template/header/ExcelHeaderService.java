package com.hybris.backoffice.excel.template.header;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelHeaderService
{
    Collection<SelectedAttribute> getHeaders(Sheet paramSheet1, Sheet paramSheet2);


    Collection<String> getHeaderDisplayNames(Sheet paramSheet);


    Collection<SelectedAttributeQualifier> getSelectedAttributesQualifiers(Sheet paramSheet1, Sheet paramSheet2);


    void insertAttributeHeader(Sheet paramSheet, ExcelAttribute paramExcelAttribute, int paramInt);


    void insertAttributesHeader(Sheet paramSheet, Collection<? extends ExcelAttribute> paramCollection);


    default String getHeaderValueWithoutSpecialMarks(@Nonnull String headerValue)
    {
        return headerValue.replaceAll("[" + ExcelTemplateConstants.SpecialMark.getMergedMarks() + "]", "");
    }
}
