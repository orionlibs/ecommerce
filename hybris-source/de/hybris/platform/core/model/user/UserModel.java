package de.hybris.platform.core.model.user;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.model.ThemeModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.enums.RetentionState;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.europe1.model.GlobalDiscountRowModel;
import de.hybris.platform.hmc.model.UserProfileModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserModel extends PrincipalModel
{
    public static final String _TYPECODE = "User";
    public static final String _ABSTRACTCOMMENTAUTHORRELATION = "AbstractCommentAuthorRelation";
    public static final String _COMMENTUSERSETTINGUSERRELATION = "CommentUserSettingUserRelation";
    public static final String _COMMENTASSIGNEERELATION = "CommentAssigneeRelation";
    public static final String _USERSFORRESTRICTION = "UsersForRestriction";
    public static final String CURRENTTIME = "currentTime";
    public static final String CURRENTDATE = "currentDate";
    public static final String DEFAULTPAYMENTADDRESS = "defaultPaymentAddress";
    public static final String DEFAULTSHIPMENTADDRESS = "defaultShipmentAddress";
    public static final String PASSWORD = "password";
    public static final String PASSWORDENCODING = "passwordEncoding";
    public static final String ENCODEDPASSWORD = "encodedPassword";
    public static final String PASSWORDANSWER = "passwordAnswer";
    public static final String PASSWORDQUESTION = "passwordQuestion";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String LOGINDISABLED = "loginDisabled";
    public static final String LASTLOGIN = "lastLogin";
    public static final String HMCLOGINDISABLED = "hmcLoginDisabled";
    public static final String RETENTIONSTATE = "retentionState";
    public static final String USERPROFILE = "userprofile";
    public static final String DEACTIVATIONDATE = "deactivationDate";
    public static final String RANDOMTOKEN = "randomToken";
    public static final String CONTACTINFOS = "contactInfos";
    public static final String CARTS = "carts";
    public static final String QUOTES = "quotes";
    public static final String ORDERS = "orders";
    public static final String ADDRESSES = "addresses";
    public static final String PAYMENTINFOS = "paymentInfos";
    public static final String ALLWRITEABLECATALOGVERSIONS = "allWriteableCatalogVersions";
    public static final String EUROPE1PRICEFACTORY_UDG = "Europe1PriceFactory_UDG";
    public static final String EUROPE1PRICEFACTORY_UPG = "Europe1PriceFactory_UPG";
    public static final String EUROPE1PRICEFACTORY_UTG = "Europe1PriceFactory_UTG";
    public static final String EUROPE1DISCOUNTS = "europe1Discounts";
    public static final String OWNEUROPE1DISCOUNTS = "ownEurope1Discounts";
    public static final String TOKENS = "tokens";
    public static final String CREATEDCOMMENTS = "createdComments";
    public static final String COMMENTUSERSETTINGS = "commentUserSettings";
    public static final String ASSIGNEDCOMMENTS = "assignedComments";
    public static final String THEMEFORBACKOFFICE = "themeForBackoffice";
    public static final String AVATAR = "avatar";
    public static final String COLLECTIONPKS = "collectionPks";
    public static final String AUTHORIZEDTOUNLOCKPAGES = "authorizedToUnlockPages";
    public static final String RESTRICTIONS = "restrictions";
    public static final String LOCKEDPAGES = "lockedPages";
    public static final String CUSTOMERREVIEWS = "customerReviews";
    public static final String ASSIGNEDQUOTES = "assignedQuotes";


    public UserModel()
    {
    }


    public UserModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserModel(boolean _loginDisabled, String _uid)
    {
        setLoginDisabled(_loginDisabled);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserModel(Collection<CustomerReviewModel> _customerReviews, boolean _loginDisabled, ItemModel _owner, String _uid)
    {
        setCustomerReviews(_customerReviews);
        setLoginDisabled(_loginDisabled);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "addresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getPropertyValue("addresses");
    }


    @Accessor(qualifier = "allWriteableCatalogVersions", type = Accessor.Type.GETTER)
    public Collection<CatalogVersionModel> getAllWriteableCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allWriteableCatalogVersions");
    }


    @Accessor(qualifier = "assignedComments", type = Accessor.Type.GETTER)
    public List<CommentModel> getAssignedComments()
    {
        return (List<CommentModel>)getPersistenceContext().getPropertyValue("assignedComments");
    }


    @Accessor(qualifier = "assignedQuotes", type = Accessor.Type.GETTER)
    public Collection<QuoteModel> getAssignedQuotes()
    {
        return (Collection<QuoteModel>)getPersistenceContext().getPropertyValue("assignedQuotes");
    }


    @Accessor(qualifier = "avatar", type = Accessor.Type.GETTER)
    public MediaModel getAvatar()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("avatar");
    }


    @Accessor(qualifier = "carts", type = Accessor.Type.GETTER)
    public Collection<CartModel> getCarts()
    {
        return (Collection<CartModel>)getPersistenceContext().getPropertyValue("carts");
    }


    @Accessor(qualifier = "collectionPks", type = Accessor.Type.GETTER)
    public Collection<BackofficeObjectSpecialCollectionModel> getCollectionPks()
    {
        return (Collection<BackofficeObjectSpecialCollectionModel>)getPersistenceContext().getPropertyValue("collectionPks");
    }


    @Accessor(qualifier = "contactInfos", type = Accessor.Type.GETTER)
    public Collection<AbstractContactInfoModel> getContactInfos()
    {
        return (Collection<AbstractContactInfoModel>)getPersistenceContext().getPropertyValue("contactInfos");
    }


    @Accessor(qualifier = "createdComments", type = Accessor.Type.GETTER)
    public List<AbstractCommentModel> getCreatedComments()
    {
        return (List<AbstractCommentModel>)getPersistenceContext().getPropertyValue("createdComments");
    }


    @Accessor(qualifier = "currentDate", type = Accessor.Type.GETTER)
    public Date getCurrentDate()
    {
        return (Date)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "currentDate");
    }


    @Accessor(qualifier = "currentTime", type = Accessor.Type.GETTER)
    public Date getCurrentTime()
    {
        return (Date)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "currentTime");
    }


    @Accessor(qualifier = "customerReviews", type = Accessor.Type.GETTER)
    public Collection<CustomerReviewModel> getCustomerReviews()
    {
        return (Collection<CustomerReviewModel>)getPersistenceContext().getPropertyValue("customerReviews");
    }


    @Accessor(qualifier = "deactivationDate", type = Accessor.Type.GETTER)
    public Date getDeactivationDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("deactivationDate");
    }


    @Accessor(qualifier = "defaultPaymentAddress", type = Accessor.Type.GETTER)
    public AddressModel getDefaultPaymentAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("defaultPaymentAddress");
    }


    @Accessor(qualifier = "defaultShipmentAddress", type = Accessor.Type.GETTER)
    public AddressModel getDefaultShipmentAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("defaultShipmentAddress");
    }


    @Accessor(qualifier = "encodedPassword", type = Accessor.Type.GETTER)
    public String getEncodedPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("encodedPassword");
    }


    @Accessor(qualifier = "europe1Discounts", type = Accessor.Type.GETTER)
    public Collection<GlobalDiscountRowModel> getEurope1Discounts()
    {
        return (Collection<GlobalDiscountRowModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "europe1Discounts");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.GETTER)
    public UserDiscountGroup getEurope1PriceFactory_UDG()
    {
        return (UserDiscountGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UDG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.GETTER)
    public UserPriceGroup getEurope1PriceFactory_UPG()
    {
        return (UserPriceGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UPG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.GETTER)
    public UserTaxGroup getEurope1PriceFactory_UTG()
    {
        return (UserTaxGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UTG");
    }


    @Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.GETTER)
    public Boolean getHmcLoginDisabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("hmcLoginDisabled");
    }


    @Accessor(qualifier = "lastLogin", type = Accessor.Type.GETTER)
    public Date getLastLogin()
    {
        return (Date)getPersistenceContext().getPropertyValue("lastLogin");
    }


    @Accessor(qualifier = "lockedPages", type = Accessor.Type.GETTER)
    public Collection<AbstractPageModel> getLockedPages()
    {
        return (Collection<AbstractPageModel>)getPersistenceContext().getPropertyValue("lockedPages");
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
    public Collection<OrderModel> getOrders()
    {
        return (Collection<OrderModel>)getPersistenceContext().getPropertyValue("orders");
    }


    @Accessor(qualifier = "ownEurope1Discounts", type = Accessor.Type.GETTER)
    public Collection<GlobalDiscountRowModel> getOwnEurope1Discounts()
    {
        return (Collection<GlobalDiscountRowModel>)getPersistenceContext().getPropertyValue("ownEurope1Discounts");
    }


    @Accessor(qualifier = "passwordAnswer", type = Accessor.Type.GETTER)
    public String getPasswordAnswer()
    {
        return (String)getPersistenceContext().getPropertyValue("passwordAnswer");
    }


    @Accessor(qualifier = "passwordEncoding", type = Accessor.Type.GETTER)
    public String getPasswordEncoding()
    {
        return (String)getPersistenceContext().getPropertyValue("passwordEncoding");
    }


    @Accessor(qualifier = "passwordQuestion", type = Accessor.Type.GETTER)
    public String getPasswordQuestion()
    {
        return (String)getPersistenceContext().getPropertyValue("passwordQuestion");
    }


    @Accessor(qualifier = "paymentInfos", type = Accessor.Type.GETTER)
    public Collection<PaymentInfoModel> getPaymentInfos()
    {
        return (Collection<PaymentInfoModel>)getPersistenceContext().getPropertyValue("paymentInfos");
    }


    @Accessor(qualifier = "quotes", type = Accessor.Type.GETTER)
    public Collection<QuoteModel> getQuotes()
    {
        return (Collection<QuoteModel>)getPersistenceContext().getPropertyValue("quotes");
    }


    @Accessor(qualifier = "randomToken", type = Accessor.Type.GETTER)
    public String getRandomToken()
    {
        return (String)getPersistenceContext().getPropertyValue("randomToken");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSUserRestrictionModel> getRestrictions()
    {
        return (Collection<CMSUserRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "retentionState", type = Accessor.Type.GETTER)
    public RetentionState getRetentionState()
    {
        return (RetentionState)getPersistenceContext().getPropertyValue("retentionState");
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
    public CurrencyModel getSessionCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("sessionCurrency");
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
    public LanguageModel getSessionLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("sessionLanguage");
    }


    @Accessor(qualifier = "themeForBackoffice", type = Accessor.Type.GETTER)
    public ThemeModel getThemeForBackoffice()
    {
        return (ThemeModel)getPersistenceContext().getPropertyValue("themeForBackoffice");
    }


    @Accessor(qualifier = "tokens", type = Accessor.Type.GETTER)
    public Collection<OAuthAccessTokenModel> getTokens()
    {
        return (Collection<OAuthAccessTokenModel>)getPersistenceContext().getPropertyValue("tokens");
    }


    @Accessor(qualifier = "userprofile", type = Accessor.Type.GETTER)
    public UserProfileModel getUserprofile()
    {
        return (UserProfileModel)getPersistenceContext().getPropertyValue("userprofile");
    }


    @Accessor(qualifier = "authorizedToUnlockPages", type = Accessor.Type.GETTER)
    public boolean isAuthorizedToUnlockPages()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("authorizedToUnlockPages"));
    }


    @Accessor(qualifier = "loginDisabled", type = Accessor.Type.GETTER)
    public boolean isLoginDisabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("loginDisabled"));
    }


    @Accessor(qualifier = "addresses", type = Accessor.Type.SETTER)
    public void setAddresses(Collection<AddressModel> value)
    {
        getPersistenceContext().setPropertyValue("addresses", value);
    }


    @Accessor(qualifier = "assignedComments", type = Accessor.Type.SETTER)
    public void setAssignedComments(List<CommentModel> value)
    {
        getPersistenceContext().setPropertyValue("assignedComments", value);
    }


    @Accessor(qualifier = "assignedQuotes", type = Accessor.Type.SETTER)
    public void setAssignedQuotes(Collection<QuoteModel> value)
    {
        getPersistenceContext().setPropertyValue("assignedQuotes", value);
    }


    @Accessor(qualifier = "authorizedToUnlockPages", type = Accessor.Type.SETTER)
    public void setAuthorizedToUnlockPages(boolean value)
    {
        getPersistenceContext().setPropertyValue("authorizedToUnlockPages", toObject(value));
    }


    @Accessor(qualifier = "avatar", type = Accessor.Type.SETTER)
    public void setAvatar(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("avatar", value);
    }


    @Accessor(qualifier = "carts", type = Accessor.Type.SETTER)
    public void setCarts(Collection<CartModel> value)
    {
        getPersistenceContext().setPropertyValue("carts", value);
    }


    @Accessor(qualifier = "collectionPks", type = Accessor.Type.SETTER)
    public void setCollectionPks(Collection<BackofficeObjectSpecialCollectionModel> value)
    {
        getPersistenceContext().setPropertyValue("collectionPks", value);
    }


    @Accessor(qualifier = "contactInfos", type = Accessor.Type.SETTER)
    public void setContactInfos(Collection<AbstractContactInfoModel> value)
    {
        getPersistenceContext().setPropertyValue("contactInfos", value);
    }


    @Accessor(qualifier = "createdComments", type = Accessor.Type.SETTER)
    public void setCreatedComments(List<AbstractCommentModel> value)
    {
        getPersistenceContext().setPropertyValue("createdComments", value);
    }


    @Accessor(qualifier = "customerReviews", type = Accessor.Type.SETTER)
    public void setCustomerReviews(Collection<CustomerReviewModel> value)
    {
        getPersistenceContext().setPropertyValue("customerReviews", value);
    }


    @Accessor(qualifier = "deactivationDate", type = Accessor.Type.SETTER)
    public void setDeactivationDate(Date value)
    {
        getPersistenceContext().setPropertyValue("deactivationDate", value);
    }


    @Accessor(qualifier = "defaultPaymentAddress", type = Accessor.Type.SETTER)
    public void setDefaultPaymentAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPaymentAddress", value);
    }


    @Accessor(qualifier = "defaultShipmentAddress", type = Accessor.Type.SETTER)
    public void setDefaultShipmentAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("defaultShipmentAddress", value);
    }


    @Accessor(qualifier = "encodedPassword", type = Accessor.Type.SETTER)
    public void setEncodedPassword(String value)
    {
        getPersistenceContext().setPropertyValue("encodedPassword", value);
    }


    @Accessor(qualifier = "europe1Discounts", type = Accessor.Type.SETTER)
    public void setEurope1Discounts(Collection<GlobalDiscountRowModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "europe1Discounts", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UDG(UserDiscountGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UDG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UPG(UserPriceGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UPG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UTG(UserTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UTG", value);
    }


    @Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.SETTER)
    public void setHmcLoginDisabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("hmcLoginDisabled", value);
    }


    @Accessor(qualifier = "lastLogin", type = Accessor.Type.SETTER)
    public void setLastLogin(Date value)
    {
        getPersistenceContext().setPropertyValue("lastLogin", value);
    }


    @Accessor(qualifier = "lockedPages", type = Accessor.Type.SETTER)
    public void setLockedPages(Collection<AbstractPageModel> value)
    {
        getPersistenceContext().setPropertyValue("lockedPages", value);
    }


    @Accessor(qualifier = "loginDisabled", type = Accessor.Type.SETTER)
    public void setLoginDisabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("loginDisabled", toObject(value));
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
    public void setOrders(Collection<OrderModel> value)
    {
        getPersistenceContext().setPropertyValue("orders", value);
    }


    @Accessor(qualifier = "ownEurope1Discounts", type = Accessor.Type.SETTER)
    public void setOwnEurope1Discounts(Collection<GlobalDiscountRowModel> value)
    {
        getPersistenceContext().setPropertyValue("ownEurope1Discounts", value);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "password", value);
    }


    @Accessor(qualifier = "passwordAnswer", type = Accessor.Type.SETTER)
    public void setPasswordAnswer(String value)
    {
        getPersistenceContext().setPropertyValue("passwordAnswer", value);
    }


    @Accessor(qualifier = "passwordEncoding", type = Accessor.Type.SETTER)
    public void setPasswordEncoding(String value)
    {
        getPersistenceContext().setPropertyValue("passwordEncoding", value);
    }


    @Accessor(qualifier = "passwordQuestion", type = Accessor.Type.SETTER)
    public void setPasswordQuestion(String value)
    {
        getPersistenceContext().setPropertyValue("passwordQuestion", value);
    }


    @Accessor(qualifier = "paymentInfos", type = Accessor.Type.SETTER)
    public void setPaymentInfos(Collection<PaymentInfoModel> value)
    {
        getPersistenceContext().setPropertyValue("paymentInfos", value);
    }


    @Accessor(qualifier = "quotes", type = Accessor.Type.SETTER)
    public void setQuotes(Collection<QuoteModel> value)
    {
        getPersistenceContext().setPropertyValue("quotes", value);
    }


    @Accessor(qualifier = "randomToken", type = Accessor.Type.SETTER)
    public void setRandomToken(String value)
    {
        getPersistenceContext().setPropertyValue("randomToken", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSUserRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "retentionState", type = Accessor.Type.SETTER)
    public void setRetentionState(RetentionState value)
    {
        getPersistenceContext().setPropertyValue("retentionState", value);
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
    public void setSessionCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("sessionCurrency", value);
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
    public void setSessionLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("sessionLanguage", value);
    }


    @Accessor(qualifier = "themeForBackoffice", type = Accessor.Type.SETTER)
    public void setThemeForBackoffice(ThemeModel value)
    {
        getPersistenceContext().setPropertyValue("themeForBackoffice", value);
    }


    @Accessor(qualifier = "tokens", type = Accessor.Type.SETTER)
    public void setTokens(Collection<OAuthAccessTokenModel> value)
    {
        getPersistenceContext().setPropertyValue("tokens", value);
    }


    @Accessor(qualifier = "userprofile", type = Accessor.Type.SETTER)
    public void setUserprofile(UserProfileModel value)
    {
        getPersistenceContext().setPropertyValue("userprofile", value);
    }
}
