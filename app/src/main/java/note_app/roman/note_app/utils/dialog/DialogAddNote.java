package note_app.roman.note_app.utils.dialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import note_app.roman.note_app.R;
import note_app.roman.note_app.interfaces.DialogStateListener;
import note_app.roman.note_app.note.Note;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.IdGenerator;
import note_app.roman.note_app.utils.NotificationReceiver;
import note_app.roman.note_app.utils.Preference;

import static android.content.Context.ALARM_SERVICE;

public class DialogAddNote {

    private Context activity;
    private String title = "";
    private String description = "";
    private String type = "";
    private String status = "";
    private long date;
    private int selectedItem = -1;
    private String user;
    private String filePath = "";

    private Note note = null;
    private Dialog dialog;
    private String typeOfDialog;

    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int hours = -1;
    private int minutes = -1;

    private AppCompatEditText etTitle;
    private AppCompatEditText etDescription;
    private Spinner spinner;
    private Button btnCalendar;
    private Button btnTime;
    private Button btnPortrait;
    private Button btnAddNoteOK;
    private Button btnAddNoteCancel;
    private ImageView ivPhoto;
    private LinearLayout llButtons;
    private ImageView ivDelete;
    private LinearLayout llTitleAndTrash;

    private DatePicker datePicker;
    private TimePicker timePicker;

    private Realm realm;
    private FragmentManager fragmentManager;
    private DialogStateListener dialogStateListener;

    private File photoFile;


    private AlarmManager alarmManager;
    private Intent alarmIntent;

