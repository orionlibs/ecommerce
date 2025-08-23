package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemCreateException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCampaignRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CMSAdminRestrictionService
{
    Collection<RestrictionTypeModel> getAllRestrictionTypes();


    Collection<RestrictionTypeModel> getAllowedRestrictionTypesForPage(String paramString) throws CMSItemNotFoundException;


    Collection<RestrictionTypeModel> getAllowedRestrictionTypesForPage(AbstractPageModel paramAbstractPageModel);


    Collection<AbstractRestrictionModel> getRestrictionsForPage(String paramString1, String paramString2) throws CMSItemNotFoundException;


    Collection<AbstractRestrictionModel> getRestrictionsForPage(AbstractPageModel paramAbstractPageModel, String paramString) throws CMSItemNotFoundException;


    Collection<AbstractRestrictionModel> getRestrictionsForPage(AbstractPageModel paramAbstractPageModel);


    default boolean hasPageRestrictions(AbstractPageModel page)
    {
        return false;
    }


    Collection<AbstractRestrictionModel> getAllRestrictionsByType(String paramString) throws CMSItemNotFoundException;


    Collection<AbstractRestrictionModel> getAllRestrictionsByTypeNotLinkedToPage(String paramString1, String paramString2) throws CMSItemNotFoundException;


    AbstractRestrictionModel getRestriction(String paramString) throws CMSItemNotFoundException;


    List<AbstractRestrictionModel> getRestrictionsByName(String paramString) throws CMSItemNotFoundException;


    Collection<AbstractRestrictionModel> getAllRestrictions();


    CMSCategoryRestrictionModel createCategoryRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<CategoryModel> paramCollection) throws CMSItemCreateException;


    CMSProductRestrictionModel createProductRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<ProductModel> paramCollection) throws CMSItemCreateException;


    CMSTimeRestrictionModel createTimeRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Date paramDate1, Date paramDate2) throws CMSItemCreateException;


    CMSUserRestrictionModel createUserRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<UserModel> paramCollection) throws CMSItemCreateException;


    CMSUserGroupRestrictionModel createUserGroupRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<UserGroupModel> paramCollection) throws CMSItemCreateException;


    CMSCatalogRestrictionModel createCatalogRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<CatalogModel> paramCollection) throws CMSItemCreateException;


    CMSCampaignRestrictionModel createCampaignRestriction(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, Collection<CampaignModel> paramCollection) throws CMSItemCreateException;


    void createRelation(AbstractPageModel paramAbstractPageModel, AbstractRestrictionModel paramAbstractRestrictionModel);


    void createRelation(String paramString1, String paramString2) throws CMSItemNotFoundException;


    boolean hasOtherRelations(AbstractRestrictionModel paramAbstractRestrictionModel, AbstractPageModel paramAbstractPageModel);


    boolean hasOtherRelations(String paramString1, String paramString2) throws CMSItemNotFoundException;


    void deleteRelation(AbstractRestrictionModel paramAbstractRestrictionModel, AbstractPageModel paramAbstractPageModel);


    void deleteRelation(String paramString1, String paramString2) throws CMSItemNotFoundException;


    List<ProductModel> getProducts(CMSProductRestrictionModel paramCMSProductRestrictionModel, PreviewDataModel paramPreviewDataModel);


    List<CategoryModel> getCategories(CMSCategoryRestrictionModel paramCMSCategoryRestrictionModel, PreviewDataModel paramPreviewDataModel);
}
