package de.hybris.platform.variants.interceptor;

import com.google.common.collect.Lists;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class GenericVariantProductValidateInterceptor implements ValidateInterceptor<GenericVariantProductModel>
{
    private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
    private L10NService l10NService;
    private SessionService sessionService;


    public void onValidate(GenericVariantProductModel genericVariant, InterceptorContext ctx) throws InterceptorException
    {
        Boolean isSyncActive = (Boolean)this.sessionService.getCurrentSession().getAttribute("catalog.sync.active");
        if(BooleanUtils.isNotTrue(isSyncActive))
        {
            Collection<CategoryModel> variantValueCategories = genericVariant.getSupercategories();
            if(CollectionUtils.isEmpty(variantValueCategories))
            {
                throw new InterceptorException(localizeForKey("error.genericvariantproduct.wrongsupercategory"));
            }
            validateBaseProductSuperCategories(genericVariant, variantValueCategories);
            validateSupercategories(variantValueCategories);
        }
    }


    protected void validateBaseProductSuperCategories(GenericVariantProductModel genericVariant, Collection<CategoryModel> variantValueCategories) throws InterceptorException
    {
        ProductModel baseProduct = genericVariant.getBaseProduct();
        if(baseProduct == null)
        {
            throw new InterceptorException(
                            getL10NService().getLocalizedString("error.genericvariantproduct.nobaseproduct", new Object[] {genericVariant
                                            .getCode()}));
        }
        Collection<CategoryModel> superCategories = baseProduct.getSupercategories();
        if(CollectionUtils.isNotEmpty(superCategories))
        {
            List<CategoryModel> variantCategoriesOfVariantValueCategories = Lists.newArrayList();
            if(CollectionUtils.isNotEmpty(variantValueCategories))
            {
                Objects.requireNonNull(VariantValueCategoryModel.class);
                Objects.requireNonNull(VariantCategoryModel.class);
                variantCategoriesOfVariantValueCategories = (List<CategoryModel>)variantValueCategories.stream().filter(VariantValueCategoryModel.class::isInstance).flatMap(v -> ((VariantValueCategoryModel)v).getSupercategories().stream()).filter(VariantCategoryModel.class::isInstance)
                                .collect(Collectors.toList());
            }
            Objects.requireNonNull(VariantCategoryModel.class);
            Collection<CategoryModel> baseVariantCategories = (Collection<CategoryModel>)superCategories.stream().filter(VariantCategoryModel.class::isInstance).collect(Collectors.toList());
            if(baseVariantCategories.size() != variantCategoriesOfVariantValueCategories.size())
            {
                throw new InterceptorException(
                                getL10NService().getLocalizedString("error.genericvariantproduct.nosameamountofvariantcategories", new Object[] {genericVariant
                                                .getCode(), Integer.valueOf(variantCategoriesOfVariantValueCategories.size()), baseProduct.getCode(), Integer.valueOf(baseVariantCategories.size())}));
            }
            for(CategoryModel varCategory : variantCategoriesOfVariantValueCategories)
            {
                if(!baseVariantCategories.contains(varCategory))
                {
                    throw new InterceptorException(getL10NService()
                                    .getLocalizedString("error.genericvariantproduct.variantcategorynotinbaseproduct", new Object[] {varCategory.getCode(), genericVariant.getCode(), baseProduct.getCode()}));
                }
            }
        }
    }


    protected void validateSupercategories(Collection<CategoryModel> superCategories) throws InterceptorException
    {
        boolean wrongCategoryExists = CollectionUtils.exists(superCategories, object -> {
            if(object instanceof VariantValueCategoryModel)
            {
                VariantValueCategoryModel variantValueCategoryModel = (VariantValueCategoryModel)object;
                return CollectionUtils.isEmpty(variantValueCategoryModel.getSupercategories()) ? true : (!(variantValueCategoryModel.getSupercategories().iterator().next() instanceof VariantCategoryModel));
            }
            return true;
        });
        if(wrongCategoryExists)
        {
            throw new InterceptorException(localizeForKey("error.genericvariantproduct.wrongsupercategory"));
        }
    }


    private String localizeForKey(String key)
    {
        return getL10NService().getLocalizedString(key);
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
