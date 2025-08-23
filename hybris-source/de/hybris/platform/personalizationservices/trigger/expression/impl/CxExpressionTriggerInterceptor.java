package de.hybris.platform.personalizationservices.trigger.expression.impl;

import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionTriggerService;
import de.hybris.platform.personalizationservices.trigger.impl.BaseTriggerInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class CxExpressionTriggerInterceptor extends BaseTriggerInterceptor implements ValidateInterceptor<CxExpressionTriggerModel>, PrepareInterceptor<CxExpressionTriggerModel>
{
    private CxExpressionTriggerService cxExpressionTriggerService;
    private CxSegmentDao cxSegmentDao;


    public void onPrepare(CxExpressionTriggerModel model, InterceptorContext context) throws InterceptorException
    {
        if(model == null || model.getVariation() == null || StringUtils.isEmpty(model.getCode()))
        {
            return;
        }
        if(StringUtils.isBlank(model.getExpression()))
        {
            throw new InterceptorException("Invalid expression");
        }
        Set<String> result = new HashSet<>();
        Set<String> invalidSegments = new HashSet<>();
        Set<CxSegmentModel> segments = new HashSet<>();
        CxExpression expression = this.cxExpressionTriggerService.extractExpression(model);
        extractSegmentCodes(expression, result);
        for(String code : result)
        {
            Optional<CxSegmentModel> optionalSegment = this.cxSegmentDao.findSegmentByCode(code);
            if(optionalSegment.isPresent())
            {
                segments.add(optionalSegment.get());
                continue;
            }
            invalidSegments.add(code);
        }
        if(!invalidSegments.isEmpty())
        {
            throw new InterceptorException("Segments " + invalidSegments + " in expression are not valid.");
        }
        model.setSegments(segments);
    }


    protected void extractSegmentCodes(CxExpression expression, Set<String> result) throws InterceptorException
    {
        if(expression instanceof CxSegmentExpression)
        {
            CxSegmentExpression segment = (CxSegmentExpression)expression;
            result.add(segment.getCode());
        }
        else if(expression instanceof CxGroupExpression)
        {
            CxGroupExpression group = (CxGroupExpression)expression;
            if(CollectionUtils.isEmpty(group.getElements()))
            {
                throw new InterceptorException("Invalid expression. Group can't be empty.");
            }
            for(CxExpression e : group.getElements())
            {
                extractSegmentCodes(e, result);
            }
        }
        else if(expression instanceof CxNegationExpression)
        {
            CxNegationExpression negation = (CxNegationExpression)expression;
            if(negation.getElement() == null)
            {
                throw new InterceptorException("Invalid expression. Negated element can't be empty.");
            }
            extractSegmentCodes(negation.getElement(), result);
        }
    }


    public void onValidate(CxExpressionTriggerModel model, InterceptorContext context) throws InterceptorException
    {
        if(model == null || model.getVariation() == null || StringUtils.isEmpty(model.getCode()))
        {
            return;
        }
        checkSegments(model);
        isTriggerUnique((CxAbstractTriggerModel)model, model.getVariation(), context);
    }


    protected void checkSegments(CxExpressionTriggerModel model) throws InterceptorException
    {
        if(CollectionUtils.isEmpty(model.getSegments()))
        {
            throw new InterceptorException("CxExpressionTrigger needs to be related to segment(s) based on expression");
        }
    }


    public void setCxExpressionTriggerService(CxExpressionTriggerService cxExpressionTriggerService)
    {
        this.cxExpressionTriggerService = cxExpressionTriggerService;
    }


    public void setCxSegmentDao(CxSegmentDao cxSegmentDao)
    {
        this.cxSegmentDao = cxSegmentDao;
    }


    protected CxExpressionTriggerService getCxExpressionTriggerService()
    {
        return this.cxExpressionTriggerService;
    }


    protected CxSegmentDao getCxSegmentDao()
    {
        return this.cxSegmentDao;
    }
}
