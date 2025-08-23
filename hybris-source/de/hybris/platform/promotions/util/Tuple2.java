package de.hybris.platform.promotions.util;

public class Tuple2<T1, T2> extends Tuple1<T1>
{
    private T2 t2;


    public Tuple2(T1 t1, T2 t2)
    {
        super(t1);
        this.t2 = t2;
    }


    public T2 getSecond()
    {
        return this.t2;
    }


    public void setSecond(T2 t2)
    {
        this.t2 = t2;
    }
}
