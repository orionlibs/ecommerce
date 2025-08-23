package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaCatalogVersionValidatorTest
{
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ExcelMediaCatalogVersionValidator excelMediaCatalogVersionValidator;


    @Before
    public void setUp()
    {
        List<CatalogVersionModel> catalogVersions = new ArrayList<>();
        catalogVersions.add(mockCatalogVersion("Staged", "Default"));
        catalogVersions.add(mockCatalogVersion("Online", "Default"));
        catalogVersions.add(mockCatalogVersion("Middle", "Cars"));
        catalogVersions.add(mockCatalogVersion("Staged", "Cars"));
        UserModel userModel = (UserModel)Mockito.mock(UserModel.class);
        BDDMockito.given(this.userService.getCurrentUser()).willReturn(userModel);
        BDDMockito.given(Boolean.valueOf(this.userService.isAdmin(userModel))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(this.catalogVersionService.getAllWritableCatalogVersions((PrincipalModel)userModel)).willReturn(catalogVersions);
    }


    private CatalogVersionModel mockCatalogVersion(String version, String catalogId)
    {
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalogModel = (CatalogModel)Mockito.mock(CatalogModel.class);
        BDDMockito.given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
        BDDMockito.given(catalogVersionModel.getVersion()).willReturn(version);
        BDDMockito.given(catalogModel.getId()).willReturn(catalogId);
        return catalogVersionModel;
    }


    @Test
    public void shouldNotReturnErrorWhenCatalogVersionExists()
    {
        Map<String, Object> ctx = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("catalog", "Default");
        parameters.put("version", "Online");
        Collection<ValidationMessage> validationMessages = this.excelMediaCatalogVersionValidator.validateSingleValue(ctx, parameters);
        Assertions.assertThat(validationMessages).isEmpty();
    }


    @Test
    public void shouldReturnErrorWhenCatalogVersionDoesNotExist()
    {
        Map<String, Object> ctx = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("catalog", "Default");
        parameters.put("version", "Stg");
        List<ValidationMessage> validationMessages = (List<ValidationMessage>)this.excelMediaCatalogVersionValidator.validateSingleValue(ctx, parameters);
        Assertions.assertThat(validationMessages).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationMessages.get(0)).getMessageKey()).isEqualTo("excel.import.validation.catalogversion.doesntexists");
    }


    @Test
    public void shouldReturnErrorWhenCatalogDoesNotExist()
    {
        Map<String, Object> ctx = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("catalog", "Clothing");
        parameters.put("version", "Online");
        List<ValidationMessage> validationMessages = (List<ValidationMessage>)this.excelMediaCatalogVersionValidator.validateSingleValue(ctx, parameters);
        Assertions.assertThat(validationMessages).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationMessages.get(0)).getMessageKey()).isEqualTo("excel.import.validation.catalog.doesntexists");
    }


    @Test
    public void shouldReturnErrorWhenVersionDoesNotMatchCatalog()
    {
        Map<String, Object> ctx = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("catalog", "Cars");
        parameters.put("version", "Online");
        List<ValidationMessage> validationMessages = (List<ValidationMessage>)this.excelMediaCatalogVersionValidator.validateSingleValue(ctx, parameters);
        Assertions.assertThat(validationMessages).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationMessages.get(0)).getMessageKey()).isEqualTo("excel.import.validation.catalogversion.doesntmatch");
    }
}
