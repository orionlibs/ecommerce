package io.github.orionlibs.ecommerce.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.lifecycle.model.Guard;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinition;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionsDAO;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstanceModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstancesDAO;
import io.github.orionlibs.ecommerce.lifecycle.model.StateTransition;
import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifecycleService
{
    @Autowired private LifecycleInstancesDAO lifecycleInstancesDAO;
    @Autowired private LifecycleDefinitionsDAO lifecycleDefinitionsDAO;
    @Qualifier("yamlObjectMapper") private ObjectMapper yamlMapper;
    @Autowired private SpelExpressionParser expressionParser;


    @Transactional
    public LifecycleInstanceModel processStateTransition(String instanceId)
    {
        LifecycleInstanceModel instance = lifecycleInstancesDAO.findById(UUID.fromString(instanceId))
                        .orElseThrow(() -> new RuntimeException("Instance not found"));
        LifecycleDefinitionModel definitionEntity = lifecycleDefinitionsDAO.findByKeyAndVersion(instance.getDefinitionKey(), instance.getDefinitionVersion())
                        .orElseThrow(() -> new RuntimeException("Definition not found"));
        LifecycleDefinition definition = parseDefinition(definitionEntity.getPayload());
        StateTransition transition = findTransition(definition, instance.getCurrentState());
        checkGuards(transition, instance);
        //String oldState = instance.getCurrentState();
        instance.setCurrentState(transition.to());
        LifecycleInstanceModel updatedInstance = lifecycleInstancesDAO.save(instance);
        return updatedInstance;
    }


    private void checkGuards(StateTransition transition, LifecycleInstanceModel instance)
    {
        if(transition.guards() == null || transition.guards().isEmpty())
        {
            return;
        }
        StandardEvaluationContext context = new StandardEvaluationContext(instance);
        for(Guard guard : transition.guards())
        {
            boolean passed = expressionParser.parseExpression(guard.expression()).getValue(context, Boolean.class);
            if(!passed)
            {
                throw new RuntimeException("Guard condition not met: " + guard.expression());
            }
        }
    }


    private StateTransition findTransition(LifecycleDefinition definition, String fromState)
    {
        return definition.transitions()
                        .stream()
                        .filter(t -> t.from().equals(fromState))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Invalid transition from state '" + fromState + "'"));
    }


    @SneakyThrows
    private LifecycleDefinition parseDefinition(String yamlPayload)
    {
        return yamlMapper.readValue(yamlPayload, LifecycleDefinition.class);
    }
}
