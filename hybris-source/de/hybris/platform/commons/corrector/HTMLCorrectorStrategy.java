package de.hybris.platform.commons.corrector;

public interface HTMLCorrectorStrategy
{
    public static final String BR_REPLACE_STRATEGY_MAP = "/commons/corrector/brreplacestrategy.properties";
    public static final String REPLACE_STRATEGY = "replace.strategy";


    String correct(String paramString1, String paramString2);
}
