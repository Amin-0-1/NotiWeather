package com.example.forecast_mvvm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class TimePickerClass extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    public TimePickerClass(@NotNull TimePickerDialog.OnTimeSetListener mTimeSetListener) {
        listener = mTimeSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);

//        TimePickerDialog.OnTimeSetListener listener = (TimePickerDialog.OnTimeSetListener) getParentFragment();
        return new TimePickerDialog(getContext(), listener, hour, minute, DateFormat.is24HourFormat(getContext()));
    }
}

