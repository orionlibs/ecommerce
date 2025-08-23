package io.github.orionlibs.ecommerce.core.api.idempotency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ActiveProfiles("test")
public class IdempotencyAspectTest
{
    private IdempotencyService idempotencyService;
    private ObjectMapper objectMapper;
    private IdempotencyAspect aspect;


    @BeforeEach
    void setUp()
    {
        idempotencyService = mock(IdempotencyService.class);
        objectMapper = new ObjectMapper();
        aspect = new IdempotencyAspect(idempotencyService, objectMapper);
    }


    @AfterEach
    void tearDown()
    {
        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    void whenNoRequestAttributes_thenProceedUnchanged() throws Throwable
    {
        RequestContextHolder.resetRequestAttributes();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        Object returned = Map.of("ok", true);
        when(pjp.proceed()).thenReturn(returned);
        Object result = aspect.aroundIdempotent(pjp);
        assertSame(returned, result);
        verifyNoInteractions(idempotencyService);
        verify(pjp, times(1)).proceed();
    }


    @Test
    void whenNoIdempotencyHeader_thenProceedAndNoIdempotencyHandling() throws Throwable
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Idempotency-Key")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/test");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        Object returned = Map.of("ok", true);
        when(pjp.proceed()).thenReturn(returned);
        Object result = aspect.aroundIdempotent(pjp);
        assertSame(returned, result);
        verifyNoInteractions(idempotencyService);
        verify(pjp, times(1)).proceed();
    }


    @Test
    void whenExistingRecordPresent_andRequestConsistent_thenReturnStoredResponse() throws Throwable
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Idempotency-Key")).thenReturn("key-123");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        IdempotencyRecordModel record = mock(IdempotencyRecordModel.class);
        when(record.getResponseBody()).thenReturn("{\"result\":\"ok\"}");
        when(record.getResponseStatus()).thenReturn(200);
        when(record.getIdempotencyKey()).thenReturn("key-123");
        when(idempotencyService.getRecord("key-123", "GET /api/test")).thenReturn(Optional.of(record));
        when(idempotencyService.isRequestConsistentWithRecord(record, request)).thenReturn(true);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        Object result = aspect.aroundIdempotent(pjp);
        //the aspect should return a ResponseEntity reconstructed from the stored payload
        assertTrue(result instanceof ResponseEntity);
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> resp = (ResponseEntity<Map<String, Object>>)result;
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("ok", resp.getBody().get("result"));
        assertEquals("key-123", resp.getHeaders().getFirst("Idempotency-Key"));
        assertEquals("true", resp.getHeaders().getFirst("X-Idempotent-Replay"));
        //the controller must not be invoked
        verify(pjp, never()).proceed();
        verify(idempotencyService, times(1)).getRecord("key-123", "GET /api/test");
        verify(idempotencyService, times(1)).isRequestConsistentWithRecord(record, request);
    }


    @Test
    void whenExistingRecordPresent_andRequestInconsistent_thenThrowConflict() throws Throwable
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Idempotency-Key")).thenReturn("key-456");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        IdempotencyRecordModel record = mock(IdempotencyRecordModel.class);
        when(idempotencyService.getRecord("key-456", "GET /api/test"))
                        .thenReturn(Optional.of(record));
        when(idempotencyService.isRequestConsistentWithRecord(record, request)).thenReturn(false);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        assertThrows(IdempotencyConflictException.class, () -> aspect.aroundIdempotent(pjp));
        verify(pjp, never()).proceed();
        verify(idempotencyService, times(1)).getRecord("key-456", "GET /api/test");
        verify(idempotencyService, times(1)).isRequestConsistentWithRecord(record, request);
    }


    @Test
    void whenNoExistingRecord_thenCreateRecordProceedAndPersistResponse() throws Throwable
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Idempotency-Key")).thenReturn("key-create");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/do");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(idempotencyService.getRecord("key-create", "GET /api/do"))
                        .thenReturn(Optional.empty());
        IdempotencyRecordModel createdRecord = mock(IdempotencyRecordModel.class);
        when(idempotencyService.save(eq("key-create"), eq("GET /api/do"), eq(request)))
                        .thenReturn(createdRecord);
        // controller returns a ResponseEntity
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        ResponseEntity<Map<String, Object>> controllerResponse =
                        ResponseEntity.status(201).header("X-Extra", "v").body(Map.of("ok", true));
        when(pjp.proceed()).thenReturn(controllerResponse);
        Object result = aspect.aroundIdempotent(pjp);
        assertSame(controllerResponse, result);
        // verify that updateRecordWithResponse is called with serialized body and headers
        verify(idempotencyService, times(1)).update(
                        eq(createdRecord),
                        eq(201),
                        anyString(),
                        any(HttpHeaders.class)
        );
        verify(pjp, times(1)).proceed();
    }


    @Test
    void whenControllerThrows_thenPersistFailureAndRethrow() throws Throwable
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Idempotency-Key")).thenReturn("key-fail");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/fail");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(idempotencyService.getRecord("key-fail", "GET /api/fail"))
                        .thenReturn(Optional.empty());
        IdempotencyRecordModel createdRecord = mock(IdempotencyRecordModel.class);
        when(idempotencyService.save(eq("key-fail"), eq("GET /api/fail"), eq(request)))
                        .thenReturn(createdRecord);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        RuntimeException failure = new RuntimeException("boom");
        when(pjp.proceed()).thenThrow(failure);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> aspect.aroundIdempotent(pjp));
        assertSame(failure, thrown);
        // verify failure update persisted with error message
        verify(idempotencyService, times(1)).update(
                        eq(createdRecord),
                        eq(422),
                        contains("boom"),
                        any(HttpHeaders.class)
        );
        verify(pjp, times(1)).proceed();
    }
}
