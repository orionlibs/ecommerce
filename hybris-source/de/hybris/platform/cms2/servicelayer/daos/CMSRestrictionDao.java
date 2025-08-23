package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CMSRestrictionDao extends Dao
{
    List<AbstractRestrictionModel> findRestrictionsForPage(ComposedTypeModel paramComposedTypeModel, AbstractPageModel paramAbstractPageModel, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractRestrictionModel> findRestrictionsById(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractRestrictionModel> findRestrictionsByName(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractRestrictionModel> findRestrictionsByType(ComposedTypeModel paramComposedTypeModel, CatalogVersionModel paramCatalogVersionModel);


    List<AbstractRestrictionModel> findRestrictions(CatalogVersionModel paramCatalogVersionModel);


    List<AbstractRestrictionModel> findRestrictionsByTypeNotLinkedToPage(AbstractPageModel paramAbstractPageModel, ComposedTypeModel paramComposedTypeModel, CatalogVersionModel paramCatalogVersionModel);


    List<CategoryModel> findCategoriesByRestriction(CMSCategoryRestrictionModel paramCMSCategoryRestrictionModel);


    List<ProductModel> findProductsByRestriction(CMSProductRestrictionModel paramCMSProductRestrictionModel);


    default List<AbstractRestrictionModel> findRestrictionsForPage(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }


    default int getNumPageRestrictions(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        return 0;
    }


    default List<AbstractRestrictionModel> findRestrictionsForComponents(Collection<AbstractCMSComponentModel> components, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }
}
