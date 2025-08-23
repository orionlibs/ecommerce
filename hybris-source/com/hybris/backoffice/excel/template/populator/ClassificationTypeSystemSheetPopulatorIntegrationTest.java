package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.testframework.Transactional;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.JUnitSoftAssertions;
import org.fest.assertions.Assertions;
import org.junit.Rule;
import org.junit.Test;

@IntegrationTest
@Transactional
public class ClassificationTypeSystemSheetPopulatorIntegrationTest extends ServicelayerTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    ExcelTemplateService excelTemplateService;
    @Resource
    I18NService i18NService;
    @Resource
    FlexibleSearchService flexibleSearchService;
    @Resource(name = "defaultClassificationTypeSystemSheetPopulator")
    ClassificationTypeSystemSheetPopulator populator;


    @Test
    public void shouldExportClassificationTypeSystemSheetOnTemplate() throws Exception
    {
        loadClassificationImpex();
        String queryForAllClassificationAssignments = "SELECT {ClassAttributeAssignment:PK} FROM {ClassAttributeAssignment}";
        SearchResult<ClassAttributeAssignmentModel> searchResult = this.flexibleSearchService.search("SELECT {ClassAttributeAssignment:PK} FROM {ClassAttributeAssignment}");
        Collection<ExcelAttribute> attributes = new LinkedList<>();
        for(ClassAttributeAssignmentModel assignment : searchResult.getResult())
        {
            for(Locale locale : this.i18NService.getSupportedLocales())
            {
                ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
                attribute.setAttributeAssignment(assignment);
                attribute.setIsoCode(String.valueOf(locale));
                attributes.add(attribute);
            }
        }
        Workbook workbook = this.excelTemplateService.createWorkbook(loadResource("/excel/excelImpExMasterTemplate.xlsx"));
        try
        {
            this.populator.populate(createExcelExportResultWithSelectedAttributes(workbook, attributes));
            Sheet classificationTypeSystemSheet = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName());
            Assertions.assertThat((Iterable)classificationTypeSystemSheet).isNotNull();
            Row firstRow = classificationTypeSystemSheet.getRow(0);
            Assertions.assertThat((Iterable)firstRow).isNotNull();
            this.soft.assertThat(getCellValue(firstRow, 0)).isEqualTo("FullName");
            this.soft.assertThat(getCellValue(firstRow, 1)).isEqualTo("ClassificationSystem");
            this.soft.assertThat(getCellValue(firstRow, 2)).isEqualTo("ClassificationVersion");
            this.soft.assertThat(getCellValue(firstRow, 3)).isEqualTo("ClassificationClass");
            this.soft.assertThat(getCellValue(firstRow, 4)).isEqualTo("ClassificationAttribute");
            this.soft.assertThat(getCellValue(firstRow, 5)).isEqualTo("AttrLocalized");
            this.soft.assertThat(getCellValue(firstRow, 6)).isEqualTo("AttrLocLang");
            this.soft.assertThat(getCellValue(firstRow, 7)).isEqualTo("IsMandatory");
            Row secondRow = classificationTypeSystemSheet.getRow(1);
            Assertions.assertThat((Iterable)secondRow).isNotNull();
            this.soft.assertThat(getCellValue(secondRow, 0)).isEqualTo("{software.manufacturerURL[de] - SampleClassification/1.0},{software.manufacturerURL[en] - SampleClassification/1.0}");
            this.soft.assertThat(getCellValue(secondRow, 1)).isEqualTo("SampleClassification");
            this.soft.assertThat(getCellValue(secondRow, 2)).isEqualTo("1.0");
            this.soft.assertThat(getCellValue(secondRow, 3)).isEqualTo("software");
            this.soft.assertThat(getCellValue(secondRow, 4)).isEqualTo("manufacturerURL");
            this.soft.assertThat(getCellValue(secondRow, 5)).isEqualTo("true");
            this.soft.assertThat(getCellValue(secondRow, 6)).isEqualTo("{de},{en}");
            this.soft.assertThat(getCellValue(secondRow, 7)).isEqualTo("false");
            if(workbook != null)
            {
                workbook.close();
            }
        }
        catch(Throwable throwable)
        {
            if(workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    private void loadClassificationImpex() throws ImpExException
    {
        importStream(loadResource("/test/excel/classificationSystem.csv"), StandardCharsets.UTF_8.name(), "classificationSystem.csv");
    }


    private InputStream loadResource(String path)
    {
        return (InputStream)Optional.<InputStream>ofNullable(getClass().getResourceAsStream(path))
                        .orElseThrow(() -> new AssertionError("Could not load resource: " + path));
    }


    private ExcelExportResult createExcelExportResultWithSelectedAttributes(Workbook workbook, Collection<ExcelAttribute> attributes)
    {
        return new ExcelExportResult(workbook, null, null, attributes, attributes);
    }


    private String getCellValue(Row row, int i)
    {
        return row.getCell(i).getStringCellValue();
    }
}
