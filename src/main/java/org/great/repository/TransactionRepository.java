package org.great.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.great.entity.Transaction;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {
    public List<Transaction> listByAccount(String number) {
        return list("sourceAccount = ?1 or targetAccount = ?1", number);
    }
}
