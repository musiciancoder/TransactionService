package org.great.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.great.dto.TransferEvent;
import org.great.entity.Transaction;

@ApplicationScoped
public class OutcomeEventsProducer {}
/*    @Channel("events-transfers-completed") Emitter<TransferEvent> completed;
    @Channel("events-transfers-failed")  Emitter<TransferEvent> failed;

    public void emitCompleted(Transaction t) {
        completed.send(new TransferEvent(
                t.id.toString(), "COMPLETED", t.sourceAccount, t.targetAccount, t.amount, t.correlationId, null
        ));
    }
    public void emitFailed(Transaction t, String reason) {
        failed.send(new TransferEvent(
                t.id.toString(), "FAILED", t.sourceAccount, t.targetAccount, t.amount, t.correlationId, reason
        ));
    }
}*/
