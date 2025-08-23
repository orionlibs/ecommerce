package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.UsageChargeTierMock;
import com.hybris.cis.api.subscription.model.CisUsageChargeTier;
import java.util.ArrayList;
import java.util.List;

public final class UsageChargeTierMockConverter
{
    private UsageChargeTierMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {UsageChargeTierMockConverter.class
                        .getSimpleName()}));
    }


    public static List<CisUsageChargeTier> convertList(List<UsageChargeTierMock> sourceList)
    {
        ArrayList<CisUsageChargeTier> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(UsageChargeTierMock tierMock : sourceList)
            {
                CisUsageChargeTier cisTierEntry = convert(tierMock);
                targetList.add(cisTierEntry);
            }
        }
        return targetList;
    }


    public static CisUsageChargeTier convert(UsageChargeTierMock source)
    {
        CisUsageChargeTier target = new CisUsageChargeTier();
        if(source != null)
        {
            target.setChargePrice(source.getChargePrice());
            target.setNumberOfUnits(source.getNumberOfUnits());
        }
        return target;
    }


    public static List<UsageChargeTierMock> reverseConvertList(List<CisUsageChargeTier> sourceList)
    {
        ArrayList<UsageChargeTierMock> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(CisUsageChargeTier cisTierEntry : sourceList)
            {
                UsageChargeTierMock chargeMock = reverseConvert(cisTierEntry);
                targetList.add(chargeMock);
            }
        }
        return targetList;
    }


    public static UsageChargeTierMock reverseConvert(CisUsageChargeTier source)
    {
        UsageChargeTierMock target = new UsageChargeTierMock();
        if(source != null)
        {
            target.setChargePrice(source.getChargePrice());
            target.setNumberOfUnits(source.getNumberOfUnits());
        }
        return target;
    }
}
