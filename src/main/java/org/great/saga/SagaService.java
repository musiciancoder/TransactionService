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

import java.time.OffsetDateTime;

@ApplicationScoped
public class SagaService {

    @Inject
    TransactionRepository repo;
    @Inject
    AccountsClient accounts;
    @Inject
    OutcomeEventsProducer events;

    @Transactional
    public Transaction start(TransactionRequest req, String correlationId) {
        var t = new Transaction();
        t.sourceAccount = req.sourceAccount();
        t.targetAccount = req.targetAccount();
        t.amount = req.amount();
        t.description = req.description();
        t.status = "PENDING";
        t.correlationId = correlationId;
        t.occurredAt = OffsetDateTime.now();
        repo.persist(t);
        return t;
    }

    public void execute(Transaction t) {
        try {
            accounts.reserve(t.sourceAccount, t.amount, t.correlationId);
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
     //   events.emitCompleted(t);
    }

    @Transactional
    void markFailed(Transaction t, String reason) {
        t.status = "FAILED";
     //   events.emitFailed(t, reason);
    }
}
