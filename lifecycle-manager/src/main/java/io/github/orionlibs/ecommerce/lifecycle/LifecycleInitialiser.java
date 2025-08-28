package io.github.orionlibs.ecommerce.lifecycle;

import io.github.orionlibs.ecommerce.core.lifecycle.LifecycleDefinition;
import io.github.orionlibs.ecommerce.core.lifecycle.LifecycleDefinitionParser;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstanceModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstancesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class LifecycleInitialiser
{
    @Autowired private LifecycleInstancesDAO lifecycleInstancesDAO;
    @Autowired private LifecycleDefinitionParser lifecycleDefinitionParser;


    LifecycleInstanceModel initialiseLifecycle(LifecycleDefinitionModel definitionEntity)
    {
        LifecycleDefinition definition = lifecycleDefinitionParser.parseDefinition(definitionEntity.getPayload());
        LifecycleInstanceModel instance = new LifecycleInstanceModel();
        instance.setDefinitionKey(definitionEntity.getDefinitionKey());
        instance.setDefinitionVersion(definitionEntity.getVersion());
        instance.setCurrentState(definition.getFirstState());
        return lifecycleInstancesDAO.save(instance);
    }
}
