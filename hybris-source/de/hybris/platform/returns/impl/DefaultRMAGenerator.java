package de.hybris.platform.returns.impl;

import de.hybris.platform.returns.RMAGenerator;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRMAGenerator implements RMAGenerator
{
    private KeyGenerator keyGenerator;


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    public String generateRMA(ReturnRequestModel request)
    {
        return this.keyGenerator.generate().toString();
    }
}
