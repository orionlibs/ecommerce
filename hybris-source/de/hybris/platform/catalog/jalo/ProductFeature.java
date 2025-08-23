package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.Utilities;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductFeature extends GeneratedProductFeature
{
    public static final int TYPE_STRING = 1;
    public static final int TYPE_BOOLEAN = 2;
    public static final int TYPE_NUMBER = 3;
    public static final int TYPE_VALUE = 4;
    public static final int TYPE_DATE = 5;
    public static final String ENABLE_STRING_TO_NUMBER_CONVERSION = "classification.enable.string.to.number.conversion";
    public static final String VALUE = "value";


    @SLDSafe(portingClass = "ProductFeaturePrepareInterceptor", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!Item.checkMandatoryAttribute("product", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new " + type.getCode(), 0);
        }
        if(isHandledInServiceLayer(ctx))
        {
            if(allAttributes.get("rawValue") == null)
            {
                throw new JaloInvalidParameterException("missing rawvalue for creating a new " + type
                                .getCode() + " from service layer", 0);
            }
        }
        else if(allAttributes.get("value") == null)
        {
            throw new JaloInvalidParameterException("missing value for creating a new " + type.getCode(), 0);
        }
        Product product = (Product)allAttributes.get("product");
        ClassAttributeAssignment attr = (ClassAttributeAssignment)allAttributes.get("classificationAttributeAssignment");
        String quali = (String)allAttributes.get("qualifier");
        if(quali == null && attr == null)
        {
            throw new JaloInvalidParameterException("either qualifier or classificationAttributeAssignment must be provided to create a new " + type
                            .getCode(), 0);
        }
        if(!FeatureContainer.isInFeatureContainerTA(ctx))
        {
            product.setModificationTime(new Date());
        }
        if(quali == null && attr != null)
        {
            Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)allAttributes);
            myMap.put("qualifier", FeatureContainer.createUniqueKey(attr));
            return super.createItem(ctx, type, myMap);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @SLDSafe(portingClass = "ProductFeaturePrepareInterceptor", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    protected JaloPropertyContainer getInitialProperties(JaloSession jaloSession, Item.ItemAttributeMap allAttributes)
    {
        JaloPropertyContainer cont = jaloSession.createPropertyContainer();
        cont.setProperty("product", allAttributes.get("product"));
        cont.setProperty("qualifier", allAttributes.get("qualifier"));
        if(allAttributes.get("language") != null)
        {
            cont.setProperty("language", allAttributes.get("language"));
        }
        cont.setProperty("classificationAttributeAssignment", allAttributes.get("classificationAttributeAssignment"));
        cont.setProperty("featurePosition",
                        (allAttributes.get("featurePosition") != null) ? allAttributes.get("featurePosition") :
                                        Integer.valueOf(0));
        cont.setProperty("valuePosition",
                        (allAttributes.get("valuePosition") != null) ? allAttributes.get("valuePosition") :
                                        Integer.valueOf(0));
        cont.setProperty("unit", allAttributes.get("unit"));
        if(isHandledInServiceLayer(jaloSession.getSessionContext()))
        {
            cont.setProperty("valueType", allAttributes.get("valueType"));
            cont.setProperty("rawValue", allAttributes.get("rawValue"));
            if(allAttributes.get("stringValue") != null)
            {
                cont.setProperty("stringValue", allAttributes.get("stringValue"));
            }
            if(allAttributes.get("booleanValue") != null)
            {
                cont.setProperty("booleanValue", allAttributes.get("booleanValue"));
            }
            if(allAttributes.get("numberValue") != null)
            {
                cont.setProperty("numberValue", allAttributes.get("numberValue"));
            }
        }
        else
        {
            doSetSingleValue(jaloSession.getSessionContext(), cont, allAttributes.get("value"));
        }
        return cont;
    }


    @SLDSafe
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap map = super.getNonInitialAttributes(ctx, allAttributes);
        map.remove("product");
        map.remove("qualifier");
        map.remove("language");
        map.remove("classificationAttributeAssignment");
        map.remove("valuePosition");
        map.remove("featurePosition");
        map.remove("valueType");
        map.remove("unit");
        map.remove("stringValue");
        map.remove("booleanValue");
        map.remove("numberValue");
        map.remove("rawValue");
        map.remove("value");
        return map;
    }


    @SLDSafe
    protected void removeLinks()
    {
    }


    @SLDSafe(portingClass = "ProductFeatureRemoveInterceptor", portingMethod = "onRemove(final ProductFeatureModel productFeatureModel, final InterceptorContext ctx)")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        markProductAsModified();
        super.remove(ctx);
    }


    public void untype()
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("core.types.creation.initial", Boolean.TRUE);
        setClassificationAttributeAssignment(ctx, null);
    }


    public void reType(ClassAttributeAssignment assignment)
    {
        if(assignment == null)
        {
            throw new NullPointerException("assignment was NULL");
        }
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("core.types.creation.initial", Boolean.TRUE);
        setClassificationAttributeAssignment(ctx, assignment);
        setQualifier(ctx, FeatureContainer.createUniqueKey(assignment));
        markProductAsModified();
    }


    protected int calculateSingleValueType(Object value)
    {
        if(value == null)
        {
            throw new NullPointerException("value cannot be null");
        }
        if(value instanceof Item)
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
        throw new JaloInvalidParameterException("invalid value " + value + " (class=" + value
                        .getClass().getName() + ") - cannot get value type", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductFeatureValueAttributeHandler", portingMethod = "set(final ProductFeatureModel model, final Object value)")
    public void setValue(SessionContext ctx, Object value) throws NullPointerException
    {
        if(value == null)
        {
            throw new NullPointerException("value was null");
        }
        if(isHandledInServiceLayer(ctx))
        {
            throw new IllegalStateException("setValue() must not be called from Service Layer");
        }
        doSetSingleValue(ctx, null, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductFeatureValueAttributeHandler", portingMethod = "get(final ProductFeatureModel model)")
    public Object getValue(SessionContext ctx)
    {
        return getRawValue(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductFeatureValueAttributeHandler", portingMethod = "get(final ProductFeatureModel model)")
    public Object getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    protected void doSetSingleValue(SessionContext ctx, JaloPropertyContainer cont, Object value)
    {
        boolean initial = (cont != null);
        if(initial)
        {
            cont.setProperty("rawValue", value);
        }
        else
        {
            setRawValue(ctx, value);
        }
        setSearchFields(ctx, cont, value);
        if(!initial)
        {
            markProductAsModified();
        }
    }


    protected void setSearchFields(SessionContext ctx, JaloPropertyContainer cont, Object value)
    {
        boolean initial = (cont != null);
        int valueType = calculateSingleValueType(value);
        if(initial)
        {
            cont.setProperty("valueType", Integer.valueOf(valueType));
        }
        else
        {
            setValueType(valueType);
        }
        String string = null;
        Boolean booleanValue = null;
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
                string = (String)value;
                booleanValue = "true".equalsIgnoreCase((String)value) ? Boolean.TRUE : Boolean.FALSE;
                break;
            case 2:
                booleanValue = (Boolean)value;
                string = ((Boolean)value).toString();
                bigDecimal = BigDecimal.valueOf(Boolean.TRUE.equals(value) ? 1.0D : 0.0D);
                break;
            case 3:
                bigDecimal = convertToBigDecimal((Number)value);
                booleanValue = (0.0D != ((Number)value).doubleValue()) ? Boolean.TRUE : Boolean.FALSE;
                string = Utilities.getNumberInstance().format(value);
                break;
            case 5:
                bigDecimal = BigDecimal.valueOf((value != null) ? ((Date)value).getTime() : 0L);
                booleanValue = (value != null) ? Boolean.TRUE : Boolean.FALSE;
                string = (value != null) ? Utilities.getDateTimeInstance().format((Date)value) : null;
                break;
            case 4:
                string = ((Item)value).getPK().toString();
                booleanValue = Boolean.TRUE;
                bigDecimal = BigDecimal.valueOf(1.0D);
                break;
            default:
                throw new JaloInvalidParameterException("invalid value type " + valueType + " for value " + value, 0);
        }
        if(initial)
        {
            cont.setProperty("stringValue", string);
            cont.setProperty("booleanValue", booleanValue);
            cont.setProperty("numberValue", bigDecimal);
        }
        else
        {
            setStringValue(ctx, string);
            setBooleanValue(ctx, booleanValue);
            setNumberValue(ctx, bigDecimal);
        }
    }


    protected BigDecimal convertToBigDecimal(Number number)
    {
        if(number == null || number instanceof BigDecimal)
        {
            return (BigDecimal)number;
        }
        return BigDecimal.valueOf(number.doubleValue());
    }


    private void markProductAsModified()
    {
        if(getProduct() != null && !FeatureContainer.isInFeatureContainerTA(JaloSession.getCurrentSession().getSessionContext()))
        {
            getProduct().setModificationTime(new Date());
        }
    }


    protected boolean isStringToNumberConversionEnabled()
    {
        return Registry.getCurrentTenant().getConfig().getBoolean("classification.enable.string.to.number.conversion", true);
    }


    private boolean isHandledInServiceLayer(SessionContext ctx)
    {
        return Boolean.TRUE.equals(ctx.getAttribute("save.from.service.layer"));
    }
}
