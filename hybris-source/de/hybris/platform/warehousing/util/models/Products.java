package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.warehousing.util.builder.ProductModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class Products extends AbstractItems<ProductModel>
{
    public static final String CODE_CAMERA = "camera";
    public static final String CODE_MEMORY_CARD = "memorycard";
    public static final String CODE_LENS = "lens";
    private ProductDao productDao;
    private CatalogVersions catalogVersions;
    private Units units;


    public ProductModel Camera()
    {
        return (ProductModel)getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode("camera"),
                        () -> ProductModelBuilder.aModel().withCode("camera").withName("Camera", Locale.ENGLISH).withStartLineNumber(Integer.valueOf(0)).withUnit(getUnits().Unit()).withApprovalStatus(ArticleApprovalStatus.APPROVED).withCatalogVersion(getCatalogVersions().Online()).build());
    }


    public ProductModel MemoryCard()
    {
        return (ProductModel)getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode("memorycard"),
                        () -> ProductModelBuilder.aModel().withCode("memorycard").withName("Memory Card", Locale.ENGLISH).withStartLineNumber(Integer.valueOf(1)).withUnit(getUnits().Unit()).withApprovalStatus(ArticleApprovalStatus.APPROVED).withCatalogVersion(getCatalogVersions().Online()).build());
    }


    public ProductModel Lens()
    {
        return (ProductModel)getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode("lens"),
                        () -> ProductModelBuilder.aModel().withCode("lens").withName("Lens", Locale.ENGLISH).withStartLineNumber(Integer.valueOf(1)).withUnit(getUnits().Unit()).withApprovalStatus(ArticleApprovalStatus.APPROVED).withCatalogVersion(getCatalogVersions().Online()).build());
    }


    public ProductDao getProductDao()
    {
        return this.productDao;
    }


    @Required
    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
    }


    public CatalogVersions getCatalogVersions()
    {
        return this.catalogVersions;
    }


    @Required
    public void setCatalogVersions(CatalogVersions catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public Units getUnits()
    {
        return this.units;
    }


    @Required
    public void setUnits(Units units)
    {
        this.units = units;
    }
}
