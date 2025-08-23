package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class CMSCopyContext extends ItemModelCloneCreator.CopyContext
{
    private static final Logger LOG = Logger.getLogger(CMSCopyContext.class);
    private final Map<ItemModel, ItemModelCloneCreator.CopyItem> items2copy;
    private final ModelCloningContext cloningContext;


    public CMSCopyContext()
    {
        this(null);
    }


    public CMSCopyContext(ModelCloningContext cloningContext)
    {
        super(cloningContext);
        this.items2copy = new LinkedHashMap<>();
        this.cloningContext = cloningContext;
    }


    protected boolean skipAttribute(Object original, String qualifier)
    {
        boolean skip = false;
        if(original instanceof ItemModel)
        {
            ComposedType ct = TypeManager.getInstance().getComposedType(((ItemModel)original).getItemtype());
            AttributeDescriptor ad = ct.getAttributeDescriptorIncludingPrivate(qualifier);
            if(ad instanceof RelationDescriptor)
            {
                RelationDescriptor rd = (RelationDescriptor)ad;
                skip = (rd.getRelationType().isOneToMany() && !ad.isPartOf() && !isOneEndAttributeOfRelation(rd) && !treatAsPartOf(original, qualifier));
            }
        }
        if(skip && LOG.isDebugEnabled())
        {
            LOG.debug("Skipping " + original + "." + qualifier);
        }
        return (skip || (this.cloningContext != null && this.cloningContext.skipAttribute(original, qualifier)));
    }


    protected boolean isOneEndAttributeOfRelation(RelationDescriptor rd)
    {
        return (rd.isProperty() || rd.getPersistenceQualifier() != null);
    }


    public Collection<ItemModelCloneCreator.CopyItem> getAll()
    {
        return this.items2copy.values();
    }
}
