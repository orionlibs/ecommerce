package de.hybris.platform.ticket.dao;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import java.util.List;

public interface AgentDao
{
    List<CsAgentGroupModel> findAgentGroups();


    List<CsAgentGroupModel> findAgentGroupsByBaseStore(BaseStoreModel paramBaseStoreModel);


    List<EmployeeModel> findAgents();


    List<EmployeeModel> findAgentsByBaseStore(BaseStoreModel paramBaseStoreModel);
}
