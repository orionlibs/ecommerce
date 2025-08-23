package io.github.orionlibs.ecommerce.core.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JSONServiceTest
{
    ObjectMapper mapper;


    @BeforeEach
    public void setup()
    {
        mapper = new JSONObjectMapper().getMapper();
    }


    @Test
    void convertObjectToJSON() throws JsonProcessingException
    {
        // given
        Pojo bean = new Pojo(64, "some message", List.of("one", "two"));
        // when
        String beanAsJSON = JSONService.convertObjectToJSON(bean);
        // then
        assertThat(beanAsJSON).isNotNull();
        String formattedJSON = """
                        {
                            "number": 64,
                            "message": "some message",
                            "some_fields": [
                                "one",
                                "two"
                            ]
                        }
                        """;
        JsonNode tree1 = mapper.readTree(formattedJSON);
        JsonNode tree2 = mapper.readTree(beanAsJSON);
        assertThat(tree2).isEqualTo(tree1);
    }


    private record Pojo(
                    int number,
                    String message,
                    @JsonProperty("some_fields") List<String> someFields
    )
    {
    }
}
