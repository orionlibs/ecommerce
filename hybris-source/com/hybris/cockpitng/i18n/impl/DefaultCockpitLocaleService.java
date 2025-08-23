/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.impl;

import com.google.common.collect.Iterables;
import com.hybris.cockpitng.config.locales.factory.CockpitLocalesFactory;
import com.hybris.cockpitng.config.locales.jaxb.CockpitLocale;
import com.hybris.cockpitng.config.locales.jaxb.CockpitLocales;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.i18n.AbstractExecutionBody;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.exception.AvailableLocaleException;
import com.hybris.cockpitng.i18n.exception.WriteAvailableLocaleException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

/**
 * Default implementation of {@link CockpitLocaleService}. Session locale are based on ZK Locale handling. Data Locale
 * are stored per principal in cockpit-configuration : 'available-locales'.
 */
public class DefaultCockpitLocaleService implements CockpitLocaleService, Resettable
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitLocaleService.class);
    private static final String LOCALE_CONTEXT = "available-locales";
    private static final String ACTIVE_LOCALES = "activeLocales";
    private static final String AVAILABLE_LOCALES = "availableLocales";
    private static final String DEFAULT_DATA_LOCALE = "defaultDataLocale";
    public static final String PROPERTY_COCKPITNG_LOCALIZATION_LOCALES = "cockpitng.localization.locales";
    public static final String PROPERTY_COCKPITNG_LEGACY_WIDGET_CONFIGURATION_PERSISTENCE_ENABLED = "cockpitng.legacy.widget.configuration.persistence.enabled";
    private CockpitProperties cockpitProperties;
    private CockpitConfigurationService cockpitConfigurationService;
    private CockpitLocalesFactory cockpitLocalesFactory;
    private AuthorityGroupService authorityGroupService;
    private List<WidgetConfigurationContextDecorator> widgetConfigurationContextDecoratorList;


    @Override
    public Locale getCurrentLocale()
    {
        return Locales.getCurrent();
    }


    @Override
    public void setCurrentLocale(final Locale locale)
    {
        lookupZkSession().ifPresent(session -> session.setAttribute(Attributes.PREFERRED_LOCALE, locale));
        Locales.setThreadLocal(locale);
    }


    @Override
    public <T> T executeWithLocale(final AbstractExecutionBody<T> executionBody, final Locale locale)
    {
        final Locale current = getCurrentLocale();
        try
        {
            setCurrentLocale(locale);
            return executionBody.execute();
        }
        finally
        {
            setCurrentLocale(current);
        }
    }


    /**
     * @deprecated since 6.7 please us {@link #lookupZkSession()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected Session getZKSession()
    {
        final Optional<Session> session = lookupZkSession();
        return session.orElse(null);
    }


    protected Optional<Session> lookupZkSession()
    {
        return Optional.ofNullable(Sessions.getCurrent());
    }


    @Override
    public List<Locale> getAllLocales()
    {
        final String property = getCockpitProperties().getProperty(PROPERTY_COCKPITNG_LOCALIZATION_LOCALES);
        final List<Locale> locales = new ArrayList<>();
        if(property != null)
        {
            for(final String isoCode : Arrays.asList(property.split(",")))
            {
                try
                {
                    locales.add(LocaleUtils.toLocale(isoCode.trim()));
                }
                catch(final IllegalArgumentException e)
                {
                    LOG.error("wrong format for locale: " + isoCode, e);
                }
            }
            return locales;
        }
        return locales;
    }


    @Override
    public List<Locale> getAvailableDataLocales(final String principal)
    {
        final List<CockpitLocale> cockpitLocales = getAvailableCockpitLocales(principal);
        return unwrapCockpitLocales(cockpitLocales);
    }


    @Override
    public void toggleDataLocale(final Locale locale, final String principal)
    {
        Validate.notNull("Principal and Locale must not be null", principal, locale);
        final List<CockpitLocale> currentLocales = getAvailableCockpitLocales(principal);
        final int localeIndex = indexOfLocale(locale, currentLocales);
        if(localeIndex != -1)
        {
            final CockpitLocale cockpitLocale = currentLocales.get(localeIndex);
            cockpitLocale.setEnabled(!cockpitLocale.isEnabled());
            cacheLocales(fetchEnabledDataLocales(principal), ACTIVE_LOCALES);
            if(isLocalePersistenceOnToggleEnabled())
            {
                saveLocales(currentLocales, principal);
            }
        }
    }


    protected boolean isLocalePersistenceOnToggleEnabled()
    {
        return getCockpitProperties().getBoolean(PROPERTY_COCKPITNG_LEGACY_WIDGET_CONFIGURATION_PERSISTENCE_ENABLED, false);
    }


    @Override
    public List<Locale> getEnabledDataLocales(final String principal)
    {
        final Optional<List<Locale>> cachedActiveLocales = getCachedLocales(ACTIVE_LOCALES);
        return cachedActiveLocales.orElseGet(() -> {
            final List<Locale> result = fetchEnabledDataLocales(principal);
            cacheLocales(result, ACTIVE_LOCALES);
            return result;
        });
    }


    private List<Locale> fetchEnabledDataLocales(final String principal)
    {
        final List<CockpitLocale> availableCockpitLocales = getAvailableCockpitLocales(principal);
        final List<Locale> result = new ArrayList<>(availableCockpitLocales.size());
        for(final CockpitLocale cockpitlocale : availableCockpitLocales)
        {
            if(cockpitlocale.isEnabled())
            {
                result.add(cockpitlocale.getLocale());
            }
        }
        return result;
    }


    private <L> Optional<List<L>> getCachedLocales(final String attribute)
    {
        final Optional<Object> attributeValue = lookupZkSession() //
                        .map(s -> s.getAttribute(attribute)) //
                        .filter(List.class::isInstance);
        return attributeValue.map(List.class::cast);
    }


    private <L> void cacheLocales(final List<L> enabledDataLocales, final String attribute)
    {
        lookupZkSession().ifPresent(session -> session.setAttribute(attribute, enabledDataLocales));
    }


    @Override
    public Locale getDefaultDataLocale(final String principal)
    {
        final Optional<Object> sessionAttribute = lookupZkSession().map(session -> session.getAttribute(DEFAULT_DATA_LOCALE));
        if(sessionAttribute.isPresent() && sessionAttribute.get() instanceof Locale)
        {
            return (Locale)sessionAttribute.get();
        }
        final List<Locale> enabledDataLocales = getEnabledDataLocales(principal);
        if(CollectionUtils.isNotEmpty(enabledDataLocales))
        {
            for(final CockpitLocale cockpitLocale : getAvailableCockpitLocales(principal))
            {
                if(cockpitLocale.isDefaultLocale() && cockpitLocale.isEnabled())
                {
                    final Locale defaultLocale = cockpitLocale.getLocale();
                    lookupZkSession().ifPresent(session -> session.setAttribute(DEFAULT_DATA_LOCALE, defaultLocale));
                    return defaultLocale;
                }
            }
            LOG.warn("No locale is marked as default and enabled. First active locale is used");
            return enabledDataLocales.get(0);
        }
        throw new AvailableLocaleException("Given user has no active locales");
    }


    @Override
    public boolean isDataLocaleEnabled(final Locale locale, final String principal)
    {
        Validate.notNull("Locale must not be null", locale);
        return getEnabledDataLocales(principal).contains(locale);
    }


    protected void saveLocales(final Collection<CockpitLocale> cockpitLocale, final String principal)
    {
        Validate.notNull("Cockpit locales and principal must not be null", principal, cockpitLocale);
        final ConfigContext configContext = buildContext(principal, false);
        final CockpitLocales cockpitLocales = cockpitLocalesFactory.createCockpitLocales(cockpitLocale);
        try
        {
            cockpitConfigurationService.storeConfiguration(configContext, cockpitLocales);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Cannot save locale configuration");
            throw new WriteAvailableLocaleException("Cannot save locale configuration", e);
        }
    }


    protected ConfigContext buildContext(final String principal, final boolean addGroupDimension)
    {
        final DefaultConfigContext context = new DefaultConfigContext(LOCALE_CONTEXT);
        context.setPrincipal(principal);
        ConfigContext decorated = context;
        if(widgetConfigurationContextDecoratorList != null)
        {
            for(final WidgetConfigurationContextDecorator decorator : widgetConfigurationContextDecoratorList)
            {
                decorated = decorator.decorateContext(decorated, CockpitLocales.class, null);
            }
        }
        if(addGroupDimension && authorityGroupService != null)
        {
            final AuthorityGroup group = authorityGroupService.getActiveAuthorityGroupForUser(principal);
            if(group != null && StringUtils.isNotBlank(group.getCode()))
            {
                final String groupCode = group.getCode();
                final DefaultConfigContext groupCtx = new GroupContext(groupCode);
                for(final String name : decorated.getAttributeNames())
                {
                    groupCtx.addAttribute(name, decorated.getAttribute(name));
                }
                return groupCtx;
            }
        }
        return decorated;
    }


    private Integer indexOfLocale(final Locale locale, final List<CockpitLocale> currentLocales)
    {
        return Iterables.indexOf(currentLocales, input -> {
            assert input != null;
            return input.getLocale().equals(locale);
        });
    }


    private List<Locale> unwrapCockpitLocales(final Collection<CockpitLocale> cockpitLocales)
    {
        final List<Locale> result = new ArrayList<>(cockpitLocales.size());
        for(final CockpitLocale cockpitLocale : cockpitLocales)
        {
            result.add(cockpitLocale.getLocale());
        }
        return result;
    }


    protected List<CockpitLocale> getAvailableCockpitLocales(final String principal)
    {
        Validate.notNull(principal, "Principal must not be null");
        final Optional<List<CockpitLocale>> cachedLocales = getCachedLocales(AVAILABLE_LOCALES);
        return cachedLocales.orElseGet(() -> {
            final List<CockpitLocale> availableDataLocales = fetchAvailableDataLocales(principal);
            cacheLocales(availableDataLocales, AVAILABLE_LOCALES);
            return availableDataLocales;
        });
    }


    private List<CockpitLocale> fetchAvailableDataLocales(final String principal)
    {
        final ConfigContext context = buildContext(principal, true);
        CockpitLocales config = null;
        try
        {
            config = getCockpitConfigurationService().loadConfiguration(context, CockpitLocales.class);
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            // Fine with that
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No locale configuration found for user " + principal, e);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Cannot retrieve locale configuration", e);
        }
        final List<CockpitLocale> allCockpitLocales = getAllCockpitLocales(principal);
        if(config != null)
        {
            final Map<Locale, CockpitLocale> configuredLocaleMap = new HashMap<>();
            for(final CockpitLocale cockpitLocale : config.getCockpitLocale())
            {
                configuredLocaleMap.put(cockpitLocale.getLocale(), cockpitLocale);
            }
            for(final CockpitLocale cockpitLocale : allCockpitLocales)
            {
                final CockpitLocale confCockpitLocale = configuredLocaleMap.get(cockpitLocale.getLocale());
                if(confCockpitLocale != null)
                {
                    cockpitLocale.setEnabled(confCockpitLocale.isEnabled());
                    cockpitLocale.setDefaultLocale(confCockpitLocale.isDefaultLocale());
                    cockpitLocale.setName(cockpitLocale.getName());
                }
            }
        }
        return allCockpitLocales;
    }


    /**
     * Override, if your system has additional language management.
     *
     * @param principal
     *           user that can be used to restrict the list of available languages. Can be null.
     */
    protected List<CockpitLocale> getAllCockpitLocales(final String principal)
    {
        final List<CockpitLocale> ret = new ArrayList<>();
        for(final Locale locale : getAllLocales())
        {
            final CockpitLocale cockpitLocale = new CockpitLocale();
            cockpitLocale.setEnabled(true);
            cockpitLocale.setLocale(locale);
            cockpitLocale.setName(locale.getDisplayName());
            ret.add(cockpitLocale);
        }
        return ret;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    protected CockpitLocalesFactory getCockpitLocalesFactory()
    {
        return cockpitLocalesFactory;
    }


    public void setCockpitLocalesFactory(final CockpitLocalesFactory cockpitLocalesFactory)
    {
        this.cockpitLocalesFactory = cockpitLocalesFactory;
    }


    @Override
    public void reset()
    {
        cacheLocales(null, ACTIVE_LOCALES);
        cacheLocales(null, AVAILABLE_LOCALES);
    }


    public void setWidgetConfigurationContextDecoratorList(
                    final List<WidgetConfigurationContextDecorator> configContextDecoratorList)
    {
        this.widgetConfigurationContextDecoratorList = configContextDecoratorList;
    }


    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }


    private static class GroupContext extends DefaultConfigContext
    {
        private final String groupCode;


        public GroupContext(final String groupCode)
        {
            super();
            this.groupCode = groupCode == null ? "" : groupCode;
        }


        @Override
        public boolean equals(final Object object)
        {
            return super.equals(object) && groupCode.equals(((GroupContext)object).groupCode);
        }


        @Override
        public int hashCode()
        {
            return super.hashCode() + groupCode.hashCode();
        }
    }
}
