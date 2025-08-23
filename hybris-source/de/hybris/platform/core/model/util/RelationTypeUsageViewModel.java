package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RelationTypeUsageViewModel extends ItemModel
{
    public static final String _TYPECODE = "RelationTypeUsageView";
    public static final String RELATIONTYPE = "relationType";
    public static final String COMPOSEDTYPE = "composedType";


    public RelationTypeUsageViewModel()
    {
    }


    public RelationTypeUsageViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RelationTypeUsageViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "composedType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getComposedType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("composedType");
    }


    @Accessor(qualifier = "relationType", type = Accessor.Type.GETTER)
    public RelationMetaTypeModel getRelationType()
    {
        return (RelationMetaTypeModel)getPersistenceContext().getPropertyValue("relationType");
    }
}
