package com.friday.guide.api.hibernate.type;

import com.friday.guide.api.utils.CryptUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.type.WrapperBinaryType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CryptByteArrayType implements UserType {

    public static final String NAME = "com.friday.guide.api.data.hibernate.type.CryptByteArrayType";
    private static byte[] KEY;

    public static void setCryptKey(byte[] key) {
        KEY = key;
    }

    public int[] sqlTypes() {
        return new int[]{WrapperBinaryType.INSTANCE.sqlType()};
    }

    public Class<?> returnedClass() {
        return byte[].class;
    }

    public boolean equals(Object x, Object y) {
        return Arrays.equals((byte[]) x, (byte[]) y);
    }

    public int hashCode(Object o) {
        if (o == null) {
            return 0;
        }
        return Arrays.hashCode((byte[]) o);
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object owner) throws HibernateException, SQLException {
        byte[] result = resultSet.getBytes(names[0]);
        try {
            return result != null ? CryptUtils.decrypt(result, KEY) : null;
        } catch (GeneralSecurityException e) {
            throw new UnknownUnwrapTypeException(returnedClass(), e);
        }
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        byte[] val;
        try {
            val = CryptUtils.encrypt(value == null ? new byte[]{} : (byte[]) value, KEY);
        } catch (GeneralSecurityException e) {
            throw new UnknownUnwrapTypeException(returnedClass(), e);
        }
        statement.setBytes(index, val == null ? new byte[]{} : val);
    }

    public Object deepCopy(Object value) {
        byte[] arr = (byte[]) value;
        return Arrays.copyOf(arr, arr.length);
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
