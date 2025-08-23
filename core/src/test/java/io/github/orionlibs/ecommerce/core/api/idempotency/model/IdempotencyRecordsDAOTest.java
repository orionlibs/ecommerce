package io.github.orionlibs.ecommerce.core.api.idempotency.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IdempotencyRecordsDAOTest
{
    @Autowired IdempotencyRecordsDAO dao;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
    }


    @Test
    void findById()
    {
        IdempotencyRecordModel model1 = new IdempotencyRecordModel();
        model1.setIdempotencyKey("key1");
        model1.setEndpoint("/endpoint1");
        model1.setRequestHash("hash1");
        model1.setResponseStatus(200);
        model1.setResponseBody("body1");
        model1.setResponseHeaders("headers1");
        model1.setExpiresAt(LocalDateTime.now().plusDays(1));
        model1 = dao.saveAndFlush(model1);
        IdempotencyRecordModel model2 = new IdempotencyRecordModel();
        model2.setIdempotencyKey("key2");
        model2.setEndpoint("/endpoint2");
        model2.setRequestHash("hash2");
        model2.setResponseStatus(201);
        model2.setResponseBody("body2");
        model2.setResponseHeaders("headers2");
        model2.setExpiresAt(LocalDateTime.now().plusDays(10));
        model2 = dao.saveAndFlush(model2);
        Optional<IdempotencyRecordModel> modelWrap = dao.findById(model2.getId());
        assertThat(modelWrap.get().getIdempotencyKey()).isEqualTo("key2");
        assertThat(modelWrap.get().getEndpoint()).isEqualTo("/endpoint2");
        assertThat(modelWrap.get().getRequestHash()).isEqualTo("hash2");
        assertThat(modelWrap.get().getResponseStatus()).isEqualTo(201);
        assertThat(modelWrap.get().getResponseBody()).isEqualTo("body2");
        assertThat(modelWrap.get().getResponseHeaders()).isEqualTo("headers2");
        assertThat(modelWrap.get().getExpiresAt()).isNotNull();
    }


    @Test
    void findAll()
    {
        IdempotencyRecordModel model1 = new IdempotencyRecordModel();
        model1.setIdempotencyKey("key1");
        model1.setEndpoint("/endpoint1");
        model1.setRequestHash("hash1");
        model1.setResponseStatus(200);
        model1.setResponseBody("body1");
        model1.setResponseHeaders("headers1");
        model1.setExpiresAt(LocalDateTime.now().plusDays(1));
        model1 = dao.saveAndFlush(model1);
        IdempotencyRecordModel model2 = new IdempotencyRecordModel();
        model2.setIdempotencyKey("key2");
        model2.setEndpoint("/endpoint2");
        model2.setRequestHash("hash2");
        model2.setResponseStatus(201);
        model2.setResponseBody("body2");
        model2.setResponseHeaders("headers2");
        model2.setExpiresAt(LocalDateTime.now().plusDays(10));
        model2 = dao.saveAndFlush(model2);
        List<IdempotencyRecordModel> models = dao.findAll();
        assertThat(models.size()).isEqualTo(2);
        assertThat(models.get(0).getIdempotencyKey()).isEqualTo("key1");
        assertThat(models.get(0).getEndpoint()).isEqualTo("/endpoint1");
        assertThat(models.get(0).getRequestHash()).isEqualTo("hash1");
        assertThat(models.get(0).getResponseStatus()).isEqualTo(200);
        assertThat(models.get(0).getResponseBody()).isEqualTo("body1");
        assertThat(models.get(0).getResponseHeaders()).isEqualTo("headers1");
        assertThat(models.get(0).getExpiresAt()).isNotNull();
        assertThat(models.get(1).getIdempotencyKey()).isEqualTo("key2");
        assertThat(models.get(1).getEndpoint()).isEqualTo("/endpoint2");
        assertThat(models.get(1).getRequestHash()).isEqualTo("hash2");
        assertThat(models.get(1).getResponseStatus()).isEqualTo(201);
        assertThat(models.get(1).getResponseBody()).isEqualTo("body2");
        assertThat(models.get(1).getResponseHeaders()).isEqualTo("headers2");
        assertThat(models.get(1).getExpiresAt()).isNotNull();
    }


    @Test
    void findByIdempotencyKeyAndEndpoint()
    {
        IdempotencyRecordModel model1 = new IdempotencyRecordModel();
        model1.setIdempotencyKey("key1");
        model1.setEndpoint("/endpoint1");
        model1.setRequestHash("hash1");
        model1.setResponseStatus(200);
        model1.setResponseBody("body1");
        model1.setResponseHeaders("headers1");
        model1.setExpiresAt(LocalDateTime.now().plusDays(1));
        model1 = dao.saveAndFlush(model1);
        IdempotencyRecordModel model2 = new IdempotencyRecordModel();
        model2.setIdempotencyKey("key2");
        model2.setEndpoint("/endpoint2");
        model2.setRequestHash("hash2");
        model2.setResponseStatus(201);
        model2.setResponseBody("body2");
        model2.setResponseHeaders("headers2");
        model2.setExpiresAt(LocalDateTime.now().plusDays(10));
        model2 = dao.saveAndFlush(model2);
        Optional<IdempotencyRecordModel> modelWrap = dao.findByIdempotencyKeyAndEndpoint("key2", "/endpoint2");
        assertThat(modelWrap.get().getIdempotencyKey()).isEqualTo("key2");
        assertThat(modelWrap.get().getEndpoint()).isEqualTo("/endpoint2");
        assertThat(modelWrap.get().getRequestHash()).isEqualTo("hash2");
        assertThat(modelWrap.get().getResponseStatus()).isEqualTo(201);
        assertThat(modelWrap.get().getResponseBody()).isEqualTo("body2");
        assertThat(modelWrap.get().getResponseHeaders()).isEqualTo("headers2");
        assertThat(modelWrap.get().getExpiresAt()).isNotNull();
    }


    @Test
    void deleteExpiredRecords()
    {
        IdempotencyRecordModel model1 = new IdempotencyRecordModel();
        model1.setIdempotencyKey("key1");
        model1.setEndpoint("/endpoint1");
        model1.setRequestHash("hash1");
        model1.setResponseStatus(200);
        model1.setResponseBody("body1");
        model1.setResponseHeaders("headers1");
        model1.setExpiresAt(LocalDateTime.now());
        model1 = dao.saveAndFlush(model1);
        IdempotencyRecordModel model2 = new IdempotencyRecordModel();
        model2.setIdempotencyKey("key2");
        model2.setEndpoint("/endpoint2");
        model2.setRequestHash("hash2");
        model2.setResponseStatus(201);
        model2.setResponseBody("body2");
        model2.setResponseHeaders("headers2");
        model2.setExpiresAt(LocalDateTime.now().plusDays(10));
        model2 = dao.saveAndFlush(model2);
        dao.deleteExpiredRecords(LocalDateTime.now());
        List<IdempotencyRecordModel> models = dao.findAll();
        assertThat(models.size()).isEqualTo(1);
        assertThat(models.get(0).getIdempotencyKey()).isEqualTo("key2");
        assertThat(models.get(0).getEndpoint()).isEqualTo("/endpoint2");
        assertThat(models.get(0).getRequestHash()).isEqualTo("hash2");
        assertThat(models.get(0).getResponseStatus()).isEqualTo(201);
        assertThat(models.get(0).getResponseBody()).isEqualTo("body2");
        assertThat(models.get(0).getResponseHeaders()).isEqualTo("headers2");
        assertThat(models.get(0).getExpiresAt()).isNotNull();
    }
}
