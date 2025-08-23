package de.hybris.platform.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CoreAlgorithms
{
    public static final String CFG_LEGACY_ROUNDING = "core.algorithms.legacy.rounding";
    private static volatile LegacyRoundingConfigProvider legacyConfigProvider = null;


    public static LegacyRoundingConfigProvider setLegacyConfigProvider(LegacyRoundingConfigProvider provider)
    {
        LegacyRoundingConfigProvider old = legacyConfigProvider;
        legacyConfigProvider = provider;
        return old;
    }


    private static final boolean isLegacyRoundingEnabled()
    {
        return (legacyConfigProvider != null && legacyConfigProvider.isLegacyRoundingEnabled());
    }


    public static double round(double value, int digits)
    {
        if(Double.isFinite(value))
        {
            BigDecimal bdValue = new BigDecimal(Double.toString(value));
            if(bdValue.signum() == -1 && isLegacyRoundingEnabled())
            {
                return bdValue.setScale(digits, RoundingMode.HALF_DOWN).doubleValue();
            }
            return bdValue.setScale(digits, RoundingMode.HALF_UP).doubleValue();
        }
        return value;
    }


    public static double convert(double sourceFactor, double targetFactor, double value)
    {
        if(isDoubleValueEqualsZero(sourceFactor) || isDoubleValueEqualsZero(targetFactor))
        {
            throw new IllegalArgumentException("currency/unit conversion factor must never be 0");
        }
        return (new BigDecimal(Double.toString(value))).multiply(new BigDecimal(Double.toString(targetFactor / sourceFactor)))
                        .doubleValue();
    }


    private static boolean isDoubleValueEqualsZero(double value)
    {
        return (Double.doubleToRawLongBits(value) == 0L);
    }
}
