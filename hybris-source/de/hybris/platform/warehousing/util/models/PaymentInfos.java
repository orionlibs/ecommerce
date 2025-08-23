package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.warehousing.util.builder.PaymentInfoModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class PaymentInfos
{
    private Users users;


    public PaymentInfoModel PaymentInfoForNancy(String code)
    {
        return PaymentInfoModelBuilder.aModel().withCode(code).withUser(getUsers().Nancy()).build();
    }


    public Users getUsers()
    {
        return this.users;
    }


    @Required
    public void setUsers(Users users)
    {
        this.users = users;
    }
}
