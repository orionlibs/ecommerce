package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import java.util.Collection;
import java.util.List;

public interface AttributeDescriptorHome extends ItemHome
{
    AttributeDescriptorRemote create(PK paramPK, AttributeDescriptorRemote paramAttributeDescriptorRemote, List paramList, TypeRemote paramTypeRemote, int paramInt) throws EJBDuplicateQualifierException, EJBInvalidParameterException, YCreateException;


    AttributeDescriptorRemote create(PK paramPK, ComposedTypeRemote paramComposedTypeRemote1, String paramString1, String paramString2, TypeRemote paramTypeRemote, AtomicTypeRemote paramAtomicTypeRemote, int paramInt, ComposedTypeRemote paramComposedTypeRemote2)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException, YCreateException;


    AttributeDescriptorRemote findByEnclosingTypeAndQualifier(PK paramPK, String paramString) throws YObjectNotFoundException, YFinderException;


    Collection findByEnclosingTypeAndSelectionDescriptor(PK paramPK1, PK paramPK2) throws YObjectNotFoundException, YFinderException;


    AttributeDescriptorRemote findPublicByEnclosingTypeAndQualifier(PK paramPK, String paramString) throws YObjectNotFoundException, YFinderException;


    AttributeDescriptorRemote findDeclaredByEnclosingTypeAndQualifier(PK paramPK, String paramString) throws YObjectNotFoundException, YFinderException;


    Collection findByEnclosingType(PK paramPK) throws YFinderException;


    Collection findInhertitedByEnclosingType(PK paramPK) throws YFinderException;


    Collection findPublicByEnclosingType(PK paramPK) throws YFinderException;


    Collection findDeclaredByEnclosingType(PK paramPK) throws YFinderException;


    Collection findPropertyByEnclosingType(PK paramPK) throws YFinderException;


    Collection findInheritedByQualifierAndInheritancePath(String paramString1, String paramString2) throws YFinderException;


    Collection findBySuperAttributeDescriptor(PK paramPK) throws YFinderException;
}
