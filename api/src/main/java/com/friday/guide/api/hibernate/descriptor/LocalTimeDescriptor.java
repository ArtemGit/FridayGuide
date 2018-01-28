package com.friday.guide.api.hibernate.descriptor;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class LocalTimeDescriptor extends AbstractTypeDescriptor<LocalTime>{
	public static final LocalTimeDescriptor INSTANCE = new LocalTimeDescriptor();
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	public LocalTimeDescriptor() {
		super(LocalTime.class);
	}
	
	@Override
	public String toString(LocalTime value) {
		return value.format(FORMATTER);
	}

	@Override
	public LocalTime fromString(String string) {
		return LocalTime.parse(string, FORMATTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X> X unwrap(LocalTime value, Class<X> type,
			WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( LocalTime.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		long instant = value.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
	public <X> LocalTime wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( LocalTime.class.isInstance( value ) ) {
			return (LocalTime) value;
		}
		Instant instant;
		if ( Calendar.class.isInstance( value ) ) {
			instant = ((Calendar) value).toInstant();
		} else if ( Date.class.isInstance( value ) ) {
			instant = ((Date) value).toInstant();
		} else {
			throw unknownWrap( value.getClass() );
		}

		return instant.atZone(ZoneId.systemDefault()).toLocalTime();
	}

}
