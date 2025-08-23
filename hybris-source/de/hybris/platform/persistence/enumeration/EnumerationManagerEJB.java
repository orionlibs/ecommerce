package de.hybris.platform.persistence.enumeration;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.EJBDuplicateCodeException;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnumerationManagerEJB extends ManagerEJB
{
    private static final String ENUM_META_TYPE_CODE = "EnumerationMetaType";


    public int getTypecode()
    {
        return 92;
    }


    public void removeItem(ItemRemote item) throws ConsistencyCheckException
    {
        if(item instanceof EnumerationValueRemote)
        {
            super.removeItem(item);
        }
        else
        {
            throw new JaloSystemException(null, "EnumerationManager cannot handle " + EJBTools.getPK(item), 0);
        }
    }


    public boolean canRemoveItem(ItemRemote item) throws ConsistencyCheckException
    {
        if(item instanceof EnumerationValueRemote)
        {
            EnumerationValueRemote enumValue = (EnumerationValueRemote)item;
            if(!enumValue.isEditable())
            {
                throw new ConsistencyCheckException(null, "cannot delete uneditable EnumerationValue " + enumValue.getCode(), 0);
            }
            return true;
        }
        return true;
    }


    public ComposedTypeRemote createEnumerationType(PK pkBase, String code, ComposedTypeRemote valueType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        TypeManagerEJB tm = SystemEJB.getInstance().getTypeManager();
        ComposedTypeRemote enumType = tm.createNonRootComposedType(pkBase,
                        (valueType != null) ? valueType : getRootEnumerationType(), code, null,
                        getEnumerationMetaType(), true);
        return enumType;
    }


    public ComposedTypeRemote getEnumerationType(String code) throws EJBItemNotFoundException
    {
        ComposedTypeRemote candidate = SystemEJB.getInstance().getTypeManager().getComposedType(code);
        if(!Utilities.ejbEquals((ItemRemote)candidate.getSuperType(), (ItemRemote)getRootEnumerationType()))
        {
            throw new EJBItemNotFoundException(null, "a type " + stringFor(candidate) + " was found, but its direct supertype is not the root enumeration type " +
                            stringFor(
                                            getRootEnumerationType()), 0);
        }
        return candidate;
    }


    protected EnumerationValueHome getEnumerationValueHome()
    {
        return (EnumerationValueHome)getPersistencePool()
                        .getHomeProxy(Registry.getPersistenceManager().getJNDIName(91));
    }


    private ComposedTypeRemote getRootEnumerationType()
    {
        try
        {
            return SystemEJB.getInstance().getTypeManager().getRootComposedType(91);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new EJBInternalException(e);
        }
    }


    public ComposedTypeRemote getEnumerationMetaType()
    {
        try
        {
            return SystemEJB.getInstance().getTypeManager().getComposedType("EnumerationMetaType");
        }
        catch(EJBItemNotFoundException e)
        {
            throw new EJBInternalException(e);
        }
    }


    protected String stringFor(ComposedTypeRemote type)
    {
        return EJBTools.getPK((ItemRemote)type).toString() + "(" + EJBTools.getPK((ItemRemote)type).toString() + ")";
    }


    public EnumerationValueRemote createEnumerationValue(PK pkBase, ComposedTypeRemote enumerationType, String valueCode, int number) throws ConsistencyCheckException, EJBInvalidParameterException
    {
        EnumerationValueRemote rem = null;
        try
        {
            rem = getEnumerationValueHome().create(pkBase, enumerationType, valueCode, number);
            return rem;
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public List getEnumerationValues(ComposedTypeRemote enumerationType) throws EJBInvalidParameterException
    {
        try
        {
            return new ArrayList(getEnumerationValueHome().findSortedValues(EJBTools.getPK((ItemRemote)enumerationType)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public EnumerationValueRemote getEnumerationValue(ComposedTypeRemote enumerationType, String code) throws EJBInvalidParameterException, EJBItemNotFoundException
    {
        try
        {
            EnumerationValueRemote re = getEnumerationValueHome().findByTypeAndCodeIgnoreCase(
                            EJBTools.getPK((ItemRemote)enumerationType), code
                                            .toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(re != null)
            {
                return re;
            }
            throw new EJBItemNotFoundException(null, "no enum value " + EJBTools.getPK(enumerationType) + ", code=" + code, 0);
        }
        catch(YObjectNotFoundException e)
        {
            throw new EJBItemNotFoundException(null, "no enum value " +
                            EJBTools.getPK(enumerationType)
                                            .getLongValueAsString() + ", code=" + code, 0);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public EnumerationValueRemote getEnumerationValue(String enumerationTypeCode, String code) throws EJBInvalidParameterException, EJBItemNotFoundException
    {
        return getEnumerationValue(getEnumerationType(enumerationTypeCode), code);
    }


    public void sortEnumerationValues(List<EnumerationValueRemote> enumerationValues) throws EJBInvalidParameterException
    {
        if(enumerationValues.isEmpty())
        {
            return;
        }
        EnumerationValueRemote firstValue = enumerationValues.iterator().next();
        List<?> requiredValues = getEnumerationValues(firstValue.getComposedType());
        if(!enumerationValues.containsAll(requiredValues))
        {
            throw new EJBInvalidParameterException(null, "not all enumeration values of type " +
                            stringFor(firstValue
                                            .getComposedType()) + " given: expected " + requiredValues + ", found " + enumerationValues, 0);
        }
        if(!requiredValues.containsAll(enumerationValues))
        {
            throw new EJBInvalidParameterException(null, "too many enumeration values for type " +
                            stringFor(firstValue
                                            .getComposedType()) + " given: expected " + requiredValues + ", found " + enumerationValues, 0);
        }
        Iterator<EnumerationValueRemote> iter = enumerationValues.iterator();
        while(iter.hasNext())
        {
            EnumerationValueRemote next = iter.next();
            if(!Utilities.ejbEquals((ItemRemote)firstValue.getComposedType(), (ItemRemote)next.getComposedType()))
            {
                throw new EJBInvalidParameterException(null, "different enumeration value types found: " +
                                stringFor(firstValue.getComposedType()) + ", " + stringFor(next.getComposedType()), 0);
            }
        }
        int index = 0;
        iter = enumerationValues.iterator();
        while(iter.hasNext())
        {
            EnumerationValueRemote next = iter.next();
            next.setSequenceNumber(index);
            index++;
        }
    }
}
