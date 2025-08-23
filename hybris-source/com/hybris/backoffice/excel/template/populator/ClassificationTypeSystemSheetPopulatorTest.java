package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.DefaultExcelCellService;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationTypeSystemSheetPopulatorTest
{
    private static final String CLASSIFICATION_ATTRIBUTE_FULL = "classificationAttributeFull";
    @Mock
    ClassificationTypeSystemSheetCompressor mockedCompressor;
    private final ExcelCellService excelCellService = (ExcelCellService)new DefaultExcelCellService();
    @InjectMocks
    ClassificationTypeSystemSheetPopulator populator;


    @Before
    public void setUp()
    {
        this.populator.setExcelCellService(this.excelCellService);
    }


    @Test
    public void shouldPopulateClassificationTypeSystemSheetWithAttributes() throws IOException
    {
        Set<ExcelAttribute> classAttributeAssignmentModels = asSet(new ExcelAttribute[] {(ExcelAttribute)createClassificationAttributeMock()});
        this.populator.setCellValuePopulators((Map)new Object(this, ExcelTemplateConstants.ClassificationTypeSystemColumns.class));
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> rows = Collections.singletonList(new Object(this, ExcelTemplateConstants.ClassificationTypeSystemColumns.class));
        BDDMockito.given(this.mockedCompressor.compress(rows)).willReturn(rows);
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        try
        {
            this.populator.populate(createExcelExportResultWithSelectedAttributes((Workbook)xSSFWorkbook, classAttributeAssignmentModels));
            Sheet classificationTypeSystemSheet = xSSFWorkbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName());
            Assertions.assertThat((Iterable)classificationTypeSystemSheet).isNotNull();
            Row firstRow = classificationTypeSystemSheet.getRow(0);
            Assertions.assertThat(getCellValue(firstRow, ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME)).isEqualTo("classificationAttributeFull");
            Row secondRow = classificationTypeSystemSheet.getRow(1);
            Assertions.assertThat((Iterable)secondRow).isNull();
            Row thirdRow = classificationTypeSystemSheet.getRow(2);
            Assertions.assertThat((Iterable)thirdRow).isNull();
            xSSFWorkbook.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                xSSFWorkbook.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    private ExcelClassificationAttribute createClassificationAttributeMock()
    {
        ClassificationSystemVersionModel classificationSystemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        Mockito.lenient().when(classificationSystemVersionModel.getVersion()).thenReturn("classificationSystemVersion");
        Mockito.lenient().when(classificationSystemVersionModel.getCategorySystemID()).thenReturn("classificationSystemId");
        ClassificationClassModel classificationClassModel = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        Mockito.lenient().when(classificationClassModel.getName(Locale.ENGLISH)).thenReturn("classificationClassEnglish");
        Mockito.lenient().when(classificationClassModel.getName(Locale.GERMAN)).thenReturn("classificationClassGerman");
        ClassificationAttributeModel classificationAttributeModel = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        Mockito.lenient().when(classificationAttributeModel.getName(Locale.ENGLISH)).thenReturn("classificationAttributeNameEnglish");
        Mockito.lenient().when(classificationAttributeModel.getName(Locale.GERMAN)).thenReturn("classificationAttributeNameGerman");
        Mockito.lenient().when(classificationAttributeModel.getSystemVersion()).thenReturn(classificationSystemVersionModel);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.lenient().when(classAttributeAssignmentModel.getClassificationAttribute()).thenReturn(classificationAttributeModel);
        Mockito.lenient().when(classAttributeAssignmentModel.getClassificationClass()).thenReturn(classificationClassModel);
        Mockito.lenient().when(classAttributeAssignmentModel.getSystemVersion()).thenReturn(classificationSystemVersionModel);
        Mockito.lenient().when(classAttributeAssignmentModel.getLocalized()).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(classAttributeAssignmentModel.getMandatory()).thenReturn(Boolean.valueOf(true));
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignmentModel);
        return attribute;
    }


    private ExcelExportResult createExcelExportResultWithSelectedAttributes(Workbook workbook, Collection<ExcelAttribute> attributes)
    {
        return new ExcelExportResult(workbook, null, null, attributes, attributes);
    }


    private String getCellValue(Row row, ExcelTemplateConstants.ClassificationTypeSystemColumns column)
    {
        return row.getCell(column.getIndex()).getStringCellValue();
    }


    @SafeVarargs
    private static <T> Set<T> asSet(T... elements)
    {
        return new HashSet<>(Arrays.asList(elements));
    }
}
