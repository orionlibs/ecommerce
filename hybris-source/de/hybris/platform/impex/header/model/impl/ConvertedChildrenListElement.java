package de.hybris.platform.impex.header.model.impl;

import de.hybris.platform.impex.header.model.ChildrenListElement;
import de.hybris.platform.impex.header.model.DescriptorElement;

public class ConvertedChildrenListElement implements ChildrenListElement
{
    private final DescriptorElement[] elements;


    protected ConvertedChildrenListElement(int size)
    {
        this.elements = new DescriptorElement[size];
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Children[").append(this.elements.length).append("]{");
        for(DescriptorElement elem : this.elements)
        {
            builder.append(elem.toString()).append(",");
        }
        builder.append("}");
        return builder.toString();
    }


    public DescriptorElement[] getChildren()
    {
        return this.elements;
    }
}
