package com.example.recyclerviewex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PageKeyedDataSource;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PokeAPI pokeAPI;

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

        // 레트로핏 초기화 과정
        // Retrofit 빌더에 주소 중 변하지 않는 앞 부분을 넣어줌
        // addConverterFactory에서 어떤 컨버터를 사용할지 지정하는 부분(현재는 Gson을 사용)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokeAPI = retrofit.create(PokeAPI.class);

        // 동기 인터페이스
        try {
            pokeAPI.listPokemons().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private class DataSource extends PageKeyedDataSource<String, Result> {

        // loadInitial 메서드를 구현해 처음으로 호출할 부분을 처리
        // params를 활용하여 loadInitial 메서드 내에서 자료를 얻은 후 그 자료와 키들을 callback, onResult로 전달하는 식
        // 데이터소스는 백그라운드에서 수행되기 때문에 별도의 비동기 인터페이스를 사용할 필요 X
        @Override
        public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, Result> callback) {
            try {
                // 동기식 인터페이스
                Response body = pokeAPI.listPokemons().execute().body();
                callback.onResult(body.results, body.previous, body.next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 기존의 pokeAPI.listPokemons()로 이전 페이지와 다음 페이지를 구현할 수 없기 때문애 레트로핏 인터페이스를 확장

        // 이전 페이지
        @Override
        public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Result> callback) {
            // pervious와 next 키가 주소?offset=숫자&limit=숫자 형태로 전달되어 오기 때문에 ?를 기준으로 잘라
            // ("\\?) 쿼리 부분만 얻음
            // "\\?"의 형태로 전달하는 것은 split 메서드가 ?를 와일드 카드로 해석해서 아무 문자열을 받아들이기 때문에
            // \?를 전달하는 것이고, 문자열 내에서 \를 표현하기 위해 \를 두 번 사용
            // 다시 "&"을 기준으로 쿼리를 하나하나 잘라내며, 다음으로 "="을 기준으로 잘라 키 밸류를 분리해서 사용
            // loadBefore와 loadAfter는 유사한데 before는 params로 이전 키(previous)를 받고 콜백에도 새 이전 키를 반환
            // after는 다음 키(next)를 받고 콜백에도 새 다음 키를 반환
            String queryPart = params.key.split("\\?")[1];
            String[] queries = queryPart.split("&");
            Map<String, String> map = new HashMap<>();
            for (String query : queries) {
                String[] splited = query.split("=");
                map.put(splited[0], splited[1]);
            }
            try {
                Response body = pokeAPI.listPokemons(map.get("offset"), map.get("limit")).execute().body();
                callback.onResult(body.results, body.previous);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 다음 페이지
        @Override
        public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Result> callback) {
            String queryPart = params.key.split("\\?")[1];
            String[] queries = queryPart.split("&");
            Map<String, String> map = new HashMap<>();
            for (String query : queries) {
                String[] splited = query.split("=");
                map.put(splited[0], splited[1]);
            }
            try {
                Response body = pokeAPI.listPokemons(map.get("offset"), map.get("limit")).execute().body();
                callback.onResult(body.results, body.next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}