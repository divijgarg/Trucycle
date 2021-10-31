package com.example.recycleapplicationv3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements CategoryRVAdapter.CategorClickInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    View rootView;
    int RC_SIGN_IN=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //01ede4fcb22d43db87ac42aa4fc6faed

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SigninFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        // Inflate the layout for this fragment
        newsRV = rootView.findViewById(R.id.idRVNews);
        categoryRV = rootView.findViewById(R.id.idRVCategories);
        loadingPB = rootView.findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, getContext());
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, getContext(), this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

        return rootView;
    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All", "https://images.unsplash.com/photo-1604187351574-c75ca79f5807?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1050&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("Recycling", "https://images.unsplash.com/photo-1582408921715-18e7806365c1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("Environment", "https://images.unsplash.com/photo-1416169607655-0c2b3ce2e1cc?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1934&q=80"));
        categoryRVModalArrayList.add(new CategoryRVModal("Composting", "https://images.unsplash.com/photo-1621460248137-1656be874a8b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2134&q=80"));

        categoryRVAdapter.notifyDataSetChanged();

    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String urlContent = "";
        switch (category){
            case "All":
                urlContent = "recycle OR recycling OR upcycling OR landfill OR EPA OR pollution OR environmental OR environment OR compost OR composting OR composted";
            case "Recycling":
                urlContent = "recycle OR recycling OR upcycling OR landfill";
                break;
            case "Environment":
                urlContent = "EPA OR pollution OR environmental OR environment";
                break;
            case "Composting":
                urlContent = "compost OR composting OR composted";

        }
        LocalDateTime myDateObj = LocalDateTime.now();
        myDateObj = myDateObj.minusDays(2);

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        String formattedDate = myDateObj.format(myFormatObj);


        String categoryURL = "https://newsapi.org/v2/everything?qInTitle=" + urlContent + "&from=" + formattedDate + "&sortBy=popularity&apiKey=01ede4fcb22d43db87ac42aa4fc6faed";
        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=01ede4fcb22d43db87ac42aa4fc6faed";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if (category.equals("All")){
            call = retrofitAPI.getNewsbyCategory(categoryURL);
        }
        else{
            call = retrofitAPI.getNewsbyCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                    newsRVAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}