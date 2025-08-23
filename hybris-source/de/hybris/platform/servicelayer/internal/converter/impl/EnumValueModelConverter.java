package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.ReadParams;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EnumValueModelConverter implements ModelConverter
{
    private final UUID uuid = UUID.randomUUID();
    private final Class enumClass;
    private final boolean javaEnumMode;
    private Method valueOfMethod;
    private final SourceTransformer sourceTransformer;


    public EnumValueModelConverter(Class enumClass, SourceTransformer sourceTransformer)
    {
        ServicesUtil.validateParameterNotNull(sourceTransformer, "sourceTransformer was null");
        this.enumClass = enumClass;
        this.javaEnumMode = enumClass.isEnum();
        this.sourceTransformer = sourceTransformer;
    }


    public Class getEnumClass()
    {
        return this.enumClass;
    }


    protected Method getValueOfMethod()
    {
        if(this.valueOfMethod == null)
        {
            try
            {
                this.valueOfMethod = getEnumClass().getMethod("valueOf", new Class[] {String.class});
            }
            catch(SecurityException e)
            {
                throw new IllegalStateException("cannot find valueOf() in " + getEnumClass() + " due to " + e.getMessage());
            }
            catch(NoSuchMethodException e)
            {
                throw new IllegalStateException("cannot find valueOf() in " + getEnumClass() + " due to " + e.getMessage());
            }
        }
        return this.valueOfMethod;
    }


    protected boolean exists(HybrisEnumValue enumValue)
    {
        try
        {
            return (EnumerationManager.getInstance().getEnumerationValue(enumValue.getType(), enumValue.getCode()) != null);
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
    }


    public boolean isJavaEnum()
    {
        return this.javaEnumMode;
    }


    public Object create(String type)
    {
        if(isJavaEnum())
        {
            throw new UnsupportedOperationException("cannot create a new java enum instance");
        }
        try
        {
            return getEnumClass().newInstance();
        }
        catch(InstantiationException e)
        {
            throw new SystemException(e);
        }
        catch(IllegalAccessException e)
        {
            throw new SystemException(e);
        }
    }


    public String getType(Object model)
    {
        return ((HybrisEnumValue)model).getType();
    }


    public PersistenceObject getPersistenceSource(Object model)
    {
        return this.sourceTransformer.transformSource(getSource(model));
    }


    public Object getSource(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "model was null");
        if(!(model instanceof HybrisEnumValue))
        {
            throw new IllegalArgumentException("expected HybrisEnumValue but got " + model.getClass().getSimpleName());
        }
        HybrisEnumValue enumValue = (HybrisEnumValue)model;
        try
        {
            return EnumerationManager.getInstance().getEnumerationValue(enumValue.getType(), enumValue.getCode());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalStateException("missing persistent item for enum value " + enumValue);
        }
    }


    public void init(ConverterRegistry registry)
    {
    }


    public Object load(Object source)
    {
        if(source == null)
        {
            return null;
        }
        PersistenceObject persistenceObject = this.sourceTransformer.transformSource(source);
        try
        {
            ReadParams params = ReadParams.builderForSingleNotLocalizedQualifier("code").build();
            String codeRawValue = (String)persistenceObject.readRawValue(params);
            String code = isJavaEnum() ? codeRawValue.toUpperCase(LocaleHelper.getPersistenceLocale()) : codeRawValue;
            HybrisEnumValue ret = (HybrisEnumValue)getValueOfMethod().invoke(null, new Object[] {code});
            if(ret == null)
            {
                throw new IllegalArgumentException("cannot find enum constant for value " + source);
            }
            return ret;
        }
        catch(Exception e)
        {
            throw new ModelLoadingException(e.getMessage(), e);
        }
    }


    public void reload(Object model)
    {
    }


    public void remove(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "model was null");
        if(isJavaEnum())
        {
            throw new UnsupportedOperationException("cannot remove java enum");
        }
        if(exists((HybrisEnumValue)model))
        {
            EnumerationValue src = (EnumerationValue)getSource(model);
            try
            {
                src.remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new ModelRemovalException(e.getMessage(), e);
            }
        }
    }


    public void save(Object model, Collection<String> excluded)
    {
        ServicesUtil.validateParameterNotNull(model, "model was null");
        HybrisEnumValue val = (HybrisEnumValue)model;
        if(!isJavaEnum() && !exists(val))
        {
            try
            {
                EnumerationManager.getInstance().createEnumerationValue(val.getType(), val.getCode());
            }
            catch(JaloInvalidParameterException e)
            {
                throw new ModelSavingException(e.getMessage(), e);
            }
            catch(ConsistencyCheckException e)
            {
                throw new ModelSavingException(e.getMessage(), e);
            }
        }
    }


    public boolean isModified(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "model was null");
        HybrisEnumValue val = (HybrisEnumValue)model;
        return (!isJavaEnum() && !exists(val));
    }


    public boolean isModified(Object model, String attribute)
    {
        return isModified(model);
    }


    public boolean isModified(Object model, String attribute, Locale loc)
    {
        return isModified(model);
    }


    public boolean exists(Object model)
    {
        if(isJavaEnum())
        {
            return true;
        }
        HybrisEnumValue enumValue = (HybrisEnumValue)model;
        try
        {
            return (EnumerationManager.getInstance().getEnumerationValue(enumValue.getType(), enumValue.getCode()) != null);
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
    }


    public boolean isRemoved(Object model)
    {
        return false;
    }


    public boolean isNew(Object model)
    {
        if(isJavaEnum())
        {
            return false;
        }
        HybrisEnumValue enumValue = (HybrisEnumValue)model;
        try
        {
            return (EnumerationManager.getInstance().getEnumerationValue(enumValue.getType(), enumValue.getCode()) == null);
        }
        catch(JaloItemNotFoundException e)
        {
            return true;
        }
    }


    public boolean isUpToDate(Object model)
    {
        if(isJavaEnum())
        {
            return true;
        }
        return (!isNew(model) && !isModified(model));
    }


    public Object getAttributeValue(Object model, String attributeQualifier)
    {
        if("itemtype".equalsIgnoreCase(attributeQualifier))
        {
            return ((HybrisEnumValue)model).getType();
        }
        if("code".equalsIgnoreCase(attributeQualifier))
        {
            return ((HybrisEnumValue)model).getCode();
        }
        throw new IllegalArgumentException("cannot read attribute " + attributeQualifier + " from enum " + model + " - use EnumerationValueModel instead");
    }


    public Object getLocalizedAttributeValue(Object model, String attributeQualifier, Locale locale)
    {
        return getAttributeValue(model, attributeQualifier);
    }


    public void setAttributeValue(Object model, String attributeQualifier, Object value)
    {
        throw new UnsupportedOperationException("cannot change attribute " + attributeQualifier + " on enum - use EnumerationValueModel instead");
    }


    public void beforeAttach(Object model, ModelContext ctx)
    {
    }


    public void afterDetach(Object model, ModelContext ctx)
    {
    }


    public Set<String> getWritablePartOfAttributes(TypeService typeService)
    {
        return Collections.emptySet();
    }


    public Set<String> getPartOfAttributes(TypeService typeService)
    {
        return Collections.emptySet();
    }


    public Map<String, Set<Locale>> getDirtyAttributes(Object model)
    {
        if(isNew(model))
        {
            Map<String, Set<Locale>> retmap = new HashMap<>(1);
            retmap.put("code", null);
            return retmap;
        }
        return Collections.emptyMap();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof EnumValueModelConverter))
        {
            return false;
        }
        EnumValueModelConverter that = (EnumValueModelConverter)o;
        return this.uuid.equals(that.uuid);
    }


    public int hashCode()
    {
        return this.uuid.hashCode();
    }
}
