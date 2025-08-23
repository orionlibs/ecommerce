package de.hybris.platform.configurablebundlecockpits.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.List;

public interface BundleNavigationService
{
    List<BundleTemplateModel> getRootNavigationNodes(CatalogVersionModel paramCatalogVersionModel);


    void add(BundleTemplateModel paramBundleTemplateModel, Collection<ProductModel> paramCollection);


    void add(BundleTemplateModel paramBundleTemplateModel, ProductModel paramProductModel);


    void remove(BundleTemplateModel paramBundleTemplateModel, ProductModel paramProductModel);


    void move(BundleTemplateModel paramBundleTemplateModel, ProductModel paramProductModel1, ProductModel paramProductModel2);


    void move(BundleTemplateModel paramBundleTemplateModel1, ProductModel paramProductModel, BundleTemplateModel paramBundleTemplateModel2);
}
