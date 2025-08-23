package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductFeature extends GenericItem
{
    public static final String QUALIFIER = "qualifier";
    public static final String CLASSIFICATIONATTRIBUTEASSIGNMENT = "classificationAttributeAssignment";
    public static final String LANGUAGE = "language";
    public static final String VALUEPOSITION = "valuePosition";
    public static final String FEATUREPOSITION = "featurePosition";
    public static final String VALUETYPE = "valueType";
    public static final String STRINGVALUE = "stringValue";
    public static final String BOOLEANVALUE = "booleanValue";
    public static final String NUMBERVALUE = "numberValue";
    public static final String RAWVALUE = "rawValue";
    public static final String UNIT = "unit";
    public static final String VALUEDETAILS = "valueDetails";
    public static final String DESCRIPTION = "description";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    protected static final BidirectionalOneToManyHandler<GeneratedProductFeature> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.PRODUCTFEATURE, false, "product", "featurePosition", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("classificationAttributeAssignment", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("valuePosition", Item.AttributeMode.INITIAL);
        tmp.put("featurePosition", Item.AttributeMode.INITIAL);
        tmp.put("valueType", Item.AttributeMode.INITIAL);
        tmp.put("stringValue", Item.AttributeMode.INITIAL);
        tmp.put("booleanValue", Item.AttributeMode.INITIAL);
        tmp.put("numberValue", Item.AttributeMode.INITIAL);
        tmp.put("rawValue", Item.AttributeMode.INITIAL);
        tmp.put("unit", Item.AttributeMode.INITIAL);
        tmp.put("valueDetails", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("productPOS", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    Boolean isBooleanValue(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "booleanValue");
    }


    Boolean isBooleanValue()
    {
        return isBooleanValue(getSession().getSessionContext());
    }


    boolean isBooleanValueAsPrimitive(SessionContext ctx)
    {
        Boolean value = isBooleanValue(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    boolean isBooleanValueAsPrimitive()
    {
        return isBooleanValueAsPrimitive(getSession().getSessionContext());
    }


    void setBooleanValue(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "booleanValue", value);
    }


    void setBooleanValue(Boolean value)
    {
        setBooleanValue(getSession().getSessionContext(), value);
    }


    void setBooleanValue(SessionContext ctx, boolean value)
    {
        setBooleanValue(ctx, Boolean.valueOf(value));
    }


    void setBooleanValue(boolean value)
    {
        setBooleanValue(getSession().getSessionContext(), value);
    }


    public ClassAttributeAssignment getClassificationAttributeAssignment(SessionContext ctx)
    {
        return (ClassAttributeAssignment)getProperty(ctx, "classificationAttributeAssignment");
    }


    public ClassAttributeAssignment getClassificationAttributeAssignment()
    {
        return getClassificationAttributeAssignment(getSession().getSessionContext());
    }


    protected void setClassificationAttributeAssignment(SessionContext ctx, ClassAttributeAssignment value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'classificationAttributeAssignment' is not changeable", 0);
        }
        setProperty(ctx, "classificationAttributeAssignment", value);
    }


    protected void setClassificationAttributeAssignment(ClassAttributeAssignment value)
    {
        setClassificationAttributeAssignment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Integer getFeaturePosition(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "featurePosition");
    }


    public Integer getFeaturePosition()
    {
        return getFeaturePosition(getSession().getSessionContext());
    }


    public int getFeaturePositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getFeaturePosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFeaturePositionAsPrimitive()
    {
        return getFeaturePositionAsPrimitive(getSession().getSessionContext());
    }


    public void setFeaturePosition(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "featurePosition", value);
    }


    public void setFeaturePosition(Integer value)
    {
        setFeaturePosition(getSession().getSessionContext(), value);
    }


    public void setFeaturePosition(SessionContext ctx, int value)
    {
        setFeaturePosition(ctx, Integer.valueOf(value));
    }


    public void setFeaturePosition(int value)
    {
        setFeaturePosition(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    protected void setLanguage(SessionContext ctx, Language value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'language' is not changeable", 0);
        }
        setProperty(ctx, "language", value);
    }


    protected void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    BigDecimal getNumberValue(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "numberValue");
    }


    BigDecimal getNumberValue()
    {
        return getNumberValue(getSession().getSessionContext());
    }


    void setNumberValue(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "numberValue", value);
    }


    void setNumberValue(BigDecimal value)
    {
        setNumberValue(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    protected void setProduct(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'product' is not changeable", 0);
        }
        PRODUCTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    Integer getProductPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "productPOS");
    }


    Integer getProductPOS()
    {
        return getProductPOS(getSession().getSessionContext());
    }


    int getProductPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getProductPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getProductPOSAsPrimitive()
    {
        return getProductPOSAsPrimitive(getSession().getSessionContext());
    }


    void setProductPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "productPOS", value);
    }


    void setProductPOS(Integer value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    void setProductPOS(SessionContext ctx, int value)
    {
        setProductPOS(ctx, Integer.valueOf(value));
    }


    void setProductPOS(int value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    protected void setQualifier(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'qualifier' is not changeable", 0);
        }
        setProperty(ctx, "qualifier", value);
    }


    protected void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }


    Object getRawValue(SessionContext ctx)
    {
        return getProperty(ctx, "rawValue");
    }


    Object getRawValue()
    {
        return getRawValue(getSession().getSessionContext());
    }


    void setRawValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "rawValue", value);
    }


    void setRawValue(Object value)
    {
        setRawValue(getSession().getSessionContext(), value);
    }


    String getStringValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "stringValue");
    }


    String getStringValue()
    {
        return getStringValue(getSession().getSessionContext());
    }


    void setStringValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "stringValue", value);
    }


    void setStringValue(String value)
    {
        setStringValue(getSession().getSessionContext(), value);
    }


    public ClassificationAttributeUnit getUnit(SessionContext ctx)
    {
        return (ClassificationAttributeUnit)getProperty(ctx, "unit");
    }


    public ClassificationAttributeUnit getUnit()
    {
        return getUnit(getSession().getSessionContext());
    }


    public void setUnit(SessionContext ctx, ClassificationAttributeUnit value)
    {
        setProperty(ctx, "unit", value);
    }


    public void setUnit(ClassificationAttributeUnit value)
    {
        setUnit(getSession().getSessionContext(), value);
    }


    public String getValueDetails(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valueDetails");
    }


    public String getValueDetails()
    {
        return getValueDetails(getSession().getSessionContext());
    }


    public void setValueDetails(SessionContext ctx, String value)
    {
        setProperty(ctx, "valueDetails", value);
    }


    public void setValueDetails(String value)
    {
        setValueDetails(getSession().getSessionContext(), value);
    }


    public Integer getValuePosition(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "valuePosition");
    }


    public Integer getValuePosition()
    {
        return getValuePosition(getSession().getSessionContext());
    }


    public int getValuePositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getValuePosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getValuePositionAsPrimitive()
    {
        return getValuePositionAsPrimitive(getSession().getSessionContext());
    }


    public void setValuePosition(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "valuePosition", value);
    }


    public void setValuePosition(Integer value)
    {
        setValuePosition(getSession().getSessionContext(), value);
    }


    public void setValuePosition(SessionContext ctx, int value)
    {
        setValuePosition(ctx, Integer.valueOf(value));
    }


    public void setValuePosition(int value)
    {
        setValuePosition(getSession().getSessionContext(), value);
    }


    Integer getValueType(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "valueType");
    }


    Integer getValueType()
    {
        return getValueType(getSession().getSessionContext());
    }


    int getValueTypeAsPrimitive(SessionContext ctx)
    {
        Integer value = getValueType(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getValueTypeAsPrimitive()
    {
        return getValueTypeAsPrimitive(getSession().getSessionContext());
    }


    void setValueType(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "valueType", value);
    }


    void setValueType(Integer value)
    {
        setValueType(getSession().getSessionContext(), value);
    }


    void setValueType(SessionContext ctx, int value)
    {
        setValueType(ctx, Integer.valueOf(value));
    }


    void setValueType(int value)
    {
        setValueType(getSession().getSessionContext(), value);
    }
}
