package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.importing.ExcelAttributeTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelClassificationTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelTypeSystemService;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
public class WorkbookTypeCodeAndAttributesValidatorTest
{
    public static final String CLASSIFICATION_SYSTEM = "SampleClassification";
    public static final String CLASSIFICATION_VERSION = "1.0";
    @Mock
    private ExcelWorkbookService excelWorkbookService;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelHeaderService excelHeaderService;
    @Mock
    private PermissionCRUDService permissionCRUDService;
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private Sheet typeSystemSheet;
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private UserService userService;
    @Mock
    private ExcelTypeSystemService excelTypeSystemService;
    @Mock
    private ExcelClassificationTypeSystemService excelClassificationTypeSystemService;
    @Spy
    @InjectMocks
    private WorkbookTypeCodeAndAttributesValidator validator;
    @Mock
    private CatalogVersionModel classificationSystem;
    @Mock
    private UserModel userModel;
    private final List<String> attributeNames = new ArrayList<>();
    private final ExcelAttributeTypeSystemService.ExcelTypeSystem excelTypeSystem = (ExcelAttributeTypeSystemService.ExcelTypeSystem)Mockito.mock(ExcelAttributeTypeSystemService.ExcelTypeSystem.class);
    private final ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem excelClassificationTypeSystem = (ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem)Mockito.mock(ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem.class);


