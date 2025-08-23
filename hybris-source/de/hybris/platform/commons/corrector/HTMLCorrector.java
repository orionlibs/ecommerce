package de.hybris.platform.commons.corrector;

public class HTMLCorrector
{
    public static String correct(String htmlIn, String strategy, String correctionMapFile)
    {
        return HTMLCorrectorHelper.getStrategy(strategy).correct(htmlIn, correctionMapFile);
    }
}
