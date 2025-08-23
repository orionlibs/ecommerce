package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.EJBInvalidParameterException;
import java.util.List;

public interface AtomicTypeRemote extends HierarchieTypeRemote
{
    Class getJavaClass();


    AtomicTypeRemote getSuperType();


    List getInheritancePath();


    String getInheritancePathString();


    boolean isAbstract();


    void setInheritancePathString(String paramString);


    void reinitializeType(AtomicTypeRemote paramAtomicTypeRemote, Class paramClass) throws EJBInvalidParameterException;
}
