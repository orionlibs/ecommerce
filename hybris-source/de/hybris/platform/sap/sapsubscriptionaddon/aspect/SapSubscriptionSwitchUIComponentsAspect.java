/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsubscriptionaddon.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * This aspect is used to apply the UI changes sap subscription is deployed.
 *
 */
@Aspect
public class SapSubscriptionSwitchUIComponentsAspect
{
    public static final Logger LOG = Logger.getLogger(SapSubscriptionSwitchUIComponentsAspect.class);
    public static final String SUBSCRIPTION_ADDON_PREFIX = "addon:/sapsubscriptionaddon/";
    public static final String REDIRECT_PREFIX = "redirect:";
    public static final String FORWARD_PREFIX = "forward:";
    public static final String ADDON_PREFIX = "addon:";
    public static final String REDIRECT_MY_ACCOUNT = "redirect:/my-account";


    /**
     * For subscriptions add the add-on prefix to the UI component.
     *
     * @param pjp
     * @return the UI component name
     * @throws Throwable
     */
    public String applyUIChanges(final ProceedingJoinPoint pjp) throws Throwable
    {
        String uiComponent = pjp.proceed().toString();
        if(uiComponent.contains(REDIRECT_PREFIX)
                        || uiComponent.contains(FORWARD_PREFIX)
                        || uiComponent.contains(ADDON_PREFIX)
                        || uiComponent.contains(SUBSCRIPTION_ADDON_PREFIX))
        {
            return uiComponent;
        }
        final StringBuilder prefix = new StringBuilder(
                        SUBSCRIPTION_ADDON_PREFIX);
        prefix.append(uiComponent);
        uiComponent = prefix.toString();
        return uiComponent;
    }


    @Around("execution(public String de.hybris.platform.*.controllers.misc.*.rolloverMiniCartPopup(..))")
    public String rolloverMiniCartPopup(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.misc.*.addGridToCart(..))")
    public String addGridToCart(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.misc.*.addToCart(..))")
    public String addToCart(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.pages.*.showCart(..))")
    public String showCart(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.pages.*.productDetail(..))")
    public String productDetail(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.pages.checkout.steps.*.enterStep(..))")
    public String enterStep(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }


    @Around("execution(public String de.hybris.platform.*.controllers.pages.*.order(..))")
    public String order(final ProceedingJoinPoint joinPoint) throws Throwable
    {
        return applyUIChanges(joinPoint);
    }
}
