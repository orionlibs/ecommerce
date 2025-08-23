package io.github.orionlibs.ecommerce.core.api.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordsDAO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdempotencyService
{
    @Autowired private IdempotencyRecordsDAO dao;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private APIRequestBodyHashCalculator apiRequestBodyHashCalculator;


    @Transactional(readOnly = true)
    public Optional<IdempotencyRecordModel> getRecord(String idempotencyKey, String endpoint)
    {
        return dao.findByIdempotencyKeyAndEndpoint(idempotencyKey, endpoint);
    }


    public boolean isRequestConsistentWithRecord(IdempotencyRecordModel existingRecord, Object requestBody)
    {
        try
        {
            String currentRequestHash = apiRequestBodyHashCalculator.calculateHash(requestBody);
            return currentRequestHash.equals(existingRecord.getRequestHash());
        }
        catch(JsonProcessingException e)
        {
            return false;
        }
    }


    @Transactional
    public IdempotencyRecordModel save(String idempotencyKey, String endpoint, Object requestBody)
    {
        try
        {
            String requestHash = apiRequestBodyHashCalculator.calculateHash(requestBody);
            IdempotencyRecordModel record = new IdempotencyRecordModel(idempotencyKey, endpoint, requestHash);
            return dao.save(record);
        }
        catch(JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public IdempotencyRecordModel save(IdempotencyRecordModel model)
    {
        return dao.saveAndFlush(model);
    }


    @Transactional
    public void update(IdempotencyRecordModel record, int status, Object responseBody, HttpHeaders headers)
    {
        record.setResponseStatus(status);
        try
        {
            record.setResponseBody(objectMapper.writeValueAsString(responseBody));
            record.setResponseHeaders(objectMapper.writeValueAsString(headers.toSingleValueMap()));
        }
        catch(Exception e)
        {
            record.setResponseBody("Error serializing response");
            record.setResponseHeaders("{}");
        }
        dao.save(record);
    }


    @Transactional
    public void deleteAll()
    {
        dao.deleteAll();
    }
}
