package de.hybris.platform.ticket.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

public class CsAgentGroupModel extends UserGroupModel
{
    public static final String _TYPECODE = "CsAgentGroup";
    public static final String EMAILDISTRIBUTIONLIST = "emailDistributionList";
    public static final String DEFAULTASSIGNEE = "defaultAssignee";
    public static final String TICKETSTORES = "ticketstores";


    public CsAgentGroupModel()
    {
    }


    public CsAgentGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsAgentGroupModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsAgentGroupModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "defaultAssignee", type = Accessor.Type.GETTER)
    public EmployeeModel getDefaultAssignee()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("defaultAssignee");
    }


    @Accessor(qualifier = "emailDistributionList", type = Accessor.Type.GETTER)
    public String getEmailDistributionList()
    {
        return (String)getPersistenceContext().getPropertyValue("emailDistributionList");
    }


    @Accessor(qualifier = "ticketstores", type = Accessor.Type.GETTER)
    public List<BaseStoreModel> getTicketstores()
    {
        return (List<BaseStoreModel>)getPersistenceContext().getPropertyValue("ticketstores");
    }


    @Accessor(qualifier = "defaultAssignee", type = Accessor.Type.SETTER)
    public void setDefaultAssignee(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("defaultAssignee", value);
    }


    @Accessor(qualifier = "emailDistributionList", type = Accessor.Type.SETTER)
    public void setEmailDistributionList(String value)
    {
        getPersistenceContext().setPropertyValue("emailDistributionList", value);
    }


    @Accessor(qualifier = "ticketstores", type = Accessor.Type.SETTER)
    public void setTicketstores(List<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("ticketstores", value);
    }
}
