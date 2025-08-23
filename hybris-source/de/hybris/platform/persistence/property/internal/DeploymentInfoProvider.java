package de.hybris.platform.persistence.property.internal;

import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeploymentInfoProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DeploymentInfoProvider.class);


    public LoadDeploymentInfoResult loadAllDeploymentsForTenant(Tenant tenant)
    {
        Objects.requireNonNull(tenant);
        List<String> extNames = new ArrayList<>(tenant.getTenantSpecificExtensionNames());
        DeploymentImpl deployments = TypeSystemUtils.loadDeployments(extNames, false, false);
        return loadItemDeployments(deployments, tenant);
    }


    private LoadDeploymentInfoResult loadItemDeployments(DeploymentImpl infos, Tenant tenant)
    {
        Map<String, ItemDeployment> deploymentInfos = new LinkedHashMap<>(1000);
        Map<Integer, String> typeCode2JndiMappings = new HashMap<>(500);
        Map<String, String> javaDefMapping = infos.getJavaTypeMapping(tenant.getDataSource().getDatabaseName());
        for(String beanName : infos.getBeanIDs())
        {
            try
            {
                ItemDeployment depl = infos.getItemDeployment(beanName);
                deploymentInfos.put(beanName, depl);
                LOG.debug("loading deployment '{}', tc = {}", beanName, Integer.valueOf(depl.getTypeCode()));
                if(depl.getTypeCode() > 0)
                {
                    typeCode2JndiMappings.put(Integer.valueOf(depl.getTypeCode()), beanName);
                }
            }
            catch(Exception e)
            {
                LOG.error("********************************************");
                LOG.error("error getting itemdeployment for: {} - will continue without this deployment.", beanName);
                LOG.error("stack trace following: ", e);
                LOG.error("********************************************");
            }
        }
        return new LoadDeploymentInfoResult(deploymentInfos, Collections.unmodifiableMap(typeCode2JndiMappings),
                        Collections.unmodifiableMap(javaDefMapping));
    }
}
