package com.friday.guide.api.hibernate.descriptor;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class LocalDateTimeDescriptor extends AbstractTypeDescriptor<LocalDateTime>{
	public static final LocalDateTimeDescriptor INSTANCE = new LocalDateTimeDescriptor();
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public LocalDateTimeDescriptor() {
		super(LocalDateTime.class);
	}
	
	@Override
	public String toString(LocalDateTime value) {
		return value.format(FORMATTER);
	}

	@Override
	public LocalDateTime fromString(String string) {
		return LocalDateTime.parse(string, FORMATTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X> X unwrap(LocalDateTime value, Class<X> type,
			WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( LocalDateTime.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		long instant = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		if ( Calendar.class.isAssignableFrom( type ) ) {
			return (X) new Calendar.Builder().setInstant(instant).build();
		}
		if ( java.sql.Date.class.isAssignableFrom( type ) ) {
			return (X) new java.sql.Date( instant );
		}
		if ( java.sql.Time.class.isAssignableFrom( type ) ) {
			return (X) new java.sql.Time( instant );
		}
		if ( java.sql.Timestamp.class.isAssignableFrom( type ) ) {
			return (X) new java.sql.Timestamp( instant );
		}
		if ( Date.class.isAssignableFrom( type ) ) {
			return (X) new  Date( instant );
		}
		throw unknownUnwrap( type );
	}

	@Override
	public <X> LocalDateTime wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( LocalDateTime.class.isInstance( value ) ) {
			return (LocalDateTime) value;
		}
		Instant instant;
		if ( Calendar.class.isInstance( value ) ) {
			instant = ((Calendar) value).toInstant();
		} else if ( Date.class.isInstance( value ) ) {
			instant = ((Date) value).toInstant();
		} else {
			throw unknownWrap( value.getClass() );
		}

		return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
