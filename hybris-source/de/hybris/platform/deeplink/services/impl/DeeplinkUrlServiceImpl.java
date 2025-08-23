package de.hybris.platform.deeplink.services.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.deeplink.DeeplinkUtils;
import de.hybris.platform.deeplink.dao.DeeplinkUrlDao;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlRuleModel;
import de.hybris.platform.deeplink.pojo.DeeplinkUrlInfo;
import de.hybris.platform.deeplink.resolvers.BarcodeUrlResolver;
import de.hybris.platform.deeplink.services.DeeplinkUrlService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.StringWriter;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class DeeplinkUrlServiceImpl implements DeeplinkUrlService
{
    private static final Logger LOG = Logger.getLogger(DeeplinkUrlServiceImpl.class);
    private DeeplinkUrlDao deeplinkUrlDao;
    private BarcodeUrlResolver resolver;
    @Resource
    private TypeService typeService;


    public String generateShortUrl(DeeplinkUrlModel deeplinkUrlModel, Object contextObject)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Short URL generation for DeeplinkUrl object - code: " + deeplinkUrlModel.getCode() + ", baseUrl: " + deeplinkUrlModel
                            .getBaseUrl() + " and Context object: " + contextObject);
        }
        StringBuilder result = new StringBuilder(deeplinkUrlModel.getBaseUrl());
        result.append("?").append(DeeplinkUtils.getDeeplinkParameterName()).append("=");
        result.append(deeplinkUrlModel.getCode());
        if(contextObject instanceof ItemModel)
        {
            result.append("-").append(((ItemModel)contextObject).getPk().toString());
        }
        LOG.info("Generated short URL: " + result);
        return result.toString();
    }


    public DeeplinkUrlService.LongUrlInfo generateUrl(String barcodeToken)
    {
        DeeplinkUrlService.LongUrlInfo result = null;
        DeeplinkUrlRuleModel deeplinkUrlRule = null;
        DeeplinkUrlInfo deeplinkUrlInfo = getResolver().resolve(barcodeToken);
        DeeplinkUrlModel deeplinkUrl = deeplinkUrlInfo.getDeeplinkUrl();
        if(deeplinkUrl != null)
        {
            deeplinkUrlRule = getDeeplinkUrlRule(deeplinkUrl.getBaseUrl(), deeplinkUrlInfo.getContextObject());
        }
        if(deeplinkUrlRule != null)
        {
            VelocityContext context = new VelocityContext();
            context.put("ctx", deeplinkUrlInfo);
            String parsedUrl = parseTemplate(deeplinkUrlRule.getDestUrlTemplate(), context);
            result = new DeeplinkUrlService.LongUrlInfo(parsedUrl, deeplinkUrlRule.getUseForward().booleanValue());
        }
        return result;
    }


    public DeeplinkUrlDao getDeeplinkUrlDao()
    {
        return this.deeplinkUrlDao;
    }


    public BarcodeUrlResolver getResolver()
    {
        return this.resolver;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public String parseTemplate(String template, VelocityContext context)
    {
        String result = null;
        try
        {
            Velocity.init();
        }
        catch(Exception e)
        {
            LOG.error("There was error during Velocity engine initialization", e);
            throw new SystemException(e.getMessage(), e);
        }
        StringWriter url = new StringWriter();
        try
        {
            Velocity.evaluate((Context)context, url, "destTemplateURL", template);
            result = url.toString();
        }
        catch(ParseErrorException e)
        {
            LOG.error("There was error during parsing template string", (Throwable)e);
        }
        catch(MethodInvocationException e)
        {
            LOG.error("There was error during invoking method", (Throwable)e);
        }
        catch(ResourceNotFoundException e)
        {
            LOG.error("Resource not found", (Throwable)e);
        }
        return result;
    }


    public void setDeeplinkUrlDao(DeeplinkUrlDao deeplinkUrlDao)
    {
        this.deeplinkUrlDao = deeplinkUrlDao;
    }


    public void setResolver(BarcodeUrlResolver resolver)
    {
        this.resolver = resolver;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected DeeplinkUrlRuleModel getDeeplinkUrlRule(String baseUrl, Object contextObject)
    {
        for(DeeplinkUrlRuleModel deeplinkUrlRule : getDeeplinkUrlDao().findDeeplinkUrlRules())
        {
            TypeModel contextObjectType = getTypeForContextObject(contextObject);
            if(contextObjectType == null)
            {
                if(Pattern.matches(deeplinkUrlRule.getBaseUrlPattern(), baseUrl))
                {
                    return deeplinkUrlRule;
                }
                continue;
            }
            if(Pattern.matches(deeplinkUrlRule.getBaseUrlPattern(), baseUrl) &&
                            getTypeService().isAssignableFrom((TypeModel)deeplinkUrlRule.getApplicableType(), contextObjectType))
            {
                return deeplinkUrlRule;
            }
        }
        return null;
    }


    protected TypeModel getTypeForContextObject(Object contextObject)
    {
        ComposedTypeModel composedTypeModel;
        TypeModel type = null;
        if(contextObject instanceof ItemModel)
        {
            composedTypeModel = getTypeService().getComposedTypeForClass(contextObject.getClass());
        }
        return (TypeModel)composedTypeModel;
    }
}
