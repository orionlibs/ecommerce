package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.productcockpit.services.catalog.CatalogListService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogListServiceImpl extends CatalogServiceImpl implements CatalogListService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogListServiceImpl.class);


    public boolean assignAndRemoveProducts(Collection<TypedObject> products, CategoryModel newSuperCategory, List<CategoryModel> oldSuperCategories)
    {
        if(isAssignProductsPermitted(products, newSuperCategory, oldSuperCategories))
        {
            List<List<CategoryModel>> originalSuperCategories = new ArrayList<>(products.size());
            List<ProductModel> assignedProducts = new ArrayList<>(products.size());
            for(TypedObject productTypedObject : products)
            {
                originalSuperCategories
                                .add((List<CategoryModel>)((ProductModel)productTypedObject.getObject()).getSupercategories());
                assignedProducts.add((ProductModel)productTypedObject.getObject());
            }
            boolean oldCategoriesChanged = false;
            for(CategoryModel oldSuperCategory : oldSuperCategories)
            {
                if(oldSuperCategory != null && !oldSuperCategory.equals(newSuperCategory))
                {
                    List<ProductModel> oldProducts = new ArrayList<>(oldSuperCategory.getProducts());
                    oldCategoriesChanged = oldProducts.removeAll(assignedProducts);
                    oldSuperCategory.setProducts(oldProducts);
                }
            }
            List<ProductModel> newCategoryProducts = new ArrayList<>(getProducts(newSuperCategory));
            if(Collections.disjoint(newCategoryProducts, assignedProducts))
            {
                newCategoryProducts.addAll(assignedProducts);
            }
            else
            {
                for(ProductModel product : assignedProducts)
                {
                    if(!newCategoryProducts.contains(product))
                    {
                        newCategoryProducts.add(product);
                    }
                }
            }
            newSuperCategory.setProducts(newCategoryProducts);
            if(oldCategoriesChanged)
            {
                List<CategoryModel> categoriesToSave = new ArrayList<>(oldSuperCategories);
                categoriesToSave.add(newSuperCategory);
                this.modelService.saveAll(categoriesToSave);
            }
            else
            {
                this.modelService.save(newSuperCategory);
            }
            for(int i = 0; i < assignedProducts.size(); i++)
            {
                ProductModel p = assignedProducts.get(i);
                JaloConnection.getInstance().logItemModification(p.getPk(),
                                Collections.singletonMap("supercategories", this.modelService.toPersistenceLayer(p.getSupercategories())),
                                Collections.singletonMap("supercategories", this.modelService
                                                .toPersistenceLayer(originalSuperCategories.get(i))), false);
            }
            return true;
        }
        return false;
    }


    public boolean assignAndRemoveCategories(Collection<TypedObject> categories, CategoryModel newSuperCategory)
    {
        Map<CategoryModel, List<CategoryModel>> catMap = new HashMap<>(categories.size());
        for(TypedObject categoryTypedObject : categories)
        {
            List<CategoryModel> originalSupercats = new ArrayList<>(((CategoryModel)categoryTypedObject.getObject()).getSupercategories());
            catMap.put((CategoryModel)categoryTypedObject.getObject(), new ArrayList<>(originalSupercats));
        }
        try
        {
            for(Map.Entry<CategoryModel, List<CategoryModel>> entry : catMap.entrySet())
            {
                List<CategoryModel> supercats = new ArrayList<>((List)entry.getValue());
                CategoryModel cat = (CategoryModel)entry.getKey();
                List<CategoryModel> newSuperCats = Arrays.asList(new CategoryModel[] {newSuperCategory});
                cat.setSupercategories(newSuperCats);
                this.modelService.save(cat);
                if(!newSuperCategory.equals(cat))
                {
                    JaloConnection.getInstance().logItemModification(cat.getPk(),
                                    Collections.singletonMap("supercategories", this.modelService.toPersistenceLayer(newSuperCats)),
                                    Collections.singletonMap("supercategories", this.modelService.toPersistenceLayer(supercats)), false);
                }
            }
            return true;
        }
        catch(ModelSavingException e)
        {
            LOGGER.error(e.getMessage(), (Throwable)e);
            return false;
        }
    }
}
