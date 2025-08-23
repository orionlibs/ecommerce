package de.hybris.platform.payment.strategy.impl;

import de.hybris.platform.payment.strategy.TransactionCodeGenerator;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMerchantTransactionCodeGenerator implements TransactionCodeGenerator
{
    public static final String DELIMITER = "-";
    private KeyGenerator keyGenerator;


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    public String generateCode(String base)
    {
        if(StringUtils.isBlank(base))
        {
            return (String)this.keyGenerator.generate();
        }
        StringBuilder builder = new StringBuilder(base);
        builder.append("-");
        builder.append((String)this.keyGenerator.generate());
        return builder.toString();
    }
}
