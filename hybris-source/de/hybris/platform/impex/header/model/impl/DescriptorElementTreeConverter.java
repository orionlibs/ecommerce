package de.hybris.platform.impex.header.model.impl;

import de.hybris.platform.impex.header.model.AlternativeListElement;
import de.hybris.platform.impex.header.model.ChildrenListElement;
import de.hybris.platform.impex.header.model.DescriptorElement;
import de.hybris.platform.impex.header.model.ValueElement;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public final class DescriptorElementTreeConverter
{
    public static DescriptorElement convertToDescriptorElement(List<AbstractDescriptor.ColumnParams>[] params)
    {
        if(params == null || params.length < 1)
        {
            return null;
        }
        return createElement(params);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static List<AbstractDescriptor.ColumnParams>[] convertFromDescriptorElement(DescriptorElement param)
    {
        List[] arrayOfList;
        List<AbstractDescriptor.ColumnParams>[] ret = null;
        if(param instanceof AlternativeListElement)
        {
            AlternativeListElement elem = (AlternativeListElement)param;
            arrayOfList = new List[(elem.getAlternatives()).length];
            for(int i = 0; i < (elem.getAlternatives()).length; i++)
            {
                arrayOfList[i] = createColumnParamList(elem.getAlternatives()[i]);
            }
        }
        else if(param instanceof ChildrenListElement)
        {
            ChildrenListElement elem = (ChildrenListElement)param;
            arrayOfList = new List[] {new ArrayList()};
            for(int i = 0; i < (elem.getChildren()).length; i++)
            {
                arrayOfList[0].add(createColumParam(elem.getChildren()[i]));
            }
        }
        else if(param instanceof ValueElement)
        {
            ValueElement elem = (ValueElement)param;
            arrayOfList = new List[] {new ArrayList()};
            arrayOfList[0].add(createColumParam((DescriptorElement)elem));
        }
        return (List<AbstractDescriptor.ColumnParams>[])arrayOfList;
    }


    private static AbstractDescriptor.ColumnParams createColumParam(DescriptorElement elem)
    {
        AbstractDescriptor.ColumnParams ret = null;
        if(elem instanceof ValueElement)
        {
            ret = new AbstractDescriptor.ColumnParams(((ValueElement)elem).getQualifier(), (List[])convertFromDescriptorElement(((ValueElement)elem)
                            .getSpecifier()));
            ret.addAllModifier(((ValueElement)elem).getModifiers());
        }
        else
        {
            throw new InvalidParameterException("Unable to convert from new to old structure: DescriptorElement to List<ColumnParams>[]");
        }
        return ret;
    }


    private static List<AbstractDescriptor.ColumnParams> createColumnParamList(DescriptorElement elem)
    {
        List<AbstractDescriptor.ColumnParams> ret = null;
        if(elem instanceof ChildrenListElement)
        {
            ChildrenListElement children = (ChildrenListElement)elem;
            ret = new ArrayList<>();
            for(int i = 0; i < (children.getChildren()).length; i++)
            {
                ret.add(createColumParam(children.getChildren()[i]));
            }
        }
        else if(elem instanceof ValueElement)
        {
            ret = new ArrayList<>();
            ret.add(createColumParam(elem));
        }
        return ret;
    }


    private static DescriptorElement createElement(List<AbstractDescriptor.ColumnParams>[] params)
    {
        if(params == null || params.length < 1)
        {
            return null;
        }
        if(params.length == 1)
        {
            return createElement(params[0]);
        }
        ConvertedAlternativeListElement convertedAlternativeListElement = new ConvertedAlternativeListElement(params.length);
        for(int i = 0; i < params.length; i++)
        {
            convertedAlternativeListElement.getAlternatives()[i] = createElement(params[i]);
        }
        return (DescriptorElement)convertedAlternativeListElement;
    }


    private static DescriptorElement createElement(List<AbstractDescriptor.ColumnParams> list)
    {
        if(list == null || list.size() < 0)
        {
            return null;
        }
        if(list.size() == 1)
        {
            return convertToDescriptorElement(list.get(0));
        }
        ConvertedChildrenListElement elem = new ConvertedChildrenListElement(list.size());
        int lenght = list.size();
        for(int i = 0; i < lenght; i++)
        {
            elem.getChildren()[i] = convertToDescriptorElement(list.get(i));
        }
        return (DescriptorElement)elem;
    }


    public static DescriptorElement convertToDescriptorElement(AbstractDescriptor.ColumnParams columnParams)
    {
        if(columnParams == null)
        {
            return null;
        }
        List[] arrayOfList = columnParams.getItemPatternLists();
        return (DescriptorElement)new ConveredValueElement(columnParams.getQualifier(), columnParams.getModifiers(),
                        createElement((List<AbstractDescriptor.ColumnParams>[])arrayOfList));
    }
}
