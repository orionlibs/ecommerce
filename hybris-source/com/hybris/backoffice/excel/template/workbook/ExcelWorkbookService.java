package com.hybris.backoffice.excel.template.workbook;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;

public interface ExcelWorkbookService
{
    Workbook createWorkbook(@WillNotClose InputStream paramInputStream);


    Sheet getMetaInformationSheet(@WillNotClose Workbook paramWorkbook);


    void addProperty(@WillNotClose Workbook paramWorkbook, @Nonnull String paramString1, @Nonnull String paramString2);


    Optional<String> getProperty(@WillNotClose Workbook paramWorkbook, @Nonnull String paramString);


    Collection<CTProperty> getUnderlyingProperties(@WillNotClose Workbook paramWorkbook);
}
