package io.github.orionlibs.ecommerce.core.api.idempotency;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IdempotencyServiceTest
{
    @Autowired IdempotencyService idempotencyService;
    @Autowired APIRequestBodyHashCalculator apiRequestBodyHashCalculator;
    IdempotencyRecordModel idempotencyRecord;


    @BeforeEach
    void setup() throws JsonProcessingException
    {
        idempotencyService.deleteAll();
        idempotencyRecord = new IdempotencyRecordModel();
        idempotencyRecord.setIdempotencyKey("key1");
        idempotencyRecord.setEndpoint("/endpoint1");
        idempotencyRecord.setRequestHash(apiRequestBodyHashCalculator.calculateHash("body1"));
        idempotencyRecord.setResponseStatus(200);
        idempotencyRecord.setResponseBody("body1");
        idempotencyRecord.setResponseHeaders("headers1");
        idempotencyRecord.setExpiresAt(LocalDateTime.now().plusDays(1));
        idempotencyRecord = idempotencyService.save(idempotencyRecord);
    }


    @Test
    void getRecord()
    {
        Optional<IdempotencyRecordModel> r1 = idempotencyService.getRecord(null, "/endpoint1");
        assertThat(r1.isEmpty()).isTrue();
        Optional<IdempotencyRecordModel> r2 = idempotencyService.getRecord(" ", "/endpoint1");
        assertThat(r2.isEmpty()).isTrue();
        Optional<IdempotencyRecordModel> r3 = idempotencyService.getRecord("key1", null);
        assertThat(r3.isEmpty()).isTrue();
        Optional<IdempotencyRecordModel> r4 = idempotencyService.getRecord("key1", " ");
        assertThat(r4.isEmpty()).isTrue();
        Optional<IdempotencyRecordModel> modelWrap = idempotencyService.getRecord("key1", "/endpoint1");
        assertThat(modelWrap.get().getIdempotencyKey()).isEqualTo("key1");
        assertThat(modelWrap.get().getEndpoint()).isEqualTo("/endpoint1");
        assertThat(modelWrap.get().getRequestHash()).isEqualTo(idempotencyRecord.getRequestHash());
        assertThat(modelWrap.get().getResponseStatus()).isEqualTo(200);
        assertThat(modelWrap.get().getResponseBody()).isEqualTo("body1");
        assertThat(modelWrap.get().getResponseHeaders()).isEqualTo("headers1");
        assertThat(modelWrap.get().getExpiresAt()).isNotNull();
    }


    @Test
    void isRequestConsistentWithRecord()
    {
        assertThat(idempotencyService.isRequestConsistentWithRecord(idempotencyRecord, "body1")).isTrue();
    }
}
