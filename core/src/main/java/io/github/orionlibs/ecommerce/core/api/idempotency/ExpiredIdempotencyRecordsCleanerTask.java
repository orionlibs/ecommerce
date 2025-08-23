package io.github.orionlibs.ecommerce.core.api.idempotency;

import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordsDAO;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExpiredIdempotencyRecordsCleanerTask
{
    @Autowired private IdempotencyRecordsDAO dao;


    @Scheduled(fixedDelay = 3600000) //runs every hour
    @Transactional
    public void cleanupExpiredRecords()
    {
        dao.deleteExpiredRecords(LocalDateTime.now());
    }
}
