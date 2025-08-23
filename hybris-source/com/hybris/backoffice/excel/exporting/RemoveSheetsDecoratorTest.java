package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveSheetsDecoratorTest
{
    private final RemoveSheetsDecorator removeSheetsDecorator = new RemoveSheetsDecorator();
    private Workbook workbook;


    @Before
    public void setup()
    {
        this.workbook = (Workbook)new XSSFWorkbook();
        this.workbook.createSheet(ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM.getSheetName());
        this.workbook.createSheet(ExcelTemplateConstants.UtilitySheet.PK.getSheetName());
        this.workbook.createSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName());
    }


    @Test
    public void shouldRemovePkSheet()
    {
        List<ExcelTemplateConstants.UtilitySheet> sheetsToRemove = Lists.newArrayList((Object[])new ExcelTemplateConstants.UtilitySheet[] {ExcelTemplateConstants.UtilitySheet.PK});
        this.removeSheetsDecorator.setSheetsToRemove(sheetsToRemove);
        this.removeSheetsDecorator.decorate(new ExcelExportResult(this.workbook, Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList()));
        Assertions.assertThat(this.workbook.getNumberOfSheets()).isEqualTo(2);
        Assertions.assertThat((Iterable)this.workbook.getSheet(ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM.getSheetName())).isNotNull();
        Assertions.assertThat((Iterable)this.workbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName())).isNotNull();
        Assertions.assertThat((Iterable)this.workbook.getSheet(ExcelTemplateConstants.UtilitySheet.PK.getSheetName())).isNull();
    }
}
