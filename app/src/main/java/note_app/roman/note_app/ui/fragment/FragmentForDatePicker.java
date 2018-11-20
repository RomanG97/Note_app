package note_app.roman.note_app.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import note_app.roman.note_app.R;
import note_app.roman.note_app.utils.Constants;

public class FragmentForDatePicker extends DialogFragment {

    private DatePicker datePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final FragmentActivity activity = getActivity();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Date date = (Date) arguments.getSerializable(Constants.ARG_DATE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            datePicker = view.findViewById(R.id.dialog_date_picker);

            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);
        }

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
//                    if (!(activity instanceof MainActivity)) {
//                        return;
//                    }
//                    DialogAddNote.year = datePicker.getYear();
//                    DialogAddNote.month = datePicker.getMonth();
//                    DialogAddNote.day = datePicker.getDayOfMonth();

                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.cancel())
                .create();
    }

    public static FragmentForDatePicker newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_DATE, date);

        FragmentForDatePicker dialog = new FragmentForDatePicker();
        dialog.setArguments(args);
        return dialog;
    }

}
