package com.mybank.banking;

import com.google.inject.AbstractModule;
import com.mybank.banking.service.BankingService;
import com.mybank.banking.service.impl.BankingServiceImpl;

/**
 * @author sakhter
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BankingService.class).to(BankingServiceImpl.class);
    }
}
