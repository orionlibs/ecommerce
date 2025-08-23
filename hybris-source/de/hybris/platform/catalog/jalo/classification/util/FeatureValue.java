package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.localization.Localization;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Deprecated(since = "ages", forRemoval = false)
public class FeatureValue<V> implements Cloneable, Serializable
{
    private ClassificationAttributeUnit cUnit;
    private V value;
    private String description;
    private final Feature<V> parent;
    private ProductFeature databaseItem;


    public FeatureValue(UntypedFeature<V> parent, V value)
    {
        if(value == null)
        {
            throw new NullPointerException("value must not be null");
        }
        this.parent = (Feature<V>)parent;
        this.value = value;
    }


    public FeatureValue(TypedFeature<V> parent, V value) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            throw new NullPointerException("value must not be null");
        }
        this.parent = (Feature<V>)parent;
        if(value instanceof ClassificationAttributeValue)
        {
            parent.assureSelectableValue((ClassificationAttributeValue)value);
        }
        this.value = value;
    }


    protected FeatureValue(FeatureValue<V> src)
    {
        this.parent = src.parent;
        this.cUnit = src.cUnit;
        this.description = src.description;
        this.value = src.value;
        this.databaseItem = src.databaseItem;
    }


    public FeatureValue(Feature<V> parent, ProductFeature dbItem)
    {
        this.parent = parent;
        this.value = (V)dbItem.getValue();
        this.description = dbItem.getDescription();
        this.cUnit = dbItem.getUnit();
        this.databaseItem = dbItem;
    }


    public Feature<V> getParent()
    {
        return this.parent;
    }


    protected PK writeToDatabase(Language lang, int featurePosition, int valuePosition)
    {
        if(this.databaseItem != null && this.databaseItem.isAlive())
        {
            Item.ItemAttributeMap attributes = new Item.ItemAttributeMap();
            attributes.put("value", getValue());
            attributes.put("unit", getUnit());
            attributes.put("description", getDescription());
            attributes.put("valuePosition", Integer.valueOf(valuePosition));
            attributes.put("featurePosition", Integer.valueOf(featurePosition));
            try
            {
                this.databaseItem.setAllAttributes((Map)attributes);
            }
            catch(RuntimeException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new JaloSystemException(e);
            }
        }
        else
        {
            Map<String, Object> map = new HashMap<>();
            if(getParent() instanceof TypedFeature)
            {
                map.put("classificationAttributeAssignment", ((TypedFeature)
                                getParent()).getClassAttributeAssignment());
            }
            else
            {
                map.put("qualifier", getParent().getUniqueKey());
            }
            map.put("product", getParent().getParent().getProduct());
            map.put("language", lang);
            map.put("description", getDescription());
            map.put("unit", getUnit());
            map.put("value", getValue());
            map.put("valuePosition", Integer.valueOf(valuePosition));
            map.put("featurePosition", Integer.valueOf(featurePosition));
            this.databaseItem = CatalogManager.getInstance().createProductFeature(map);
        }
        return this.databaseItem.getPK();
    }


    protected ProductFeature getDatabaseItem()
    {
        return this.databaseItem;
    }


    public int hashCode()
    {
        int ret = this.value.hashCode();
        if(this.description != null)
        {
            ret = ret * 31 + this.description.hashCode();
        }
        if(this.cUnit != null)
        {
            ret = ret * 31 + this.cUnit.hashCode();
        }
        return ret;
    }


    public boolean equalsValue(FeatureValue<? extends Number> other, double maxDifference)
    {
        if(!(getValue() instanceof Number))
        {
            throw new JaloInvalidParameterException("incompatible features " + this + " vs " + other + " - can only compare number features", 0);
        }
        double number1 = ((Number)getValue()).doubleValue();
        double number2 = ((Number)other.getValue()).doubleValue();
        ClassificationAttributeUnit classificationAttributeUnit1 = getUnit();
        ClassificationAttributeUnit classificationAttributeUnit2 = other.getUnit();
        if(classificationAttributeUnit1 != null)
        {
            if(classificationAttributeUnit2 == null)
            {
                throw new JaloInvalidParameterException("incompatible features " + this + " vs " + other + " - one value is missing unit", 0);
            }
            String ut1 = classificationAttributeUnit1.getUnitType().toLowerCase();
            String ut2 = classificationAttributeUnit2.getUnitType().toLowerCase();
            if(ut1 != ut2 && (ut1 == null || !ut1.equals(ut2)))
            {
                throw new JaloInvalidParameterException("incompatible features " + this + " vs " + other + " - units " + classificationAttributeUnit1 + " and " + classificationAttributeUnit2 + " are not convertible", 0);
            }
            number2 = CoreAlgorithms.convert(classificationAttributeUnit2.getConversionFactorAsPrimitive(), classificationAttributeUnit1
                            .getConversionFactorAsPrimitive(), number2);
        }
        else if(classificationAttributeUnit2 != null)
        {
            throw new JaloInvalidParameterException("incompatible features " + this + " vs " + other + " - one value is missing unit", 0);
        }
        return (Math.abs(number2 - number1) <= maxDifference);
    }


    public boolean equals(Object obj)
    {
        return (obj != null && obj instanceof FeatureValue && this.value
                        .equals(((FeatureValue)obj).value) && (this.cUnit == ((FeatureValue)obj).cUnit || (this.cUnit != null && this.cUnit
                        .equals(((FeatureValue)obj).cUnit))) && (this.description == ((FeatureValue)obj).description || (this.description != null && this.description
                        .equals(((FeatureValue)obj).description))));
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder("FV[");
        stringBuilder.append(getValue());
        if(getUnit() != null)
        {
            stringBuilder.append("[").append(getUnit()).append("]");
        }
        if(getDescription() != null)
        {
            stringBuilder.append("(").append(getDescription()).append(")");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    public FeatureValue clone() throws CloneNotSupportedException
    {
        return new FeatureValue(this);
    }


    public ClassificationAttributeUnit getUnit()
    {
        return this.cUnit;
    }


    public void setUnit(ClassificationAttributeUnit unit)
    {
        this.cUnit = unit;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttributeUnit getClassificationAttributeUnit()
    {
        return getUnit();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setClassificationAttributeUnit(ClassificationAttributeUnit unit)
    {
        setUnit(unit);
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public V getValue()
    {
        return this.value;
    }


    public V getValue(ClassificationAttributeUnit otherUnit)
    {
        if(this.cUnit == null)
        {
            throw new JaloInvalidParameterException("value has no unit - cannot convert", 0);
        }
        if(!this.cUnit.equals(otherUnit) && (this.cUnit
                        .getUnitType() == null || !this.cUnit.getUnitType().equalsIgnoreCase(otherUnit.getUnitType())))
        {
            throw new JaloInvalidParameterException("unit " + this.cUnit + " is not convertible into " + otherUnit, 0);
        }
        if(!(this.value instanceof Number))
        {
            throw new JaloInvalidParameterException("value " + this.value + " is no number - cannot convert", 0);
        }
        return (V)createNumber((Number)this.value, this.cUnit.convertTo(((Number)this.value).doubleValue(), otherUnit));
    }


    public String getValueFormatted()
    {
        return getValueFormatted(JaloSession.getCurrentSession().getSessionContext());
    }


    public String getValueFormatted(SessionContext ctx)
    {
        String ret = null;
        if(this.value == null)
        {
            ret = getParent().getParent().getEmptyValueString();
        }
        else if(this.value instanceof Boolean)
        {
            ret = Boolean.TRUE.equals(this.value) ? Localization.getLocalizedString("featurevalue.true") : Localization.getLocalizedString("featurevalue.false");
        }
        else if(this.value instanceof String)
        {
            ret = (String)this.value;
        }
        else if(this.value instanceof ClassificationAttributeValue)
        {
            String name = ((ClassificationAttributeValue)this.value).getName(ctx);
            ret = (name != null) ? name : ("[" + ((ClassificationAttributeValue)this.value).getCode() + "]");
        }
        else if(this.value instanceof Number)
        {
            ret = getParent().getNumberFormat(ctx).format(this.value);
        }
        else if(this.value instanceof java.util.Date)
        {
            ret = getParent().getDateFormat(ctx).format(this.value);
        }
        else
        {
            ret = this.value.toString();
        }
        if(this.cUnit != null)
        {
            return ret + " " + ret;
        }
        return ret;
    }


    protected Number createNumber(Number original, double newValue)
    {
        if(original instanceof Double)
        {
            return Double.valueOf(newValue);
        }
        if(original instanceof Integer)
        {
            return Integer.valueOf((int)newValue);
        }
        if(original instanceof Long)
        {
            return Long.valueOf((long)newValue);
        }
        if(original instanceof Float)
        {
            return Float.valueOf((float)newValue);
        }
        if(original instanceof Short)
        {
            return Short.valueOf((short)(int)newValue);
        }
        return BigDecimal.valueOf(newValue);
    }


    public void setValue(V value)
    {
        if(value == null)
        {
            throw new IllegalArgumentException("value cannot be null - use Feature.removeValue() to remove a feature value");
        }
        this.value = value;
    }
}
