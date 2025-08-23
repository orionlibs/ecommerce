package io.github.orionlibs.ecommerce.core.api.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class IdempotencyAspect
{
    @Autowired private IdempotencyService idempotencyService;
    @Autowired private ObjectMapper objectMapper;


    public IdempotencyAspect()
    {
    }


    public IdempotencyAspect(IdempotencyService idempotencyService, ObjectMapper objectMapper)
    {
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;
    }


    @Around("@annotation(io.github.orionlibs.ecommerce.core.api.idempotency.Idempotent)")
    public Object aroundIdempotent(ProceedingJoinPoint pjp) throws Throwable
    {
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(attrs == null)
        {
            return pjp.proceed();
        }
        HttpServletRequest request = attrs.getRequest();
        String idempotencyKey = request.getHeader("Idempotency-Key");
        String endpoint = request.getMethod() + " " + request.getRequestURI();
        if(idempotencyKey == null || idempotencyKey.trim().isEmpty())
        {
            return pjp.proceed();
        }
        Optional<IdempotencyRecordModel> existing = idempotencyService.getRecord(idempotencyKey, endpoint);
        if(existing.isPresent())
        {
            IdempotencyRecordModel record = existing.get();
            if(!idempotencyService.isRequestConsistentWithRecord(record, request))
            {
                throw new IdempotencyConflictException("Idempotency key reused with different request body");
            }
            return buildStoredResponse(record);
        }
        IdempotencyRecordModel record = idempotencyService.save(idempotencyKey, endpoint, request);
        try
        {
            Object result = pjp.proceed();
            if(result instanceof ResponseEntity<?> respEntity)
            {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Idempotency-Key", idempotencyKey);
                idempotencyService.update(
                                record,
                                respEntity.getStatusCodeValue(),
                                objectMapper.writeValueAsString(respEntity.getBody()),
                                responseHeaders
                );
                return respEntity;
            }
            else
            {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Idempotency-Key", idempotencyKey);
                idempotencyService.update(
                                record,
                                200,
                                objectMapper.writeValueAsString(result),
                                responseHeaders
                );
                return result;
            }
        }
        catch(Exception e)
        {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Idempotency-Key", idempotencyKey);
            idempotencyService.update(
                            record,
                            422,
                            objectMapper.writeValueAsString(Map.of("error", e.getMessage())),
                            responseHeaders
            );
            throw e;
        }
    }


    private ResponseEntity<Map<?, ?>> buildStoredResponse(IdempotencyRecordModel record)
    {
        try
        {
            Map responseBody = objectMapper.readValue(record.getResponseBody(), Map.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Idempotency-Key", record.getIdempotencyKey());
            headers.add("X-Idempotent-Replay", "true");
            return ResponseEntity.status(record.getResponseStatus())
                            .headers(headers)
                            .body(responseBody);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed to reconstruct idempotent response", e);
        }
    }
}
