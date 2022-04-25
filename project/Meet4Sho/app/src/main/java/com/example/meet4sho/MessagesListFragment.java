package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;

import java.util.ArrayList;
import java.util.List;


public class MessagesListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View v;
    private ConversationsAdapter ca;
    private List<Conversation> conversationsList = new ArrayList<>();
    private RecyclerView conversationsRecyclerView;


    public MessagesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.messages_list_page, container, false);
        conversationsRecyclerView = (RecyclerView) v.findViewById(R.id.conversationsRecyclerView);

        getConversationsList();



        return v;
    }

    public void getConversationsList() {
        ConversationsRequest conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().build();

        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                // Hanlde list of conversations
                conversationsList = conversations;
                ca = new ConversationsAdapter(getActivity(), conversationsList, getActivity().getFragmentManager());

                conversationsRecyclerView.setAdapter(ca);
                conversationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("CometChat ", "Conversations Retrieved");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("ComeChat ", "Couldn't retrieve conversations");
            }
        });
    }

}