package de.hybris.platform.catalog.jalo.classification.proficlass;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import org.apache.log4j.Logger;

public class ProfiClassAttributeTypeTranslator extends SingleValueTranslator
{
    private static final Logger log = Logger.getLogger(ProfiClassAttributeTypeTranslator.class);


    protected Object convertToJalo(String expr, Item forItem)
    {
        if("alphanumerisch".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
        }
        if("numerisch".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
        }
        if("logisch".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
        }
        log.warn("unknown proficlass attribute type '" + expr + "' - ignored");
        return null;
    }


    protected String convertToString(Object value)
    {
        String code = ((EnumerationValue)value).getCode();
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING.equalsIgnoreCase(code))
        {
            return "alphanumerisch";
        }
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equalsIgnoreCase(code))
        {
            return "numerisch";
        }
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN.equalsIgnoreCase(code))
        {
            return "logisch";
        }
        throw new JaloInvalidParameterException("invalid enum value " + value + " - expected " + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING + "," + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER + " or "
                        + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN + ".", 0);
    }
}
