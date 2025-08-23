package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.Utilities;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = false)
public abstract class Feature<T> implements Cloneable, Serializable
{
    private final Map<Language, List<FeatureValue<T>>> featureValues;
    private final FeatureContainer parent;
    private boolean localized;


    public static <X> TypedFeature<X> loadTyped(Product product, ClassAttributeAssignment assignment)
    {
        return FeatureContainer.loadTyped(product, new ClassAttributeAssignment[] {assignment}).getFeature(assignment);
    }


    public static <X> TypedFeature<X> loadTyped(Product product, ClassificationAttribute attr) throws JaloInvalidParameterException
    {
        Set<ClassificationClass> classes = FeatureContainer.resolveClasses(product);
        if(classes.isEmpty())
        {
            throw new JaloInvalidParameterException("product " + product + " has no classification class", 0);
        }
        ClassAttributeAssignment asgn = null;
        for(ClassificationClass cl : classes)
        {
            try
            {
                ClassAttributeAssignment classAttributeAssignment = cl.getAttributeAssignment(attr);
                if(asgn != null && !asgn.equals(classAttributeAssignment))
                {
                    throw new JaloInvalidParameterException("ambigous attribute " + attr + " for product " + product + " - found at least two assignments " + asgn + " and " + classAttributeAssignment, 0);
                }
                asgn = classAttributeAssignment;
            }
            catch(JaloItemNotFoundException jaloItemNotFoundException)
            {
            }
        }
        if(asgn == null)
        {
            throw new JaloInvalidParameterException("unknown attribute " + attr + " for product " + product + " - no class found holding this attribute (checked " + classes + ")", 0);
        }
        return loadTyped(product, asgn);
    }


    protected Feature(FeatureContainer parent, boolean localized)
    {
        this.parent = parent;
        this.featureValues = new HashMap<>();
        this.localized = localized;
    }


    protected Feature(Feature<T> src) throws CloneNotSupportedException
    {
        this.parent = src.parent;
        this.featureValues = new HashMap<>();
        this.localized = src.localized;
        for(Map.Entry<Language, List<FeatureValue<T>>> entry : src.featureValues.entrySet())
        {
            List<FeatureValue<T>> srcList = entry.getValue();
            if(srcList != null)
            {
                List<FeatureValue> copyList = new ArrayList();
                for(FeatureValue<T> fv : srcList)
                {
                    copyList.add((fv != null) ? fv.clone() : null);
                }
                this.featureValues.put(entry.getKey(), copyList);
                continue;
            }
            this.featureValues.put(entry.getKey(), null);
        }
    }


    public String toString()
    {
        return "Feature(" + getName() + ")";
    }


    public FeatureContainer getParent()
    {
        return this.parent;
    }