    public DialogAddNote(AppCompatActivity activity, DialogStateListener dialogStateListener) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
        typeOfDialog = "AddDialog";
        user = Preference.getUser(activity);
        this.dialogStateListener = dialogStateListener;


    }

    public DialogAddNote(AppCompatActivity activity, Note note, DialogStateListener dialogStateListener) {
        this.activity = activity;
        this.note = note;
        fragmentManager = activity.getSupportFragmentManager();
        typeOfDialog = "CorrectionDialog";
        user = Preference.getUser(activity);
        this.dialogStateListener = dialogStateListener;
    }


    public void showDialog() {
        initUI();
        initTextListeners();
        initSpinner();

        btnAddNoteOK.setOnClickListener(v -> {
            if (isValidNote()) {

                saveNote();

                if (type.equals(Constants.TASKS)) {
                    alarmIntent.putExtra("TITLE", title);
                    alarmIntent.putExtra("DESCRIPTION", description);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,
                            (int) System.currentTimeMillis() + 10000,
                            alarmIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    if (alarmIntent != null) {
                        int SDK_INT = Build.VERSION.SDK_INT;
                        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    (int) (System.currentTimeMillis() + 10000),
                                    pendingIntent);
                        } else if (SDK_INT < Build.VERSION_CODES.M) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                    (int) (System.currentTimeMillis() + 10000),
                                    pendingIntent);
                        } else {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                    (int) (System.currentTimeMillis() + 10000),
                                    pendingIntent);
                        }
                    }
                }

                dialogStateListener.setState(false);
                dialog.dismiss();
            }
        });

        btnAddNoteCancel.setOnClickListener(v -> {
            dialogStateListener.setState(false);
            dialog.dismiss();
        });

        btnCalendar.setOnClickListener(v -> {
            llTitleAndTrash.setVisibility(View.GONE);
            etDescription.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            timePicker.setVisibility(View.GONE);
            llButtons.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();
            if (null != note) {
                calendar.setTime(new Date(note.getDate()));
            } else {
                if (year != -1 && month != -1 && day != -1) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                } else {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }

            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), (view, yearDP, monthOfYearDP, dayOfMonthDP) -> {
                        year = yearDP;
                        month = monthOfYearDP;
                        day = dayOfMonthDP;
                    });

            datePicker.setVisibility(View.VISIBLE);
        });

        btnTime.setOnClickListener(v -> {
            llTitleAndTrash.setVisibility(View.GONE);
            etDescription.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            datePicker.setVisibility(View.GONE);
            llButtons.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();
            if (null != note) {
                calendar.setTime(new Date(note.getDate()));
            } else {
                if (hours != -1 && minutes != -1) {
                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minutes);
                } else {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }

            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

            timePicker.setVisibility(View.VISIBLE);

            timePicker.setOnTimeChangedListener((view, hourOfDayTP, minuteTP) -> {
                hours = hourOfDayTP;
                minutes = minuteTP;
            });
        });

        btnPortrait.setOnClickListener(view -> {
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.GONE);
            llTitleAndTrash.setVisibility(View.VISIBLE);
            etDescription.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            llButtons.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.VISIBLE);

        });

        ivDelete.setOnClickListener(v -> {
            realm.executeTransaction(realm -> {
                RealmResults<Note> rows = realm.where(Note.class).equalTo("id", note.getId()).findAll();
                rows.deleteAllFromRealm();
            });
            dialogStateListener.setState(false);
            dialog.dismiss();
        });

        ivPhoto.setOnClickListener(view -> {

            photoFile = getPhotoFile();

            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            boolean canTakePhoto = photoFile != null && captureIntent.resolveActivity(activity.getPackageManager()) != null;
            if (!canTakePhoto) {
                ivPhoto.setVisibility(View.GONE);
                return;
            }

            Uri uri = FileProvider.getUriForFile(
                    activity,
                    "note_app.roman.note_app.fileprovider",
                    photoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            List<ResolveInfo> cameraActivities =
                    activity.getPackageManager()
                            .queryIntentActivities(
                                    captureIntent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo activityRI : cameraActivities) {
                activity.grantUriPermission(
                        activityRI.activityInfo.packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }


            ((Activity) activity).startActivityForResult(captureIntent, 9001);
            dialogStateListener.setPhotoFile(photoFile);


        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    private boolean isValidNote() {
        if (!verificationOfFields(title, description, type, status, user)) {
            Toast.makeText(activity, "Error of filling the fields", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * add Note to database
     */
    private void saveNote() {
        Calendar calendarForNote = Calendar.getInstance();
        calendarForNote.set(Calendar.YEAR, year);
        calendarForNote.set(Calendar.MONTH, month);
        calendarForNote.set(Calendar.DAY_OF_MONTH, day);
        calendarForNote.set(Calendar.HOUR_OF_DAY, hours);
        calendarForNote.set(Calendar.MINUTE, minutes);
        long date = calendarForNote.getTimeInMillis();

        if (!(photoFile == null || !photoFile.exists())) {
            filePath = photoFile.getPath();
        }
        else{
            filePath = "No_Photo_File";
        }

        realm.executeTransaction(realm -> realm.copyToRealm(new Note(IdGenerator.Generate(title),
                title, description, type, status, date,
                System.currentTimeMillis(), Preference.getUser(activity), filePath)));
    }

    private boolean verificationOfFields(String title, String description, String type,
                                         String status, String user) {

        if (TextUtils.isEmpty(title)) {
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            return false;
        }
        if (TextUtils.isEmpty(type)) {
            return false;
        }
        if (TextUtils.isEmpty(status)) {
            return false;
        }
        if (TextUtils.isEmpty(user)) {
            return false;
        }

        return true;
    }

    private void initUI() {
        dialogStateListener.setState(true);
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_note);

        etTitle = dialog.findViewById(R.id.etTitle);
        etDescription = dialog.findViewById(R.id.etDescription);
        btnCalendar = dialog.findViewById(R.id.btnCalendar);
        btnTime = dialog.findViewById(R.id.btnTime);
        btnPortrait = dialog.findViewById(R.id.btnPortrait);
        btnAddNoteOK = dialog.findViewById(R.id.btnAddNoteOK);
        ivPhoto = dialog.findViewById(R.id.ivPhoto);
        llButtons = dialog.findViewById(R.id.llButtons);
        btnAddNoteCancel = dialog.findViewById(R.id.btnAddNoteCancel);
        llTitleAndTrash = dialog.findViewById(R.id.llTitleAndTrash);
        ivDelete = dialog.findViewById(R.id.ivDelete);
        ivDelete.setVisibility(View.GONE);
        if ("CorrectionDialog".equals(typeOfDialog)) {
            ivDelete.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.GONE);
        }
        datePicker = dialog.findViewById(R.id.dialog_date_picker);
        datePicker.setVisibility(View.GONE);
        timePicker = dialog.findViewById(R.id.dialog_time_picker);
        timePicker.setVisibility(View.GONE);


        spinner = dialog.findViewById(R.id.spinner);

        realm = Realm.getDefaultInstance();

        setInfoToNote();

        alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(activity, NotificationReceiver.class);
    }

    private void setInfoToNote(){
        if (note != null) {
            etTitle.setText(note.getTitle());
            etDescription.setText(note.getDescription());
        }
    }
    private void initTextListeners() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_row,
                R.id.tvSpinnerItem, activity.getResources().getStringArray(R.array.items)) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v;
                v = super.getDropDownView(position, null, parent);
                return v;
            }
        };

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_row_dropdown);
        spinner.setAdapter(spinnerAdapter);

        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    type = Constants.TASKS;
                    status = Constants.CURRENT;
                    btnCalendar.setVisibility(View.VISIBLE);
                    btnTime.setVisibility(View.VISIBLE);
                    btnPortrait.setVisibility(View.VISIBLE);
                } else {
                    type = Constants.NOTES;
                    status = Constants.COMPLETED;
                    btnCalendar.setVisibility(View.GONE);
                    btnTime.setVisibility(View.GONE);
                    btnPortrait.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String getPhotoFilename() {
        return "IMG_" + user + "_" + title + ".jpg";
    }

    private File getPhotoFile() {
        File filesDir = activity.getFilesDir();
        return new File(filesDir, getPhotoFilename());
    }

}
