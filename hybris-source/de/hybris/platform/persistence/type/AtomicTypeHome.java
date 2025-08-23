package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface AtomicTypeHome extends HierarchieTypeHome
{
    AtomicTypeRemote create(PK paramPK, Class paramClass) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    AtomicTypeRemote create(PK paramPK, AtomicTypeRemote paramAtomicTypeRemote, Class paramClass) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    AtomicTypeRemote create(PK paramPK, AtomicTypeRemote paramAtomicTypeRemote, String paramString) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    AtomicTypeRemote findByPrimaryKey(PK paramPK) throws YFinderException;


    Collection findByJavaClass(String paramString) throws YFinderException;
}
