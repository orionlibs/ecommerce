package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.EJBInvalidParameterException;
import java.util.List;

public interface AttributeDescriptorRemote extends DescriptorRemote
{
    void reinitializeAttribute(String paramString1, String paramString2, TypeRemote paramTypeRemote, AtomicTypeRemote paramAtomicTypeRemote, int paramInt, AttributeDescriptorRemote paramAttributeDescriptorRemote1, List paramList, ComposedTypeRemote paramComposedTypeRemote,
                    AttributeDescriptorRemote paramAttributeDescriptorRemote2) throws EJBDuplicateQualifierException, EJBInvalidParameterException;


    ComposedTypeRemote getDeclaringEnclosingType();


    void changeDeclaringEnclosingType(ComposedTypeRemote paramComposedTypeRemote) throws EJBInvalidParameterException;


    ComposedTypeRemote getEnclosingType();


    void setEnclosingType(ComposedTypeRemote paramComposedTypeRemote) throws EJBInvalidParameterException;


    String getQualifier();


    String getPersistenceQualifier();


    TypeRemote getAttributeType();


    void setAttributeType(TypeRemote paramTypeRemote) throws EJBInvalidParameterException;


    AtomicTypeRemote getPersistenceType();


    AttributeDescriptorRemote getDeclaringSuperAttributeDescriptor();


    boolean isAssignableFrom(AttributeDescriptorRemote paramAttributeDescriptorRemote);


    List getInheritancePath();


    void setDatabaseColumn(String paramString);


    String getDatabaseColumn();


    Class getPersistenceClass();


    String getInheritancePathString();


    AttributeDescriptorRemote getSelectionOf();


    void setSelectionOf(AttributeDescriptorRemote paramAttributeDescriptorRemote) throws EJBInvalidParameterException;


    int getModifiers();


    void setModifiers(int paramInt) throws EJBInvalidParameterException;


    boolean isProperty();


    void setProperty(boolean paramBoolean);


    boolean isLocalized();


    void setLocalized(boolean paramBoolean);


    boolean isInherited();


    boolean isInitial();


    void setInitial(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isReadable();


    void setReadable(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isWritable();


    void setWritable(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isRemovable();


    void setRemovable(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isOptional();


    void setOptional(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isSearchable();


    void setSearchable(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isPartOf();


    void setPartOf(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean isPrivate();


    void setPrivate(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean dontOptimize();


    void setDontOptimize(boolean paramBoolean) throws EJBInvalidParameterException;


    boolean setDeclared();
}
