package de.hybris.platform.servicelayer.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class ServicesUtil
{
    public static void validateIfSingleResult(Collection<? extends Object> resultToCheck, String unknownIdException, String ambiguousIdException)
    {
        Preconditions.checkArgument((resultToCheck != null), "the result collection can not be null");
        validateSingleResultWithType(resultToCheck, Object.class, unknownIdException, ambiguousIdException);
    }


    public static void validateIfSingleResult(Collection<? extends Object> resultToCheck, Class clazz, String qualifier, Object qualifierValue)
    {
        Preconditions.checkArgument((resultToCheck != null), "the result collection can not be null");
        Preconditions.checkArgument((clazz != null), "the given clazz can not be null");
        Preconditions.checkArgument((qualifier != null && qualifier.length() > 0), "qualifier must contain something");
        String unknownId = clazz.getSimpleName() + " with " + clazz.getSimpleName() + " '" + qualifier + "' not found!";
        String ambiguousId = clazz.getSimpleName() + " " + clazz.getSimpleName() + " '" + qualifier + "' is not unique, " + qualifierValue.toString() + " instances  of type " + resultToCheck.size() + " found!";
        validateSingleResultWithType(resultToCheck, clazz, unknownId, ambiguousId);
    }


    private static void validateSingleResultWithType(Collection<? extends Object> resultToCheck, Class clazz, String unknownIdException, String ambiguousIdException)
    {
        if(CollectionUtils.isEmpty(resultToCheck))
        {
            throw new UnknownIdentifierException(unknownIdException);
        }
        if(resultToCheck.size() > 1)
        {
            throw new AmbiguousIdentifierException(ambiguousIdException);
        }
        for(Object element : resultToCheck)
        {
            if(!clazz.isInstance(element))
            {
                throw new IllegalStateException("element in result ('" + element.getClass() + "') is not the same class or a subclass of '" + clazz + "'");
            }
        }
    }


    public static void validateIfAnyResult(Collection resultToCheck, String unknownIdException)
    {
        if(CollectionUtils.isEmpty(resultToCheck))
        {
            throw new UnknownIdentifierException(unknownIdException);
        }
    }


    public static void validateParameterNotNull(Object parameter, String nullMessage)
    {
        Preconditions.checkArgument((parameter != null), nullMessage);
    }


    public static void validateParameterNotNullStandardMessage(String parameter, Object parameterValue)
    {
        validateParameterNotNull(parameterValue, "Parameter " + parameter + " can not be null");
    }
}
