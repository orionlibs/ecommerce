package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

@Transactional
@IntegrationTest
public class DefaultRequiredAttributesFactoryIntegrationTest extends ServicelayerTest
{
    @Resource
    TypeService typeService;
    @Resource
    RequiredAttributesFactory requiredAttributesFactory;
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();


    @Test
    public void shouldPrepareStructureForCatalogVersion()
    {
        AttributeDescriptorModel catalogVersionModel = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(catalogVersionModel);
        this.soft.assertThat(requiredAttribute.getEnclosingType()).isEqualTo("Product");
        this.soft.assertThat(requiredAttribute.getQualifier()).isEqualTo("catalogVersion");
        this.soft.assertThat(requiredAttribute.getChildren()).hasSize(2);
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getEnclosingType()).isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getQualifier()).isEqualTo("version");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getChildren()).isEmpty();
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getEnclosingType()).isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getQualifier()).isEqualTo("catalog");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren()).hasSize(1);
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getEnclosingType())
                        .isEqualTo("Catalog");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getQualifier()).isEqualTo("id");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getChildren()).isEmpty();
    }


    @Test
    public void shouldPrepareStructureForSupercategories()
    {
        AttributeDescriptorModel catalogVersionModel = this.typeService.getAttributeDescriptor("Product", "supercategories");
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(catalogVersionModel);
        this.soft.assertThat(requiredAttribute.getEnclosingType()).isEqualTo("Product");
        this.soft.assertThat(requiredAttribute.getQualifier()).isEqualTo("supercategories");
        this.soft.assertThat(requiredAttribute.getChildren()).hasSize(2);
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getEnclosingType()).isEqualTo("Category");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getQualifier()).isEqualTo("code");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getChildren()).isEmpty();
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getEnclosingType()).isEqualTo("Category");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getQualifier()).isEqualTo("catalogVersion");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren()).hasSize(2);
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getEnclosingType())
                        .isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getQualifier())
                        .isEqualTo("version");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getChildren()).isEmpty();
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getEnclosingType())
                        .isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getQualifier())
                        .isEqualTo("catalog");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren()).hasSize(1);
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getEnclosingType())
                        .isEqualTo("Catalog");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getQualifier())
                        .isEqualTo("id");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getChildren()).isEmpty();
    }


    @Test
    public void shouldPrepareStructureForComposedType()
    {
        ComposedTypeModel productComposedType = this.typeService.getComposedTypeForCode("Product");
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(productComposedType);
        this.soft.assertThat(requiredAttribute.getEnclosingType()).isEqualTo("Product");
        this.soft.assertThat(requiredAttribute.getQualifier()).isNullOrEmpty();
        this.soft.assertThat(requiredAttribute.getChildren()).hasSize(2);
        this.soft.assertThat(requiredAttribute.getTypeModel()).isNotNull();
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getEnclosingType()).isEqualTo("Product");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getQualifier()).isEqualTo("code");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(0)).getChildren()).isEmpty();
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getEnclosingType()).isEqualTo("Product");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getQualifier()).isEqualTo("catalogVersion");
        this.soft.assertThat(((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren()).hasSize(2);
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getEnclosingType())
                        .isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getQualifier())
                        .isEqualTo("version");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(0)).getChildren()).isEmpty();
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getEnclosingType())
                        .isEqualTo("CatalogVersion");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getQualifier())
                        .isEqualTo("catalog");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren()).hasSize(1);
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getEnclosingType())
                        .isEqualTo("Catalog");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getQualifier())
                        .isEqualTo("id");
        this.soft.assertThat(((RequiredAttribute)((RequiredAttribute)((RequiredAttribute)requiredAttribute.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getChildren()).isEmpty();
    }


    @Test
    public void shouldSkipGenerationForAtomicType()
    {
        AttributeDescriptorModel localizedAtomic = this.typeService.getAttributeDescriptor("Product", "name");
        RequiredAttribute requiredAttribute = this.requiredAttributesFactory.create(localizedAtomic);
        Assertions.assertThat(requiredAttribute.getEnclosingType()).isEqualTo("Product");
        Assertions.assertThat(requiredAttribute.getQualifier()).isEqualTo("name");
        Assertions.assertThat(requiredAttribute.getChildren()).isEmpty();
    }
}
