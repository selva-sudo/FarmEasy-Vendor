package com.selvaraj.vendorapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.selvaraj.vendorapp.Interface.DeleteListener;
import com.selvaraj.vendorapp.Interface.RecyclerViewItemClickListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.activity.HomeActivity;
import com.selvaraj.vendorapp.adapter.ProfileAdapter;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.base.BaseFragment;
import com.selvaraj.vendorapp.model.ProfileItems;
import com.selvaraj.vendorapp.model.SaveVendor;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, DeleteListener {

    private RecyclerView rvProfile;
    private TextView tvUserName, tvAboutUs;
    private CircleImageView civInfo;
    private Button btnLogout;
    private AlertDialog dialogFeedBack;
    private List<ProfileItems> profileItemsList = new ArrayList<>();
    private ProfileAdapter profileAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProfile = view.findViewById(R.id.rv_profile);
        tvUserName = view.findViewById(R.id.tv_profile_name);
        civInfo = view.findViewById(R.id.iv_info_alert);
        btnLogout = view.findViewById(R.id.btn_logout);
        tvAboutUs = view.findViewById(R.id.tv_about_us);
        tvAboutUs.setVisibility(View.INVISIBLE);
        initListeners();
        initRecyclerView();
        prepareList();
    }

    private void initListeners() {
        civInfo.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void prepareList() {
        profileItemsList.clear();
        SaveVendor user = BaseApplication.getInstance().getUserManager().getAuthUser();
        String date = "";
        if (user != null) {
            date = "  "+user.getDateOfJoining();
            tvUserName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        }
        profileItemsList.add(new ProfileItems(R.drawable.ic_edit_icon, getString(R.string.edit_profile)));
        profileItemsList.add(new ProfileItems(R.drawable.ic_date_icon, getString(R.string.from) + date));
        profileItemsList.add(new ProfileItems(R.drawable.ic_feedback_icon, getString(R.string.feedback)));
        profileItemsList.add(new ProfileItems(R.drawable.ic_share_icon, getString(R.string.invite)));
        profileItemsList.add(new ProfileItems(R.drawable.ic_lang_icon, getString(R.string.app_language)));
        profileItemsList.add(new ProfileItems(R.drawable.ic_delete_icon, getString(R.string.delete)));
        profileAdapter.updateDetails(profileItemsList);
    }

    //scroll to position
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvProfile.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvProfile.addItemDecoration(itemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rvProfile.setItemAnimator(itemAnimator);
        profileAdapter = new ProfileAdapter(profileItemsList, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        ((HomeActivity) context).loadFragment(EditProfileFragment.newInstance(), getString(R.string.edit));
                        break;
                    case 2:
                        getFeedBack();
                        break;
                    case 3:
                        createShareAlert();
                        break;
                    case 4:
                        ((HomeActivity) context).changeLanguageAlert();
                        break;
                    case 5:
                        showDeleteAlert();
                        break;
                }

            }
        });
        rvProfile.setAdapter(profileAdapter);
    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.MyDialog);
        builder1.setIcon(android.R.drawable.ic_dialog_alert);
        builder1.setTitle("Delete Account?");
        builder1.setMessage("Are you sure to delete your Account?");
        builder1.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseApplication.getInstance().getFireBaseUtils().deleteMyAccount(ProfileFragment.this);
            }
        }).setNegativeButton("Leave FeedBack", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getFeedBack();
            }
        });
        builder1.show();
    }

    private void createShareAlert() {

    }

    private void getFeedBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.feed_back_alert, null);
        builder.setIcon(R.drawable.ic_feedback_icon);
        builder.setView(view);
        Button btnSend, btnCancel;
        btnSend = view.findViewById(R.id.btn_send_feed_back);
        btnCancel = view.findViewById(R.id.btn_cancel_feed_back);
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        dialogFeedBack = builder.create();
        dialogFeedBack.setCanceledOnTouchOutside(false);
        dialogFeedBack.show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_info_alert:
                if (tvAboutUs.getVisibility() == View.INVISIBLE) {
                    tvAboutUs.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_about_us:
                showAboutUsAlert();
                break;
            case R.id.btn_logout:
                showLogoutAlert();
                break;
            case R.id.btn_send_feed_back:
                dialogFeedBack.dismiss();
                break;
            case R.id.btn_cancel_feed_back:
                dialogFeedBack.dismiss();
                break;
        }
    }

    private void showAboutUsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialog);
        builder.setIcon(R.drawable.ic_info_icon);
        builder.setTitle("About Farmer-Go");
        View view = getLayoutInflater().inflate(R.layout.about_us, null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tvAboutUs.getVisibility() == View.VISIBLE) {
                    tvAboutUs.setVisibility(View.INVISIBLE);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialog);
        builder.setIcon(R.drawable.ic_info_icon);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure to perform logout?!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((HomeActivity) context).logout();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onDeleteSuccess(boolean state) {
        if (state) {
            ((HomeActivity) context).logout();
        } else {
            Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show();
        }
    }
}
