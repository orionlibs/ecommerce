package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.util.Key;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ModelValueHistory implements Serializable
{
    private static final int INITIAL_COLLECTION_ERRORS_SIZE = 4;
    private static final int INITIAL_COLLECTION_LOCALIZED_ERRORS_SIZE = 4;
    private static final int INITIAL_COLLECTION_DIRTY_ATTRIBUTES_SIZE = 8;
    private static final int INITIAL_COLLECTION_DIRTY_LOCALIZED_ATTRIBUTES_SIZE = 8;
    private long persistenceVersion;
    private final OriginalValueHolder<String> originalValues = OriginalValueHolder.newUnlocalized();
    private final OriginalValueHolder<Key<Locale, String>> originalLocalizedValues = OriginalValueHolder.newLocalized();
    private volatile Set<String> dirtyAttributes;
    private volatile Map<Locale, Set<String>> dirtyLocAttributes;
    private volatile Map<String, ModelLoadingException> attributeErrors;
    private volatile Map<Locale, Map<String, ModelLoadingException>> locAttributeErrors;
    private volatile transient HistoryListener listener;
    private AttributeProvider attributeProvider;


    public ModelValueHistory()
    {
        this(null);
    }


    public ModelValueHistory(AttributeProvider attributeProvider)
    {
        this.attributeProvider = attributeProvider;
    }


    public synchronized void setAttributeLoadingError(String qualifier, ModelLoadingException e)
    {
        Map<String, ModelLoadingException> localattributeErrors = this.attributeErrors;
        if(localattributeErrors == null)
        {
            this.attributeErrors = localattributeErrors = new HashMap<>(4);
        }
        localattributeErrors.put(qualifier, e);
    }


    public synchronized void resetAttributeLoadingError(String qualifier)
    {
        resetAttributeLoadingErrorNoSync(qualifier);
    }


    private void resetAttributeLoadingErrorNoSync(String qualifier)
    {
        Map<String, ModelLoadingException> localattributeErrors = this.attributeErrors;
        if(localattributeErrors != null)
        {
            localattributeErrors.remove(qualifier);
        }
    }


    public synchronized void throwAttributeError(String qualifier) throws ModelLoadingException
    {
        Map<String, ModelLoadingException> localattributeErrors = this.attributeErrors;
        if(localattributeErrors != null)
        {
            ModelLoadingException e = localattributeErrors.get(qualifier);
            if(e != null)
            {
                throw e;
            }
        }
    }


    public synchronized void setLocAttributeLoadingError(String qualifier, Locale loc, ModelLoadingException e)
    {
        Map<Locale, Map<String, ModelLoadingException>> localAttributeErrors = this.locAttributeErrors;
        if(localAttributeErrors == null)
        {
            this.locAttributeErrors = localAttributeErrors = new HashMap<>(4);
        }
        Map<String, ModelLoadingException> attrMap = localAttributeErrors.get(loc);
        if(attrMap == null)
        {
            localAttributeErrors.put(loc, attrMap = new HashMap<>(4));
        }
        attrMap.put(qualifier, e);
    }


    public synchronized void resetLocAttributeLoadingError(String qualifier, Locale loc)
    {
        resetLocAttributeLoadingErrorNoSync(qualifier, loc);
    }


    private void resetLocAttributeLoadingErrorNoSync(String qualifier, Locale loc)
    {
        Map<Locale, Map<String, ModelLoadingException>> localAttributeErrors = this.locAttributeErrors;
        if(localAttributeErrors != null)
        {
            Map<String, ModelLoadingException> attrMap = localAttributeErrors.get(loc);
            if(attrMap != null)
            {
                attrMap.remove(qualifier);
            }
        }
    }


    public synchronized void throwLocAttributeError(String qualifier, Locale loc) throws ModelLoadingException
    {
        Map<Locale, Map<String, ModelLoadingException>> localAttributeErrors = this.locAttributeErrors;
        if(localAttributeErrors != null)
        {
            Map<String, ModelLoadingException> attrMap = localAttributeErrors.get(loc);
            if(attrMap != null)
            {
                ModelLoadingException e = attrMap.get(qualifier);
                if(e != null)
                {
                    throw e;
                }
            }
        }
    }


    public HistoryListener getListener()
    {
        return this.listener;
    }


    public synchronized void setListener(HistoryListener givenListener)
    {
        HistoryListener localListener = this.listener;
        if(givenListener != null && localListener != null && localListener != givenListener)
        {
            throw new IllegalStateException("already got modification listener " + localListener + " - cannot set " + givenListener);
        }
        this.listener = givenListener;
    }


    public synchronized Object getOriginalValue(String qualifier)
    {
        return this.originalValues.get(qualifier, this.attributeProvider);
    }


    public synchronized Object getOriginalValue(String qualifier, Locale locale)
    {
        Key key = Key.get(locale, qualifier);
        return this.originalLocalizedValues.get(key, this.attributeProvider);
    }


    public synchronized void loadOriginalValue(String qualifier, Object value)
    {
        loadOriginalValue(qualifier, value, true);
    }


    public synchronized void loadOriginalValue(String qualifier, Object value, boolean resetDirtyFlag)
    {
        this.originalValues.set(qualifier, value);
        if(resetDirtyFlag)
        {
            removeDirtyAttributeNoSync(qualifier);
        }
    }


    public synchronized void clearOriginalValue(String qualifier)
    {
        this.originalValues.remove(qualifier);
        removeDirtyAttributeNoSync(qualifier);
    }


    public synchronized void markUnchanged(String qualifier)
    {
        removeDirtyAttributeNoSync(qualifier);
    }


    private void removeDirtyAttributeNoSync(String qualifier)
    {
        Set<String> localDirtyAttributes = this.dirtyAttributes;
        if(localDirtyAttributes != null)
        {
            localDirtyAttributes.remove(qualifier);
            if(localDirtyAttributes.isEmpty())
            {
                this.dirtyAttributes = null;
            }
        }
        resetAttributeLoadingErrorNoSync(qualifier);
    }


    public synchronized void loadOriginalValue(String qualifier, Locale locale, Object value)
    {
        loadOriginalValue(qualifier, locale, value, true);
    }


    public synchronized void loadOriginalValue(String qualifier, Locale locale, Object value, boolean resetDirtyFlag)
    {
        this.originalLocalizedValues.set(Key.create(locale, qualifier), value);
        if(resetDirtyFlag)
        {
            removeDirtyLocAttributeNoSync(qualifier, locale);
        }
    }


    public synchronized void clearOriginalValue(String qualifier, Locale locale)
    {
        this.originalLocalizedValues.remove(Key.get(locale, qualifier));
        removeDirtyLocAttributeNoSync(qualifier, locale);
    }


    private void removeDirtyLocAttributeNoSync(String qualifier, Locale locale)
    {
        Map<Locale, Set<String>> localDirtyLocAttributes = this.dirtyLocAttributes;
        if(localDirtyLocAttributes != null)
        {
            Set<String> qualifiers = localDirtyLocAttributes.get(locale);
            if(qualifiers != null)
            {
                qualifiers.remove(qualifier);
                if(qualifiers.isEmpty())
                {
                    localDirtyLocAttributes.remove(locale);
                    if(localDirtyLocAttributes.isEmpty())
                    {
                        this.dirtyLocAttributes = null;
                    }
                }
            }
        }
        resetLocAttributeLoadingErrorNoSync(qualifier, locale);
    }


    public synchronized void markUnchanged(String qualifier, Locale locale)
    {
        Map<Locale, Set<String>> localDirtyLocAttributes = this.dirtyLocAttributes;
        if(localDirtyLocAttributes != null)
        {
            Set<String> qualifiers = localDirtyLocAttributes.get(locale);
            if(qualifiers != null)
            {
                qualifiers.remove(qualifier);
                if(qualifiers.isEmpty())
                {
                    localDirtyLocAttributes.remove(locale);
                    if(localDirtyLocAttributes.isEmpty())
                    {
                        this.dirtyLocAttributes = null;
                    }
                }
            }
            resetLocAttributeLoadingErrorNoSync(qualifier, locale);
        }
    }


    public synchronized boolean isValueLoaded(String qualifier)
    {
        return this.originalValues.hasKey(qualifier);
    }


    public synchronized boolean isValueLoaded(String qualifier, Locale locale)
    {
        return this.originalLocalizedValues.hasKey(Key.get(locale, qualifier));
    }


    public synchronized void markDirty(String qualifier)
    {
        Set<String> localDirtyAttributes = this.dirtyAttributes;
        if(localDirtyAttributes == null)
        {
            this.dirtyAttributes = localDirtyAttributes = new HashSet<>(8);
        }
        localDirtyAttributes.add(qualifier);
        resetAttributeLoadingErrorNoSync(qualifier);
        notifyListener();
    }


    private void notifyListener()
    {
        HistoryListener locallistener = this.listener;
        if(locallistener != null)
        {
            locallistener.historyChanged();
        }
    }


    public synchronized void markDirty(String qualifier, Locale locale)
    {
        Map<Locale, Set<String>> localDirtyLocAttributes = this.dirtyLocAttributes;
        if(localDirtyLocAttributes == null)
        {
            this.dirtyLocAttributes = localDirtyLocAttributes = new LinkedHashMap<>(8);
        }
        Set<String> attributes = localDirtyLocAttributes.get(locale);
        if(attributes == null)
        {
            attributes = new HashSet<>(8);
            localDirtyLocAttributes.put(locale, attributes);
        }
        attributes.add(qualifier);
        resetLocAttributeLoadingError(qualifier, locale);
        notifyListener();
    }


    public synchronized void mergeDirty(Map<Locale, Set<Locale>> data2nonDataLocales)
    {
        Map<Locale, Set<String>> dirtyLocAttributes = this.dirtyLocAttributes;
        if(dirtyLocAttributes != null)
        {
            for(Map.Entry<Locale, Set<Locale>> e : data2nonDataLocales.entrySet())
            {
                if(e.getValue() != null)
                {
                    Locale dataLocale = e.getKey();
                    Set<String> dataLocaleAttributes = null;
                    for(Locale nonDataLocale : e.getValue())
                    {
                        Set<String> nonDataAttributes = dirtyLocAttributes.remove(nonDataLocale);
                        if(nonDataAttributes != null && !nonDataAttributes.isEmpty())
                        {
                            if(dataLocaleAttributes == null)
                            {
                                dataLocaleAttributes = dirtyLocAttributes.get(dataLocale);
                                if(dataLocaleAttributes == null)
                                {
                                    dataLocaleAttributes = new HashSet<>(8);
                                    dirtyLocAttributes.put(dataLocale, dataLocaleAttributes);
                                }
                            }
                            dataLocaleAttributes.addAll(nonDataAttributes);
                        }
                    }
                }
            }
        }
    }


    public synchronized boolean isDirty(String qualifier)
    {
        Set<String> localDirtyAttributes = this.dirtyAttributes;
        return (localDirtyAttributes != null && localDirtyAttributes.contains(qualifier));
    }


    public synchronized boolean isDirty()
    {
        Set<String> localDirtyAttributes = this.dirtyAttributes;
        if(localDirtyAttributes != null && !localDirtyAttributes.isEmpty())
        {
            return true;
        }
        Map<Locale, Set<String>> localDirtyLocAttributes = this.dirtyLocAttributes;
        if(localDirtyLocAttributes != null && !localDirtyLocAttributes.isEmpty())
        {
            return true;
        }
        return false;
    }


    public synchronized boolean isDirty(String qualifier, Locale locale)
    {
        Map<Locale, Set<String>> localeDirtyLocAttributes = this.dirtyLocAttributes;
        if(localeDirtyLocAttributes != null)
        {
            if(locale == null)
            {
                for(Map.Entry<Locale, Set<String>> e : localeDirtyLocAttributes.entrySet())
                {
                    if(((Set)e.getValue()).contains(qualifier))
                    {
                        return true;
                    }
                }
                return false;
            }
            Set<String> attributes = localeDirtyLocAttributes.get(locale);
            return (attributes != null && attributes.contains(qualifier));
        }
        return false;
    }


    public synchronized Set<String> getDirtyAttributes()
    {
        Set<String> localDirtyAttributes = this.dirtyAttributes;
        if(localDirtyAttributes == null)
        {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(localDirtyAttributes);
    }


    public synchronized Map<Locale, Set<String>> getDirtyLocalizedAttributes()
    {
        Map<Locale, Set<String>> dirtyLocAttributes = this.dirtyLocAttributes;
        if(dirtyLocAttributes == null)
        {
            return Collections.emptyMap();
        }
        Map<Locale, Set<String>> ret = new LinkedHashMap<>((int)(dirtyLocAttributes.size() / 0.75F) + 1);
        for(Map.Entry<Locale, Set<String>> entry : dirtyLocAttributes.entrySet())
        {
            ret.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<>(entry.getValue())));
        }
        return ret;
    }


    public Map<String, Set<Locale>> getAllDirtyAttributes()
    {
        if(isDirty())
        {
            Map<String, Set<Locale>> modAttrQualMap = (Map<String, Set<Locale>>)new HashMap<>();
            for(String unlocAttrQual : getDirtyAttributes())
            {
                modAttrQualMap.put(unlocAttrQual, null);
            }
            for(Map.Entry<Locale, Set<String>> mapentry : getDirtyLocalizedAttributes().entrySet())
            {
                for(String locAttrQual : mapentry.getValue())
                {
                    if(modAttrQualMap.get(locAttrQual) == null)
                    {
                        Set<Locale> localeset = new HashSet<>();
                        localeset.add(mapentry.getKey());
                        modAttrQualMap.put(locAttrQual, localeset);
                        continue;
                    }
                    ((Set<Locale>)modAttrQualMap.get(locAttrQual)).add(mapentry.getKey());
                }
            }
            return modAttrQualMap;
        }
        return Collections.emptyMap();
    }


    public synchronized Set<String> getLoadedAttributes()
    {
        return this.originalValues.toKeys();
    }


    public synchronized Set<Key<Locale, String>> getLoadedLocAttributes()
    {
        return this.originalLocalizedValues.toKeys();
    }


    public long getPersistenceVersion()
    {
        return this.persistenceVersion;
    }


    public void setPersistenceVersion(long persistenceVersion)
    {
        this.persistenceVersion = persistenceVersion;
    }


    public void setAttributeProvider(AttributeProvider attributeProvider)
    {
        this.attributeProvider = attributeProvider;
    }
}
