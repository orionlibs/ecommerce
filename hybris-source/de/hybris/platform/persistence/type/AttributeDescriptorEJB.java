package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class AttributeDescriptorEJB extends TypeManagerManagedEJB implements AttributeDescriptorRemote, AttributeDescriptorHome
{
    public static final String SQL_COLUMN_DESCRIPTIONS = "sqlcolumndescriptions";


    public abstract PK getEnclosingTypePK();


    public abstract void setEnclosingTypePK(PK paramPK);


    public abstract String getQualifierInternal();


    public abstract void setQualifierInternal(String paramString);


    public abstract String getQualifierLowerCaseInternal();


    public abstract void setQualifierLowerCaseInternal(String paramString);


    public abstract String getPersistenceQualifierInternal();


    public abstract void setPersistenceQualifierInternal(String paramString);


    public abstract PK getAttributeTypePK();


    public abstract void setAttributeTypePK(PK paramPK);


    public abstract PK getPersistenceTypePK();


    public abstract void setPersistenceTypePK(PK paramPK);


    public abstract PK getSuperAttributeDescriptorPK();


    public abstract void setSuperAttributeDescriptorPK(PK paramPK);


    public abstract String getInheritancePathString();


    public abstract void setInheritancePathString(String paramString);


    public abstract String getColumnNameInternal();


    public abstract void setColumnNameInternal(String paramString);


    public abstract PK getSelectionDescriptorPK();


    public abstract void setSelectionDescriptorPK(PK paramPK);


    public abstract int getAttributeModifiers();


    public abstract void setAttributeModifiers(int paramInt);


    public abstract boolean getHiddenFlag();


    public abstract void setHiddenFlag(boolean paramBoolean);


    public abstract boolean getPropertyFlag();


    public abstract void setPropertyFlag(boolean paramBoolean);


    protected int typeCode()
    {
        return 87;
    }


    protected void checkInheritancePath(AttributeDescriptorRemote superFD, List<ComposedTypeRemote> inheritancePath) throws EJBInvalidParameterException
    {
        String quali = superFD.getQualifier();
        if(inheritancePath.isEmpty())
        {
            throw new EJBInvalidParameterException(null, "illegal inheritance path: " + inheritancePathAsString(inheritancePath) + " with respect to super feature-descriptor " + quali, 4711);
        }
        ComposedTypeRemote enclosingType = inheritancePath.get(inheritancePath.size() - 1);
        ComposedTypeRemote dclEnclosingType = superFD.getEnclosingType();
        if(dclEnclosingType == null)
        {
            throw new EJBInvalidParameterException(null, "enclosing type of super feature-descriptor " + quali + " must not be null", 4711);
        }
        if(dclEnclosingType.equals(enclosingType))
        {
            throw new EJBInvalidParameterException(null, "enclosing type " + TypeTools.asString(dclEnclosingType) + " of super feature-descriptor " + quali + " is no proper super-type of " +
                            TypeTools.asString(enclosingType), 4711);
        }
        if((new HashSet(inheritancePath)).size() != inheritancePath.size())
        {
            throw new EJBInvalidParameterException(null, "inheritance path " + inheritancePathAsString(inheritancePath) + " of attribute " + quali + " contains duplicate enclosed type ", 4711);
        }
    }


    public PK ejbCreate(PK pkBase, AttributeDescriptorRemote superFD, List<ComposedTypeRemote> inheritancePath, TypeRemote type, int modifiers) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        if(superFD == null || inheritancePath == null || inheritancePath.contains(null))
        {
            throw new EJBInvalidParameterException(null, "null arguments are not admissible here.", 4711);
        }
        doCreateInternal(pkBase, superFD.getComposedType(), null, null);
        checkInheritancePath(superFD, inheritancePath);
        ComposedTypeRemote enclosingType = inheritancePath.get(inheritancePath.size() - 1);
        AttributeDescriptorRemote superSelectionAttribute = superFD.getSelectionOf();
        AttributeDescriptorRemote mySelectionAttribute = null;
        if(superSelectionAttribute != null)
        {
            try
            {
                mySelectionAttribute = SystemEJB.getInstance().getTypeManager().getEveryAttributeDescriptor(enclosingType, superSelectionAttribute
                                .getQualifier());
            }
            catch(EJBItemNotFoundException e1)
            {
                throw new EJBInvalidParameterException(e1, "cannot copy selection attribute since source attribute " +
                                TypeTools.asString(superSelectionAttribute) + " has not been copied yet", 0);
            }
        }
        String qualifier = superFD.getQualifier();
        TypeRemote superType = superFD.getAttributeType();
        AtomicTypeRemote superPersistenceType = superFD.getPersistenceType();
        doCreate(enclosingType, qualifier);
        initialize(enclosingType, qualifier, superFD.getPersistenceQualifier(), (type != null) ? type : superType,
                        (superType.equals(superPersistenceType) && type instanceof AtomicTypeRemote) ? (AtomicTypeRemote)type :
                                        superPersistenceType, modifiers, superFD, inheritancePath, mySelectionAttribute, false);
        return null;
    }


    public PK ejbCreate(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, ComposedTypeRemote metaType) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        if(qualifier == null || type == null)
        {
            throw new EJBInvalidParameterException(null, "null arguments are not admissible here.", 4711);
        }
        doCreateInternal(pkBase, metaType, null, null);
        doCreate(enclosingType, qualifier);
        initialize(enclosingType, qualifier, persistenceQualifier, type, persistenceType, modifiers, null, null, null, false);
        return null;
    }


    public void reinitializeAttribute(String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, AttributeDescriptorRemote superFD, List inheritencePath, ComposedTypeRemote metaType, AttributeDescriptorRemote selectionOfDesc)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        initialize(getEnclosingType(), qualifier, persistenceQualifier, type, persistenceType, modifiers, superFD, inheritencePath, selectionOfDesc, true);
        if(metaType != null)
        {
            setComposedType(metaType);
        }
        else if(superFD == null)
        {
            setComposedType(null);
        }
    }


    private final void doCreate(ComposedTypeRemote enclosingType, String qualifier) throws EJBInvalidParameterException, EJBDuplicateQualifierException
    {
        qualifier = qualifier.trim();
        if(!TypeTools.isValidQualifier(qualifier))
        {
            throw new EJBInvalidParameterException(null, "'" + qualifier + "' is no valid feature-descriptor qualifier.", 4711);
        }
        assureIdentity(enclosingType, qualifier);
        setEnclosingTypePK(EJBTools.getPK((ItemRemote)enclosingType));
        setOwner((ItemRemote)enclosingType);
        setQualifierInternal(qualifier);
        setQualifierLowerCaseInternal(PlatformStringUtils.toLowerCaseCached(qualifier));
    }


    private final void initialize(ComposedTypeRemote enclosingType, String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, AttributeDescriptorRemote superFD, List inheritancePath, AttributeDescriptorRemote selectionOfDescr, boolean reinit)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        setInheritancePathString((inheritancePath != null && !inheritancePath.isEmpty()) ?
                        EJBTools.getPKCollectionString(inheritancePath) : EJBTools.addPKToPKCollectionString(EJBTools.EMPTY_PK_COLLECTION_STRING,
                        EJBTools.getPK((ItemRemote)enclosingType)));
        ItemDeployment deployment = enclosingType.getDeployment();
        setPersistenceQualifierInternal((persistenceQualifier == null) ? null : persistenceQualifier);
        setAttributeTypePK(EJBTools.getPK((ItemRemote)type));
        setPersistenceTypePK((persistenceType == null) ? null : EJBTools.getPK((ItemRemote)persistenceType));
        if(persistenceQualifier != null && deployment != null)
        {
            ItemDeployment.Attribute a = deployment.getAttribute(persistenceQualifier);
            if(a != null)
            {
                setColumnNameInternal(a.getColumnName(Config.getDatabase()));
            }
            else
            {
                throw new EJBInvalidParameterException(null, "invalid persistence qualifier '" + persistenceQualifier + "' - cannot find deployment attribute inside " + deployment
                                .getName(), 0);
            }
        }
        else if(getColumnNameInternal() == null && superFD != null)
        {
            setColumnNameInternal(superFD.getDatabaseColumn());
        }
        setSuperAttributeDescriptorPK((superFD == null) ? null : EJBTools.getPK((ItemRemote)superFD));
        boolean search = ((modifiers & 0x10) == 16);
        boolean property = ((modifiers & 0x100) == 256);
        boolean reallySearchble = (search && (property || getPersistenceQualifier() != null || getDatabaseColumn() != null));
        modifiers = reallySearchble ? modifiers : (modifiers & 0xFFFFFFEF);
        setModifiers((superFD != null) ? (
                        modifiers & 0xFFFFFCFF | superFD.getModifiers() & 0x300 | 0x400) : modifiers);
        setSelectionDescriptorPK(EJBTools.getPK((ItemRemote)selectionOfDescr));
        setLocalized((isLocalized() || getTypeManager().isLocalizationType(type, Collections.EMPTY_LIST)));
        if(superFD != null)
        {
            setComposedType(superFD.getComposedType());
        }
        if(!qualifier.equals(getQualifierInternal()) && qualifier.equalsIgnoreCase(getQualifierInternal()))
        {
            setQualifierInternal(qualifier);
        }
    }


    public void ejbPostCreate(PK pkBase, AttributeDescriptorRemote superFD, List inheritancePath, TypeRemote type, int modifiers) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        doPostCreateInternal(pkBase, null, null);
    }


    public void ejbPostCreate(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, ComposedTypeRemote metaType) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        doPostCreateInternal(pkBase, metaType, null);
    }


    public Class getPersistenceClass()
    {
        AtomicTypeRemote persistenceType = getPersistenceType();
        return (persistenceType != null) ? persistenceType.getJavaClass() : null;
    }


    public String getDatabaseColumn()
    {
        return getColumnNameInternal();
    }


    public void setDatabaseColumn(String col)
    {
        setColumnNameInternal(col);
        if(col == null || "".equals(col))
        {
            (new Exception()).printStackTrace();
            try
            {
                setSearchable(false);
            }
            catch(EJBInvalidParameterException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    protected String inheritancePathAsString(List inheritancePath)
    {
        StringBuilder sb = new StringBuilder();
        for(Iterator<ComposedTypeRemote> iter = inheritancePath.iterator(); iter.hasNext(); )
        {
            ComposedTypeRemote ec = iter.next();
            sb.append(TypeTools.asString((TypeRemote)ec) + TypeTools.asString((TypeRemote)ec));
        }
        return sb.toString();
    }


    public ComposedTypeRemote getDeclaringEnclosingType()
    {
        List<ComposedTypeRemote> ihp = getInheritancePath();
        return (ihp != null) ? ihp.get(0) : null;
    }


    public ComposedTypeRemote getEnclosingType()
    {
        PK pk = getEnclosingTypePK();
        return (pk == null || PK.NULL_PK.equals(pk)) ? null : (ComposedTypeRemote)EJBTools.instantiatePK(pk);
    }


    public void setEnclosingType(ComposedTypeRemote enclosingType) throws EJBInvalidParameterException
    {
        if(enclosingType == null)
        {
            throw new EJBInvalidParameterException(null, "enclosing type must be non-null", 4711);
        }
        ComposedTypeRemote oldEnclosingType = getEnclosingType();
        if(oldEnclosingType != null && !oldEnclosingType.equals(enclosingType))
        {
            throw new EJBInvalidParameterException(null, "Cannot change the enclosing type of the bound feature-descriptor " +
                            getPkString(), 4711);
        }
        if(oldEnclosingType == null)
        {
            try
            {
                assureIdentity(enclosingType, getQualifier());
            }
            catch(EJBDuplicateQualifierException e)
            {
                throw new EJBInvalidParameterException(e, "!!", 4711);
            }
            setEnclosingTypePK(EJBTools.getPK((ItemRemote)enclosingType));
            setInheritancePathString(EJBTools.addPKToPKCollectionString(EJBTools.EMPTY_PK_COLLECTION_STRING,
                            EJBTools.getPK((ItemRemote)enclosingType)));
            setOwner((ItemRemote)enclosingType);
        }
    }


    public String getQualifier()
    {
        return getQualifierInternal();
    }


    public String getPersistenceQualifier()
    {
        String pq = getPersistenceQualifierInternal();
        return (pq == null || "null".equals(pq)) ? null : pq;
    }


    public TypeRemote getAttributeType()
    {
        return (TypeRemote)EJBTools.instantiatePK(getAttributeTypePK());
    }


    public void setAttributeType(TypeRemote type) throws EJBInvalidParameterException
    {
        TypeRemote oldType = getAttributeType();
        if(!type.equals(oldType))
        {
            setAttributeTypePK(EJBTools.getPK((ItemRemote)type));
            setLocalized(getTypeManager().isLocalizationType(type, Collections.EMPTY_LIST));
        }
    }


    public AtomicTypeRemote getPersistenceType()
    {
        PK ptPK = getPersistenceTypePK();
        return (ptPK == null || PK.NULL_PK.equals(ptPK)) ? null : (AtomicTypeRemote)EJBTools.instantiatePK(ptPK);
    }


    public int getModifiers()
    {
        return getAttributeModifiers();
    }


    public void setModifiers(int modifiers) throws EJBInvalidParameterException
    {
        int oldModifiers = getModifiers();
        if(modifiers != oldModifiers)
        {
            setAttributeModifiers(modifiers);
            setHiddenFlag(((modifiers & 0x80) == 128));
            setPropertyFlag(((modifiers & 0x100) == 256));
        }
    }


    public AttributeDescriptorRemote getSelectionOf()
    {
        PK selectionPK = getSelectionDescriptorPK();
        return (selectionPK == null || PK.NULL_PK.equals(selectionPK)) ? null :
                        (AttributeDescriptorRemote)EJBTools.instantiatePK(selectionPK);
    }


    public void setSelectionOf(AttributeDescriptorRemote selectionDescriptor) throws EJBInvalidParameterException
    {
        PK spk = EJBTools.getPK((ItemRemote)selectionDescriptor);
        if(spk == null)
        {
            setSelectionDescriptorPK(null);
        }
        else
        {
            if(getPkString().equals(spk))
            {
                throw new EJBInvalidParameterException(null, "given descriptor for selection equals current instance: AD: " +
                                getPkString() + " - SelectAD:" + TypeTools.asString(selectionDescriptor), 4711);
            }
            if(!selectionDescriptor.getEnclosingType().equals(getEnclosingType()))
            {
                throw new EJBInvalidParameterException(null, "given descriptor for selection does not have same enclosing type as current instance: " +
                                TypeTools.asString(selectionDescriptor
                                                .getEnclosingType()) + " - " +
                                TypeTools.asString(getEnclosingType()), 4711);
            }
            setSelectionDescriptorPK(selectionDescriptor.getPkString());
        }
    }


    public AttributeDescriptorRemote getDeclaringSuperAttributeDescriptor()
    {
        PK superPK = getSuperAttributeDescriptorPK();
        return (superPK == null || PK.NULL_PK.equals(superPK)) ? null : (AttributeDescriptorRemote)EJBTools.instantiatePK(superPK);
    }


    public boolean setDeclared()
    {
        if(!isInherited())
        {
            return false;
        }
        setSuperAttributeDescriptorPK(null);
        setInherited(false);
        setInheritancePathString(EJBTools.getPKCollectionString(Collections.singletonList(getEnclosingType())));
        return true;
    }


    protected void setInherited(boolean inherited)
    {
        setSingleModifier(1024, inherited);
    }


    public void changeDeclaringEnclosingType(ComposedTypeRemote type) throws EJBInvalidParameterException
    {
        if(!isInherited())
        {
            throw new EJBInvalidParameterException(null, "attribute " + getPkString() + " is not inherited", 0);
        }
        List current = getInheritancePath();
        int pos = current.indexOf(type);
        if(pos < 0)
        {
            throw new EJBInvalidParameterException(null, "feature " + getPkString() + " cannot be redeclared within " +
                            TypeTools.asString(type) + " since inheritance path did not contain this type, was " +
                            EJBTools.getPKCollectionString(current), 0);
        }
        if(pos > 0)
        {
            setInheritancePathString(EJBTools.getPKCollectionString(current.subList(pos, current.size())));
        }
    }


    public List getInheritancePath()
    {
        String ihps = getInheritancePathString();
        return (ihps == null || "null".equals(ihps)) ? null : EJBTools.instantiateCommaSeparatedPKString(ihps);
    }


    public boolean isAssignableFrom(AttributeDescriptorRemote fd)
    {
        TypeRemote myType = getAttributeType();
        TypeRemote otherType = fd.getAttributeType();
        return (equals(getQualifier(), fd.getQualifier()) &&
                        equals(getPersistenceQualifier(), fd.getPersistenceQualifier()) &&
                        equals(getPersistenceType(), fd.getPersistenceType()) && (
                        !isReadable() || myType.isAssignableFrom(otherType)) && (!isWritable() || otherType.isAssignableFrom(myType)) &&
                        isReadable() == fd.isReadable() && isWritable() == fd.isWritable() && isRemovable() == fd.isRemovable() &&
                        isOptional() == fd.isOptional() && isSearchable() == fd.isSearchable() && isPartOf() == fd.isPartOf() &&
                        isInitial() == fd.isInitial() && isLocalized() == fd.isLocalized() && isProperty() == fd.isProperty() &&
                        isPrivate() == fd.isPrivate());
    }


    protected void assureIdentity(ComposedTypeRemote enclosingType, String qualifier) throws EJBDuplicateQualifierException
    {
        try
        {
            AttributeDescriptorHome ah = (AttributeDescriptorHome)getEntityContext().getPersistencePool().getHomeProxy(
                            getEntityContext().getPersistencePool().getPersistenceManager().getJNDIName(typeCode()));
            AttributeDescriptorRemote fd = ah.findByEnclosingTypeAndQualifier(EJBTools.getPK((ItemRemote)enclosingType),
                            (qualifier != null) ? PlatformStringUtils.toLowerCaseCached(qualifier) :
                                            null);
            if(fd != null)
            {
                throw new EJBDuplicateQualifierException(null, (enclosingType != null)
                                ? ("attribute descriptor " +
                                TypeTools.asString(
                                                fd) + " is already " + (enclosingType.equals(fd.getDeclaringEnclosingType()) ? "declared in" : "inherited from") + " " + TypeTools.asString(fd.getDeclaringEnclosingType()) + " enctype: " + enclosingType.getCode() + ", qualifier: " + qualifier)
                                : ("unbound attribute descriptor " + TypeTools.asString(fd) + " already exists"), 4711);
            }
        }
        catch(YObjectNotFoundException yObjectNotFoundException)
        {
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected boolean getSingleModifier(int flag)
    {
        return ((getModifiers() & flag) == flag);
    }


    protected void setSingleModifier(int flag, boolean on)
    {
        try
        {
            setModifiers(on ? (getModifiers() | flag) : (getModifiers() & (flag ^ 0xFFFFFFFF)));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public boolean isReadable()
    {
        return getSingleModifier(1);
    }


    public void setReadable(boolean readable) throws EJBInvalidParameterException
    {
        setSingleModifier(1, readable);
    }


    public boolean isWritable()
    {
        return getSingleModifier(2);
    }


    public void setWritable(boolean writable) throws EJBInvalidParameterException
    {
        setSingleModifier(2, writable);
    }


    public boolean isRemovable()
    {
        return getSingleModifier(4);
    }


    public void setRemovable(boolean removable) throws EJBInvalidParameterException
    {
        setSingleModifier(4, removable);
    }


    public boolean isOptional()
    {
        return getSingleModifier(8);
    }


    public void setOptional(boolean optional) throws EJBInvalidParameterException
    {
        setSingleModifier(8, optional);
    }


    public boolean isSearchable()
    {
        return getSingleModifier(16);
    }


    public void setSearchable(boolean search) throws EJBInvalidParameterException
    {
        setSingleModifier(16, search);
    }


    public boolean isPartOf()
    {
        return getSingleModifier(32);
    }


    public void setPartOf(boolean partOf) throws EJBInvalidParameterException
    {
        setSingleModifier(32, partOf);
    }


    public boolean isPrivate()
    {
        return getSingleModifier(128);
    }


    public void setPrivate(boolean priv) throws EJBInvalidParameterException
    {
        setSingleModifier(128, priv);
    }


    public boolean isProperty()
    {
        return getSingleModifier(256);
    }


    public void setProperty(boolean isProp)
    {
        setSingleModifier(256, isProp);
    }


    public boolean isLocalized()
    {
        return getSingleModifier(512);
    }


    public void setLocalized(boolean isLocalized)
    {
        setSingleModifier(512, isLocalized);
    }


    public boolean isInherited()
    {
        return (getSuperAttributeDescriptorPK() != null && !PK.NULL_PK.equals(getSuperAttributeDescriptorPK()));
    }


    public boolean isInitial()
    {
        return getSingleModifier(2048);
    }


    public void setInitial(boolean initial) throws EJBInvalidParameterException
    {
        setSingleModifier(2048, initial);
    }


    public boolean dontOptimize()
    {
        return getSingleModifier(8192);
    }


    public void setDontOptimize(boolean dontopt) throws EJBInvalidParameterException
    {
        setSingleModifier(8192, dontopt);
    }
}
