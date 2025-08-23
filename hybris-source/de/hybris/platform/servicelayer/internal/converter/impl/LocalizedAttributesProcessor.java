package de.hybris.platform.servicelayer.internal.converter.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

public final class LocalizedAttributesProcessor
{
    private static final Logger LOG = Logger.getLogger(LocalizedAttributesProcessor.class);
    private static final Comparator<Locale> LOCALE_COMPARATOR = (Comparator<Locale>)new LocaleComparator();
    private static final Comparator<Locale> REVERSED_LOCALE_COMPARATOR = (Comparator<Locale>)new ReversedLocaleComparator();
    private final I18NService i18NService;


    public LocalizedAttributesProcessor(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    public Map<Locale, Map<String, Object>> processQualifiersWithValues(Map<Locale, Map<String, Object>> additionalLocalizedValuesMap)
    {
        Map<Locale, Set<Locale>> specificFirstLoc2data = getLocaleOrder(additionalLocalizedValuesMap.keySet());
        Map<Locale, Map<String, Object>> ret = new HashMap<>(specificFirstLoc2data.size());
        for(Map.Entry<Locale, Set<Locale>> entry : specificFirstLoc2data.entrySet())
        {
            Locale dataLocale = entry.getKey();
            Set<Locale> nonDataLocales = entry.getValue();
            Map<String, Object> mergedLocValues = new HashMap<>();
            ret.put(dataLocale, mergedLocValues);
            if(nonDataLocales != null)
            {
                for(Locale nonDataLoc : nonDataLocales)
                {
                    mergeOrSkipAttribute(additionalLocalizedValuesMap.get(nonDataLoc), mergedLocValues, nonDataLoc);
                }
            }
            Map<String, Object> values = additionalLocalizedValuesMap.get(dataLocale);
            if(values != null)
            {
                mergeOrSkipAttribute(values, mergedLocValues, dataLocale);
            }
        }
        return ret;
    }


    public Map<Locale, Set<String>> processQualifiers(Map<Locale, Set<String>> dirtyLocalizedAttributes)
    {
        Map<Locale, Set<Locale>> specificFirstLoc2data = getLocaleReverseOrder(dirtyLocalizedAttributes.keySet());
        Map<Locale, Set<LocaleAttribute>> tempResult = Maps.newHashMapWithExpectedSize(dirtyLocalizedAttributes.size());
        Map<Locale, Set<String>> result = Maps.newHashMapWithExpectedSize(dirtyLocalizedAttributes.size());
        for(Locale locale : specificFirstLoc2data.keySet())
        {
            overrideLocaleWithMoreSpecific(dirtyLocalizedAttributes, tempResult, locale);
            if(CollectionUtils.isNotEmpty(specificFirstLoc2data.get(locale)))
            {
                for(Locale overriddenLocale : specificFirstLoc2data.get(locale))
                {
                    overrideLocaleWithMoreSpecific(dirtyLocalizedAttributes, tempResult, overriddenLocale);
                }
            }
        }
        for(Set<LocaleAttribute> attributes : tempResult.values())
        {
            for(LocaleAttribute locale : attributes)
            {
                Set<String> target = result.get(locale.originalLocale);
                if(target == null)
                {
                    target = new HashSet<>();
                    result.put(locale.originalLocale, target);
                }
                target.add(locale.qualifier);
            }
        }
        return result;
    }


    public Map<Locale, Set<Locale>> getLocaleOrder(Collection<Locale> locales)
    {
        return getLocaleOrderImpl(locales, LOCALE_COMPARATOR);
    }


    private void mergeOrSkipAttribute(Map<String, Object> additionalLocalizedValuesMap, Map<String, Object> mergedLocValuesInOut, Locale nonDataLoc)
    {
        for(Map.Entry<String, Object> entry : additionalLocalizedValuesMap.entrySet())
        {
            String qualifier = entry.getKey();
            Object value = entry.getValue();
            if(!mergedLocValuesInOut.containsKey(qualifier))
            {
                mergedLocValuesInOut.put(qualifier, value);
                continue;
            }
            LOG.warn("skipped " + qualifier + "[" + nonDataLoc + "]=" + value + " since there is more specific value!");
        }
    }


    private Map<Locale, Set<Locale>> getLocaleReverseOrder(Collection<Locale> locales)
    {
        return getLocaleOrderImpl(locales, REVERSED_LOCALE_COMPARATOR);
    }


    private Map<Locale, Set<Locale>> getLocaleOrderImpl(Collection<Locale> locales, Comparator<Locale> comparator)
    {
        Map<Locale, Set<Locale>> loc2data = new HashMap<>(locales.size());
        for(Locale loc : locales)
        {
            Locale dataLoc = this.i18NService.getBestMatchingLocale(loc);
            if(!loc.equals(dataLoc))
            {
                Set<Locale> nonDataLocales = loc2data.get(dataLoc);
                if(nonDataLocales == null)
                {
                    nonDataLocales = new TreeSet<>(comparator);
                    loc2data.put(dataLoc, nonDataLocales);
                }
                nonDataLocales.add(loc);
                continue;
            }
            if(!loc2data.containsKey(loc))
            {
                loc2data.put(loc, null);
            }
        }
        return loc2data;
    }


    private void overrideLocaleWithMoreSpecific(Map<Locale, Set<String>> dirtyLocalizedAttributes, Map<Locale, Set<LocaleAttribute>> tempResult, Locale locale)
    {
        if(MapUtils.isNotEmpty(dirtyLocalizedAttributes))
        {
            Locale dbBasedLocale = this.i18NService.getBestMatchingLocale(locale);
            Set<String> qualifiers = dirtyLocalizedAttributes.get(locale);
            if(CollectionUtils.isNotEmpty(qualifiers))
            {
                for(String qualifier : qualifiers)
                {
                    Set<LocaleAttribute> target = tempResult.get(dbBasedLocale);
                    if(target == null)
                    {
                        target = new HashSet<>();
                        tempResult.put(dbBasedLocale, target);
                    }
                    LocaleAttribute newOne = new LocaleAttribute(locale, qualifier);
                    if(target.contains(newOne))
                    {
                        target.remove(newOne);
                        LOG.info("Overwriting general attribute " + newOne.qualifier + " with the more specific for locale " + newOne.originalLocale);
                    }
                    target.add(newOne);
                }
            }
        }
    }
}
