package com.hybris.backoffice.excel;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@Transactional
@IntegrationTest
public class ExcelAttributeDescriptorAttributeIntegrationTest extends ServicelayerTest
{
    @Resource
    TypeService typeService;


    @Test
    public void shouldHandleCodeOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "code");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo(String.class.getName());
    }


    @Test
    public void shouldHandleNameOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "name");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isTrue();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo(String.class.getName());
    }


    @Test
    public void shouldHandleCatalogVersionOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo("CatalogVersion");
    }


    @Test
    public void shouldHandleThumbnailOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "thumbnail");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo("Media");
    }


    @Test
    public void shouldHandleSupercategoriesOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isTrue();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo("Category");
    }


    @Test
    public void shouldHandlePricesOfProduct()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "europe1Prices");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isTrue();
        Assertions.assertThat(excelAttributeDescriptorAttribute.isLocalized()).isFalse();
        Assertions.assertThat(excelAttributeDescriptorAttribute.getType()).isEqualTo("PriceRow");
    }


    @Test
    public void shouldCollectionBeMultivalue()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("CatalogVersion", "languages");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isTrue();
    }


    @Test
    public void shouldOneRelationNotBeMultivalue()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("CatalogVersion", "catalog");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isFalse();
    }


    @Test
    public void shouldManyRelationBeMultivalue()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "logo");
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute(attributeDescriptor);
        Assertions.assertThat(excelAttributeDescriptorAttribute.isMultiValue()).isTrue();
    }
}
