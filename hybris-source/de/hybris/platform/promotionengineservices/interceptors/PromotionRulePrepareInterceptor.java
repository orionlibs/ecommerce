package de.hybris.platform.promotionengineservices.interceptors;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContextFactory;
import de.hybris.platform.ruleengineservices.compiler.impl.DefaultRuleCompilerContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Required;

public class PromotionRulePrepareInterceptor implements PrepareInterceptor<AbstractRuleEngineRuleModel>
{
    private ModelService modelService;
    private CommonI18NService commonI18NService;
    private RuleCompilerContextFactory<DefaultRuleCompilerContext> ruleCompilerContextFactory;
    private RuleDao ruleDao;


    public void onPrepare(AbstractRuleEngineRuleModel model, InterceptorContext context) throws InterceptorException
    {
        if(!model.getRuleType().equals(RuleType.PROMOTION))
        {
            return;
        }
        doOnPrepare(model, context);
    }


    protected void doOnPrepare(AbstractRuleEngineRuleModel model, InterceptorContext context)
    {
        RuleBasedPromotionModel ruleBasedPromotion = getRuleBasedPromotion(model);
        AbstractRuleModel rule = model.getSourceRule();
        if(Objects.nonNull(rule))
        {
            setLocalizedValue(locale -> ruleBasedPromotion.setName(rule.getName(locale), locale));
            setLocalizedValue(locale -> ruleBasedPromotion.setPromotionDescription(rule.getDescription(locale), locale));
            ruleBasedPromotion.setPriority(rule.getPriority());
            ruleBasedPromotion.setStartDate(rule.getStartDate());
            ruleBasedPromotion.setEndDate(rule.getEndDate());
            if(rule instanceof PromotionSourceRuleModel)
            {
                ruleBasedPromotion.setPromotionGroup(((PromotionSourceRuleModel)rule).getWebsite());
            }
            context.registerElementFor(ruleBasedPromotion, PersistenceOperation.SAVE);
        }
        if(context.isModified(model, "messageFired") &&
                        setLocalizedMessageFired(model, ruleBasedPromotion))
        {
            context.registerElementFor(ruleBasedPromotion, PersistenceOperation.SAVE);
        }
    }


    protected RuleBasedPromotionModel getRuleBasedPromotion(AbstractRuleEngineRuleModel model)
    {
        RuleBasedPromotionModel ruleBasedPromotion = model.getPromotion();
        if(Objects.isNull(ruleBasedPromotion) || !model.getVersion().equals(ruleBasedPromotion.getRuleVersion()))
        {
            ruleBasedPromotion = createNewPromotionAndAddToRuleModel(model);
        }
        return ruleBasedPromotion;
    }


    protected RuleBasedPromotionModel createNewPromotionAndAddToRuleModel(AbstractRuleEngineRuleModel ruleModel)
    {
        RuleBasedPromotionModel ruleBasedPromotion = (RuleBasedPromotionModel)getModelService().create(RuleBasedPromotionModel.class);
        ruleBasedPromotion.setRuleVersion(ruleModel.getVersion());
        ruleBasedPromotion.setCode(ruleModel.getCode());
        ruleBasedPromotion.setTitle(ruleModel.getCode());
        setLocalizedMessageFired(ruleModel, ruleBasedPromotion);
        ruleBasedPromotion.setEnabled(Boolean.TRUE);
        ruleBasedPromotion.setRule(ruleModel);
        ruleModel.setPromotion(ruleBasedPromotion);
        return ruleBasedPromotion;
    }


    protected boolean setLocalizedMessageFired(AbstractRuleEngineRuleModel engineRule, RuleBasedPromotionModel promotion)
    {
        boolean changed = false;
        for(LanguageModel language : getCommonI18NService().getAllLanguages())
        {
            Locale locale = getCommonI18NService().getLocaleForLanguage(language);
            promotion.setMessageFired(engineRule.getMessageFired(locale), locale);
            changed = true;
        }
        return changed;
    }


    protected void setLocalizedValue(Consumer<Locale> consumer)
    {
        getCommonI18NService().getAllLanguages().forEach(language -> consumer.accept(getCommonI18NService().getLocaleForLanguage(language)));
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


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected RuleCompilerContextFactory<DefaultRuleCompilerContext> getRuleCompilerContextFactory()
    {
        return this.ruleCompilerContextFactory;
    }


    @Required
    public void setRuleCompilerContextFactory(RuleCompilerContextFactory<DefaultRuleCompilerContext> ruleCompilerContextFactory)
    {
        this.ruleCompilerContextFactory = ruleCompilerContextFactory;
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
    }
}
