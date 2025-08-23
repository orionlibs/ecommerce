package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import java.util.Collections;
import java.util.Set;

public class AttributeCopyDescriptor
{
    public static final int ATOMIC = 1;
    public static final int PARTOF = 2;
    public static final int LOCALIZED = 4;
    public static final int UNIQUE = 8;
    public static final int INITIAL_ONLY = 16;
    public static final int OPTIONAL = 32;
    public static final int COPY_ON_DEMAND = 64;
    private final String qualifier;
    private AttributeDescriptor adCache;
    private final TypeCopyDescriptor parent;
    private final int modifiers;
    private boolean source;
    private long relationTypePK = -1L;
    private RelationType relationType = null;
    private Set<RelationDescriptor> relationAttributes;


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


    private void assureTypeInit()
    {
        if(this.relationTypePK == -1L)
        {
            AttributeDescriptor attributeDescriptor = getRealAttributeDescriptor();
            if(attributeDescriptor instanceof RelationDescriptor)
            {
                RelationDescriptor relationDescriptor = (RelationDescriptor)attributeDescriptor;
                this.relationType = relationDescriptor.getRelationType();
                if(this.relationType.isOneToMany())
                {
                    this.relationTypePK = 0L;
                }
                else
                {
                    if(relationDescriptor.getRelationType().getDeclaredAttributeDescriptors().isEmpty())
                    {
                        this.relationAttributes = Collections.EMPTY_SET;
                    }
                    else
                    {
                        this.relationAttributes = Collections.unmodifiableSet(relationDescriptor.getRelationType()
                                        .getDeclaredAttributeDescriptors());
                    }
                    this.source = relationDescriptor.isSource();
                    this.relationTypePK = this.relationType.getPK().getLongValue();
                }
            }
            else
            {
                this.relationTypePK = 0L;
            }
        }
    }


    public Set<RelationDescriptor> getRelationAttributes()
    {
        return (this.relationAttributes != null) ? this.relationAttributes : Collections.EMPTY_SET;
    }


    public final boolean isOneToManyRelation()
    {
        assureTypeInit();
        return (this.relationTypePK != 0L);
    }


    public RelationType getRelationType()
    {
        return this.relationType;
    }


    public final long getRelationTypePK()
    {
        assureTypeInit();
        return this.relationTypePK;
    }


    public final boolean isSource()
    {
        assureTypeInit();
        return this.source;
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


    public boolean isCopyOnDemand()
    {
        return ((this.modifiers & 0x40) == 64);
    }


    public AttributeDescriptor getRealAttributeDescriptor()
    {
        if(this.adCache == null)
        {
            this.adCache = this.parent.getTargetType().getAttributeDescriptorIncludingPrivate(this.qualifier);
        }
        return this.adCache;
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
        if(isCopyOnDemand())
        {
            stringBuilder.append("D");
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
