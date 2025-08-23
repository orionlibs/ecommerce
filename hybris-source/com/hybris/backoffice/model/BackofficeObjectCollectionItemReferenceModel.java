package com.hybris.backoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BackofficeObjectCollectionItemReferenceModel extends ItemModel
{
    public static final String _TYPECODE = "BackofficeObjectCollectionItemReference";
    public static final String _BACKOFFICECOLLECTION2ELEMENTRELATION = "BackofficeCollection2ElementRelation";
    public static final String PRODUCT = "product";
    public static final String COLLECTIONPK = "collectionPk";


    public BackofficeObjectCollectionItemReferenceModel()
    {
    }


    public BackofficeObjectCollectionItemReferenceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeObjectCollectionItemReferenceModel(BackofficeObjectSpecialCollectionModel _collectionPk, ProductModel _product)
    {
        setCollectionPk(_collectionPk);
        setProduct(_product);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeObjectCollectionItemReferenceModel(BackofficeObjectSpecialCollectionModel _collectionPk, ItemModel _owner, ProductModel _product)
    {
        setCollectionPk(_collectionPk);
        setOwner(_owner);
        setProduct(_product);
    }


    @Accessor(qualifier = "collectionPk", type = Accessor.Type.GETTER)
    public BackofficeObjectSpecialCollectionModel getCollectionPk()
    {
        return (BackofficeObjectSpecialCollectionModel)getPersistenceContext().getPropertyValue("collectionPk");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "collectionPk", type = Accessor.Type.SETTER)
    public void setCollectionPk(BackofficeObjectSpecialCollectionModel value)
    {
        getPersistenceContext().setPropertyValue("collectionPk", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }
}
