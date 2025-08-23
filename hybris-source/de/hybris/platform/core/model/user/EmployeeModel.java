package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.List;

public class EmployeeModel extends UserModel
{
    public static final String _TYPECODE = "Employee";
    public static final String _B2BUNIT2EMPLOYEE = "B2BUnit2Employee";
    public static final String ORGANIZATIONROLES = "organizationRoles";
    public static final String UNIT = "Unit";
    public static final String TICKETSTORES = "ticketstores";


    public EmployeeModel()
    {
    }


    public EmployeeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmployeeModel(boolean _loginDisabled, String _uid)
    {
        setLoginDisabled(_loginDisabled);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmployeeModel(Collection<CustomerReviewModel> _customerReviews, boolean _loginDisabled, ItemModel _owner, String _uid)
    {
        setCustomerReviews(_customerReviews);
        setLoginDisabled(_loginDisabled);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "organizationRoles", type = Accessor.Type.GETTER)
    public Collection<PrincipalGroupModel> getOrganizationRoles()
    {
        return (Collection<PrincipalGroupModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "organizationRoles");
    }


    @Accessor(qualifier = "ticketstores", type = Accessor.Type.GETTER)
    public List<BaseStoreModel> getTicketstores()
    {
        return (List<BaseStoreModel>)getPersistenceContext().getPropertyValue("ticketstores");
    }


    @Accessor(qualifier = "ticketstores", type = Accessor.Type.SETTER)
    public void setTicketstores(List<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("ticketstores", value);
    }
}
