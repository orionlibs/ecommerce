package de.hybris.platform.persistence.audit.payload.json;

public class ValueType
{
    private final String type;
    private final String collection;


    public ValueType()
    {
        this.type = null;
        this.collection = null;
    }


    private ValueType(String type, String collection)
    {
        this.type = type;
        this.collection = collection;
    }


    public static ValueType newType(String type)
    {
        return new ValueType(type, "");
    }


    public static ValueType newCollectionType(String type, String collection)
    {
        return new ValueType(type, collection);
    }


    public String getType()
    {
        return this.type;
    }


    public String getCollection()
    {
        return this.collection;
    }
}
