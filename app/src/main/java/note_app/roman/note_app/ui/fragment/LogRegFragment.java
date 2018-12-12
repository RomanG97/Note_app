package note_app.roman.note_app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import note_app.roman.note_app.R;
import note_app.roman.note_app.ui.activity.MainActivity;
import note_app.roman.note_app.user.User;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.Preference;

public class LogRegFragment extends Fragment {

    private int type = -1;
    private String Log = "";
    private String Pas = "";
    private View view;
    private Realm realm;
    private String patternPas = "(?=.*[0-9]).{5,}";
    private String patternLog = "(?=.*[a-z]).{5,}";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @BindView(R.id.etLog)
    EditText etLog;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnContinue)
    Button btnContinue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reg, null);

        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        if (Constants.REG == type) {
            btnContinue.setText("Register new user");
        } else {
            btnContinue.setText("Login");
        }

        etLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pas = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    @OnClick(R.id.btnContinue)
    public void clickBtnContinue() {
        if ("".equals(Log)) {
            Toast.makeText(getContext(), "Empty Login", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Constants.REG == type) {
            if (!(Log.matches(patternLog))) {
                Toast.makeText(getContext(), "Use 5 or more characters of the alphabet (a-z)", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if ("".equals(Pas)) {
            Toast.makeText(getContext(), "Empty Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Constants.REG == type) {
            if (!(Pas.matches(patternPas))) {
                Toast.makeText(getContext(), "Use 5 or more numeric characters (0-9)", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (getContext() != null) {
            Preference.setUser(getContext(), Log);
        } else {
            return;
        }

        if (Constants.REG == type) {
            List<User> myUserArray = realm.where(User.class).findAll();
            for (int i = 0; i < myUserArray.size(); i++) {
                if (Log.equals(Objects.requireNonNull(myUserArray.get(i)).getLogin())) {
                    Toast.makeText(getContext(), "This Login is already used", Toast.LENGTH_SHORT).show();
                }
            }
            realm.executeTransaction(realm -> realm.copyToRealm(new User(Log, Pas)));
            Preference.setUser(getContext(), Log);
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        } else {
            List<User> myUserArray = realm.where(User.class).findAll();
            for (int i = 0; i < myUserArray.size(); i++) {
                if (Log.equals(Objects.requireNonNull(myUserArray.get(i)).getLogin())) {
                    if (Pas.equals(Objects.requireNonNull(myUserArray.get(i)).getPassword())) {
                        Preference.setUser(getContext(), Log);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                        return;
                    }

                }
            }
            Toast.makeText(getContext(), "This Login/Password is incorrect or doesn't exist", Toast.LENGTH_LONG).show();
        }
    }
}
