package de.hybris.platform.commons.corrector.strategy;

import de.hybris.platform.commons.corrector.CorrectionMapFileException;
import de.hybris.platform.commons.corrector.HTMLCorrector;
import de.hybris.platform.commons.corrector.HTMLCorrectorStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class HTMLCorrectorReplaceStrategy implements HTMLCorrectorStrategy
{
    public String correct(String htmlIn, String correctionMapFile)
    {
        String correctedText = htmlIn;
        InputStream replaceStrategyMap_IS = null;
        replaceStrategyMap_IS = HTMLCorrector.class.getResourceAsStream("/commons/corrector/brreplacestrategy.properties");
        if(replaceStrategyMap_IS == null)
        {
            throw new CorrectionMapFileException("File not found!", "/commons/corrector/brreplacestrategy.properties");
        }
        try
        {
            Properties props = new Properties();
            props.load(replaceStrategyMap_IS);
            Enumeration<Object> propKeys = props.keys();
            while(propKeys.hasMoreElements())
            {
                String key = (String)propKeys.nextElement();
                correctedText = correctedText.replaceAll(key, props.getProperty(key));
            }
        }
        catch(IOException e)
        {
            throw new CorrectionMapFileException(e.getMessage(), "/commons/corrector/brreplacestrategy.properties");
        }
        return correctedText;
    }
}
