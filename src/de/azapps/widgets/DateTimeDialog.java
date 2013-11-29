package de.azapps.widgets;

import com.fourmob.datetimepicker.date.DatePicker;
import com.fourmob.datetimepicker.date.DatePicker.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePicker;
import com.sleepbot.datetimepicker.time.TimePicker.KeyboardListener;
import com.sleepbot.datetimepicker.time.TimePicker.OnTimeSetListener;

import de.azapps.mirakelandroid.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

public class DateTimeDialog extends DialogFragment  {

	protected static final String TAG = "DateTimeDialog";

	public static DateTimeDialog newInstance(OnDateTimeSetListner callback,
			int year, int month, int dayOfMonth, int hourOfDay, int minute,
			boolean vibrate, boolean dark) {
		DateTimeDialog dt = new DateTimeDialog();
		dt.init(year,month,dayOfMonth,hourOfDay,minute);
		dt.setOnDateTimeSetListner(callback);
		// dt.initialize(callback, year, month, dayOfMonth, hourOfDay, minute,
		// vibrate, dark);
		return dt;
	}

	private void init(int year, int month, int dayOfMonth, int hourOfDay,
			int minute) {
		if(tp!=null){
			tp.setHour(hourOfDay,false);
			tp.setMinute(minute);
		}
		if(dp!=null){
			dp.setYear(year);
			dp.setMonth(month);
			dp.setDay(dayOfMonth);
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	};

	float startX;
	float startY;
	private ViewSwitcher viewAnimator;
	private TimePicker tp;
	private DatePicker dp;
	private boolean isCurrentDatepicker=true;
	private OnDateTimeSetListner mCallback;
	
	void setOnDateTimeSetListner(OnDateTimeSetListner listner){
		mCallback=listner;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View v = inflater.inflate(R.layout.date_time_picker, container);
		final Context ctx = getDialog().getContext();
		 Button switchToDate = (Button) v
		 .findViewById(R.id.datetime_picker_date);
		 Button switchToTime = (Button) v
		 .findViewById(R.id.datetime_picker_time);
		viewAnimator = (ViewSwitcher) v
				.findViewById(R.id.datetime_picker_animator);


		viewAnimator.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// viewAnimator.showNext();
			}
		});
		dp=(DatePicker)v.findViewById(R.id.date_picker);
		tp=(TimePicker)v.findViewById(R.id.time_picker);
		tp.set24HourMode(true);
		tp.setOnKeyListener(tp.getNewKeyboardListner(getDialog()));
		tp.setOnTimeSetListener(new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
				if(mCallback!=null){
					mCallback.onDateTimeSet(dp.getYear(), dp.getMonth(), dp.getDay(), hourOfDay, minute);
				}
				dismiss();
				
			}
			
			@Override
			public void onNoTimeSet() {
				if(mCallback!=null){
					mCallback.onNoTimeSet();
				}
				dismiss();
				
			}
		});
		
		dp.setOnDateSetListener(new OnDateSetListener() {
			
			@Override
			public void onNoDateSet() {
				if(mCallback!=null){
					mCallback.onNoTimeSet();
				}
				dismiss();
				
			}
			
			@Override
			public void onDateSet(DatePicker datePickerDialog, int year, int month,
					int day) {
				if(mCallback!=null){
					mCallback.onDateTimeSet(year, month, day, tp.getHour(), tp.getMinute());
				}
				dismiss();
				
			}
		});

		switchToDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!isCurrentDatepicker){
					viewAnimator.showPrevious();
					isCurrentDatepicker=true;
				}
			}
		});

		switchToTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(isCurrentDatepicker){
					viewAnimator.showNext();
					isCurrentDatepicker=false;

				}
			}
		});
		return v;

	};

	protected float getMinSwipe() {
		int height = viewAnimator.getHeight();
		int width = viewAnimator.getWidth();
		return (height > width ? width : height) / 3f;
	}

	public interface OnDateTimeSetListner {

		/**
		 * @param view
		 *            The view associated with this listener.
		 * @param hourOfDay
		 *            The hour that was set.
		 * @param minute
		 *            The minute that was set.
		 */
		void onDateTimeSet(int year, int month, int dayOfMonth, int hourOfDay,
				int minute);

		void onNoTimeSet();
	}

}
