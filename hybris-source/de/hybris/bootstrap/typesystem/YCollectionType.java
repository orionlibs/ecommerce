package de.hybris.bootstrap.typesystem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class YCollectionType extends YType
{
    private final String elementTypeCode;
    private final TypeOfCollection typeOfCollection;
    private YType elementType;


    public YCollectionType(YNamespace container, String code, String elementTypeCode, TypeOfCollection toc)
    {
        super(container, code);
        if(elementTypeCode == null)
        {
            throw new IllegalArgumentException("element type was null");
        }
        this.elementTypeCode = elementTypeCode;
        this.typeOfCollection = (toc != null) ? toc : TypeOfCollection.COLLECTION;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "CollectionType";
    }


    public void validate()
    {
        super.validate();
        getElementType();
    }


    public String getElementTypeCode()
    {
        return this.elementTypeCode;
    }


    public TypeOfCollection getTypeOfCollection()
    {
        return (this.typeOfCollection != null) ? this.typeOfCollection : TypeOfCollection.COLLECTION;
    }


    public YType getElementType()
    {
        if(this.elementType == null)
        {
            this.elementType = getTypeSystem().getType(getElementTypeCode());
        }
        if(this.elementType == null)
        {
            throw new IllegalStateException("invalid collection type " + this + " due to missing element type '" +
                            getElementTypeCode() + "'");
        }
        return this.elementType;
    }


    public String getJavaClassName()
    {
        StringBuilder result = new StringBuilder();
        switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YCollectionType$TypeOfCollection[getTypeOfCollection().ordinal()])
        {
            case 1:
                result.append(Set.class.getName());
                break;
            case 2:
                result.append(List.class.getName());
                break;
            case 3:
                result.append(Collection.class.getName());
                break;
        }
        result.append("<").append(getElementType().getJavaClassName()).append(">");
        return result.toString();
    }


    public boolean isAssignableFrom(YType type)
    {
        return (type instanceof YCollectionType && (
                        equals(type) || getElementType().isAssignableFrom(((YCollectionType)type).getElementType())));
    }


    public boolean equals(Object type)
    {
        return (type instanceof YCollectionType && ((YCollectionType)type).getCode().equals(getCode()));
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }
}
