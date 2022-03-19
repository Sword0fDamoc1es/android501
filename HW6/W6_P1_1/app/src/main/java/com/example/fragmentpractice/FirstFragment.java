package com.example.fragmentpractice;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private onFirstFragmentListener mCallBack;

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
        Button button = v.findViewById(R.id.btn1);
        TextView tv = v.findViewById(R.id.shit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = tv.getText().toString();
                Log.w("firstButton",message);
                mCallBack.messageFromFirstFragment(message);
            }
        });
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