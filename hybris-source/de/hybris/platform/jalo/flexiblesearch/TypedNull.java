package de.hybris.platform.jalo.flexiblesearch;

public class TypedNull
{
    private final Class elementType;


    public TypedNull(Class elementType)
    {
        this.elementType = elementType;
    }


    public Class getElementType()
    {
        return this.elementType;
    }
}
