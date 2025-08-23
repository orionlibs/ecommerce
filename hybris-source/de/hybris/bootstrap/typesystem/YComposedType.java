package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;

public class YComposedType extends YType
{
    private final String superTypeCode;
    private YComposedType superType;
    private String jaloClassName;
    private boolean abstractVariable = false;
    private boolean singleton = false;
    private boolean jaloOnly = false;
    private String deploymentName;
    private YDeployment deployment;
    private boolean generateModel = false;
    private String typeDescription;
    private boolean legacyPersistence;
    private String deprecatedSince;
    private final List<ModelTagListener.ModelDataConstructor> modelDataConstrSignatures = new ArrayList<>();
    private List<YModelConstructor> ymodelconstr = Collections.emptyList();


    public YComposedType(YNamespace container, String code, String superTypeCode, String jaloClassName)
    {
        super(container, code);
        this.superTypeCode = superTypeCode;
        this.jaloClassName = jaloClassName;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "ComposedType";
    }


    public YComposedType getMetaType()
    {
        if(getMetaTypeCode() != null || getSuperTypeCode() == null || !allowMetaTypeInheritanceFrom(getSuperType()))
        {
            return super.getMetaType();
        }
        return getSuperType().getMetaType();
    }


    protected boolean allowMetaTypeInheritanceFrom(YComposedType superType)
    {
        return true;
    }


    public Map<String, String> getCustomProps()
    {
        Map<String, String> allProps = new HashMap<>();
        for(YComposedType superType : getAllSuperTypes())
        {
            Map<String, String> map = superType.getOwnCustomProps();
            if(MapUtils.isNotEmpty(map))
            {
                allProps.putAll(map);
            }
        }
        Map<String, String> props = getOwnCustomProps();
        if(MapUtils.isNotEmpty(props))
        {
            allProps.putAll(props);
        }
        return allProps;
    }


    public Map<String, String> getOwnCustomProps()
    {
        return super.getCustomProps();
    }


