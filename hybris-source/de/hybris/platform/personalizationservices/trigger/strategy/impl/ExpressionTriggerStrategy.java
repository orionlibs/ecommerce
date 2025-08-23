package de.hybris.platform.personalizationservices.trigger.strategy.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionContext;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionEvaluator;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionTriggerService;
import de.hybris.platform.personalizationservices.trigger.expression.impl.DefaultExpressionEvaluator;
import de.hybris.platform.personalizationservices.trigger.strategy.CxTriggerStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionTriggerStrategy implements CxTriggerStrategy
{
    private FlexibleSearchService flexibleSearchService;
    private CxConfigurationService cxConfigurationService;
    private CxExpressionTriggerService cxExpressionTriggerService;
    private CxSegmentService cxSegmentService;
    private final CxExpressionEvaluator evaluator = (CxExpressionEvaluator)new DefaultExpressionEvaluator();


    public Collection<CxVariationModel> getVariations(UserModel user, CatalogVersionModel catalogVersion)
    {
        CxExpressionTriggerModel example = new CxExpressionTriggerModel();
        example.setCatalogVersion(catalogVersion);
        List<CxExpressionTriggerModel> triggers = this.flexibleSearchService.getModelsByExample(example);
        if(triggers.isEmpty())
        {
            return Collections.emptyList();
        }
        CxExpressionContext context = buildContext(user);
        return (Collection<CxVariationModel>)triggers.stream().filter(t -> {
            CxExpression element = this.cxExpressionTriggerService.extractExpression(t);
            return this.evaluator.evaluate(element, context);
        }).map(t -> t.getVariation()).collect(Collectors.toList());
    }


    private CxExpressionContext buildContext(UserModel user)
    {
        CxExpressionContext context = new CxExpressionContext();
        BigDecimal min = getMinAffinity();
        Collection<CxUserToSegmentModel> userToSegments = this.cxSegmentService.getUserToSegmentForCalculation(user);
        Set<String> userSegments = (Set<String>)userToSegments.stream().filter(u2s -> filterMinAffinity(u2s, min)).map(u2s -> u2s.getSegment().getCode()).collect(Collectors.toSet());
        context.setSegments(userSegments);
        return context;
    }


    protected boolean filterMinAffinity(CxUserToSegmentModel u2s, BigDecimal min)
    {
        return (u2s.getAffinity().compareTo(min) >= 0);
    }


    protected BigDecimal getMinAffinity()
    {
        return this.cxConfigurationService.getMinAffinity();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setCxExpressionTriggerService(CxExpressionTriggerService cxExpressionTriggerService)
    {
        this.cxExpressionTriggerService = cxExpressionTriggerService;
    }


    protected CxExpressionTriggerService getCxExpressionTriggerService()
    {
        return this.cxExpressionTriggerService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }
}
