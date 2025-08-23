package de.hybris.platform.platformbackoffice.accessors;

import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class LocalizedModelPropertyAccessor implements PropertyAccessor, Ordered
{
    public static final Logger LOG = LoggerFactory.getLogger(LocalizedModelPropertyAccessor.class);
    public static final String USE_SESSION_LANGUAGE_FOR_LOCALIZED = "useSessionLanguageForLocalized";
    private static final Class[] targetClasses = new Class[] {ItemModel.class};
    private static final int DEFAULT_ORDER = 300;
    private TypeFacade typeFacade;
    private CockpitLocaleService cockpitLocaleService;
    private ModelService modelService;
    private CockpitUserService cockpitUserService;
    private int order = 300;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public Class[] getSpecificTargetClasses()
    {
        return targetClasses;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return canHandle((ItemModel)target, qualifier);
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return canHandle((ItemModel)target, qualifier);
    }


    private boolean canHandle(ItemModel model, String qualifier) throws AccessException
    {
        try
        {
            DataAttribute attributeType = getAttributeType(model, qualifier);
            return (attributeType != null && attributeType.isLocalized());
        }
        catch(TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("", (Throwable)e);
            }
            throw new AccessException("", e);
        }
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        Object useSessionLanguageForLocalized = evaluationContext.lookupVariable("useSessionLanguageForLocalized");
        if(useSessionLanguageForLocalized instanceof Boolean && ((Boolean)useSessionLanguageForLocalized).booleanValue())
        {
            Locale currentLocale = this.cockpitLocaleService.getCurrentLocale();
            return new TypedValue(getValueForLocale(target, qualifier, currentLocale));
        }
        Map<Locale, Object> result = new HashMap<>();
        for(Locale locale : getLocales(evaluationContext))
        {
            result.put(locale, getValueForLocale(target, qualifier, locale));
        }
        return new TypedValue(result);
    }


    protected Collection<Locale> getLocales(EvaluationContext context)
    {
        Object locales = context.lookupVariable("locales");
        if(locales instanceof Collection && !((Collection)locales).isEmpty())
        {
            return (Collection<Locale>)locales;
        }
        return this.cockpitLocaleService.getEnabledDataLocales(this.cockpitUserService.getCurrentUser());
    }


    private Object getValueForLocale(Object target, String qualifier, Locale locale)
    {
        try
        {
            return this.modelService.getAttributeValue(target, qualifier, locale);
        }
        catch(IllegalArgumentException | IllegalStateException iae)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Value not found for locale [%s]", new Object[] {locale.toString()}), iae);
            }
            return null;
        }
    }


    public void write(EvaluationContext evaluationContext, Object target, String qualifier, Object newValue) throws AccessException
    {
        validateWrite(newValue);
        this.modelService.setAttributeValue(target, qualifier, (Map)newValue);
    }


    protected void validateWrite(Object newValue)
    {
        Validate.isInstanceOf(Map.class, newValue);
        Map<Locale, Object> valuesToWrite = (Map<Locale, Object>)newValue;
        List<Locale> enabledDataLocales = this.cockpitLocaleService.getEnabledDataLocales(this.cockpitUserService.getCurrentUser());
        Iterator<Map.Entry<Locale, Object>> iterator = valuesToWrite.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry<Locale, Object> next = iterator.next();
            if(!enabledDataLocales.contains(next.getKey()))
            {
                iterator.remove();
            }
        }
    }


    private DataAttribute getAttributeType(ItemModel target, String qualifier) throws TypeNotFoundException
    {
        return this.typeFacade.load(target.getItemtype()).getAttribute(qualifier);
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setCockpitLocaleService(CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCockpitUserService(CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }
}
