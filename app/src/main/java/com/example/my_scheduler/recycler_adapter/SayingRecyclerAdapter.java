package com.example.my_scheduler.recycler_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.my_scheduler.R;
import com.example.my_scheduler.data.SayingRecyclerData;

import java.util.ArrayList;

public class SayingRecyclerAdapter extends RecyclerView.Adapter<SayingRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SayingRecyclerAdapter";


    private ArrayList<SayingRecyclerData> data = null ;

    public void setData(ArrayList<SayingRecyclerData> list) {
        data = list;
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SayingRecyclerAdapter(ArrayList<SayingRecyclerData> list) {
        data = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SayingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.saying_item, parent, false) ;
        SayingRecyclerAdapter.ViewHolder vh = new SayingRecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SayingRecyclerAdapter.ViewHolder holder, int position) {

        SayingRecyclerData item = data.get(position) ;

        holder.img.setImageResource(item.getImg());
        holder.wise_saying.setText(item.getWiseSaying());
        holder.saying_man.setText(item.getSayingMan());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return data.size() ;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView wise_saying;
        public TextView saying_man;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            img = itemView.findViewById(R.id.wise_saying_image);
            wise_saying = itemView.findViewById(R.id.wise_saying_text);
            saying_man = itemView.findViewById(R.id.saying_man_text);
        }
    }
}