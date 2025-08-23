package de.hybris.platform.configurablebundlebackoffice.widgets.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.clone.CloneAction;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleTemplateCloneAction extends CloneAction
{
    private static final Logger LOG = LoggerFactory.getLogger(BundleTemplateCloneAction.class);
    private final Map<String, BundleTemplateModel> templateIdsMap = new HashMap<>();
    private BundleTemplateStatusModel bundleStatus;
    @Resource
    private KeyGenerator cloneIdGenerator;
    @Resource
    private UserService userService;


    public ActionResult<Object> perform(ActionContext<Object> ctx)
    {
        BundleTemplateModel bundleTemplateToClone = resolveBundleTemplateToClone(ctx);
        if(!isBundleTemplateClonable(bundleTemplateToClone))
        {
            LOG.warn("Selected bundle templates are not valid for cloning");
            return new ActionResult("error", bundleTemplateToClone);
        }
        try
        {
            BundleTemplateModel clonedBundleTemplate = cloneBundleTemplate(bundleTemplateToClone);
            sendOutput("clonedObject", clonedBundleTemplate);
            return new ActionResult("success", clonedBundleTemplate);
        }
        catch(ObjectCloningException | ObjectCreationException exception)
        {
            LOG.warn(exception.getMessage(), (Throwable)exception);
            return new ActionResult("error", bundleTemplateToClone);
        }
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        return (super.canPerform(ctx) && isBundleTemplateClonable(resolveBundleTemplateToClone(ctx)));
    }


    protected BundleTemplateModel resolveBundleTemplateToClone(ActionContext<Object> ctx)
    {
        Object objectToClone = resolveObjectToClone(ctx);
        return (objectToClone instanceof BundleTemplateModel) ? (BundleTemplateModel)objectToClone : null;
    }


    protected boolean isBundleTemplateClonable(BundleTemplateModel bundleTemplate)
    {
        return (bundleTemplate != null && bundleTemplate.getParentTemplate() == null &&
                        !BundleTemplateStatusEnum.ARCHIVED.equals(bundleTemplate.getStatus().getStatus()));
    }


    protected BundleTemplateModel cloneBundleTemplate(BundleTemplateModel bundleToBeCloned) throws ObjectCloningException, ObjectCreationException
    {
        BundleTemplateModel clonedBundleTemplate = cloneBundleTemplateRecursive(bundleToBeCloned);
        cloneRequiredBundleTemplatesRecursive(bundleToBeCloned, clonedBundleTemplate);
        cloneDependentBundleTemplatesRecursive(bundleToBeCloned, clonedBundleTemplate);
        return clonedBundleTemplate;
    }


    protected BundleTemplateModel cloneBundleTemplateRecursive(BundleTemplateModel bundleToBeCloned) throws ObjectCloningException, ObjectCreationException
    {
        BundleTemplateModel clonedBundleTemplateModel = (BundleTemplateModel)getObjectFacade().clone(bundleToBeCloned);
        String cloneGeneratedId = (String)getCloneIdGenerator().generate();
        if(null == bundleToBeCloned.getParentTemplate())
        {
            clonedBundleTemplateModel.setName("CLONE: " + clonedBundleTemplateModel.getName());
            this.bundleStatus = (BundleTemplateStatusModel)getObjectFacade().create("BundleTemplateStatus");
            this.bundleStatus.setCatalogVersion(clonedBundleTemplateModel.getCatalogVersion());
            this.bundleStatus.setStatus(BundleTemplateStatusEnum.CHECK);
        }
        cloneChildBundleTemplatesRecursive(bundleToBeCloned, clonedBundleTemplateModel);
        clonedBundleTemplateModel.setId("BT_" + cloneGeneratedId);
        this.templateIdsMap.put(bundleToBeCloned.getId(), clonedBundleTemplateModel);
        clonedBundleTemplateModel.setStatus(this.bundleStatus);
        if(null != clonedBundleTemplateModel.getBundleSelectionCriteria())
        {
            clonedBundleTemplateModel.getBundleSelectionCriteria().setId("BSC_" + cloneGeneratedId);
        }
        if(!CollectionUtils.isEmpty(clonedBundleTemplateModel.getDisableProductBundleRules()))
        {
            for(DisableProductBundleRuleModel disableProductBundleRuleModel : clonedBundleTemplateModel
                            .getDisableProductBundleRules())
            {
                String dpbrGeneratedId = (String)getCloneIdGenerator().generate();
                disableProductBundleRuleModel.setId("DPBR_" + dpbrGeneratedId);
            }
        }
        if(!CollectionUtils.isEmpty(clonedBundleTemplateModel.getChangeProductPriceBundleRules()))
        {
            for(ChangeProductPriceBundleRuleModel changeProductPriceBundleRuleModel : clonedBundleTemplateModel
                            .getChangeProductPriceBundleRules())
            {
                String cppbrGeneratedId = (String)getCloneIdGenerator().generate();
                changeProductPriceBundleRuleModel.setId("CPPBR_" + cppbrGeneratedId);
            }
        }
        clonedBundleTemplateModel.setCreationtime(new Date());
        clonedBundleTemplateModel.setModifiedtime(null);
        clonedBundleTemplateModel.setOwner((ItemModel)getUserService().getCurrentUser());
        return clonedBundleTemplateModel;
    }


    protected void cloneChildBundleTemplatesRecursive(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel) throws ObjectCloningException, ObjectCreationException
    {
        List<BundleTemplateModel> childTemplates = new ArrayList<>();
        for(BundleTemplateModel childTemplate : bundleToBeCloned.getChildTemplates())
        {
            BundleTemplateModel newChildTemplateModel = cloneBundleTemplateRecursive(childTemplate);
            newChildTemplateModel.setParentTemplate(clonedBundleTemplateModel);
            childTemplates.add(newChildTemplateModel);
        }
        clonedBundleTemplateModel.setChildTemplates(childTemplates);
    }


    protected void cloneRequiredBundleTemplatesRecursive(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel)
    {
        List<BundleTemplateModel> requiredBundles = new ArrayList<>();
        for(BundleTemplateModel requiredBundle : bundleToBeCloned.getRequiredBundleTemplates())
        {
            if(null != this.templateIdsMap.get(requiredBundle.getId()))
            {
                requiredBundles.add(this.templateIdsMap.get(requiredBundle.getId()));
            }
        }
        clonedBundleTemplateModel.setRequiredBundleTemplates(requiredBundles);
        for(BundleTemplateModel childTemplate : bundleToBeCloned.getChildTemplates())
        {
            cloneRequiredBundleTemplatesRecursive(childTemplate, this.templateIdsMap.get(childTemplate.getId()));
        }
    }


    protected void cloneDependentBundleTemplatesRecursive(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel)
    {
        List<BundleTemplateModel> dependentBundles = new ArrayList<>();
        for(BundleTemplateModel dependentBundle : bundleToBeCloned.getDependentBundleTemplates())
        {
            if(null != this.templateIdsMap.get(dependentBundle.getId()))
            {
                dependentBundles.add(this.templateIdsMap.get(dependentBundle.getId()));
            }
        }
        clonedBundleTemplateModel.setDependentBundleTemplates(dependentBundles);
        for(BundleTemplateModel childTemplate : bundleToBeCloned.getChildTemplates())
        {
            cloneDependentBundleTemplatesRecursive(childTemplate, this.templateIdsMap.get(childTemplate.getId()));
        }
    }


    protected KeyGenerator getCloneIdGenerator()
    {
        return this.cloneIdGenerator;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }
}
