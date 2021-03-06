package note_app.roman.note_app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import note_app.roman.note_app.R;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.Preference;

public class SettingsActivity extends AppCompatActivity {

    private String info;
    private int color;
    private Unbinder unbinder;

    @BindView(R.id.rbRed)
    RadioButton rbRed;
    @BindView(R.id.rbGreen)
    RadioButton rbGreen;
    @BindView(R.id.rbBlue)
    RadioButton rbBlue;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.cbShake)
    CheckBox cbShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);


        this.info = Preference.getInfo(this);
        tvInfo.setText(info);
        this.color = Preference.getColor(this);

        switch (color) {
            case Constants.RED:
                rbRed.toggle();
                break;
            case Constants.GREEN:
                rbGreen.toggle();
                break;
            case Constants.BLUE:
                rbBlue.toggle();
                break;
        }
        cbShake.setChecked(Preference.getSensor(this));


    }

    @OnClick(R.id.rbRed)
    public void clickRbRed() {
        color = Constants.RED;
    }

    @OnClick(R.id.rbGreen)
    public void clickRbGreen() {
        color = Constants.GREEN;
    }

    @OnClick(R.id.rbBlue)
    public void clickRbBlue() {
        color = Constants.BLUE;
    }

    @OnClick(R.id.btnSettingsOK)
    public void clickBtnSettingsOK() {

        Preference.setColor(this, color);

        if (cbShake.isChecked()) {
            Preference.setSensor(this, true);
        } else {
            Preference.setSensor(this, false);
        }

        finish();
    }

    @OnClick(R.id.btnSettingsCancel)
    public void clickBtnSettingsCancel() {

        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
