package spiral.bit.dev.qrscanner.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import spiral.bit.dev.qrscanner.Activities.AboutAppActivity;
import spiral.bit.dev.qrscanner.Activities.QrGenerateActivity;
import spiral.bit.dev.qrscanner.Activities.QrHistoryActivity;
import spiral.bit.dev.qrscanner.Activities.ScanQrCodeActivity;
import spiral.bit.dev.qrscanner.Adapters.QrGenerateRecyclerViewAdapter;
import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Other.CustomScrollGenerateListener;
import spiral.bit.dev.qrscanner.R;
import spiral.bit.dev.qrscanner.ViewModels.QrGenerateItemViewModel;

public class QrHistoryGenerateFragment extends Fragment {

    //MADE BY SPIRAL BIT DEVELOPMENT

    private RecyclerView qrRecycler;
    private QrGenerateRecyclerViewAdapter adapter;
    private QrGenerateItemViewModel qrItemViewModel;
    private TextView emptyLabelTextViewGenerate;
    private static BottomNavigationViewEx bnve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_qr_history_generate, container, false);
        setHasOptionsMenu(true);
        qrItemViewModel = new ViewModelProvider(this).get(QrGenerateItemViewModel.class);
        qrItemViewModel.getAllQrItems().observe(getViewLifecycleOwner(), new Observer<List<QrGenerateItem>>() {
            @Override
            public void onChanged(List<QrGenerateItem> qrItems) {
                emptyLabelTextViewGenerate = view.findViewById(R.id.empty_label_text_view);
                if (qrItemViewModel.getAllQrItems().getValue().isEmpty()) {
                    qrRecycler.setVisibility(View.GONE);
                    emptyLabelTextViewGenerate.setVisibility(View.VISIBLE);
                } else {
                    qrRecycler.setVisibility(View.VISIBLE);
                    emptyLabelTextViewGenerate.setVisibility(View.GONE);
                }
                adapter.submitList(qrItems);
            }
        });

        bnve = view.findViewById(R.id.bottom_nav_menu);
        bnve.setTextVisibility(true);
        bnve.setTextSize(12);
        bnve.setItemHeight(200);
        bnve.setIconSize(45f, 45f);
        bnve.setItemRippleColor(ColorStateList.valueOf(Color.YELLOW));
        bnve.enableItemShiftingMode(true);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(false);

        for (int i = 0; i < bnve.getItemCount(); i++) {
            bnve.setIconTintList(i, null);
        }

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.qr_code_scan:
                        Intent toAddIntent = new Intent(getContext().getApplicationContext(), ScanQrCodeActivity.class);
                        toAddIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(toAddIntent);
                        return true;
                    case R.id.qr_code_generate:
                        Intent toProfileIntent = new Intent(getContext().getApplicationContext(), QrGenerateActivity.class);
                        toProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(toProfileIntent);
                        return true;
                    case R.id.qr_code_history:
                        Intent toAddTimeTaskIntent = new Intent(getContext().getApplicationContext(), QrHistoryActivity.class);
                        toAddTimeTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(toAddTimeTaskIntent);
                        return true;
                    case R.id.qr_code_about:
                        Intent toToDoIntent = new Intent(getContext().getApplicationContext(), AboutAppActivity.class);
                        toToDoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(toToDoIntent);
                        return true;
                }
                return false;
            }
        });

        qrRecycler = view.findViewById(R.id.qr_recycler_view);
        adapter = new QrGenerateRecyclerViewAdapter();
        qrRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        qrRecycler.setAdapter(adapter);
        qrRecycler.addOnScrollListener(new CustomScrollGenerateListener());
        return view;
    }

    public static void hideBottomNav() {
        bnve.setVisibility(View.INVISIBLE);
    }

    public static void unHideBottomNav() {
        bnve.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.clear_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_history:
                qrItemViewModel.deleteAllQrItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}