package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.cms2.version.converter.attribute.data.VersionAttributeDescriptor;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.function.Predicate;

public class AtomicTypePredicate implements Predicate<VersionAttributeDescriptor>
{
    public boolean test(VersionAttributeDescriptor versionAttributeDescriptor)
    {
        TypeModel attributeType = versionAttributeDescriptor.getType();
        return attributeType.getItemtype().contains("AtomicType");
    }
}
