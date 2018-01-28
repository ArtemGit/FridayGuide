package com.friday.guide.api.hibernate.type.arrays;

import org.hibernate.engine.spi.SessionImplementor;

import java.sql.Array;
import java.sql.SQLException;

public class LongArrayType extends BasicArrayType<Long[]> {

    public static final String NAME = "com.friday.guide.api.data.hibernate.type.arrays.LongArrayType";

    @Override
    protected Class<Long[]> arrayType() {
        return Long[].class;
    }

    @Override
    protected Array createSqlArray(Long[] base, SessionImplementor session) throws SQLException {
        //Long[] castObject = Arrays.stream(base).boxed().toArray(Long[]::new);
        return session.connection().createArrayOf("int8", base);
    }

    @Override
    protected Long[] fromSqlArray(Array array) throws SQLException {
        return (Long[]) array.getArray();
    }
}
