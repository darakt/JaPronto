package com.example.darakt.japronto;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by darakt on 14/10/16.
 */

public class MyTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerListener{
        void OnfinshTimePick(String time);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimePickerListener activity = (TimePickerListener) getActivity();
        activity.OnfinshTimePick(hourOfDay + ":" + minute);
    }
}
