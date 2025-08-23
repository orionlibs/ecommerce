package de.hybris.platform.util;

class Lang implements Comparable<Lang>
{
    private String isoCode;
    private double weight;


    public Lang(String isoCode, double weight)
    {
        this.isoCode = isoCode;
        this.weight = weight;
    }


    public Lang(String isoCode)
    {
        this.isoCode = isoCode;
        this.weight = 1.0D;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public double getWeight()
    {
        return this.weight;
    }


    public void setWeight(double weight)
    {
        this.weight = weight;
    }


    public int compareTo(Lang o)
    {
        return (int)(o.getWeight() * 10.0D) - (int)(this.weight * 10.0D);
    }
}
