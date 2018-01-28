package com.friday.guide.api.hibernate.type.arrays;

import org.hibernate.engine.spi.SessionImplementor;

import java.sql.Array;
import java.sql.SQLException;


public class StringArrayType extends BasicArrayType<String[]> {

    public static final String NAME = "com.friday.guide.api.data.hibernate.type.arrays.StringArrayType";

    @Override
    protected Class<String[]> arrayType() {
        return String[].class;
    }

    @Override
    protected Array createSqlArray(String[] base, SessionImplementor session) throws SQLException {
        return session.connection().createArrayOf("text", base);
    }

    @Override
    protected String[] fromSqlArray(Array array) throws SQLException {
        return (String[]) array.getArray();
    }
}
