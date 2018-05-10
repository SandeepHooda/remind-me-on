import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Test {

	public static void main(String[] args) throws ParseException {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MONTH, 5);
		System.out.println(cal.get(Calendar.MONTH));
		cal.set(Calendar.DATE, 30);
		System.out.println(cal.get(Calendar.MONTH));
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.DATE, 30);
		System.out.println(cal.getTime());

	}

}
