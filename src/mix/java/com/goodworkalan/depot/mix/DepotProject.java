package com.goodworkalan.strata.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Depot.
 *
 * @author Alan Gutierrez
 */
public class DepotProject implements ProjectModule {
    /**
     * Build the project definition for Depot.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.goodworkalan/depot/0.1")
                .depends()
                    .production("javax.mail/mail/1.4")
                    .production("com.goodworkalan/manifold/0.1")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
