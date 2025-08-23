package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ObjectValueContainer implements Cloneable
{
    private Object object;
    private final ObjectType type;
    private final Set<ObjectValueHolder> valueHolders;
    private final Map<PropertyDescriptor, Set<String>> propertyMap;
    private final ObjectValueLazyLoader lazyLoader;
    private boolean allLoaded = false;
    private boolean validated;
    private Set<String> ignoredValidationConstraints = new HashSet<>();


    public ObjectValueContainer(ObjectType type, Object object)
    {
        this(type, object, null, null, null);
    }


    public ObjectValueContainer(ObjectType type, Object object, Set<PropertyDescriptor> descriptorsToLoad, Set<String> languagesToLoad, ObjectValueLazyLoader lazyLoader)
    {
        this.type = type;
        this.object = object;
        this.valueHolders = new LinkedHashSet<>();
        this.propertyMap = new HashMap<>();
        if(descriptorsToLoad != null)
        {
            for(PropertyDescriptor pd : descriptorsToLoad)
            {
                if(pd.isLocalized())
                {
                    if(languagesToLoad != null)
                    {
                        for(String lang : languagesToLoad)
                        {
                            registerProperty(pd, lang);
                        }
                    }
                    continue;
                }
                registerProperty(pd, null);
            }
        }
        this.lazyLoader = lazyLoader;
    }


    public void addToIgnoredValidationConstraints(String pk)
    {
        this.ignoredValidationConstraints.add(pk);
    }


    public ObjectValueHolder addValue(PropertyDescriptor propertyDescriptor, String languageIso, Object originalValue)
    {
        ObjectValueHolder holder = new ObjectValueHolder(this, propertyDescriptor, languageIso);
        if(this.valueHolders.contains(holder))
        {
            throw new IllegalStateException("value container already hold value for " + propertyDescriptor + " and language " + languageIso);
        }
        holder.load(originalValue);
        this.valueHolders.add(holder);
        registerProperty(propertyDescriptor, languageIso);
        return holder;
    }


    public Set<ObjectValueHolder> getModifiedValues()
    {
        Set<ObjectValueHolder> modifiedHolders = new HashSet<>();
        Set<ObjectValueHolder> localValueHolders = new HashSet<>(this.valueHolders);
        for(ObjectValueHolder holder : localValueHolders)
        {
            if(holder.isModified())
            {
                PropertyDescriptor propertyDescriptor = holder.getPropertyDescriptor();
                Set<String> languages = this.propertyMap.get(propertyDescriptor);
                if(propertyDescriptor.isLocalized() && languages != null && !languages.isEmpty())
                {
                    for(String lang : languages)
                    {
                        getValue(propertyDescriptor, lang);
                    }
                }
                else
                {
                    getValue(propertyDescriptor, null);
                }
                modifiedHolders.add(holder);
            }
        }
        localValueHolders.clear();
        localValueHolders = null;
        return modifiedHolders;
    }


    public Set<ObjectValueHolder> getAllValues()
    {
        if(!this.allLoaded && this.lazyLoader != null)
        {
            for(Map.Entry<PropertyDescriptor, Set<String>> entry : this.propertyMap.entrySet())
            {
                PropertyDescriptor propertyDescriptor = entry.getKey();
                Set<String> langIsos = entry.getValue();
                if(langIsos != null && !langIsos.isEmpty())
                {
                    for(String lang : langIsos)
                    {
                        getValue(propertyDescriptor, lang);
                    }
                    continue;
                }
                getValue(propertyDescriptor, null);
            }
        }
        this.allLoaded = true;
        return Collections.unmodifiableSet(this.valueHolders);
    }


    public Set<ObjectValueHolder> getConflicts()
    {
        if(this.valueHolders.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<ObjectValueHolder> ret = new LinkedHashSet<>();
        for(ObjectValueHolder holder : this.valueHolders)
        {
            if(holder.hasConflict())
            {
                ret.add(holder);
            }
        }
        return Collections.unmodifiableSet(ret);
    }


    public Set<String> getIgnoredValidationConstraints()
    {
        return this.ignoredValidationConstraints;
    }


    public Object getObject()
    {
        return this.object;
    }


    public Set<PropertyDescriptor> getPropertyDescriptors()
    {
        return this.propertyMap.keySet();
    }


    public ObjectType getType()
    {
        return this.type;
    }


    public ObjectValueHolder getValue(PropertyDescriptor propertyDescriptor, String languageIso)
    {
        if(!hasProperty(propertyDescriptor, languageIso))
        {
            throw new IllegalArgumentException("cannot find value holder for " + propertyDescriptor);
        }
        ObjectValueHolder holder = getValueInternal(propertyDescriptor, languageIso);
        if(holder == null && this.lazyLoader != null)
        {
            this.lazyLoader.loadValue(this, propertyDescriptor, languageIso);
            holder = getValueInternal(propertyDescriptor, languageIso);
        }
        if(holder != null)
        {
            return holder;
        }
        throw new IllegalArgumentException("cannot find value holder for " + propertyDescriptor);
    }


    public boolean hasConflicts()
    {
        if(this.valueHolders.isEmpty())
        {
            return false;
        }
        for(ObjectValueHolder holder : this.valueHolders)
        {
            if(holder.hasConflict())
            {
                return true;
            }
        }
        return false;
    }


    public boolean hasProperty(PropertyDescriptor propertyDescriptor)
    {
        return this.propertyMap.containsKey(propertyDescriptor);
    }


    public boolean hasProperty(PropertyDescriptor propertyDescriptor, String language)
    {
        if(language == null)
        {
            return this.propertyMap.containsKey(propertyDescriptor);
        }
        Set<String> langIsos = this.propertyMap.get(propertyDescriptor);
        return (langIsos != null && langIsos.contains(language));
    }


    public boolean isModified()
    {
        if(this.valueHolders.isEmpty())
        {
            return false;
        }
        for(ObjectValueHolder holder : this.valueHolders)
        {
            if(holder.isModified())
            {
                return true;
            }
        }
        return false;
    }


    public boolean isValidated()
    {
        return this.validated;
    }


    public void removeFromIgnoredValidationConstraints(String pk)
    {
        this.ignoredValidationConstraints.remove(pk);
    }


    public boolean removeValue(ObjectValueHolder holder)
    {
        if(this.valueHolders.contains(holder))
        {
            unregisterProperty(holder.getPropertyDescriptor(), holder.getLanguageIso());
            return this.valueHolders.remove(holder);
        }
        return false;
    }


    public void resetIgnoredValidationConstraints()
    {
        this.ignoredValidationConstraints = new HashSet<>();
    }


    public void setObject(Object object)
    {
        this.object = object;
    }


    public void setValidated(boolean validated)
    {
        this.validated = validated;
    }


    public void stored()
    {
        for(ObjectValueHolder holder : this.valueHolders)
        {
            holder.stored();
        }
    }


    public String toString()
    {
        return "[" + getObject() + "::" + getType() + "->" + getPropertyDescriptors().size() + (isModified() ? "*" : "") + (
                        hasConflicts() ? "!" : "") + "]";
    }


    public void unmodifyAll()
    {
        for(ObjectValueHolder holder : this.valueHolders)
        {
            holder.unmodify();
        }
    }


    private ObjectValueHolder getValueInternal(PropertyDescriptor propertyDescriptor, String languageIso)
    {
        for(ObjectValueHolder holder : this.valueHolders)
        {
            if(propertyDescriptor.equals(holder.getPropertyDescriptor()) && (languageIso == holder
                            .getLanguageIso() || (languageIso != null && languageIso.equalsIgnoreCase(holder
                            .getLanguageIso()))))
            {
                return holder;
            }
        }
        return null;
    }


    private void registerProperty(PropertyDescriptor propertyDescriptor, String languageIso)
    {
        Set<String> langIsos = this.propertyMap.get(propertyDescriptor);
        if(langIsos == null)
        {
            langIsos = new HashSet<>();
            this.propertyMap.put(propertyDescriptor, langIsos);
        }
        if(languageIso != null)
        {
            langIsos.add(languageIso);
        }
    }


    private void unregisterProperty(PropertyDescriptor propertyDescriptor, String languageIso)
    {
        Set<String> langIsos = this.propertyMap.get(propertyDescriptor);
        if(langIsos != null)
        {
            if(languageIso != null)
            {
                langIsos.remove(languageIso);
            }
            if(langIsos.isEmpty())
            {
                this.propertyMap.remove(propertyDescriptor);
            }
        }
    }


    public ObjectValueContainer clone()
    {
        ObjectValueContainer objectValueContainer = new ObjectValueContainer(this.type, this.object, null, null, this.lazyLoader);
        objectValueContainer.allLoaded = this.allLoaded;
        objectValueContainer.ignoredValidationConstraints = this.ignoredValidationConstraints;
        objectValueContainer.validated = this.validated;
        for(Map.Entry<PropertyDescriptor, Set<String>> propEntry : this.propertyMap.entrySet())
        {
            Set<String> value = new HashSet<>();
            if(propEntry.getValue() != null)
            {
                value.addAll(propEntry.getValue());
            }
            objectValueContainer.propertyMap.put(propEntry.getKey(), value);
        }
        for(ObjectValueHolder valueHolder : this.valueHolders)
        {
            objectValueContainer.valueHolders.add(valueHolder.clone());
        }
        return objectValueContainer;
    }


    public void resetModifiedFlag()
    {
        for(ObjectValueHolder holder : this.valueHolders)
        {
            holder.setModified(false);
        }
    }
}
