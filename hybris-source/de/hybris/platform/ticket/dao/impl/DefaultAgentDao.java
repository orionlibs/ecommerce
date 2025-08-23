package de.hybris.platform.ticket.dao.impl;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.ticket.dao.AgentDao;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import java.util.Collections;
import java.util.List;

public class DefaultAgentDao extends AbstractItemDao implements AgentDao
{
    public List<CsAgentGroupModel> findAgentGroups()
    {
        SearchResult<CsAgentGroupModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsAgentGroup}");
        return result.getResult();
    }


    public List<CsAgentGroupModel> findAgentGroupsByBaseStore(BaseStoreModel baseStore)
    {
        String query = "\tSELECT {relation:source} \tFROM {" + GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE + " as relation }\tWHERE {relation:target} = ?store";
        SearchResult<CsAgentGroupModel> result = getFlexibleSearchService().search(query,
                        Collections.singletonMap("store", baseStore));
        return result.getResult();
    }


    public List<EmployeeModel> findAgents()
    {
        SearchResult<EmployeeModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {Employee}");
        return result.getResult();
    }


    public List<EmployeeModel> findAgentsByBaseStore(BaseStoreModel baseStore)
    {
        String query = "\tSELECT {relation:source} \tFROM {" + GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE + " as relation }\tWHERE {relation:target} = ?store";
        SearchResult<EmployeeModel> result = getFlexibleSearchService().search(query,
                        Collections.singletonMap("store", baseStore));
        return result.getResult();
    }
}
