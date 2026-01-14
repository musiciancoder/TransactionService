package org.great.dto;

import java.math.BigDecimal;

public record ReleaseRequest(String number, BigDecimal amount)  {
}
