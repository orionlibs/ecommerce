package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
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
public class PermissionCrudAttributePredicateTest
{
    @Mock
    PermissionCRUDService mockedPermissionCRUDService;
    @Spy
    @InjectMocks
    PermissionCrudAttributePredicate permissionCrudAttributePredicate;


    @Test
    public void shouldNotIncludeAttributesToWhichTheUserHasNoReadAccess()
    {
        String qualifier = "price";
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("price");
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(attributeDescriptorModel))).willReturn(Boolean.valueOf(false));
        boolean result = this.permissionCrudAttributePredicate.test(attributeDescriptorModel);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldIncludeAttributesToWhichTheUserHasReadAccess()
    {
        String qualifier = "price";
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Mockito.lenient().when(attributeDescriptorModel.getQualifier()).thenReturn("price");
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(attributeDescriptorModel))).willReturn(Boolean.valueOf(true));
        boolean result = this.permissionCrudAttributePredicate.test(attributeDescriptorModel);
        Assertions.assertThat(result).isTrue();
    }
}
