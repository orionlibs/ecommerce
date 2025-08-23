package de.hybris.platform.catalog.jalo.classification.etim;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import org.apache.log4j.Logger;

public class ETIMAttributeTypeTranslator extends SingleValueTranslator
{
    private static final Logger log = Logger.getLogger(ETIMAttributeTypeTranslator.class);


    protected Object convertToJalo(String expr, Item forItem)
    {
        if("A".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
        }
        if("N".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
        }
        if("L".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
        }
        if("R".equalsIgnoreCase(expr))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
        }
        log.warn("unknown etim attribute type '" + expr + "' - ignored");
        return null;
    }


    protected String convertToString(Object value)
    {
        String code = ((EnumerationValue)value).getCode();
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING.equalsIgnoreCase(code))
        {
            return "A";
        }
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equalsIgnoreCase(code))
        {
            return "N";
        }
        if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN.equalsIgnoreCase(code))
        {
            return "L";
        }
        throw new JaloInvalidParameterException("invalid enum value " + value + " - expected " + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING + "," + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER + " or "
                        + GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN + ".", 0);
    }
}
