package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;
import de.hybris.platform.fraud.strategy.OrderFraudSymptomDetection;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DifferentAddressesSymptom extends AbstractOrderFraudSymptomDetection
{
    private boolean firstTimeOrderRule;
    private boolean countryOnly;
    private OrderFraudSymptomDetection firstOrderSymptomDetection;


    public OrderFraudSymptomDetection getFirstOrderSymptomDetection()
    {
        return this.firstOrderSymptomDetection;
    }


    @Required
    public void setFirstOrderSymptomDetection(OrderFraudSymptomDetection firstOrderSymptomDetection)
    {
        this.firstOrderSymptomDetection = firstOrderSymptomDetection;
    }


    public boolean isFirstTimeOrderRule()
    {
        return this.firstTimeOrderRule;
    }


    public void setFirstTimeOrderRule(boolean firstTimeOrderRule)
    {
        this.firstTimeOrderRule = firstTimeOrderRule;
    }


    public boolean isCountryOnly()
    {
        return this.countryOnly;
    }


    public void setCountryOnly(boolean countryOnly)
    {
        this.countryOnly = countryOnly;
    }


    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        AddressModel shippingAddress = order.getDeliveryAddress();
        AddressModel billingAddress = order.getPaymentAddress();
        boolean addresesDifferent = !verifyAddresses(shippingAddress, billingAddress);
        if(addresesDifferent && (!this.firstTimeOrderRule || fraudResponse
                        .getScore(((AbstractOrderFraudSymptomDetection)
                                        getFirstOrderSymptomDetection()).getSymptomName()) > 0.0D))
        {
            fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), getIncrement()));
        }
        else
        {
            fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), 0.0D));
        }
        return fraudResponse;
    }


    protected boolean verifyAddresses(AddressModel shipping, AddressModel delivery)
    {
        if(shipping == null || delivery == null)
        {
            return true;
        }
        if(Objects.nonNull(shipping.getCountry()) && !shipping.getCountry().equals(delivery.getCountry()))
        {
            return false;
        }
        if(!this.countryOnly)
        {
            return verifyAddressesSame(shipping, delivery);
        }
        return true;
    }


    protected boolean verifyAddressesSame(AddressModel shipping, AddressModel delivery)
    {
        return (!isNotEqual(shipping.getTown(), delivery.getTown()) && !isNotEqual(shipping.getStreetname(), delivery
                        .getStreetname()) && !isNotEqual(shipping.getBuilding(), delivery.getBuilding()));
    }


    protected boolean isNotEqual(String input, String expected)
    {
        return (Objects.nonNull(input) && !input.equals(expected));
    }
}
