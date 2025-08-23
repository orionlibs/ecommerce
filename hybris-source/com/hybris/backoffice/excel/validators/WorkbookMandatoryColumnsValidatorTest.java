package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.DisplayNameAttributeNameFormatter;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.AttributeModifierCriteria;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookMandatoryColumnsValidatorTest
{
    @Mock
    private ExcelWorkbookService excelWorkbookService;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private DisplayNameAttributeNameFormatter displayNameAttributeNameFormatter;
    @Mock
    private TypeService typeService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private ExcelMapper<String, AttributeDescriptorModel> mapper;
    @Spy
    @InjectMocks
    private WorkbookMandatoryColumnsValidator workbookMandatoryColumnsValidator;
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private Sheet typeSystemSheet;


    @Before
    public void setup()
    {
        Mockito.when(this.excelSheetService.getSheets(this.workbook)).thenReturn(Arrays.asList(new Sheet[] {this.productSheet}));
        Mockito.when(this.excelWorkbookService.getMetaInformationSheet(this.workbook)).thenReturn(this.typeSystemSheet);
        Mockito.when(this.productSheet.getSheetName()).thenReturn("Product");
        Mockito.when(this.excelSheetService.findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.eq("Product"))).thenReturn("Product");
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.lenient().when(this.commonI18NService.getCurrentLanguage()).thenReturn(languageModel);
        Mockito.lenient().when(languageModel.getIsocode()).thenReturn("en");
    }


    @Test
    public void shouldNotReturnValidationErrorsWhenAllMandatoryColumnsAreSelected()
    {
        AttributeDescriptorModel code = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelAttributeDescriptorAttribute excelAttribute = new ExcelAttributeDescriptorAttribute(code);
        Mockito.lenient().when(this.typeService.getAttributesForModifiers((String)Matchers.eq("Product"), (AttributeModifierCriteria)Matchers.any())).thenReturn(Sets.newHashSet((Object[])new AttributeDescriptorModel[] {code}));
        Mockito.when(this.mapper.apply("Product")).thenReturn(Collections.emptyList());
        Mockito.lenient().when(this.workbookMandatoryColumnsValidator.prepareExcelAttribute((AttributeDescriptorModel)Matchers.eq(code), (String)Matchers.any())).thenReturn(excelAttribute);
        Mockito.lenient().when(Integer.valueOf(this.excelSheetService.findColumnIndex(this.typeSystemSheet, this.productSheet, (ExcelAttribute)excelAttribute))).thenReturn(Integer.valueOf(0));
        List<ExcelValidationResult> validationResult = this.workbookMandatoryColumnsValidator.validate(this.workbook);
        Assertions.assertThat(validationResult).isEmpty();
    }


    @Test
    public void shouldNotReturnValidationErrorsWhenLocalizedMandatoryColumnIsSelectedForCurrentLanguage()
    {
        AttributeDescriptorModel code = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelAttributeDescriptorAttribute excelAttribute = new ExcelAttributeDescriptorAttribute(code);
        Mockito.lenient().when(this.typeService.getAttributesForModifiers((String)Matchers.eq("Product"), (AttributeModifierCriteria)Matchers.any())).thenReturn(Sets.newHashSet((Object[])new AttributeDescriptorModel[] {code}));
        Mockito.when(this.mapper.apply("Product")).thenReturn(Collections.emptyList());
        Mockito.lenient().when(this.workbookMandatoryColumnsValidator.prepareExcelAttribute((AttributeDescriptorModel)Matchers.eq(code), (String)Matchers.any())).thenReturn(excelAttribute);
        Mockito.lenient().when(Integer.valueOf(this.excelSheetService.findColumnIndex(this.typeSystemSheet, this.productSheet, (ExcelAttribute)excelAttribute))).thenReturn(Integer.valueOf(0));
        List<ExcelValidationResult> validationResult = this.workbookMandatoryColumnsValidator.validate(this.workbook);
        Assertions.assertThat(validationResult).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorsWhenMandatoryColumnIsNotSelected()
    {
        AttributeDescriptorModel code = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        SelectedAttribute codeSelectedAttribute = new SelectedAttribute(code);
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(code);
        ((WorkbookMandatoryColumnsValidator)Mockito.doReturn(codeSelectedAttribute).when(this.workbookMandatoryColumnsValidator)).prepareSelectedAttribute(code);
        Mockito.when(this.mapper.apply("Product")).thenReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {code}));
        Mockito.when(Integer.valueOf(this.excelSheetService.findColumnIndex(this.typeSystemSheet, this.productSheet, (ExcelAttribute)excelAttributeDescriptorAttribute))).thenReturn(Integer.valueOf(-1));
        ((WorkbookMandatoryColumnsValidator)Mockito.doReturn(excelAttributeDescriptorAttribute).when(this.workbookMandatoryColumnsValidator)).prepareExcelAttribute((AttributeDescriptorModel)Matchers.eq(code), (String)Matchers.any());
        List<ExcelValidationResult> validationResult = this.workbookMandatoryColumnsValidator.validate(this.workbook);
        Assertions.assertThat(validationResult).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResult.get(0)).getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)((ExcelValidationResult)validationResult.get(0)).getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.mandatory.column.description");
        Assertions.assertThat(((ExcelValidationResult)validationResult.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.mandatory.column.header");
    }
}
