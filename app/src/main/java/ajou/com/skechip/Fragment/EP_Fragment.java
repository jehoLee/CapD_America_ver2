package ajou.com.skechip.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import ajou.com.skechip.Adapter.EP_CustomAdapter;
import ajou.com.skechip.CalendarActivity;
import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.ColTitle;
import ajou.com.skechip.Fragment.bean.FriendEntity;
import ajou.com.skechip.Fragment.bean.RowTitle;
import ajou.com.skechip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ajou.com.skechip.UploadingActivity;
import cn.zhouchaoyuan.excelpanel.ExcelPanel;

public class EP_Fragment extends Fragment {
    public static final String WEEK_FORMAT_PATTERN = "EEEE";
    public List<String> PLACE_NAME = new ArrayList<String>();
    public List<String> SUBJECT_NAME = new ArrayList<String>();
    public List<Cell> SELECTED_CELLS = new ArrayList<Cell>();
    public static final long ONE_DAY = 24 * 3600 * 1000;
    public static final int PAGE_SIZE = 5;
    public static final int ROW_SIZE = 8;
    private Button append;
    private ImageButton change;
    private ImageButton change_selected;
    private ImageButton calendar;
    private ImageButton setting;
    private ExcelPanel excelPanel;
    private ProgressBar progress;
    private EP_CustomAdapter adapter;
    private boolean revise_mode;
    private List<RowTitle> rowTitles;
    private List<ColTitle> colTitles;
    private List<List<Cell>> cells;
    private SimpleDateFormat weekFormatPattern;
    private TextView title;
    private List<String> friendsNickname_list = new ArrayList<>();
    private String kakaoUserImg;
    private String kakaoUserName;
    private Long kakaoUserID;
    private List<AppFriendInfo> kakaoFriendInfo_list;
    private List<FriendEntity> friendEntities = new ArrayList<>();
    private Boolean timeTableUploaded;

