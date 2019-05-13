package ajou.com.skechip;

import ajou.com.skechip.Fragment.SelectMeetingTimeFragment;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MeetingCreateActivity extends AppCompatActivity {

    private GroupEntity groupEntity;
    private Bundle bundle;
    private SelectMeetingTimeFragment selectMeetingTimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("kakaoBundle");
            groupEntity = getIntent().getParcelableExtra("groupEntity");
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        selectMeetingTimeFragment = SelectMeetingTimeFragment.newInstance(bundle);
        transaction.add(R.id.frame_layout, selectMeetingTimeFragment);
        transaction.commit();








        Button createConfirmBtn = findViewById(R.id.create_meeting_button);
        createConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }









    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
