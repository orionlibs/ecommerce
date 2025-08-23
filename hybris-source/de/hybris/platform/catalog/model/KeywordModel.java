package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class KeywordModel extends ItemModel
{
    public static final String _TYPECODE = "Keyword";
    public static final String _CATEGORY2KEYWORDRELATION = "Category2KeywordRelation";
    public static final String KEYWORD = "keyword";
    public static final String LANGUAGE = "language";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String PRODUCTS = "products";
    public static final String CATEGORIES = "categories";


    public KeywordModel()
    {
    }


    public KeywordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public KeywordModel(CatalogVersionModel _catalogVersion, String _keyword, LanguageModel _language)
    {
        setCatalogVersion(_catalogVersion);
        setKeyword(_keyword);
        setLanguage(_language);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public KeywordModel(CatalogVersionModel _catalogVersion, String _keyword, LanguageModel _language, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setKeyword(_keyword);
        setLanguage(_language);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getCategories()
    {
        return getCategories(null);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getCategories(Locale loc)
    {
        return (Collection<CategoryModel>)getPersistenceContext().getLocalizedRelationValue("categories", loc);
    }


    @Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
    public String getKeyword()
    {
        return (String)getPersistenceContext().getPropertyValue("keyword");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return getProducts(null);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts(Locale loc)
    {
        return (Collection<ProductModel>)getPersistenceContext().getLocalizedRelationValue("products", loc);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(Collection<CategoryModel> value)
    {
        setCategories(value, null);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(Collection<CategoryModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("categories", loc, value);
    }


    @Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
    public void setKeyword(String value)
    {
        getPersistenceContext().setPropertyValue("keyword", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        setProducts(value, null);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("products", loc, value);
    }
}
