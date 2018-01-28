package com.friday.guide.api.hibernate.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.LoggableUserType;
import org.hibernate.usertype.UserType;
import org.jboss.logging.Logger;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

@SuppressWarnings({"serial", "rawtypes"})
public class EnumCodeType implements UserType, DynamicParameterizedType, LoggableUserType, Serializable {
    private static final Logger LOG = Logger.getLogger(EnumCodeType.class.getName());

    public static final String ENUM = "enumClass";
    public static final String NAME = "com.friday.guide.api.data.hibernate.type.EnumCodeType";

    private Class<? extends Enum> enumClass;
    private Enum[] enumValues;
    private Method codeMethod;
    private int sqlType = Types.INTEGER;

    @Override
    public int[] sqlTypes() {
        return new int[]{sqlType};
    }

    @Override
    public Class<? extends Enum> returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        return getValue(rs, names);
    }


    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        setValue(st, (Enum) value, index);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);

        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
        } else {
            String enumClassName = (String) parameters.get(ENUM);
            try {
                enumClass = ReflectHelper.classForName(enumClassName, this.getClass()).asSubclass(Enum.class);
            } catch (ClassNotFoundException exception) {
                throw new HibernateException("Enum class not found", exception);
            }
        }
        try {
            codeMethod = enumClass.getMethod("getCode");
        } catch (NoSuchMethodException | SecurityException e) {
            throw new HibernateException("Method 'getCode()' for class " + enumClass + " not avaliable.", e);
        }
    }

    @Override
    public String toLoggableString(Object value, SessionFactoryImplementor factory) {
        return value.toString();
    }

    public Enum getValue(ResultSet rs, String[] names) throws SQLException {
        final int code = rs.getInt(names[0]);
        final boolean traceEnabled = LOG.isTraceEnabled();
        if (rs.wasNull()) {
            if (traceEnabled) {
                LOG.trace(String.format("Returning null as column [%s]", names[0]));
            }
            return null;
        }

        final Enum enumValue = fromCode(code);
        if (traceEnabled) {
            LOG.trace(String.format("Returning [%s] as column [%s]", enumValue, names[0]));
        }
        return enumValue;
    }

    private Enum fromCode(int code) {
        if (code <= 0) return null;
        final Enum[] enumValues = enumValues();
        for (Enum value : enumValues) {
            int eCode = getCode(value);
            if (eCode == code) return value;
        }
        throw new IllegalArgumentException(
            String.format(
                "Unknown ordinal value [%s] for enum class [%s]",
                code,
                enumClass.getName()
            )
        );
    }

    private Enum[] enumValues() {
        if (enumValues == null) {
            enumValues = enumClass.getEnumConstants();
            if (enumValues == null) {
                throw new HibernateException("Failed to init enum values");
            }
        }
        return enumValues;
    }

    public void setValue(PreparedStatement st, Enum value, int index) throws SQLException {
        final int jdbcValue = value == null ? 0 : getCode(value);

        final boolean traceEnabled = LOG.isTraceEnabled();
        if (jdbcValue == 0) {
            if (traceEnabled) {
                LOG.trace(String.format("Binding null to parameter: [%s]", index));
            }
            st.setNull(index, sqlType);
            return;
        }

        if (traceEnabled) {
            LOG.trace(String.format("Binding [%s] to parameter: [%s]", jdbcValue, index));
        }
        st.setInt(index, jdbcValue);
    }

    private int getCode(Enum value) {
        try {
            return (int) codeMethod.invoke(value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new HibernateException(e);
        }
    }
}