    public void validate()
    {
        super.validate();
        getSuperType();
        if(getSuperType() != null && !getSuperType().getSubtypes().contains(this))
        {
            throw new IllegalStateException("invalid composed type " + this + " since it doesnt seem to be assigned to supertype " +
                            getSuperType());
        }
        getOwnJaloClass();
        if(getJaloClass() == null && !getTypeSystem().isBuildMode())
        {
            throw new IllegalStateException("invalid composed type " + this + " since it got no jalo class at all");
        }
        if(isJaloClassAbstract() && !isAbstract())
        {
            throw new IllegalStateException("invalid non-abstract composed type " + this + " since it got abstract jalo class " +
                            getJaloClass());
        }
        getOwnDeployment();
        for(ModelTagListener.ModelDataConstructor signature : this.modelDataConstrSignatures)
        {
            if(signature.getQualifiers().isEmpty())
            {
                throw new IllegalStateException("Model constructor signature for composedType " + this + " is empty! An empty constructor for the model will be created per default!");
            }
            for(String qualifier : signature.getQualifiers())
            {
                if("itemtype".equals(qualifier))
                {
                    throw new IllegalStateException("The qualifier 'itemtype' is not allowed in the tag model constructor! (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                if("creationtime".equals(qualifier))
                {
                    throw new IllegalStateException("The qualifier 'creationtime' is not allowed in the tag model constructor! (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                YAttributeDescriptor yad = getAttributeIncludingSuperType(qualifier);
                if(yad == null)
                {
                    throw new IllegalStateException("Model constructor qualifier '" + qualifier + "' does not exists for composedType " +
                                    getCode() + " (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                if(yad.isPrivate())
                {
                    throw new IllegalStateException("Model constructor qualifier '" + qualifier + "' is private. Cannot create model constructor for type " +
                                    getCode() + " (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                if(!yad.isWritable() && !yad.isInitial())
                {
                    throw new IllegalStateException("Model constructor qualifier '" + qualifier + "' must be writable or initial. Cannot create model constructor for type " +
                                    getCode() + " (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                if(yad.isLocalized())
                {
                    throw new IllegalStateException("Model constructor qualifier '" + qualifier + "' is localized. Cannot create model constructor for type " +
                                    getCode() + " (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
                if(!isAttributeTypeGenerationEnabled(yad.getType()))
                {
                    throw new IllegalStateException("There was no model class for the model constructor qualifier '" + qualifier + "' generated. Cannot create model constructor for type " +
                                    getCode() + " (file: " + signature
                                    .getLocationOfDefinition() + " )");
                }
            }
            if(this.ymodelconstr.isEmpty())
            {
                this.ymodelconstr = new ArrayList<>();
            }
            this.ymodelconstr.add(new YModelConstructor(this, signature, getNamespace()));
        }
        validateModelConstructors();
    }


    private void validateModelConstructors()
    {
        List<YModelConstructor> checkedConstructors = new ArrayList<>(this.ymodelconstr.size());
        for(YModelConstructor toBeCheckConst : this.ymodelconstr)
        {
            for(YModelConstructor compareConst : checkedConstructors)
            {
                if(toBeCheckConst.hasSameAttributeQualifier(compareConst))
                {
                    throw new IllegalStateException("A similar model constructor was already declared! " + toBeCheckConst + " <-> " + compareConst);
                }
                if(toBeCheckConst.hasSameSignature(compareConst))
                {
                    throw new IllegalStateException("Found model constructor with same type signature! " + toBeCheckConst + " <-> " + compareConst);
                }
            }
            checkedConstructors.add(toBeCheckConst);
        }
    }


    private boolean isAttributeTypeGenerationEnabled(YType type)
    {
        if(type instanceof YEnumType)
        {
            return true;
        }
        if(type instanceof YComposedType)
        {
            YComposedType cType = (YComposedType)type;
            return cType.isGenerateModel();
        }
        if(type instanceof YCollectionType)
        {
            YCollectionType cType = (YCollectionType)type;
            return isAttributeTypeGenerationEnabled(cType.getElementType());
        }
        if(type instanceof YMapType)
        {
            YMapType mType = (YMapType)type;
            return (isAttributeTypeGenerationEnabled(mType.getArgumentType()) &&
                            isAttributeTypeGenerationEnabled(mType.getReturnType()));
        }
        return true;
    }


    public List<YModelConstructor> getYModelConstructors()
    {
        return this.ymodelconstr;
    }


    public boolean isAbstract()
    {
        return (isJaloClassAbstract() || (!isJaloOnly() && this.abstractVariable));
    }


    public void setAbstract(boolean isAbstract)
    {
        this.abstractVariable = isAbstract;
    }


    public boolean isSingleton()
    {
        return this.singleton;
    }


    public void setSingleton(boolean isSingleton)
    {
        this.singleton = isSingleton;
    }


    public String getJaloClassName()
    {
        String ret = null;
        for(YComposedType t = this; t != null && ret == null; t = t.getSuperType())
        {
            ret = t.getOwnJaloClassName();
        }
        return ret;
    }


    public String getOwnJaloClassName()
    {
        return this.jaloClassName;
    }


    protected boolean isJaloClassAbstract()
    {
        Class own = getJaloClass();
        return (own != null && (own.getModifiers() & 0x400) != 0);
    }


    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
    }


    public String getTypeDescription()
    {
        return this.typeDescription;
    }


    public boolean isViewType()
    {
        return "ViewType".equalsIgnoreCase(getMetaType().getCode());
    }


    public Class getJaloClass()
    {
        Class clazz = getOwnJaloClass();
        if(clazz == null)
        {
            if(isViewType())
            {
                clazz = getTypeSystem().resolveClass(this, "de.hybris.platform.util.ViewResultItem");
            }
            else
            {
                clazz = (getSuperType() != null) ? getSuperType().getJaloClass() : null;
            }
        }
        return clazz;
    }


    public Class getOwnJaloClass()
    {
        return (getJaloClassName() != null) ? getTypeSystem().resolveClass(this, getJaloClassName()) : null;
    }


    public void setJaloClassName(String jaloClassName)
    {
        this.jaloClassName = jaloClassName;
    }


    public String getSuperTypeCode()
    {
        return this.superTypeCode;
    }


    public YComposedType getSuperType()
    {
        if(this.superType == null && getSuperTypeCode() != null)
        {
            YType type = getTypeSystem().getType(getSuperTypeCode());
            if(!(type instanceof YComposedType))
            {
                throw new IllegalStateException("invalid composed type " + this + " due to missing super type '" +
                                getSuperTypeCode() + "'");
            }
            this.superType = (YComposedType)type;
        }
        return this.superType;
    }


    protected Class getJaloOnlyClass()
    {
        return getTypeSystem().resolveClass(this, "de.hybris.platform.jalo.JaloOnlyItem");
    }


    public boolean isJaloOnly()
    {
        if(this.jaloOnly)
        {
            return true;
        }
        Class jaloOnlyCl = getJaloOnlyClass();
        return (jaloOnlyCl != null && jaloOnlyCl.isAssignableFrom(getJaloClass()));
    }


    public void setJaloOnly(boolean isJaloOnly)
    {
        this.jaloOnly = isJaloOnly;
    }


    public String getDeploymentName()
    {
        return this.deploymentName;
    }


    public void setDeploymentName(String deploymentName)
    {
        this.deploymentName = deploymentName;
    }


    public YDeployment getDeployment()
    {
        YDeployment depl = getOwnDeployment();
        return (depl != null) ? depl : ((getSuperType() != null) ? getSuperType().getDeployment() : null);
    }


    public YDeployment getOwnDeployment()
    {
        if(this.deployment == null && getDeploymentName() != null)
        {
            YDeployment type = getTypeSystem().getDeployment(getDeploymentName());
            if(type == null)
            {
                throw new IllegalStateException("invalid composed type " + this + " due to missing deployment '" +
                                getDeploymentName() + "'");
            }
            this.deployment = type;
        }
        return this.deployment;
    }


    public YAttributeDescriptor getAttribute(String qualifier)
    {
        return getTypeSystem().getAttribute(getCode(), qualifier);
    }


    public YAttributeDescriptor getAttributeIncludingSuperType(String qualifier)
    {
        YAttributeDescriptor yad = null;
        YComposedType supertype = this;
        while(supertype != null && yad == null)
        {
            yad = supertype.getAttribute(qualifier);
            supertype = supertype.getSuperType();
        }
        return yad;
    }


    public Set<YComposedType> getSubtypes()
    {
        return getTypeSystem().getSubtypes(getCode());
    }


    public List<YComposedType> getAllSuperTypes()
    {
        List<YComposedType> ret = null;
        for(YComposedType st = getSuperType(); st != null; st = st.getSuperType())
        {
            if(ret == null)
            {
                ret = new ArrayList<>(5);
            }
            ret.add(st);
        }
        if(ret != null)
        {
            Collections.reverse(ret);
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Set<YComposedType> getAllSubtypes()
    {
        Set<YComposedType> ret = null;
        Set<YComposedType> types = Collections.singleton(this);
        while(types != null && !types.isEmpty())
        {
            Set<YComposedType> nextLevel = null;
            for(YComposedType t : types)
            {
                for(YComposedType subtype : t.getSubtypes())
                {
                    if(ret == null)
                    {
                        ret = new HashSet<>();
                    }
                    if(nextLevel == null)
                    {
                        nextLevel = new HashSet<>();
                    }
                    ret.add(subtype);
                    nextLevel.add(subtype);
                }
            }
            types = nextLevel;
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YAttributeDescriptor> getAttributes()
    {
        return getTypeSystem().getAttributes(getCode());
    }


    public Set<YAttributeDescriptor> getDeclaredAttributes()
    {
        Set<YAttributeDescriptor> declared = null;
        Set<YAttributeDescriptor> all = getAttributes();
        if(!all.isEmpty())
        {
            for(YAttributeDescriptor ad : all)
            {
                if(!ad.isInherited())
                {
                    if(declared == null)
                    {
                        declared = new LinkedHashSet<>();
                    }
                    declared.add(ad);
                }
            }
        }
        return (declared == null) ? Collections.EMPTY_SET : declared;
    }


    public Set<YAttributeDescriptor> getRedeclaredAttributes()
    {
        Set<YAttributeDescriptor> ret = null;
        Set<YAttributeDescriptor> all = getAttributes();
        if(!all.isEmpty())
        {
            for(YAttributeDescriptor ad : all)
            {
                if(ad.isRedeclared())
                {
                    if(ret == null)
                    {
                        ret = new LinkedHashSet<>();
                    }
                    ret.add(ad);
                }
            }
        }
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    public String getJavaClassName()
    {
        return getJaloClassName();
    }


    public boolean isGenerateModel()
    {
        return this.generateModel;
    }


    public void setGenerateModel(boolean generateModel)
    {
        this.generateModel = generateModel;
    }


    public void addConstructorSignatures(List<ModelTagListener.ModelDataConstructor> signatures)
    {
        this.modelDataConstrSignatures.addAll(signatures);
    }


    public boolean isLegacyPersistence()
    {
        return this.legacyPersistence;
    }


    public void setLegacyPersistence(boolean legacyPersistence)
    {
        this.legacyPersistence = legacyPersistence;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public void setDeprecatedSince(String deprecatedSince)
    {
        this.deprecatedSince = deprecatedSince;
    }


    public boolean isAssignableFrom(YType type)
    {
        return (type instanceof YComposedType && isSameOrSuperTypeOf((YComposedType)type));
    }


    public boolean equals(Object type)
    {
        return (type instanceof YComposedType && ((YComposedType)type).getCode().equals(getCode()));
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }


    private boolean isSameOrSuperTypeOf(YComposedType type)
    {
        if(type == null)
        {
            return false;
        }
        if(equals(type))
        {
            return true;
        }
        for(YComposedType superType = type.getSuperType(); superType != null; superType = superType.getSuperType())
        {
            if(equals(superType))
            {
                return true;
            }
        }
        return false;
    }
}
