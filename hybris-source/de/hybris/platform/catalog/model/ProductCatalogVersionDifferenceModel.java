package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductCatalogVersionDifferenceModel extends CatalogVersionDifferenceModel
{
    public static final String _TYPECODE = "ProductCatalogVersionDifference";
    public static final String SOURCEPRODUCT = "sourceProduct";
    public static final String TARGETPRODUCT = "targetProduct";
    public static final String MODE = "mode";
    public static final String SOURCEPRODUCTAPPROVALSTATUS = "sourceProductApprovalStatus";
    public static final String TARGETPRODUCTAPPROVALSTATUS = "targetProductApprovalStatus";


    public ProductCatalogVersionDifferenceModel()
    {
    }


    public ProductCatalogVersionDifferenceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, ProductDifferenceMode _mode, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setMode(_mode);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, ProductDifferenceMode _mode, ItemModel _owner, ProductModel _sourceProduct, CatalogVersionModel _sourceVersion, ProductModel _targetProduct, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setMode(_mode);
        setOwner(_owner);
        setSourceProduct(_sourceProduct);
        setSourceVersion(_sourceVersion);
        setTargetProduct(_targetProduct);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public ProductDifferenceMode getMode()
    {
        return (ProductDifferenceMode)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "sourceProduct", type = Accessor.Type.GETTER)
    public ProductModel getSourceProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("sourceProduct");
    }


    @Accessor(qualifier = "sourceProductApprovalStatus", type = Accessor.Type.GETTER)
    public ArticleApprovalStatus getSourceProductApprovalStatus()
    {
        return (ArticleApprovalStatus)getPersistenceContext().getPropertyValue("sourceProductApprovalStatus");
    }


    @Accessor(qualifier = "targetProduct", type = Accessor.Type.GETTER)
    public ProductModel getTargetProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("targetProduct");
    }


    @Accessor(qualifier = "targetProductApprovalStatus", type = Accessor.Type.GETTER)
    public ArticleApprovalStatus getTargetProductApprovalStatus()
    {
        return (ArticleApprovalStatus)getPersistenceContext().getPropertyValue("targetProductApprovalStatus");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(ProductDifferenceMode value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "sourceProduct", type = Accessor.Type.SETTER)
    public void setSourceProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("sourceProduct", value);
    }


    @Accessor(qualifier = "targetProduct", type = Accessor.Type.SETTER)
    public void setTargetProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("targetProduct", value);
    }
}
