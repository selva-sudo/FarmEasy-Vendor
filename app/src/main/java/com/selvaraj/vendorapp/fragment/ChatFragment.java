package com.selvaraj.vendorapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.selvaraj.vendorapp.Interface.ChatListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.activity.HomeActivity;
import com.selvaraj.vendorapp.adapter.MessageAdapter;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.base.BaseFragment;
import com.selvaraj.vendorapp.model.UserMessage;
import com.selvaraj.vendorapp.utils.FireBaseUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatFragment extends BaseFragment implements View.OnClickListener, ChatListener {

    private RecyclerView rvChat;
    private List<UserMessage> messageList = new ArrayList<>();
    private EditText etNewMessage;
    private MessageAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnSend = view.findViewById(R.id.btn_chatbox_send);
        rvChat = view.findViewById(R.id.rv_chat);
        etNewMessage = view.findViewById(R.id.et_chatbox);
        btnSend.setOnClickListener(this);
        initRecyclerView();
        getMessages();
    }

    private void getMessages() {
        BaseApplication.getInstance().getFireBaseUtils().getMessage(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvChat.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messageList);
        rvChat.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chatbox_send:
                String message = etNewMessage.getText().toString();
                if (!message.isEmpty()) {
                    ((HomeActivity) context).hideKeyboard();
                    String messageuser = new FireBaseUtils().firebaseUser.getDisplayName();
                    long msgTime = new Date().getTime();
                    FirebaseDatabase.getInstance().getReference("Users" + "/" + "Chats").push().setValue(new UserMessage(message, false, messageuser, msgTime));
                    messageList.add(new UserMessage(message, false, messageuser, msgTime));
                    adapter.updateDetails(messageList);
                    rvChat.getLayoutManager().scrollToPosition(messageList.size() - 1);
                    etNewMessage.setText("");
                }
                break;
        }
    }

    @Override
    public void onNewMessage(UserMessage message) {
        messageList.add(message);
        adapter.updateDetails(messageList);
    }
}