    @Before
    public void setup()
    {
        Mockito.when(this.productSheet.getWorkbook()).thenReturn(this.workbook);
        Mockito.when(this.excelWorkbookService.getMetaInformationSheet(this.workbook)).thenReturn(this.typeSystemSheet);
        Mockito.when(this.excelSheetService.getSheets(this.workbook)).thenReturn(Arrays.asList(new Sheet[] {this.productSheet}));
        Mockito.when(this.productSheet.getSheetName()).thenReturn("Product");
        Mockito.when(this.excelSheetService.findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.eq("Product"))).thenReturn("Product");
        Mockito.when(this.excelHeaderService.getHeaderDisplayNames(this.productSheet)).thenReturn(this.attributeNames);
        Mockito.when(this.excelTypeSystemService.loadTypeSystem(this.workbook)).thenReturn(this.excelTypeSystem);
        Mockito.when(this.excelClassificationTypeSystemService.loadTypeSystem(this.workbook)).thenReturn(this.excelClassificationTypeSystem);
        Mockito.lenient().when(this.userService.getCurrentUser()).thenReturn(this.userModel);
        Mockito.when(this.catalogVersionService.getCatalogVersion("SampleClassification", "1.0"))
                        .thenReturn(this.classificationSystem);
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType(Matchers.anyString()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType(Matchers.anyString()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance(Matchers.anyString()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute(Matchers.anyString(), Matchers.anyString()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute(Matchers.anyString(), Matchers.anyString()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.excelTypeSystem.findRow((String)Matchers.any())).thenReturn(Optional.empty());
        Mockito.when(this.excelClassificationTypeSystem.findRow((String)Matchers.any())).thenReturn(Optional.empty());
    }


    @Test
    public void shouldPassWhenTypeAndAttributesExist()
    {
        addAttribute("Approval", "approval");
        addClassificationAttribute("electronics", "weight");
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenTypeCodeDoesNotExist()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenThrow(UnknownIdentifierException.class);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.type.unknown.header", "excel.import.validation.workbook.type.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToReadType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("Product"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.type.unknown.header", "excel.import.validation.workbook.type.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToChangeType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("Product"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.type.unknown.header", "excel.import.validation.workbook.type.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToCreatingNewInstanceOfType()
    {
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("Product"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.type.unknown.header", "excel.import.validation.workbook.type.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenColumnsAreDuplicated()
    {
        addAttribute("Approval", "approval");
        addAttribute("Identifier", "identifier");
        addAttribute("Approval", "approval");
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.attribute.duplicated.header", "excel.import.validation.workbook.attribute.duplicated.description");
    }


    @Test
    public void shouldReturnValidationErrorWhenColumnDoesNotExist()
    {
        addAttribute("Approval", "approval");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "approval")))
                        .thenThrow(UnknownIdentifierException.class);
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.attribute.unknown.header", "excel.import.validation.workbook.attribute.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToReadAttribute()
    {
        addAttribute("Approval", "approval");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute("Product", "approval"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.attribute.unknown.header", "excel.import.validation.workbook.attribute.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHavePermissionToChangeAttribute()
    {
        addAttribute("Approval", "approval");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeAttribute("Product", "approval"))).thenReturn(Boolean.valueOf(false));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.attribute.unknown.header", "excel.import.validation.workbook.attribute.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUnknownColumnExists()
    {
        this.attributeNames.add("Unknown column");
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.attribute.unknown.header", "excel.import.validation.workbook.attribute.unknown.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenClassificationSystemDoesNotExistOrUserDontHavePermission()
    {
        addClassificationAttribute("electronics", "weight");
        Mockito.when(this.catalogVersionService.getCatalogVersion("SampleClassification", "1.0")).thenThrow(UnknownIdentifierException.class);
        Mockito.when(Boolean.valueOf(this.excelClassificationTypeSystem.exists())).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.classification.header", "excel.import.validation.workbook.classification.unknown.system.version");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHaveReadPermissionsToClassificationTypes()
    {
        addClassificationAttribute("electronics", "weight");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canReadType("ProductFeature"))).thenReturn(Boolean.valueOf(false));
        Mockito.when(Boolean.valueOf(this.excelClassificationTypeSystem.exists())).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.classification.header", "excel.import.validation.workbook.insufficient.permissions.to.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHaveWritePermissionsToClassificationTypes()
    {
        addClassificationAttribute("electronics", "weight");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canChangeType("ProductFeature"))).thenReturn(Boolean.valueOf(false));
        Mockito.when(Boolean.valueOf(this.excelClassificationTypeSystem.exists())).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.classification.header", "excel.import.validation.workbook.insufficient.permissions.to.type");
    }


    @Test
    public void shouldReturnValidationErrorWhenUserDoesNotHaveCreatePermissionsToClassificationTypes()
    {
        addClassificationAttribute("electronics", "weight");
        Mockito.when(Boolean.valueOf(this.permissionCRUDService.canCreateTypeInstance("ProductFeature"))).thenReturn(Boolean.valueOf(false));
        Mockito.when(Boolean.valueOf(this.excelClassificationTypeSystem.exists())).thenReturn(Boolean.valueOf(true));
        List<ExcelValidationResult> validationResults = this.validator.validate(this.workbook);
        assertValidationResult(validationResults, "excel.import.validation.workbook.classification.header", "excel.import.validation.workbook.insufficient.permissions.to.type");
    }


    @Test
    public void shouldValidationOfClassificationNotBeLaunchedWhenThereIsNoClassificationInExcelSheet()
    {
        BDDMockito.given(Boolean.valueOf(this.excelClassificationTypeSystem.exists())).willReturn(Boolean.valueOf(false));
        this.validator.validate(this.workbook);
        ((WorkbookTypeCodeAndAttributesValidator)BDDMockito.then(this.validator).should(Mockito.never())).validateClassificationAttributes((String)Matchers.any(), (List)Matchers.any());
    }


    private void addAttribute(String name, String qualifier)
    {
        this.attributeNames.add(name);
        TypeSystemRow row = createTypeSystemRow(name, name, qualifier);
        BDDMockito.given(this.excelTypeSystem.findRow(name)).willReturn(Optional.of(row));
    }


    private void addClassificationAttribute(String clazz, String attribute)
    {
        String fullName = clazz + "." + clazz + " - SampleClassification/1.0";
        this.attributeNames.add(fullName);
        ClassificationTypeSystemRow row = createClassificationTypeSystemRow(fullName, clazz, attribute);
        BDDMockito.given(this.excelClassificationTypeSystem.findRow(row.getFullName())).willReturn(Optional.of(row));
    }


    private ClassificationTypeSystemRow createClassificationTypeSystemRow(String fullName, String clazz, String attribute)
    {
        ClassificationTypeSystemRow row = new ClassificationTypeSystemRow();
        row.setFullName(fullName);
        row.setClassificationSystem("SampleClassification");
        row.setClassificationVersion("1.0");
        row.setClassificationClass(clazz);
        row.setClassificationAttribute(attribute);
        return row;
    }


    private TypeSystemRow createTypeSystemRow(String attrDisplayName, String attrName, String attrQualifier)
    {
        TypeSystemRow typeSystemRow = new TypeSystemRow();
        typeSystemRow.setAttrName(attrName);
        typeSystemRow.setAttrQualifier(attrQualifier);
        typeSystemRow.setAttrDisplayName(attrDisplayName);
        return typeSystemRow;
    }


    protected void assertValidationResult(List<ExcelValidationResult> validationResults, String headerMessageKey, String descriptionMessageKey)
    {
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getHeader().getMessageKey()).isEqualTo(headerMessageKey);
        Assertions.assertThat(((ExcelValidationResult)validationResults.get(0)).getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)((ExcelValidationResult)validationResults.get(0)).getValidationErrors().get(0)).getMessageKey()).isEqualTo(descriptionMessageKey);
    }
}
