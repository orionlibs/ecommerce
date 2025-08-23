package de.hybris.platform.configurablebundlecockpits.servicelayer.services.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree.ParentBundleTemplateComparator;
import de.hybris.platform.configurablebundlecockpits.servicelayer.services.BundleNavigationService;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;

public class DefaultBundleNavigationService extends AbstractBusinessService implements BundleNavigationService
{
    private transient BundleTemplateService bundleTemplateService;


    public List<BundleTemplateModel> getRootNavigationNodes(CatalogVersionModel catVer)
    {
        List<BundleTemplateModel> rootBundles = getBundleTemplateService().getAllRootBundleTemplates(catVer);
        List<BundleTemplateModel> bundleList = new ArrayList<>(rootBundles);
        Collections.sort(bundleList, (Comparator<? super BundleTemplateModel>)new ParentBundleTemplateComparator());
        return bundleList;
    }


    public void add(BundleTemplateModel bundleTemplateModel, Collection<ProductModel> productModels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("bundleTemplateModel", bundleTemplateModel);
        ServicesUtil.validateParameterNotNullStandardMessage("productModels", productModels);
        List<ProductModel> existingProducts = new ArrayList<>(bundleTemplateModel.getProducts());
        for(ProductModel productToAdd : productModels)
        {
            if(!existingProducts.contains(productToAdd))
            {
                existingProducts.add(productToAdd);
            }
        }
        bundleTemplateModel.setProducts(existingProducts);
        getModelService().save(bundleTemplateModel);
    }


    public void add(BundleTemplateModel bundleTemplateModel, ProductModel productModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("bundleTemplateModel", bundleTemplateModel);
        ServicesUtil.validateParameterNotNullStandardMessage("productModel", productModel);
        add(bundleTemplateModel, Arrays.asList(new ProductModel[] {productModel}));
    }


    public void remove(BundleTemplateModel bundleTemplateModel, ProductModel productModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("bundleTemplateModel", bundleTemplateModel);
        ServicesUtil.validateParameterNotNullStandardMessage("productModel", productModel);
        List<ProductModel> items = new ArrayList<>(bundleTemplateModel.getProducts());
        items.remove(productModel);
        bundleTemplateModel.setProducts(items);
        getModelService().save(bundleTemplateModel);
    }


    public void move(BundleTemplateModel bundleTemplateModel, ProductModel sourceEntry, ProductModel targetEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("bundleTemplateModel", bundleTemplateModel);
        ServicesUtil.validateParameterNotNullStandardMessage("sourceEntry", sourceEntry);
        ServicesUtil.validateParameterNotNullStandardMessage("targetEntry", targetEntry);
        List<ProductModel> items = new ArrayList<>(bundleTemplateModel.getProducts());
        int sourceIndex = items.indexOf(sourceEntry);
        int targetIndex = items.indexOf(targetEntry);
        if(sourceIndex == -1 || targetIndex == -1)
        {
            return;
        }
        if(sourceIndex < targetIndex)
        {
            items.add(targetIndex, sourceEntry);
            items.remove(sourceIndex);
        }
        else
        {
            items.add(targetIndex, sourceEntry);
            items.remove(items.lastIndexOf(sourceEntry));
        }
        bundleTemplateModel.setProducts(items);
        getModelService().save(bundleTemplateModel);
    }


    public void move(BundleTemplateModel sourceBundleTemplateModel, ProductModel productModel, BundleTemplateModel targetBundleTemplateModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("sourceBundleTemplateModel", sourceBundleTemplateModel);
        ServicesUtil.validateParameterNotNullStandardMessage("productModel", productModel);
        ServicesUtil.validateParameterNotNullStandardMessage("targetBundleTemplateModel", targetBundleTemplateModel);
        add(targetBundleTemplateModel, productModel);
        remove(sourceBundleTemplateModel, productModel);
    }


    protected BundleTemplateService getBundleTemplateService()
    {
        return this.bundleTemplateService;
    }


    @Resource
    public void setBundleTemplateService(BundleTemplateService bundleTemplateService)
    {
        this.bundleTemplateService = bundleTemplateService;
    }
}
