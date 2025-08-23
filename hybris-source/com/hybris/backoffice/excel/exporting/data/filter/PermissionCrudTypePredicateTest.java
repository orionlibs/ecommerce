package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
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
public class PermissionCrudTypePredicateTest
{
    @Mock
    PermissionCRUDService mockedPermissionCRUDService;
    @Spy
    @InjectMocks
    PermissionCrudTypePredicate permissionCrudTypePredicate;


    @Test
    public void shouldNotIncludeTypesToWhichTheUserHasNoReadAccess()
    {
        String typeCode = "product";
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(composedTypeModel.getCode()).willReturn("product");
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadType(composedTypeModel))).willReturn(Boolean.valueOf(false));
        boolean result = this.permissionCrudTypePredicate.test(composedTypeModel);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldIncludeTypesToWhichTheUserHasReadAccess()
    {
        String typeCode = "product";
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.lenient().when(composedTypeModel.getCode()).thenReturn("product");
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadType(composedTypeModel))).willReturn(Boolean.valueOf(true));
        boolean result = this.permissionCrudTypePredicate.test(composedTypeModel);
        Assertions.assertThat(result).isTrue();
    }
}
