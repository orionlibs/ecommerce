package io.github.orionlibs.ecommerce.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.lifecycle.LifecycleDefinition;
import io.github.orionlibs.ecommerce.core.lifecycle.LifecycleDefinitionParser;
import io.github.orionlibs.ecommerce.core.lifecycle.StateTransition;
import io.github.orionlibs.ecommerce.core.lifecycle.StateTransitionFinder;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionsDAO;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstanceModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstancesDAO;
import jakarta.annotation.Resource;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifecycleService
{
    @Autowired private LifecycleInstancesDAO lifecycleInstancesDAO;
    @Autowired private LifecycleDefinitionsDAO lifecycleDefinitionsDAO;
    @Autowired private StateTransitionFinder stateTransitionFinder;
    @Autowired private LifecycleDefinitionParser lifecycleDefinitionParser;
    @Autowired private LifecycleInitialiser lifecycleInitialiser;
    @Resource(name = "yamlObjectMapper") private ObjectMapper yamlMapper;


    @Transactional
    public LifecycleInstanceModel initialiseLifecycle(LifecycleDefinitionModel definitionEntity)
    {
        return lifecycleInitialiser.initialiseLifecycle(definitionEntity);
    }


    @Transactional
    public LifecycleInstanceModel processStateTransition(UUID instanceId)
    {
        LifecycleInstanceModel instance = lifecycleInstancesDAO.findById(instanceId)
                        .orElseThrow(() -> new RuntimeException("Instance not found"));
        LifecycleDefinitionModel definitionEntity = lifecycleDefinitionsDAO.findByKeyAndVersion(instance.getDefinitionKey(), instance.getDefinitionVersion())
                        .orElseThrow(() -> new RuntimeException("Definition not found"));
        LifecycleDefinition definition = lifecycleDefinitionParser.parseDefinition(definitionEntity.getPayload());
        Optional<StateTransition> transition = stateTransitionFinder.findTransition(definition, instance.getCurrentState());
        if(transition.isPresent())
        {
            instance.setCurrentState(transition.get().to());
            LifecycleInstanceModel updatedInstance = saveInstance(instance);
            return updatedInstance;
        }
        else
        {
            throw new RuntimeException("Transition not found");
        }
    }


    @Transactional
    public LifecycleInstanceModel processStateTransition(UUID instanceId, String stateToTransitionTo)
    {
        LifecycleInstanceModel instance = lifecycleInstancesDAO.findById(instanceId)
                        .orElseThrow(() -> new RuntimeException("Instance not found"));
        LifecycleDefinitionModel definitionEntity = lifecycleDefinitionsDAO.findByKeyAndVersion(instance.getDefinitionKey(), instance.getDefinitionVersion())
                        .orElseThrow(() -> new RuntimeException("Definition not found"));
        LifecycleDefinition definition = lifecycleDefinitionParser.parseDefinition(definitionEntity.getPayload());
        Optional<StateTransition> transition = stateTransitionFinder.findTransition(definition, instance.getCurrentState(), stateToTransitionTo);
        if(transition.isPresent())
        {
            instance.setCurrentState(stateToTransitionTo);
            LifecycleInstanceModel updatedInstance = saveInstance(instance);
            return updatedInstance;
        }
        else
        {
            throw new RuntimeException("Transition not found");
        }
    }


    @Transactional
    public LifecycleDefinitionModel saveDefinition(LifecycleDefinitionModel model)
    {
        return lifecycleDefinitionsDAO.saveAndFlush(model);
    }


    @Transactional
    public LifecycleInstanceModel saveInstance(LifecycleInstanceModel model)
    {
        return lifecycleInstancesDAO.saveAndFlush(model);
    }


    @Transactional
    public void deleteAllDefinitions()
    {
        lifecycleDefinitionsDAO.deleteAll();
    }


    @Transactional
    public void deleteAllInstances()
    {
        lifecycleInstancesDAO.deleteAll();
    }
}
