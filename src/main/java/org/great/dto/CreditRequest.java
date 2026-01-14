package org.great.dto;

import java.math.BigDecimal;

public record CreditRequest (String number, BigDecimal amount) {
}
