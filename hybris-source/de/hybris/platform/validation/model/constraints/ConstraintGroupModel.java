package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class ConstraintGroupModel extends ItemModel
{
    public static final String _TYPECODE = "ConstraintGroup";
    public static final String _CONSTRAINTGROUP2ABSTRACTCONSTRAINTRELATION = "ConstraintGroup2AbstractConstraintRelation";
    public static final String ID = "id";
    public static final String INTERFACENAME = "interfaceName";
    public static final String CONSTRAINTS = "constraints";


    public ConstraintGroupModel()
    {
    }


    public ConstraintGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConstraintGroupModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConstraintGroupModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
    public Set<AbstractConstraintModel> getConstraints()
    {
        return (Set<AbstractConstraintModel>)getPersistenceContext().getPropertyValue("constraints");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "interfaceName", type = Accessor.Type.GETTER)
    public String getInterfaceName()
    {
        return (String)getPersistenceContext().getPropertyValue("interfaceName");
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
    public void setConstraints(Set<AbstractConstraintModel> value)
    {
        getPersistenceContext().setPropertyValue("constraints", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "interfaceName", type = Accessor.Type.SETTER)
    public void setInterfaceName(String value)
    {
        getPersistenceContext().setPropertyValue("interfaceName", value);
    }
}
