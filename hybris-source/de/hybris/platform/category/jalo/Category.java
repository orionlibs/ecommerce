package de.hybris.platform.category.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Category extends GeneratedCategory
{
    public static final String DISABLE_SUBCATEGORY_REMOVALCHECK = "disable_subcategory_removalcheck";
    public static final String DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY = "disable_setallowedprincipal_recursively";
    private static final int MAX_CATEGORY_DEPTH = 50;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DISABLE_CYCLIC_CHECKS = Constants.DISABLE_CYCLIC_CHECKS;


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "MandatoryAttributesValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        adjustInitialAttributes(ctx, allAttributes);
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("code", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    private void adjustInitialAttributes(SessionContext ctx, Item.ItemAttributeMap initialAttributes) throws JaloBusinessException
    {
        Collection<Category> subCategories = (Collection<Category>)initialAttributes.get("categories");
        Collection<Category> superCategories = (Collection<Category>)initialAttributes.get("supercategories");
        if(subCategories != null && !subCategories.isEmpty() && superCategories != null && !superCategories.isEmpty())
        {
            assertNoCycle(ctx, superCategories, subCategories, true);
        }
    }


    public String toString()
    {
        return super.toString();
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public String getCode()
    {
        return super.getCode();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getSubcategories()
    {
        return getCategories();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<Category> getSubcategories(SessionContext ctx)
    {
        return getCategories(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getSubcategoryCount()
    {
        return getLinkedItemsCount(true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getAllSubcategories()
    {
        return getAllSubcategories(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getAllSubcategories(SessionContext ctx)
    {
        Set<PK> ret = null;
        LinkManager linkman = LinkManager.getInstance();
        LazyLoadItemList currentLevel = (LazyLoadItemList)linkman.getAllLinkedItems(ctx, Collections.singletonList(this), true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
        while(currentLevel != null && !currentLevel.isEmpty())
        {
            for(int i = currentLevel.size() - 1; i >= 0; i--)
            {
                PK pk = currentLevel.getPK(i);
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                if(!ret.add(pk))
                {
                    currentLevel.remove(i);
                }
            }
            if(currentLevel.isEmpty())
            {
                break;
            }
            LazyLoadItemList nextLevel = (LazyLoadItemList)linkman.getAllLinkedItems(ctx, (Collection)currentLevel, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
            currentLevel = nextLevel;
        }
        return (ret == null || ret.isEmpty()) ? Collections.EMPTY_LIST :
                        (Collection<Category>)new LazyLoadItemList(WrapperFactory.getPrefetchLanguages(ctx), new ArrayList<>(ret), 100);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getAllSubcategoriesCount()
    {
        return getAllSubcategories().size();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getAllProductsCount()
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT( {p:").append(PK).append("} ) ");
        query.append("FROM {").append(TypeManager.getInstance().getComposedType(Product.class).getCode()).append(" AS p ");
        query.append("JOIN ").append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append("* AS c2pRel ");
        query.append("ON {c2pRel:").append("target").append("}={p:").append(PK).append("} } ");
        query.append("WHERE ");
        query.append("{c2pRel:").append("source").append("} ");
        Collection<Category> allSub = getAllSubcategories();
        if(allSub.isEmpty())
        {
            query.append(" = ?me");
            return ((Integer)FlexibleSearch.getInstance()
                            .search(query.toString(), Collections.singletonMap("me", this), Integer.class)
                            .getResult()
                            .get(0)).intValue();
        }
        query.append(" IN ( ?categories )");
        int count = 0;
        List<Category> categoryList = new ArrayList<>(allSub);
        categoryList.add(this);
        int pageSize = getTenant().getDataSource().getMaxPreparedParameterCount();
        if(pageSize == -1)
        {
            pageSize = categoryList.size();
        }
        int offset = 0;
        while(offset < categoryList.size())
        {
            int currentPageEnd = Math.min(categoryList.size(), offset + pageSize);
            count += (
                            (Integer)FlexibleSearch.getInstance().search(query.toString(),
                                                            Collections.singletonMap("categories", categoryList
                                                                            .subList(offset, currentPageEnd)), Integer.class)
                                            .getResult().get(0)).intValue();
            offset += pageSize;
        }
        return count;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setCategories(SessionContext ctx, List<Category> categories) throws JaloInvalidParameterException
    {
        if(!disableChecks(ctx))
        {
            assertNoCycle(ctx, Collections.singleton(this), categories, true);
        }
        super.setCategories(ctx, categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCategories(Category... categories)
    {
        setCategories(getSession().getSessionContext(), categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCategories(SessionContext ctx, Category... categories)
    {
        setCategories(ctx, (categories == null) ? Collections.EMPTY_LIST : Arrays.<Category>asList(categories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addToCategories(SessionContext ctx, Category cat) throws JaloInvalidParameterException
    {
        if(!disableChecks(ctx))
        {
            assertNoCycle(ctx, Collections.singleton(this), Collections.singleton(cat), true);
        }
        super.addToCategories(ctx, cat);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setSupercategories(SessionContext ctx, List<Category> categories) throws JaloInvalidParameterException
    {
        if(!disableChecks(ctx))
        {
            assertNoCycle(ctx, Collections.singleton(this), categories, false);
        }
        super.setSupercategories(ctx, categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSupercategories(Category... superCategories)
    {
        setSupercategories(getSession().getSessionContext(), superCategories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSupercategories(SessionContext ctx, Category... superCategories)
    {
        setSupercategories(ctx, (superCategories == null) ? Collections.EMPTY_LIST : Arrays.<Category>asList(superCategories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addToSupercategories(SessionContext ctx, Category cat) throws JaloInvalidParameterException
    {
        if(!disableChecks(ctx))
        {
            assertNoCycle(ctx, Collections.singleton(this), Collections.singleton(cat), false);
        }
        super.addToSupercategories(ctx, cat);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean disableChecks(SessionContext ctx)
    {
        return (isInCreate(ctx) || (ctx != null && Boolean.TRUE.equals(ctx.getAttribute(Constants.DISABLE_CYCLIC_CHECKS))));
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void assertNoCycle(SessionContext ctx, Collection<Category> toAddTo, Collection<Category> toCheck, boolean sub) throws JaloInvalidParameterException
    {
        Set<Category> controlSet = new HashSet<>();
        if(sub)
        {
            Set<Category> currentLevel = (toAddTo == null) ? new HashSet<>() : new HashSet<>(toAddTo);
            do
            {
                Set<Category> nextLevel = null;
                for(Category current : currentLevel)
                {
                    if(toCheck != null && toCheck.contains(current))
                    {
                        throw new JaloInvalidParameterException("cannot assign supercategory " + current + " as subcategory of " + toAddTo + " - cycle detected", 0);
                    }
                    controlSet.add(current);
                    Collection<Category> mySuper = current.getSupercategories(ctx);
                    if(mySuper != null && !mySuper.isEmpty())
                    {
                        if(nextLevel == null)
                        {
                            nextLevel = new HashSet<>();
                        }
                        nextLevel.addAll(mySuper);
                    }
                }
                if(nextLevel != null)
                {
                    nextLevel.removeAll(controlSet);
                }
                currentLevel = nextLevel;
            }
            while(currentLevel != null && !currentLevel.isEmpty());
        }
        else
        {
            Set<Category> currentLevel = (toCheck == null) ? new HashSet<>() : new HashSet<>(toCheck);
            do
            {
                Set<Category> nextLevel = null;
                for(Category current : currentLevel)
                {
                    if(toAddTo != null && toAddTo.contains(current))
                    {
                        throw new JaloInvalidParameterException("cannot assign subcategory " + current + " as super category of " + toAddTo + " - cycle detected", 0);
                    }
                    controlSet.add(current);
                    Collection<Category> mySuper = current.getSupercategories(ctx);
                    if(mySuper != null && !mySuper.isEmpty())
                    {
                        if(nextLevel == null)
                        {
                            nextLevel = new HashSet<>();
                        }
                        nextLevel.addAll(mySuper);
                    }
                }
                if(nextLevel != null)
                {
                    nextLevel.removeAll(controlSet);
                }
                currentLevel = nextLevel;
            }
            while(currentLevel != null && !currentLevel.isEmpty());
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSubcategories(List<Category> subcategories)
    {
        setCategories(subcategories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSubcategories(SessionContext ctx, List<Category> subcategories)
    {
        setCategories(ctx, subcategories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSubcategory(Category category)
    {
        addToCategories(category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSubcategory(SessionContext ctx, Category category)
    {
        addToCategories(ctx, category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSubcategories(Collection<Category> categories)
    {
        addSubcategories(getSession().getSessionContext(), categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSubcategories(SessionContext ctx, Collection<Category> categories)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        (categories instanceof List) ? (List)categories : new ArrayList<>(categories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSubcategory(Category category)
    {
        removeFromCategories(category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSubcategory(SessionContext ctx, Category category)
    {
        removeFromCategories(ctx, category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSubcategories(Collection<Category> categories)
    {
        removeSubcategories(getSession().getSessionContext(), categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSubcategories(SessionContext ctx, Collection<Category> categories)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        (categories instanceof List) ? (List)categories : new ArrayList<>(categories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getSupercategory()
    {
        return getSupercategory(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getSupercategory(SessionContext ctx)
    {
        Collection<Category> supercategories = getSupercategories(ctx);
        if(supercategories == null || supercategories.isEmpty())
        {
            return null;
        }
        return supercategories.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getSupercategoryCount()
    {
        return getLinkedItemsCount(false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductService", portingMethod = "getProductsCountForCategory(CategoryModel category)")
    public long getProductsCount()
    {
        return getLinkedItemsCount(true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isEmpty()
    {
        return (getSubcategoryCount() == 0L && getProductsCount() == 0L);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean containsProducts()
    {
        return (getAllProductsCount() > 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getAllSupercategories()
    {
        return getAllSupercategories(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getAllSupercategories(SessionContext ctx)
    {
        Set<PK> ret = null;
        LinkManager linkman = LinkManager.getInstance();
        LazyLoadItemList currentLevel = (LazyLoadItemList)linkman.getAllLinkedItems(ctx, Collections.singletonList(this), false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
        while(!currentLevel.isEmpty())
        {
            for(int i = currentLevel.size() - 1; i >= 0; i--)
            {
                PK pk = currentLevel.getPK(i);
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                if(!ret.add(pk))
                {
                    currentLevel.remove(i);
                }
            }
            if(currentLevel.isEmpty())
            {
                break;
            }
            LazyLoadItemList nextLevel = (LazyLoadItemList)linkman.getAllLinkedItems(ctx, (Collection)currentLevel, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null);
            currentLevel = nextLevel;
        }
        return (ret == null || ret.isEmpty()) ? Collections.EMPTY_LIST :
                        (Collection<Category>)new LazyLoadItemList(WrapperFactory.getPrefetchLanguages(ctx), new ArrayList<>(ret), -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSupercategory(Category category)
    {
        addToSupercategories(category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSupercategory(SessionContext ctx, Category category)
    {
        addToSupercategories(ctx, category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSupercategories(Collection<Category> categories)
    {
        addSupercategories(getSession().getSessionContext(), categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addSupercategories(SessionContext ctx, Collection<Category> categories)
    {
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        (categories instanceof List) ? (List)categories : new ArrayList<>(categories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSupercategory(Category category)
    {
        removeFromSupercategories(category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSupercategory(SessionContext ctx, Category category)
    {
        removeFromSupercategories(ctx, category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSupercategories(Collection<Category> categories)
    {
        removeSupercategories(getSession().getSessionContext(), categories);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeSupercategories(SessionContext ctx, Collection<Category> categories)
    {
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        (categories instanceof List) ? (List)categories : new ArrayList<>(categories));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProduct(Product product)
    {
        addToProducts(product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProduct(SessionContext ctx, Product product)
    {
        addToProducts(ctx, product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProduct(Product product, int position, boolean shift)
    {
        addProduct(getSession().getSessionContext(), product, position, shift);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProduct(SessionContext ctx, Product product, int position, boolean shift)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null, Collections.singletonList(product), position, shift);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProducts(Collection<Product> products)
    {
        addProducts(getSession().getSessionContext(), products);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addProducts(SessionContext ctx, Collection<Product> products)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        (products instanceof List) ? (List)products : new ArrayList<>(products));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeProduct(Product product)
    {
        removeFromProducts(product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeProduct(SessionContext ctx, Product product)
    {
        removeFromProducts(ctx, product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeProducts(Collection<Product> products)
    {
        removeProducts(getSession().getSessionContext(), products);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeProducts(SessionContext ctx, Collection<Product> products)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        (products instanceof List) ? (List)products : new ArrayList<>(products));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<Category> getPath()
    {
        if(isRootAsPrimitive())
        {
            return Collections.singletonList(this);
        }
        List<Category> ret = new LinkedList<>();
        int level = 0;
        for(Category cat = this; cat != null && level++ < 50; cat = cat.getSupercategory())
        {
            ret.add(cat);
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<List<Category>> getPaths()
    {
        return getPathsInternal(0);
    }


    private Collection<List<Category>> getPathsInternal(int level)
    {
        if(level >= 50 || isRootAsPrimitive())
        {
            return Collections.singletonList(Collections.singletonList(this));
        }
        Collection<List<Category>> ret = null;
        for(Category parent : getSupercategories())
        {
            for(List<Category> parentPath : parent.getPathsInternal(level + 1))
            {
                if(ret == null)
                {
                    ret = new LinkedList<>();
                }
                if(!(parentPath instanceof LinkedList))
                {
                    parentPath = new LinkedList<>(parentPath);
                }
                parentPath.add(this);
                ret.add(parentPath);
            }
        }
        return (ret == null) ? Collections.<List<Category>>singletonList(Collections.singletonList(this)) : ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "CategoryRemovalValidator")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        if(!isSubcategoryRemovalCheckDisabled(ctx) && getSubcategoryCount() > 0L)
        {
            throw new ConsistencyCheckException("cannot remove " + toString() + " since this category still has subcategories", 0);
        }
        super.checkRemovable(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Boolean isRoot(SessionContext ctx)
    {
        return (getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null) == 0L) ? Boolean.TRUE :
                        Boolean.FALSE;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Boolean isRoot()
    {
        return isRoot(getSession().getSessionContext());
    }


    public boolean isRootAsPrimitive(SessionContext ctx)
    {
        return Boolean.TRUE.equals(isRoot(ctx));
    }


    public boolean isRootAsPrimitive()
    {
        return Boolean.TRUE.equals(isRoot());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "CategoryPrepareInterceptor", portingMethod = "onPrepare(final CategoryModel model, final InterceptorContext ctx)")
    public void setAllowedPrincipals(SessionContext ctx, List<Principal> newOnes)
    {
        super.setAllowedPrincipals(ctx, newOnes);
        if(!isSetAllowedPrincipalsRecursivelyDisabled(ctx))
        {
            LinkManager linkman = LinkManager.getInstance();
            List toSet = (newOnes == null) ? null : new ArrayList<>(newOnes);
            Collection<Category> allSub = getAllSubcategories(ctx);
            boolean isLazy = allSub instanceof LazyLoadItemList;
            List<Item> subList = isLazy ? (List)allSub : new ArrayList<>(allSub);
            for(int i = 0; i < subList.size(); i += 900)
            {
                LazyLoadItemList range = new LazyLoadItemList(null, Collections.EMPTY_LIST, 100);
                for(int j = 0; i + j < subList.size() && j < 900; j++)
                {
                    range.add(isLazy ? ((LazyLoadItemList)subList).getPK(i + j) : ((Item)subList.get(i + j)).getPK());
                }
                linkman.setAllLinkedItems(ctx, (Collection)range, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, null, toSet, false);
            }
            Collection<Category> allSuper = getAllSupercategories(ctx);
            for(Iterator<Category> it = allSuper.iterator(); it.hasNext(); )
            {
                Category superCat = it.next();
                pullUpAllowedPrincipalInternals(ctx, superCat, newOnes);
            }
        }
    }


    public void setAllowedPrincipalsOnlyForPassedCategory(SessionContext ctx, List<Principal> value)
    {
        super.setAllowedPrincipals(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isSetAllowedPrincipalsRecursivelyDisabled(SessionContext ctx)
    {
        if(ctx != null && (Boolean.TRUE.equals(ctx.getAttribute("disable_setallowedprincipal_recursively")) || Boolean.TRUE
                        .equals(ctx.getAttribute("save.from.service.layer"))))
        {
            return true;
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void pullUpAllowedPrincipalInternals(SessionContext ctx, Category superCat, List<Principal> newOnes)
    {
        if(newOnes != null)
        {
            Set<Principal> current = new HashSet(superCat.getAllowedPrincipals(ctx));
            current.addAll(newOnes);
            CatalogManager.getInstance().setAllowedPrincipalsOnlyForGivenCategory(ctx, superCat, new ArrayList<>(current));
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isSubcategoryRemovalCheckDisabled(SessionContext ctx)
    {
        if(ctx != null)
        {
            if(Boolean.TRUE.equals(ctx.getAttribute("disable_subcategory_removalcheck")))
            {
                return true;
            }
        }
        return false;
    }
}
