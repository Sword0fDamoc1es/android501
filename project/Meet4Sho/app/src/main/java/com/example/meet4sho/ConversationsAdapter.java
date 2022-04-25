package com.example.meet4sho;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.User;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    private List<Conversation> conversations;
    private Context context;
    private FragmentManager fm;

    public ConversationsAdapter(Activity ct, List<Conversation> converses, android.app.FragmentManager f) {
        this.context = ct;
        conversations = converses;
        fm = f;
    }
    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.conversation_list_row, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        Conversation chat = conversations.get(position);
        holder.conversationNameTextView.setText(((User)chat.getConversationWith()).getName());
        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageChatFragment chatRoom = new MessageChatFragment();
                Bundle bundle = new Bundle();
//                Intent i = new Intent(context, TM_EventInfoActivity.class);
                bundle.putString("Chat ID", chat.getConversationId());
                bundle.putString("Recipient", ((User)chat.getConversationWith()).getUid());


                chatRoom.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.displayedView, chatRoom);
                fragmentTransaction.addToBackStack("chatRoomFrag");
                fragmentTransaction.commit();

//                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() { return conversations.size(); }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView conversationNameTextView;
        LinearLayout containerLayout;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            conversationNameTextView = itemView.findViewById(R.id.conversationNameTextView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
        }

    }
}
