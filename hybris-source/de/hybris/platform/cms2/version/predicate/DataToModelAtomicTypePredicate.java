package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.function.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DataToModelAtomicTypePredicate implements Predicate<String>
{
    private static Logger LOG = Logger.getLogger(DataToModelAtomicTypePredicate.class);
    private TypeService typeService;


    public boolean test(String type)
    {
        return isAtomicType(type);
    }


    protected boolean isAtomicType(String typeCode)
    {
        try
        {
            getTypeService().getAtomicTypeForCode(typeCode);
            return true;
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Provided type is not Atomic Type: " + typeCode, (Throwable)e);
            return false;
        }
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
