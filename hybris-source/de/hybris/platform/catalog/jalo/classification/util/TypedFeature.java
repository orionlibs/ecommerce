package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class TypedFeature<T> extends Feature<T>
{
    private static final Logger LOG = Logger.getLogger(TypedFeature.class);
    private final String uniqueKey;
    private final ClassAttributeAssignment assignment;
    private final ClassificationClass cclass;
    private final ClassificationAttribute cattr;
    private final ClassificationAttributeUnit unit;
    private final boolean isRange;
    private final FeatureType featureType;
    private Collection<ClassificationAttributeValue> selectable;
    public static final String CONVERT_TO_FEATUREUNIT = "convert.to.featureunit";


    protected TypedFeature(FeatureContainer parent, ClassAttributeAssignment assignment)
    {
        super(parent, assignment.isLocalizedAsPrimitive());
        this.assignment = assignment;
        this.cclass = assignment.getClassificationClass();
        this.cattr = assignment.getClassificationAttribute();
        this.unit = assignment.getUnit();
        this.isRange = assignment.isRangeAsPrimitive();
        this.featureType = FeatureType.toEnum(assignment.getAttributeType());
        this.uniqueKey = FeatureContainer.createUniqueKey(assignment);
    }


    protected TypedFeature(TypedFeature<T> src) throws CloneNotSupportedException
    {
        super(src);
        this.assignment = src.assignment;
        this.uniqueKey = src.uniqueKey;
        this.featureType = src.featureType;
        this.cclass = src.cclass;
        this.cattr = src.cattr;
        this.unit = src.unit;
        this.isRange = src.isRange;
    }


    public TypedFeature<T> clone() throws CloneNotSupportedException
    {
        return new TypedFeature(this);
    }


    public String getName(SessionContext ctx)
    {
        return getClassificationAttribute().getName(ctx);
    }


    public String getCode()
    {
        return getClassificationAttribute().getCode();
    }


    public ClassAttributeAssignment getClassAttributeAssignment()
    {
        return this.assignment;
    }


    public ClassificationAttribute getClassificationAttribute()
    {
        return this.cattr;
    }


    public ClassificationClass getClassificationClass()
    {
        return this.cclass;
    }


    public ClassificationAttributeUnit getUnit()
    {
        return this.unit;
    }


    public ClassificationAttributeValue getSelectableValue(String code)
    {
        Collection<ClassificationAttributeValue> selectable = getSelectableValues();
        if(selectable != null && !selectable.isEmpty())
        {
            for(ClassificationAttributeValue val : selectable)
            {
                if(code.equalsIgnoreCase(val.getCode()))
                {
                    return val;
                }
            }
        }
        return null;
    }


    public Collection<ClassificationAttributeValue> getSelectableValues()
    {
        if(this.selectable == null)
        {
            this.selectable = this.cclass.getAttributeValues(getClassAttributeAssignment());
            if(this.selectable == null)
            {
                this.selectable = Collections.EMPTY_LIST;
            }
        }
        return this.selectable;
    }


    public FeatureValue<T> setSelectableValue(String code)
    {
        return setSelectableValue(JaloSession.getCurrentSession().getSessionContext(), code);
    }


    public FeatureValue<T> setSelectableValue(SessionContext ctx, String code)
    {
        ClassificationAttributeValue val = getSelectableValue(code);
        if(val == null)
        {
            throw new JaloInvalidParameterException("no value '" + code + "' within selectable values " + getSelectableValues(), 0);
        }
        return setValue(ctx, val);
    }


    public List<FeatureValue<T>> setSelectableValues(String... codes)
    {
        return setSelectableValues(JaloSession.getCurrentSession().getSessionContext(), Arrays.asList(codes));
    }


    public List<FeatureValue<T>> setSelectableValues(List<String> codes)
    {
        return setSelectableValues(JaloSession.getCurrentSession().getSessionContext(), codes);
    }


    public List<FeatureValue<T>> setSelectableValues(SessionContext ctx, String... codes)
    {
        return setSelectableValues(ctx, Arrays.asList(codes));
    }


    public List<FeatureValue<T>> setSelectableValues(SessionContext ctx, List<String> codes)
    {
        if(codes != null && !codes.isEmpty())
        {
            List<T> valueList = new ArrayList<>(codes.size());
            for(String code : codes)
            {
                ClassificationAttributeValue val = getSelectableValue(code);
                if(val == null)
                {
                    throw new JaloInvalidParameterException("no value '" + code + "' within selectable values " +
                                    getSelectableValues(), 0);
                }
                valueList.add((T)val);
            }
            return setValues(ctx, valueList);
        }
        throw new JaloInvalidParameterException("selectable value code list was null or empty", 0);
    }


    public FeatureType getFeatureType()
    {
        return this.featureType;
    }


    protected String getUniqueKey()
    {
        return this.uniqueKey;
    }


    public FeatureValue<T> createValue(SessionContext ctx, int index, Object value)
    {
        return createValue(ctx, index, value, true);
    }


    public FeatureValue<T> createValue(SessionContext ctx, int index, Object value, boolean typeSafe)
    {
        FeatureValue<T> ret = null;
        try
        {
            switch(null.$SwitchMap$de$hybris$platform$catalog$jalo$classification$util$TypedFeature$FeatureType[getFeatureType().ordinal()])
            {
                case 1:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
                case 2:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
                case 3:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
                case 4:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
                case 5:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
                case 6:
                    ret = new FeatureValue(this, value);
                    ret.setUnit(this.unit);
                    add(ctx, index, ret);
                    return ret;
            }
            throw new UnsupportedOperationException("ClassificationFeatureValues.createValue only supports value types STRING, NUMBER and BOOLEAN - got " + getFeatureType() + " instead");
        }
        catch(ClassCastException e)
        {
            if(typeSafe)
            {
                throw new IllegalArgumentException("Given value " + value.toString() + " is not of type " + getFeatureType(), e);
            }
            LOG.warn("Value " + value.toString() + " is not of type " + getFeatureType() + ", will use FeatureValue of type String");
            ret = new FeatureValue(this, value);
        }
        ret.setUnit(this.unit);
        add(ctx, index, ret);
        return ret;
    }


    public String getValuesFormatted(SessionContext ctx)
    {
        if(this.isRange)
        {
            List<FeatureValue<T>> values = getValues(ctx);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((values.size() >= 1) ? ((FeatureValue)values.get(0)).getValueFormatted(ctx) : getParent().getEmptyValueString());
            stringBuilder.append(" - ");
            stringBuilder.append((values.size() >= 2) ? ((FeatureValue)values.get(1)).getValueFormatted(ctx) : getParent().getEmptyValueString());
            return stringBuilder.toString();
        }
        return super.getValuesFormatted(ctx);
    }


    public List<T> getValuesDirect(SessionContext ctx)
    {
        if(isEmpty(ctx))
        {
            return Collections.EMPTY_LIST;
        }
        boolean dontConvert = (ctx != null && Boolean.FALSE.equals(ctx.getAttribute("convert.to.featureunit")));
        List<T> ret = new ArrayList<>();
        for(FeatureValue<T> fv : (Iterable<FeatureValue<T>>)getValues(ctx))
        {
            ret.add((!dontConvert && getUnit() != null) ? (T)fv.getValue(getUnit()) : (T)fv.getValue());
        }
        return ret;
    }


    public NumberFormat getNumberFormat(SessionContext ctx)
    {
        NumberFormat numberFormat = getClassAttributeAssignment().getNumberFormat(ctx);
        return (numberFormat != null) ? numberFormat : super.getNumberFormat(ctx);
    }


    public DateFormat getDateFormat(SessionContext ctx)
    {
        DateFormat dateFormat = getClassAttributeAssignment().getDateFormat(ctx);
        return (dateFormat != null) ? dateFormat : super.getDateFormat(ctx);
    }


    protected void assureSelectableValue(ClassificationAttributeValue value) throws JaloInvalidParameterException
    {
        if(value != null && !getSelectableValues().contains(value))
        {
            throw new JaloInvalidParameterException("selected value " + value + " is not permitted for assignment " +
                            getClassAttributeAssignment() + " - expected one of " + getSelectableValues(), 0);
        }
    }
}
