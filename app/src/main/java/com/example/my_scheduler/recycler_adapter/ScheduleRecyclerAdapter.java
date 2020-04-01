package com.example.my_scheduler.recycler_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.my_scheduler.R;
import com.example.my_scheduler.data.ScheduleRecyclerData;

import java.util.ArrayList;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    private static final String TAG = "ScheduleRecyclerAdapter";


    private ArrayList<ScheduleRecyclerData> data = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public ScheduleRecyclerAdapter(ArrayList<ScheduleRecyclerData> data) {
        this.data = data ;
    }

    // 커스텀 리스터 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v, int pos);
    }

    // 전달된 객체를 저장할 변수 정의
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;

    // 리스너 객체를 전달하는 메서드 정의
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        this.mLongListener = listener;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.schedule_item, parent, false) ;
        ScheduleRecyclerAdapter.ViewHolder vh = new ScheduleRecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ScheduleRecyclerAdapter.ViewHolder holder, int position) {

        ScheduleRecyclerData item = data.get(position) ;

        holder.title.setText(item.getTitle()) ;
        holder.end.setText(item.getEnd()) ;
        holder.start.setText(item.getStart()) ;
        holder.content.setText(item.getContent());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return data.size() ;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView end ;
        TextView start ;
        TextView content;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 아이템 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "item onClick");

                    int item_position = getAdapterPosition();
                    if(item_position != RecyclerView.NO_POSITION) {
                        // 아이템 클릭 이벤트 발생시, 리스너 객체 메서드 호출
                        if(mListener != null) {
                            mListener.onItemClick(v, item_position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    Log.i(TAG, "item onLongClick");

                    int item_position = getAdapterPosition();
                    if(item_position != RecyclerView.NO_POSITION) {
                        // 아이템 클릭 이벤트 발생시, 리스너 객체 메서드 호출
                        //if(mLongListener != null) {
                            mLongListener.onItemLongClick(v, item_position);
                        //}
                    }
                    return true;
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title) ;
            end = itemView.findViewById(R.id.end_date);
            start = itemView.findViewById(R.id.start_date) ;
            content = itemView.findViewById(R.id.content);
        }
    }
}