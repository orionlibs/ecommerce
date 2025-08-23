package com.hybris.cis.api.model;

public class Pair<K, V>
{
    private K key;
    private V value;


    public Pair()
    {
        this.key = null;
        this.value = null;
    }


    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }


    public K getKey()
    {
        return this.key;
    }


    public void setKey(K key)
    {
        this.key = key;
    }


    public V getValue()
    {
        return this.value;
    }


    public void setValue(V value)
    {
        this.value = value;
    }


    public static <K, V> Pair<K, V> newInstance(K key, V value)
    {
        return new Pair<>(key, value);
    }
}
