package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class InterceptorExecutionPolicy
{
    private static final Logger LOG = Logger.getLogger(InterceptorExecutionPolicy.class);
    private static final Predicate<Interceptor> PASS_ALL_PREDICATE = i -> true;
    private static final Predicate<Interceptor> REJECT_ALL_PREDICATE = PASS_ALL_PREDICATE.negate();
    private static final String UNIQUE_ATTRIBUTE_VALIDATOR_BEAN_ID = "UniqueAttributesValidator";
    public static final String DISABLED_INTERCEPTOR_BEANS = "disable.interceptor.beans";
    public static final String DISABLED_INTERCEPTOR_TYPES = "disable.interceptor.types";
    public static final String DISABLED_UNIQUE_ATTRIBUTE_VALIDATOR_FOR_ITEM_TYPES = "disable.UniqueAttributesValidator.for.types";
    private SessionService sessionService;


    public <T extends Interceptor> Collection<T> getEnabledInterceptors(InterceptorExecutionContext<T> ctx)
    {
        Predicate<? super Interceptor> predicate = constructFilterPredicate(ctx);
        if(predicate == PASS_ALL_PREDICATE)
        {
            return ctx.getAvailableInterceptors();
        }
        if(predicate == REJECT_ALL_PREDICATE)
        {
            return Collections.emptyList();
        }
        return (Collection<T>)ctx.getAvailableInterceptors().stream().filter(predicate).collect(Collectors.toList());
    }


    private Predicate<Interceptor> constructFilterPredicate(InterceptorExecutionContext ctx)
    {
        if(getDisabledInterceptorTypesInSession().contains(ctx.getInterceptorType()))
        {
            return REJECT_ALL_PREDICATE;
        }
        Predicate<Interceptor> disabledInterceptorsPredicate = createDisabledInterceptorsPredicate(ctx);
        Predicate<Interceptor> disableUniqueAttributeValidatorForTypesPredicate = createDisableUniqueAttributeValidatorForTypesPredicate(ctx);
        if(disabledInterceptorsPredicate == PASS_ALL_PREDICATE && disableUniqueAttributeValidatorForTypesPredicate == PASS_ALL_PREDICATE)
        {
            return PASS_ALL_PREDICATE;
        }
        return disabledInterceptorsPredicate.and(disableUniqueAttributeValidatorForTypesPredicate);
    }


    private Predicate<Interceptor> createDisabledInterceptorsPredicate(InterceptorExecutionContext ctx)
    {
        Set<String> disabledInterceptorBeanIds = getDisabledInterceptorsInSession();
        if(disabledInterceptorBeanIds.isEmpty())
        {
            return PASS_ALL_PREDICATE;
        }
        Set<Class<? super Interceptor>> disabledClasses = ctx.getInterceptorClassesByBeanIds(disabledInterceptorBeanIds);
        return i -> !disabledClasses.contains(i.getClass());
    }


    private Predicate<Interceptor> createDisableUniqueAttributeValidatorForTypesPredicate(InterceptorExecutionContext ctx)
    {
        if(!ctx.isValidateInterceptorType())
        {
            return PASS_ALL_PREDICATE;
        }
        Set<String> itemTypes = getItemTypesWithDisabledUniqueAttributeValidator();
        if(itemTypes.isEmpty())
        {
            return PASS_ALL_PREDICATE;
        }
        Optional<Class<? super Interceptor>> optionalClass = ctx.getInterceptorClassesByBeanId("UniqueAttributesValidator");
        if(!optionalClass.isPresent())
        {
            return PASS_ALL_PREDICATE;
        }
        Class<? super Interceptor> clazz = optionalClass.get();
        return i -> (clazz != i.getClass()) ? true : (!ctx.containsAnyTypeInTypeHierarchy(itemTypes));
    }


    private Set<String> getItemTypesWithDisabledUniqueAttributeValidator()
    {
        try
        {
            Set<String> itemTypes = (Set<String>)this.sessionService.getAttribute("disable.UniqueAttributesValidator.for.types");
            if(itemTypes != null)
            {
                return (Set<String>)itemTypes.stream().map(t -> t.toLowerCase(LocaleHelper.getPersistenceLocale())).collect(Collectors.toSet());
            }
        }
        catch(ClassCastException ex)
        {
            LOG.error("An error has occured while trying extract types with disabled UniqueAttributesValidator!");
        }
        return Collections.emptySet();
    }


    private Set<String> getDisabledInterceptorsInSession()
    {
        try
        {
            Set<String> disabledBeans = (Set<String>)this.sessionService.getAttribute("disable.interceptor.beans");
            if(disabledBeans != null)
            {
                return disabledBeans;
            }
        }
        catch(ClassCastException ex)
        {
            LOG.error("An error has occured while trying extract disabled validation in session!");
        }
        return Collections.emptySet();
    }


    private Set<InterceptorType> getDisabledInterceptorTypesInSession()
    {
        try
        {
            Set<InterceptorType> disabledTypes = (Set<InterceptorType>)this.sessionService.getAttribute("disable.interceptor.types");
            if(disabledTypes != null)
            {
                return disabledTypes;
            }
        }
        catch(ClassCastException ex)
        {
            LOG.error("An error has occured while trying extract disabled interceptor types in session!");
        }
        return Collections.emptySet();
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