    public static EP_Fragment newInstance(Bundle bundle) {
        EP_Fragment fragment = new EP_Fragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            kakaoUserID = bundle.getLong("kakaoUserID");
            kakaoUserName = bundle.getString("kakaoUserName");
            kakaoUserImg = bundle.getString("kakaoUserImg");
            kakaoFriendInfo_list = bundle.getParcelableArrayList("kakaoFriendInfo_list");
            friendsNickname_list = bundle.getStringArrayList("friendsNickname_list");

//            timeTableUploaded = bundle.getBoolean("timeTableUploaded");
            timeTableUploaded = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (timeTableUploaded) { // for 정흠
            view = inflater.inflate(R.layout.fragment_time_table, container, false);
            calendar = view.findViewById(R.id.calendar);
            change = view.findViewById(R.id.change_timetable);
            change_selected = view.findViewById(R.id.change_timetable_selected);
            setting = view.findViewById(R.id.timetable_setting);
            excelPanel = view.findViewById(R.id.content_container);
            progress = view.findViewById(R.id.progress);
            adapter = new EP_CustomAdapter(getActivity(), blockListener);
            title=view.findViewById(R.id.center_desc_text);
            excelPanel.setAdapter(adapter);
            append = view.findViewById(R.id.append_timetable_button);
            append.setVisibility(View.INVISIBLE);
            PLACE_NAME.add("");
            PLACE_NAME.add("팔달325");
            PLACE_NAME.add("팔달409");
            PLACE_NAME.add("팔달410");
            PLACE_NAME.add("팔달309");
            PLACE_NAME.add("팔달409");

            SUBJECT_NAME.add("");
            SUBJECT_NAME.add("정보보호");
            SUBJECT_NAME.add("이산수학");
            SUBJECT_NAME.add("확률통계");
            SUBJECT_NAME.add("캡디");
            SUBJECT_NAME.add("컴파일러");
//        revise_mode = bundle.getBoolean("revise_mode");
            revise_mode = false;
            initData();

            calendar.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CalendarActivity.class);
                    startActivity(intent);
                }
            });
            setting.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CalendarActivity.class);
                    startActivity(intent);
                }
            });
            change.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "일정 추가할 시간대를 선택해주세요", Toast.LENGTH_LONG).show();
                    change_selected.setVisibility(View.VISIBLE);
                    change.setVisibility(View.GONE);
                    revise_mode = true;
                    title.setText("추가");
                    append.setVisibility(View.VISIBLE);
                }
            });
            change_selected.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_LONG).show();
                    change_selected.setVisibility(View.GONE);
                    change.setVisibility(View.VISIBLE);
                    revise_mode = false;
                    append.setVisibility(View.INVISIBLE);
                    for(int i=0;i<SELECTED_CELLS.size();i++){
                        SELECTED_CELLS.get(i).setStatus(0);
                    }
                    adapter.setAllData(colTitles, rowTitles, cells);
                    SELECTED_CELLS.clear();
                }
            });

            append.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change_selected.setVisibility(View.GONE);
                    change.setVisibility(View.VISIBLE);
                    revise_mode = false;
                    append.setVisibility(View.INVISIBLE);

                    if (SELECTED_CELLS.size() > 0) {
                        Toast.makeText(getActivity(), "추가정보를 입력해주세요", Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View dialog_view = inflater.inflate(R.layout.dialog_revise_timetable, null);
                        builder.setView(dialog_view);
                        final Button submit = (Button) dialog_view.findViewById(R.id.confirm_timetable);
                        final Button submit_one = (Button) dialog_view.findViewById(R.id.revise_one_plan);
                        final EditText subject = (EditText) dialog_view.findViewById(R.id.timetable_subject);
                        final EditText place = (EditText) dialog_view.findViewById(R.id.timetable_place);
                        final TextView textviewLogo = (TextView) dialog_view.findViewById(R.id.textviewLogo);
                        final ImageButton delete_Button = (ImageButton) dialog_view.findViewById(R.id.delete_timetable);
                        final ImageButton all_delete_Button = (ImageButton) dialog_view.findViewById(R.id.delete_all_timetable);
                        final AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        textviewLogo.setText("추가");
                        dialog.show();
                        delete_Button.setVisibility(View.INVISIBLE);
                        all_delete_Button.setVisibility(View.INVISIBLE);
                        submit_one.setVisibility(View.INVISIBLE);
                        submit.setText("저장");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String strSubject = subject.getText().toString();
                                String strPlace = place.getText().toString();
                                boolean newone = true;
                                for (int i = 0; i < SUBJECT_NAME.size(); i++) {
                                    if (SUBJECT_NAME.get(i).equals(strSubject)) {
                                        for (int j = 0; j < SELECTED_CELLS.size(); j++) {
                                            SELECTED_CELLS.get(j).setStatus(i);
                                            SELECTED_CELLS.get(j).setSubjectName(strSubject);
                                            SELECTED_CELLS.get(j).setPlaceName(strPlace);
                                        }
                                        newone = false;
                                        break;
                                    }
                                }
                                if (newone) {
                                    for (int i = 0; i < SELECTED_CELLS.size(); i++) {
                                        SELECTED_CELLS.get(i).setStatus(SUBJECT_NAME.size());
                                        SELECTED_CELLS.get(i).setSubjectName(strSubject);
                                        SELECTED_CELLS.get(i).setPlaceName(strPlace);
                                    }
                                    SUBJECT_NAME.add(strSubject);
                                    PLACE_NAME.add(strPlace);
                                }
                                SELECTED_CELLS.clear();
                                dialog.dismiss();
                                adapter.setAllData(colTitles, rowTitles, cells);

                            }
                        });
                    }
                }
            });
        } else { // for 충희
            view = inflater.inflate(R.layout.fragment_upload_timetable, container, false);

            Button uploadBtn = view.findViewById(R.id.upload_button);
            uploadBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO : 업로드 액티비티 띄우고 갤러리 이미지 불러온 뒤 선택하게 하기
                    Intent intent = new Intent(getActivity(), UploadingActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }
    /*==================================Time Table code============================== */

    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Cell cell = (Cell) view.getTag();
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialog_view = inflater.inflate(R.layout.dialog_revise_timetable, null);
            builder.setView(dialog_view);
            final Button submit = (Button) dialog_view.findViewById(R.id.confirm_timetable);
            final Button submit_one = (Button) dialog_view.findViewById(R.id.revise_one_plan);
            final EditText subject = (EditText) dialog_view.findViewById(R.id.timetable_subject);
            final EditText place = (EditText) dialog_view.findViewById(R.id.timetable_place);
            final TextView textviewLogo = (TextView) dialog_view.findViewById(R.id.textviewLogo);
            final ImageButton delete_Button = (ImageButton) dialog_view.findViewById(R.id.delete_timetable);
            final ImageButton all_delete_Button = (ImageButton) dialog_view.findViewById(R.id.delete_all_timetable);
            final AlertDialog dialog = builder.create();
            if (cell.getStatus() == 0 && revise_mode) {      //빈칸 클릭
                SELECTED_CELLS.add(cell);
                cell.setStatus(-1);
                adapter.setAllData(colTitles, rowTitles, cells);
            } else if (cell.getStatus() == -1 && revise_mode) {
                SELECTED_CELLS.remove(cell);
                cell.setStatus(0);
                adapter.setAllData(colTitles, rowTitles, cells);
            } else if (cell.getStatus() != 0) {    //빈칸이 아닐때, 대화창을 띄워준다.
                textviewLogo.setText("수정");
                subject.setText(cell.getSubjectName());
                place.setText(cell.getPlaceName());
                submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strSubject = subject.getText().toString();
                        String strPlace = place.getText().toString();

                        for (int i = 0; i < cells.size(); i++) {
                            for (int j = 0; j < cells.get(i).size(); j++) {
                                Cell tmp = cells.get(i).get(j);
                                if (tmp.getStatus() == cell.getStatus()) {
                                    tmp.setSubjectName(strSubject);
                                    tmp.setPlaceName(strPlace);
                                }
                            }
                        }
                        dialog.dismiss();
                        adapter.setAllData(colTitles, rowTitles, cells);
                    }
                });
                submit_one.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strSubject = subject.getText().toString();
                        String strPlace = place.getText().toString();
                        cell.setSubjectName(strSubject);
                        cell.setPlaceName(strPlace);
                        dialog.dismiss();
                        adapter.setAllData(colTitles, rowTitles, cells);
                    }
                });
                all_delete_Button.setVisibility(View.VISIBLE);
                delete_Button.setVisibility(View.VISIBLE);
                delete_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        builder1.setMessage("해당 일정이 삭제됩니다. 계속하시겠습니까?");
                        builder1.setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        cell.setStatus(0);
                                        cell.setPlaceName("");
                                        cell.setSubjectName("");
                                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        adapter.setAllData(colTitles, rowTitles, cells);

                                    }
                                });
                        builder1.setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder1.create();
                        builder1.show();
                    }
                });
                all_delete_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        builder1.setMessage("관련된 모든 일정이 삭제됩니다. 계속하시겠습니까?");
                        builder1.setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int cur_tmp = cell.getStatus();
                                        for (int i = 0; i < cells.size(); i++) {
                                            for (int j = 0; j < cells.get(i).size(); j++) {
                                                Cell tmp = cells.get(i).get(j);
                                                if (tmp.getStatus() == cur_tmp) {
                                                    tmp.setStatus(0);
                                                    tmp.setPlaceName("");
                                                    tmp.setSubjectName("");
                                                }
                                            }
                                        }
                                        adapter.setAllData(colTitles, rowTitles, cells);
                                        Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder1.setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder1.create();
                        builder1.show();
                    }
                });
                dialog.show();
            }
        }
    };

    private void initData() {
        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
        }
        setOption();
    }

    private void setOption() {
        long startTime = 24 * 3600 * 1000 * 4;
        List<RowTitle> rowTitles1 = genRowData(startTime);

        List<List<Cell>> cells1 = genCellData();
        rowTitles.addAll(rowTitles1);
        for (int i = 0; i < cells1.size(); i++) {
            Log.e("sssss",""+cells1.get(i));
            cells.get(i).addAll(cells1.get(i));
        }
        if (colTitles.size() == 0) {
            colTitles.addAll(genColData());
        }
        progress.setVisibility(View.GONE);
        adapter.setAllData(colTitles, rowTitles, cells);
        adapter.disableFooter();
        adapter.disableHeader();
    }

    private List<RowTitle> genRowData(long startTime) {
        List<RowTitle> rowTitles = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE; i++) {
            RowTitle rowTitle = new RowTitle();
            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
            rowTitles.add(rowTitle);
        }
        return rowTitles;
    }

    private List<ColTitle> genColData() {
        List<ColTitle> colTitles = new ArrayList<>();
        int hour = 9;
        List<String> minute = new ArrayList<String>();
        minute.add(":00");
        minute.add(":30");

        for (int i = 1; i < ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            String c = new Character((char) (i + 64)).toString();
            colTitle.setRoomNumber(c);
            if (i % 2 == 1) {
                String str = hour + minute.get(0) + "~";
                hour++;
                str += hour + minute.get(1);
                colTitle.setRoomTypeName(str);
            } else {
                String str = hour + minute.get(1) + "~";
                hour += 2;
                str += hour + minute.get(0);
                colTitle.setRoomTypeName(str);
            }
            colTitles.add(colTitle);
        }
        return colTitles;
    }

    //====================================generate data==========================================
    private ArrayList<List<Cell>> genCellData() {
        ArrayList<List<Cell>> cells = new ArrayList<List<Cell>>();
        for (int i = 1; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            cells.add(cellList);
            for (int j = 0; j < PAGE_SIZE; j++) {
                Cell cell = new Cell();
                Random random = new Random();
                int number = random.nextInt(15);
                if (number == 1 || number == 2 || number == 3 || number == 4 || number == 5) {
                    cell.setStatus(number);
                    cell.setPlaceName(PLACE_NAME.get(number));
                    cell.setSubjectName(SUBJECT_NAME.get(number));
//                    Log.e("Cell val:",""+NAME[number]+i+j);
                } else {
                    cell.setStatus(0);
//                    Log.e("Cell val:",""+0);
                }
                cellList.add(cell);
            }
        }
        return cells;
    }
    private Cell getCell(String startTime, String dayofweek){
        List<Cell>cell;
        switch(startTime){
            case "09:00":
                cell = cells.get(0);
                break;
            case "10:30":
                cell = cells.get(1);
                break;

            case "12:00":
                cell = cells.get(2);
                break;

            case "01:30":
                cell = cells.get(3);
                break;

            case "03:00":
                cell = cells.get(4);
                break;

            case "04:30":
                cell = cells.get(5);
                break;

            case "06:00":
                cell = cells.get(6);
                break;

            default:
                cell=cells.get(7);
                break;
        }
        switch(dayofweek){
            case "mon":
                return cell.get(0);
            case "tue":
                return cell.get(1);
            case "wed":
                return cell.get(2);
            case "thu":
                return cell.get(3);
            default:
                return cell.get(4);
        }
    }
}