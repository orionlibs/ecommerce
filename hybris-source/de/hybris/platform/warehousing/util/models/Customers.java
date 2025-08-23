package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.warehousing.util.builder.CustomerModelBuilder;
import de.hybris.platform.warehousing.util.dao.impl.CustomerDaoImpl;
import org.springframework.beans.factory.annotation.Required;

public class Customers extends AbstractItems<CustomerModel>
{
    public static final String UID_POLO = "polo@polo.com";
    public static final String NAME_POLO = "polo";
    private CustomerDaoImpl omsCustomerDao;


    public UserModel polo()
    {
        return getOrCreateUser("polo@polo.com", "polo");
    }


    protected UserModel getOrCreateUser(String uid, String name)
    {
        return (UserModel)getOrSaveAndReturn(() -> (CustomerModel)getOmsCustomerDao().getByCode(uid), () -> CustomerModelBuilder.aModel().withUid(uid).withName(name).build());
    }


    public CustomerDaoImpl getOmsCustomerDao()
    {
        return this.omsCustomerDao;
    }


    @Required
    public void setOmsCustomerDao(CustomerDaoImpl omsCustomerDao)
    {
        this.omsCustomerDao = omsCustomerDao;
    }
}
