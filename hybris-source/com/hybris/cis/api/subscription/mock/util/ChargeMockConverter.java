package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.ChargeMock;
import com.hybris.cis.api.subscription.model.CisChargeEntry;
import java.util.ArrayList;
import java.util.List;

public class ChargeMockConverter extends MockConverter
{
    private ChargeMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {ChargeMockConverter.class
                        .getSimpleName()}));
    }


    public static List<CisChargeEntry> convertList(List<ChargeMock> sourceList)
    {
        ArrayList<CisChargeEntry> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(ChargeMock chargeMock : sourceList)
            {
                CisChargeEntry cisChargeEntry = convert(chargeMock);
                targetList.add(cisChargeEntry);
            }
        }
        return targetList;
    }


    public static CisChargeEntry convert(ChargeMock source)
    {
        CisChargeEntry target = new CisChargeEntry();
        if(source != null)
        {
            target.setChargePrice(source.getChargePrice());
            target.setNumberOfCycles(source.getNumberOfCycles());
            target.setOneTimeChargeTime(source.getOneTimeChargeTime());
            target.setVendorParameters(convertMapToVendorParameters(source.getVendorParameters()));
        }
        return target;
    }


    public static List<ChargeMock> reverseConvertList(List<CisChargeEntry> sourceList)
    {
        ArrayList<ChargeMock> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(CisChargeEntry cisChargeEntry : sourceList)
            {
                ChargeMock chargeMock = reverseConvert(cisChargeEntry);
                targetList.add(chargeMock);
            }
        }
        return targetList;
    }


    public static ChargeMock reverseConvert(CisChargeEntry source)
    {
        ChargeMock target = new ChargeMock();
        if(source != null)
        {
            target.setChargePrice(source.getChargePrice());
            target.setNumberOfCycles(source.getNumberOfCycles());
            target.setOneTimeChargeTime(source.getOneTimeChargeTime());
            target.setVendorParameters(convertVendorParametersToMap(source.getVendorParameters()));
        }
        return target;
    }
}
