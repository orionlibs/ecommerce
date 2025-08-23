package com.hybris.backoffice.excel.translators.classification;

import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.UnitExcelParserSplitter;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.collections4.ListUtils;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractClassificationAttributeTranslatorTest
{
    @Mock
    private ExcelClassificationAttribute excelClassificationAttribute;
    @Mock
    private ClassificationSystemService classificationSystemService;
    @Spy
    private AbstractClassificationAttributeTranslator abstractClassificationAttributeTranslator;


    @Before
    public void setUp()
    {
        BDDMockito.given(this.abstractClassificationAttributeTranslator.getClassificationSystemService()).willReturn(this.classificationSystemService);
    }


    @Test
    public void shouldImportDataIfImportedValueIsNull()
    {
        ImportParameters anyImportParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelImportContext anyExcelImportContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(assignment);
        Mockito.lenient().when(this.abstractClassificationAttributeTranslator.importValue(this.excelClassificationAttribute, anyImportParameters, new ExcelImportContext()))
                        .thenReturn(null);
        Impex result = this.abstractClassificationAttributeTranslator.importData((ExcelAttribute)this.excelClassificationAttribute, anyImportParameters, anyExcelImportContext);
        Assertions.assertThat(result.getImpexes()).isEmpty();
    }


    @Test
    public void shouldImportExcelClassificationAttributeOnly()
    {
        ExcelAttribute notAClassificationAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters anyImportParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelImportContext anyExcelImportContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        Impex result = this.abstractClassificationAttributeTranslator.importData(notAClassificationAttribute, anyImportParameters, anyExcelImportContext);
        Assertions.assertThat(result.getImpexes()).isEmpty();
        ((AbstractClassificationAttributeTranslator)Mockito.verify(this.abstractClassificationAttributeTranslator, Mockito.never())).importValue((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (ExcelImportContext)Matchers.any());
    }


    @Test
    public void shouldImportValueAndPutItToImpex()
    {
        String typeCode = "typeCode";
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(importParameters.getTypeCode()).willReturn("typeCode");
        ExcelImportContext excelImportContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(assignment);
        ((AbstractClassificationAttributeTranslator)Mockito.doReturn(new ImpexValue("value", (new ImpexHeaderValue.Builder("header")).build()))
                        .when(this.abstractClassificationAttributeTranslator))
                        .importValue(this.excelClassificationAttribute, importParameters, excelImportContext);
        Impex result = this.abstractClassificationAttributeTranslator.importData((ExcelAttribute)this.excelClassificationAttribute, importParameters, excelImportContext);
        ((AbstractClassificationAttributeTranslator)Mockito.verify(this.abstractClassificationAttributeTranslator)).importValue(this.excelClassificationAttribute, importParameters, excelImportContext);
        Assertions.assertThat(result.findUpdates("typeCode").getImpexTable().cellSet())
                        .hasSize(1)
                        .hasOnlyOneElementSatisfying(cell -> {
                            Assertions.assertThat(cell.getValue()).isEqualTo("value");
                            ((AbstractObjectAssert)Assertions.assertThat(cell.getColumnKey()).isNotNull()).extracting(new String[] {"name"}).containsOnly(new Object[] {"header"});
                            Assertions.assertThat((Integer)cell.getRowKey()).isZero();
                        });
    }


    @Test
    public void shouldCalculateReferenceFormatForAttributeWithUnit()
    {
        String unitType = "unitType";
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeUnitModel unit = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        List<ClassificationAttributeUnitModel> unitsForUnitType = Arrays.asList(new ClassificationAttributeUnitModel[] {createUnit("kg", 0.001D), createUnit("g", 1.0D)});
        ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        BDDMockito.given(classAttributeAssignment.getUnit()).willReturn(unit);
        BDDMockito.given(unit.getSystemVersion()).willReturn(classificationSystemVersion);
        BDDMockito.given(unit.getUnitType()).willReturn("unitType");
        BDDMockito.given(this.classificationSystemService.getUnitsOfTypeForSystemVersion(classificationSystemVersion, "unitType"))
                        .willReturn(unitsForUnitType);
        String referenceFormat = this.abstractClassificationAttributeTranslator.referenceFormat(this.excelClassificationAttribute);
        Assertions.assertThat(referenceFormat).isEqualTo("value:unit[g,kg]");
    }


    @Test
    public void shouldCalculateReferenceFormatForAttributeWithUnitWithNoType()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeUnitModel unit = createUnit("g", 1.0D);
        ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        BDDMockito.given(classAttributeAssignment.getUnit()).willReturn(unit);
        Mockito.lenient().when(unit.getSystemVersion()).thenReturn(classificationSystemVersion);
        String referenceFormat = this.abstractClassificationAttributeTranslator.referenceFormat(this.excelClassificationAttribute);
        Assertions.assertThat(referenceFormat).isEqualTo("value:unit[g]");
    }


    @Test
    public void shouldMultiValueBeSplitNTimesForSubtranslator()
    {
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getMultiValued()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(assignment);
        ImportParameters imp1 = createImportParameters("imp1", "imp1");
        ImportParameters imp2 = createImportParameters("imp2", "imp2");
        ImportParameters imp3 = createImportParameters("imp3", "imp3");
        ImportParameters imp = mergeImportParametersForMultiValueCase(new ImportParameters[] {imp1, imp2, imp3});
        ((AbstractClassificationAttributeTranslator)Mockito.doAnswer(invocationOnMock -> {
            Serializable cellValue = ((ImportParameters)invocationOnMock.getArguments()[1]).getCellValue();
            return new ImpexValue(cellValue, (new ImpexHeaderValue.Builder("any")).withUnique(true).build());
        }).when(this.abstractClassificationAttributeTranslator)).importValue((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (ExcelImportContext)Matchers.any());
        Impex result = this.abstractClassificationAttributeTranslator.importData((ExcelAttribute)this.excelClassificationAttribute, imp, null);
        ((AbstractClassificationAttributeTranslator)BDDMockito.then(this.abstractClassificationAttributeTranslator).should(Mockito.times(3))).importValue((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (ExcelImportContext)Matchers.any());
        Assertions.assertThat(((ImpexForType)result.getImpexes().get(0)).getImpexTable().cellSet())
                        .hasSize(1)
                        .hasOnlyOneElementSatisfying(cell -> Assertions.assertThat(cell.getValue()).isEqualTo(imp.getCellValue()));
    }


    @Test
    public void shouldSubtranslatorRetrieveValueWithoutUnit()
    {
        this.abstractClassificationAttributeTranslator.setExcelParserSplitter((ExcelParserSplitter)new UnitExcelParserSplitter());
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getUnit()).willReturn(Mockito.mock(ClassificationAttributeUnitModel.class));
        BDDMockito.given(this.excelClassificationAttribute.getAttributeAssignment()).willReturn(assignment);
        String cellValue = "abc";
        String unit = "kg";
        String cellValueWithUnit = "abc:kg";
        ImportParameters importParameters = createImportParameters("abc:kg", "abc:kg");
        ((AbstractClassificationAttributeTranslator)Mockito.doAnswer(invocationOnMock -> {
            Serializable val = ((ImportParameters)invocationOnMock.getArguments()[1]).getCellValue();
            return new ImpexValue(val, (new ImpexHeaderValue.Builder("any")).withUnique(true).build());
        }).when(this.abstractClassificationAttributeTranslator)).importSingle((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (ExcelImportContext)Matchers.any());
        ImpexValue result = this.abstractClassificationAttributeTranslator.importValue(this.excelClassificationAttribute, importParameters, null);
        ((AbstractClassificationAttributeTranslator)BDDMockito.then(this.abstractClassificationAttributeTranslator).should()).importSingle((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.argThat((ArgumentMatcher)new Object(this)),
                        (ExcelImportContext)Matchers.any());
        Assertions.assertThat(result.getValue()).isEqualTo("abc:kg");
    }


    protected ImportParameters createImportParameters(String cellValue, String rawValue)
    {
        return new ImportParameters(null, null, cellValue, null,
                        Lists.newArrayList((Object[])new Map[] {(Map)ImmutableMap.of("rawValue", rawValue)}));
    }


    protected ImportParameters mergeImportParametersForMultiValueCase(ImportParameters... importParameters)
    {
        return Stream.<ImportParameters>of(importParameters).reduce((imp1, imp2) -> {
            String cellValue = "" + imp1.getCellValue() + "," + imp1.getCellValue();
            List<Map<String, String>> params = ListUtils.union(imp1.getMultiValueParameters(), imp2.getMultiValueParameters());
            return new ImportParameters(null, null, cellValue, null, params);
        }).get();
    }


    protected ClassificationAttributeUnitModel createUnit(String code, double conversionFactor)
    {
        ClassificationAttributeUnitModel unit = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        BDDMockito.given(unit.getCode()).willReturn(code);
        BDDMockito.given(unit.getConversionFactor()).willReturn(Double.valueOf(conversionFactor));
        return unit;
    }
}
