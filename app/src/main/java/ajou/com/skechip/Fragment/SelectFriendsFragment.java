package ajou.com.skechip.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ajou.com.skechip.Adapter.FriendListAdapter;
import ajou.com.skechip.GroupCreateActivity;
import ajou.com.skechip.R;

public class SelectFriendsFragment extends Fragment {
    private List<AppFriendInfo> kakaoFriends;
    private FriendListAdapter friendListAdapter;

    public static SelectFriendsFragment newInstance(Bundle bundle) {
        SelectFriendsFragment fragment = new SelectFriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            kakaoFriends = bundle.getParcelableArrayList("kakaoFriends");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_select_friends, container, false);

        ListView selectableListView = view.findViewById(R.id.selectableListView);
        RelativeLayout checkInteractionView = view.findViewById(R.id.check_Interaction_view);

        friendListAdapter = new FriendListAdapter(getActivity(), kakaoFriends, checkInteractionView);
        selectableListView.setAdapter(friendListAdapter);

        Button confirmFriendsButton = view.findViewById(R.id.confirm_friends_button);

        confirmFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AppFriendInfo> selectedFriends = friendListAdapter.getSelectedFriends();
//                Toast.makeText(getActivity(), selectedFriends + "is selected!", Toast.LENGTH_SHORT).show();
                if(selectedFriends.size() == 0){
                    Toast.makeText(getActivity(), "모임을 생성하려면 적어도 2명 이상은 선택해야 합니다", Toast.LENGTH_LONG).show();

                }else if(selectedFriends.size() == 1){
                    Toast.makeText(getActivity(), "1명의 친구와 모임을 생성하려면 친구 탭에서 친구와의 약속을 잡으세요!", Toast.LENGTH_LONG).show();

                }else{
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("selectedFriends", (ArrayList<? extends Parcelable>) selectedFriends);

                    ((GroupCreateActivity) Objects.requireNonNull(getActivity())).replaceFragment(bundle);
                }




            }
        });


        return view;
    }









}
