package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;

public class DefaultModelCloningContext implements ModelCloningContext
{
    public Object getPresetValue(Object original, String qualifier)
    {
        return null;
    }


    public boolean skipAttribute(Object original, String qualifier)
    {
        boolean skip = false;
        if(original instanceof ItemModel)
        {
            ComposedType ct = TypeManager.getInstance().getComposedType(((ItemModel)original).getItemtype());
            AttributeDescriptor ad = ct.getAttributeDescriptorIncludingPrivate(qualifier);
            if(ad instanceof RelationDescriptor)
            {
                RelationDescriptor rd = (RelationDescriptor)ad;
                skip = (rd.getRelationType().isOneToMany() && !ad.isPartOf() && !isOneEndAttribute(rd));
            }
        }
        if(skip)
        {
            System.err.println("Skipping " + original + "." + qualifier);
        }
        return skip;
    }


    private boolean isOneEndAttribute(RelationDescriptor rd)
    {
        return (rd.isProperty() || rd.getPersistenceQualifier() != null);
    }


    public boolean treatAsPartOf(Object original, String qualifier)
    {
        return false;
    }


    public boolean usePresetValue(Object original, String qualifier)
    {
        return false;
    }
}
