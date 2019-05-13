package ajou.com.skechip.Fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ajou.com.skechip.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectMeetingTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectMeetingTimeFragment extends Fragment {

    private String meetingType;

    public static SelectMeetingTimeFragment newInstance(Bundle bundle) {
        SelectMeetingTimeFragment fragment = new SelectMeetingTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_meeting_time, container, false);

        setMeetingTypeSpinner(view);

        return view;
    }


    private void setMeetingTypeSpinner(View view) {
        String[] meetingTypes = new String[]{"일정 타입을 선택하세요","매주 정기적인 일정", "이번 주만 일시적인 일정"};
        Spinner meetingTypeSpinner = view.findViewById(R.id.meeting_type_spinner);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, meetingTypes) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        meetingTypeSpinner.setAdapter(spinnerArrayAdapter);

        meetingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
//                    Toast.makeText
//                            (getActivity(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                    meetingType = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
