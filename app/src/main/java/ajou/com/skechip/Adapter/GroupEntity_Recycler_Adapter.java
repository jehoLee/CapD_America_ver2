package ajou.com.skechip.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ajou.com.skechip.Fragment.bean.GroupEntity;
import ajou.com.skechip.R;

import java.util.ArrayList;

public class GroupEntity_Recycler_Adapter extends RecyclerView.Adapter<GroupEntity_Recycler_Adapter.ViewHolder> {
    private ArrayList<GroupEntity> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mImageView;
        private TextView mTextView;
        private TextView tagText;
        private TextView memberNumText;
        private ListView mListView;
        private Meeting_Recycler_Adapter mAdapter;

        public ViewHolder(View view) {
            super(view);
//            mImageView = view.findViewById(R.id.image);
            mTextView = view.findViewById(R.id.group_name_edit_text);
            tagText = view.findViewById(R.id.group_tag_edit_text);
            memberNumText = view.findViewById(R.id.member_num);

//            mListView = view.findViewById(R.id.meeting_schedule_list);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GroupEntity_Recycler_Adapter(ArrayList<GroupEntity> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GroupEntity_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getGroupTitle());
        holder.tagText.setText(mDataset.get(position).getGroupTag());
        holder.memberNumText.setText(Integer.toString(mDataset.get(position).getGroupMemberNum()));
//        holder.mImageView.setImageResource(mDataset.get(position).getGroupImg());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

