package note_app.roman.note_app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import note_app.roman.note_app.R;
import note_app.roman.note_app.ui.fragment.LogRegFragment;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.Preference;

public class RegLogActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LogRegFragment logRegFragment1;
    private LogRegFragment logRegFragment2;
    private Unbinder unbinder;
    @BindView(R.id.btnReg)
    Button btnReg;

    @BindView(R.id.btnLog)
    Button btnLog;

    @BindView(R.id.llLogRegButtons)
    LinearLayout llLogRegButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log);

        unbinder = ButterKnife.bind(this);

        if (!(Preference.getUser(this).equals("Empty User"))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }

        fragmentManager = getSupportFragmentManager();
        logRegFragment1 = new LogRegFragment();
        logRegFragment2 = new LogRegFragment();
    }

    @OnClick(R.id.btnLog)
    public void clickLog() {
        logRegFragment1.setType(Constants.LOG);
        fragmentManager
                .beginTransaction()
                .remove(logRegFragment2)
                .replace(R.id.llRegLog, logRegFragment1)
                .commit();
    }

    @OnClick(R.id.btnReg)
    public void clickReg() {
        logRegFragment2.setType(Constants.REG);
        fragmentManager
                .beginTransaction()
                .remove(logRegFragment1)
                .replace(R.id.llRegLog, logRegFragment2)
                .commit();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
