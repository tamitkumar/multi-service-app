package com.user.module.config;

import org.hibernate.dialect.MySQLDialect;

public class DialectConfig extends MySQLDialect {
    @Override
    public boolean dropConstraints() {
        return false;
    }
}
