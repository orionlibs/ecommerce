package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.BillingInfoMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class BillingInfoMockConverter
{
    private BillingInfoMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {BillingInfoMockConverter.class
                        .getSimpleName()}));
    }


    public static CisSubscriptionBillingInfo convert(BillingInfoMock source)
    {
        CisSubscriptionBillingInfo target = new CisSubscriptionBillingInfo();
        target.setBillingId(source.getBillingId());
        populateBillingDate(source, target);
        target.setAmount(source.getAmount());
        target.setBillingPeriod(source.getBillingPeriod());
        target.setStatus(source.getStatus());
        return target;
    }


    private static void populateBillingDate(BillingInfoMock source, CisSubscriptionBillingInfo target)
    {
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        target.setBillingDate(format.format(source.getBillingDate()));
    }
}
