package io.github.orionlibs.ecommerce.lifecycle;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinition;
import io.github.orionlibs.ecommerce.lifecycle.model.StateTransition;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class StateTransitionFinderTest
{
    @Autowired LifecycleDefinitionParser lifecycleDefinitionParser;
    @Autowired StateTransitionFinder stateTransitionFinder;


    @Test
    void findTransition()
    {
        LifecycleDefinition result = lifecycleDefinitionParser.parseDefinition("""
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
        Optional<StateTransition> resultWrap = stateTransitionFinder.findTransition(result, "STATE_2");
        assertThat(resultWrap.get().from()).isEqualTo("STATE_2");
        assertThat(resultWrap.get().to()).isEqualTo("STATE_3");
        resultWrap = stateTransitionFinder.findTransition(result, "STATE_2", "STATE_4");
        assertThat(resultWrap.get().from()).isEqualTo("STATE_2");
        assertThat(resultWrap.get().to()).isEqualTo("STATE_4");
        resultWrap = stateTransitionFinder.findTransition(result, "STATE_2000");
        assertThat(resultWrap.isEmpty()).isTrue();
        resultWrap = stateTransitionFinder.findTransition(result, "STATE_2", "STATE_5000");
        assertThat(resultWrap.isEmpty()).isTrue();
    }
}
