package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;

public class PaymentInfoModelBuilder
{
    private final PaymentInfoModel model = new PaymentInfoModel();


    private PaymentInfoModel getModel()
    {
        return this.model;
    }


    public static PaymentInfoModelBuilder aModel()
    {
        return new PaymentInfoModelBuilder();
    }


    public PaymentInfoModel build()
    {
        return getModel();
    }


    public PaymentInfoModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public PaymentInfoModelBuilder withUser(UserModel user)
    {
        getModel().setUser(user);
        return this;
    }
}
