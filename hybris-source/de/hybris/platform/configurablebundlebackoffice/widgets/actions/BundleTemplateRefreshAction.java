package de.hybris.platform.configurablebundlebackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.misc.RefreshAction;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;

public class BundleTemplateRefreshAction extends RefreshAction
{
    @Resource
    public ObjectFacade objectFacade;


    public ActionResult<Object> perform(ActionContext<Object> ctx)
    {
        Collection<Object> ctxObjects = getCtxObjects(ctx);
        List<BundleTemplateModel> rootBundles = getRootBundles(ctxObjects);
        ObjectFacadeOperationResult<Object> result = new ObjectFacadeOperationResult();
        if(CollectionUtils.isNotEmpty(rootBundles))
        {
            List<BundleTemplateStatusModel> statuses = (List<BundleTemplateStatusModel>)rootBundles.stream().map(BundleTemplateModel::getStatus).collect(Collectors.toList());
            statuses.forEach(s -> s.setStatus(BundleTemplateStatusEnum.UNAPPROVED));
            ObjectFacadeOperationResult<BundleTemplateStatusModel> saveResult = this.objectFacade.save(statuses, (Context)new DefaultContext());
            saveResult.getFailedObjects().forEach(fo -> result.addFailedObject(fo, saveResult.getErrorForObject(fo)));
            result.addSuccessfulObject(saveResult.getSuccessfulObjects());
        }
        ActionResult<Object> actionResult = new ActionResult((result.countSuccessfulObjects() < 1) ? "error" : "success", ctxObjects);
        return actionResult;
    }


    public boolean needsConfirmation(ActionContext<Object> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<Object> ctx)
    {
        return CollectionUtils.isNotEmpty(getRootBundles(getCtxObjects(ctx))) ?
                        ctx.getLabel("refresh.confirm") :
                        super.getConfirmationMessage(ctx);
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        Objects.requireNonNull(BundleTemplateModel.class);
        Objects.requireNonNull(BundleTemplateModel.class);
        return (super.canPerform(ctx) && !getCtxObjects(ctx).isEmpty() && getCtxObjects(ctx).stream().filter(BundleTemplateModel.class::isInstance).map(BundleTemplateModel.class::cast)
                        .allMatch(t -> (Objects.isNull(t.getParentTemplate()) && isBundleTemplateRefreshable(t))));
    }


    protected List<BundleTemplateModel> getRootBundles(Collection<Object> ctxObjects)
    {
        Objects.requireNonNull(BundleTemplateModel.class);
        Objects.requireNonNull(BundleTemplateModel.class);
        return (List<BundleTemplateModel>)ctxObjects.stream().filter(BundleTemplateModel.class::isInstance).map(BundleTemplateModel.class::cast)
                        .filter(t -> Objects.isNull(t.getParentTemplate())).collect(Collectors.toList());
    }


    protected boolean isBundleTemplateRefreshable(BundleTemplateModel templateModel)
    {
        return BundleTemplateStatusEnum.ARCHIVED.equals(templateModel.getStatus().getStatus());
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


    protected ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
