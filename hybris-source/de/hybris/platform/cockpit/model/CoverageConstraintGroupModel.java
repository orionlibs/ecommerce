package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Set;

public class CoverageConstraintGroupModel extends ConstraintGroupModel
{
    public static final String _TYPECODE = "CoverageConstraintGroup";
    public static final String _COMPOSEDTYPE2COVERAGECONSTRAINTGROUPRELATION = "ComposedType2CoverageConstraintGroupRelation";
    public static final String COVERAGEDOMAINID = "coverageDomainID";
    public static final String DEDICATEDTYPES = "dedicatedTypes";


    public CoverageConstraintGroupModel()
    {
    }


    public CoverageConstraintGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CoverageConstraintGroupModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CoverageConstraintGroupModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "coverageDomainID", type = Accessor.Type.GETTER)
    public String getCoverageDomainID()
    {
        return (String)getPersistenceContext().getPropertyValue("coverageDomainID");
    }


    @Accessor(qualifier = "dedicatedTypes", type = Accessor.Type.GETTER)
    public Set<ComposedTypeModel> getDedicatedTypes()
    {
        return (Set<ComposedTypeModel>)getPersistenceContext().getPropertyValue("dedicatedTypes");
    }


    @Accessor(qualifier = "coverageDomainID", type = Accessor.Type.SETTER)
    public void setCoverageDomainID(String value)
    {
        getPersistenceContext().setPropertyValue("coverageDomainID", value);
    }


    @Accessor(qualifier = "dedicatedTypes", type = Accessor.Type.SETTER)
    public void setDedicatedTypes(Set<ComposedTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("dedicatedTypes", value);
    }
}