    public void setLocalized(boolean isLocalized)
    {
        if(isLocalized != this.localized)
        {
            this.localized = isLocalized;
            if(!isLocalized)
            {
                List<FeatureValue<T>> unified = new ArrayList<>();
                for(Map.Entry<Language, List<FeatureValue<T>>> entry : this.featureValues.entrySet())
                {
                    unified.addAll(entry.getValue());
                }
                this.featureValues.clear();
                this.featureValues.put(null, unified);
            }
        }
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public boolean isEmpty()
    {
        return isEmpty(JaloSession.getCurrentSession().getSessionContext());
    }


    public boolean isEmpty(SessionContext ctx)
    {
        return getValues(ctx).isEmpty();
    }


    public boolean isEmptyIgnoringLanguage()
    {
        if(this.featureValues.isEmpty())
        {
            return true;
        }
        for(Map.Entry<Language, List<FeatureValue<T>>> entry : this.featureValues.entrySet())
        {
            if(entry.getValue() != null && !((List)entry.getValue()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }


    public final int hashCode()
    {
        return getUniqueKey().hashCode();
    }


    public final boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        return getUniqueKey().equals(((Feature)obj).getUniqueKey());
    }


    public FeatureValue<T> createValue(T value)
    {
        return createValue(JaloSession.getCurrentSession().getSessionContext(), value);
    }


    public FeatureValue<T> createValue(SessionContext ctx, T value)
    {
        return createValue(ctx, -1, value);
    }


    public List<FeatureValue<T>> createValues(Collection<T> values)
    {
        return createValues(JaloSession.getCurrentSession().getSessionContext(), values);
    }


    public List<FeatureValue<T>> createValues(SessionContext ctx, Collection<T> values)
    {
        if(values == null || values.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<FeatureValue<T>> ret = new ArrayList<>(values.size());
        for(T t : values)
        {
            ret.add(createValue(ctx, -1, t));
        }
        return ret;
    }


    public List<FeatureValue<T>> createValues(T... values)
    {
        return createValues(Arrays.asList(values));
    }


    public List<FeatureValue<T>> createValues(SessionContext ctx, T... values)
    {
        return createValues(ctx, Arrays.asList(values));
    }


    public FeatureValue<T> createValue(int index, T value)
    {
        return createValue(JaloSession.getCurrentSession().getSessionContext(), index, value);
    }


    public List<FeatureValue<T>> setValues(T... values)
    {
        return setValues(JaloSession.getCurrentSession().getSessionContext(), values);
    }


    public List<FeatureValue<T>> setValues(SessionContext ctx, T... values)
    {
        return setValues(ctx, Arrays.asList(values));
    }


    public List<FeatureValue<T>> setValues(List<T> values)
    {
        return setValues(JaloSession.getCurrentSession().getSessionContext(), values);
    }


    public List<FeatureValue<T>> setValues(SessionContext ctx, List<T> values)
    {
        clear(ctx);
        return createValues(ctx, values);
    }


    public FeatureValue<T> setValue(T value)
    {
        return setValue(JaloSession.getCurrentSession().getSessionContext(), value);
    }


    public FeatureValue<T> setValue(SessionContext ctx, T value)
    {
        clear(ctx);
        return createValue(ctx, value);
    }


    public void clearAll()
    {
        this.featureValues.clear();
    }


    public void clear()
    {
        clear(JaloSession.getCurrentSession().getSessionContext());
    }


    public void clear(SessionContext ctx)
    {
        getLanguageValues(ctx, extractLanguage(ctx), false).clear();
    }


    public FeatureValue<T> getValue(int index) throws IndexOutOfBoundsException
    {
        return getValue(JaloSession.getCurrentSession().getSessionContext(), index);
    }


    public FeatureValue<T> getValue(SessionContext ctx, int index) throws IndexOutOfBoundsException
    {
        return getLanguageValues(ctx, extractLanguage(ctx), false).get(index);
    }


    protected boolean isFallbackEnabled(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("enable.language.fallback")));
    }


    public void moveValue(int index, FeatureValue<T> featureValue) throws IndexOutOfBoundsException
    {
        moveValue(JaloSession.getCurrentSession().getSessionContext(), index, featureValue);
    }


    public void moveValue(SessionContext ctx, int index, FeatureValue<T> featureValue) throws IndexOutOfBoundsException, JaloInvalidParameterException
    {
        assureOwnValue(featureValue);
        List<FeatureValue<T>> valueList = getLanguageValues(ctx, extractLanguage(ctx), false);
        int oldPos = valueList.indexOf(featureValue);
        if(oldPos >= 0 && index != oldPos)
        {
            valueList.add(index, featureValue);
            valueList.remove(oldPos);
        }
    }


    public boolean removeValue(FeatureValue<T> featureValue)
    {
        return remove(JaloSession.getCurrentSession().getSessionContext(), featureValue);
    }


    public boolean remove(SessionContext ctx, FeatureValue<T> featureValue) throws IndexOutOfBoundsException
    {
        return getLanguageValues(ctx, extractLanguage(ctx), false).remove(featureValue);
    }


    public FeatureValue<T> remove(int index) throws IndexOutOfBoundsException
    {
        return remove(JaloSession.getCurrentSession().getSessionContext(), index);
    }


    public FeatureValue<T> remove(SessionContext ctx, int index) throws IndexOutOfBoundsException
    {
        return getLanguageValues(ctx, extractLanguage(ctx), false).remove(index);
    }


    public int size()
    {
        return size(JaloSession.getCurrentSession().getSessionContext());
    }


    public int size(SessionContext ctx)
    {
        return getLanguageValues(ctx, extractLanguage(ctx), false).size();
    }


    public List<FeatureValue<T>> getValues()
    {
        return getValues(JaloSession.getCurrentSession().getSessionContext());
    }


    public List<FeatureValue<T>> getValues(SessionContext ctx)
    {
        List<FeatureValue<T>> ret = getLanguageValues(ctx, extractLanguage(ctx), false);
        return (ret != null && !ret.isEmpty()) ? new ArrayList<>(ret) : Collections.EMPTY_LIST;
    }


    public List<T> getValuesDirect()
    {
        return getValuesDirect(JaloSession.getCurrentSession().getSessionContext());
    }


    public List<T> getValuesDirect(SessionContext ctx)
    {
        if(isEmpty(ctx))
        {
            return Collections.EMPTY_LIST;
        }
        List<T> ret = new ArrayList<>();
        for(FeatureValue<T> fv : getValues(ctx))
        {
            ret.add((T)fv.getValue());
        }
        return ret;
    }


    public String getAllDescriptions()
    {
        return getAllDescriptions(JaloSession.getCurrentSession().getSessionContext());
    }


    public String getAllDescriptions(SessionContext ctx)
    {
        if(isEmpty())
        {
            return null;
        }
        Set<String> descriptions = new LinkedHashSet<>();
        for(FeatureValue<T> fv : getValues(ctx))
        {
            if(fv.getDescription() != null)
            {
                descriptions.add(fv.getDescription());
            }
        }
        if(descriptions.isEmpty())
        {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : descriptions)
        {
            if(stringBuilder.length() > 0)
            {
                stringBuilder.append(",");
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }


    public String getName()
    {
        return getName(JaloSession.getCurrentSession().getSessionContext());
    }


    public NumberFormat getNumberFormat(SessionContext ctx)
    {
        Locale locale = (ctx != null && ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLanguage().getLocale();
        return Utilities.getIntegerInstance(locale);
    }


    public DateFormat getDateFormat(SessionContext ctx)
    {
        Locale locale = (ctx != null && ctx.getLanguage() != null) ? ctx.getLanguage().getLocale() : JaloSession.getCurrentSession().getSessionContext().getLanguage().getLocale();
        return Utilities.getDateTimeInstance(2, 2, locale);
    }


    public String getValuesFormatted()
    {
        return getValuesFormatted(JaloSession.getCurrentSession().getSessionContext());
    }


    public String getValuesFormatted(SessionContext ctx)
    {
        List<FeatureValue<T>> values = getValues(ctx);
        if(values == null || values.isEmpty())
        {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(FeatureValue<T> v : values)
        {
            if(stringBuilder.length() > 0)
            {
                stringBuilder.append(", ");
            }
            stringBuilder.append(v.getValueFormatted(ctx));
        }
        return stringBuilder.toString();
    }


    protected FeatureValue<T> loadValue(SessionContext ctx, ProductFeature datbaseItem)
    {
        if(datbaseItem == null)
        {
            throw new NullPointerException("value was null");
        }
        FeatureValue<T> ret = new FeatureValue(this, datbaseItem);
        add(ctx, -1, ret);
        return ret;
    }


    public List<FeatureValue<T>> getLanguageValues(SessionContext ctx, Language language, boolean create)
    {
        List<FeatureValue<T>> ret = this.featureValues.get(language);
        if(ret != null)
        {
            return ret;
        }
        if(!create && language != null && isFallbackEnabled(ctx) && !language.getFallbackLanguages().isEmpty())
        {
            for(Language fallback : language.getFallbackLanguages())
            {
                ret = this.featureValues.get(fallback);
                if(ret != null)
                {
                    return ret;
                }
            }
        }
        if(!create)
        {
            return Collections.EMPTY_LIST;
        }
        this.featureValues.put(language, ret = new ArrayList<>());
        return ret;
    }


    public Language extractLanguage(SessionContext ctx)
    {
        if(isLocalized())
        {
            if(ctx != null && ctx.getLanguage() != null)
            {
                return ctx.getLanguage();
            }
            throw new JaloInvalidParameterException("no session language provided for localized feature value " +
                            getUniqueKey(), 0);
        }
        return null;
    }


    protected Collection<PK> writeToDatabase(int featurePosition)
    {
        Collection<PK> ret = new ArrayList<>();
        for(Map.Entry<Language, List<FeatureValue<T>>> entry : this.featureValues.entrySet())
        {
            Language language = entry.getKey();
            int index = 0;
            for(FeatureValue<T> v : entry.getValue())
            {
                ret.add(v.writeToDatabase(language, featurePosition, index++));
            }
        }
        return ret;
    }


    protected void assureOwnValue(FeatureValue<T> value) throws JaloInvalidParameterException
    {
        if(value != null && equals(value.getParent()))
        {
            throw new JaloInvalidParameterException("value " + value + " doesnt belong to feature " + this, 0);
        }
    }


    protected void add(SessionContext ctx, int index, FeatureValue<T> featureValue) throws IndexOutOfBoundsException
    {
        List<FeatureValue<T>> list = getLanguageValues(ctx, extractLanguage(ctx), true);
        if(index == -1 || index > list.size())
        {
            list.add(featureValue);
        }
        else
        {
            list.add(index, featureValue);
        }
    }


    public abstract Feature clone() throws CloneNotSupportedException;


    public abstract FeatureValue<T> createValue(SessionContext paramSessionContext, int paramInt, T paramT);


    public abstract String getName(SessionContext paramSessionContext);


    protected abstract String getUniqueKey();
}
