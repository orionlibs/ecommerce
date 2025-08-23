package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductReference extends GenericItem
{
    public static final String QUALIFIER = "qualifier";
    public static final String TARGET = "target";
    public static final String QUANTITY = "quantity";
    public static final String REFERENCETYPE = "referenceType";
    public static final String ICON = "icon";
    public static final String DESCRIPTION = "description";
    public static final String PRESELECTED = "preselected";
    public static final String ACTIVE = "active";
    public static final String SOURCEPOS = "sourcePOS";
    public static final String SOURCE = "source";
    protected static final BidirectionalOneToManyHandler<GeneratedProductReference> SOURCEHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.PRODUCTREFERENCE, false, "source", "sourcePOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("target", Item.AttributeMode.INITIAL);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("referenceType", Item.AttributeMode.INITIAL);
        tmp.put("icon", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("preselected", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("sourcePOS", Item.AttributeMode.INITIAL);
        tmp.put("source", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOURCEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductReference.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductReference.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Media getIcon(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "icon");
    }


    public Media getIcon()
    {
        return getIcon(getSession().getSessionContext());
    }


    public void setIcon(SessionContext ctx, Media value)
    {
        setProperty(ctx, "icon", value);
    }


    public void setIcon(Media value)
    {
        setIcon(getSession().getSessionContext(), value);
    }


    public Boolean isPreselected(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "preselected");
    }


    public Boolean isPreselected()
    {
        return isPreselected(getSession().getSessionContext());
    }


    public boolean isPreselectedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPreselected(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPreselectedAsPrimitive()
    {
        return isPreselectedAsPrimitive(getSession().getSessionContext());
    }


    public void setPreselected(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "preselected", value);
    }


    public void setPreselected(Boolean value)
    {
        setPreselected(getSession().getSessionContext(), value);
    }


    public void setPreselected(SessionContext ctx, boolean value)
    {
        setPreselected(ctx, Boolean.valueOf(value));
    }


    public void setPreselected(boolean value)
    {
        setPreselected(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    public void setQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "qualifier", value);
    }


    public void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }


    public Integer getQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "quantity");
    }


    public Integer getQuantity()
    {
        return getQuantity(getSession().getSessionContext());
    }


    public int getQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQuantityAsPrimitive()
    {
        return getQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "quantity", value);
    }


    public void setQuantity(Integer value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public void setQuantity(SessionContext ctx, int value)
    {
        setQuantity(ctx, Integer.valueOf(value));
    }


    public void setQuantity(int value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public EnumerationValue getReferenceType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "referenceType");
    }


    public EnumerationValue getReferenceType()
    {
        return getReferenceType(getSession().getSessionContext());
    }


    public void setReferenceType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "referenceType", value);
    }


    public void setReferenceType(EnumerationValue value)
    {
        setReferenceType(getSession().getSessionContext(), value);
    }


    public Product getSource(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "source");
    }


    public Product getSource()
    {
        return getSource(getSession().getSessionContext());
    }


    protected void setSource(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'source' is not changeable", 0);
        }
        SOURCEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSource(Product value)
    {
        setSource(getSession().getSessionContext(), value);
    }


    Integer getSourcePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sourcePOS");
    }


    Integer getSourcePOS()
    {
        return getSourcePOS(getSession().getSessionContext());
    }


    int getSourcePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSourcePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSourcePOSAsPrimitive()
    {
        return getSourcePOSAsPrimitive(getSession().getSessionContext());
    }


    void setSourcePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sourcePOS", value);
    }


    void setSourcePOS(Integer value)
    {
        setSourcePOS(getSession().getSessionContext(), value);
    }


    void setSourcePOS(SessionContext ctx, int value)
    {
        setSourcePOS(ctx, Integer.valueOf(value));
    }


    void setSourcePOS(int value)
    {
        setSourcePOS(getSession().getSessionContext(), value);
    }


    public Product getTarget(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "target");
    }


    public Product getTarget()
    {
        return getTarget(getSession().getSessionContext());
    }


    public void setTarget(SessionContext ctx, Product value)
    {
        setProperty(ctx, "target", value);
    }


    public void setTarget(Product value)
    {
        setTarget(getSession().getSessionContext(), value);
    }
}
