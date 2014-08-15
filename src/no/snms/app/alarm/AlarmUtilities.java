package no.snms.app.alarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.snms.app.database.SnmsDAO;
import no.snms.app.domain.PreyItem;
import no.snms.app.preylist.PreyOverviewFragment;
import no.snms.app.utils.SnmsPrayTimeAdapter;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmUtilities {

	private SnmsDAO dao;
	List<Alarm> alarms;
	// private DateTime currentDate;
	final Calendar cal = Calendar.getInstance();

	public AlarmUtilities(SnmsDAO snmsDAO) {
		this.dao = snmsDAO;
	}

	public void RemoveAlarm(int id, Context context, String name) {
		// Intent intent = new Intent(this, AlarmReceiverActivity.class);
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		PendingIntent sender = PendingIntent.getActivity(context, id, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Activity.ALARM_SERVICE);
		alarmManager.cancel(sender);

//		Toast.makeText(context, "Alarm er blitt deaktivert.",
//				Toast.LENGTH_SHORT).show();

		if (hasAlarm(name)) {
			dao.deleteAlarm(name);
			alarms = dao.getAlarms();
			dao.closeDB();
		}


	}

	// public void SetAlarm(Calendar cal, int id, Intent intent, Context
	// context) {
	// // TODO Auto-generated method stub
	//
	//
	// if (cal.after(Calendar.getInstance())) {
	//
	// // Intent intent = new Intent(this, AlarmReceiverActivity.class);
	// PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
	// intent, PendingIntent.FLAG_CANCEL_CURRENT);
	// AlarmManager am = (AlarmManager)
	// context.getSystemService(Activity.ALARM_SERVICE);
	// am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
	//
	// Toast.makeText(context, "Alarm er blitt satt",
	// Toast.LENGTH_SHORT).show();
	//
	// }else{
	// Toast.makeText(context, "Oops... Alarmdato har allerede passert",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// }

	public boolean hasAlarm(String name) {
		alarms = dao.getAlarms();
		for (Alarm alarm : alarms) {
			if (alarm.getPrey().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public Alarm getAlarm(String name) {
		if (alarms == null)
			alarms = dao.getAlarms();

		for (Alarm alarm : alarms) {
			System.out.println("Dette er alarm name: " + name);
			System.out.println("Dette er alarm name: " + alarm.getPrey());
			System.out.println("Dette er alarm id: " + alarm.getId());
			if (alarm.getPrey().equals(name)) {
				return alarm;
			}
		}
		dao.closeDB();

		return null;

	}

	public void SetRepeatingAlarm(PreyItem prey, int id, Context context,
			String name, int offset) {

		// if (cal.after(Calendar.getInstance())) {
		// DateTime DateTime;
		DateTime ThisDay = org.joda.time.DateTime.now();
		DateTime NextDay = org.joda.time.DateTime.now().plusDays(1);
		// PreyOverviewFragment prayOverview = new PreyOverviewFragment();
		SnmsPrayTimeAdapter snmspraytimeadapter = new SnmsPrayTimeAdapter(
				context.getAssets(),dao);
		ThisDay = ThisDay.minusHours(ThisDay.getHourOfDay())
				.minusMinutes(ThisDay.getMinuteOfHour())
				.minusSeconds(ThisDay.getSecondOfMinute());

		NextDay = NextDay.minusHours(NextDay.getHourOfDay())
				.minusMinutes(NextDay.getMinuteOfHour())
				.minusSeconds(NextDay.getSecondOfMinute());
		
		DateTime thisPrey = new DateTime();
		DateTime nextPrey = new DateTime();
		List<PreyItem> PreyItemList = new ArrayList<PreyItem>();

		PreyItemList = snmspraytimeadapter.getPrayListForDate(ThisDay);

		for (PreyItem item : PreyItemList) {
			if (item.getName().equals(name)) {
				prey = item;
				thisPrey = prey.getTime().minusMinutes(offset);
			}
		}

		if (prey.getTime().isBefore(org.joda.time.DateTime.now())) {
			// Get the time for the next event the day after for repeating
			ThisDay = NextDay;
			NextDay = NextDay.plusDays(1);

			PreyItemList = snmspraytimeadapter.getPrayListForDate(ThisDay);

			for (PreyItem item : PreyItemList) {
				if (item.getName().equals(name)) {
					prey = item;
					thisPrey = prey.getTime().minusMinutes(offset);
				}
			}

		}

		PreyItemList = snmspraytimeadapter.getPrayListForDate(NextDay);
		

		for (PreyItem item : PreyItemList) {
			if (item.getName().equals(name)) {
				nextPrey = item.getTime().minusMinutes(offset);
			}
		}

		long intervalMillis = nextPrey.getMillis() - thisPrey.getMillis();

		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Activity.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, thisPrey.getMillis(),
				intervalMillis, pendingIntent);



		Alarm alarm = new Alarm(name, offset, id);
		dao.insertAlarm(alarm);
		dao.closeDB();


		dao.getAlarms(); // Fjern denne

		dao.closeDB(); // Fjern denne

	}

	public DateTime getDateTimeNow() {
		DateTime ThisDay = DateTime.now();

		return ThisDay;

	}

	public int getAlarmId(DateTime alarmId) {

		int id = Integer.parseInt(Integer.toString(alarmId.getYear() - 2012)
				+ Integer.toString(alarmId.getMonthOfYear())
				+ Integer.toString(alarmId.getDayOfMonth())
				+ Integer.toString(alarmId.getHourOfDay())
				+ Integer.toString(alarmId.getMinuteOfHour()));

		System.out.println("ID:" + id);
		return id;

	}

	// Refactor from String (2013:9:23:...) to Integer[]
	public Integer[] RefactorToIntegerForSettingRebootAlarm(
			String AlarmDateString) {

		Integer[] AlarmDateInt = new Integer[6];

		String[] separated = AlarmDateString.split(":");

		AlarmDateInt[0] = Integer.parseInt(separated[0]);
		AlarmDateInt[1] = Integer.parseInt(separated[1]);
		AlarmDateInt[2] = Integer.parseInt(separated[2]);
		AlarmDateInt[3] = Integer.parseInt(separated[3]);
		AlarmDateInt[4] = Integer.parseInt(separated[4]);
		AlarmDateInt[5] = Integer.parseInt(separated[5]);

		return AlarmDateInt;
	}

	public String RefactorToStringForSavingAlarm(Integer[] date) {
		String AlarmDateString = Integer.toString(date[0]) + ":"
				+ Integer.toString(date[1]) + ":" + Integer.toString(date[2])
				+ ":" + Integer.toString(date[3]) + ":"
				+ Integer.toString(date[4]) + ":" + Integer.toString(date[5]);

		return AlarmDateString;
	}

	public Integer[] RefactorToIntegerFromDatabase(String AlarmDate) {

		Integer[] AlarmDateInteger = new Integer[6];
		String[] AlarmDateString = new String[6];
		AlarmDateString = AlarmDate.split(":");
		AlarmDateInteger[0] = Integer.parseInt(AlarmDateString[0]);
		AlarmDateInteger[1] = Integer.parseInt(AlarmDateString[1]);
		AlarmDateInteger[2] = Integer.parseInt(AlarmDateString[2]);
		AlarmDateInteger[3] = Integer.parseInt(AlarmDateString[3]);
		AlarmDateInteger[4] = Integer.parseInt(AlarmDateString[4]);
		AlarmDateInteger[5] = Integer.parseInt(AlarmDateString[5]);

		return AlarmDateInteger;
	}

	public Calendar GregorianCalender(Integer[] date) {

		// Create an offset from the current time in which the alarm will go
		// off.
		Calendar cal = Calendar.getInstance();
		// Calendar now = Calendar.getInstance();
		int YearNow = cal.get(Calendar.YEAR);
		int MonthNow = cal.get(Calendar.MONTH);
		int DayNow = cal.get(Calendar.DAY_OF_MONTH);
		int HourNow = cal.get(Calendar.HOUR_OF_DAY);
		int MinuteNow = cal.get(Calendar.MINUTE);
		int SecondNow = cal.get(Calendar.SECOND);

		cal.add(Calendar.YEAR, date[0] - YearNow);
		cal.add(Calendar.MONTH, date[1] - MonthNow);
		cal.add(Calendar.DAY_OF_MONTH, date[2] - DayNow);
		cal.add(Calendar.HOUR_OF_DAY, date[3] - HourNow);
		cal.add(Calendar.MINUTE, date[4] - MinuteNow);
		cal.add(Calendar.SECOND, -SecondNow);

		// System.out.println("year");
		// System.out.println(date[0] - YearNow);
		// System.out.println("month");
		// System.out.println(date[1] - MonthNow);
		// System.out.println("day");
		// System.out.println(date[2] - DayNow);
		// System.out.println("hour");
		// System.out.println(date[3] - HourNow);
		// System.out.println("minute");
		// System.out.println(date[4] - MinuteNow);
		// System.out.println("second");
		// System.out.println(-SecondNow);

		return cal;

	}
}
