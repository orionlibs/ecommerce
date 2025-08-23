package de.hybris.platform.masterserver.impl;

import com.google.common.base.Preconditions;
import com.hybris.encryption.asymmetric.AsymmetricEncryptor;
import com.hybris.encryption.symmetric.SymmetricEncryptor;
import de.hybris.platform.masterserver.StatisticsPayloadEncryptor;
import java.security.interfaces.RSAPublicKey;
import org.apache.commons.lang.RandomStringUtils;

public class DefaultStatisticsPayloadEncryptor implements StatisticsPayloadEncryptor
{
    private final SymmetricEncryptor aesEncryptor;
    private final AsymmetricEncryptor rsaEncryptor;
    private final RSAPublicKey publicKey;


    public DefaultStatisticsPayloadEncryptor(SymmetricEncryptor aesEncryptor, AsymmetricEncryptor rsaEncryptor, RSAPublicKey publicKey)
    {
        this.aesEncryptor = aesEncryptor;
        this.rsaEncryptor = rsaEncryptor;
        this.publicKey = publicKey;
    }


    public StatisticsPayload encrypt(String statisticsData, String homeURL)
    {
        Preconditions.checkArgument((statisticsData != null), "statisticsData for encryption required");
        Preconditions.checkArgument((homeURL != null), "homeURL is required");
        String password = RandomStringUtils.randomAlphanumeric(15);
        String cryptedStatData = this.aesEncryptor.encrypt(statisticsData, password);
        String cryptedPassword = this.rsaEncryptor.encrypt(password, this.publicKey);
        return new StatisticsPayload(cryptedPassword, homeURL, cryptedStatData);
    }
}
