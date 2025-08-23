package com.hybris.backoffice.excel;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import javax.annotation.Nonnull;
import javax.annotation.Resource;
import org.junit.Before;

@Transactional
@IntegrationTest
public abstract class ExcelIntegrationTest extends ServicelayerTest
{
    protected static final String PRODUCT_SHEET_NAME = "Product";
    protected static final String TYPE_SYSTEM_SHEET_NAME = "TypeSystem";
    @Resource
    TypeService typeService;
    @Resource
    ModelService modelService;


    @Before
    public void setUp() throws Exception
    {
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
    }


    protected void setAttributeDescriptorNamesForProductCodeAndCatalogVersion()
    {
        AttributeDescriptorModel code = this.typeService.getAttributeDescriptor("Product", "code");
        AttributeDescriptorModel catalogVersion = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        code.setName("Article Number");
        catalogVersion.setName("Catalog version");
        this.modelService.saveAll(new Object[] {catalogVersion, code});
    }


    protected CatalogVersionModel createCatalogVersionModel(@Nonnull String catalogId, @Nonnull String catalogVersion)
    {
        CatalogModel catalogModel = new CatalogModel();
        catalogModel.setId(catalogId);
        CatalogVersionModel catalogVersionModel = new CatalogVersionModel();
        catalogVersionModel.setVersion(catalogVersion);
        catalogVersionModel.setCatalog(catalogModel);
        return catalogVersionModel;
    }


    protected <T extends ItemModel> T saveItem(@Nonnull T itemModel)
    {
        this.modelService.save(itemModel);
        this.modelService.detach(itemModel);
        return itemModel;
    }


    protected AttributeDescriptorModel getAttributeDescriptorOf(@Nonnull ItemModel item, @Nonnull String attributeQualifier)
    {
        return this.typeService.getAttributeDescriptor(this.typeService.getComposedTypeForClass(item.getClass()), attributeQualifier);
    }


    protected AttributeDescriptorModel getAttributeDescriptorOf(@Nonnull Class clazz, @Nonnull String attributeQualifier)
    {
        return this.typeService.getAttributeDescriptor(this.typeService.getComposedTypeForClass(clazz), attributeQualifier);
    }


    protected ProductModel prepareProduct(String code, CatalogVersionModel catalogVersionModel)
    {
        ProductModel productModel = new ProductModel();
        productModel.setCode(code);
        productModel.setCatalogVersion(catalogVersionModel);
        return productModel;
    }


    protected ProductModel prepareProductWithVariant(ProductModel productModel, VariantTypeModel variantType)
    {
        productModel.setVariantType(variantType);
        return productModel;
    }


    protected VariantProductModel prepareVariantProductModel(ProductModel baseProduct, VariantTypeModel variantType)
    {
        VariantProductModel variantProductModel = new VariantProductModel();
        variantProductModel.setBaseProduct(baseProduct);
        variantProductModel.setVariantType(variantType);
        variantProductModel.setCode("variantProduct");
        variantProductModel.setCatalogVersion(baseProduct.getCatalogVersion());
        return variantProductModel;
    }


    protected VariantTypeModel prepareVariant()
    {
        VariantTypeModel variantTypeModel = new VariantTypeModel();
        variantTypeModel.setCode("ProductVariant");
        variantTypeModel.setCatalogItemType(Boolean.valueOf(false));
        variantTypeModel.setGenerate(Boolean.valueOf(false));
        variantTypeModel.setSingleton(Boolean.valueOf(false));
        return variantTypeModel;
    }


    protected ImportConfig createImportConfig(String script)
    {
        ImportConfig importConfig = new ImportConfig();
        importConfig.setSynchronous(true);
        importConfig.setFailOnError(true);
        importConfig.setDistributedImpexEnabled(false);
        importConfig.setScript(script);
        return importConfig;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }
}
