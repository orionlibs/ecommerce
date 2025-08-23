package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class TypeSystemRelationAttributesProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeSystemRelationAttributesProvider.class);


    public RelationAttributes get(PK typePk)
    {
        Objects.requireNonNull(typePk);
        try
        {
            Item item = JaloSession.lookupItem(typePk);
            if(item instanceof RelationType)
            {
                return getRelationAttributes((RelationType)item);
            }
            if(item instanceof ComposedType)
            {
                return getRelationAttributes((ComposedType)item);
            }
            LOGGER.warn("Unsupported item type `{}`", item);
            return RelationAttributes.empty();
        }
        catch(JaloItemNotFoundException | IllegalArgumentException e)
        {
            LOGGER.warn("Not able to found an item for `{}`.", typePk, e);
            return RelationAttributes.empty();
        }
    }


    private RelationAttributes getRelationAttributes(RelationType relationType)
    {
        if(relationType == null || relationType.isOneToMany())
        {
            return RelationAttributes.empty();
        }
        boolean isSourceNavigable = relationType.isSourceNavigable();
        boolean isTargetNavigable = relationType.isTargetNavigable();
        String relationTypeCode = relationType.getCode();
        if(isSourceNavigable && isTargetNavigable)
        {
            return (RelationAttributes)new LinkRelationAttributes(relationTypeCode, relationType
                            .getSourceAttributeDescriptor().getQualifier(), relationType
                            .getTargetAttributeDescriptor().getQualifier());
        }
        if(isSourceNavigable)
        {
            return (RelationAttributes)new SingleRelationAttribute(relationTypeCode, "source", relationType
                            .getSourceAttributeDescriptor().getQualifier());
        }
        if(isTargetNavigable)
        {
            return (RelationAttributes)new SingleRelationAttribute(relationTypeCode, "target", relationType
                            .getTargetAttributeDescriptor().getQualifier());
        }
        return RelationAttributes.empty();
    }


    private RelationAttributes getRelationAttributes(ComposedType composedType)
    {
        Map<String, String> fkToMany = new HashMap<>();
        for(AttributeDescriptor attributeDescriptor : composedType.getAttributeDescriptorsIncludingPrivate())
        {
            if(!(attributeDescriptor instanceof RelationDescriptor))
            {
                continue;
            }
            RelationDescriptor relationDescriptor = (RelationDescriptor)attributeDescriptor;
            RelationType relationType = relationDescriptor.getRelationType();
            if(!relationType.isOneToMany())
            {
                continue;
            }
            if(relationDescriptor.isSource() ? relationType.isSourceTypeOne() : relationType.isTargetTypeOne())
            {
            }
            else
            {
            }
            boolean isManySide = true;
            if(!isManySide)
            {
                continue;
            }
            if(relationType.isSourceTypeOne())
            {
                fkToMany.put(relationType.getTargetAttributeDescriptor().getQualifier(), relationType
                                .getSourceAttributeDescriptor().getQualifier());
                continue;
            }
            fkToMany.put(relationType.getSourceAttributeDescriptor().getQualifier(), relationType
                            .getTargetAttributeDescriptor().getQualifier());
        }
        return (RelationAttributes)new ManyRelationAttributes(composedType.getCode(), fkToMany);
    }
}
