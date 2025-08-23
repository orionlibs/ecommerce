package de.hybris.platform.catalog.jalo.classification.impex;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import java.util.Collection;

public class ClassificationAttributeValueTranslator extends SingleValueTranslator
{
    private final ClassAttributeAssignment assignment;


    public ClassificationAttributeValueTranslator(ClassAttributeAssignment assignment)
    {
        this.assignment = assignment;
    }


    protected Object convertToJalo(String string, Item item)
    {
        Collection<ClassificationAttributeValue> values = this.assignment.getClassificationClass().getAttributeValues(this.assignment
                        .getClassificationAttribute());
        for(ClassificationAttributeValue value : values)
        {
            if(string.equalsIgnoreCase(value.getCode()))
            {
                return value;
            }
        }
        for(ClassificationAttributeValue value : values)
        {
            for(String name : value.getAllName().values())
            {
                if(string.equalsIgnoreCase(name))
                {
                    return value;
                }
            }
        }
        throw new JaloInvalidParameterException("Classification attribute value " + string + " not found ", 0);
    }


    protected String convertToString(Object obj)
    {
        ClassificationAttributeValue val = (ClassificationAttributeValue)obj;
        return val.getCode();
    }
}
