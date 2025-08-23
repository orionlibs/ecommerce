package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.PhoneContactInfoType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PhoneContactInfoModel extends AbstractContactInfoModel
{
    public static final String _TYPECODE = "PhoneContactInfo";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String TYPE = "type";


    public PhoneContactInfoModel()
    {
    }


    public PhoneContactInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PhoneContactInfoModel(String _code, String _phoneNumber, PhoneContactInfoType _type, UserModel _user)
    {
        setCode(_code);
        setPhoneNumber(_phoneNumber);
        setType(_type);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PhoneContactInfoModel(String _code, ItemModel _owner, String _phoneNumber, PhoneContactInfoType _type, UserModel _user)
    {
        setCode(_code);
        setOwner(_owner);
        setPhoneNumber(_phoneNumber);
        setType(_type);
        setUser(_user);
    }


    @Accessor(qualifier = "phoneNumber", type = Accessor.Type.GETTER)
    public String getPhoneNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("phoneNumber");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public PhoneContactInfoType getType()
    {
        return (PhoneContactInfoType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "phoneNumber", type = Accessor.Type.SETTER)
    public void setPhoneNumber(String value)
    {
        getPersistenceContext().setPropertyValue("phoneNumber", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(PhoneContactInfoType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
