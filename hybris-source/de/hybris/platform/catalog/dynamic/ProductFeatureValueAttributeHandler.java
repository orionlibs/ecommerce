package de.hybris.platform.catalog.dynamic;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.type.TypeService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class ProductFeatureValueAttributeHandler implements DynamicAttributeHandler<Object, ProductFeatureModel>
{
    private FormatFactory formatFactory;
    private TypeService typeService;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_BOOLEAN = 2;
    public static final int TYPE_NUMBER = 3;
    public static final int TYPE_VALUE = 4;
    public static final int TYPE_DATE = 5;
    public static final String ENABLE_STRING_TO_NUMBER_CONVERSION = "classification.enable.string.to.number.conversion";


    public Object get(ProductFeatureModel feature)
    {
        return feature.getProperty("rawValue");
    }


    public void set(ProductFeatureModel model, Object value)
    {
        Objects.requireNonNull(value, "value was null!");
        Object rawValue = transformValueIfNeeded(value);
        setRawValue(model, rawValue);
        setSearchFields(model, rawValue);
    }


    private Object transformValueIfNeeded(Object value)
    {
        return (value instanceof HybrisEnumValue) ? this.typeService.getEnumerationValue((HybrisEnumValue)value) : value;
    }


    private void setRawValue(ProductFeatureModel feature, Object value)
    {
        feature.setProperty("rawValue", value);
    }


    private void setSearchFields(ProductFeatureModel feature, Object value)
    {
        String stringValue;
        Boolean booleanValue;
        int valueType = calculateSingleValueType(value);
        feature.setProperty("valueType", Integer.valueOf(valueType));
        BigDecimal bigDecimal = null;
        switch(valueType)
        {
            case 1:
                if(isStringToNumberConversionEnabled())
                {
                    try
                    {
                        bigDecimal = BigDecimal.valueOf(Double.parseDouble((String)value));
                    }
                    catch(NumberFormatException numberFormatException)
                    {
                    }
                }
                stringValue = (String)value;
                booleanValue = Boolean.TRUE.equals(value) ? Boolean.TRUE : Boolean.FALSE;
                break;
            case 2:
                booleanValue = (Boolean)value;
                stringValue = value.toString();
                bigDecimal = BigDecimal.valueOf(Boolean.TRUE.equals(value) ? 1.0D : 0.0D);
                break;
            case 3:
                bigDecimal = convertToBigDecimal((Number)value);
                booleanValue = (0.0D != ((Number)value).doubleValue()) ? Boolean.TRUE : Boolean.FALSE;
                stringValue = this.formatFactory.createNumberFormat().format(value);
                break;
            case 5:
                bigDecimal = BigDecimal.valueOf(((Date)value).getTime());
                booleanValue = Boolean.TRUE;
                stringValue = this.formatFactory.createDateTimeFormat(2, 2).format((Date)value);
                break;
            case 4:
                stringValue = ((ItemModel)value).getPk().toString();
                booleanValue = Boolean.TRUE;
                bigDecimal = BigDecimal.valueOf(1.0D);
                break;
            default:
                throw new IllegalArgumentException("invalid value type " + valueType + " for value " + value);
        }
        feature.setProperty("stringValue", stringValue);
        feature.setProperty("booleanValue", booleanValue);
        feature.setProperty("numberValue", bigDecimal);
    }


    private int calculateSingleValueType(Object value)
    {
        Objects.requireNonNull(value, "value cannot be null");
        if(value instanceof ItemModel)
        {
            return 4;
        }
        if(value instanceof Boolean)
        {
            return 2;
        }
        if(value instanceof Number)
        {
            return 3;
        }
        if(value instanceof String)
        {
            return 1;
        }
        if(value instanceof Date)
        {
            return 5;
        }
        throw new IllegalArgumentException("invalid value " + value + " (class=" + value
                        .getClass().getName() + ") - cannot get value type");
    }


    protected BigDecimal convertToBigDecimal(Number number)
    {
        if(number instanceof BigDecimal)
        {
            return (BigDecimal)number;
        }
        return BigDecimal.valueOf(number.doubleValue());
    }


    private boolean isStringToNumberConversionEnabled()
    {
        return Registry.getCurrentTenant().getConfig().getBoolean("classification.enable.string.to.number.conversion", true);
    }


    @Required
    public void setFormatFactory(FormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
