package com.mybank.banking;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import ru.vyarus.dropwizard.guice.GuiceBundle;


/**
 * @author sakhter
 */
public class MyBankingApp extends Application<MyBankConfiguration> {

    @Override
    public void initialize(Bootstrap<MyBankConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new ServiceModule())
                .build());

        bootstrap.addBundle(new SwaggerBundle<MyBankConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MyBankConfiguration myBankConfiguration) {
                return myBankConfiguration.swaggerBundleConfiguration;
            }
        }) ;
    }

    @Override
    public void run(MyBankConfiguration c, Environment e) throws Exception {
    }

    public static void main(String[] args) throws Exception {
        new MyBankingApp().run(new String[] {"server", "config.yml"});
    }
}
