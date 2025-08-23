package de.hybris.platform.cockpit.wizards;

public class Transition
{
    private String[] ifAttributesTrue;
    private String[] ifAttributesFalse;
    private String destination;
    private String source;


    public String getDestination()
    {
        return this.destination;
    }


    public void setDestination(String destination)
    {
        this.destination = destination;
    }


    public void setSource(String source)
    {
        this.source = source;
    }


    public String getSource()
    {
        return this.source;
    }


    public String[] getIfAttributesTrue()
    {
        return this.ifAttributesTrue;
    }


    public void setIfAttributesTrue(String[] ifAttributesTrue)
    {
        this.ifAttributesTrue = ifAttributesTrue;
    }


    public String[] getIfAttributesFalse()
    {
        return this.ifAttributesFalse;
    }


    public void setIfAttributesFalse(String[] ifAttributesFalse)
    {
        this.ifAttributesFalse = ifAttributesFalse;
    }
}
