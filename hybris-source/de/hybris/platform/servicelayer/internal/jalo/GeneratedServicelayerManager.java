package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.audit.AuditReportConfig;
import de.hybris.platform.hmc.jalo.SavedValueEntry;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.hmc.jalo.UserProfile;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.config.ConfigProxyItem;
import de.hybris.platform.jalo.cors.CorsConfigurationProperty;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.initialization.SystemSetupAudit;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.DerivedMedia;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.media.MediaContext;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaFormatMapping;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.Quote;
import de.hybris.platform.jalo.order.QuoteEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.test.TestEmployee;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jalo.test.TestItem3;
import de.hybris.platform.jalo.test.TestUserGroup;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ExpressionAttributeDescriptor;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.type.ViewAttributeDescriptor;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.jalo.user.AbstractContactInfo;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.BruteForceLoginAttempts;
import de.hybris.platform.jalo.user.BruteForceLoginDisabledAudit;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.PhoneContactInfo;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserPasswordChangeAudit;
import de.hybris.platform.jalo.web.StoredHttpSession;
import de.hybris.platform.servicelayer.internal.jalo.order.InMemoryCart;
import de.hybris.platform.servicelayer.internal.jalo.order.InMemoryCartEntry;
import de.hybris.platform.util.ConfigAttributeDescriptor;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedServicelayerManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public AbstractContactInfo createAbstractContactInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ABSTRACTCONTACTINFO);
            return (AbstractContactInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AbstractContactInfo : " + e.getMessage(), 0);
        }
    }


    public AbstractContactInfo createAbstractContactInfo(Map attributeValues)
    {
        return createAbstractContactInfo(getSession().getSessionContext(), attributeValues);
    }


    public Address createAddress(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ADDRESS);
            return (Address)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Address : " + e.getMessage(), 0);
        }
    }


    public Address createAddress(Map attributeValues)
    {
        return createAddress(getSession().getSessionContext(), attributeValues);
    }


    public PaymentInfo createAdvancePaymentInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ADVANCEPAYMENTINFO);
            return (PaymentInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AdvancePaymentInfo : " + e.getMessage(), 0);
        }
    }


    public PaymentInfo createAdvancePaymentInfo(Map attributeValues)
    {
        return createAdvancePaymentInfo(getSession().getSessionContext(), attributeValues);
    }


    public AtomicType createAtomicType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ATOMICTYPE);
            return (AtomicType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AtomicType : " + e.getMessage(), 0);
        }
    }


    public AtomicType createAtomicType(Map attributeValues)
    {
        return createAtomicType(getSession().getSessionContext(), attributeValues);
    }


    public AttributeDescriptor createAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ATTRIBUTEDESCRIPTOR);
            return (AttributeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AttributeDescriptor : " + e.getMessage(), 0);
        }
    }


    public AttributeDescriptor createAttributeDescriptor(Map attributeValues)
    {
        return createAttributeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public AuditReportConfig createAuditReportConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.AUDITREPORTCONFIG);
            return (AuditReportConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AuditReportConfig : " + e.getMessage(), 0);
        }
    }


    public AuditReportConfig createAuditReportConfig(Map attributeValues)
    {
        return createAuditReportConfig(getSession().getSessionContext(), attributeValues);
    }


    public BruteForceLoginAttempts createBruteForceLoginAttempts(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.BRUTEFORCELOGINATTEMPTS);
            return (BruteForceLoginAttempts)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BruteForceLoginAttempts : " + e.getMessage(), 0);
        }
    }


    public BruteForceLoginAttempts createBruteForceLoginAttempts(Map attributeValues)
    {
        return createBruteForceLoginAttempts(getSession().getSessionContext(), attributeValues);
    }


    public BruteForceLoginDisabledAudit createBruteForceLoginDisabledAudit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.BRUTEFORCELOGINDISABLEDAUDIT);
            return (BruteForceLoginDisabledAudit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BruteForceLoginDisabledAudit : " + e.getMessage(), 0);
        }
    }


    public BruteForceLoginDisabledAudit createBruteForceLoginDisabledAudit(Map attributeValues)
    {
        return createBruteForceLoginDisabledAudit(getSession().getSessionContext(), attributeValues);
    }


    public Cart createCart(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CART);
            return (Cart)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Cart : " + e.getMessage(), 0);
        }
    }


    public Cart createCart(Map attributeValues)
    {
        return createCart(getSession().getSessionContext(), attributeValues);
    }


    public CartEntry createCartEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CARTENTRY);
            return (CartEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CartEntry : " + e.getMessage(), 0);
        }
    }


    public CartEntry createCartEntry(Map attributeValues)
    {
        return createCartEntry(getSession().getSessionContext(), attributeValues);
    }


    public CollectionType createCollectionType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.COLLECTIONTYPE);
            return (CollectionType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CollectionType : " + e.getMessage(), 0);
        }
    }


    public CollectionType createCollectionType(Map attributeValues)
    {
        return createCollectionType(getSession().getSessionContext(), attributeValues);
    }


    public ComposedType createComposedType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.COMPOSEDTYPE);
            return (ComposedType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ComposedType : " + e.getMessage(), 0);
        }
    }


    public ComposedType createComposedType(Map attributeValues)
    {
        return createComposedType(getSession().getSessionContext(), attributeValues);
    }


    public ConfigAttributeDescriptor createConfigAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CONFIGATTRIBUTEDESCRIPTOR);
            return (ConfigAttributeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConfigAttributeDescriptor : " + e.getMessage(), 0);
        }
    }


    public ConfigAttributeDescriptor createConfigAttributeDescriptor(Map attributeValues)
    {
        return createConfigAttributeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public ConfigProxyItem createConfigProxyItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CONFIGPROXYITEM);
            return (ConfigProxyItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConfigProxyItem : " + e.getMessage(), 0);
        }
    }


    public ConfigProxyItem createConfigProxyItem(Map attributeValues)
    {
        return createConfigProxyItem(getSession().getSessionContext(), attributeValues);
    }


    public ComposedType createConfigProxyMetaType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CONFIGPROXYMETATYPE);
            return (ComposedType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConfigProxyMetaType : " + e.getMessage(), 0);
        }
    }


    public ComposedType createConfigProxyMetaType(Map attributeValues)
    {
        return createConfigProxyMetaType(getSession().getSessionContext(), attributeValues);
    }


    public CorsConfigurationProperty createCorsConfigurationProperty(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CORSCONFIGURATIONPROPERTY);
            return (CorsConfigurationProperty)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CorsConfigurationProperty : " + e.getMessage(), 0);
        }
    }


    public CorsConfigurationProperty createCorsConfigurationProperty(Map attributeValues)
    {
        return createCorsConfigurationProperty(getSession().getSessionContext(), attributeValues);
    }


    public Country createCountry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.COUNTRY);
            return (Country)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Country : " + e.getMessage(), 0);
        }
    }


    public Country createCountry(Map attributeValues)
    {
        return createCountry(getSession().getSessionContext(), attributeValues);
    }


    public PaymentInfo createCreditCardPaymentInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CREDITCARDPAYMENTINFO);
            return (PaymentInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CreditCardPaymentInfo : " + e.getMessage(), 0);
        }
    }


    public PaymentInfo createCreditCardPaymentInfo(Map attributeValues)
    {
        return createCreditCardPaymentInfo(getSession().getSessionContext(), attributeValues);
    }


    public Currency createCurrency(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CURRENCY);
            return (Currency)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Currency : " + e.getMessage(), 0);
        }
    }


    public Currency createCurrency(Map attributeValues)
    {
        return createCurrency(getSession().getSessionContext(), attributeValues);
    }


    public Customer createCustomer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.CUSTOMER);
            return (Customer)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Customer : " + e.getMessage(), 0);
        }
    }


    public Customer createCustomer(Map attributeValues)
    {
        return createCustomer(getSession().getSessionContext(), attributeValues);
    }


    public PaymentInfo createDebitPaymentInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.DEBITPAYMENTINFO);
            return (PaymentInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DebitPaymentInfo : " + e.getMessage(), 0);
        }
    }


    public PaymentInfo createDebitPaymentInfo(Map attributeValues)
    {
        return createDebitPaymentInfo(getSession().getSessionContext(), attributeValues);
    }


    public DeliveryMode createDeliveryMode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.DELIVERYMODE);
            return (DeliveryMode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DeliveryMode : " + e.getMessage(), 0);
        }
    }


    public DeliveryMode createDeliveryMode(Map attributeValues)
    {
        return createDeliveryMode(getSession().getSessionContext(), attributeValues);
    }


    public DerivedMedia createDerivedMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.DERIVEDMEDIA);
            return (DerivedMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DerivedMedia : " + e.getMessage(), 0);
        }
    }


    public DerivedMedia createDerivedMedia(Map attributeValues)
    {
        return createDerivedMedia(getSession().getSessionContext(), attributeValues);
    }


    public Discount createDiscount(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.DISCOUNT);
            return (Discount)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Discount : " + e.getMessage(), 0);
        }
    }


    public Discount createDiscount(Map attributeValues)
    {
        return createDiscount(getSession().getSessionContext(), attributeValues);
    }


    public Employee createEmployee(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.EMPLOYEE);
            return (Employee)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Employee : " + e.getMessage(), 0);
        }
    }


    public Employee createEmployee(Map attributeValues)
    {
        return createEmployee(getSession().getSessionContext(), attributeValues);
    }


    public EnumerationType createEnumerationMetaType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ENUMERATIONMETATYPE);
            return (EnumerationType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating EnumerationMetaType : " + e.getMessage(), 0);
        }
    }


    public EnumerationType createEnumerationMetaType(Map attributeValues)
    {
        return createEnumerationMetaType(getSession().getSessionContext(), attributeValues);
    }


    public EnumerationValue createEnumerationValue(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ENUMERATIONVALUE);
            return (EnumerationValue)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating EnumerationValue : " + e.getMessage(), 0);
        }
    }


    public EnumerationValue createEnumerationValue(Map attributeValues)
    {
        return createEnumerationValue(getSession().getSessionContext(), attributeValues);
    }


    public ExpressionAttributeDescriptor createExpressionAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.EXPRESSIONATTRIBUTEDESCRIPTOR);
            return (ExpressionAttributeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ExpressionAttributeDescriptor : " + e.getMessage(), 0);
        }
    }


    public ExpressionAttributeDescriptor createExpressionAttributeDescriptor(Map attributeValues)
    {
        return createExpressionAttributeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public GenericItem createGenericItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.GENERICITEM);
            return (GenericItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GenericItem : " + e.getMessage(), 0);
        }
    }


    public GenericItem createGenericItem(Map attributeValues)
    {
        return createGenericItem(getSession().getSessionContext(), attributeValues);
    }


    public GenericItem createGenericTestItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.GENERICTESTITEM);
            return (GenericItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GenericTestItem : " + e.getMessage(), 0);
        }
    }


    public GenericItem createGenericTestItem(Map attributeValues)
    {
        return createGenericTestItem(getSession().getSessionContext(), attributeValues);
    }


    public IndexTestItem createIndexTestItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.INDEXTESTITEM);
            return (IndexTestItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating IndexTestItem : " + e.getMessage(), 0);
        }
    }


    public IndexTestItem createIndexTestItem(Map attributeValues)
    {
        return createIndexTestItem(getSession().getSessionContext(), attributeValues);
    }


    public InMemoryCart createInMemoryCart(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.INMEMORYCART);
            return (InMemoryCart)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating InMemoryCart : " + e.getMessage(), 0);
        }
    }


    public InMemoryCart createInMemoryCart(Map attributeValues)
    {
        return createInMemoryCart(getSession().getSessionContext(), attributeValues);
    }


    public InMemoryCartEntry createInMemoryCartEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.INMEMORYCARTENTRY);
            return (InMemoryCartEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating InMemoryCartEntry : " + e.getMessage(), 0);
        }
    }


    public InMemoryCartEntry createInMemoryCartEntry(Map attributeValues)
    {
        return createInMemoryCartEntry(getSession().getSessionContext(), attributeValues);
    }


    public PaymentInfo createInvoicePaymentInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.INVOICEPAYMENTINFO);
            return (PaymentInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating InvoicePaymentInfo : " + e.getMessage(), 0);
        }
    }


    public PaymentInfo createInvoicePaymentInfo(Map attributeValues)
    {
        return createInvoicePaymentInfo(getSession().getSessionContext(), attributeValues);
    }


    public Language createLanguage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.LANGUAGE);
            return (Language)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Language : " + e.getMessage(), 0);
        }
    }


    public Language createLanguage(Map attributeValues)
    {
        return createLanguage(getSession().getSessionContext(), attributeValues);
    }


    public Link createLink(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.LINK);
            return (Link)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Link : " + e.getMessage(), 0);
        }
    }


    public Link createLink(Map attributeValues)
    {
        return createLink(getSession().getSessionContext(), attributeValues);
    }


    public MapType createMapType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MAPTYPE);
            return (MapType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MapType : " + e.getMessage(), 0);
        }
    }


    public MapType createMapType(Map attributeValues)
    {
        return createMapType(getSession().getSessionContext(), attributeValues);
    }


    public Media createMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIA);
            return (Media)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Media : " + e.getMessage(), 0);
        }
    }


    public Media createMedia(Map attributeValues)
    {
        return createMedia(getSession().getSessionContext(), attributeValues);
    }


    public MediaContainer createMediaContainer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIACONTAINER);
            return (MediaContainer)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaContainer : " + e.getMessage(), 0);
        }
    }


    public MediaContainer createMediaContainer(Map attributeValues)
    {
        return createMediaContainer(getSession().getSessionContext(), attributeValues);
    }


    public MediaContext createMediaContext(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIACONTEXT);
            return (MediaContext)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaContext : " + e.getMessage(), 0);
        }
    }


    public MediaContext createMediaContext(Map attributeValues)
    {
        return createMediaContext(getSession().getSessionContext(), attributeValues);
    }


    public MediaFolder createMediaFolder(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIAFOLDER);
            return (MediaFolder)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFolder : " + e.getMessage(), 0);
        }
    }


    public MediaFolder createMediaFolder(Map attributeValues)
    {
        return createMediaFolder(getSession().getSessionContext(), attributeValues);
    }


    public MediaFormat createMediaFormat(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIAFORMAT);
            return (MediaFormat)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFormat : " + e.getMessage(), 0);
        }
    }


    public MediaFormat createMediaFormat(Map attributeValues)
    {
        return createMediaFormat(getSession().getSessionContext(), attributeValues);
    }


    public MediaFormatMapping createMediaFormatMapping(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.MEDIAFORMATMAPPING);
            return (MediaFormatMapping)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFormatMapping : " + e.getMessage(), 0);
        }
    }


    public MediaFormatMapping createMediaFormatMapping(Map attributeValues)
    {
        return createMediaFormatMapping(getSession().getSessionContext(), attributeValues);
    }


    public Order createOrder(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ORDER);
            return (Order)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Order : " + e.getMessage(), 0);
        }
    }


    public Order createOrder(Map attributeValues)
    {
        return createOrder(getSession().getSessionContext(), attributeValues);
    }


    public OrderEntry createOrderEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.ORDERENTRY);
            return (OrderEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderEntry : " + e.getMessage(), 0);
        }
    }


    public OrderEntry createOrderEntry(Map attributeValues)
    {
        return createOrderEntry(getSession().getSessionContext(), attributeValues);
    }


    public PaymentInfo createPaymentInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.PAYMENTINFO);
            return (PaymentInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PaymentInfo : " + e.getMessage(), 0);
        }
    }


    public PaymentInfo createPaymentInfo(Map attributeValues)
    {
        return createPaymentInfo(getSession().getSessionContext(), attributeValues);
    }


    public PaymentMode createPaymentMode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.PAYMENTMODE);
            return (PaymentMode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PaymentMode : " + e.getMessage(), 0);
        }
    }


    public PaymentMode createPaymentMode(Map attributeValues)
    {
        return createPaymentMode(getSession().getSessionContext(), attributeValues);
    }


    public PhoneContactInfo createPhoneContactInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.PHONECONTACTINFO);
            return (PhoneContactInfo)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PhoneContactInfo : " + e.getMessage(), 0);
        }
    }


    public PhoneContactInfo createPhoneContactInfo(Map attributeValues)
    {
        return createPhoneContactInfo(getSession().getSessionContext(), attributeValues);
    }


    public Product createProduct(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.PRODUCT);
            return (Product)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Product : " + e.getMessage(), 0);
        }
    }


    public Product createProduct(Map attributeValues)
    {
        return createProduct(getSession().getSessionContext(), attributeValues);
    }


    public Link createProductMediaLink(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.PRODUCTMEDIALINK);
            return (Link)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductMediaLink : " + e.getMessage(), 0);
        }
    }


    public Link createProductMediaLink(Map attributeValues)
    {
        return createProductMediaLink(getSession().getSessionContext(), attributeValues);
    }


    public Quote createQuote(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.QUOTE);
            return (Quote)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Quote : " + e.getMessage(), 0);
        }
    }


    public Quote createQuote(Map attributeValues)
    {
        return createQuote(getSession().getSessionContext(), attributeValues);
    }


    public QuoteEntry createQuoteEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.QUOTEENTRY);
            return (QuoteEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating QuoteEntry : " + e.getMessage(), 0);
        }
    }


    public QuoteEntry createQuoteEntry(Map attributeValues)
    {
        return createQuoteEntry(getSession().getSessionContext(), attributeValues);
    }


    public Region createRegion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.REGION);
            return (Region)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Region : " + e.getMessage(), 0);
        }
    }


    public Region createRegion(Map attributeValues)
    {
        return createRegion(getSession().getSessionContext(), attributeValues);
    }


    public RelationDescriptor createRelationDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.RELATIONDESCRIPTOR);
            return (RelationDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RelationDescriptor : " + e.getMessage(), 0);
        }
    }


    public RelationDescriptor createRelationDescriptor(Map attributeValues)
    {
        return createRelationDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public RelationType createRelationMetaType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.RELATIONMETATYPE);
            return (RelationType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RelationMetaType : " + e.getMessage(), 0);
        }
    }


    public RelationType createRelationMetaType(Map attributeValues)
    {
        return createRelationMetaType(getSession().getSessionContext(), attributeValues);
    }


    public SavedQuery createSavedQuery(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.SAVEDQUERY);
            return (SavedQuery)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SavedQuery : " + e.getMessage(), 0);
        }
    }


    public SavedQuery createSavedQuery(Map attributeValues)
    {
        return createSavedQuery(getSession().getSessionContext(), attributeValues);
    }


    public SavedValueEntry createSavedValueEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.SAVEDVALUEENTRY);
            return (SavedValueEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SavedValueEntry : " + e.getMessage(), 0);
        }
    }


    public SavedValueEntry createSavedValueEntry(Map attributeValues)
    {
        return createSavedValueEntry(getSession().getSessionContext(), attributeValues);
    }


    public SavedValues createSavedValues(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.SAVEDVALUES);
            return (SavedValues)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SavedValues : " + e.getMessage(), 0);
        }
    }


    public SavedValues createSavedValues(Map attributeValues)
    {
        return createSavedValues(getSession().getSessionContext(), attributeValues);
    }


    public SearchRestriction createSearchRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.SEARCHRESTRICTION);
            return (SearchRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SearchRestriction : " + e.getMessage(), 0);
        }
    }


    public SearchRestriction createSearchRestriction(Map attributeValues)
    {
        return createSearchRestriction(getSession().getSessionContext(), attributeValues);
    }


    public StoredHttpSession createStoredHttpSession(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.STOREDHTTPSESSION);
            return (StoredHttpSession)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating StoredHttpSession : " + e.getMessage(), 0);
        }
    }


    public StoredHttpSession createStoredHttpSession(Map attributeValues)
    {
        return createStoredHttpSession(getSession().getSessionContext(), attributeValues);
    }


    public SystemSetupAudit createSystemSetupAudit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.SYSTEMSETUPAUDIT);
            return (SystemSetupAudit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SystemSetupAudit : " + e.getMessage(), 0);
        }
    }


    public SystemSetupAudit createSystemSetupAudit(Map attributeValues)
    {
        return createSystemSetupAudit(getSession().getSessionContext(), attributeValues);
    }


    public Tax createTax(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TAX);
            return (Tax)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Tax : " + e.getMessage(), 0);
        }
    }


    public Tax createTax(Map attributeValues)
    {
        return createTax(getSession().getSessionContext(), attributeValues);
    }


    public TestEmployee createTestEmployee(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TESTEMPLOYEE);
            return (TestEmployee)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TestEmployee : " + e.getMessage(), 0);
        }
    }


    public TestEmployee createTestEmployee(Map attributeValues)
    {
        return createTestEmployee(getSession().getSessionContext(), attributeValues);
    }


    public TestItem createTestItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TESTITEM);
            return (TestItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TestItem : " + e.getMessage(), 0);
        }
    }


    public TestItem createTestItem(Map attributeValues)
    {
        return createTestItem(getSession().getSessionContext(), attributeValues);
    }


    public TestItem createTestItemType2(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TESTITEMTYPE2);
            return (TestItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TestItemType2 : " + e.getMessage(), 0);
        }
    }


    public TestItem createTestItemType2(Map attributeValues)
    {
        return createTestItemType2(getSession().getSessionContext(), attributeValues);
    }


    public TestItem3 createTestItemType3(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TESTITEMTYPE3);
            return (TestItem3)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TestItemType3 : " + e.getMessage(), 0);
        }
    }


    public TestItem3 createTestItemType3(Map attributeValues)
    {
        return createTestItemType3(getSession().getSessionContext(), attributeValues);
    }


    public TestUserGroup createTestUserGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TESTUSERGROUP);
            return (TestUserGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TestUserGroup : " + e.getMessage(), 0);
        }
    }


    public TestUserGroup createTestUserGroup(Map attributeValues)
    {
        return createTestUserGroup(getSession().getSessionContext(), attributeValues);
    }


    public Title createTitle(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.TITLE);
            return (Title)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Title : " + e.getMessage(), 0);
        }
    }


    public Title createTitle(Map attributeValues)
    {
        return createTitle(getSession().getSessionContext(), attributeValues);
    }


    public Unit createUnit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.UNIT);
            return (Unit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Unit : " + e.getMessage(), 0);
        }
    }


    public Unit createUnit(Map attributeValues)
    {
        return createUnit(getSession().getSessionContext(), attributeValues);
    }


    public User createUser(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.USER);
            return (User)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating User : " + e.getMessage(), 0);
        }
    }


    public User createUser(Map attributeValues)
    {
        return createUser(getSession().getSessionContext(), attributeValues);
    }


    public UserGroup createUserGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.USERGROUP);
            return (UserGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating UserGroup : " + e.getMessage(), 0);
        }
    }


    public UserGroup createUserGroup(Map attributeValues)
    {
        return createUserGroup(getSession().getSessionContext(), attributeValues);
    }


    public UserPasswordChangeAudit createUserPasswordChangeAudit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.USERPASSWORDCHANGEAUDIT);
            return (UserPasswordChangeAudit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating UserPasswordChangeAudit : " + e.getMessage(), 0);
        }
    }


    public UserPasswordChangeAudit createUserPasswordChangeAudit(Map attributeValues)
    {
        return createUserPasswordChangeAudit(getSession().getSessionContext(), attributeValues);
    }


    public UserProfile createUserProfile(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.USERPROFILE);
            return (UserProfile)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating UserProfile : " + e.getMessage(), 0);
        }
    }


    public UserProfile createUserProfile(Map attributeValues)
    {
        return createUserProfile(getSession().getSessionContext(), attributeValues);
    }


    public UserRight createUserRight(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.USERRIGHT);
            return (UserRight)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating UserRight : " + e.getMessage(), 0);
        }
    }


    public UserRight createUserRight(Map attributeValues)
    {
        return createUserRight(getSession().getSessionContext(), attributeValues);
    }


    public ViewAttributeDescriptor createViewAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.VIEWATTRIBUTEDESCRIPTOR);
            return (ViewAttributeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ViewAttributeDescriptor : " + e.getMessage(), 0);
        }
    }


    public ViewAttributeDescriptor createViewAttributeDescriptor(Map attributeValues)
    {
        return createViewAttributeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public ViewType createViewType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCoreConstants.TC.VIEWTYPE);
            return (ViewType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ViewType : " + e.getMessage(), 0);
        }
    }


    public ViewType createViewType(Map attributeValues)
    {
        return createViewType(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "core";
    }
}
