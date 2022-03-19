package com.example.fragmentpractice;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private onFirstFragmentListener mCallBack;

    MediaPlayer mp;

    private ListView lvMenu;


//    ArrayAdapter adapter;
    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first,container,false);

//        mp = MediaPlayer.create(getActivity(), R.raw.dog);
//
//        mp.start();
        lvMenu = v.findViewById(R.id.lvMenu);

        final String[] Animals = {"Dog", "Cow", "Chimp", "Lion","Rooster"};
        ArrayAdapter AnimalListAdapter = new ArrayAdapter<String>(getActivity(),           //Context
                android.R.layout.simple_list_item_1, //type of list (simple)
                Animals);

        lvMenu.setAdapter(AnimalListAdapter);

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Animal;
                //  Animal = Animals[position];  //Note, This is much simpler,
                //Q (for above): Why is referring to the original array less preferable then using CallBack Parms (below)? A: _____________
                Animal = String.valueOf(parent.getItemAtPosition(position));  //Parent refers to the parent of the item, the ListView.  position is the index of the item clicked.
                if (Animal == "Cow") {
                    mp = MediaPlayer.create(getActivity(), R.raw.cowmooing);
                    mp.start();
                }
                else if (Animal == "Dog")
                {
                    mp = MediaPlayer.create(getActivity(), R.raw.dog);
                    mp.start();
                }
                else if (Animal == "Chimp")
                {
                    mp = MediaPlayer.create(getActivity(), R.raw.monkey);
                    mp.start();
                }
                else if (Animal == "Lion")
                {
                    mp = MediaPlayer.create(getActivity(), R.raw.lion);
                    mp.start();
                }
                else if (Animal == "Rooster")
                {
                    mp = MediaPlayer.create(getActivity(), R.raw.rooster);
                    mp.start();
                }
                String message = Animal;
//                Log.w("firstButton",message);
                mCallBack.messageFromFirstFragment(message);


//                Toast.makeText(MainActivity.this, "You Clicked on "  + Animal, Toast.LENGTH_LONG).show();
            }
        });

//        Button button = v.findViewById(R.id.btn1);
//        TextView tv = v.findViewById(R.id.shit);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = tv.getText().toString();
//                Log.w("firstButton",message);
//                mCallBack.messageFromFirstFragment(message);
//            }
//        });

        return v;
    }
    public interface onFirstFragmentListener{
        void messageFromFirstFragment(String text);
    }
    public void onAttach(Context context) {

        super.onAttach(context);
        if(context instanceof onFirstFragmentListener){
            mCallBack = (onFirstFragmentListener) context;

        }else{
            throw new RuntimeException();
        }
    }
    public void onDetach(){
        super.onDetach();
        mCallBack=null;
    }
}