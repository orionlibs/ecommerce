package de.hybris.platform.couponservices;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.util.PromotionResultUtils;
import de.hybris.platform.ruleengine.RuleActionMetadataHandler;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleActionCouponMetadataHandler implements RuleActionMetadataHandler<AbstractRuleBasedPromotionActionModel>
{
    private ModelService modelService;
    private String metadataId;
    private CouponService couponService;
    private PromotionResultUtils promotionResultUtils;


    protected Set<String> getUsedCouponCodes(AbstractOrderModel order)
    {
        return (Set<String>)order.getAllPromotionResults().stream().flatMap(p -> p.getActions().stream())
                        .filter(a -> a instanceof AbstractRuleBasedPromotionActionModel).map(a -> (AbstractRuleBasedPromotionActionModel)a)
                        .filter(a -> CollectionUtils.isNotEmpty(a.getUsedCouponCodes())).flatMap(a -> a.getUsedCouponCodes().stream())
                        .collect(Collectors.toSet());
    }


    public void handle(AbstractRuleBasedPromotionActionModel actionModel, String metadataValue)
    {
        ServicesUtil.validateParameterNotNull(actionModel, "ActionModel can't be null");
        ServicesUtil.validateParameterNotNull(actionModel.getPromotionResult(), "PromotionResult of ActionModel can't be null");
        AbstractOrderModel order = getPromotionResultUtils().getOrder(actionModel.getPromotionResult());
        ServicesUtil.validateParameterNotNull(order, "Order of ActionModel can't be null");
        if(CollectionUtils.isNotEmpty(order.getAppliedCouponCodes()))
        {
            Set<String> couponIdsFromActionMetadata = (Set<String>)Arrays.<String>asList(metadataValue.split(",")).stream().map(s -> s.replaceAll("\"", "").trim()).collect(Collectors.toSet());
            Set<String> usedCouponCodes = getUsedCouponCodes(order);
            List<String> orderCouponCodesToUse = (List<String>)order.getAppliedCouponCodes().stream().filter(cc -> (!usedCouponCodes.contains(cc) && isCouponPresentInActionMetadata(couponIdsFromActionMetadata, cc))).collect(Collectors.toList());
            usedCouponCodes.addAll(orderCouponCodesToUse);
            actionModel.setUsedCouponCodes(orderCouponCodesToUse);
            List<String> actionMetadataHandlers = Objects.nonNull(actionModel.getMetadataHandlers()) ? new ArrayList<>(actionModel.getMetadataHandlers()) : new ArrayList<>();
            if(!actionMetadataHandlers.contains(getMetadataId()))
            {
                actionMetadataHandlers.add(getMetadataId());
                actionModel.setMetadataHandlers(actionMetadataHandlers);
            }
        }
    }


    protected boolean isCouponPresentInActionMetadata(Set<String> couponIdsFromActionMetadata, String couponCode)
    {
        String couponId = getCouponIdByCouponCode(couponCode);
        return (Objects.nonNull(couponId) && couponIdsFromActionMetadata.contains(couponId));
    }


    protected String getCouponIdByCouponCode(String couponCode)
    {
        return getCouponService().getCouponForCode(couponCode).map(AbstractCouponModel::getCouponId).orElse(null);
    }


    public void undoHandle(AbstractRuleBasedPromotionActionModel actionModel)
    {
        ServicesUtil.validateParameterNotNull(actionModel, "ActionModel can't be null");
        if(CollectionUtils.isNotEmpty(actionModel.getMetadataHandlers()))
        {
            List<String> actionMetadataHandlers = (List<String>)actionModel.getMetadataHandlers().stream().filter(mdid -> !mdid.equals(getMetadataId())).collect(Collectors.toList());
            actionModel.setMetadataHandlers(actionMetadataHandlers);
        }
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


    protected String getMetadataId()
    {
        return this.metadataId;
    }


    @Required
    public void setMetadataId(String metadataId)
    {
        this.metadataId = metadataId;
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    @Required
    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
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
}
