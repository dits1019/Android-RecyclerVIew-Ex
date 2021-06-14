package com.example.recyclerviewex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

        // 어댑터는 화면에 표시될 내용을 다루고 레이아웃 매니저는 화면에 어떤 방식으로 나열될지 결정
        // LinearLayoutManager로 배치되어 있고 이것은 순차적으로 배치되는 것을 의미
        // 그 외 GridLayoutManager는 격자로 배치
        // StaggeredGridLayoutManager는 크기가 다른 여러 요소를 격자로 배치
        recyclerView.setAdapter(new MainRecyclerViewAdapter());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DivideDecoration(this));

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

    private static class DivideDecoration extends RecyclerView.ItemDecoration {
        private final Paint paint;

        // 생성자에서 Paint 객체를 생성
        public DivideDecoration(Context context){
            paint = new Paint();
            // 5dp의 두께를 설정
            // dp 단위가 단말기마다 수치가 다르기 때문에 context.getResources().getDisplayMetrics().density를 이용해
            // 단말기마다 다른 수치를 계산하도록 함
            paint.setStrokeWidth(context.getResources().getDisplayMetrics().density * 5);
        }

        // onDraw 메서드는 각각의 뷰에서 어떻게 그려져야 할지 처리하는 메서드
        // 이 메서드에 전달되는 parent를 통해 리시트에 표시되는 항목들을 가져오고 그 영역에 c.drawLine을 통해 선을 그음
        @Override
        public void onDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
            for (int i = 0; i < parent.getChildCount(); i++){
                final View view = parent.getChildAt(i);
                c.drawLine(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom(), paint);
            }
        }
    }

}