package de.hybris.platform.jalo.user;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAddress extends GenericItem
{
    public static final String ORIGINAL = "original";
    public static final String DUPLICATE = "duplicate";
    public static final String APPARTMENT = "appartment";
    public static final String BUILDING = "building";
    public static final String CELLPHONE = "cellphone";
    public static final String COMPANY = "company";
    public static final String COUNTRY = "country";
    public static final String DEPARTMENT = "department";
    public static final String DISTRICT = "district";
    public static final String EMAIL = "email";
    public static final String FAX = "fax";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String MIDDLENAME = "middlename";
    public static final String MIDDLENAME2 = "middlename2";
    public static final String PHONE1 = "phone1";
    public static final String PHONE2 = "phone2";
    public static final String POBOX = "pobox";
    public static final String POSTALCODE = "postalcode";
    public static final String REGION = "region";
    public static final String STREETNAME = "streetname";
    public static final String STREETNUMBER = "streetnumber";
    public static final String TITLE = "title";
    public static final String TOWN = "town";
    public static final String GENDER = "gender";
    public static final String DATEOFBIRTH = "dateOfBirth";
    protected static final BidirectionalOneToManyHandler<GeneratedAddress> OWNERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.ITEM, false, "owner", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("original", Item.AttributeMode.INITIAL);
        tmp.put("duplicate", Item.AttributeMode.INITIAL);
        tmp.put("appartment", Item.AttributeMode.INITIAL);
        tmp.put("building", Item.AttributeMode.INITIAL);
        tmp.put("cellphone", Item.AttributeMode.INITIAL);
        tmp.put("company", Item.AttributeMode.INITIAL);
        tmp.put("country", Item.AttributeMode.INITIAL);
        tmp.put("department", Item.AttributeMode.INITIAL);
        tmp.put("district", Item.AttributeMode.INITIAL);
        tmp.put("email", Item.AttributeMode.INITIAL);
        tmp.put("fax", Item.AttributeMode.INITIAL);
        tmp.put("firstname", Item.AttributeMode.INITIAL);
        tmp.put("lastname", Item.AttributeMode.INITIAL);
        tmp.put("middlename", Item.AttributeMode.INITIAL);
        tmp.put("middlename2", Item.AttributeMode.INITIAL);
        tmp.put("phone1", Item.AttributeMode.INITIAL);
        tmp.put("phone2", Item.AttributeMode.INITIAL);
        tmp.put("pobox", Item.AttributeMode.INITIAL);
        tmp.put("postalcode", Item.AttributeMode.INITIAL);
        tmp.put("region", Item.AttributeMode.INITIAL);
        tmp.put("streetname", Item.AttributeMode.INITIAL);
        tmp.put("streetnumber", Item.AttributeMode.INITIAL);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("town", Item.AttributeMode.INITIAL);
        tmp.put("gender", Item.AttributeMode.INITIAL);
        tmp.put("dateOfBirth", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAppartment(SessionContext ctx)
    {
        return (String)getProperty(ctx, "appartment");
    }


    public String getAppartment()
    {
        return getAppartment(getSession().getSessionContext());
    }


    public void setAppartment(SessionContext ctx, String value)
    {
        setProperty(ctx, "appartment", value);
    }


    public void setAppartment(String value)
    {
        setAppartment(getSession().getSessionContext(), value);
    }


    public String getBuilding(SessionContext ctx)
    {
        return (String)getProperty(ctx, "building");
    }


    public String getBuilding()
    {
        return getBuilding(getSession().getSessionContext());
    }


    public void setBuilding(SessionContext ctx, String value)
    {
        setProperty(ctx, "building", value);
    }


    public void setBuilding(String value)
    {
        setBuilding(getSession().getSessionContext(), value);
    }


    public String getCellphone(SessionContext ctx)
    {
        return (String)getProperty(ctx, "cellphone");
    }


    public String getCellphone()
    {
        return getCellphone(getSession().getSessionContext());
    }


    public void setCellphone(SessionContext ctx, String value)
    {
        setProperty(ctx, "cellphone", value);
    }


    public void setCellphone(String value)
    {
        setCellphone(getSession().getSessionContext(), value);
    }


    public String getCompany(SessionContext ctx)
    {
        return (String)getProperty(ctx, "company");
    }


    public String getCompany()
    {
        return getCompany(getSession().getSessionContext());
    }


    public void setCompany(SessionContext ctx, String value)
    {
        setProperty(ctx, "company", value);
    }


    public void setCompany(String value)
    {
        setCompany(getSession().getSessionContext(), value);
    }


    public Country getCountry(SessionContext ctx)
    {
        return (Country)getProperty(ctx, "country");
    }


    public Country getCountry()
    {
        return getCountry(getSession().getSessionContext());
    }


    public void setCountry(SessionContext ctx, Country value)
    {
        setProperty(ctx, "country", value);
    }


    public void setCountry(Country value)
    {
        setCountry(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        OWNERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Date getDateOfBirth(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "dateOfBirth");
    }


    public Date getDateOfBirth()
    {
        return getDateOfBirth(getSession().getSessionContext());
    }


    public void setDateOfBirth(SessionContext ctx, Date value)
    {
        setProperty(ctx, "dateOfBirth", value);
    }


    public void setDateOfBirth(Date value)
    {
        setDateOfBirth(getSession().getSessionContext(), value);
    }


    public String getDepartment(SessionContext ctx)
    {
        return (String)getProperty(ctx, "department");
    }


    public String getDepartment()
    {
        return getDepartment(getSession().getSessionContext());
    }


    public void setDepartment(SessionContext ctx, String value)
    {
        setProperty(ctx, "department", value);
    }


    public void setDepartment(String value)
    {
        setDepartment(getSession().getSessionContext(), value);
    }


    public String getDistrict(SessionContext ctx)
    {
        return (String)getProperty(ctx, "district");
    }


    public String getDistrict()
    {
        return getDistrict(getSession().getSessionContext());
    }


    public void setDistrict(SessionContext ctx, String value)
    {
        setProperty(ctx, "district", value);
    }


    public void setDistrict(String value)
    {
        setDistrict(getSession().getSessionContext(), value);
    }


    public Boolean isDuplicate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "duplicate");
    }


    public Boolean isDuplicate()
    {
        return isDuplicate(getSession().getSessionContext());
    }


    public boolean isDuplicateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDuplicate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDuplicateAsPrimitive()
    {
        return isDuplicateAsPrimitive(getSession().getSessionContext());
    }


    public void setDuplicate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "duplicate", value);
    }


    public void setDuplicate(Boolean value)
    {
        setDuplicate(getSession().getSessionContext(), value);
    }


    public void setDuplicate(SessionContext ctx, boolean value)
    {
        setDuplicate(ctx, Boolean.valueOf(value));
    }


    public void setDuplicate(boolean value)
    {
        setDuplicate(getSession().getSessionContext(), value);
    }


    public String getEmail(SessionContext ctx)
    {
        return (String)getProperty(ctx, "email");
    }


    public String getEmail()
    {
        return getEmail(getSession().getSessionContext());
    }


    public void setEmail(SessionContext ctx, String value)
    {
        setProperty(ctx, "email", value);
    }


    public void setEmail(String value)
    {
        setEmail(getSession().getSessionContext(), value);
    }


    public String getFax(SessionContext ctx)
    {
        return (String)getProperty(ctx, "fax");
    }


    public String getFax()
    {
        return getFax(getSession().getSessionContext());
    }


    public void setFax(SessionContext ctx, String value)
    {
        setProperty(ctx, "fax", value);
    }


    public void setFax(String value)
    {
        setFax(getSession().getSessionContext(), value);
    }


    public String getFirstname(SessionContext ctx)
    {
        return (String)getProperty(ctx, "firstname");
    }


    public String getFirstname()
    {
        return getFirstname(getSession().getSessionContext());
    }


    public void setFirstname(SessionContext ctx, String value)
    {
        setProperty(ctx, "firstname", value);
    }


    public void setFirstname(String value)
    {
        setFirstname(getSession().getSessionContext(), value);
    }


    public EnumerationValue getGender(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "gender");
    }


    public EnumerationValue getGender()
    {
        return getGender(getSession().getSessionContext());
    }


    public void setGender(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "gender", value);
    }


    public void setGender(EnumerationValue value)
    {
        setGender(getSession().getSessionContext(), value);
    }


    public String getLastname(SessionContext ctx)
    {
        return (String)getProperty(ctx, "lastname");
    }


    public String getLastname()
    {
        return getLastname(getSession().getSessionContext());
    }


    public void setLastname(SessionContext ctx, String value)
    {
        setProperty(ctx, "lastname", value);
    }


    public void setLastname(String value)
    {
        setLastname(getSession().getSessionContext(), value);
    }


    public String getMiddlename(SessionContext ctx)
    {
        return (String)getProperty(ctx, "middlename");
    }


    public String getMiddlename()
    {
        return getMiddlename(getSession().getSessionContext());
    }


    public void setMiddlename(SessionContext ctx, String value)
    {
        setProperty(ctx, "middlename", value);
    }


    public void setMiddlename(String value)
    {
        setMiddlename(getSession().getSessionContext(), value);
    }


    public String getMiddlename2(SessionContext ctx)
    {
        return (String)getProperty(ctx, "middlename2");
    }


    public String getMiddlename2()
    {
        return getMiddlename2(getSession().getSessionContext());
    }


    public void setMiddlename2(SessionContext ctx, String value)
    {
        setProperty(ctx, "middlename2", value);
    }


    public void setMiddlename2(String value)
    {
        setMiddlename2(getSession().getSessionContext(), value);
    }


    public Address getOriginal(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "original");
    }


    public Address getOriginal()
    {
        return getOriginal(getSession().getSessionContext());
    }


    protected void setOriginal(SessionContext ctx, Address value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'original' is not changeable", 0);
        }
        setProperty(ctx, "original", value);
    }


    protected void setOriginal(Address value)
    {
        setOriginal(getSession().getSessionContext(), value);
    }


    public String getPhone1(SessionContext ctx)
    {
        return (String)getProperty(ctx, "phone1");
    }


    public String getPhone1()
    {
        return getPhone1(getSession().getSessionContext());
    }


    public void setPhone1(SessionContext ctx, String value)
    {
        setProperty(ctx, "phone1", value);
    }


    public void setPhone1(String value)
    {
        setPhone1(getSession().getSessionContext(), value);
    }


    public String getPhone2(SessionContext ctx)
    {
        return (String)getProperty(ctx, "phone2");
    }


    public String getPhone2()
    {
        return getPhone2(getSession().getSessionContext());
    }


    public void setPhone2(SessionContext ctx, String value)
    {
        setProperty(ctx, "phone2", value);
    }


    public void setPhone2(String value)
    {
        setPhone2(getSession().getSessionContext(), value);
    }


    public String getPobox(SessionContext ctx)
    {
        return (String)getProperty(ctx, "pobox");
    }


    public String getPobox()
    {
        return getPobox(getSession().getSessionContext());
    }


    public void setPobox(SessionContext ctx, String value)
    {
        setProperty(ctx, "pobox", value);
    }


    public void setPobox(String value)
    {
        setPobox(getSession().getSessionContext(), value);
    }


    public String getPostalcode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "postalcode");
    }


    public String getPostalcode()
    {
        return getPostalcode(getSession().getSessionContext());
    }


    public void setPostalcode(SessionContext ctx, String value)
    {
        setProperty(ctx, "postalcode", value);
    }


    public void setPostalcode(String value)
    {
        setPostalcode(getSession().getSessionContext(), value);
    }


    public Region getRegion(SessionContext ctx)
    {
        return (Region)getProperty(ctx, "region");
    }


    public Region getRegion()
    {
        return getRegion(getSession().getSessionContext());
    }


    public void setRegion(SessionContext ctx, Region value)
    {
        setProperty(ctx, "region", value);
    }


    public void setRegion(Region value)
    {
        setRegion(getSession().getSessionContext(), value);
    }


    public String getStreetname(SessionContext ctx)
    {
        return (String)getProperty(ctx, "streetname");
    }


    public String getStreetname()
    {
        return getStreetname(getSession().getSessionContext());
    }


    public void setStreetname(SessionContext ctx, String value)
    {
        setProperty(ctx, "streetname", value);
    }


    public void setStreetname(String value)
    {
        setStreetname(getSession().getSessionContext(), value);
    }


    public String getStreetnumber(SessionContext ctx)
    {
        return (String)getProperty(ctx, "streetnumber");
    }


    public String getStreetnumber()
    {
        return getStreetnumber(getSession().getSessionContext());
    }


    public void setStreetnumber(SessionContext ctx, String value)
    {
        setProperty(ctx, "streetnumber", value);
    }


    public void setStreetnumber(String value)
    {
        setStreetnumber(getSession().getSessionContext(), value);
    }


    public Title getTitle(SessionContext ctx)
    {
        return (Title)getProperty(ctx, "title");
    }


    public Title getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, Title value)
    {
        setProperty(ctx, "title", value);
    }


    public void setTitle(Title value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public String getTown(SessionContext ctx)
    {
        return (String)getProperty(ctx, "town");
    }


    public String getTown()
    {
        return getTown(getSession().getSessionContext());
    }


    public void setTown(SessionContext ctx, String value)
    {
        setProperty(ctx, "town", value);
    }


    public void setTown(String value)
    {
        setTown(getSession().getSessionContext(), value);
    }
}
