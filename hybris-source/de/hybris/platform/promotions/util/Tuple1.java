package de.hybris.platform.promotions.util;

public class Tuple1<T1>
{
    private T1 t1;


    public Tuple1(T1 t1)
    {
        this.t1 = t1;
    }


    public T1 getFirst()
    {
        return this.t1;
    }


    public void setFirst(T1 t1)
    {
        this.t1 = t1;
    }
}
