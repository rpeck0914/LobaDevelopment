package rpeck.loba_login_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddBarSpecial extends Activity implements View.OnClickListener {

    private String mDayOfWeekString;
    private String mDescriptionString;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mMaxMonthDays;

    private TextView mSubmitButton, mBackButton;
    private EditText mDescriptionEntered;
    private Spinner mDateEnteredSpinner;

    private List<String> mDates;

    private String mBarid;
    private UserLocalStore mUserLocalStore;

    private static final String EXTRA_BAR_ID = "rpeck.loba_login_register.add_special.bar_id";

    public static Intent newIntent(Context context, String barId) {
        Intent intent = new Intent(context, AddBarSpecial.class);
        intent.putExtra(EXTRA_BAR_ID, barId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bar_special);

        mBarid = getIntent().getStringExtra(EXTRA_BAR_ID);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMaxMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        loadDaySpinner();

        mDateEnteredSpinner = (Spinner) findViewById(R.id.add_special_date_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDates);
        mDateEnteredSpinner.setAdapter(adapter);
        mDateEnteredSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                mDayOfWeekString = mDateEnteredSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });

        mDescriptionEntered = (EditText) findViewById(R.id.add_special_description_edit_text);
        mDescriptionEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDescriptionString = mDescriptionEntered.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBackButton = (TextView) findViewById(R.id.add_special_back_button);
        mSubmitButton = (TextView) findViewById(R.id.add_special_submit_button);

        mBackButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);

        mUserLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_special_back_button:
                this.finish();
                break;
            case R.id.add_special_submit_button:
                submitBarSpecial();
                break;
        }
    }

    private void loadDaySpinner() {
        String dateToAdd = (mMonth + 1) + "/" + mDay + "/" + mYear;

        mDates = new ArrayList<>();
        mDates.add(dateToAdd);

        for(int i = 0; i < 6; i++) {
            mDay += 1;
            if(mDay > mMaxMonthDays) {
                if(mMonth + 1 == 12) {
                    mYear += 1;
                    mMonth = 0;
                } else {
                    mMonth += 1;
                }
                mDay = 1;
                Calendar newMonth = new GregorianCalendar(mYear, mMonth, mDay);
                mMaxMonthDays = newMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            dateToAdd = (mMonth + 1) + "/" + mDay + "/" + mYear;
            mDates.add(dateToAdd);
        }
    }

    private void submitBarSpecial() {
        getDayOfWeek();

        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeSpecialDataAsyncTask(createSpecial(), new GetBarSpecialsCallback() {
            @Override
            public void done(DayOfWeek returnedDayOfWeek) {
                finishAddSpecialActivity();
            }
        });

    }

    private void getDayOfWeek() {
        String parts[] = (mDateEnteredSpinner.getSelectedItem().toString()).split("/");
        mYear = Integer.parseInt(parts[2]);
        mMonth = Integer.parseInt(parts[0]) - 1;
        mDay = Integer.parseInt(parts[1]);

        Calendar newDate = new GregorianCalendar(mYear, mMonth, mDay);
        mDayOfWeekString = newDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    }

    private DayOfWeek createSpecial() {
        User user = mUserLocalStore.getLoggedInUser();

        List<Specials> specialToAddArrayList = new ArrayList<>();

        Specials specialToAdd = new Specials(mDescriptionString, user.mUserName);

        specialToAddArrayList.add(specialToAdd);

        String parts[] = (mDateEnteredSpinner.getSelectedItem().toString()).split("/");
        mMonth = Integer.parseInt(parts[0]);
        mDay = Integer.parseInt(parts[1]);
        String date = mMonth + "/" + mDay;

        DayOfWeek dayOfWeekToAdd = new DayOfWeek(mDayOfWeekString, date, mBarid);
        dayOfWeekToAdd.addSpecials(specialToAdd);

        return dayOfWeekToAdd;
    }
    private void finishAddSpecialActivity(){
        this.finish();
    }
}
