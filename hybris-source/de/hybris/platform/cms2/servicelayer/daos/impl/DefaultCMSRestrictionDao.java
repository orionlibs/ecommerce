package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSRestrictionDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.List;

public class DefaultCMSRestrictionDao extends AbstractItemDao implements CMSRestrictionDao
{
    public List<AbstractRestrictionModel> findRestrictionsForPage(ComposedTypeModel composedType, AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {n:target} FROM {RestrictionsForPages AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} = ?page AND {r:catalogVersion} = ?catalogVersion AND {r:itemType} = ?type";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {n:target} FROM {RestrictionsForPages AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} = ?page AND {r:catalogVersion} = ?catalogVersion AND {r:itemType} = ?type");
        fQuery.addQueryParameter("page", page);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        fQuery.addQueryParameter("type", composedType);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictionsForPage(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        return findRestrictionsForPageInternal(page, catalogVersion).getResult();
    }


    public int getNumPageRestrictions(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        return findRestrictionsForPageInternal(page, catalogVersion).getCount();
    }


    protected SearchResult<AbstractRestrictionModel> findRestrictionsForPageInternal(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {n:target} FROM {RestrictionsForPages AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} = ?page AND {r:catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {n:target} FROM {RestrictionsForPages AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} = ?page AND {r:catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("page", page);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        return search(fQuery);
    }


    public List<AbstractRestrictionModel> findRestrictionsById(String id, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractRestriction} WHERE {uid} = ?uid AND {catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractRestriction} WHERE {uid} = ?uid AND {catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("uid", id);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictionsByName(String name, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractRestriction} WHERE {name} = ?name AND {catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractRestriction} WHERE {name} = ?name AND {catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("name", name);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictionsByType(ComposedTypeModel composedType, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractRestriction} WHERE {itemType} = ?type AND {catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractRestriction} WHERE {itemType} = ?type AND {catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("type", composedType);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictions(CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {r:pk} FROM {AbstractRestriction AS r} WHERE {r:catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {r:pk} FROM {AbstractRestriction AS r} WHERE {r:catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictionsByTypeNotLinkedToPage(AbstractPageModel page, ComposedTypeModel composedType, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {r:pk} FROM {AbstractRestriction} AS rWHERE {r:itemType} = ?type AND {r:catalogVersion} = ?catalogVersion AND NOT EXISTS ({{SELECT {n:target}FROM {" + GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES + "} AS n WHERE {n:target} = {r:pk} AND {n:source} = ?page}})";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("page", page);
        fQuery.addQueryParameter("type", composedType);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }


    public List<CategoryModel> findCategoriesByRestriction(CMSCategoryRestrictionModel restriction)
    {
        String query = "SELECT {cat.pk} from { Category as cat JOIN CategoriesForRestriction as rel ON {cat.pk} = {rel.target}}  WHERE {rel.source} = ?restriction";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {cat.pk} from { Category as cat JOIN CategoriesForRestriction as rel ON {cat.pk} = {rel.target}}  WHERE {rel.source} = ?restriction");
        fQuery.addQueryParameter("restriction", restriction);
        SearchResult<CategoryModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ProductModel> findProductsByRestriction(CMSProductRestrictionModel restriction)
    {
        String query = "SELECT {prod.pk} from { Product as prod JOIN ProductsForRestriction as rel ON {prod.pk} = {rel.target}}  WHERE {rel.source} = ?restriction";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {prod.pk} from { Product as prod JOIN ProductsForRestriction as rel ON {prod.pk} = {rel.target}}  WHERE {rel.source} = ?restriction");
        fQuery.addQueryParameter("restriction", restriction);
        SearchResult<ProductModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractRestrictionModel> findRestrictionsForComponents(Collection<AbstractCMSComponentModel> components, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT DISTINCT {n:target} FROM {RestrictionsForComponents AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} in (?components) AND {r:catalogVersion} = ?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT DISTINCT {n:target} FROM {RestrictionsForComponents AS n LEFT JOIN AbstractRestriction AS r ON {n:target} = {r:pk} } WHERE {n:source} in (?components) AND {r:catalogVersion} = ?catalogVersion");
        fQuery.addQueryParameter("components", components);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<AbstractRestrictionModel> result = search(fQuery);
        return result.getResult();
    }
}
