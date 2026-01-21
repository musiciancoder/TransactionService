package org.great.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Transaction extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    public String sourceAccount;
    public String targetAccount;
    public BigDecimal amount;
    public String status; // PENDING | COMPLETED | FAILED
    public String description;
    public String correlationId;
    public OffsetDateTime occurredAt;
}
