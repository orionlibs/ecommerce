package io.github.orionlibs.ecommerce.core.api.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class APIRequestBodyHashCalculator
{
    @Autowired private ObjectMapper objectMapper;


    public String calculateHash(Object obj) throws JsonProcessingException
    {
        String json = objectMapper.writeValueAsString(obj);
        return DigestUtils.sha256Hex(json);
    }
}
