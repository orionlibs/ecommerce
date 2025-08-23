package de.hybris.platform.cockpit.util;

public class ListProviders
{
    public static final ListProvider EMPTY_LIST_PROVIDER = emptyListProvider();


    public static <T> ListProvider<T> singletonListProvider(T object)
    {
        return (ListProvider<T>)new Object(object);
    }


    public static <T> ListProvider<T> emptyListProvider()
    {
        return (ListProvider<T>)new Object();
    }
}
