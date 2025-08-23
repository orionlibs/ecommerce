package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemAttributeProvider;
import de.hybris.platform.servicelayer.internal.model.LocMap;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.internal.model.impl.AttributeProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.strategies.FetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ItemModelContextImpl implements ItemModelInternalContext, Serializable
{
    private final String tenantID;
    private PK pk;
    private PK newPK;
    private String itemType;
    private final Map<String, LocMap<Locale, Object>> combinedLocalizedAttributeValues = new HashMap<>();
    private final Map<String, Object> combinedAttributeValues = new HashMap<>();
    private SerializationStrategy serializationStrategy;
    private final ModelValueHistory valueHistory;
    private FetchStrategy fetchStrategy;
    private AttributeProvider attributeProvider;
    private LocaleProvider locProvider;
    private DynamicAttributesProvider dynamicAttributesProvider;


    ItemModelContextImpl(ItemContextBuilder builder)
    {
        this.tenantID = builder.getTenantID();
        this.pk = builder.getPk();
        this.itemType = builder.getItemType();
        this.serializationStrategy = builder.getSerializationStrategy();
        this.valueHistory = builder.getValueHistory();
        this.attributeProvider = builder.getAttributeProvider();
        this.locProvider = builder.getLocProvider();
        this.dynamicAttributesProvider = builder.getDynamicAttributesProvider();
        this.fetchStrategy = builder.getFetchStrategy();
    }


    public int hashCode(AbstractItemModel itemModel)
    {
        PK pk = getPK();
        return (pk == null) ? System.identityHashCode(itemModel) : pk.hashCode();
    }


    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            return true;
        }
        if(!(obj instanceof ItemModelContextImpl))
        {
            return false;
        }
        ItemModelContextImpl other = (ItemModelContextImpl)obj;
        PK pk = getPK();
        if(pk != null && pk.equals(other.getPK()))
        {
            return (this.tenantID == other.tenantID || (this.tenantID != null && this.tenantID.equals(other.tenantID)));
        }
        return false;
    }


    public int hashCode()
    {
        return (new HashCodeBuilder()).append(getPK()).append(this.tenantID).toHashCode();
    }


    public ModelValueHistory getValueHistory()
    {
        return this.valueHistory;
    }


    public boolean isDynamicAttribute(String attributeName)
    {
        if(this.dynamicAttributesProvider == null)
        {
            return false;
        }
        return this.dynamicAttributesProvider.isDynamic(attributeName);
    }


    public Object writeReplace(Object model) throws ObjectStreamException
    {
        if(this.serializationStrategy != null)
        {
            return this.serializationStrategy.writeReplace((AbstractItemModel)model);
        }
        return model;
    }


    public PK getPK()
    {
        return this.pk;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public long getPersistenceVersion()
    {
        return getValueHistory().getPersistenceVersion();
    }


    public Object getSource()
    {
        return (this.attributeProvider == null) ? null : ((ItemAttributeProvider)this.attributeProvider).getItem();
    }


    public PersistenceObject getPersistenceSource()
    {
        return (this.attributeProvider == null) ? null : ((ItemAttributeProvider)this.attributeProvider).getPersistenceObject();
    }


    public boolean isDirty()
    {
        return getValueHistory().isDirty();
    }


    public boolean exists()
    {
        if(isNew())
        {
            return false;
        }
        PersistenceObject src = getPersistenceSource();
        return (src != null && src.isAlive());
    }


    public boolean isRemoved()
    {
        if(isNew())
        {
            return false;
        }
        PersistenceObject src = getPersistenceSource();
        return (src == null || !src.isAlive());
    }


    public boolean isUpToDate()
    {
        if(isNew())
        {
            return false;
        }
        PersistenceObject src = getPersistenceSource();
        if(src == null || !src.isAlive())
        {
            return false;
        }
        if(isDirty())
        {
            return false;
        }
        if(getPersistenceVersion() != src.getPersistenceVersion())
        {
            return false;
        }
        return true;
    }


    public boolean isNew()
    {
        return (this.pk == null);
    }


    public <T> T getValue(String key, T currentValue)
    {
        T result = currentValue;
        if(!isNew())
        {
            if(getFetchStrategy().needsFetch(key))
            {
                result = (T)loadUnlocalizedAttribute(key, false, true);
            }
            else if(!isLoaded(key) && !isDirty(key))
            {
                result = (T)loadUnlocalizedAttribute(key, true, true);
            }
        }
        throwLoadingError(key);
        return result;
    }


    public <T> T getPropertyValue(String attribute)
    {
        T value = (T)this.combinedAttributeValues.get(attribute);
        if(value != null)
        {
            return value;
        }
        value = getValue(attribute, value);
        this.combinedAttributeValues.put(attribute, value);
        return value;
    }


    public <T> void setPropertyValue(String attribute, T value)
    {
        T val = setValue(attribute, value);
        this.combinedAttributeValues.put(attribute, val);
    }


    public Object getRawPropertyValue(String qualifier)
    {
        return this.combinedAttributeValues.get(qualifier);
    }


    public void setRawPropertyValue(String qualifier, Object value)
    {
        this.combinedAttributeValues.put(qualifier, value);
    }


    protected Object loadUnlocalizedAttribute(String key, boolean keepAsOriginalValue, boolean resetDirty)
    {
        Object ret = (this.attributeProvider == null) ? null : this.attributeProvider.getAttribute(key);
        if(keepAsOriginalValue)
        {
            getValueHistory().loadOriginalValue(key, ret, resetDirty);
        }
        return ret;
    }


    public <T> T setValue(String key, T newValue)
    {
        if(!getFetchStrategy().isMutable())
        {
            throw new IllegalStateException("You are not allowed to modify cached model");
        }
        markDirty(key);
        return newValue;
    }


    public Set<String> getDirtyAttributes()
    {
        return getValueHistory().getDirtyAttributes();
    }


    public boolean isDirty(String attribute)
    {
        return getValueHistory().isDirty(attribute);
    }


    public boolean isLoaded(String attribute)
    {
        return ("pk".equalsIgnoreCase(attribute) || "itemtype".equalsIgnoreCase(attribute) ||
                        getValueHistory().isValueLoaded(attribute));
    }


    public <T> T getOriginalValue(String key)
    {
        return (T)getValueHistory().getOriginalValue(key);
    }


    public Object loadOriginalValue(String attribute)
    {
        return loadUnlocalizedAttribute(attribute, true, false);
    }


    public Object loadOriginalValue(String attribute, Locale locale)
    {
        return loadLocalized(null, attribute, locale, true, false);
    }


    public <T> T getDynamicValue(AbstractItemModel model, String attribute)
    {
        return (getDynamicAttributesProvider() == null) ? null : (T)getDynamicAttributesProvider().get(model, attribute);
    }


    public <T> void setDynamicValue(AbstractItemModel model, String attribute, T value)
    {
        if(getDynamicAttributesProvider() != null)
        {
            getDynamicAttributesProvider().set(model, attribute, value);
        }
    }


    public <T> T getLocalizedDynamicValue(AbstractItemModel model, String attribute, Locale loc)
    {
        if(getDynamicAttributesProvider() == null)
        {
            return null;
        }
        Locale dataLocale = toDataLocale(loc);
        return (T)getDynamicAttributesProvider().getLocalized(model, attribute, dataLocale);
    }


    public <T> void setLocalizedDynamicValue(AbstractItemModel model, String attribute, Locale loc, T value)
    {
        if(getDynamicAttributesProvider() != null)
        {
            Locale dataLocale = toDataLocale(loc);
            getDynamicAttributesProvider().setLocalized(model, attribute, value, dataLocale);
        }
    }


    public <T> T getLocalizedValue(String key, Locale locale)
    {
        Locale dataLocale = toDataLocale(locale);
        T result = null;
        if(getFetchStrategy().needsFetch(key))
        {
            result = (T)getLocalizedWithFallback(null, key, dataLocale, false, false);
        }
        else
        {
            LocMap<Locale, Object> locMap = ensureLocAttributeMap(key);
            result = (T)getLocalizedWithFallback((Map<Locale, Object>)locMap, key, dataLocale, true, false);
        }
        throwLoadingError(key, dataLocale);
        return result;
    }


    public <T> T getLocalizedRelationValue(String key, Locale locale)
    {
        Locale dataLocale = toDataLocale(locale);
        T result = null;
        if(getFetchStrategy().needsFetch(key))
        {
            result = (T)getLocalizedWithFallback(null, key, dataLocale, false, true);
        }
        else
        {
            LocMap<Locale, Object> locMap = ensureLocAttributeMap(key);
            result = (T)getLocalizedWithFallback((Map<Locale, Object>)locMap, key, dataLocale, true, true);
        }
        throwLoadingError(key, dataLocale);
        return result;
    }


    private LocMap<Locale, Object> ensureLocAttributeMap(String qualifier)
    {
        LocMap<Locale, Object> attributeLocMap = null;
        synchronized(this.combinedLocalizedAttributeValues)
        {
            attributeLocMap = this.combinedLocalizedAttributeValues.get(qualifier);
            if(attributeLocMap == null)
            {
                this.combinedLocalizedAttributeValues.put(qualifier, attributeLocMap = new LocMap());
            }
        }
        return attributeLocMap;
    }


    protected Object getLocalizedWithFallback(Map<Locale, Object> attrLocMap, String qualifier, Locale dataLocale, boolean keepOriginals, boolean isRelationAttribute) throws IllegalArgumentException
    {
        Object result = getOrLoadLocalized(attrLocMap, qualifier, dataLocale, keepOriginals);
        if(isEmptyValue(result, isRelationAttribute) && getLocaleProvider(false) != null &&
                        getLocaleProvider(true).isFallbackEnabled())
        {
            for(Locale fallbackDataLocale : getLocaleProvider(true).getFallbacks(dataLocale))
            {
                result = getOrLoadLocalized(attrLocMap, qualifier, fallbackDataLocale, keepOriginals);
                if(!isEmptyValue(result, isRelationAttribute))
                {
                    break;
                }
            }
        }
        return result;
    }


    private boolean isEmptyValue(Object value, boolean isRelationAttribute)
    {
        return (value == null || (isRelationAttribute && CollectionUtils.isEmpty((Collection)value)));
    }


    protected <T> T getOrLoadLocalized(Map<Locale, Object> locMap, String qualifier, Locale dataLoc, boolean keepAsOriginal)
    {
        if(!isNew() && !isLoaded(qualifier, dataLoc) && !isDirty(qualifier, dataLoc))
        {
            return loadLocalized(locMap, qualifier, dataLoc, keepAsOriginal);
        }
        return (locMap != null) ? (T)locMap.get(dataLoc) : null;
    }


    protected <T> T loadLocalized(Map<Locale, Object> locMap, String qualifier, Locale dataLoc, boolean keepAsOriginal)
    {
        return loadLocalized(locMap, qualifier, dataLoc, keepAsOriginal, true);
    }


    protected <T> T loadLocalized(Map<Locale, Object> locMap, String qualifier, Locale dataLoc, boolean keepAsOriginal, boolean resetDirty)
    {
        T ret = (this.attributeProvider == null) ? null : (T)this.attributeProvider.getLocalizedAttribute(qualifier, dataLoc);
        if(locMap != null)
        {
            locMap.put(dataLoc, ret);
        }
        if(keepAsOriginal)
        {
            getValueHistory().loadOriginalValue(qualifier, dataLoc, ret, resetDirty);
        }
        return ret;
    }


    protected Locale toDataLocale(Locale locale) throws IllegalArgumentException
    {
        if(locale == null)
        {
            return getCurrentLocale();
        }
        return (getLocaleProvider(false) != null) ? getLocaleProvider(true).toDataLocale(locale) : locale;
    }


    public <T> void setLocalizedValue(String qualifier, Locale locale, T value)
    {
        Locale dataLocale = toDataLocale(locale);
        if(!getFetchStrategy().isMutable())
        {
            throw new IllegalStateException("You are not allowed to modify cached model");
        }
        ensureLocAttributeMap(qualifier).put(dataLocale, value);
        markDirty(qualifier, dataLocale);
    }


    public boolean isDirty(String attribute, Locale loc)
    {
        return getValueHistory().isDirty(attribute, loc);
    }


    public boolean isLoaded(String attribute, Locale loc)
    {
        return getValueHistory().isValueLoaded(attribute, toDataLocale(loc));
    }


    public <T> T getOriginalValue(String key, Locale loc)
    {
        return (T)getValueHistory().getOriginalValue(key, toDataLocale(loc));
    }


    public Map<Locale, Set<String>> getDirtyLocalizedAttributes()
    {
        return getValueHistory().getDirtyLocalizedAttributes();
    }


    protected void throwLoadingError(String qualifier)
    {
        getValueHistory().throwAttributeError(qualifier);
    }


    protected void throwLoadingError(String qualifier, Locale dataLocale)
    {
        getValueHistory().throwLocAttributeError(qualifier, dataLocale);
    }


    protected void markDirty(String attribute)
    {
        getValueHistory().markDirty(attribute);
    }


    protected void markDirty(String attribute, Locale dataLocale)
    {
        getValueHistory().markDirty(attribute, dataLocale);
    }


    protected Locale getCurrentLocale() throws IllegalStateException
    {
        return getLocaleProvider(true).getCurrentDataLocale();
    }


    public SerializationStrategy getSerializationStrategy()
    {
        return this.serializationStrategy;
    }


    public LocaleProvider getLocaleProvider(boolean required)
    {
        if(this.locProvider == null && required)
        {
            throw new IllegalStateException("there is no LocaleProvider for (detached) model " + this);
        }
        return this.locProvider;
    }


    public void setLocaleProvider(LocaleProvider localeProvider)
    {
        this.locProvider = localeProvider;
    }


    public DynamicAttributesProvider getDynamicAttributesProvider()
    {
        return this.dynamicAttributesProvider;
    }


    protected FetchStrategy getFetchStrategy()
    {
        return this.fetchStrategy;
    }


    public String getTenantId()
    {
        return this.tenantID;
    }


    public Map<String, LocMap<Locale, Object>> getCombinedLocalizedValuesMap()
    {
        return this.combinedLocalizedAttributeValues;
    }


    public void beforeAttach(LocaleProvider localeProvider)
    {
        this.locProvider = localeProvider;
    }


    public void setAttributeProvider(AttributeProvider attributeProvider)
    {
        this.attributeProvider = attributeProvider;
    }


    public AttributeProvider getAttributeProvider()
    {
        return this.attributeProvider;
    }


    public void setDynamicAttributesProvider(DynamicAttributesProvider attributeProvider)
    {
        this.dynamicAttributesProvider = attributeProvider;
    }


    public void afterCreate(LocaleProvider locProvider, AttributeProvider attrProvider, DynamicAttributesProvider dynProvider, PK pk, long persistenceVersion, String typeCode)
    {
        this.pk = pk;
        this.itemType = typeCode;
        this.locProvider = locProvider;
        this.attributeProvider = attrProvider;
        this.dynamicAttributesProvider = dynProvider;
        getValueHistory().setPersistenceVersion(persistenceVersion);
        getValueHistory().setAttributeProvider(attrProvider);
    }


    public void afterUpdate(long persistenceVersion)
    {
        getValueHistory().setPersistenceVersion(persistenceVersion);
    }


    public void afterReload(long persistenceVersion)
    {
        getValueHistory().setPersistenceVersion(persistenceVersion);
    }


    public void setFetchStrategy(FetchStrategy fetchStrategy)
    {
        this.fetchStrategy = fetchStrategy;
    }


    public void setSerializationStrategy(SerializationStrategy strategy)
    {
        this.serializationStrategy = strategy;
    }


    public PK getNewPK()
    {
        return this.newPK;
    }


    public PK generateNewPK()
    {
        if(this.newPK == null)
        {
            if(!isNew())
            {
                throw new IllegalStateException("Could not generate new PK for model which is not new.");
            }
            this.newPK = generatePK(getItemType());
        }
        return this.newPK;
    }


    public void unloadAttribute(String attribute)
    {
        this.combinedAttributeValues.remove(attribute);
        getValueHistory().clearOriginalValue(attribute);
    }


    private int getTypeCode(String type)
    {
        TypeInfoMap typeInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(type);
        return typeInfo.getItemTypeCode();
    }


    private PK generatePK(String type)
    {
        return PK.createCounterPK(getTypeCode(type));
    }
}
