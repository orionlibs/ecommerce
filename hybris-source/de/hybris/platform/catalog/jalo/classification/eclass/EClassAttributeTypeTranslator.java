package de.hybris.platform.catalog.jalo.classification.eclass;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import org.apache.log4j.Logger;

public class EClassAttributeTypeTranslator extends SingleValueTranslator
{
    private static final Logger log = Logger.getLogger(EClassAttributeTypeTranslator.class);


    protected Object convertToJalo(String expr, Item forItem)
    {
        try
        {
            return (new EClassFieldFormat(expr)).getClassificationAttributeType();
        }
        catch(RuntimeException e)
        {
            log.warn("could not parse eclass field format '" + expr + "' due to " + e.getMessage() + " - using text format");
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
        }
    }


    protected String convertToString(Object value)
    {
        throw new JaloSystemException("export not suported");
    }
}
