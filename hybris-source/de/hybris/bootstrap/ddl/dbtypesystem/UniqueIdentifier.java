package de.hybris.bootstrap.ddl.dbtypesystem;

public abstract class UniqueIdentifier
{
    public static <T> UniqueIdentifier createFrom(T uid)
    {
        return (UniqueIdentifier)new TypedUniqueId(uid);
    }


    public static UniqueIdentifier createFrom(long uid)
    {
        return (UniqueIdentifier)new TypedUniqueId(new Long(uid));
    }


    public static UniqueIdentifier combineFrom(String... parts)
    {
        StringBuilder result = new StringBuilder("{");
        for(String part : parts)
        {
            result.append("[");
            result.append(part);
            result.append("]");
        }
        result.append("}");
        return (UniqueIdentifier)new TypedUniqueId(result.toString());
    }
}
