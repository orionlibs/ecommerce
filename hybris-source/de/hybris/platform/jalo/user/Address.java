package de.hybris.platform.jalo.user;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;

public class Address extends GeneratedAddress
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String STREETTYPE = "streettype";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String FLOOR = "floor";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PLANET = "planet";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String STREETSECTION = "streetsection";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CUSTOM1 = "custom1";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CUSTOM2 = "custom2";


    @SLDSafe(portingClass = "AddressPrepareInterceptor", portingMethod = "onPrepare(final AddressModel model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item owner = (Item)allAttributes.get(Item.OWNER);
        if(owner == null)
        {
            throw new JaloInvalidParameterException("Missing parameter( " + Item.OWNER + ") to create a Address", 0);
        }
        if(type == null)
        {
            throw new JaloInvalidParameterException("Address type cannot be null", 0);
        }
        if(!Address.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("Address type " + type + " is incompatible to default Address class", 0);
        }
        allAttributes.setAttributeMode("appartment", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("building", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("cellphone", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("company", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("department", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("district", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("email", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("fax", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("firstname", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("lastname", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("middlename", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("middlename2", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("phone1", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("phone2", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pobox", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("postalcode", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("streetname", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("streetnumber", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("town", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("gender", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("dateOfBirth", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode(OWNER, Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("original", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("title", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("country", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("region", Item.AttributeMode.INITIAL);
        if(allAttributes.get("duplicate") == null)
        {
            if(allAttributes.get("original") == null)
            {
                allAttributes.put("duplicate", Boolean.FALSE);
            }
            else
            {
                allAttributes.put("duplicate", Boolean.TRUE);
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public static final String USER = Item.OWNER;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DATHEOFBIRTH = "dateOfBirth";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String[] ALL_PROPERTY_FIELDS = new String[] {
                    "firstname", "lastname", "middlename", "middlename2", "streetname", "streetnumber", "streetsection", "streettype", "town", "postalcode",
                    "pobox", "floor", "appartment", "company", "department", "building", "district", "custom1", "custom2", "email",
                    "phone1", "phone2", "fax", "cellphone"};
    @Deprecated(since = "ages", forRemoval = false)
    public static final String GENDER_ENUM_TYPE = "Gender";


    public User getUser()
    {
        Item item = getOwner();
        return (item instanceof User) ? (User)item : null;
    }


    public void setUser(User user)
    {
        try
        {
            setOwner((Item)user);
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setGender(String key, EnumerationValue value)
    {
        setGender(value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getGender(String key)
    {
        return getGender();
    }


    @SLDSafe(portingClass = "AddressModel", portingMethod = "setOriginal(final AddressModel value)")
    public void setOriginal(SessionContext ctx, Address value)
    {
        setProperty(ctx, "original", value);
    }


    @SLDSafe(portingClass = "AddressModel", portingMethod = "setOriginal(final AddressModel value)")
    public void setOriginal(Address value)
    {
        setOriginal(getSession().getSessionContext(), value);
    }


    @SLDSafe(portingClass = "core-items.xml", portingMethod = "nullDecorator")
    public Boolean isDuplicate(SessionContext ctx)
    {
        Boolean result = super.isDuplicate(ctx);
        return (result == null) ? Boolean.FALSE : result;
    }


    @SLDSafe(portingClass = "DefaultModelService", portingMethod = "refresh(final Object model)")
    public void remove() throws ConsistencyCheckException
    {
        User user = getUser();
        if(user != null)
        {
            if(equals(user.getDefaultPaymentAddress()))
            {
                user.setDefaultPaymentAddress(null);
            }
            if(equals(user.getDefaultDeliveryAddress()))
            {
                user.setDefaultDeliveryAddress(null);
            }
        }
        super.remove();
    }
}
