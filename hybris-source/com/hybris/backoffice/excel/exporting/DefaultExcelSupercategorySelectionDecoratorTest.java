package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportParams;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelSupercategorySelectionDecoratorTest
{
    private static final List<ItemModel> NO_ITEMS_TO_EXPORT = Collections.emptyList();
    private static final List<SelectedAttribute> NO_SELECTED_ATTRIBUTES = Collections.emptyList();
    private static final Collection<ExcelAttribute> NO_ADDITIONAL_ATTRIBUTES = Collections.emptySet();
    @Mock
    TypeService mockedTypeService;
    @Mock
    PermissionCRUDService mockedPermissionCRUDService;
    @InjectMocks
    DefaultExcelSupercategorySelectionDecorator decorator;


    @Test
    public void shouldAddSupercategoryAttribute()
    {
        Collection<ExcelAttribute> classificationAttributes = (Collection)Collections.singletonList(new ExcelClassificationAttribute());
        List<SelectedAttribute> selectedAttributes = new ArrayList<>();
        ExcelExportParams excelExportParams = new ExcelExportParams(NO_ITEMS_TO_EXPORT, selectedAttributes, classificationAttributes);
        boolean hasAccessToSupercategoriesAttribute = true;
        ComposedTypeModel productType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel supercategoriesAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(this.mockedTypeService.getComposedTypeForCode("Product")).willReturn(productType);
        BDDMockito.given(this.mockedTypeService.getAttributeDescriptor(productType, "supercategories"))
                        .willReturn(supercategoriesAttribute);
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(supercategoriesAttribute)))
                        .willReturn(Boolean.valueOf(true));
        ExcelExportParams result = this.decorator.decorate(excelExportParams);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSelectedAttributes()).hasSize(1);
        Assertions.assertThat(((SelectedAttribute)result.getSelectedAttributes().get(0)).getAttributeDescriptor()).isEqualTo(supercategoriesAttribute);
    }


    @Test
    public void shouldNotDecorateIfSupercategoriesAttributeIsAlreadySelected()
    {
        List<SelectedAttribute> selectedAttributesIncludingSupercategories = Collections.singletonList(prepareSupercategoriesSelectedAttribute());
        ExcelExportParams excelExportParams = new ExcelExportParams(NO_ITEMS_TO_EXPORT, selectedAttributesIncludingSupercategories, NO_ADDITIONAL_ATTRIBUTES);
        ExcelExportParams result = this.decorator.decorate(excelExportParams);
        Assertions.assertThat(result).isSameAs(excelExportParams);
        Mockito.verifyZeroInteractions(new Object[] {this.mockedTypeService, this.mockedPermissionCRUDService});
    }


    @Test
    public void shouldNotDecorateIfNotASingleClassificationAttributeIsSelected()
    {
        ExcelExportParams excelExportParams = new ExcelExportParams(NO_ITEMS_TO_EXPORT, NO_SELECTED_ATTRIBUTES, NO_ADDITIONAL_ATTRIBUTES);
        ExcelExportParams result = this.decorator.decorate(excelExportParams);
        Assertions.assertThat(result).isSameAs(excelExportParams);
        Mockito.verifyZeroInteractions(new Object[] {this.mockedTypeService, this.mockedPermissionCRUDService});
    }


    @Test
    public void shouldNotDecorateIfDoesNotHavePermissionToReadSupercategoriesAttribute()
    {
        Collection<ExcelAttribute> classificationAttributes = (Collection)Collections.singletonList(new ExcelClassificationAttribute());
        ExcelExportParams excelExportParams = new ExcelExportParams(NO_ITEMS_TO_EXPORT, NO_SELECTED_ATTRIBUTES, classificationAttributes);
        boolean hasAccessToSupercategoriesAttribute = false;
        ComposedTypeModel productType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel supercategoriesAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(this.mockedTypeService.getComposedTypeForCode("Product")).willReturn(productType);
        BDDMockito.given(this.mockedTypeService.getAttributeDescriptor(productType, "supercategories"))
                        .willReturn(supercategoriesAttribute);
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadAttribute(supercategoriesAttribute)))
                        .willReturn(Boolean.valueOf(false));
        ExcelExportParams result = this.decorator.decorate(excelExportParams);
        Assertions.assertThat(result).isSameAs(excelExportParams);
    }


    @Test
    public void shouldHaveOrderInjectable()
    {
        int orderValue = 1337;
        this.decorator.setOrder(1337);
        Assertions.assertThat(this.decorator.getOrder()).isEqualTo(1337);
    }


    private SelectedAttribute prepareSupercategoriesSelectedAttribute()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("supercategories");
        return new SelectedAttribute(attributeDescriptorModel);
    }
}
