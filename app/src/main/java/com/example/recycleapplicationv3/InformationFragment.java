package com.example.recycleapplicationv3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView, recyclerView2;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;


    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);
        recyclerView = rootView.findViewById(R.id.idTutorialItems);
        String[] tutPic = {"https://img.youtube.com/vi/ayBv8GHeh0U/0.jpg", "https://img.youtube.com/vi/b7GMpjx2jDQ/0.jpg", "https://img.youtube.com/vi/Yw-U8AWtqZw/0.jpg", "https://img.youtube.com/vi/14dW1chYAZc/0.jpg"};
        String[] tutName = {"Recycle Right", "How Recycling Works", "Tips for Recycling", "How to recycle batteries"};
        String[] tutUrl = {"https://www.youtube.com/watch?v=ayBv8GHeh0U", "https://www.youtube.com/watch?v=b7GMpjx2jDQ", "https://www.youtube.com/watch?v=Yw-U8AWtqZw", "https://www.youtube.com/watch?v=14dW1chYAZc"};


        mainModels = new ArrayList<>();
        for (int i = 0; i < tutPic.length; i++) {
            MainModel model = new MainModel(tutPic[i], tutName[i], tutUrl[i]);
            mainModels.add(model);

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false

        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainAdapter = new MainAdapter(getContext(), mainModels);
        recyclerView.setAdapter(mainAdapter);

        recyclerView2 = rootView.findViewById(R.id.idTutorialItems2);
        tutPic = new String[]{"https://cdn.wm.com/content/dam/wm/assets/recycle-right/recycling-101/curbside-recycling-bin-man.jpg/jcr:content/renditions/rendition.xl.jpeg", "https://www.pennwaste.com/wp-content/uploads/IMG_20150213_121626286.jpg", "https://how2recycle.info/assets/uploads/featured/_newsSingleFullSize/How2RecycleLogowith-words-horizontal-6.jpeg", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/EPA_logo.svg/1280px-EPA_logo.svg.png"};
        tutName = new String[]{"Recyclable Materials", "Recycling Routine", "Dos and donts of recycling", "My recycling life", "Separating waste and recycling"};
        tutUrl = new String[]{"https://www.wm.com/us/en/recycle-right/recycling-101", "https://www.pennwaste.com/recycling/why-recycle/", "https://how2recycle.info/", "https://www.epa.gov/recycle/how-do-i-recycle-common-recyclables"};


        mainModels = new ArrayList<>();
        for (int i = 0; i < tutPic.length; i++) {
            MainModel model = new MainModel(tutPic[i], tutName[i], tutUrl[i]);
            mainModels.add(model);

        }
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false

        );
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        mainAdapter = new MainAdapter(getContext(), mainModels);
        recyclerView2.setAdapter(mainAdapter);
        
        return rootView;
    }
}