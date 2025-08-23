package com.hybris.backoffice.proxy;

import java.util.Optional;

public interface DataQualityCalculationServiceProxy
{
    Optional<Double> calculate(Object paramObject, String paramString);


    Optional<Double> calculate(Object paramObject, String paramString1, String paramString2);
}
