package org.great.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.great.dto.TransferEvent;
import org.great.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class OutcomeEventsProducer {

    private final Logger logger = LoggerFactory.getLogger(OutcomeEventsProducer.class);

    @Channel("events-transfers-completed")
    Emitter<TransferEvent> completed;
    @Channel("events-transfers-failed")
    Emitter<TransferEvent> failed;

    public void emitCompleted(Transaction t) {
        logger.info("Emitting COMPLETED event for transaction id {}", t.id);
        completed.send(new TransferEvent(
                t.id.toString(), "COMPLETED", t.sourceAccount, t.targetAccount, t.amount, t.correlationId, null
        ));
        logger.info("Emitted COMPLETED event for transaction id {}", t.id);
    }

    public void emitFailed(Transaction t, String reason) {
        logger.info("Emitting FAILED event for transaction id {} with reason {}", t.id, reason);
        failed.send(new TransferEvent(
                t.id.toString(), "FAILED", t.sourceAccount, t.targetAccount, t.amount, t.correlationId, reason
        ));
        logger.info("Emitted FAILED event for transaction id {}", t.id);
    }
}
