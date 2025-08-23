package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.UsageChargeMock;
import com.hybris.cis.api.subscription.model.CisUsageCharge;
import java.util.ArrayList;
import java.util.List;

public final class UsageChargeMockConverter
{
    private UsageChargeMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {UsageChargeMockConverter.class
                        .getSimpleName()}));
    }


    public static List<CisUsageCharge> convertList(List<UsageChargeMock> sourceList)
    {
        ArrayList<CisUsageCharge> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(UsageChargeMock usageChargeMock : sourceList)
            {
                CisUsageCharge cisUsageCharge = convert(usageChargeMock);
                targetList.add(cisUsageCharge);
            }
        }
        return targetList;
    }


    public static CisUsageCharge convert(UsageChargeMock source)
    {
        CisUsageCharge target = new CisUsageCharge();
        if(source != null)
        {
            target.setName(source.getName());
            target.setType(source.getType());
            target.setUnitId(source.getUnitId());
            target.setTiers(UsageChargeTierMockConverter.convertList(source.getTiers()));
        }
        return target;
    }


    public static List<UsageChargeMock> reverseConvertList(List<CisUsageCharge> sourceList)
    {
        ArrayList<UsageChargeMock> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(CisUsageCharge cisUsageCharge : sourceList)
            {
                UsageChargeMock usageChargeMock = reverseConvert(cisUsageCharge);
                targetList.add(usageChargeMock);
            }
        }
        return targetList;
    }


    public static UsageChargeMock reverseConvert(CisUsageCharge source)
    {
        UsageChargeMock target = new UsageChargeMock();
        if(source != null)
        {
            target.setName(source.getName());
            target.setType(source.getType());
            target.setUnitId(source.getUnitId());
            target.setTiers(UsageChargeTierMockConverter.reverseConvertList(source.getTiers()));
        }
        return target;
    }
}
