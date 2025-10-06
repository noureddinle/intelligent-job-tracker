package com.jobtracker.config;

import org.hibernate.dialect.PostgreSQLDialect;

public class PgVectorDialect extends PostgreSQLDialect {

    public PgVectorDialect() {
        super();
    }
}