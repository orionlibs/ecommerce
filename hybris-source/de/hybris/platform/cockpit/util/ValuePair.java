package de.hybris.platform.cockpit.util;

public class ValuePair<T, X>
{
    private final T value1;
    private final X value2;


    public ValuePair(T value1, X value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }


    public T getFirst()
    {
        return this.value1;
    }


    public X getSecond()
    {
        return this.value2;
    }
}
