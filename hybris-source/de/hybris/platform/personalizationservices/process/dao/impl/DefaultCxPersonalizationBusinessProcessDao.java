package de.hybris.platform.personalizationservices.process.dao.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.dao.CxPersonalizationBusinessProcessDao;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCxPersonalizationBusinessProcessDao extends AbstractCxDao<CxPersonalizationProcessModel> implements CxPersonalizationBusinessProcessDao
{
    public DefaultCxPersonalizationBusinessProcessDao()
    {
        super("CxPersonalizationProcess");
    }


    protected Collection<ProcessState> getActiveProcessStates()
    {
        return Sets.newHashSet((Object[])new ProcessState[] {ProcessState.CREATED, ProcessState.RUNNING, ProcessState.WAITING});
    }


    public List<CxPersonalizationProcessModel> findActiveBusinessProcesses(String processDefinitionName, String processKey)
    {
        ServicesUtil.validateParameterNotNull(processDefinitionName, "CxPersonalizationProcess processDefinitionName must not be null");
        String query = "SELECT DISTINCT({pk})  FROM {CxPersonalizationProcess as p   JOIN CxPersProcToCatVer as p2cv on {p.pk} = {p2cv.target}}  WHERE { p.processDefinitionName}= ?processDefinitionName  AND {p.state} IN (?processStates)  AND {p.key} = ?key";
        Map<String, Object> params = new HashMap<>();
        params.put("processDefinitionName", processDefinitionName);
        params.put("processStates", getActiveProcessStates());
        params.put("key", processKey);
        return queryList("SELECT DISTINCT({pk})  FROM {CxPersonalizationProcess as p   JOIN CxPersProcToCatVer as p2cv on {p.pk} = {p2cv.target}}  WHERE { p.processDefinitionName}= ?processDefinitionName  AND {p.state} IN (?processStates)  AND {p.key} = ?key", params);
    }
}
