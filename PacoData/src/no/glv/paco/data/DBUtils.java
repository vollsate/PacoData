package no.glv.paco.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import no.glv.paco.intrfc.BaseValues;

public class DBUtils implements BaseValues {

	private static final SimpleDateFormat sdf = new SimpleDateFormat( DATE_PATTERN, Locale.getDefault() );

	private DBUtils() {
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String ConvertToString( Date date, String pattern ) {
		SimpleDateFormat simpleDF = sdf;
		if ( pattern != null )
			simpleDF = new SimpleDateFormat( pattern, Locale.getDefault() );

		return simpleDF.format( date );
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String ConvertToString( Date date ) {
		return ConvertToString( date, null );
	}

	/**
	 * 
	 * @param date
	 * @param pattern The patter to use. May be null, and default pattern from
	 *            {@link BaseValues#DATE_PATTERN} will be used
	 * @return
	 */
	public static Date ConvertStringToDate( String date, String pattern ) {
		SimpleDateFormat simpleDF = sdf;
		if ( pattern != null )
			simpleDF = new SimpleDateFormat( pattern, Locale.getDefault() );

		try {
			return simpleDF.parse( date );
		}
		catch ( ParseException e ) {
			return null;
		}
	}

}
