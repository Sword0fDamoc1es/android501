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

/**
 * Fragment that displays all the conversations/chat-rooms that a user is in
 */
public class MessagesListFragment extends Fragment {

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

    /**
     * Retrieve all the conversations/chat-rooms that a user is in via CometChat and display them
     *      on the RecyclerView
     */
    public void getConversationsList() {
        ConversationsRequest conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().build();
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                // Handle list of conversations
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