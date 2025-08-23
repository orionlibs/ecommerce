package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.RestrictionEvaluationException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CMSRestrictionService
{
    boolean evaluate(AbstractRestrictionModel paramAbstractRestrictionModel, RestrictionData paramRestrictionData) throws RestrictionEvaluationException;


    boolean evaluateCMSComponent(AbstractCMSComponentModel paramAbstractCMSComponentModel, RestrictionData paramRestrictionData);


    List<AbstractCMSComponentModel> evaluateCMSComponents(List<AbstractCMSComponentModel> paramList, RestrictionData paramRestrictionData);


    Collection<AbstractPageModel> evaluatePages(Collection<AbstractPageModel> paramCollection, RestrictionData paramRestrictionData);


    Collection<String> getCategoryCodesForRestriction(CMSCategoryRestrictionModel paramCMSCategoryRestrictionModel);


    Collection<String> getProductCodesForRestriction(CMSProductRestrictionModel paramCMSProductRestrictionModel);


    default Collection<AbstractRestrictionModel> getOwnRestrictionsForPage(AbstractPageModel pageModel, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }


    default Collection<AbstractRestrictionModel> getOwnRestrictionsForComponents(Collection<AbstractCMSComponentModel> components, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }


    default boolean relatedSharedSlots(AbstractRestrictionModel restrictionModel)
    {
        return false;
    }
}
