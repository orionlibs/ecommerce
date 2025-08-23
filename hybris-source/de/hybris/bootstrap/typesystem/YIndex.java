package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.ddl.sql.IndexCreationMode;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YIndex extends YNameSpaceElement
{
    private final String enclosingTypeCode;
    private YComposedType enclosingType;
    private final String name;
    private final IndexCreationMode creationMode;
    private List<String> qualifiers;
    private List<String> includeQualifiers;
    private BitSet lower;
    private final boolean unique;
    private final boolean remove;
    private final boolean replace;


    public YIndex(YNamespace container, String enclosingType, String name, IndexCreationMode creationMode, boolean unique, boolean remove, boolean replace)
    {
        super(container);
        if(name == null || name.length() == 0)
        {
            throw new IllegalArgumentException("name is required, was '" + name + "'");
        }
        if(enclosingType == null || enclosingType.length() == 0)
        {
            throw new IllegalArgumentException("enclosing type code is required, was '" + enclosingType + "'");
        }
        this.name = name;
        this.creationMode = creationMode;
        this.unique = unique;
        this.enclosingTypeCode = enclosingType.intern();
        this.remove = remove;
        this.replace = replace;
    }


    public void validate()
    {
        super.validate();
        if(getIndexedAttributeQualifiers().isEmpty())
        {
            throw new IllegalStateException("invalid index " + this + " due to empty attributes set");
        }
        validateAttributeUniqueness(getIndexedAttributeQualifiers());
        validateAttributeUniqueness(getIndexedIncludeAttributeQualifiers());
        validateIndexedAttributesQualifiersAvailability(getIndexedAttributeQualifiers());
        validateIndexedAttributesQualifiersAvailability(getIndexedIncludeAttributeQualifiers());
        validateIfIncludeIndexedAttributeNotDefinedInIndexedAttribute(getIndexedIncludeAttributeQualifiers(), getIndexedAttributeQualifiers());
    }


    private void validateIfIncludeIndexedAttributeNotDefinedInIndexedAttribute(List<String> indexedIncludeAttributes, List<String> indexedAttributes)
    {
        Set<String> indexedAttribSet = new HashSet<>(indexedAttributes);
        for(String qualifier : indexedIncludeAttributes)
        {
            if(indexedAttribSet.contains(qualifier))
            {
                throw new IllegalStateException(String.format("Index %s has the '%s' attribute set as include, which is already used as key attribute in this index", new Object[] {this, qualifier}));
            }
        }
    }


    private void validateAttributeUniqueness(List<String> indexedAttributes)
    {
        if(indexedAttributes.size() != (new HashSet(indexedAttributes)).size())
        {
            throw new IllegalStateException("invalid index " + this + " due to duplicate attributes");
        }
    }


    private void validateIndexedAttributesQualifiersAvailability(List<String> indexedAttributes)
    {
        for(String qual : indexedAttributes)
        {
            if(!hasAttribute(qual))
            {
                throw new IllegalStateException("Index " + this + " based on attribute '" + qual + "' which is not available for the type " +
                                getEnclosingTypeCode());
            }
        }
    }


    private boolean hasAttribute(String qualifier)
    {
        return (getEnclosingType().getAttributeIncludingSuperType(qualifier) != null);
    }


    public String getEnclosingTypeCode()
    {
        return this.enclosingTypeCode;
    }


    public YComposedType getEnclosingType()
    {
        if(this.enclosingType == null)
        {
            YType tmp = getTypeSystem().getType(getEnclosingTypeCode());
            if(!(tmp instanceof YComposedType))
            {
                throw new IllegalStateException("invalid index " + this + " due to missing enclosing type '" +
                                getEnclosingTypeCode() + "'");
            }
            this.enclosingType = (YComposedType)tmp;
        }
        return this.enclosingType;
    }


    public String getName()
    {
        return this.name;
    }


    public void addIndexedAttributes(Map<String, Boolean> qualifierMap)
    {
        if(qualifierMap != null)
        {
            for(Map.Entry<String, Boolean> entry : qualifierMap.entrySet())
            {
                addIndexedAttribute(entry.getKey(), Boolean.TRUE.equals(entry.getValue()));
            }
        }
    }


    public void addIncludeAttributes(Collection<String> qualifierCollection)
    {
        if(qualifierCollection != null)
        {
            for(String value : qualifierCollection)
            {
                addIncludeAttribute(value);
            }
        }
    }


    public void addIncludeAttribute(String qualifier)
    {
        if(this.includeQualifiers == null)
        {
            this.includeQualifiers = new ArrayList<>();
        }
        this.includeQualifiers.add(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public void addIndexedAttribute(String qualifier, boolean lower)
    {
        if(this.lower == null)
        {
            this.qualifiers = new ArrayList<>();
            this.lower = new BitSet();
        }
        this.qualifiers.add(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(lower)
        {
            this.lower.set(this.qualifiers.size() - 1);
        }
    }


    public List<String> getIndexedAttributeQualifiers()
    {
        return (this.qualifiers != null) ? Collections.<String>unmodifiableList(this.qualifiers) : Collections.EMPTY_LIST;
    }


    public List<String> getIndexedIncludeAttributeQualifiers()
    {
        return (this.includeQualifiers != null) ? Collections.<String>unmodifiableList(this.includeQualifiers) : Collections.<String>emptyList();
    }


    public List<YAttributeDescriptor> getIndexedAttributes()
    {
        if(this.qualifiers == null || this.qualifiers.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<YAttributeDescriptor> attributes = new ArrayList<>(this.qualifiers.size());
        for(String q : this.qualifiers)
        {
            YAttributeDescriptor attDesc = getEnclosingType().getAttributeIncludingSuperType(q);
            if(attDesc == null)
            {
                throw new IllegalStateException("invalid index " + this + " due to missing attribute " + q + " from " + this.qualifiers);
            }
            attributes.add(attDesc);
        }
        return attributes;
    }


    public List<YAttributeDescriptor> getIncludeAttributes()
    {
        if(this.includeQualifiers == null || this.includeQualifiers.isEmpty())
        {
            return Collections.emptyList();
        }
        List<YAttributeDescriptor> attributes = new ArrayList<>(this.includeQualifiers.size());
        for(String q : this.includeQualifiers)
        {
            YAttributeDescriptor attDesc = getEnclosingType().getAttributeIncludingSuperType(q);
            if(attDesc == null)
            {
                throw new IllegalStateException("invalid index " + this + " due to missing attribute " + q + " from " + this.includeQualifiers);
            }
            attributes.add(attDesc);
        }
        return attributes;
    }


    public boolean isLower(String qualifier)
    {
        int pos = getIndexedAttributeQualifiers().indexOf(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(pos < 0)
        {
            throw new IllegalArgumentException("index " + this + " doesnt contain attribute " + qualifier);
        }
        return this.lower.get(pos);
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public List<Boolean> getLowerOptions()
    {
        List<Boolean> lowerOptions = new ArrayList<>(getIndexedAttributeQualifiers().size());
        for(String qualifier : getIndexedAttributeQualifiers())
        {
            lowerOptions.add(Boolean.valueOf(isLower(qualifier)));
        }
        return lowerOptions;
    }


    public boolean isRemove()
    {
        return this.remove;
    }


    public boolean isReplace()
    {
        return this.replace;
    }


    public String toString()
    {
        return getName() + " for type " + getName() + " on " + getEnclosingTypeCode() + " declared at " + getIndexedAttributeQualifiers();
    }


    public IndexCreationMode getCreationMode()
    {
        return this.creationMode;
    }
}
