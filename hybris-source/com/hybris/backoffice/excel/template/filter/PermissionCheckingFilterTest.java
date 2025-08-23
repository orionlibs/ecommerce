package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionCheckingFilterTest
{
    @Mock
    PermissionCRUDService mockedPermissionCRUDService;
    @InjectMocks
    PermissionCheckingFilter filter;


    @Test
    public void shouldFilterOutNotAcceptableByPermissionServiceAttributes()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(attributeDescriptorModel))).willReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(attributeDescriptorModel);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotFilterOutAcceptableByPermissionServiceAttributes()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(attributeDescriptorModel))).willReturn(Boolean.valueOf(true));
        boolean result = this.filter.test(attributeDescriptorModel);
        Assertions.assertThat(result).isTrue();
    }
}
