package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ExcelCatalogVersionFieldValidatorTest
{
    public static final String NOT_EXISTING_CATALOG = "notExistingCatalog";
    public static final String SECOND_A_VERSION = "secondAVersion";
    public static final String FIRST_B_VERSION = "firstBVersion";
    public static final String FIRST_CATALOG = "firstCatalog";
    public static final String SECOND_CATALOG = "secondCatalog";
    public static final String FIRST_A_VERSION = "firstAVersion";
    public static final String NOT_EXISTING_VERSION = "Not existing version";
    public static final String FIRST_CATALOG_FIRST_BVERSION = "firstCatalog:firstBVersion";
    @Mock
    private TypeService typeService;
    @Mock
    private UserService userService;
    @Mock
    private CatalogVersionService catalogVersionService;
    @InjectMocks
    private ExcelCatalogVersionValidator excelCatalogVersionValidator;


    @Before
    public void setup()
    {
        CatalogModel firstCatalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        CatalogModel secondCatalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        Mockito.when(firstCatalog.getId()).thenReturn("firstCatalog");
        Mockito.when(secondCatalog.getId()).thenReturn("secondCatalog");
        CatalogVersionModel firstAVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogVersionModel firstBVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogVersionModel secondAVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        Mockito.when(this.catalogVersionService.getAllWritableCatalogVersions((PrincipalModel)Matchers.any()))
                        .thenReturn(Arrays.asList(new CatalogVersionModel[] {firstAVersion, firstBVersion, secondAVersion}));
        Mockito.when(firstAVersion.getVersion()).thenReturn("firstAVersion");
        Mockito.when(firstAVersion.getCatalog()).thenReturn(firstCatalog);
        Mockito.when(firstBVersion.getVersion()).thenReturn("firstBVersion");
        Mockito.when(firstBVersion.getCatalog()).thenReturn(firstCatalog);
        Mockito.when(secondAVersion.getVersion()).thenReturn("secondAVersion");
        Mockito.when(secondAVersion.getCatalog()).thenReturn(secondCatalog);
        Mockito.lenient().when(firstCatalog.getCatalogVersions()).thenReturn(Sets.newHashSet((Object[])new CatalogVersionModel[] {firstAVersion, firstBVersion}));
        Mockito.lenient().when(secondCatalog.getCatalogVersions()).thenReturn(Sets.newHashSet((Object[])new CatalogVersionModel[] {secondAVersion}));
    }


    @Test
    public void shouldHandleWhenParamsContainsCatalogAndVersion()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> singleParams = new HashMap<>();
        parametersList.add(singleParams);
        singleParams.put("catalog", "firstCatalog");
        singleParams.put("version", "firstAVersion");
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Boolean.class.getCanonicalName());
        boolean canHandle = this.excelCatalogVersionValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenParamsDoesNotContainsCatalogAndVersion()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> singleParams = new HashMap<>();
        parametersList.add(singleParams);
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Boolean.class.getCanonicalName());
        boolean canHandle = this.excelCatalogVersionValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenParamsContainsExistingCatalogsAndVersions()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", "firstCatalog");
        firstParams.put("version", "firstBVersion");
        Map<String, String> secondParams = new HashMap<>();
        parametersList.add(secondParams);
        secondParams.put("catalog", "secondCatalog");
        secondParams.put("version", "secondAVersion");
        ImportParameters importParameters = new ImportParameters("Product", null, "firstCatalog:firstBVersion,secondCatalog:secondAVersion", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Boolean.class.getCanonicalName());
        ExcelValidationResult validationCellResult = this.excelCatalogVersionValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
        Assertions.assertThat(validationCellResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenCatalogIsEmpty()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", null);
        firstParams.put("version", "firstBVersion");
        ImportParameters importParameters = new ImportParameters("Product", null, ":firstBVersion", null, parametersList);
        testCatalogValidation(importParameters, new ValidationMessage("excel.import.validation.catalog.empty"));
    }


    @Test
    public void shouldReturnValidationErrorWhenParamsDoesNotContainsExistingCatalog()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", "firstCatalog");
        firstParams.put("version", "firstBVersion");
        Map<String, String> secondParams = new HashMap<>();
        parametersList.add(secondParams);
        secondParams.put("catalog", "notExistingCatalog");
        secondParams.put("version", "secondAVersion");
        ImportParameters importParameters = new ImportParameters("Product", null, "firstCatalog:firstBVersion", null, parametersList);
        testCatalogValidation(importParameters, new ValidationMessage("excel.import.validation.catalog.doesntexists", new Serializable[] {"notExistingCatalog"}));
    }


    @Test
    public void shouldReturnValidationErrorWhenCatalogVersionIsEmpty()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", "firstCatalog");
        firstParams.put("version", null);
        ImportParameters importParameters = new ImportParameters("Product", null, "firstCatalog:", null, parametersList);
        testCatalogValidation(importParameters, new ValidationMessage("excel.import.validation.catalogversion.empty"));
    }


    @Test
    public void shouldReturnValidationErrorWhenParamsDoesNotContainsExistingVersions()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", "firstCatalog");
        firstParams.put("version", "firstBVersion");
        Map<String, String> secondParams = new HashMap<>();
        parametersList.add(secondParams);
        secondParams.put("catalog", "secondCatalog");
        secondParams.put("version", "Not existing version");
        ImportParameters importParameters = new ImportParameters("Product", null, "firstCatalog:firstBVersion", null, parametersList);
        testCatalogValidation(importParameters, new ValidationMessage("excel.import.validation.catalogversion.doesntexists", new Serializable[] {"Not existing version"}));
    }


    @Test
    public void shouldReturnValidationErrorWhenParamsCatalogAndVersionDoesNotMatch()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> firstParams = new HashMap<>();
        parametersList.add(firstParams);
        firstParams.put("catalog", "firstCatalog");
        firstParams.put("version", "firstBVersion");
        Map<String, String> secondParams = new HashMap<>();
        parametersList.add(secondParams);
        secondParams.put("catalog", "secondCatalog");
        secondParams.put("version", "firstAVersion");
        ImportParameters importParameters = new ImportParameters("Product", null, "firstCatalog:firstBVersion", null, parametersList);
        testCatalogValidation(importParameters, new ValidationMessage("excel.import.validation.catalogversion.doesntmatch", new Serializable[] {"firstAVersion", "secondCatalog"}));
    }


    protected void testCatalogValidation(ImportParameters importParameters, ValidationMessage expectedValidationMessage)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Boolean.class.getCanonicalName());
        ExcelValidationResult validationCellResult = this.excelCatalogVersionValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo(expectedValidationMessage.getMessageKey());
        Assertions.assertThat((Object[])((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getParams()).contains((Object[])expectedValidationMessage.getParams());
    }
}
