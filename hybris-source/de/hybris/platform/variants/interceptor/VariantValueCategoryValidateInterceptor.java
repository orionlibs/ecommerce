package de.hybris.platform.variants.interceptor;

import com.google.common.collect.Sets;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantValueCategoryModel;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class VariantValueCategoryValidateInterceptor implements ValidateInterceptor<VariantValueCategoryModel>
{
    private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
    private L10NService l10NService;
    private SessionService sessionService;


    public void onValidate(VariantValueCategoryModel variantValueCategory, InterceptorContext ctx) throws InterceptorException
    {
        Boolean isSyncActive = (Boolean)this.sessionService.getCurrentSession().getAttribute("catalog.sync.active");
        if(BooleanUtils.isNotTrue(isSyncActive))
        {
            List<CategoryModel> variantCategories = variantValueCategory.getSupercategories();
            if(CollectionUtils.isEmpty(variantCategories))
            {
                throw new InterceptorException(getL10NService().getLocalizedString("error.variantvaluecategory.nosupercategoryfound"));
            }
            if(variantCategories.size() > 1)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("error.variantvaluecategory.maxonesupercategory"));
            }
            if(variantCategories.size() == 1)
            {
                CategoryModel variantCategory = variantCategories.iterator().next();
                if(variantCategory instanceof de.hybris.platform.variants.model.VariantCategoryModel)
                {
                    validateVariantValueCategory(variantValueCategory);
                    List<CategoryModel> siblings = variantCategory.getCategories();
                    validateSequenceWithinSiblings(siblings, variantValueCategory);
                }
                else
                {
                    throw new InterceptorException(getL10NService().getLocalizedString("error.variantvaluecategory.wrongcategorytype"));
                }
            }
        }
    }


    protected void validateVariantValueCategory(VariantValueCategoryModel variantValueCategory) throws InterceptorException
    {
        if(variantValueCategory.getSequence() == null)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("error.variantvaluecategory.nosequencenumberprovided"));
        }
        if(variantValueCategory.getSequence() != null && variantValueCategory.getSequence().intValue() < 0)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("error.variantvaluecategory.negativesequencenumber"));
        }
    }


    protected void validateSequenceWithinSiblings(List<CategoryModel> siblings, VariantValueCategoryModel currentCategory) throws InterceptorException
    {
        HashSet<Integer> sequences = Sets.newHashSet();
        if(!siblings.contains(currentCategory))
        {
            sequences.add(currentCategory.getSequence());
        }
        for(CategoryModel c : siblings)
        {
            if(c instanceof VariantValueCategoryModel)
            {
                VariantValueCategoryModel variantValueCat = (VariantValueCategoryModel)c;
                if(sequences.contains(variantValueCat.getSequence()))
                {
                    throw new InterceptorException(getL10NService().getLocalizedString("error.genericvariantproduct.morethenonecategorywithsamesequence", new Object[] {variantValueCat
                                    .getSequence()}));
                }
                sequences.add(variantValueCat.getSequence());
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
