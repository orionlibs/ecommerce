package com.hybris.backoffice.excel.template.workbook;

import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExcelWorkbookService implements ExcelWorkbookService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExcelWorkbookService.class);
    private ExcelTemplateConstants.UtilitySheet metaInformationSheet = ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM;


    public Sheet getMetaInformationSheet(@WillNotClose Workbook workbook)
    {
        return workbook.getSheet(this.metaInformationSheet.getSheetName());
    }


    public Workbook createWorkbook(@WillNotClose InputStream is)
    {
        try
        {
            return (Workbook)new XSSFWorkbook(is);
        }
        catch(IOException ex)
        {
            LOG.error("Cannot read excel template. Returning empty workbook", ex);
            return (Workbook)new XSSFWorkbook();
        }
    }


    public void addProperty(@WillNotClose Workbook workbook, @Nonnull String key, @Nonnull String value)
    {
        getProperties(workbook).ifPresent(customProperties -> customProperties.addProperty(key, value));
    }


    public Optional<String> getProperty(@WillNotClose Workbook workbook, @Nonnull String key)
    {
        return getProperties(workbook)
                        .map(customProperties -> customProperties.getProperty(key))
                        .map(CTProperty::getLpwstr);
    }


    public Collection<CTProperty> getUnderlyingProperties(Workbook workbook)
    {
        return getProperties(workbook)
                        .<Collection<CTProperty>>map(customProperties -> customProperties.getUnderlyingProperties().getPropertyList())
                        .orElse(Collections.emptyList());
    }


    protected Optional<POIXMLProperties.CustomProperties> getProperties(Workbook workbook)
    {
        Objects.requireNonNull(XSSFWorkbook.class);
        Objects.requireNonNull(XSSFWorkbook.class);
        return Optional.<Workbook>ofNullable(workbook).filter(XSSFWorkbook.class::isInstance).map(XSSFWorkbook.class::cast)
                        .map(POIXMLDocument::getProperties)
                        .map(POIXMLProperties::getCustomProperties);
    }


    public void setMetaInformationSheet(ExcelTemplateConstants.UtilitySheet metaInformationSheet)
    {
        this.metaInformationSheet = metaInformationSheet;
    }
}
