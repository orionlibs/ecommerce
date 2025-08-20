package io.github.orionlibs.ecommerce.core.api.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordsDAO;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyService
{
    @Autowired private IdempotencyRecordsDAO dao;
    @Autowired private ObjectMapper objectMapper;


    public Optional<IdempotencyRecordModel> findExistingRecord(String idempotencyKey, String endpoint)
    {
        if(idempotencyKey == null || idempotencyKey.trim().isEmpty())
        {
            return Optional.empty();
        }
        return dao.findByIdempotencyKeyAndEndpoint(idempotencyKey, endpoint);
    }


    public IdempotencyRecordModel createRecord(String idempotencyKey, String endpoint, Object requestBody)
    {
        String requestHash = calculateHash(requestBody);
        IdempotencyRecordModel record = new IdempotencyRecordModel(idempotencyKey, endpoint, requestHash);
        return dao.save(record);
    }


    public void updateRecordWithResponse(IdempotencyRecordModel record, int status, Object responseBody, HttpHeaders headers)
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


    public boolean isRequestConsistent(IdempotencyRecordModel existingRecord, Object requestBody)
    {
        String currentRequestHash = calculateHash(requestBody);
        return currentRequestHash.equals(existingRecord.getRequestHash());
    }


    private String calculateHash(Object obj)
    {
        try
        {
            String json = objectMapper.writeValueAsString(obj);
            return DigestUtils.sha256Hex(json);
        }
        catch(Exception e)
        {
            return "hash-error";
        }
    }


    @Scheduled(fixedDelay = 3600000) // Run every hour
    public void cleanupExpiredRecords()
    {
        dao.deleteExpiredRecords(LocalDateTime.now());
    }
}
