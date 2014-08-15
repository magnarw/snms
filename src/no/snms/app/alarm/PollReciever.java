package no.snms.app.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.snms.app.MainApplication;
import no.snms.app.database.SnmsDAO;
import no.snms.app.domain.PreyItem;
import no.snms.app.preylist.PreyOverviewFragment;
import no.snms.app.utils.SnmsPrayTimeAdapter;

import org.joda.time.DateTime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

public class PollReciever extends BroadcastReceiver {

	private SnmsDAO dao;
	List<Alarm> alarms;
	PreyItem prey;
	PreyOverviewFragment prayOverview = new PreyOverviewFragment();

	// public PollReciever(SnmsDAO snmsDAO){
	// this.dao = snmsDAO;
	// }

	@Override
	public void onReceive(Context context, Intent myIntent) {
		dao = new SnmsDAO(context);
		// Get alarms after reboot
		AlarmUtilities Util = new AlarmUtilities(dao);
		alarms = dao.getAlarms();
		if (alarms != null) {
			DateTime ThisDay = Util.getDateTimeNow();
			ThisDay = ThisDay.minusHours(ThisDay.getHourOfDay())
					.minusMinutes(ThisDay.getMinuteOfHour())
					.minusSeconds(ThisDay.getSecondOfMinute());
			List<PreyItem> PreyItemList = new ArrayList<PreyItem>();
			PreyItemList = loadPrayTimes(ThisDay, context);
			for (Alarm alarm : alarms) {
				for (PreyItem item : PreyItemList) {
					if (item.getName().equals(alarm.getPrey())) {
						prey = item;
						Util.SetRepeatingAlarm(prey, alarm.getId(),
								getAppContext(), alarm.getPrey(),
								alarm.getOffset());
						System.out.println("Alarm has been set on reboot!");
					}
				}
			}
		} else {
			System.out.println("There are no alarms to be set on reboot!");
		}

		dao.closeDB();

	}

	public static Context getAppContext() {
		return MainApplication.getAppContext();
	}

	public List<PreyItem> loadPrayTimes(DateTime dateTime, Context context) {
		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter(
				context.getAssets(),dao);
		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay())
				.minusMinutes(dateTime.getMinuteOfHour())
				.minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight);

	}
}
