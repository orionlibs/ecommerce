package de.hybris.platform.variants.interceptor;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class VariantCategoryValidateInterceptor implements ValidateInterceptor<VariantCategoryModel>
{
    private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
    private L10NService l10NService;
    private SessionService sessionService;


    public void onValidate(VariantCategoryModel variantCategory, InterceptorContext ctx) throws InterceptorException
    {
        Boolean isSyncActive = (Boolean)this.sessionService.getCurrentSession().getAttribute("catalog.sync.active");
        if(BooleanUtils.isNotTrue(isSyncActive))
        {
            List<CategoryModel> subcategories = variantCategory.getCategories();
            validateIntegrityBetweenVariantCategories(variantCategory);
            validateVariantValueCategories(variantCategory, subcategories);
        }
    }


    private void validateVariantValueCategories(VariantCategoryModel variantCategoryToValidate, List<CategoryModel> subcategories) throws InterceptorException
    {
        if(CollectionUtils.isNotEmpty(subcategories))
        {
            List<CategoryModel> categoriesToCheck = new ArrayList<>();
            CollectionUtils.select(subcategories,
                            PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(VariantValueCategoryModel.class)), categoriesToCheck);
            if(CollectionUtils.isNotEmpty(categoriesToCheck))
            {
                List<VariantCategoryModel> variantCategories = new ArrayList<>();
                List<CategoryModel> otherCategories = new ArrayList<>();
                CollectionUtils.select(categoriesToCheck, PredicateUtils.instanceofPredicate(VariantCategoryModel.class), variantCategories);
                CollectionUtils.select(categoriesToCheck,
                                PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(VariantCategoryModel.class)), otherCategories);
                if(CollectionUtils.isNotEmpty(variantCategories) && variantCategories.size() > 1)
                {
                    throw new InterceptorException(getL10NService()
                                    .getLocalizedString("error.variantcategory.onlyonevariantcategoryassubcategoryallowed", new Object[] {variantCategoryToValidate.getCode()}));
                }
                if(CollectionUtils.isNotEmpty(otherCategories))
                {
                    throw new InterceptorException(getL10NService()
                                    .getLocalizedString("error.variantcategory.onlyvariantcategoryassubcategoryallowed", new Object[] {variantCategoryToValidate.getCode()}));
                }
            }
        }
    }


    private void validateIntegrityBetweenVariantCategories(VariantCategoryModel variantCategoryToValidate) throws InterceptorException
    {
        Collection<CategoryModel> supercategories = variantCategoryToValidate.getSupercategories();
        if(CollectionUtils.isNotEmpty(supercategories))
        {
            if(supercategories.size() > 1)
            {
                throw new InterceptorException(
                                getL10NService().getLocalizedString("error.variantcategory.onlyonesupercategoryallowed", new Object[] {variantCategoryToValidate
                                                .getCode()}));
            }
            CollectionUtils.filter(supercategories, PredicateUtils.instanceofPredicate(VariantCategoryModel.class));
            if(CollectionUtils.isEmpty(supercategories))
            {
                throw new InterceptorException(getL10NService()
                                .getLocalizedString("error.variantcategory.onlyvariantcategoryassupercategoryallowed", new Object[] {variantCategoryToValidate.getCode()}));
            }
        }
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    @Required
    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
