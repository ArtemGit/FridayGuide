package com.friday.guide.api.hibernate.type.arrays;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.LoggableUserType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;


public abstract class BasicArrayType<T> implements UserType, LoggableUserType, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(BasicArrayType.class);

    private final int sqlType = Types.ARRAY;

    protected abstract Class<T> arrayType();

    protected abstract Array createSqlArray(T base, SessionImplementor session) throws SQLException;

    protected abstract T fromSqlArray(Array array) throws SQLException;

    @Override
    public int[] sqlTypes() {
        return new int[]{sqlType};
    }

    @Override
    public Class<?> returnedClass() {
        return arrayType();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Arrays.equals((Object[]) x, (Object[]) y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Arrays.hashCode((Object[]) x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        return getValue(rs, names);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        setValue(st, (T) value, index, session);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if(value==null) return null;
        Object[] v = (Object[]) value;
        return Arrays.copyOf(v, v.length);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public T getValue(ResultSet rs, String[] names) throws SQLException {
        Array array = rs.getArray(names[0]);
        final boolean traceEnabled = LOG.isTraceEnabled();
        if (rs.wasNull()) {
            if (traceEnabled) {
                LOG.trace(String.format("Returning null as column [%s]", names[0]));
            }
            return null;
        }

        T arr = this.fromSqlArray(array);
        if (traceEnabled) {
            LOG.trace(String.format("Returning [%s] as column [%s]", Arrays.toString((Object[]) arr), names[0]));
        }
        return arr;
    }

    public void setValue(PreparedStatement st, T value, int index, SessionImplementor session) throws SQLException {
        final boolean traceEnabled = LOG.isTraceEnabled();
        if (value == null) {
            if (traceEnabled) {
                LOG.trace(String.format("Binding null to parameter: [%s]", index));
            }
            st.setNull(index, sqlType);
            return;
        }

        if (traceEnabled) {
            LOG.trace(String.format("Binding [%s] to parameter: [%s]", Arrays.toString((Object[]) value), index));
        }

        Array array = this.createSqlArray(value, session);
        st.setArray(index, array);
    }

    @Override
    public String toLoggableString(Object value, SessionFactoryImplementor factory) {
        return Arrays.toString((Object[]) value);
    }

}
