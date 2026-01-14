package org.great.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Transaction extends PanacheEntity {
    public String sourceAccount;
    public String targetAccount;
    public BigDecimal amount;
    public String status; // PENDING | COMPLETED | FAILED
    public String description;
    public String correlationId;
    public OffsetDateTime occurredAt;
}
