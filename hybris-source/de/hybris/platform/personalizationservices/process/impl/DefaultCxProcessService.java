package de.hybris.platform.personalizationservices.process.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.CxProcessService;
import de.hybris.platform.personalizationservices.process.dao.CxPersonalizationBusinessProcessDao;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterStrategy;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterType;
import de.hybris.platform.personalizationservices.strategies.CxProcessKeyStrategy;
import de.hybris.platform.personalizationservices.strategies.ProcessSelectionStrategy;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxProcessService implements CxProcessService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxProcessService.class);
    private List<CxProcessParameterStrategy> cxProcessParameterStrategies = Collections.emptyList();
    private CxPersonalizationBusinessProcessDao cxPersonalizationBusinessProcessDao;
    private BusinessProcessService businessProcessService;
    private ProcessSelectionStrategy processSelectionStrategy;
    private CxProcessKeyStrategy cxProcessKeyStrategy;


    public CxPersonalizationProcessModel startPersonalizationCalculationProcess(UserModel user, CatalogVersionModel catalogVersion)
    {
        return startPersonalizationCalculationProcess(user, catalogVersion, null);
    }


    public CxPersonalizationProcessModel startPersonalizationCalculationProcess(UserModel user, CatalogVersionModel catalogVersion, Map<String, Object> parameters)
    {
        List<CatalogVersionModel> catalogVersionList = Collections.singletonList(catalogVersion);
        String processDefinitionName = this.processSelectionStrategy.retrieveProcessDefinitionName(user, catalogVersionList);
        String processKey = this.cxProcessKeyStrategy.getProcessKey(user, catalogVersion);
        if(!(user instanceof de.hybris.platform.core.model.user.CustomerModel))
        {
            LOG.error("personalization process should only be started for customers");
            return null;
        }
        if(StringUtils.isBlank(processDefinitionName) || isProcessStarted(processDefinitionName, processKey))
        {
            return null;
        }
        try
        {
            CxPersonalizationProcessModel process = createProcess(processDefinitionName, user, catalogVersionList, processKey, parameters);
            this.businessProcessService.startProcess((BusinessProcessModel)process);
            return process;
        }
        catch(RuntimeException e)
        {
            LOG.error("Personalization calculation process failed : '{}'. Turn on debug logging for more details", processDefinitionName);
            LOG.debug("Personalization calculation process failed", e);
            return null;
        }
    }


    protected boolean isProcessStarted(String processDefinitionName, String processKey)
    {
        List<CxPersonalizationProcessModel> activeBusinessProcesses = this.cxPersonalizationBusinessProcessDao.findActiveBusinessProcesses(processDefinitionName, processKey);
        if(CollectionUtils.isNotEmpty(activeBusinessProcesses))
        {
            LOG.debug("Found {} already running processes (definition name: {}, process key: {})", new Object[] {Integer.valueOf(activeBusinessProcesses.size()), processDefinitionName, processKey});
            return true;
        }
        return false;
    }


    protected CxPersonalizationProcessModel createProcess(String processDefinitionName, UserModel user, Collection<CatalogVersionModel> catalogVersions, String processKey, Map<String, Object> parameters)
    {
        CxPersonalizationProcessModel process;
        String processCode = String.format("%s-%s-%s", new Object[] {processDefinitionName, user.getUid(), UUID.randomUUID()});
        if(MapUtils.isEmpty(parameters))
        {
            process = (CxPersonalizationProcessModel)this.businessProcessService.createProcess(processCode, processDefinitionName);
        }
        else
        {
            process = (CxPersonalizationProcessModel)this.businessProcessService.createProcess(processCode, processDefinitionName, parameters);
        }
        process.setUser(user);
        process.setCatalogVersions(catalogVersions);
        process.setKey(processKey);
        storeAllParametersForProcess(process);
        return process;
    }


    public void loadAllParametersFromProcess(CxPersonalizationProcessModel process)
    {
        this.cxProcessParameterStrategies.forEach(s -> s.load(process));
    }


    public void storeAllParametersForProcess(CxPersonalizationProcessModel process)
    {
        this.cxProcessParameterStrategies.forEach(s -> s.store(process));
    }


    public void storeParametersForProcess(CxPersonalizationProcessModel process, CxProcessParameterType... cxProcessParameterTypes)
    {
        if(cxProcessParameterTypes == null)
        {
            return;
        }
        List<CxProcessParameterType> parameterTypeList = Arrays.asList(cxProcessParameterTypes);
        this.cxProcessParameterStrategies.stream()
                        .filter(s -> isTypeSupported(s, parameterTypeList))
                        .forEach(s -> s.store(process));
    }


    protected boolean isTypeSupported(CxProcessParameterStrategy strategy, Collection<CxProcessParameterType> parameters)
    {
        Objects.requireNonNull(strategy);
        return parameters.stream().anyMatch(strategy::supports);
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    @Required
    public void setCxPersonalizationBusinessProcessDao(CxPersonalizationBusinessProcessDao cxPersonalizationBusinessProcessDao)
    {
        this.cxPersonalizationBusinessProcessDao = cxPersonalizationBusinessProcessDao;
    }


    @Required
    public void setProcessSelectionStrategy(ProcessSelectionStrategy processSelectionStrategy)
    {
        this.processSelectionStrategy = processSelectionStrategy;
    }


    @Required
    public void setCxProcessKeyStrategy(CxProcessKeyStrategy cxProcessKeyStrategy)
    {
        this.cxProcessKeyStrategy = cxProcessKeyStrategy;
    }


    @Autowired
    public void setCxProcessParameterStrategies(List<CxProcessParameterStrategy> cxProcessParameterStrategies)
    {
        this
                        .cxProcessParameterStrategies = CollectionUtils.isNotEmpty(cxProcessParameterStrategies) ? cxProcessParameterStrategies : Collections.<CxProcessParameterStrategy>emptyList();
    }


    protected CxPersonalizationBusinessProcessDao getCxPersonalizationBusinessProcessDao()
    {
        return this.cxPersonalizationBusinessProcessDao;
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    protected ProcessSelectionStrategy getProcessSelectionStrategy()
    {
        return this.processSelectionStrategy;
    }


    protected CxProcessKeyStrategy getCxProcessKeyStrategy()
    {
        return this.cxProcessKeyStrategy;
    }
}
