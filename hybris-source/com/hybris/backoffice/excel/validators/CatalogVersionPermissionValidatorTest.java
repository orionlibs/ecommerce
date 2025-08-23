package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionPermissionValidatorTest
{
    private static final Map<String, String> PARAMS = Map.of("Catalog.version", "version");
    @Mock
    private TypeService typeService;
    @Mock
    private UserService userService;
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private RequiredAttributesFactory requiredAttributesFactory;
    @Spy
    @InjectMocks
    private CatalogVersionPermissionValidator validator;


    @Test
    public void shouldCheckAssignableFormOfAttributeDescriptor()
    {
        String code = "CatalogVersion";
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel catalogVersionTypeModel = (TypeModel)Mockito.mock(TypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(catalogVersionTypeModel);
        BDDMockito.given(catalogVersionTypeModel.getCode()).willReturn("CatalogVersion");
        this.validator.canHandle((ImportParameters)Mockito.mock(ImportParameters.class), attributeDescriptor);
        ((TypeService)BDDMockito.then(this.typeService).should()).isAssignableFrom("CatalogVersion", "CatalogVersion");
    }


    @Test
    public void shouldInvokeValidateWhenCatalogVersionAndIdExist()
    {
        Map<String, Object> context = null;
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Map<String, String> testParams = Map.of("CatalogVersion.version", "version", "Catalog.id", "id");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(testParams);
        BDDMockito.given(this.catalogVersionService.getCatalogVersion("id", "version")).willReturn(catalogVersion);
        BDDMockito.given(this.userService.getCurrentUser()).willReturn(user);
        BDDMockito.given(Boolean.valueOf(this.userService.isAdmin(user))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.catalogVersionService.getAllCatalogVersions()).willReturn(List.of(catalogVersion));
        ExcelValidationResult result = this.validator.validate(importParameters, attributeDescriptor, context);
        ((CatalogVersionPermissionValidator)Mockito.verify(this.validator, Mockito.times(1))).validateCatalogVersion(testParams, attributeDescriptor, catalogVersion);
        Assertions.assertThat(result.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldSkipValidateWhenCatalogVersionIsNull()
    {
        Map<String, Object> context = null;
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Map<String, String> testParams = Map.of("Catalog.id", "id");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(testParams);
        ExcelValidationResult result = this.validator.validate(importParameters, attributeDescriptor, context);
        ((CatalogVersionPermissionValidator)Mockito.verify(this.validator, Mockito.times(0))).validateCatalogVersion((Map)Mockito.any(Map.class),
                        (AttributeDescriptorModel)Mockito.any(AttributeDescriptorModel.class), (CatalogVersionModel)Mockito.any(CatalogVersionModel.class));
        Assertions.assertThat(result.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldSkipValidateWhenCatalogIdIsNull()
    {
        Map<String, Object> context = null;
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Map<String, String> testParams = Map.of("CatalogVersion.version", "version");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(testParams);
        ExcelValidationResult result = this.validator.validate(importParameters, attributeDescriptor, context);
        ((CatalogVersionPermissionValidator)Mockito.verify(this.validator, Mockito.times(0))).validateCatalogVersion((Map)Mockito.any(Map.class),
                        (AttributeDescriptorModel)Mockito.any(AttributeDescriptorModel.class), (CatalogVersionModel)Mockito.any(CatalogVersionModel.class));
        Assertions.assertThat(result.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldNotCreateValidationMessageIfUserHasAccessToCatalogVersion()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        BDDMockito.given(this.userService.getCurrentUser()).willReturn(user);
        BDDMockito.given(Boolean.valueOf(this.userService.isAdmin(user))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(this.catalogVersionService.getAllWritableCatalogVersions((PrincipalModel)user)).willReturn(List.of(catalogVersion));
        Optional<ValidationMessage> validationMessage = this.validator.validateCatalogVersion(PARAMS, attributeDescriptor, catalogVersion);
        Assertions.assertThat(validationMessage).isEmpty();
    }


    @Test
    public void shouldNotCreateValidationMessageForAdmin()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        BDDMockito.given(this.userService.getCurrentUser()).willReturn(user);
        BDDMockito.given(Boolean.valueOf(this.userService.isAdmin(user))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.catalogVersionService.getAllCatalogVersions()).willReturn(List.of(catalogVersion));
        Optional<ValidationMessage> validationMessage = this.validator.validateCatalogVersion(PARAMS, attributeDescriptor, catalogVersion);
        Assertions.assertThat(validationMessage).isEmpty();
    }


    @Test
    public void shouldCreateValidationMessageIfUserHasNotAccessToCatalogVersion()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        ValidationMessage validationMessage = new ValidationMessage("messageKey", new Serializable[] {"CatalogVersion", "params"});
        RequiredAttribute requiredAttribute = (RequiredAttribute)Mockito.mock(RequiredAttribute.class);
        BDDMockito.given(this.userService.getCurrentUser()).willReturn(user);
        BDDMockito.given(Boolean.valueOf(this.userService.isAdmin(user))).willReturn(Boolean.valueOf(false));
        Mockito.lenient().when(this.catalogVersionService.getAllCatalogVersions()).thenReturn(List.of((CatalogVersionModel)Mockito.mock(CatalogVersionModel.class)));
        BDDMockito.given(this.requiredAttributesFactory.create(attributeDescriptor)).willReturn(requiredAttribute);
        ((CatalogVersionPermissionValidator)Mockito.doReturn(validationMessage).when(this.validator)).prepareValidationMessage(requiredAttribute, PARAMS);
        Optional<ValidationMessage> result = this.validator.validateCatalogVersion(PARAMS, attributeDescriptor, catalogVersion);
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat((Comparable)result.get()).isEqualTo(validationMessage);
    }
}
