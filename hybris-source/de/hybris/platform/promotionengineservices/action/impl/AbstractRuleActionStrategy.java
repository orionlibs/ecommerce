package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionActionService;
import de.hybris.platform.promotionengineservices.util.ActionUtils;
import de.hybris.platform.promotionengineservices.util.PromotionResultUtils;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.RuleActionMetadataHandler;
import de.hybris.platform.ruleengineservices.action.RuleActionStrategy;
import de.hybris.platform.ruleengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRuleActionStrategy<A extends AbstractRuleBasedPromotionActionModel> implements RuleActionStrategy, BeanNameAware
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRuleActionStrategy.class);
    private ExtendedOrderDao extendedOrderDao;
    private ModelService modelService;
    private PromotionActionService promotionActionService;
    private CalculationService calculationService;
    private Class<A> promotionAction;
    private Boolean forceImmediateRecalculation = Boolean.FALSE;
    private String beanName;
    private Map<String, List<RuleActionMetadataHandler>> ruleActionMetadataHandlers;
    private Collection<String> defaultRuleMetadataKeys;
    private PromotionResultUtils promotionResultUtils;
    private ActionUtils actionUtils;


    protected A createPromotionAction(PromotionResultModel promotionResult, AbstractRuleActionRAO action)
    {
        AbstractRuleBasedPromotionActionModel abstractRuleBasedPromotionActionModel = (AbstractRuleBasedPromotionActionModel)getModelService().create(this.promotionAction);
        abstractRuleBasedPromotionActionModel.setPromotionResult(promotionResult);
        abstractRuleBasedPromotionActionModel.setGuid(getActionUtils().createActionUUID());
        abstractRuleBasedPromotionActionModel.setRule(getPromotionActionService().getRule(action));
        abstractRuleBasedPromotionActionModel.setMarkedApplied(Boolean.TRUE);
        abstractRuleBasedPromotionActionModel.setStrategyId(getStrategyId());
        return (A)abstractRuleBasedPromotionActionModel;
    }


    protected void handleActionMetadata(AbstractRuleActionRAO action, AbstractRuleBasedPromotionActionModel actionModel)
    {
        if(action.getMetadata() != null)
        {
            for(Map.Entry<String, String> mdEntry : (Iterable<Map.Entry<String, String>>)action.getMetadata().entrySet())
            {
                for(RuleActionMetadataHandler mdHandler : getMetadataHandlers(mdEntry.getKey()))
                {
                    mdHandler.handle(actionModel, mdEntry.getValue());
                }
            }
        }
    }


    protected void handleUndoActionMetadata(AbstractRuleBasedPromotionActionModel action)
    {
        if(action.getMetadataHandlers() != null)
        {
            for(String mdHandlerId : action.getMetadataHandlers())
            {
                for(RuleActionMetadataHandler mdHandler : getMetadataHandlers(mdHandlerId))
                {
                    mdHandler.undoHandle(action);
                }
            }
        }
    }


    protected List<RuleActionMetadataHandler> getMetadataHandlers(String mdKey)
    {
        if(getRuleActionMetadataHandlers().containsKey(mdKey))
        {
            return getRuleActionMetadataHandlers().get(mdKey);
        }
        if(!getDefaultRuleMetadataKeys().contains(mdKey))
        {
            LOG.error("RuleActionMetadataHandler for {} not found", mdKey);
        }
        return Collections.emptyList();
    }


    protected AbstractOrderModel undoInternal(A action)
    {
        PromotionResultModel promoResult = action.getPromotionResult();
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promoResult);
        List<ItemModel> modifiedItems = getPromotionActionService().removeDiscountValue(action.getGuid(), order);
        getModelService().remove(action);
        if(((Set)promoResult.getAllPromotionActions().stream().filter(promoAction -> !getModelService().isRemoved(promoAction))
                        .collect(Collectors.toSet())).isEmpty())
        {
            getModelService().remove(promoResult);
        }
        getModelService().saveAll(modifiedItems);
        return order;
    }


    protected boolean recalculateIfNeeded(AbstractOrderModel order)
    {
        if(BooleanUtils.isTrue(getForceImmediateRecalculation()))
        {
            try
            {
                getCalculationService().calculateTotals(order, true);
            }
            catch(CalculationException e)
            {
                LOG.error(String.format("Recalculation of order with code '%s' failed.", new Object[] {order.getCode()}), (Throwable)e);
                order.setCalculated(Boolean.FALSE);
                getModelService().save(order);
                return false;
            }
        }
        return true;
    }


    protected ExtendedOrderDao getExtendedOrderDao()
    {
        return this.extendedOrderDao;
    }


    @Required
    public void setExtendedOrderDao(ExtendedOrderDao extendedOrderDao)
    {
        this.extendedOrderDao = extendedOrderDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected Class<A> getPromotionAction()
    {
        return this.promotionAction;
    }


    @Required
    public void setPromotionAction(Class<A> promotionAction)
    {
        this.promotionAction = promotionAction;
        if(promotionAction != null)
        {
            try
            {
                promotionAction.newInstance();
            }
            catch(InstantiationException | IllegalAccessException e)
            {
                throw new SystemException("could not instantiate class " + promotionAction.getSimpleName(), e);
            }
        }
    }


    protected PromotionActionService getPromotionActionService()
    {
        return this.promotionActionService;
    }


    @Required
    public void setPromotionActionService(PromotionActionService promotionActionService)
    {
        this.promotionActionService = promotionActionService;
    }


    protected Boolean getForceImmediateRecalculation()
    {
        return this.forceImmediateRecalculation;
    }


    public void setForceImmediateRecalculation(Boolean forceImmediateRecalculation)
    {
        this.forceImmediateRecalculation = forceImmediateRecalculation;
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }


    public String getStrategyId()
    {
        return this.beanName;
    }


    protected Map<String, List<RuleActionMetadataHandler>> getRuleActionMetadataHandlers()
    {
        return this.ruleActionMetadataHandlers;
    }


    @Required
    public void setRuleActionMetadataHandlers(Map<String, List<RuleActionMetadataHandler>> ruleActionMetadataHandlers)
    {
        this.ruleActionMetadataHandlers = ruleActionMetadataHandlers;
    }


    protected PromotionResultUtils getPromotionResultUtils()
    {
        return this.promotionResultUtils;
    }


    @Required
    public void setPromotionResultUtils(PromotionResultUtils promotionResultUtils)
    {
        this.promotionResultUtils = promotionResultUtils;
    }


    protected ActionUtils getActionUtils()
    {
        return this.actionUtils;
    }


    @Required
    public void setActionUtils(ActionUtils actionUtils)
    {
        this.actionUtils = actionUtils;
    }


    protected Collection<String> getDefaultRuleMetadataKeys()
    {
        return this.defaultRuleMetadataKeys;
    }


    @Required
    public void setDefaultRuleMetadataKeys(Collection<String> defaultRuleMetadataKeys)
    {
        this.defaultRuleMetadataKeys = defaultRuleMetadataKeys;
    }
}
