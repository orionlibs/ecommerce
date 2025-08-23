package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;

public class B2BRegistrationModel extends ItemModel
{
    public static final String _TYPECODE = "B2BRegistration";
    public static final String BASESITE = "baseSite";
    public static final String LANGUAGE = "language";
    public static final String CURRENCY = "currency";
    public static final String BASESTORE = "baseStore";
    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String POSITION = "position";
    public static final String TELEPHONE = "telephone";
    public static final String TELEPHONEEXTENSION = "telephoneExtension";
    public static final String COMPANYNAME = "companyName";
    public static final String COMPANYADDRESSSTREET = "companyAddressStreet";
    public static final String COMPANYADDRESSSTREETLINE2 = "companyAddressStreetLine2";
    public static final String COMPANYADDRESSCITY = "companyAddressCity";
    public static final String COMPANYADDRESSPOSTALCODE = "companyAddressPostalCode";
    public static final String COMPANYADDRESSREGION = "companyAddressRegion";
    public static final String COMPANYADDRESSCOUNTRY = "companyAddressCountry";
    public static final String MESSAGE = "message";
    public static final String REJECTREASON = "rejectReason";
    public static final String DEFAULTB2BUNIT = "defaultB2BUnit";


    public B2BRegistrationModel()
    {
    }


    public B2BRegistrationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationModel(BaseSiteModel _baseSite, BaseStoreModel _baseStore, CurrencyModel _currency, String _email, LanguageModel _language, String _name)
    {
        setBaseSite(_baseSite);
        setBaseStore(_baseStore);
        setCurrency(_currency);
        setEmail(_email);
        setLanguage(_language);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationModel(BaseSiteModel _baseSite, BaseStoreModel _baseStore, CurrencyModel _currency, String _email, LanguageModel _language, String _name, ItemModel _owner)
    {
        setBaseSite(_baseSite);
        setBaseStore(_baseStore);
        setCurrency(_currency);
        setEmail(_email);
        setLanguage(_language);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
    public BaseSiteModel getBaseSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("baseSite");
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
    public BaseStoreModel getBaseStore()
    {
        return (BaseStoreModel)getPersistenceContext().getPropertyValue("baseStore");
    }


    @Accessor(qualifier = "companyAddressCity", type = Accessor.Type.GETTER)
    public String getCompanyAddressCity()
    {
        return (String)getPersistenceContext().getPropertyValue("companyAddressCity");
    }


    @Accessor(qualifier = "companyAddressCountry", type = Accessor.Type.GETTER)
    public CountryModel getCompanyAddressCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("companyAddressCountry");
    }


    @Accessor(qualifier = "companyAddressPostalCode", type = Accessor.Type.GETTER)
    public String getCompanyAddressPostalCode()
    {
        return (String)getPersistenceContext().getPropertyValue("companyAddressPostalCode");
    }


    @Accessor(qualifier = "companyAddressRegion", type = Accessor.Type.GETTER)
    public RegionModel getCompanyAddressRegion()
    {
        return (RegionModel)getPersistenceContext().getPropertyValue("companyAddressRegion");
    }


    @Accessor(qualifier = "companyAddressStreet", type = Accessor.Type.GETTER)
    public String getCompanyAddressStreet()
    {
        return (String)getPersistenceContext().getPropertyValue("companyAddressStreet");
    }


    @Accessor(qualifier = "companyAddressStreetLine2", type = Accessor.Type.GETTER)
    public String getCompanyAddressStreetLine2()
    {
        return (String)getPersistenceContext().getPropertyValue("companyAddressStreetLine2");
    }


    @Accessor(qualifier = "companyName", type = Accessor.Type.GETTER)
    public String getCompanyName()
    {
        return (String)getPersistenceContext().getPropertyValue("companyName");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.GETTER)
    public B2BUnitModel getDefaultB2BUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("defaultB2BUnit");
    }


    @Accessor(qualifier = "email", type = Accessor.Type.GETTER)
    public String getEmail()
    {
        return (String)getPersistenceContext().getPropertyValue("email");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("message");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public String getPosition()
    {
        return (String)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "rejectReason", type = Accessor.Type.GETTER)
    public String getRejectReason()
    {
        return (String)getPersistenceContext().getPropertyValue("rejectReason");
    }


    @Accessor(qualifier = "telephone", type = Accessor.Type.GETTER)
    public String getTelephone()
    {
        return (String)getPersistenceContext().getPropertyValue("telephone");
    }


    @Accessor(qualifier = "telephoneExtension", type = Accessor.Type.GETTER)
    public String getTelephoneExtension()
    {
        return (String)getPersistenceContext().getPropertyValue("telephoneExtension");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public TitleModel getTitle()
    {
        return (TitleModel)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
    public void setBaseSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("baseSite", value);
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
    public void setBaseStore(BaseStoreModel value)
    {
        getPersistenceContext().setPropertyValue("baseStore", value);
    }


    @Accessor(qualifier = "companyAddressCity", type = Accessor.Type.SETTER)
    public void setCompanyAddressCity(String value)
    {
        getPersistenceContext().setPropertyValue("companyAddressCity", value);
    }


    @Accessor(qualifier = "companyAddressCountry", type = Accessor.Type.SETTER)
    public void setCompanyAddressCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("companyAddressCountry", value);
    }


    @Accessor(qualifier = "companyAddressPostalCode", type = Accessor.Type.SETTER)
    public void setCompanyAddressPostalCode(String value)
    {
        getPersistenceContext().setPropertyValue("companyAddressPostalCode", value);
    }


    @Accessor(qualifier = "companyAddressRegion", type = Accessor.Type.SETTER)
    public void setCompanyAddressRegion(RegionModel value)
    {
        getPersistenceContext().setPropertyValue("companyAddressRegion", value);
    }


    @Accessor(qualifier = "companyAddressStreet", type = Accessor.Type.SETTER)
    public void setCompanyAddressStreet(String value)
    {
        getPersistenceContext().setPropertyValue("companyAddressStreet", value);
    }


    @Accessor(qualifier = "companyAddressStreetLine2", type = Accessor.Type.SETTER)
    public void setCompanyAddressStreetLine2(String value)
    {
        getPersistenceContext().setPropertyValue("companyAddressStreetLine2", value);
    }


    @Accessor(qualifier = "companyName", type = Accessor.Type.SETTER)
    public void setCompanyName(String value)
    {
        getPersistenceContext().setPropertyValue("companyName", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.SETTER)
    public void setDefaultB2BUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("defaultB2BUnit", value);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.SETTER)
    public void setEmail(String value)
    {
        getPersistenceContext().setPropertyValue("email", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        getPersistenceContext().setPropertyValue("message", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(String value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }


    @Accessor(qualifier = "rejectReason", type = Accessor.Type.SETTER)
    public void setRejectReason(String value)
    {
        getPersistenceContext().setPropertyValue("rejectReason", value);
    }


    @Accessor(qualifier = "telephone", type = Accessor.Type.SETTER)
    public void setTelephone(String value)
    {
        getPersistenceContext().setPropertyValue("telephone", value);
    }


    @Accessor(qualifier = "telephoneExtension", type = Accessor.Type.SETTER)
    public void setTelephoneExtension(String value)
    {
        getPersistenceContext().setPropertyValue("telephoneExtension", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(TitleModel value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }
}
