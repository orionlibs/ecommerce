package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CatalogServiceImpl extends AbstractServiceImpl implements CatalogService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);
    private UIAccessRightService accessService;
    private SessionService sessionService;
    private SearchRestrictionService searchRestrictionService;
    private CatalogService catalogService;
    private CatalogVersionService catalogVersionService;
    private UserService userService;


    public List<CatalogModel> getAvailableCatalogs(UserModel u)
    {
        List<CatalogModel> catalogs = new ArrayList<>();
        if(this.userService.isAdmin(u))
        {
            catalogs.addAll(this.catalogService.getAllCatalogs());
        }
        else
        {
            Collection<CatalogVersionModel> versions = this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)u);
            Set<CatalogModel> catalogSet = new LinkedHashSet<>();
            for(CatalogVersionModel catalogVersion : versions)
            {
                catalogSet.add(catalogVersion.getCatalog());
            }
            catalogs.addAll(catalogSet);
        }
        return catalogs;
    }


    public List<CatalogVersionModel> getCatalogVersions(CatalogModel catalog, UserModel u)
    {
        List<CatalogVersionModel> ret = new ArrayList<>();
        for(CatalogVersionModel catalogVersion : getAllCatalogVersions(u))
        {
            if(catalogVersion.getCatalog().equals(catalog))
            {
                ret.add(catalogVersion);
            }
        }
        return ret;
    }


    public List<CatalogVersionModel> getAllCatalogVersions(UserModel u)
    {
        List<CatalogVersionModel> catalogVersions = new ArrayList<>();
        if(this.userService.isAdmin(u))
        {
            catalogVersions.addAll(this.catalogVersionService.getAllCatalogVersions());
        }
        else
        {
            catalogVersions.addAll(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)u));
        }
        return catalogVersions;
    }


    public List<CatalogVersionModel> getSortedCatalogVersions(UserModel u)
    {
        List<CatalogVersionModel> ret = getAllCatalogVersions(u);
        Collections.sort(ret, (Comparator<? super CatalogVersionModel>)new Object(this));
        return ret;
    }


    private Collection<String> getSubcategoriesHavingSubcategoriesList(CategoryModel category)
    {
        Set<String> ret = new HashSet<>();
        String query = "SELECT DISTINCT({sr.target}) FROM {CategoryCategoryRelation AS sr JOIN CategoryCategoryRelation AS tr ON {sr.target} = {tr.source}} WHERE {sr.source} = " + category.getPk();
        ret.addAll(JaloSession.getCurrentSession().getFlexibleSearch().search(query, String.class).getResult());
        return Collections.unmodifiableSet(ret);
    }


    private Collection<String> getSubcategoriesHavingSubcategoriesList(CatalogVersionModel catalogVersion)
    {
        Set<String> ret = new HashSet<>();
        String cpk = catalogVersion.getPk().toString();
        String query = "SELECT DISTINCT({v:pk}) FROM {Category AS v JOIN CategoryCategoryRelation AS rel ON {v:pk} = {rel.source}} WHERE {v:catalogVersion}=" + cpk
                        + " AND NOT EXISTS \t({{SELECT {rel:pk} \tFROM {CategoryCategoryRelation AS rel JOIN Category AS c ON {c:pk}={rel:source} } \tWHERE {c:catalogVersion}=" + cpk + " AND {rel:target}={v:pk} }})";
        ret.addAll(JaloSession.getCurrentSession().getFlexibleSearch().search(query, String.class).getResult());
        return Collections.unmodifiableSet(ret);
    }


    public Collection<String> getSubcategoriesHavingSubcategories(ItemModel item)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("item must not be null");
        }
        if(item instanceof CatalogVersionModel)
        {
            return getSubcategoriesHavingSubcategoriesList((CatalogVersionModel)item);
        }
        if(item instanceof CategoryModel)
        {
            return getSubcategoriesHavingSubcategoriesList((CategoryModel)item);
        }
        throw new IllegalArgumentException(String.format("Unexpected type of argument: %s. %s or %s expected", new Object[] {item.getClass(), CatalogVersionModel.class, CategoryModel.class}));
    }


    public List<CategoryModel> getCategories(CatalogVersionModel version)
    {
        this.modelService.refresh(version);
        return version.getRootCategories();
    }


    public List<CategoryModel> getCategories(CategoryModel category)
    {
        this.modelService.refresh(category);
        return category.getCategories();
    }


    public CatalogModel getCatalog(CatalogVersionModel version)
    {
        return version.getCatalog();
    }


    public CatalogVersionModel getCatalogVersion(CategoryModel category)
    {
        return category.getCatalogVersion();
    }


    public Collection<CategoryModel> getAllSupercategories(CategoryModel category)
    {
        return category.getAllSupercategories();
    }


    public List<CategoryModel> getCategoryPath(CategoryModel category)
    {
        List<CategoryModel> ret = new ArrayList<>();
        Category current = ((Category)this.modelService.getSource(category)).getSupercategory();
        while(current != null)
        {
            ret.add((CategoryModel)this.modelService.get(current));
            current = current.getSupercategory();
        }
        return ret;
    }


    public List<CatalogVersionModel> getAvailableCatalogVersions()
    {
        return getAllCatalogVersions((UserModel)this.modelService.get(JaloSession.getCurrentSession().getUser()));
    }


    public long getCategoryCount(CatalogVersionModel version)
    {
        return ((CatalogVersion)this.modelService.getSource(version)).getRootCategoriesCount();
    }


    public long getCategoryCount(CategoryModel category)
    {
        return ((Category)this.modelService.getSource(category)).getSubcategoryCount();
    }


    public List<MacFinderTreeNode> getItems(MacFinderTreeNode wrappedCategory, CatalogVersionModel version, boolean includSubCategory, MacFinderTreeNode connectedItem)
    {
        List<MacFinderTreeNode> ret = null;
        try
        {
            Item category = null;
            if(wrappedCategory != null && wrappedCategory.getOriginalItem() != null &&
                            !TypeTools.checkInstanceOfCatalogVersion(getTypeService(), wrappedCategory.getOriginalItem()))
            {
                category = (Item)getModelService().getSource(wrappedCategory.getOriginalItem().getObject());
            }
            ret = connectedItem.getDao().getItems(category, (CatalogVersion)this.modelService.getSource(version), includSubCategory);
        }
        catch(Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return (ret == null) ? Collections.EMPTY_LIST : ret;
    }


    public int getItemCount(MacFinderTreeNode wrappedCategory, CatalogVersionModel version, boolean includeSubCategory, MacFinderTreeNode connectedItem)
    {
        int ret = 0;
        try
        {
            Item category = null;
            if(wrappedCategory != null && wrappedCategory.getOriginalItem() != null &&
                            !TypeTools.checkInstanceOfCatalogVersion(getTypeService(), wrappedCategory.getOriginalItem()))
            {
                category = (Item)getModelService().getSource(wrappedCategory.getOriginalItem().getObject());
            }
            ret = connectedItem.getDao().getItemCount(category, (CatalogVersion)this.modelService.getSource(version), includeSubCategory);
        }
        catch(Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return ret;
    }


    public CatalogVersionModel getCatalogVersion(PK pk)
    {
        CatalogVersionModel ret = null;
        try
        {
            Item item = JaloSession.getCurrentSession().getItem(pk);
            CatalogVersion cv = CatalogManager.getInstance().getCatalogVersion(item);
            ret = (CatalogVersionModel)this.modelService.get(cv);
        }
        catch(Exception e)
        {
            LOGGER.warn("Could not find catalog version for item (PK: " + pk + ")", e);
        }
        return ret;
    }


    public boolean assignProduct(TypedObject product, CatalogVersionModel catalogVersionModel, CategoryModel removeFromCategory)
    {
        if(isAssignProductPermitted(product, null, null) && isAssignCatalogVersionPermitted(product))
        {
            Object o = getModelService().getSource(product.getObject());
            if(o instanceof Product && catalogVersionModel != null)
            {
                Product p = (Product)o;
                CatalogVersion catVer = (CatalogVersion)this.modelService.getSource(catalogVersionModel);
                Collection<Category> originalSuperCategories = CategoryManager.getInstance().getSupercategories(p);
                Collection<Category> newCategories = new HashSet<>(originalSuperCategories);
                for(Category category : originalSuperCategories)
                {
                    if(CatalogManager.getInstance().getCatalogVersion(category).equals(catVer))
                    {
                        newCategories.remove(category);
                    }
                }
                if(removeFromCategory != null)
                {
                    newCategories.remove(this.modelService.getSource(removeFromCategory));
                }
                CatalogVersion originalCatalogVersion = CatalogManager.getInstance().getCatalogVersion(p);
                CategoryManager.getInstance().setSupercategories(p, newCategories);
                CatalogManager.getInstance().setCatalogVersion(p, catVer);
                Map<String, Object> oldMap = new HashMap<>();
                Map<String, Object> newMap = new HashMap<>();
                newMap.put(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, CategoryManager.getInstance().getSupercategories(p));
                newMap.put(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, CatalogManager.getInstance().getCatalogVersion(p));
                oldMap.put(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, originalSuperCategories);
                oldMap.put(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, originalCatalogVersion);
                JaloConnection.getInstance().logItemModification((Item)p, newMap, oldMap, false);
                return true;
            }
        }
        return false;
    }


    public boolean assignProduct(TypedObject product, CategoryModel newSuperCategory, CategoryModel removeFromCategory)
    {
        if(isAssignProductPermitted(product, newSuperCategory, removeFromCategory))
        {
            Object o = getModelService().getSource(product.getObject());
            if(o instanceof Product)
            {
                Product p = (Product)o;
                Collection<Category> originalSuperCategories = CategoryManager.getInstance().getSupercategories(p);
                if(removeFromCategory != null)
                {
                    Category rscat = (Category)this.modelService.getSource(removeFromCategory);
                    rscat.removeProduct(p);
                }
                Category nscat = (Category)this.modelService.getSource(newSuperCategory);
                if(!getProducts(nscat).contains(p))
                {
                    nscat.addProduct(p);
                }
                JaloConnection.getInstance().logItemModification((Item)p,
                                Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES,
                                                CategoryManager.getInstance().getSupercategories(p)),
                                Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, originalSuperCategories), false);
                return true;
            }
            LOGGER.error("TypedObject doesn't contain a product");
        }
        else
        {
            LOGGER.warn("Assign operation not permitted.");
        }
        return false;
    }


    public boolean assignProducts(Collection<TypedObject> products, CategoryModel newSuperCategory, CategoryModel oldSuperCategory)
    {
        if(isAssignProductsPermitted(products, newSuperCategory, oldSuperCategory))
        {
            List<List<CategoryModel>> originalSuperCategories = new ArrayList<>(products.size());
            List<ProductModel> assignedProducts = new ArrayList<>(products.size());
            for(TypedObject productTypedObject : products)
            {
                Object object = productTypedObject.getObject();
                if(object instanceof ProductModel)
                {
                    originalSuperCategories.add((List<CategoryModel>)((ProductModel)object).getSupercategories());
                    assignedProducts.add((ProductModel)object);
                }
            }
            boolean oldCategoryChanged = false;
            if(oldSuperCategory != null)
            {
                List<ProductModel> oldProducts = new ArrayList<>(oldSuperCategory.getProducts());
                oldCategoryChanged = oldProducts.removeAll(assignedProducts);
                oldSuperCategory.setProducts(oldProducts);
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
            if(oldCategoryChanged)
            {
                this.modelService.saveAll(new Object[] {oldSuperCategory, newSuperCategory});
            }
            else
            {
                this.modelService.save(newSuperCategory);
            }
            for(int i = 0; i < assignedProducts.size(); i++)
            {
                ProductModel p = assignedProducts.get(i);
                JaloConnection.getInstance().logItemModification((Item)this.modelService.getSource(p),
                                Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, p.getSupercategories()),
                                Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, originalSuperCategories
                                                .get(i)), false);
            }
            return true;
        }
        return false;
    }


    public boolean moveCategories(Collection<TypedObject> categories, CategoryModel newSuperCategory, CategoryModel removeCategory)
    {
        if(isMoveCategoriesPermitted(categories, newSuperCategory, removeCategory))
        {
            Map<CategoryModel, List<CategoryModel>> catMap = new HashMap<>(categories.size());
            List<CategoryModel> originalSupercats = null;
            for(TypedObject categoryTypedObject : categories)
            {
                Object object = categoryTypedObject.getObject();
                originalSupercats = new ArrayList<>(((CategoryModel)object).getSupercategories());
                catMap.put((CategoryModel)object, new ArrayList<>(originalSupercats));
            }
            try
            {
                for(Map.Entry<CategoryModel, List<CategoryModel>> entry : catMap.entrySet())
                {
                    List<CategoryModel> supercats = new ArrayList<>((List)entry.getValue());
                    if(!supercats.contains(newSuperCategory))
                    {
                        supercats.add(newSuperCategory);
                    }
                    if(removeCategory != null)
                    {
                        supercats.remove(removeCategory);
                    }
                    CategoryModel cat = (CategoryModel)entry.getKey();
                    cat.setSupercategories(supercats);
                    this.modelService.save(cat);
                    JaloConnection.getInstance().logItemModification((Item)this.modelService.getSource(cat),
                                    Collections.singletonMap("supercategories", supercats),
                                    Collections.singletonMap("supercategories", originalSupercats), false);
                }
                return true;
            }
            catch(ModelSavingException e)
            {
                LOGGER.warn("Move operation not permitted - cyclic reference.", (Throwable)e);
                return false;
            }
        }
        LOGGER.warn("Move operation not permitted.");
        return false;
    }


    public boolean moveCategory(CategoryModel category, CategoryModel newSuperCategory, CategoryModel removeCategory)
    {
        if(isMoveCategoryPermitted(category, newSuperCategory, removeCategory))
        {
            Category cat = (Category)this.modelService.getSource(category);
            Category nscat = (Category)this.modelService.getSource(newSuperCategory);
            Category rscat = (removeCategory == null) ? null : (Category)this.modelService.getSource(removeCategory);
            List<Category> supercats = new ArrayList<>(cat.getSupercategories());
            List<Category> originalSupercats = new ArrayList<>(supercats);
            if(!supercats.contains(nscat))
            {
                supercats.add(nscat);
            }
            if(rscat != null)
            {
                supercats.remove(rscat);
            }
            try
            {
                cat.setSupercategories(supercats);
                JaloConnection.getInstance().logItemModification((Item)cat,
                                Collections.singletonMap("supercategories", supercats),
                                Collections.singletonMap("supercategories", originalSupercats), false);
                return true;
            }
            catch(JaloInvalidParameterException e)
            {
                LOGGER.warn("Move operation not permitted - cyclic reference.", (Throwable)e);
                return false;
            }
        }
        LOGGER.warn("Move operation not permitted.");
        return false;
    }


    public boolean isAssignProductPermitted(TypedObject product, CategoryModel newSuperCategory, CategoryModel removeFromCategory)
    {
        PropertyDescriptor prodDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Product.supercategories");
        return (getUiAccessRightService().isWritable((ObjectType)product.getType(), product, prodDesc, false) &&
                        getUiAccessRightService().isWritable(getTypeService().getObjectType(GeneratedCatalogConstants.TC.CATEGORY),
                                        getTypeService().wrapItem(newSuperCategory)) &&
                        getUiAccessRightService().isWritable(getTypeService().getObjectType(GeneratedCatalogConstants.TC.CATEGORY),
                                        getTypeService().wrapItem(removeFromCategory)));
    }


    protected boolean isAssignCatalogVersionPermitted(TypedObject product)
    {
        PropertyDescriptor prodDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Product.catalogVersion");
        return getUiAccessRightService().isWritable((ObjectType)product.getType(), product, prodDesc, false);
    }


    protected Collection<List<Category>> getCategoryPaths(Category category)
    {
        return category.getPaths();
    }


    protected Collection<Product> getProducts(Category category)
    {
        Collection<Product> ret = null;
        SessionContext ctx = null;
        try
        {
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            ret = category.getProducts();
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return ret;
    }


    protected List<ProductModel> getProducts(CategoryModel category)
    {
        List<ProductModel> products;
        if(UITools.searchRestrictionsDisabledInCockpit())
        {
            products = (List<ProductModel>)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, category));
        }
        else
        {
            products = category.getProducts();
        }
        return (products == null) ? Collections.EMPTY_LIST : products;
    }


    protected boolean isAssignProductsPermitted(Collection<TypedObject> products, CategoryModel newSuperCategory, CategoryModel removeFromCategory)
    {
        return isAssignProductsPermitted(products, newSuperCategory,
                        (removeFromCategory != null) ? Collections.<CategoryModel>singletonList(removeFromCategory) : null);
    }


    protected boolean isAssignProductsPermitted(Collection<TypedObject> products, CategoryModel newSuperCategory, List<CategoryModel> removeFromCategories)
    {
        PropertyDescriptor catProdDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Category.products");
        if(newSuperCategory != null)
        {
            TypedObject to = getTypeService().wrapItem(newSuperCategory);
            if(!getUiAccessRightService().isWritable((ObjectType)to.getType(), to, catProdDesc, false))
            {
                return false;
            }
        }
        if(CollectionUtils.isNotEmpty(removeFromCategories))
        {
            for(CategoryModel removeFromCategory : removeFromCategories)
            {
                TypedObject to = getTypeService().wrapItem(removeFromCategory);
                if(!getUiAccessRightService().isWritable((ObjectType)to.getType(), to, catProdDesc, false))
                {
                    return false;
                }
            }
        }
        PropertyDescriptor prodDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Product.supercategories");
        for(TypedObject product : products)
        {
            if(!getUiAccessRightService().isWritable((ObjectType)product.getType(), product, prodDesc, false))
            {
                return false;
            }
        }
        return true;
    }


    public boolean isMoveCategoriesPermitted(Collection<TypedObject> categories, CategoryModel newSuperCategory, CategoryModel removeFromCategory)
    {
        PropertyDescriptor catProdDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Category.categories");
        if(newSuperCategory != null)
        {
            TypedObject to = getTypeService().wrapItem(newSuperCategory);
            if(!getUiAccessRightService().isWritable((ObjectType)to.getType(), to, catProdDesc, false))
            {
                return false;
            }
        }
        if(removeFromCategory != null)
        {
            TypedObject to = getTypeService().wrapItem(removeFromCategory);
            if(!getUiAccessRightService().isWritable((ObjectType)to.getType(), to, catProdDesc, false))
            {
                return false;
            }
        }
        PropertyDescriptor prodDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Category.supercategories");
        for(TypedObject category : categories)
        {
            if(!getUiAccessRightService().isWritable((ObjectType)category.getType(), category, prodDesc, false))
            {
                return false;
            }
        }
        Set<Category> categorySet = new HashSet<>();
        for(TypedObject category : categories)
        {
            categorySet.add((Category)this.modelService.getSource(category.getObject()));
        }
        Category nscat = (Category)this.modelService.getSource(newSuperCategory);
        Collection<List<Category>> paths = getCategoryPaths(nscat);
        for(List<Category> list : paths)
        {
            for(Category pathCat : list)
            {
                for(Category cat : categorySet)
                {
                    if(pathCat.equals(cat))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public boolean isMoveCategoryPermitted(CategoryModel category, CategoryModel newSuperCategory, CategoryModel removeCategory)
    {
        boolean ret = false;
        Category cat = (Category)this.modelService.getSource(category);
        Category nscat = (Category)this.modelService.getSource(newSuperCategory);
        Collection<List<Category>> paths = getCategoryPaths(nscat);
        for(List<Category> list : paths)
        {
            for(Category pathCat : list)
            {
                if(pathCat.equals(cat))
                {
                    return false;
                }
            }
        }
        ret = getUiAccessRightService().isWritable(getTypeService().getObjectType(GeneratedCatalogConstants.TC.CATEGORY));
        return ret;
    }


    public boolean setAsRootCategory(CategoryModel category)
    {
        Category cat = (Category)this.modelService.getSource(category);
        List<Category> originalSuperCategories = cat.getSupercategories();
        List<TypedObject> ownSuperCategories = getSupercategories(getTypeService().wrapItem(category.getPk()), true);
        for(TypedObject superCat : ownSuperCategories)
        {
            cat.removeFromSupercategories((Category)getModelService().getSource(superCat.getObject()));
        }
        JaloConnection.getInstance().logItemModification((Item)cat,
                        Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, cat.getSupercategories()),
                        Collections.singletonMap(GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES, originalSuperCategories), false);
        this.modelService.refresh(category);
        return true;
    }


    public CategoryModel wrapCategory(TypedObject typedObject)
    {
        CategoryModel ret = null;
        if(typedObject != null)
        {
            Object object = typedObject.getObject();
            if(object instanceof CategoryModel)
            {
                ret = (CategoryModel)object;
            }
        }
        return ret;
    }


    public CatalogVersionModel wrapCatalogVersion(TypedObject typedObject)
    {
        CatalogVersionModel ret = null;
        if(typedObject != null)
        {
            Object object = typedObject.getObject();
            if(object instanceof CatalogVersionModel)
            {
                ret = (CatalogVersionModel)object;
            }
        }
        return ret;
    }


    public TypedObject getCatalogVersionTypedObject(CatalogVersionModel catVer)
    {
        return getTypeService().wrapItem(catVer.getPk());
    }


    public List<TypedObject> getSupercategories(TypedObject categorizableItem, boolean fromSameCatalogVersionOnly)
    {
        ItemModel item = (ItemModel)categorizableItem.getObject();
        Item it = (Item)getModelService().getSource(item);
        List<Category> supercategories = new ArrayList<>();
        CatalogVersion catalogVersion = CatalogManager.getInstance().getCatalogVersion(it);
        if(item instanceof ProductModel)
        {
            supercategories.addAll(CategoryManager.getInstance().getSupercategories((Product)it));
        }
        else if(item instanceof CategoryModel)
        {
            supercategories.addAll(((Category)it).getSupercategories());
        }
        if(fromSameCatalogVersionOnly && catalogVersion != null)
        {
            Set<Category> filtered = new HashSet<>(supercategories.size());
            for(Category cat : supercategories)
            {
                if(!catalogVersion.equals(CatalogManager.getInstance().getCatalogVersion(cat)))
                {
                    filtered.add(cat);
                }
            }
            for(Category cat : filtered)
            {
                supercategories.remove(cat);
            }
        }
        return getTypeService().wrapItems(supercategories);
    }


    public void addToCategories(TypedObject categorizableItem, List<TypedObject> categories)
    {
        ItemModel item = (ItemModel)categorizableItem.getObject();
        Item it = (Item)getModelService().getSource(item);
        List<Category> supercategories = new ArrayList<>();
        if(item instanceof ProductModel)
        {
            supercategories.addAll(CategoryManager.getInstance().getSupercategories((Product)it));
        }
        else if(item instanceof CategoryModel)
        {
            supercategories.addAll(((Category)it).getSupercategories());
        }
        for(TypedObject cat : categories)
        {
            Category category = (Category)getModelService().getSource(cat.getObject());
            if(!supercategories.contains(category))
            {
                supercategories.add(category);
            }
        }
        if(item instanceof ProductModel)
        {
            CategoryManager.getInstance().setSupercategories((Product)it, supercategories);
        }
        else if(item instanceof CategoryModel)
        {
            ((Category)it).setSupercategories(supercategories);
        }
    }


    public void removeFromCategories(TypedObject categorizableItem, List<TypedObject> categories)
    {
        ItemModel item = (ItemModel)categorizableItem.getObject();
        Item it = (Item)getModelService().getSource(item);
        List<Category> supercategories = new ArrayList<>();
        if(item instanceof ProductModel)
        {
            supercategories.addAll(CategoryManager.getInstance().getSupercategories((Product)it));
        }
        else if(item instanceof CategoryModel)
        {
            supercategories.addAll(((Category)it).getSupercategories());
        }
        for(TypedObject cat : categories)
        {
            Category category = (Category)getModelService().getSource(cat.getObject());
            supercategories.remove(category);
        }
        if(item instanceof ProductModel)
        {
            CategoryManager.getInstance().setSupercategories((Product)it, supercategories);
        }
        else if(item instanceof CategoryModel)
        {
            ((Category)it).setSupercategories(supercategories);
        }
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService accessService)
    {
        this.accessService = accessService;
    }


    public UIAccessRightService getUiAccessRightService()
    {
        return this.accessService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
