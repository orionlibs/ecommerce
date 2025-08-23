/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n;

import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Executions;

/**
 * Manages Locale for a cockpit application. The currently set locale is used for label localization on the cockpit (UI)
 * side.
 */
public interface CockpitLocaleService
{
    /**
     * Returns the currently active locale. You normally do not need to call this. All localized widget labels are
     * automatically rendered using the current locale.
     *
     * @return currently active locale
     */
    Locale getCurrentLocale();


    /**
     * Returns a list of all available locales.
     *
     * @return a list of all available locales
     */
    List<Locale> getAllLocales();


    /**
     * Returns a list of all available locales.
     *
     * @return a list of all available UI locales
     */
    default List<Locale> getAllUILocales()
    {
        return getAllLocales();
    }


    /**
     * Changes the current locale to the given one. After calling this you need to refresh the cockpit application (unless
     * this is called before the application was rendered, e.g. shortly after login). You can use
     * {@link Executions#sendRedirect(String)} with <code>null</code> as a parameter to re-render the application.
     *
     * @param locale
     *           the locale to be set as the current one
     */
    void setCurrentLocale(Locale locale);


    /**
     * Using this method you can execute code in a context of a different locale than the current one
     *
     * @param executionBody
     *           the execution body
     * @param locale
     *           the locale
     * @return the result of the execution body call or null if execution is without result
     */
    <T> T executeWithLocale(AbstractExecutionBody<T> executionBody, Locale locale);


    /**
     * Toggles the specified data locale. Does nothing if the locale does not exist. Toggling data locale would enable or
     * disable it for localized editors.
     *
     * @param locale
     *           to toggle
     * @param principal
     *           needed to filter available cockpit locales
     * @throws com.hybris.cockpitng.i18n.exception.AvailableLocaleException
     *            when an attempt to toggle the cockpit locale failed.
     */
    void toggleDataLocale(final Locale locale, final String principal);


    /**
     * Returns a collection of available data locales (both enabled and disabled) for specified principal.
     * An exception of type {@code AvailableLocaleException} is thrown when an attempt to load the cockpit locales failed.
     *
     * @param principal
     *           needed to filter available cockpit locales
     * @return collection of available locales
     * @throws com.hybris.cockpitng.i18n.exception.AvailableLocaleException
     *            when an attempt to load the cockpit locales failed.
     */
    List<Locale> getAvailableDataLocales(String principal);


    /**
     * Returns a collection of enabled locales for specified principal. Only enabled locales are used to render localized
     * attributes. By disabling locale you can reduce the visible localized data.
     * An exception of type {@code AvailableLocaleException} is thrown when an attempt to load the cockpit locales failed.
     *
     * @param principal
     *           needed to filter available cockpit locales
     * @return collection of enabled locales
     * @throws com.hybris.cockpitng.i18n.exception.AvailableLocaleException
     *            when an attempt to load the cockpit locales failed.
     */
    List<Locale> getEnabledDataLocales(String principal);


    /**
     * Returns the default data locale for specified principal. A collapsed localized editor would only show the data using
     * the default data locale.
     * An exception of type {@code AvailableLocaleException} is thrown when an attempt to load the cockpit locales failed.
     *
     * @param principal
     *           needed to filter available cockpit locales
     * @return {@link Locale}
     * @throws com.hybris.cockpitng.i18n.exception.AvailableLocaleException
     *            when an attempt to load the cockpit locales failed.
     */
    Locale getDefaultDataLocale(String principal);


    /**
     * Checks if the given data locale is an active data locale for the given user.
     *
     * @param locale
     *           to check
     * @param principal
     *           needed to filter available cockpit locales
     * @return true if data locale is enabled.
     */
    boolean isDataLocaleEnabled(Locale locale, String principal);
}
