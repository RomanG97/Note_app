package note_app.roman.note_app.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import note_app.roman.note_app.R;
import note_app.roman.note_app.utils.Constants;

public class FragmentForTimePicker extends DialogFragment {

    private TimePicker timePicker;
    private int curHours;
    private int curMinutes;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final FragmentActivity activity = getActivity();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_time, null);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Date time = (Date) arguments.getSerializable(Constants.ARG_TIME);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);

            timePicker = view.findViewById(R.id.dialog_time_picker);

            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    curHours = hourOfDay;
                    curMinutes = minute;
                }
            });
        }

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if (!(activity instanceof TempActivity)) {
//                            return;
//                        }
//                        DialogAddNote.hours = curHours;
//                        DialogAddNote.minutes = curMinutes;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }

    public static FragmentForTimePicker newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_TIME, time);

        FragmentForTimePicker dialog = new FragmentForTimePicker();
        dialog.setArguments(args);

        return dialog;
    }


}