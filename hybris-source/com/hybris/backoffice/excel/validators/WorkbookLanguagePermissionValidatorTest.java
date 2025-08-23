package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.importing.ExcelAttributeTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelClassificationTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelTypeSystemService;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookLanguagePermissionValidatorTest
{
    @Mock
    private ExcelHeaderService excelHeaderService;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelTypeSystemService excelTypeSystemService;
    @Mock
    private ExcelClassificationTypeSystemService excelClassificationTypeSystemService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private ServicelayerManager servicelayerManager;
    @Mock
    private ModelService modelService;
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private Sheet categorySheet;
    @Spy
    @InjectMocks
    private WorkbookLanguagePermissionValidator validator;
    private static final String ENGLISH_ISO_CODE = "en";
    private static final String GERMAN_ISO_CODE = "de";
    private final Language enLanguage = (Language)Mockito.mock(Language.class);
    private final LanguageModel enLangModel = (LanguageModel)Mockito.mock(LanguageModel.class);
    private final Locale enLocale = new Locale("en");
    private final Locale deLocale = new Locale("de");
    private final List<String> productAttributeNames = new ArrayList<>();
    private final List<String> categoryAttributeNames = new ArrayList<>();
    private final ExcelAttributeTypeSystemService.ExcelTypeSystem excelTypeSystem = (ExcelAttributeTypeSystemService.ExcelTypeSystem)Mockito.mock(ExcelAttributeTypeSystemService.ExcelTypeSystem.class);
    private final ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem excelClassificationTypeSystem = (ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem)Mockito.mock(ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem.class);


    @Before
    public void setup()
    {
        Mockito.when(this.excelSheetService.getSheets(this.workbook)).thenReturn(Arrays.asList(new Sheet[] {this.productSheet, this.categorySheet}));
        Mockito.when(this.productSheet.getWorkbook()).thenReturn(this.workbook);
        Mockito.when(this.categorySheet.getWorkbook()).thenReturn(this.workbook);
        Mockito.when(this.excelTypeSystemService.loadTypeSystem(this.workbook)).thenReturn(this.excelTypeSystem);
        Mockito.when(this.excelClassificationTypeSystemService.loadTypeSystem(this.workbook)).thenReturn(this.excelClassificationTypeSystem);
        Mockito.when(this.excelTypeSystem.findRow((String)Matchers.any())).thenReturn(Optional.empty());
        Mockito.when(this.excelClassificationTypeSystem.findRow((String)Matchers.any())).thenReturn(Optional.empty());
        Mockito.when(this.validator.getServicelayerManager()).thenReturn(this.servicelayerManager);
        Mockito.when(this.excelHeaderService.getHeaderDisplayNames(this.productSheet)).thenReturn(this.productAttributeNames);
        Mockito.when(this.excelHeaderService.getHeaderDisplayNames(this.categorySheet)).thenReturn(this.categoryAttributeNames);
        Mockito.when(this.modelService.get(this.enLanguage)).thenReturn(this.enLangModel);
        Mockito.when(this.commonI18NService.getLocaleForLanguage(this.enLangModel)).thenReturn(this.enLocale);
        Mockito.when(this.commonI18NService.getLocaleForIsoCode("en")).thenReturn(this.enLocale);
        Mockito.when(this.commonI18NService.getLocaleForIsoCode("de")).thenReturn(this.deLocale);
    }


    @Test
    public void shouldNotReturnValidationErrorsWhenUserHaveSufficientLanguagePermission()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"description[en]", "code", "testClass.testFeature[en]"}));
        mockTypeSystemRow("description", "en");
        mockTypeSystemRow("code", null);
        mockClassificationTypeSystemRow("testClass", "testFeature", "en");
        Mockito.when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(Set.of(this.enLanguage));
        Mockito.when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of(this.enLanguage));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldNotReturnValidationErrorsWhenAttributesUnknown()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"test1[en]", "test2", "testClass.testFeature[de]"}));
        Mockito.lenient().when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(null);
        Mockito.lenient().when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(null);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldNotReturnValidationErrorsWhenNoLocalizedAttributeExist()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"code", "testClass.testFeature"}));
        this.categoryAttributeNames.addAll(Arrays.asList(new String[] {"Identifier"}));
        mockTypeSystemRow("code", null);
        mockTypeSystemRow("Identifier", null);
        mockClassificationTypeSystemRow("testClass", "testFeature", null);
        Mockito.lenient().when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(null);
        Mockito.lenient().when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(null);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorsWhenNoLanguagePermission()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"description[en]", "testClass.testFeature[de]"}));
        this.categoryAttributeNames.addAll(Arrays.asList(new String[] {"name[de]"}));
        mockTypeSystemRow("description", "en");
        mockTypeSystemRow("name", "de");
        mockClassificationTypeSystemRow("testClass", "testFeature", "de");
        Mockito.when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(null);
        Mockito.lenient().when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(null);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, 2, Set.of("en", "de"));
    }


    @Test
    public void shouldReturnValidationErrorsWhenNoLanguageWritablePermission()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"description[en]"}));
        mockTypeSystemRow("description", "en");
        Mockito.when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(null);
        Mockito.lenient().when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of(this.enLanguage));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, 1, Set.of("en"));
    }


    @Test
    public void shouldReturnValidationErrorsWhenNoLanguageReadablePermission()
    {
        this.productAttributeNames.addAll(Arrays.asList(new String[] {"description[en]"}));
        mockTypeSystemRow("description", "en");
        Mockito.when(this.servicelayerManager.getAllWritableLanguages()).thenReturn(Set.of(this.enLanguage));
        Mockito.when(this.servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of());
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, 1, Set.of("en"));
    }


    private void assertValidationResult(List<ExcelValidationResult> validationResults, int expectedErrorLength, Set<Serializable> expectedLangSet)
    {
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.insufficient.permissions.language.header");
        List<ValidationMessage> validationMessages = ((ExcelValidationResult)validationResults.get(0)).getValidationErrors();
        Assertions.assertThat(validationMessages).hasSize(expectedErrorLength);
        validationMessages.stream().forEach(message -> {
            Assertions.assertThat(message.getMessageKey()).isEqualTo("excel.import.validation.workbook.insufficient.permissions.language.detail");
            Assertions.assertThat((Object[])message.getParams()).hasSize(1);
            Assertions.assertThat(expectedLangSet).contains((Object[])new Serializable[] {message.getParams()[0]});
        });
    }


    private void mockTypeSystemRow(String name, String isoCode)
    {
        String fullName = (isoCode == null) ? name : (name + "[" + name + "]");
        TypeSystemRow typeSystemRow = new TypeSystemRow();
        typeSystemRow.setAttrDisplayName(fullName);
        typeSystemRow.setAttrLocLang(isoCode);
        BDDMockito.given(this.excelTypeSystem.findRow(fullName)).willReturn(Optional.of(typeSystemRow));
    }


    private void mockClassificationTypeSystemRow(String clazz, String attribute, String isoCode)
    {
        String fullName = (isoCode == null) ? (clazz + "." + clazz) : (clazz + "." + clazz + "[" + attribute + "]");
        ClassificationTypeSystemRow row = new ClassificationTypeSystemRow();
        row.setClassificationClass(clazz);
        row.setClassificationAttribute(attribute);
        row.setIsoCode(isoCode);
        BDDMockito.given(this.excelClassificationTypeSystem.findRow(fullName)).willReturn(Optional.of(row));
    }
}
