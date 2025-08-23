package de.hybris.platform.platformbackoffice.bulkedit;

import com.hybris.backoffice.attributechooser.Attribute;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class BulkEditSelectedAttributesHelper
{
    public Collection<Attribute> findLeaves(Collection<Attribute> attributes)
    {
        return (Collection<Attribute>)attributes.stream().map(this::findLeaves).flatMap(Collection::stream).collect(Collectors.toSet());
    }


    private Collection<Attribute> findLeaves(Attribute attribute)
    {
        Collection<Attribute> result = new HashSet<>();
        if(!attribute.hasSubAttributes())
        {
            result.add(attribute);
        }
        for(Attribute child : attribute.getSubAttributes())
        {
            result.addAll(findLeaves(child));
        }
        return result;
    }
}
