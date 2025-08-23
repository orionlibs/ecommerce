package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.subscription.mock.data.AddressMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMockData;
import com.hybris.cis.api.subscription.model.CisPaymentMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public final class PaymentMethodMockConverter
{
    private static final Map<String, String> PAYMENT_METHOD_DATA_DEFAULTS = new HashMap<>();

    static
    {
        PAYMENT_METHOD_DATA_DEFAULTS.put("defaults", "false");
        PAYMENT_METHOD_DATA_DEFAULTS.put("cardNumber", "1234123412341234");
        PAYMENT_METHOD_DATA_DEFAULTS.put("cardType", "VISA");
        PAYMENT_METHOD_DATA_DEFAULTS.put("nameOnCard", "John Doe");
        PAYMENT_METHOD_DATA_DEFAULTS.put("expiryMonth", "07");
        PAYMENT_METHOD_DATA_DEFAULTS.put("expiryYear", "2017");
        PAYMENT_METHOD_DATA_DEFAULTS.put("startMonth", "05");
        PAYMENT_METHOD_DATA_DEFAULTS.put("startYear", "2005");
        PAYMENT_METHOD_DATA_DEFAULTS.put("issueNumber", "123");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_countryIso", "US");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_titleCode", "dr");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_firstName", "John");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_lastName", "Doe");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_line1", "90210");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_line2", "Beverly Hills");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_postcode", "90210");
        PAYMENT_METHOD_DATA_DEFAULTS.put("billingAddress_townCity", "Beverly Hills");
    }

    private PaymentMethodMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {PaymentMethodMockConverter.class
                        .getSimpleName()}));
    }


    public static CisPaymentMethod convert(PaymentMethodMock source)
    {
        CisPaymentMethod target = new CisPaymentMethod();
        if(source != null)
        {
            PaymentMethodMockData data = source.getPaymentMethodMockData();
            if(data != null)
            {
                target.setCardHolder(data.getAccountHolderName());
                target.setCardType(data.getCardType());
                target.setCcNumber(data.getCardNumber());
                target.setMerchantPaymentMethodId(source.getMerchantPaymentMethodId());
                target.setEnabled(source.isActive());
                if(StringUtils.isNotEmpty(data.getExpiryDate()))
                {
                    String[] expiryDate = StringUtils.split(data.getExpiryDate(), "/");
                    Integer expirationMonth = Integer.valueOf(Integer.parseInt(expiryDate[0]));
                    target.setExpirationMonth(expirationMonth.intValue());
                    Integer expirationYear = Integer.valueOf(Integer.parseInt(expiryDate[1]));
                    target.setExpirationYear(expirationYear.intValue());
                }
                target.setBillingAddress(AddressMockConverter.convert(data.getAddressMock()));
            }
        }
        return target;
    }


    public static List<CisPaymentMethod> convertList(List<PaymentMethodMock> sourceList)
    {
        ArrayList<CisPaymentMethod> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(PaymentMethodMock paymentMethodMock : sourceList)
            {
                CisPaymentMethod cisPaymentMethod = convert(paymentMethodMock);
                targetList.add(cisPaymentMethod);
            }
        }
        return targetList;
    }


    public static PaymentMethodMockData convert(AnnotationHashMap map)
    {
        PaymentMethodMockData paymentMethodMockData = new PaymentMethodMockData();
        Boolean useDefaults = Boolean.valueOf(getValue(map, "defaults", true));
        AddressMock addressMock = new AddressMock();
        addressMock.setTitle(getValue(map, "billingAddress_titleCode", useDefaults.booleanValue()));
        addressMock.setAddr1(getValue(map, "billingAddress_line1", useDefaults.booleanValue()));
        addressMock.setAddr2(getValue(map, "billingAddress_line2", useDefaults.booleanValue()));
        addressMock.setCity(getValue(map, "billingAddress_townCity", useDefaults.booleanValue()));
        addressMock.setCountry(getValue(map, "billingAddress_countryIso", useDefaults.booleanValue()));
        addressMock.setFirstName(getValue(map, "billingAddress_firstName", useDefaults.booleanValue()));
        addressMock.setLastName(getValue(map, "billingAddress_lastName", useDefaults.booleanValue()));
        addressMock.setPostalCode(getValue(map, "billingAddress_postcode", useDefaults.booleanValue()));
        paymentMethodMockData.setAddressMock(addressMock);
        paymentMethodMockData.setAccountHolderName(getValue(map, "nameOnCard", useDefaults.booleanValue()));
        paymentMethodMockData.setCardNumber(getValue(map, "cardNumber", useDefaults.booleanValue()));
        paymentMethodMockData.setCardType(getValue(map, "cardType", useDefaults.booleanValue()));
        paymentMethodMockData.setIssueNumber(getValue(map, "issueNumber", useDefaults.booleanValue()));
        String expiryMonth = getValue(map, "expiryMonth", useDefaults.booleanValue());
        String expiryYear = getValue(map, "expiryYear", useDefaults.booleanValue());
        if(StringUtils.isNotBlank(expiryMonth) && StringUtils.isNotBlank(expiryYear))
        {
            paymentMethodMockData.setExpiryDate(expiryMonth + "/" + expiryMonth);
        }
        String startMonth = getValue(map, "startMonth", useDefaults.booleanValue());
        String startYear = getValue(map, "startYear", useDefaults.booleanValue());
        if(StringUtils.isNotBlank(startMonth) && StringUtils.isNotBlank(startYear))
        {
            paymentMethodMockData.setStartDate(startMonth + "/" + startMonth);
        }
        return paymentMethodMockData;
    }


    private static String getValue(AnnotationHashMap map, String key, boolean useDefaults)
    {
        String value = null;
        if(map != null && map.getMap() != null)
        {
            value = (String)map.getMap().get(key);
        }
        if(StringUtils.isEmpty(value) && useDefaults)
        {
            value = PAYMENT_METHOD_DATA_DEFAULTS.get(key);
        }
        return value;
    }
}
