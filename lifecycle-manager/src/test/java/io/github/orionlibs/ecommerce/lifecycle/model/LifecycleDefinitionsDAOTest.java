package io.github.orionlibs.ecommerce.lifecycle.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LifecycleDefinitionsDAOTest
{
    @Autowired LifecycleDefinitionsDAO dao;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
    }


    @Test
    void findById()
    {
        LifecycleDefinitionModel model1 = new LifecycleDefinitionModel();
        model1.setDefinitionKey("key1");
        model1.setName("name1");
        model1.setPayload("""
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
                            guards:
                              - expression: "metadata['isReviewed'] == true"
                          - name: "transition3"
                            from: "STATE_2"
                            to: "STATE_4"
                        """);
        model1.setVersion(1);
        model1 = dao.saveAndFlush(model1);
        LifecycleDefinitionModel model2 = new LifecycleDefinitionModel();
        model2.setDefinitionKey("key2");
        model2.setName("name2");
        model2.setPayload("");
        model2.setVersion(2);
        model2 = dao.saveAndFlush(model2);
        Optional<LifecycleDefinitionModel> modelWrap = dao.findById(model1.getId());
        assertThat(modelWrap.get().getDefinitionKey()).isEqualTo("key1");
    }


    @Test
    void findAll()
    {
        LifecycleDefinitionModel model1 = new LifecycleDefinitionModel();
        model1.setDefinitionKey("key1");
        model1.setName("name1");
        model1.setPayload("""
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
                            guards:
                              - expression: "metadata['isReviewed'] == true"
                          - name: "transition3"
                            from: "STATE_2"
                            to: "STATE_4"
                        """);
        model1.setVersion(1);
        model1 = dao.saveAndFlush(model1);
        LifecycleDefinitionModel model2 = new LifecycleDefinitionModel();
        model2.setDefinitionKey("key2");
        model2.setName("name2");
        model2.setPayload("");
        model2.setVersion(2);
        model2 = dao.saveAndFlush(model2);
        List<LifecycleDefinitionModel> models = dao.findAll();
        assertThat(models.size()).isEqualTo(2);
        assertThat(models.get(0).getDefinitionKey()).isEqualTo("key1");
        assertThat(models.get(1).getDefinitionKey()).isEqualTo("key2");
    }


    @Test
    void findByKeyAndVersion()
    {
        LifecycleDefinitionModel model1 = new LifecycleDefinitionModel();
        model1.setDefinitionKey("key1");
        model1.setName("name1");
        model1.setPayload("""
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
                            guards:
                              - expression: "metadata['isReviewed'] == true"
                          - name: "transition3"
                            from: "STATE_2"
                            to: "STATE_4"
                        """);
        model1.setVersion(1);
        model1 = dao.saveAndFlush(model1);
        LifecycleDefinitionModel model2 = new LifecycleDefinitionModel();
        model2.setDefinitionKey("key2");
        model2.setName("name2");
        model2.setPayload("");
        model2.setVersion(2);
        model2 = dao.saveAndFlush(model2);
        Optional<LifecycleDefinitionModel> modelWrap = dao.findByDefinitionKeyAndVersion("key2", 2);
        assertThat(modelWrap.get().getDefinitionKey()).isEqualTo("key2");
        modelWrap = dao.findByDefinitionKeyAndVersion("key2", 1);
        assertThat(modelWrap.isEmpty()).isTrue();
    }
}
