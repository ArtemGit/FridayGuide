package com.friday.guide.api.hibernate.type;

import com.friday.guide.api.utils.CryptUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CryptStringType implements UserType {

    public static final String NAME = "com.friday.guide.api.data.hibernate.type.CryptStringType";
    public static final Charset ENCODING = StandardCharsets.UTF_8;
    private static byte[] KEY;

    public static void setCryptKey(byte[] key) {
        KEY = key;
    }

    public int[] sqlTypes() {
        return new int[]{StringType.INSTANCE.sqlType()};
    }

    public Class<String> returnedClass() {
        return String.class;
    }

    public boolean equals(Object x, Object y) {
        return x == y || x != null && x.equals(y);
    }

    public int hashCode(Object o) {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object owner) throws HibernateException, SQLException {
        String result = resultSet.getString(names[0]);
        try {
            return result == null || result.trim().length() == 0 ? null : CryptUtils.decrypt(result, KEY, ENCODING);
        } catch (GeneralSecurityException | IOException e) {
            throw new UnknownUnwrapTypeException(returnedClass(), e);
        }
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        String val;
        try {
            val = CryptUtils.encrypt(value == null ? "" : value.toString(), KEY, ENCODING);
        } catch (GeneralSecurityException | IOException e) {
            throw new UnknownUnwrapTypeException(returnedClass(), e);
        }
        statement.setString(index, val == null ? " " : val);
    }

    public Object deepCopy(Object value) {
        return value == null ? null : value.toString();
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble(Object o) {
        return (Serializable) o;
    }

    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) {
        return deepCopy(original);
    }
}
