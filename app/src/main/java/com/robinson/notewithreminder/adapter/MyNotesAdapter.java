package com.robinson.notewithreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robinson.notewithreminder.R;
import com.robinson.notewithreminder.model.NoteItemModel;

import java.util.List;

public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.ViewHolder> {
    private Context context;
    private List<NoteItemModel> itemList;
    private MyItemClickListener itemClickListener;
    public MyNotesAdapter(Context context, List<NoteItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      //  NoteItemModel item = itemList.get(position);
        final NoteItemModel s = itemList.get(position);
        String rem_DateTime = s.Time + " " + s.Date;
        if (s.Time.equalsIgnoreCase("notset")) {
            holder.txt_datetime_rem.setVisibility(View.GONE);
        } else {
            holder.txt_datetime_rem.setVisibility(View.VISIBLE);
            holder.txt_datetime_rem.setText(rem_DateTime);
        }
        holder.txt_title.setText(s.Title);
        holder.txt_title.setSelected(true);
        holder.txt_desc.setText(s.Description);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_datetime_rem, txt_title, txt_desc;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.title_tv2);
            txt_desc = (TextView) itemView.findViewById(R.id.desc_tv2);
            txt_datetime_rem = (TextView) itemView.findViewById(R.id.date_time_id_rem);
        }
    }

    public interface MyItemClickListener {
        void onItemClick(NoteItemModel data);
    }
}
