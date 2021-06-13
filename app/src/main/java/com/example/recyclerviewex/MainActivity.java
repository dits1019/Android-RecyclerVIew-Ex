package com.example.recyclerviewex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(new MainRecyclerViewAdapter());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    // RecyclerView 어댑터 생성
    private static class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewViewHolder> {

        // 레이아웃을 만들고 해당 아이템에 관련된 자료를 담을 뷰 홀더를 만듬,
        // 뷰 홀더를 마드는 과정에 LayoutInflater.from을 호출하여 레이아웃 R.layout.item_recylcerview에
        // 해당하는 뷰를 생성
        // 이후 생성된 뷰에 대한 정보를 가지고 있는 뷰 홀더를 생성
        @Override
        public MainRecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);

            return new MainRecyclerViewViewHolder(itemView);
        }


        // 뷰 홀더가 실제로 연결될 때 호출
        //onCreateViewHolder 후에 호출될 수 있고 이전에 만들어진 뷰 홀더를 재사용 가능
        // 리스트가 스크롤되며 이전에 보였던 아이템이 사라지게 되면 그 뷰와 그 뷰에 관련된 뷰 홀더는
        // 다른 아이템을 위해 재사용 될 수 있음
        @Override
        public void onBindViewHolder(@NonNull @NotNull MainActivity.MainRecyclerViewViewHolder holder, int position) {
            // 아이템 순번을 반영
            // position은 아이템의 위치(순번)
            holder.setTitle((position + 1) + "번째 아이템입니다.");
        }

        //리스트에 표시될 아이템 개수를 리턴
        @Override
        public int getItemCount() {
            return 10;
        }
    }

    // 뷰 홀더는 아이템에 대한 뷰와 그와 관련된 메타데이터를 가지고 있는 객체
    private static class MainRecyclerViewViewHolder extends RecyclerView.ViewHolder{
        // 아이템 순번을 반영하가 위해서 아이템에 title을 가져옴
        private final TextView title;

        public MainRecyclerViewViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title);
        }

        public void setTitle(String title){
            this.title.setText(title);
        }
    }

}