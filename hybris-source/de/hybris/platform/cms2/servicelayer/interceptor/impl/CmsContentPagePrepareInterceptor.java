package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.Collection;
import java.util.function.Predicate;
import org.apache.commons.lang.StringUtils;

public class CmsContentPagePrepareInterceptor implements PrepareInterceptor
{
    private CMSAdminPageService cmsAdminPageService;


    protected void changeLabelForPersonalizedPages(Collection<AbstractPageModel> contentPages, ContentPageModel currentContentPage, String oldLabelValue, InterceptorContext ctx)
    {
        if(oldLabelValue != null)
        {
            Predicate<ContentPageModel> hasSameLabel = contentPage -> oldLabelValue.equals(contentPage.getLabel());
            Predicate<ContentPageModel> isVariationPage = contentPage -> !contentPage.getDefaultPage().booleanValue();
            contentPages.stream().map(abstractPage -> (ContentPageModel)abstractPage)
                            .filter(isNotCurrentPage(currentContentPage))
                            .filter(hasSameLabel)
                            .filter(isVariationPage)
                            .forEach(contentPage -> {
                                contentPage.setLabel(currentContentPage.getLabel());
                                ctx.getModelService().save(contentPage);
                            });
        }
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        ContentPageModel contentPageModel = (ContentPageModel)model;
        if(getCmsAdminPageService().getActiveCatalogVersion() != null)
        {
            Collection<AbstractPageModel> contentPages = getCmsAdminPageService().getAllPagesByType(contentPageModel.getItemtype(), contentPageModel.getCatalogVersion());
            if(ctx.isModified(contentPageModel, "homepage") && contentPageModel
                            .isHomepage() && contentPageModel
                            .getDefaultPage().booleanValue())
            {
                resetHomepageFlag(contentPages, contentPageModel, ctx);
            }
            if(ctx.getModelService().isNew(contentPageModel) && StringUtils.isBlank(contentPageModel.getLabel()))
            {
                contentPageModel.setLabel(contentPageModel.getUid());
            }
            else if(contentPageModel.getDefaultPage().booleanValue() && contentPageModel
                            .getPk() != null && ctx
                            .isModified(contentPageModel, "label"))
            {
                String oldLabel = (String)getContext((AbstractItemModel)contentPageModel).getValueHistory().getOriginalValue("label");
                changeLabelForPersonalizedPages(contentPages, contentPageModel, oldLabel, ctx);
            }
        }
    }


    protected void resetHomepageFlag(Collection<AbstractPageModel> contentPages, ContentPageModel currentPageModel, InterceptorContext ctx)
    {
        contentPages.stream().map(abstractPage -> (ContentPageModel)abstractPage)
                        .filter(isNotCurrentPage(currentPageModel))
                        .filter(ContentPageModel::isHomepage)
                        .forEach(contentPageModel -> {
                            contentPageModel.setHomepage(false);
                            ctx.getModelService().save(contentPageModel);
                        });
    }


    protected Predicate<ContentPageModel> isNotCurrentPage(ContentPageModel currentContentPage)
    {
        return abstractPage -> !abstractPage.equals(currentContentPage);
    }


    public CMSAdminPageService getCmsAdminPageService()
    {
        return this.cmsAdminPageService;
    }


    public void setCmsAdminPageService(CMSAdminPageService cmsAdmiPageService)
    {
        this.cmsAdminPageService = cmsAdmiPageService;
    }


    protected ItemModelContextImpl getContext(AbstractItemModel model)
    {
        return (ItemModelContextImpl)ModelContextUtils.getItemModelContext(model);
    }
}
