package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.MethodWriter;

public class AttributeMethodDelegate extends MethodWriter
{
    private final AbstractAttributeMethodWriter target;


    public AttributeMethodDelegate(AbstractAttributeMethodWriter target)
    {
        super(target.getVisibility(), target.getReturnType(), target.getName());
        setModifiers(target.getModifiers() & 0xFFFFFFF7);
        this.target = target;
    }


    public AbstractAttributeMethodWriter getTarget()
    {
        return this.target;
    }
}
