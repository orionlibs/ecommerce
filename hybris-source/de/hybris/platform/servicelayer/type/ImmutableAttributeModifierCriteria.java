package de.hybris.platform.servicelayer.type;

public class ImmutableAttributeModifierCriteria extends AttributeModifierCriteria
{
    public ImmutableAttributeModifierCriteria()
    {
        this(0, 0, 0);
    }


    public ImmutableAttributeModifierCriteria(int exclusive, int alternative, int disallowed)
    {
        super(exclusive, alternative, disallowed);
    }


    public void addAlternative(int modifier)
    {
        throw new UnsupportedOperationException();
    }


    public void addDisallowed(int modifier)
    {
        throw new UnsupportedOperationException();
    }


    public void addRequired(int modifier)
    {
        throw new UnsupportedOperationException();
    }
}
