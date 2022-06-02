package com.example.meet4sho;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.example.meet4sho.messages.MessageWrapper;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment the displays a chat-room for the user and from where the user can message other people
 *      (The main messaging screen from where you type in your message and receive the messages)
 */

public class MessageChatFragment extends Fragment {


    private String conversationID;
    private String recipient;
    private Bundle bundle;
    private MessagesListAdapter<IMessage> adapter;


    public MessageChatFragment() {
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
        View v = inflater.inflate(R.layout.message_chat_page, container, false);
        bundle = this.getArguments();
        conversationID = bundle.getString("Chat ID");
        recipient = bundle.getString("Recipient");

        /**
         * Set views so that the user can see what they're typing, the messages they've received,
         *      and send the text that they've typed
         */
        MessageInput inputView  = v.findViewById(R.id.input);
        MessagesList messagesList = v.findViewById(R.id.messagesList);
        ImageLoader imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                sendMessage(input.toString());
                return true;
            }
        });

        String senderId = CometChat.getLoggedInUser().getUid();
        adapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesList.setAdapter(adapter);

        String listenerID = "Listener 1";
        CometChat.addMessageListener(listenerID, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                addMessage(textMessage);
                Log.d("CometChat", "Text message received successfully: " + textMessage.toString());
            }
            @Override
            public void onMediaMessageReceived(MediaMessage mediaMessage) {
                Log.d("CometChat", "Media message received successfully: " + mediaMessage.toString());
            }
            @Override
            public void onCustomMessageReceived(CustomMessage customMessage) {
                Log.d("CometChat", "Custom message received successfully: " +customMessage.toString());
            }
        });

        /**
         * Load all previous messages that were in the chat so that the user may view them even if they've
         *      left the chat-room screen (Load chat-room history)
         */
        MessagesRequest messagesRequest = new MessagesRequest.MessagesRequestBuilder().setUID(recipient).build();
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                addMessages(baseMessages);
                Log.d("CometChat", "Old Messages Retrieved");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("CometChat", e.getMessage());
            }
        });

        return v;
    }

    /**
     * Helper method to display all the messages in the adapter
     */
    private void addMessages(List<BaseMessage> baseMessages) {
        List<IMessage> list = new ArrayList<>();
        for(BaseMessage message: baseMessages) {
            if(message instanceof TextMessage) {
                list.add(new MessageWrapper((TextMessage) message));
            }
        }
        adapter.addToEnd(list, true);
    }

    /**
     * Helper method to send the message to the other user
     */
    private void sendMessage(String message) {
        TextMessage textMessage = new TextMessage(recipient, message, CometChatConstants.RECEIVER_TYPE_USER);
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
            @Override
            public void onSuccess(TextMessage textMessage) {
                addMessage(textMessage);
                Log.d("CometChat", "Message sent successfully: " + textMessage.toString());
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("CometChat", "Message sending failed with exception: " + e.getMessage());
            }
        });
    }

    /**
     * Helper method to add a singular message to the adapter
     */
    private void addMessage(TextMessage textMessage) {
        adapter.addToStart(new MessageWrapper(textMessage), true);
    }

}