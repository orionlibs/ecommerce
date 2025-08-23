package de.hybris.platform.fraud.impl.mock;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.impl.AbstractFraudServiceProvider;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.util.Utilities;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.NotImplementedException;

public class CommercialFraudMockService extends AbstractFraudServiceProvider
{
    private static final Set<String> bannedDomains = new HashSet<>();

    static
    {
        bannedDomains.add("foo.pl");
        bannedDomains.add("foo.de");
        bannedDomains.add("foo.com");
    }

    private static final Set<String> bannedIPs = new HashSet<>();

    static
    {
        bannedIPs.add("212.60.65.173");
        bannedIPs.add("196.46.71.251");
        bannedIPs.add("202.105.37.196");
        bannedIPs.add("41.218.203.86");
        bannedIPs.add("81.91.228.100");
        bannedIPs.add("74.125.16.3");
        bannedIPs.add("41.191.68.197");
        bannedIPs.add("41.189.4.251");
        bannedIPs.add("41.202.76.10");
        bannedIPs.add("41.207.214.7");
        bannedIPs.add("196.207.228.102");
        bannedIPs.add("41.215.160.133");
        bannedIPs.add("41.189.35.234");
        bannedIPs.add("217.117.5.118");
        bannedIPs.add("41.207.212.232");
        bannedIPs.add("68.68.107.24");
        bannedIPs.add("112.110.109.247");
        bannedIPs.add("41.216.40.59");
        bannedIPs.add("41.207.25.103");
        bannedIPs.add("41.207.162.2");
    }

    private static final Set<String> stolenCards = new HashSet<>();

    static
    {
        stolenCards.add("0000-0000-00000");
        stolenCards.add("1111-1111-11111");
    }

    private static final Map<String, String> fakeGeolocation = new HashMap<>();
    private static final int DEFAULT_INCREMENT = 100;

    static
    {
        fakeGeolocation.put("USA", "83.13.130.42");
        fakeGeolocation.put("GER", "83.13.130.42");
    }

    public int isFreeEmailService(String email)
    {
        if(null == email)
        {
            throw new JaloInvalidParameterException("Email must not be null", 0);
        }
        return email.toLowerCase(Utilities.getDefaultLocale()).matches(".*free.*") ? 100 : 0;
    }


    public int isBannedDomain(String domain)
    {
        if(null == domain)
        {
            throw new JaloInvalidParameterException("Domain must not be null", 0);
        }
        return bannedDomains.contains(domain) ? 100 : 0;
    }


    public int isBannedIP(String ipAddress)
    {
        if(null == ipAddress)
        {
            throw new JaloInvalidParameterException("IP address must not be null", 0);
        }
        return bannedIPs.contains(ipAddress) ? 100 : 0;
    }


    public int isFraudulentCreditCard(String hashedNumber)
    {
        return stolenCards.contains(hashedNumber) ? 100 : 0;
    }


    public int isFraudulentEmailAddress(String email)
    {
        return (email.hashCode() % 4 == 0) ? 100 : 0;
    }


    public int isFraudulentGeolocation(String ipAddress, String country, String zipCode, String state)
    {
        return ((String)fakeGeolocation.get(country)).equals(ipAddress) ? 100 : 0;
    }


    protected Map<String, String> decomposeOrderModel(AbstractOrderModel order)
    {
        Map<String, String> result = new HashMap<>();
        AddressModel address = order.getDeliveryAddress();
        if(Objects.nonNull(address))
        {
            if(Objects.nonNull(address.getCountry()))
            {
                result.put("country", address.getCountry().getIsocode());
            }
            if(Objects.nonNull(address.getTown()))
            {
                result.put("town", address.getTown());
            }
            if(Objects.nonNull(address.getPostalcode()))
            {
                result.put("zipCode", address.getPostalcode());
            }
            if(Objects.nonNull(address.getDistrict()))
            {
                result.put("state", address.getDistrict());
            }
            if(Objects.nonNull(address.getEmail()))
            {
                result.put("email", address.getEmail());
            }
        }
        return result;
    }


    public FraudServiceResponse recognizeOrderFraudSymptoms(AbstractOrderModel order)
    {
        Map<String, Double> serviceResponse = doAll(decomposeOrderModel(order));
        FraudServiceResponse response = new FraudServiceResponse(null, getProviderName());
        for(Map.Entry<String, Double> entry : serviceResponse.entrySet())
        {
            response.addSymptom(new FraudSymptom(entry.getKey(), ((Double)serviceResponse.get(entry.getKey())).doubleValue()));
        }
        return response;
    }


    protected Map<String, Double> doAll(Map<String, String> parameters)
    {
        Map<String, Double> result = new HashMap<>();
        if(parameters.containsKey("email"))
        {
            result.put("Free email service",
                            Double.valueOf(isFreeEmailService(parameters.get("email"))));
            result.put("Suspicious email", Double.valueOf(isFraudulentEmailAddress("email")));
        }
        if(parameters.containsKey("domain"))
        {
            result.put("Banned domain", Double.valueOf(isBannedDomain(parameters.get("domain"))));
        }
        if(parameters.containsKey("IP"))
        {
            result.put("Banned IP", Double.valueOf(isBannedDomain(parameters.get("IP"))));
            if(parameters.containsKey("country") && parameters
                            .containsKey("zipCode") && parameters
                            .containsKey("state"))
            {
                result.put("Fraudulent IP GeoLocation",
                                Double.valueOf(isFraudulentGeolocation(parameters.get("IP"), parameters
                                                .get("country"), parameters
                                                .get("zipCode"), parameters
                                                .get("state"))));
            }
        }
        return result;
    }


    public FraudServiceResponse recognizeUserActivitySymptoms(UserModel order)
    {
        throw new NotImplementedException(getClass());
    }
}
