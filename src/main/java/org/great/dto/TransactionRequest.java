package org.great.dto;

import java.math.BigDecimal;

public record TransactionRequest( String sourceAccount, String targetAccount, BigDecimal amount, String description ) {}