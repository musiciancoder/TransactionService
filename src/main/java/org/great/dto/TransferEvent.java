package org.great.dto;

import java.math.BigDecimal;

public record TransferEvent(
        String transactionId,
        String type, // COMPLETED | FAILED
        String sourceAccount,
        String targetAccount,
        BigDecimal amount,
        String correlationId,
        String reason // optional failure reason
 ) { }