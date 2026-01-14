package org.great.dto;

import java.math.BigDecimal;

public record ReserveRequest(String number, BigDecimal amount) { }