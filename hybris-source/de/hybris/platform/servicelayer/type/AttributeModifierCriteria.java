package de.hybris.platform.servicelayer.type;

public class AttributeModifierCriteria
{
    private int exclusive;
    private int alternative;
    private int disallowed;


    public AttributeModifierCriteria(int exclusive, int alternative, int disallowed)
    {
        this.exclusive = exclusive;
        this.alternative = alternative;
        this.disallowed = disallowed;
    }


    public void addRequired(int modifier)
    {
        this.exclusive |= modifier;
    }


    public void addAlternative(int modifier)
    {
        this.alternative |= modifier;
    }


    public void addDisallowed(int modifier)
    {
        this.disallowed |= modifier;
    }


    public boolean matches(int actualAttributeModifiers)
    {
        return ((this.exclusive & actualAttributeModifiers) == this.exclusive && (this.alternative & actualAttributeModifiers) > 0 && (this.disallowed & actualAttributeModifiers) == 0);
    }
}
