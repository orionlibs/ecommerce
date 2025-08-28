package io.github.orionlibs.ecommerce.lifecycle;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinitionModel;
import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleInstanceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LifecycleServiceTest
{
    @Autowired LifecycleService lifecycleService;


    @BeforeEach
    void setup()
    {
        lifecycleService.deleteAllDefinitions();
        lifecycleService.deleteAllInstances();
    }


    @Test
    void processStateTransition()
    {
        LifecycleDefinitionModel definition = new LifecycleDefinitionModel();
        definition.setDefinitionKey("key1");
        definition.setName("name1");
        definition.setPayload("""
                        key: "key1"
                        name: "name1"
                        version: 1
                        states:
                          - "STATE_1"
                          - "STATE_2"
                          - "STATE_3"
                          - "STATE_4"
                        transitions:
                          - name: "transition1"
                            from: "STATE_1"
                            to: "STATE_2"
                          - name: "transition2"
                            from: "STATE_2"
                            to: "STATE_3"
                          - name: "transition3"
                            from: "STATE_2"
                            to: "STATE_4"
                        """);
        definition.setVersion(1);
        definition = lifecycleService.saveDefinition(definition);
        LifecycleInstanceModel instance = lifecycleService.initialiseLifecycle(definition);
        LifecycleInstanceModel newInstance = lifecycleService.processStateTransition(instance.getId());
        assertThat(newInstance.getCurrentState()).isEqualTo("STATE_2");
        newInstance = lifecycleService.processStateTransition(newInstance.getId(), "STATE_4");
        assertThat(newInstance.getCurrentState()).isEqualTo("STATE_4");
    }
}
