package de.hybris.platform.impex.header.model.impl;

import de.hybris.platform.impex.header.model.AlternativeListElement;
import de.hybris.platform.impex.header.model.DescriptorElement;

public class ConvertedAlternativeListElement implements AlternativeListElement
{
    private final DescriptorElement[] elements;


    protected ConvertedAlternativeListElement(int size)
    {
        this.elements = new DescriptorElement[size];
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Alternatives[").append(this.elements.length).append("]{");
        for(DescriptorElement elem : this.elements)
        {
            builder.append(elem.toString()).append(",");
        }
        builder.append("}");
        return builder.toString();
    }


    public DescriptorElement[] getAlternatives()
    {
        return this.elements;
    }
}
