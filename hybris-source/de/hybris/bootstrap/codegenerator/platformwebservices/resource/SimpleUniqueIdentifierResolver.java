package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@Deprecated(since = "1818", forRemoval = true)
public class SimpleUniqueIdentifierResolver implements UniqueIdentifierResolver
{
    public String getUniqueIdentifier(YComposedType type)
    {
        Collection<String> attribs = getAllUniqueAttributes(type);
        return !attribs.isEmpty() ? attribs.iterator().next() : "pk";
    }


    protected Collection<String> getAllUniqueAttributes(YComposedType type)
    {
        Set<String> result = new TreeSet<>();
        YComposedType curType = type;
        while(curType != null)
        {
            for(YAttributeDescriptor attribute : curType.getAttributes())
            {
                if(isAtrributeUniqueAndAllowed(attribute))
                {
                    result.add(attribute.getQualifier());
                }
            }
            curType = curType.getSuperType();
        }
        return result;
    }


    protected boolean isAtrributeUniqueAndAllowed(YAttributeDescriptor attribute)
    {
        if(attribute.isUniqueModifier() && attribute.isReadable() && attribute.isGenerateInModel())
        {
            String javaClass = attribute.getType().getJavaClassName();
            if("String".equalsIgnoreCase(javaClass) || "java.lang.String".equalsIgnoreCase(javaClass))
            {
                return true;
            }
        }
        return false;
    }
}
