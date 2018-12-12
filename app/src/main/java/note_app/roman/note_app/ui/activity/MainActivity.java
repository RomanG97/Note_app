package note_app.roman.note_app.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import note_app.roman.note_app.R;
import note_app.roman.note_app.interfaces.DialogStateListener;
import note_app.roman.note_app.note.Note;
import note_app.roman.note_app.ui.fragment.FragmentForTabs;
import note_app.roman.note_app.utils.BaseActivity;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.Preference;
import note_app.roman.note_app.utils.dialog.DialogAddNote;

public class MainActivity extends BaseActivity implements SensorEventListener, DialogStateListener {

    private static final String TAG = "MainActivity";
    private TabLayout tabs;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private DialogAddNote customDialog;
    private boolean isOpened;
    private Realm realm;
    private int curTab = -1;
    private File photoFile;
    private Unbinder unbinder;
    private ViewPagerAdapter adapter;
    private ViewPager vpViewPager;

    FragmentForTabs fragment1;
    FragmentForTabs fragment2;
    FragmentForTabs fragment3;

    long lastUpdate = 0;
    float last_x = 0;
    float last_y = 0;
    float last_z = 0;


    @BindView(R.id.ivDelete)
    ImageView ivDelete;

    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;

    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initializationUI();


    }

    @OnClick(R.id.ivAddNote)
    public void clickAddNote() {
        if (isOpened) {
            return;
        }

        customDialog = new DialogAddNote(this, this);
        customDialog.showDialog();
    }


    @OnClick(R.id.ivSettings)
    public void clickSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }


    @OnClick(R.id.ivDelete)
    public void clickDelete() {
        switch (curTab) {
            case 0:
                realm.executeTransaction(realm -> {
                    RealmResults<Note> rows = realm.where(Note.class)
                            .equalTo("login", Preference.getUser(this))
                            .equalTo("type", Constants.TASKS)
                            .findAll();
                    rows.deleteAllFromRealm();
                });
                break;
            case 1:
                realm.executeTransaction(realm -> {
                    RealmResults<Note> rows = realm.where(Note.class)
                            .equalTo("login", Preference.getUser(this))
                            .equalTo("type", Constants.NOTES)
                            .findAll();
                    rows.deleteAllFromRealm();
                });
                break;
            case 2:
                realm.executeTransaction(realm -> {
                    RealmResults<Note> rows = realm.where(Note.class).equalTo("login", Preference.getUser(this)).findAll();
                    rows.deleteAllFromRealm();
                });
                break;
        }
    }

    @OnClick(R.id.llLogout)
    public void clickIvLogout() {
        Preference.setUser(this, "Empty User");
        Intent intent = new Intent(this, RegLogActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupViewPager(ViewPager vpViewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment1, getString(R.string.tasks));
        adapter.addFragment(fragment2, getString(R.string.notes));
        adapter.addFragment(fragment3, getString(R.string.all));
        vpViewPager.setAdapter(adapter);
    }


    private void initTabBarLayout(ViewPager viewPager) {
        tabs.setupWithViewPager(viewPager);
        tabs.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab currentTab = tabs.getTabAt(i);
            if (currentTab != null) {
                currentTab.setCustomView(R.layout.custom_view_tab);
                View customView = currentTab.getCustomView();
                if (customView != null) {
                    TextView tvTabName = customView.findViewById(R.id.tvTabName);
                    ImageView ivTabIcon = customView.findViewById(R.id.ivTabIcon);
                    ivTabIcon.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.list_black_18dp));
                    switch (i) {
                        case Constants.FIRST_TAB:
                            tvTabName.setText(R.string.tasks);
                            break;
                        case Constants.SECOND_TAB:
                            tvTabName.setText(R.string.notes);
                            break;
                        case Constants.THIRD_TAB:
                            tvTabName.setText(R.string.all);
                            break;
                    }
                }
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tvTabName = customView.findViewById(R.id.tvTabName);
                    if (tvTabName != null) {
                        tvTabName.setTextSize(16);
                    }

                    switch (tab.getPosition()) {
                        case Constants.FIRST_TAB:
                            ivDelete.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.done_all_black_18dp));
                            curTab = 0;
                            break;
                        case Constants.SECOND_TAB:
                            ivDelete.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.delete_black_18dp));
                            curTab = 1;
                            break;
                        case Constants.THIRD_TAB:
                            ivDelete.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.delete_sweep_black_18dp));
                            curTab = 2;
                            break;
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tvTabName = customView.findViewById(R.id.tvTabName);
                    if (tvTabName != null) {
                        tvTabName.setTextSize(12);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout.Tab currentTab = tabs.getTabAt(Constants.FIRST_TAB);
        if (currentTab != null) {
            View customView = currentTab.getCustomView();
            if (customView != null) {
                TextView tvTabName = customView.findViewById(R.id.tvTabName);
                if (tvTabName != null) {
                    tvTabName.setTextSize(16);
                }

            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (Preference.getSensor(this)) {
            if (accelerometer == sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float x = sensorEvent.values[SensorManager.DATA_X];
                    float y = sensorEvent.values[SensorManager.DATA_Y];
                    float z = sensorEvent.values[SensorManager.DATA_Z];

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if (speed > 4000) {
                        clickAddNote();

                    }
                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void setState(boolean isOpened) {
        this.isOpened = isOpened;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }


    private void initializationUI() {
        realm = Realm.getDefaultInstance();

        fragment1 = new FragmentForTabs();
        fragment1.setFragmentType(Constants.TASKS);
        fragment2 = new FragmentForTabs();
        fragment2.setFragmentType(Constants.NOTES);
        fragment3 = new FragmentForTabs();
        fragment3.setFragmentType(Constants.ALL);

        ViewPager viewPager = findViewById(R.id.vpFragments);
        tabs = findViewById(R.id.tlTabs);

        setupViewPager(viewPager);
        initTabBarLayout(viewPager);

        tvSubTitle.setText(Preference.getUser(this));

    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensorListener();

    }

    @Override
    public void update() {
        super.update();
        switch (Preference.getColor(MainActivity.this)) {
            case 0:
                rlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_Red));
                setColorToStatusBar(getResources().getColor(R.color.theme_Red));
                break;

            case 1:
                rlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_Green));
                setColorToStatusBar(getResources().getColor(R.color.theme_Green));
                break;

            case 2:
                rlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_Blue));
                setColorToStatusBar(getResources().getColor(R.color.theme_Blue));
                break;
        }
    }

    public void setColorToStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void registerSensorListener() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            return;
        }
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 9001) {
            Uri uri = FileProvider.getUriForFile(
                    this,
                    "note_app.roman.note_app.fileprovider",
                    photoFile);

            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
