package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookTypeCodeAndSelectedAttributeValidatorTest
{
    @Mock
    private ExcelWorkbookService excelWorkbookService;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelHeaderService excelHeaderService;
    @Mock
    private PermissionCRUDService permissionCRUDService;
    @InjectMocks
    private WorkbookTypeCodeAndSelectedAttributeValidator validator;
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private Sheet typeSystemSheet;


    @Before
    public void setup()
    {
        Mockito.when(this.excelWorkbookService.getMetaInformationSheet(this.workbook)).thenReturn(this.typeSystemSheet);
        Mockito.when(this.excelSheetService.getSheets(this.workbook)).thenReturn(Arrays.asList(new Sheet[] {this.productSheet}));
        Mockito.when(this.productSheet.getSheetName()).thenReturn("Product");
        Mockito.when(this.excelSheetService.findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.eq("Product"))).thenReturn("Product");
    }


    @Test
    public void shouldNotReturnValidationErrorWhenTypeCodeAndSelectedAttributesExists()
    {
        SelectedAttributeQualifier selectedAttributeQualifier = new SelectedAttributeQualifier("code", "code");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.excelHeaderService.getSelectedAttributesQualifiers(this.typeSystemSheet, this.productSheet))
                        .thenReturn(Arrays.asList(new SelectedAttributeQualifier[] {selectedAttributeQualifier}));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenTypeCodeDoesNotExist()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenThrow(UnknownIdentifierException.class);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.type.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canReadAttribute((String)Matchers.any(), (String)Matchers.any());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canChangeAttribute((String)Matchers.any(), (String)Matchers.any());
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToReadType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.type.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canReadAttribute((String)Matchers.any(), (String)Matchers.any());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canChangeAttribute((String)Matchers.any(), (String)Matchers.any());
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToChangeType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.type.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canReadAttribute((String)Matchers.any(), (String)Matchers.any());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canChangeAttribute((String)Matchers.any(), (String)Matchers.any());
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToCreatingNewInstanceOfType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.type.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canReadAttribute((String)Matchers.any(), (String)Matchers.any());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService, Mockito.never())).canChangeAttribute((String)Matchers.any(), (String)Matchers.any());
    }


    @Test
    public void shouldReturnValidationErrorWhenColumnsAreDuplicated()
    {
        SelectedAttributeQualifier firstSelectedColumn = new SelectedAttributeQualifier("code", "code");
        SelectedAttributeQualifier secondSelectedColumn = new SelectedAttributeQualifier("code", "code");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.excelHeaderService.getSelectedAttributesQualifiers(this.typeSystemSheet, this.productSheet))
                        .thenReturn(Arrays.asList(new SelectedAttributeQualifier[] {firstSelectedColumn, secondSelectedColumn}));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.attribute.duplicated.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canReadType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canChangeType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canCreateTypeInstance(Matchers.anyString());
    }


    @Test
    public void shouldReturnValidationErrorWhenColumnDoesNotExist()
    {
        SelectedAttributeQualifier firstSelectedColumn = new SelectedAttributeQualifier("code", null);
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.excelHeaderService.getSelectedAttributesQualifiers(this.typeSystemSheet, this.productSheet))
                        .thenReturn(Arrays.asList(new SelectedAttributeQualifier[] {firstSelectedColumn}));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.attribute.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canReadType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canChangeType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canCreateTypeInstance(Matchers.anyString());
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToReadAttribute()
    {
        SelectedAttributeQualifier firstSelectedColumn = new SelectedAttributeQualifier("code", "code");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "code"))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.excelHeaderService.getSelectedAttributesQualifiers(this.typeSystemSheet, this.productSheet))
                        .thenReturn(Arrays.asList(new SelectedAttributeQualifier[] {firstSelectedColumn}));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.attribute.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canReadType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canChangeType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canCreateTypeInstance(Matchers.anyString());
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToChangeAttribute()
    {
        SelectedAttributeQualifier firstSelectedColumn = new SelectedAttributeQualifier("code", "code");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "code"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "code"))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.excelHeaderService.getSelectedAttributesQualifiers(this.typeSystemSheet, this.productSheet))
                        .thenReturn(Arrays.asList(new SelectedAttributeQualifier[] {firstSelectedColumn}));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey())
                        .isEqualTo("excel.import.validation.workbook.attribute.unknown.header");
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canReadType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canChangeType(Matchers.anyString());
        ((PermissionCRUDService)Mockito.verify(this.permissionCRUDService)).canCreateTypeInstance(Matchers.anyString());
    }
}
