package io.github.orionlibs.ecommerce.lifecycle;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LifecycleDefinitionParserTest
{
    @Autowired LifecycleDefinitionParser lifecycleDefinitionParser;


    @Test
    void parseDefinition()
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
        assertThat(result.states()).isEqualTo(List.of("STATE_1", "STATE_2", "STATE_3", "STATE_4"));
    }
}
