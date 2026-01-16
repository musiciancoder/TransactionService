package org.great.saga;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.great.dto.AccountsClient;
import org.great.dto.TransactionRequest;
import org.great.entity.Transaction;
import org.great.messaging.OutcomeEventsProducer;
import org.great.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

@ApplicationScoped
public class SagaService {

    @Inject
    TransactionRepository repo;
    @Inject
    AccountsClient accounts;
    @Inject
    OutcomeEventsProducer events;

    private final Logger logger = LoggerFactory.getLogger(SagaService.class);

    @Transactional
    public Transaction start(TransactionRequest req, String correlationId) {
        var t = new Transaction();
        t.sourceAccount = req.sourceAccount();
        t.targetAccount = req.targetAccount();
        t.amount = req.amount();
        t.description = req.description();
        t.status = "PENDING";
        logger.info("Starting saga for transaction from {} to {} amount {}",
                t.sourceAccount, t.targetAccount, t.amount);
        t.correlationId = correlationId;
        t.occurredAt = OffsetDateTime.now();
        repo.persist(t);
        return t;
    }

    public void execute(Transaction t) {
        try {
            logger.info("Saga started for transaction id {}", t.id);
            accounts.reserve(t.sourceAccount, t.amount, t.correlationId);
            logger.info("Reserved amount {} from account {} for transaction id {}",
                    t.amount, t.sourceAccount, t.id);
            accounts.credit(t.targetAccount, t.amount, t.correlationId);
            markCompleted(t);
        } catch (WebApplicationException ex) {
            // Compensation if reserve succeeded but credit failed
            try { accounts.release(t.sourceAccount, t.amount, t.correlationId); } catch (Exception ignore) { }
            markFailed(t, ex.getMessage());
        } catch (Exception ex) {
            try { accounts.release(t.sourceAccount, t.amount, t.correlationId); } catch (Exception ignore) { }
            markFailed(t, "UNEXPECTED_ERROR");
        }
    }

    @Transactional
    void markCompleted(Transaction t) {
        t.status = "COMPLETED";
        logger.info("Saga completed for transaction id {}", t.id);
        events.emitCompleted(t);
    }

    @Transactional
    void markFailed(Transaction t, String reason) {
        t.status = "FAILED";
        events.emitFailed(t, reason);
    }
}
