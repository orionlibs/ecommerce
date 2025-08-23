package com.hybris.backoffice.excel.translators.generic;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

@Transactional
@IntegrationTest
public class ExcelGenericReferenceTranslatorIntegrationTest extends ServicelayerTest
{
    @Resource
    ExcelGenericReferenceTranslator excelGenericReferenceTranslator;
    @Resource
    TypeService typeService;


    @Before
    public void setup()
    {
        this.excelGenericReferenceTranslator.setExcludedFields(new ArrayList());
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeIsPartOf()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getPartOf()).willReturn(Boolean.valueOf(true));
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldHandleRequestWhenAttributeTypeIsRelation()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "comments");
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldHandleRequestWhenAttributeTypeIsCollection()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldHandleRequestWhenAttributeTypeIsReferenceType()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeHasNoUniqueAttributes()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "owner");
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeTypeIsPlainComposedType()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(composedTypeModel);
        BDDMockito.given(composedTypeModel.getCode()).willReturn("ComposedType");
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeIsOnExcludedList()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(composedTypeModel);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("products");
        BDDMockito.given(composedTypeModel.getCode()).willReturn("Category");
        BDDMockito.given(attributeDescriptor.getEnclosingType()).willReturn(composedTypeModel);
        this.excelGenericReferenceTranslator.setExcludedFields(Arrays.asList(new String[] {"Category.products"}));
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeForParentTypeIsOnExcludedList()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(composedTypeModel);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("products");
        BDDMockito.given(composedTypeModel.getCode()).willReturn("ClassificationClass");
        BDDMockito.given(attributeDescriptor.getEnclosingType()).willReturn(composedTypeModel);
        this.excelGenericReferenceTranslator.setExcludedFields(Arrays.asList(new String[] {"Category.products"}));
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldHandleRequestWhenAttributeIsNotOnExcludedList()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        this.excelGenericReferenceTranslator.setExcludedFields(Arrays.asList(new String[] {"FakeCategory.products"}));
        boolean canHandle = this.excelGenericReferenceTranslator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }
}
