package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserConstants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CustomerRuleParameterValueMapper implements RuleParameterValueMapper<CustomerModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomerRuleParameterValueMapper.class);
    private UserService userService;
    private ModelService modelService;
    private Function<UserModel, String> userIdentifierProvider;


    public String toString(CustomerModel customer)
    {
        ServicesUtil.validateParameterNotNull(customer, "Object cannot be null");
        return getUserIdentifierProvider().apply(customer);
    }


    public CustomerModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        return lookupCustomerByPK(value)
                        .orElseGet(() -> (CustomerModel)lookupCustomerByUID(value).<Throwable>orElseThrow(()));
    }


    protected Optional<CustomerModel> lookupCustomerByPK(String value)
    {
        try
        {
            return Optional.ofNullable((CustomerModel)getModelService().get(PK.parse(value)));
        }
        catch(de.hybris.platform.core.PK.PKException | de.hybris.platform.servicelayer.exceptions.ModelLoadingException exc)
        {
            LOG.debug("Exception caught. Return Optional.empty()", exc);
            return Optional.empty();
        }
    }


    protected Optional<CustomerModel> lookupCustomerByUID(String value)
    {
        try
        {
            return Optional.ofNullable(UserConstants.ANONYMOUS_CUSTOMER_UID.equals(value) ? getUserService().getAnonymousUser() :
                            (CustomerModel)getUserService().getUserForUID(value, CustomerModel.class));
        }
        catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.ClassMismatchException exc)
        {
            LOG.debug("Exception caught. Return Optional.empty()", (Throwable)exc);
            return Optional.empty();
        }
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected Function<UserModel, String> getUserIdentifierProvider()
    {
        return this.userIdentifierProvider;
    }


    @Required
    public void setUserIdentifierProvider(Function<UserModel, String> userIdentifierProvider)
    {
        this.userIdentifierProvider = userIdentifierProvider;
    }
}
