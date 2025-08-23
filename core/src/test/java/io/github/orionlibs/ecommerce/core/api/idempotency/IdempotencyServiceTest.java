package io.github.orionlibs.ecommerce.core.api.idempotency;

import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IdempotencyServiceTest
{
    @Autowired IdempotencyService idempotencyService;
    //@Autowired IdempotencyRecordsDAO dao;
    //@Autowired ObjectMapper objectMapper;
    IdempotencyRecordModel idempotencyRecordModel;


    @BeforeEach
    void setup()
    {
        idempotencyService.deleteAll();
        idempotencyRecordModel = new IdempotencyRecordModel();
        idempotencyRecordModel.setIdempotencyKey("key1");
        idempotencyRecordModel.setEndpoint("/endpoint1");
        idempotencyRecordModel.setRequestHash("hash1");
        idempotencyRecordModel.setResponseStatus(200);
        idempotencyRecordModel.setResponseBody("body1");
        idempotencyRecordModel.setResponseHeaders("headers1");
        idempotencyRecordModel.setExpiresAt(LocalDateTime.now().plusDays(1));
        idempotencyRecordModel = idempotencyService.save(idempotencyRecordModel);
    }


    /*@BeforeEach
    void setUp() {
        service = new IdempotencyService();
        dao = mock(IdempotencyRecordsDAO.class);
        objectMapper = mock(ObjectMapper.class);

        // Inject mocks into private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(service, "dao", dao);
        ReflectionTestUtils.setField(service, "objectMapper", objectMapper);
    }*/
    /*@Test
    void findExistingRecord_whenKeyIsNullOrEmpty_returnsEmptyAndDoesNotCallDao()
    {
        Optional<IdempotencyRecordModel> r1 = service.findExistingRecord(null, "/ep");
        assertThat(r1.isEmpty()).isTrue();
        Optional<IdempotencyRecordModel> r2 = service.findExistingRecord("  ", "/ep");
        assertThat(r2.isEmpty()).isTrue();
    }


    @Test
    void findExistingRecord_whenKeyProvided_callsDaoAndReturns()
    {
        //IdempotencyRecordModel model = mock(IdempotencyRecordModel.class);
        //when(dao.findByIdempotencyKeyAndEndpoint("k", "/ep")).thenReturn(Optional.of(model));
        Optional<IdempotencyRecordModel> out = service.findExistingRecord("k", "/ep");
        assertThat(out.isPresent()).isTrue();
        //assertSame(model, out.get());
        //verify(dao, times(1)).findByIdempotencyKeyAndEndpoint("k", "/ep");
    }


    @Test
    void createRecord_calculatesHashAndSavesRecord() throws Exception
    {
        Object requestBody = Map.of("a", 1);
        String json = "{\"a\":1}";
        when(objectMapper.writeValueAsString(requestBody)).thenReturn(json);
        // dao.save should return its argument
        when(dao.save(any(IdempotencyRecordModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        IdempotencyRecordModel saved = service.createRecord("key-1", "/ep/1", requestBody);
        ArgumentCaptor<IdempotencyRecordModel> captor = ArgumentCaptor.forClass(IdempotencyRecordModel.class);
        verify(dao, times(1)).save(captor.capture());
        IdempotencyRecordModel captured = captor.getValue();
        String expectedHash = DigestUtils.sha256Hex(json);
        assertEquals(expectedHash, captured.getRequestHash());
        assertEquals("key-1", captured.getIdempotencyKey());
        assertEquals("/ep/1", captured.getEndpoint());
        // service should return the DAO's save result
        assertSame(captured, saved);
    }


    @Test
    void createRecord_whenObjectMapperThrowsHashError_returnsRecordWithHashErrorAndSaves() throws Exception
    {
        Object requestBody = Map.of("b", 2);
        when(objectMapper.writeValueAsString(requestBody)).thenThrow(new RuntimeException("fail-json"));
        when(dao.save(any(IdempotencyRecordModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        IdempotencyRecordModel saved = service.createRecord("key-2", "/ep/2", requestBody);
        ArgumentCaptor<IdempotencyRecordModel> captor = ArgumentCaptor.forClass(IdempotencyRecordModel.class);
        verify(dao).save(captor.capture());
        IdempotencyRecordModel captured = captor.getValue();
        assertEquals("hash-error", captured.getRequestHash());
        assertSame(captured, saved);
    }


    @Test
    void updateRecordWithResponse_serializesBodyAndHeadersAndSaves() throws Exception
    {
        IdempotencyRecordModel record = mock(IdempotencyRecordModel.class);
        // objectMapper will be called twice: once for body, once for headers map
        when(objectMapper.writeValueAsString(Map.of("ok", true))).thenReturn("{\"ok\":true}");
        when(objectMapper.writeValueAsString(Map.of("X", "v"))).thenReturn("{\"X\":\"v\"}");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X", "v");
        service.updateRecordWithResponse(record, 201, Map.of("ok", true), headers);
        verify(record).setResponseStatus(201);
        verify(record).setResponseBody("{\"ok\":true}");
        verify(record).setResponseHeaders("{\"X\":\"v\"}");
        verify(dao).save(record);
    }


    @Test
    void updateRecordWithResponse_whenSerializationFails_setsFallbackValuesAndSaves() throws Exception
    {
        IdempotencyRecordModel record = mock(IdempotencyRecordModel.class);
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("boom"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("H", "v");
        service.updateRecordWithResponse(record, 500, Map.of("x", 1), headers);
        verify(record).setResponseStatus(500);
        verify(record).setResponseBody("Error serializing response");
        verify(record).setResponseHeaders("{}");
        verify(dao).save(record);
    }


    @Test
    void isRequestConsistent_trueAndFalseCases() throws Exception
    {
        Object requestBody = Map.of("z", 9);
        String json = "{\"z\":9}";
        when(objectMapper.writeValueAsString(requestBody)).thenReturn(json);
        String expectedHash = DigestUtils.sha256Hex(json);
        IdempotencyRecordModel existing = mock(IdempotencyRecordModel.class);
        when(existing.getRequestHash()).thenReturn(expectedHash);
        assertTrue(service.isRequestConsistent(existing, requestBody));
        // now simulate mismatch
        when(existing.getRequestHash()).thenReturn("some-other-hash");
        assertFalse(service.isRequestConsistent(existing, requestBody));
    }


    @Test
    void cleanupExpiredRecords_invokesDaoWithNow()
    {
        // We just capture the LocalDateTime value passed and assert it's close to now
        doNothing().when(dao).deleteExpiredRecords(any(LocalDateTime.class));
        service.cleanupExpiredRecords();
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(dao).deleteExpiredRecords(captor.capture());
        LocalDateTime passed = captor.getValue();
        LocalDateTime now = LocalDateTime.now();
        // assert passed is within 5 seconds of now (small tolerance)
        long diffSeconds = Duration.between(passed, now).abs().getSeconds();
        assertTrue(diffSeconds < 5, "cleanup timestamp should be recent, diffSeconds=" + diffSeconds);
    }*/
}
