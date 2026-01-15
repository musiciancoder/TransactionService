package org.great.dto;

import jakarta.enterprise.context.ApplicationScoped;
import org.great.rest.AccountServiceRest;

import java.math.BigDecimal;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AccountsClient {

    @RestClient
    AccountServiceRest rest;

    public void reserve(String number, BigDecimal amount, String correlationId) {
        rest.reserve(new ReserveRequest(number, amount), correlationId);
    }
    public void credit(String number, BigDecimal amount, String correlationId) {
        rest.credit(new CreditRequest(number, amount), correlationId);
    }
    public void release(String number, BigDecimal amount, String correlationId) {
        rest.release(new ReleaseRequest(number, amount), correlationId);
    }
}
