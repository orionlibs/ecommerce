package de.hybris.bootstrap.typesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YAtomicType extends YType
{
    private final String superTypeCode;
    private Set<YAtomicType> subtypes;
    private YAtomicType superType;


    public YAtomicType(YNamespace container, String className, String superClassName)
    {
        super(container, className);
        this.superTypeCode = superClassName;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "AtomicType";
    }


    public YComposedType getMetaType()
    {
        if(getMetaTypeCode() != null || getSuperClassName() == null)
        {
            return super.getMetaType();
        }
        return getSuperType().getMetaType();
    }


    public void validate()
    {
        super.validate();
        getSuperType();
        if(getSuperType() != null && !getSuperType().getSubtypes().contains(this))
        {
            throw new IllegalStateException("invalid atomic type " + this + " since it doesnt seem to be assigned to supertype " +
                            getSuperType());
        }
        getJavaClass();
    }


    public String getSuperClassName()
    {
        return this.superTypeCode;
    }


    public YAtomicType getSuperType()
    {
        if(getSuperClassName() == null)
        {
            return null;
        }
        if(this.superType == null)
        {
            YType type = getTypeSystem().getType(getSuperClassName());
            if(!(type instanceof YAtomicType))
            {
                throw new IllegalStateException("invalid atomic type " + this + " due to missing super type '" +
                                getSuperClassName() + "'");
            }
            this.superType = (YAtomicType)type;
        }
        return this.superType;
    }


    public Set<YAtomicType> getSubtypes()
    {
        if(this.subtypes == null)
        {
            this.subtypes = new HashSet<>(getTypeSystem().getSubtypes(getCode()));
        }
        return this.subtypes;
    }


    public Class getJavaClass()
    {
        return getTypeSystem().resolveClass(this, getCode());
    }


    private static final Map<Class, Class> PRIMITVE_TYPES_MAPPING = (Map)new HashMap<>();

    static
    {
        PRIMITVE_TYPES_MAPPING.put(Boolean.class, boolean.class);
        PRIMITVE_TYPES_MAPPING.put(boolean.class, boolean.class);
        PRIMITVE_TYPES_MAPPING.put(Character.class, char.class);
        PRIMITVE_TYPES_MAPPING.put(char.class, char.class);
        PRIMITVE_TYPES_MAPPING.put(Byte.class, byte.class);
        PRIMITVE_TYPES_MAPPING.put(byte.class, byte.class);
        PRIMITVE_TYPES_MAPPING.put(Short.class, short.class);
        PRIMITVE_TYPES_MAPPING.put(short.class, short.class);
        PRIMITVE_TYPES_MAPPING.put(Integer.class, int.class);
        PRIMITVE_TYPES_MAPPING.put(int.class, int.class);
        PRIMITVE_TYPES_MAPPING.put(Long.class, long.class);
        PRIMITVE_TYPES_MAPPING.put(long.class, long.class);
        PRIMITVE_TYPES_MAPPING.put(Float.class, float.class);
        PRIMITVE_TYPES_MAPPING.put(float.class, float.class);
        PRIMITVE_TYPES_MAPPING.put(Double.class, double.class);
        PRIMITVE_TYPES_MAPPING.put(double.class, double.class);
    }

    public Class getPrimitiveJavaClass()
    {
        return PRIMITVE_TYPES_MAPPING.get(getJavaClass());
    }


    public String getJavaClassName()
    {
        return getCode();
    }


    public boolean isAssignableFrom(YType type)
    {
        return (type instanceof YAtomicType && isSameOrSuperTypeOf((YAtomicType)type));
    }


    private boolean isSameOrSuperTypeOf(YAtomicType type)
    {
        if(type == null)
        {
            return false;
        }
        if(equals(type))
        {
            return true;
        }
        for(YAtomicType superType = type.getSuperType(); superType != null; superType = superType.getSuperType())
        {
            if(equals(superType))
            {
                return true;
            }
        }
        return false;
    }


    public boolean equals(Object type)
    {
        return (type instanceof YAtomicType && ((YAtomicType)type).getCode().equals(getCode()));
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }
}
