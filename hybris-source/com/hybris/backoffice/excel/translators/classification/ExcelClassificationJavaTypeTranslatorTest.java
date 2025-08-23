package com.hybris.backoffice.excel.translators.classification;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.OptionalAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationJavaTypeTranslatorTest
{
    private static final ExcelImportContext EMPTY_CTX = new ExcelImportContext();
    @Mock
    private ClassAttributeAssignmentModel classAttributeAssignment;
    @Mock
    private ExcelClassificationAttribute excelAttribute;
    @Mock
    private ProductModel productToExport;
    @Mock
    private LocalizedFeature feature;
    @Mock
    private ClassificationAttributeHeaderValueCreator classificationAttributeHeaderValueCreator;
    @Mock
    private ExcelDateUtils excelDateUtils;
    @Mock
    private ClassificationService classificationService;
    @Mock
    private ClassificationSystemService classificationSystemService;
    @Mock
    private CommonI18NService commonI18NService;
    @InjectMocks
    private ExcelClassificationJavaTypeTranslator translator;


    @Before
    public void setUp()
    {
        ((ExcelClassificationAttribute)Mockito.doReturn(this.classAttributeAssignment).when(this.excelAttribute)).getAttributeAssignment();
        ((LocalizedFeature)Mockito.doReturn(this.classAttributeAssignment).when(this.feature)).getClassAttributeAssignment();
        ((ClassificationService)Mockito.doReturn(this.feature).when(this.classificationService)).getFeature(this.productToExport, this.classAttributeAssignment);
        Mockito.when(this.excelDateUtils.exportDate((Date)Matchers.any())).thenAnswer(invocationOnMock -> {
            Date date = (Date)invocationOnMock.getArguments()[0];
            return DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.of("UTC")).format(date.toInstant());
        });
        Mockito.lenient().when(this.excelDateUtils.importDate((String)Matchers.any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }


    @Test
    public void testCanHandle()
    {
        Set<ClassificationAttributeTypeEnum> supportedTypes = EnumSet.of(ClassificationAttributeTypeEnum.NUMBER, ClassificationAttributeTypeEnum.STRING, ClassificationAttributeTypeEnum.BOOLEAN, ClassificationAttributeTypeEnum.DATE);
        Set<ClassificationAttributeTypeEnum> unsupportedTypes = Sets.newHashSet((Object[])ClassificationAttributeTypeEnum.values());
        unsupportedTypes.removeAll(supportedTypes);
        supportedTypes.forEach(type -> {
            ((ClassAttributeAssignmentModel)Mockito.doReturn(type).when(this.classAttributeAssignment)).getAttributeType();
            boolean result = this.translator.canHandle(this.excelAttribute);
            ((AbstractBooleanAssert)Assertions.assertThat(result).as("Should be able to handle type: " + type, new Object[0])).isTrue();
        });
        unsupportedTypes.forEach(type -> {
            ((ClassAttributeAssignmentModel)Mockito.doReturn(type).when(this.classAttributeAssignment)).getAttributeType();
            boolean result = this.translator.canHandle(this.excelAttribute);
            ((AbstractBooleanAssert)Assertions.assertThat(result).as("Should not handle type: " + type, new Object[0])).isFalse();
        });
    }


    @Test
    public void shouldImportFeatureValues()
    {
        Object[][] testData = {{Long.valueOf(123L), "Identifier", ClassificationAttributeTypeEnum.NUMBER, Long.valueOf(123L)}, {"A description of a product.", "Description", ClassificationAttributeTypeEnum.STRING, "A description of a product."},
                        {Boolean.valueOf(true), "Approved", ClassificationAttributeTypeEnum.BOOLEAN, Boolean.valueOf(true)}, {"2018-03-04", "Identifier", ClassificationAttributeTypeEnum.DATE, "2018-03-04"}, {"123:kg", "Identifier", ClassificationAttributeTypeEnum.NUMBER, "123:kg"},
                        {"3:g,8kg,12t", "Identifier", ClassificationAttributeTypeEnum.NUMBER, "3:g,8kg,12t"}, {"one,two,three", "Identifier", ClassificationAttributeTypeEnum.STRING, "one,two,three"},
                        {"2018-03-04,2018-03-05", "Identifier", ClassificationAttributeTypeEnum.DATE, "2018-03-04,2018-03-05"}};
        for(Object[] testDataRow : testData)
        {
            Serializable excelCellValue = (Serializable)testDataRow[0];
            String attributeHeader = (String)testDataRow[1];
            ClassificationAttributeTypeEnum attributeType = (ClassificationAttributeTypeEnum)testDataRow[2];
            Serializable expectedOutput = (Serializable)testDataRow[3];
            BDDMockito.given(this.classificationAttributeHeaderValueCreator.create((ExcelClassificationAttribute)Matchers.eq(this.excelAttribute), (ExcelImportContext)Matchers.any())).willReturn(attributeHeader);
            ImportParameters importParameters = new ImportParameters("Product", null, excelCellValue, null, Collections.emptyList());
            Impex impex = this.translator.importData((ExcelAttribute)this.excelAttribute, importParameters, EMPTY_CTX);
            Assertions.assertThat(impex.findUpdates("Product").getImpexTable().cellSet())
                            .hasSize(1)
                            .hasOnlyOneElementSatisfying(element -> {
                                Assertions.assertThat(element.getValue()).isEqualTo(expectedOutput);
                                ((AbstractObjectAssert)Assertions.assertThat(element.getColumnKey()).isNotNull()).extracting(new String[] {"name"}).containsOnly(new Object[] {attributeHeader});
                            });
        }
    }


    @Test
    public void shouldExportFeatureValues()
    {
        for(TestData testData : createListOfTestData())
        {
            initializeTest(testData);
            Optional<String> result = this.translator.exportData(this.excelAttribute, this.productToExport);
            ((OptionalAssert)Assertions.assertThat(result).as("Should export for test data: " + testData, new Object[0])).isPresent();
            ((AbstractCharSequenceAssert)Assertions.assertThat(result.get()).as("Should export for test data: " + testData, new Object[0])).isEqualTo(testData.getExpectedOutput());
        }
    }


    private List<TestData> createListOfTestData()
    {
        List<TestData> listOfTestData = new ArrayList<>();
        TestData singleInteger = new TestData();
        singleInteger.addFeatureValue(Integer.valueOf(1));
        singleInteger.setExpectedOutput("1");
        listOfTestData.add(singleInteger);
        TestData singleDouble = new TestData();
        singleDouble.addFeatureValue(Double.valueOf(1.1D));
        singleDouble.setExpectedOutput("1.1");
        listOfTestData.add(singleDouble);
        TestData singleBoolean = new TestData();
        singleBoolean.addFeatureValue(Boolean.valueOf(true));
        singleBoolean.setExpectedOutput("true");
        listOfTestData.add(singleBoolean);
        TestData singleString = new TestData();
        singleString.addFeatureValue("Test string.");
        singleString.setExpectedOutput("Test string.");
        listOfTestData.add(singleString);
        TestData singleDate = new TestData();
        singleDate.addFeatureValue(createDate(2018, 4, 23));
        singleDate.setExpectedOutput("2018-04-23");
        listOfTestData.add(singleDate);
        TestData singleDoubleWithUnit = new TestData();
        singleDoubleWithUnit.addFeatureValueWithUnit(Integer.valueOf(1), "kg");
        singleDoubleWithUnit.setExpectedOutput("1:kg");
        listOfTestData.add(singleDoubleWithUnit);
        TestData multiInteger = new TestData();
        multiInteger.addFeatureValue(Integer.valueOf(1));
        multiInteger.addFeatureValue(Integer.valueOf(5));
        multiInteger.addFeatureValue(Integer.valueOf(7));
        multiInteger.setExpectedOutput("1,5,7");
        listOfTestData.add(multiInteger);
        TestData multiDouble = new TestData();
        multiDouble.addFeatureValue(Double.valueOf(1.1D));
        multiDouble.addFeatureValue(Double.valueOf(5.3D));
        multiDouble.addFeatureValue(Double.valueOf(67.23D));
        multiDouble.setExpectedOutput("1.1,5.3,67.23");
        listOfTestData.add(multiDouble);
        TestData multiDate = new TestData();
        multiDate.addFeatureValue(createDate(2017, 3, 9));
        multiDate.addFeatureValue(createDate(2018, 4, 23));
        multiDate.setExpectedOutput("2017-03-09,2018-04-23");
        listOfTestData.add(multiDate);
        TestData multiIntegerWithUnit = new TestData();
        multiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(1), "g");
        multiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(5), "kg");
        multiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(7), "t");
        multiIntegerWithUnit.setExpectedOutput("1:g,5:kg,7:t");
        listOfTestData.add(multiIntegerWithUnit);
        TestData localizedMultiIntegerWithUnit = new TestData();
        localizedMultiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(1), "g");
        localizedMultiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(5), "kg");
        localizedMultiIntegerWithUnit.addFeatureValueWithUnit(Integer.valueOf(7), "t");
        localizedMultiIntegerWithUnit.setLang("en");
        localizedMultiIntegerWithUnit.setExpectedOutput("1:g,5:kg,7:t");
        listOfTestData.add(localizedMultiIntegerWithUnit);
        TestData localizedMultiDoubleWithUnit = new TestData();
        localizedMultiDoubleWithUnit.addFeatureValueWithUnit(Double.valueOf(1.7D), "g");
        localizedMultiDoubleWithUnit.addFeatureValueWithUnit(Double.valueOf(55.2D), "kg");
        localizedMultiDoubleWithUnit.addFeatureValueWithUnit(Double.valueOf(70.123D), "t");
        localizedMultiDoubleWithUnit.setLang("de");
        localizedMultiDoubleWithUnit.setExpectedOutput("1.7:g,55.2:kg,70.123:t");
        listOfTestData.add(localizedMultiDoubleWithUnit);
        TestData localizedMultiString = new TestData();
        localizedMultiString.addFeatureValue("first");
        localizedMultiString.addFeatureValue("second");
        localizedMultiString.addFeatureValue("third");
        localizedMultiString.setLang("fr");
        localizedMultiString.setExpectedOutput("first,second,third");
        listOfTestData.add(localizedMultiString);
        return listOfTestData;
    }


    private Date createDate(int year, int month, int dayOfMonth)
    {
        return Date.from(LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0).toInstant(ZoneOffset.UTC));
    }


    private void initializeTest(TestData testData)
    {
        ((ClassAttributeAssignmentModel)Mockito.doReturn(Boolean.valueOf(testData.isMultiValued())).when(this.classAttributeAssignment)).getMultiValued();
        ((ClassAttributeAssignmentModel)Mockito.doReturn(Boolean.valueOf(testData.isLocalized())).when(this.classAttributeAssignment)).getLocalized();
        ((ExcelClassificationAttribute)Mockito.doReturn(testData.getLang()).when(this.excelAttribute)).getIsoCode();
        ((CommonI18NService)Mockito.doReturn(testData.getLocale()).when(this.commonI18NService)).getLocaleForIsoCode(testData.getLang());
        if(testData.isLocalized())
        {
            if(testData.isMultiValued())
            {
                ((LocalizedFeature)Mockito.doReturn(testData.getFeatureValues()).when(this.feature)).getValues(testData.getLocale());
            }
            else
            {
                ((LocalizedFeature)Mockito.doReturn(testData.getFeatureValues().get(0)).when(this.feature)).getValue(testData.getLocale());
            }
        }
        else if(testData.isMultiValued())
        {
            ((LocalizedFeature)Mockito.doReturn(testData.getFeatureValues()).when(this.feature)).getValues();
        }
        else
        {
            ((LocalizedFeature)Mockito.doReturn(testData.getFeatureValues().get(0)).when(this.feature)).getValue();
        }
    }


    @Test
    public void shouldReturnReferencePatternForDateFromExcelDateUtils()
    {
        BDDMockito.given(this.classAttributeAssignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.DATE);
        BDDMockito.given(this.excelDateUtils.getDateTimeFormat()).willReturn("dd.MM.yyyy HH:mm:s");
        this.translator.referenceFormat(this.excelAttribute);
        ((ExcelDateUtils)Mockito.verify(this.excelDateUtils)).getDateTimeFormat();
    }
}
