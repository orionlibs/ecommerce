package de.hybris.platform.ruleengine.test;

import com.google.common.base.Preconditions;

public class Fibonacci
{
    private int sequence;
    private long value;


    public Fibonacci(int sequence)
    {
        this.sequence = sequence;
        this.value = -1L;
    }


    public long getValue()
    {
        Preconditions.checkArgument((this.sequence > -1), "Illegal sequence [" + this.sequence + "]");
        if(this.value > -1L)
        {
            return this.value;
        }
        if(this.sequence == 0 || this.sequence == 1)
        {
            this.value = this.sequence;
        }
        else if(this.sequence > 1)
        {
            this.value = (new Fibonacci(this.sequence - 2)).getValue() + (new Fibonacci(this.sequence - 1)).getValue();
        }
        return this.value;
    }


    public int getSequence()
    {
        return this.sequence;
    }


    public static void main(String[] args)
    {
        System.out.println("Fibonacci sequence: ");
        for(int i = 0; i < 20; i++)
        {
            System.out.println((new Fibonacci(i)).getValue());
        }
    }
}
