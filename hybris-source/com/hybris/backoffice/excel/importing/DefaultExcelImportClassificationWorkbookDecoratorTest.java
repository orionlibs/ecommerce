package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.classification.ExcelClassificationAttributeFactory;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.fest.assertions.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelImportClassificationWorkbookDecoratorTest
{
    private static final String DIMENSIONS_FULLNAME = "Electronical Goods.Dimensions - SampleClassification/1.0";
    private static final String WEIGHT_FULL_NAME = "Electronical Goods.Weight[en] - SampleClassification/1.0";
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private ExcelCellService excelCellService;
    @Mock
    private ClassificationSystemService classificationSystemService;
    @Mock
    private ExcelClassificationTypeSystemService excelClassificationTypeSystemService;
    @Mock
    private ExcelClassificationAttributeFactory excelClassificationAttributeFactory;
    @InjectMocks
    private DefaultExcelImportClassificationWorkbookDecorator defaultExcelImportClassificationWorkbookDecorator;
    @Mock
    private ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem classificationTypeSystem;


    @Before
    public void setup()
    {
        BDDMockito.given(this.productSheet.getWorkbook()).willReturn(this.workbook);
        BDDMockito.given(this.excelClassificationTypeSystemService.loadTypeSystem(this.workbook)).willReturn(this.classificationTypeSystem);
        ClassificationTypeSystemRow firstTypeSystemRow = new ClassificationTypeSystemRow();
        firstTypeSystemRow.setFullName("Electronical Goods.Dimensions - SampleClassification/1.0");
        firstTypeSystemRow.setClassificationSystem("SampleClassification");
        firstTypeSystemRow.setClassificationVersion("1.0");
        firstTypeSystemRow.setClassificationClass("electronics");
        firstTypeSystemRow.setClassificationAttribute("dimensions");
        firstTypeSystemRow.setLocalized(false);
        firstTypeSystemRow.setIsoCode("");
        firstTypeSystemRow.setMandatory(true);
        BDDMockito.given(this.classificationTypeSystem.findRow(firstTypeSystemRow.getFullName())).willReturn(Optional.of(firstTypeSystemRow));
        ClassificationTypeSystemRow secondTypeSystemRow = new ClassificationTypeSystemRow();
        secondTypeSystemRow.setFullName("Electronical Goods.Weight[en] - SampleClassification/1.0");
        secondTypeSystemRow.setClassificationSystem("SampleClassification");
        secondTypeSystemRow.setClassificationVersion("1.0");
        secondTypeSystemRow.setClassificationClass("electronics");
        secondTypeSystemRow.setClassificationAttribute("weight");
        secondTypeSystemRow.setLocalized(true);
        secondTypeSystemRow.setIsoCode("en");
        secondTypeSystemRow.setMandatory(false);
        BDDMockito.given(this.classificationTypeSystem.findRow(secondTypeSystemRow.getFullName())).willReturn(Optional.of(secondTypeSystemRow));
        Matcher<String> matcher = Matchers.not((Matcher)Matchers.anyOf(Matchers.equalTo(secondTypeSystemRow.getFullName()),
                        Matchers.equalTo(firstTypeSystemRow.getFullName())));
        Object object = new Object(this, matcher);
        BDDMockito.given(this.classificationTypeSystem.findRow((String)ArgumentMatchers.argThat((ArgumentMatcher)object))).willReturn(Optional.empty());
    }


    private void mockCell(Row row, int index, String cellValue)
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(row.getCell(index)).willReturn(cell);
        BDDMockito.given(this.excelCellService.getCellValue(cell)).willReturn(cellValue);
    }


    @Test
    public void shouldFindClassificationColumnsInSheet()
    {
        Row headerRow = (Row)Mockito.mock(Row.class);
        BDDMockito.given(this.productSheet.getRow(0)).willReturn(headerRow);
        BDDMockito.given(Short.valueOf(headerRow.getFirstCellNum())).willReturn(Short.valueOf((short)0));
        BDDMockito.given(Short.valueOf(headerRow.getLastCellNum())).willReturn(Short.valueOf((short)3));
        mockCell(headerRow, 0, "Article Number*^");
        mockCell(headerRow, 1, "Catalog version*^");
        mockCell(headerRow, 2, "Electronical Goods.Dimensions - SampleClassification/1.0");
        mockCell(headerRow, 3, "Electronical Goods.Weight[en] - SampleClassification/1.0");
        ClassificationSystemVersionModel systemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(this.classificationSystemService.getSystemVersion("SampleClassification", "1.0")).willReturn(systemVersionModel);
        ClassificationClassModel classificationClassModel = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(this.classificationSystemService.getClassForCode(systemVersionModel, "electronics")).willReturn(classificationClassModel);
        ClassAttributeAssignmentModel dimensionsAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeModel dimensionsAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        ClassAttributeAssignmentModel weightAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeModel weightAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(classificationClassModel.getDeclaredClassificationAttributeAssignments())
                        .willReturn(Arrays.asList(new ClassAttributeAssignmentModel[] {dimensionsAssignment, weightAssignment}));
        BDDMockito.given(dimensionsAssignment.getClassificationAttribute()).willReturn(dimensionsAttribute);
        BDDMockito.given(weightAssignment.getClassificationAttribute()).willReturn(weightAttribute);
        BDDMockito.given(dimensionsAttribute.getCode()).willReturn("dimensions");
        BDDMockito.given(weightAttribute.getCode()).willReturn("weight");
        BDDMockito.given(this.excelClassificationAttributeFactory.create(dimensionsAssignment, ""))
                        .willReturn(createExcelAttributeWithAssignment(dimensionsAssignment));
        BDDMockito.given(this.excelClassificationAttributeFactory.create(weightAssignment, "en"))
                        .willReturn(createExcelAttributeWithAssignment(weightAssignment));
        BDDMockito.given(this.excelClassificationTypeSystemService.loadTypeSystem(this.workbook)).willReturn(this.classificationTypeSystem);
        Collection<ExcelAttribute> classificationAttributes = this.defaultExcelImportClassificationWorkbookDecorator.getExcelAttributes(this.productSheet);
        Assertions.assertThat(classificationAttributes).hasSize(2);
        Assertions.assertThat(classificationAttributes).onProperty("attributeAssignment").contains(new Object[] {dimensionsAssignment, weightAssignment});
    }


    private ExcelClassificationAttribute createExcelAttributeWithAssignment(ClassAttributeAssignmentModel assignmentModel)
    {
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(assignmentModel);
        return attribute;
    }
}
