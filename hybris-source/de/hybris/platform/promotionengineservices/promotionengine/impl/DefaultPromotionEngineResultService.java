package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.promotionengineservices.model.PromotionActionParameterModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPotentialPromotionMessageActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotionengineservices.promotionengine.coupons.CouponCodeRetrievalStrategy;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.impl.DefaultPromotionResultService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionEngineResultService implements PromotionResultService
{
    protected static final String EMPTY_VALUE = "?";
    protected static final Pattern LIST_PATTERN = Pattern.compile("^List\\((.*)\\)");
    protected static final Pattern MAP_PATTERN = Pattern.compile("^Map\\((.+),\\s*(.+)\\)");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPromotionEngineResultService.class);
    private CouponCodeRetrievalStrategy couponCodeRetrievalStrategy;
    private DefaultPromotionResultService defaultPromotionResultService;
    private CommonI18NService commonI18NService;
    private ModelService modelService;
    private Map<String, PromotionMessageParameterResolutionStrategy> resolutionStrategies;
    private RuleParametersService ruleParametersService;


    public String getDescription(PromotionResultModel promotionResult)
    {
        return getDescription(promotionResult, null);
    }


    public boolean apply(PromotionResultModel promotionResult)
    {
        return (!isRuleBasedPromotion(promotionResult.getPromotion()) && getDefaultPromotionResultService().apply(promotionResult));
    }


    public long getConsumedCount(PromotionResultModel promotionResult, boolean paramBoolean)
    {
        return !isRuleBasedPromotion(promotionResult.getPromotion()) ?
                        getDefaultPromotionResultService().getConsumedCount(promotionResult, paramBoolean) : 0L;
    }


    public boolean getCouldFire(PromotionResultModel promotionResult)
    {
        return isRuleBasedPromotion(promotionResult.getPromotion()) ? ((promotionResult.getCertainty().floatValue() < 1.0F)) :
                        getDefaultPromotionResultService().getCouldFire(promotionResult);
    }


    public String getDescription(PromotionResultModel promotionResult, Locale locale)
    {
        Locale localeToUse = Objects.isNull(locale) ? getCommonI18NService().getLocaleForLanguage(getCommonI18NService().getCurrentLanguage()) : locale;
        if(isRuleBasedPromotion(promotionResult.getPromotion()))
        {
            RuleBasedPromotionModel promotion = (RuleBasedPromotionModel)promotionResult.getPromotion();
            String messageFiredPositional = promotion.getMessageFired();
            try
            {
                if(StringUtils.isEmpty(messageFiredPositional))
                {
                    return messageFiredPositional;
                }
                AbstractRuleEngineRuleModel rule = promotion.getRule();
                if(Objects.isNull(rule))
                {
                    LOG.warn("promotion {} has no corresponding rule. Cannot substitute message parameters, returning message as is.", promotion
                                    .getCode());
                    return messageFiredPositional;
                }
                List<RuleParameterData> parameters = null;
                String paramString = rule.getRuleParameters();
                if(Objects.nonNull(paramString))
                {
                    parameters = getRuleParametersService().convertParametersFromString(paramString);
                }
                if(Objects.isNull(parameters))
                {
                    LOG.warn("rule with code {} has no rule parameters. Cannot substitute message parameters, returning message as is.", rule
                                    .getCode());
                    return messageFiredPositional;
                }
                if(Objects.nonNull(promotionResult.getActions()))
                {
                    Map<String, Object> messageActionValues = (Map<String, Object>)promotionResult.getActions().stream().filter(action -> action instanceof RuleBasedPotentialPromotionMessageActionModel)
                                    .flatMap(action -> ((RuleBasedPotentialPromotionMessageActionModel)action).getParameters().stream()).collect(Collectors.toMap(PromotionActionParameterModel::getUuid, PromotionActionParameterModel::getValue));
                    logMissingParametersResolutionStrategies(parameters, messageActionValues);
                    parameters = (List<RuleParameterData>)parameters.stream().map(parameter -> replaceRuleParameterValue(promotionResult, messageActionValues, parameter)).collect(Collectors.toList());
                }
                return getMessageWithResolvedParameters(promotionResult, localeToUse, messageFiredPositional, parameters);
            }
            catch(Exception e)
            {
                LOG.error("error during promotion message calculation, returning empty string", e);
                return messageFiredPositional;
            }
        }
        if(!getModelService().isNew(promotionResult))
        {
            return getDefaultPromotionResultService().getDescription(promotionResult, localeToUse);
        }
        return null;
    }


    protected void logMissingParametersResolutionStrategies(List<RuleParameterData> parameters, Map<String, Object> messageActionValues)
    {
        if(LOG.isWarnEnabled())
        {
            parameters.stream()
                            .filter(parameter -> (messageActionValues.containsKey(parameter.getUuid()) && !getResolutionStrategies().containsKey(parameter.getType())))
                            .forEach(parameter -> LOG.warn("Parameter {} has to be replaced but resolution strategy for type {} is not defined", parameter.getUuid(), parameter.getType()));
        }
    }


    protected boolean isRuleBasedPromotion(AbstractPromotionModel abstractPromotion)
    {
        return abstractPromotion instanceof RuleBasedPromotionModel;
    }


    protected RuleParameterData replaceRuleParameterValue(PromotionResultModel promotionResult, Map<String, Object> messageActionValues, RuleParameterData parameter)
    {
        return (messageActionValues.containsKey(parameter.getUuid()) && getResolutionStrategies().containsKey(parameter.getType())) ? (
                        (PromotionMessageParameterResolutionStrategy)getResolutionStrategies().get(parameter.getType())).getReplacedParameter(parameter, promotionResult, messageActionValues
                        .get(parameter.getUuid())) :
                        parameter;
    }


    protected String getMessageWithResolvedParameters(PromotionResultModel promotionResult, Locale locale, String messageFiredPositional, List<RuleParameterData> parameters)
    {
        Map<String, Object> valuesMap = new HashMap<>();
        for(RuleParameterData parameter : parameters)
        {
            if(messageFiredPositional.contains(parameter.getUuid()))
            {
                valuesMap.put(parameter.getUuid(), resolveParameterValue(parameter, promotionResult, locale));
            }
        }
        String substitorInputMessage = messageFiredPositional.replace("{", "${");
        String resolvedMessage = (new StrSubstitutor(valuesMap)).replace(substitorInputMessage);
        if(resolvedMessage.contains("${"))
        {
            logUnresolvedPlaceholder(promotionResult, resolvedMessage);
            return resolvedMessage.replace("${", "{");
        }
        return resolvedMessage;
    }


    protected void logUnresolvedPlaceholder(PromotionResultModel promotionResult, String resolvedMessage)
    {
        LOG.info("One of message placeholders cannot be filled for the \"{}\" promotion and message \"{}\"", promotionResult
                        .getPromotion().getCode(), resolvedMessage);
    }


    protected Object resolveParameterValue(RuleParameterData parameter, PromotionResultModel promotionResult, Locale locale)
    {
        if(Objects.isNull(parameter.getValue()))
        {
            Matcher listMatcher = LIST_PATTERN.matcher(parameter.getType());
            if(listMatcher.matches())
            {
                return Collections.emptyList();
            }
            Matcher mapMatcher = MAP_PATTERN.matcher(parameter.getType());
            if(mapMatcher.matches())
            {
                return Collections.emptyMap();
            }
            return "?";
        }
        if(MapUtils.isNotEmpty(getResolutionStrategies()))
        {
            PromotionMessageParameterResolutionStrategy strategy = getResolutionStrategies().get(parameter.getType());
            if(strategy != null)
            {
                return strategy.getValue(parameter, promotionResult, locale);
            }
        }
        return parameter.getValue();
    }


    public boolean getFired(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return (promotionResult.getCertainty().floatValue() >= 1.0F);
        }
        return getDefaultPromotionResultService().getFired(promotionResult);
    }


    public double getTotalDiscount(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return 0.0D;
        }
        return getDefaultPromotionResultService().getTotalDiscount(promotionResult);
    }


    public boolean isApplied(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return false;
        }
        return getDefaultPromotionResultService().isApplied(promotionResult);
    }


    public boolean isAppliedToOrder(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return false;
        }
        return getDefaultPromotionResultService().isAppliedToOrder(promotionResult);
    }


    public boolean undo(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return false;
        }
        return getDefaultPromotionResultService().undo(promotionResult);
    }


    public List<PromotionResultModel> getPotentialProductPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return Collections.emptyList();
        }
        return getDefaultPromotionResultService().getPotentialProductPromotions(promoResult, promotion);
    }


    public List<PromotionResultModel> getPotentialOrderPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return Collections.emptyList();
        }
        return getDefaultPromotionResultService().getPotentialOrderPromotions(promoResult, promotion);
    }


    public List<PromotionResultModel> getFiredProductPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return Collections.emptyList();
        }
        return getDefaultPromotionResultService().getFiredProductPromotions(promoResult, promotion);
    }


    public List<PromotionResultModel> getFiredOrderPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return Collections.emptyList();
        }
        return getDefaultPromotionResultService().getFiredOrderPromotions(promoResult, promotion);
    }


    public Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof RuleBasedPromotionModel)
        {
            return getCouponCodeRetrievalStrategy().getCouponCodesFromPromotion(promotionResult);
        }
        return getDefaultPromotionResultService().getCouponCodesFromPromotion(promotionResult);
    }


    protected DefaultPromotionResultService getDefaultPromotionResultService()
    {
        return this.defaultPromotionResultService;
    }


    @Required
    public void setDefaultPromotionResultService(DefaultPromotionResultService defaultPromotionResultService)
    {
        this.defaultPromotionResultService = defaultPromotionResultService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected Map<String, PromotionMessageParameterResolutionStrategy> getResolutionStrategies()
    {
        return this.resolutionStrategies;
    }


    @Required
    public void setResolutionStrategies(Map<String, PromotionMessageParameterResolutionStrategy> resolutionStrategies)
    {
        this.resolutionStrategies = resolutionStrategies;
    }


    protected RuleParametersService getRuleParametersService()
    {
        return this.ruleParametersService;
    }


    @Required
    public void setRuleParametersService(RuleParametersService ruleParametersService)
    {
        this.ruleParametersService = ruleParametersService;
    }


    protected CouponCodeRetrievalStrategy getCouponCodeRetrievalStrategy()
    {
        return this.couponCodeRetrievalStrategy;
    }


    @Required
    public void setCouponCodeRetrievalStrategy(CouponCodeRetrievalStrategy couponCodeRetrievalStrategy)
    {
        this.couponCodeRetrievalStrategy = couponCodeRetrievalStrategy;
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
}
