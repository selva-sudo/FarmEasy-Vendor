package com.selvaraj.vendorapp.utils;

import android.util.ArraySet;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selvaraj.vendorapp.Interface.ChatListener;
import com.selvaraj.vendorapp.Interface.DeleteListener;
import com.selvaraj.vendorapp.Interface.GetCartListener;
import com.selvaraj.vendorapp.Interface.GetProductListener;
import com.selvaraj.vendorapp.Interface.GetUserNameListener;
import com.selvaraj.vendorapp.Interface.LoginListener;
import com.selvaraj.vendorapp.activity.LoginActivity;
import com.selvaraj.vendorapp.activity.SignUpActivity;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.model.AddProduct;
import com.selvaraj.vendorapp.model.Cart;
import com.selvaraj.vendorapp.model.SaveVendor;
import com.selvaraj.vendorapp.model.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FireBaseUtils {
    public FirebaseUser firebaseUser;
    public DatabaseReference chatReference;
    public String userID;
    protected List<String> userIdList = new ArrayList<>();
    protected SaveVendor user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference cartReference;

    public FireBaseUtils() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        databaseReference = firebaseDatabase.getReference("Users" + "/" + userID);
        chatReference = firebaseDatabase.getReference("Users" + "/" + "Chats" + "/");
        cartReference = firebaseDatabase.getReference("Users" + "/" + "Carts" + "/");
    }

    public void getUserList() {
        databaseReference = firebaseDatabase.getReference("Users" + "/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (entry.getKey().equals("Chats")) {
                            continue;
                        }
                        String user = entry.getKey();
                        userIdList.add(user);
                    }
                }
                BaseApplication.getInstance().getUserManager().setUserList(userIdList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAuthUserName(final GetUserNameListener listener) {
        databaseReference = firebaseDatabase.getReference("Users" + "/" + userID + "/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SaveVendor user = dataSnapshot.getValue(SaveVendor.class);
                if (user != null) {
                    BaseApplication.getInstance().getUserManager().setAuthUser(user);
                    listener.onSuccess(user.getFirstName() + " " + user.getLastName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getDetailsFromFireBase(final GetProductListener listener) {
        List<String> userList = BaseApplication.getInstance().getUserManager().getUserList();
        final List<AddProduct> productList = new ArrayList<>();
        if (userList != null) {
            for (int i = 0; i < userList.size(); i++) {
                String userId = userList.get(i);
                databaseReference = firebaseDatabase.getReference("Users" + "/" + userId + "/" + "Products");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AddProduct product = snapshot.getValue(AddProduct.class);
                            productList.add(product);
                        }
                        listener.onSuccess(productList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public void getCartList(final GetCartListener listener) {
        cartReference = firebaseDatabase.getReference("Users" + "/" + userID + "/" + "Cart" + "/");
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<Cart> cartList = new ArraySet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    if (cart == null) {
                        continue;
                    }
                    cartList.add(cart);
                }
                listener.onSuccess(cartList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getMessage(final ChatListener listener) {
        databaseReference = firebaseDatabase.getReference("Users" + "/" + "Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserMessage message = snapshot.getValue(UserMessage.class);
                    if (message != null && message.getMessage() != null && message.isUser()) {
                        listener.onNewMessage(message);
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users" + "/" + "Chats").removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkLogin(final LoginListener listener, String email, String password, LoginActivity context) {
        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onLoginSuccess(true);
                        } else {
                            listener.onLoginSuccess(false);
                        }
                    }
                });
    }

    public void createAccount(final SaveVendor user, final LoginListener listener, String password, SignUpActivity context) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users" + "/" + userId);
                            reference.setValue(user);
                            listener.onLoginSuccess(true);
                        } else {
                            listener.onLoginSuccess(false);
                        }
                    }
                });
    }

    public void deleteMyAccount(final DeleteListener listener) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = BaseApplication.getInstance().getUserManager().getUserEmail();
        String password = BaseApplication.getInstance().getUserManager().getUserPassword();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onDeleteSuccess(true);
                                            } else {
                                                listener.onDeleteSuccess(false);
                                            }
                                        }
                                    });

                        }
                    });
        }
    }
}
