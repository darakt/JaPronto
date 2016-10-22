package com.example.darakt.japronto.order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by darakt on 14/10/16.
 */

public class MyDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerListener{
        void OnfinshDatePick(String date);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar now = Calendar.getInstance();
        final int day = now.get(Calendar.DAY_OF_MONTH),
                month = now.get(Calendar.MONTH),
                year = now.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Light_Dialog_NoActionBar, this, year, month, day);
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DatePickerListener activity = (DatePickerListener) getActivity();
        activity.OnfinshDatePick(year + "-" + monthOfYear + "-" + dayOfMonth);
    }
}
