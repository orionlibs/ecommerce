package de.hybris.platform.configurablebundlebackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class BundleTemplateDeleteAction extends DeleteAction
{
    private static final Logger LOG = LoggerFactory.getLogger(BundleTemplateDeleteAction.class);
    @Resource
    private BundleTemplateService bundleTemplateService;
    @Resource
    private FlexibleSearchService flexibleSearchService;


    public ActionResult<Object> perform(ActionContext<Object> ctx)
    {
        Collection<Object> ctxObjects = getCtxObjects(ctx);
        List<BundleTemplateModel> rootBundles = getRootBundles(ctxObjects);
        ObjectFacadeOperationResult<Object> result = new ObjectFacadeOperationResult();
        if(CollectionUtils.isNotEmpty(rootBundles))
        {
            List<BundleTemplateStatusModel> statuses = (List<BundleTemplateStatusModel>)rootBundles.stream().map(BundleTemplateModel::getStatus).collect(Collectors.toList());
            statuses.forEach(s -> s.setStatus(BundleTemplateStatusEnum.ARCHIVED));
            ObjectFacadeOperationResult<BundleTemplateStatusModel> saveResult = getObjectFacade().save(statuses, (Context)new DefaultContext());
            saveResult.getFailedObjects().forEach(fo -> result.addFailedObject(fo, saveResult.getErrorForObject(fo)));
            result.addSuccessfulObject(saveResult.getSuccessfulObjects());
        }
        Collection<Object> childBundles = CollectionUtils.subtract(ctxObjects, rootBundles);
        if(CollectionUtils.isNotEmpty(childBundles))
        {
            ObjectFacadeOperationResult<Object> deleteResult = getObjectFacade().delete(childBundles);
            deleteResult.getFailedObjects().forEach(fo -> result.addFailedObject(fo, deleteResult.getErrorForObject(fo)));
            result.addSuccessfulObject(deleteResult.getSuccessfulObjects());
        }
        if(result.hasError())
        {
            showFailureNotification(ctx, (Map)result
                            .getFailedObjects().stream().collect(Collectors.toMap(e -> e, e -> result.getErrorForObject(e))));
        }
        if(result.countSuccessfulObjects() > 0)
        {
            showSuccessNotification(ctx, (List)ctxObjects.stream().collect(Collectors.toList()));
        }
        ActionResult<Object> actionResult = new ActionResult((result.countSuccessfulObjects() < 1) ? "error" : "success", ctxObjects);
        return actionResult;
    }


    public String getConfirmationMessage(ActionContext<Object> ctx)
    {
        return CollectionUtils.isNotEmpty(getRootBundles(getCtxObjects(ctx))) ?
                        Labels.getLabel("configurablebundlecockpits.bundle.archive.confirmationMessage") :
                        super.getConfirmationMessage(ctx);
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        Objects.requireNonNull(BundleTemplateModel.class);
        Objects.requireNonNull(BundleTemplateModel.class);
        return (super.canPerform(ctx) && getCtxObjects(ctx).stream().filter(BundleTemplateModel.class::isInstance).map(BundleTemplateModel.class::cast).anyMatch(t -> isBundleTemplateDeletable(t)));
    }


    protected List<BundleTemplateModel> getRootBundles(Collection<Object> ctxObjects)
    {
        Objects.requireNonNull(BundleTemplateModel.class);
        Objects.requireNonNull(BundleTemplateModel.class);
        return (List<BundleTemplateModel>)ctxObjects.stream().filter(BundleTemplateModel.class::isInstance).map(BundleTemplateModel.class::cast)
                        .filter(t -> Objects.isNull(t.getParentTemplate())).collect(Collectors.toList());
    }


    protected boolean isBundleTemplateDeletable(BundleTemplateModel bundleTemplateModel)
    {
        try
        {
            BundleTemplateModel reloadedTemplateModel = (BundleTemplateModel)getObjectFacade().reload(bundleTemplateModel);
            if(Objects.isNull(reloadedTemplateModel.getParentTemplate()))
            {
                return isRootBundleTemplateDeletable(reloadedTemplateModel);
            }
            return isChildBundleTemplateDeletable(reloadedTemplateModel);
        }
        catch(ObjectNotFoundException e)
        {
            LOG.warn("BundleTemplate could not be reloaded", (Throwable)e);
            return false;
        }
    }


    protected boolean isRootBundleTemplateDeletable(BundleTemplateModel bundleTemplateModel)
    {
        return !BundleTemplateStatusEnum.ARCHIVED.equals(bundleTemplateModel.getStatus().getStatus());
    }


    protected boolean isChildBundleTemplateDeletable(BundleTemplateModel bundleTemplateModel)
    {
        return !BundleTemplateStatusEnum.APPROVED.equals(bundleTemplateModel.getStatus().getStatus());
    }


    protected List<BundleTemplateModel> getAllCatalogVersionsOfBundleTemplate(BundleTemplateModel templateModel)
    {
        BundleTemplateModel exampleTemplate = new BundleTemplateModel();
        exampleTemplate.setId(templateModel.getId());
        exampleTemplate.setVersion(templateModel.getVersion());
        return getFlexibleSearchService().getModelsByExample(exampleTemplate);
    }


    protected void fillCtxObjects(ActionContext<Object> ctx, Collection<Object> ctxObjects)
    {
        if(ctx.getData() instanceof Collection)
        {
            ctxObjects.addAll((Collection)ctx.getData());
        }
        else
        {
            ctxObjects.add(ctx.getData());
        }
    }


    protected Collection<Object> getCtxObjects(ActionContext<Object> ctx)
    {
        Collection<Object> ctxObjects = new ArrayList();
        fillCtxObjects(ctx, ctxObjects);
        return ctxObjects;
    }


    public BundleTemplateService getBundleTemplateService()
    {
        return this.bundleTemplateService;
    }


    public void setBundleTemplateService(BundleTemplateService bundleTemplateService)
    {
        this.bundleTemplateService = bundleTemplateService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
