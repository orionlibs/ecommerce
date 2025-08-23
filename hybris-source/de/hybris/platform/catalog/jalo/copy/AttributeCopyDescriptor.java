package de.hybris.platform.catalog.jalo.copy;

import de.hybris.platform.jalo.type.AttributeDescriptor;

@Deprecated(since = "ages", forRemoval = false)
public class AttributeCopyDescriptor
{
    public static final int ATOMIC = 1;
    public static final int PARTOF = 2;
    public static final int LOCALIZED = 4;
    public static final int UNIQUE = 8;
    public static final int INITIAL_ONLY = 16;
    public static final int OPTIONAL = 32;
    private final String qualifier;
    private final TypeCopyDescriptor parent;
    private final int modifiers;


    public AttributeCopyDescriptor(TypeCopyDescriptor parent, String qualifier, int options)
    {
        if(parent == null || qualifier == null)
        {
            throw new NullPointerException("parent or qualifier was null");
        }
        this.parent = parent;
        this.qualifier = qualifier;
        this.modifiers = options;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public boolean isAtomic()
    {
        return ((this.modifiers & 0x1) == 1);
    }


    public boolean isPartOf()
    {
        return ((this.modifiers & 0x2) == 2);
    }


    public boolean isLocalized()
    {
        return ((this.modifiers & 0x4) == 4);
    }


    public boolean isUnique()
    {
        return ((this.modifiers & 0x8) == 8);
    }


    public boolean isInitialOnly()
    {
        return ((this.modifiers & 0x10) == 16);
    }


    public boolean isOptional()
    {
        return ((this.modifiers & 0x20) == 32);
    }


    public AttributeDescriptor getRealAttributeDescriptor()
    {
        return this.parent.getTargetType().getAttributeDescriptorIncludingPrivate(this.qualifier);
    }


    public int hashCode()
    {
        return this.parent.hashCode() ^ this.qualifier.hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj instanceof AttributeCopyDescriptor && this.parent.equals(((AttributeCopyDescriptor)obj).parent) && this.qualifier
                        .equals(((AttributeCopyDescriptor)obj).qualifier));
    }


    private String modifiersToString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(isAtomic())
        {
            stringBuilder.append("A");
        }
        if(isPartOf())
        {
            stringBuilder.append("P");
        }
        if(isLocalized())
        {
            stringBuilder.append("L");
        }
        if(isUnique())
        {
            stringBuilder.append("1");
        }
        if(isInitialOnly())
        {
            stringBuilder.append("c");
        }
        if(isOptional())
        {
            stringBuilder.append("?");
        }
        return stringBuilder.toString();
    }


    public String toString()
    {
        return getQualifier() + "[" + getQualifier() + "]";
    }
}
