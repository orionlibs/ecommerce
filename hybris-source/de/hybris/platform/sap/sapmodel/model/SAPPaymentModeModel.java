package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPPaymentModeModel extends ItemModel
{
    public static final String _TYPECODE = "SAPPaymentMode";
    public static final String _SAPCONFIGPAYMENTRELATION = "SapConfigPaymentRelation";
    public static final String SAPCONFIGURATION = "sapConfiguration";
    public static final String PAYMENTMODE = "paymentMode";
    public static final String PAYMENTNAME = "paymentName";
    public static final String PAYMENTVALUE = "paymentValue";


    public SAPPaymentModeModel()
    {
    }


    public SAPPaymentModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPaymentModeModel(PaymentModeModel _paymentMode, String _paymentName, String _paymentValue)
    {
        setPaymentMode(_paymentMode);
        setPaymentName(_paymentName);
        setPaymentValue(_paymentValue);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPPaymentModeModel(ItemModel _owner, PaymentModeModel _paymentMode, String _paymentName, String _paymentValue, SAPConfigurationModel _sapConfiguration)
    {
        setOwner(_owner);
        setPaymentMode(_paymentMode);
        setPaymentName(_paymentName);
        setPaymentValue(_paymentValue);
        setSapConfiguration(_sapConfiguration);
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
    public PaymentModeModel getPaymentMode()
    {
        return (PaymentModeModel)getPersistenceContext().getPropertyValue("paymentMode");
    }


    @Accessor(qualifier = "paymentName", type = Accessor.Type.GETTER)
    public String getPaymentName()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentName");
    }


    @Accessor(qualifier = "paymentValue", type = Accessor.Type.GETTER)
    public String getPaymentValue()
    {
        return (String)getPersistenceContext().getPropertyValue("paymentValue");
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.GETTER)
    public SAPConfigurationModel getSapConfiguration()
    {
        return (SAPConfigurationModel)getPersistenceContext().getPropertyValue("sapConfiguration");
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
    public void setPaymentMode(PaymentModeModel value)
    {
        getPersistenceContext().setPropertyValue("paymentMode", value);
    }


    @Accessor(qualifier = "paymentName", type = Accessor.Type.SETTER)
    public void setPaymentName(String value)
    {
        getPersistenceContext().setPropertyValue("paymentName", value);
    }


    @Accessor(qualifier = "paymentValue", type = Accessor.Type.SETTER)
    public void setPaymentValue(String value)
    {
        getPersistenceContext().setPropertyValue("paymentValue", value);
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.SETTER)
    public void setSapConfiguration(SAPConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapConfiguration", value);
    }
}
