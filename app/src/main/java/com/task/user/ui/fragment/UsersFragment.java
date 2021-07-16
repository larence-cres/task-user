package com.task.user.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.task.user.R;
import com.task.user.databinding.FragmentUsersBinding;
import com.task.user.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class UsersFragment extends Fragment {

    private SharedViewModel viewModel;
    private FragmentUsersBinding binding;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    private UsersAdapter adapter;
    private ArrayList<User> users;

    private Long toDate;
    private Long fromDate;
    private Calendar calendar;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, null, false, DataBindingUtil.getDefaultComponent());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        calendar = Calendar.getInstance();

        initView();
    }

    private void initView() {
        users = new ArrayList<>();
        fromDate = calendar.getTimeInMillis();
        toDate = calendar.getTimeInMillis();
        binding.setFragment(this);
        binding.setFrom(String.valueOf(fromDate));
        binding.setTo(String.valueOf(toDate));
        binding.setFormat(getString(R.string.date_format_ymd));
        initRecyclerView();
        getUsers();

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.getRoot());
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                binding.dimLayer.setVisibility(newState == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void getUsers() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), userList -> {
            users.addAll(userList);
            binding.setSize(users.size());
            adapter.setUsers(users, requireContext());
        });
    }

    private void initRecyclerView() {
        binding.rvProductList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProductList.setHasFixedSize(true);

        adapter = new UsersAdapter();
        binding.rvProductList.setAdapter(adapter);
    }

    public void showDatePickerDialog(Boolean isFrom) {
        int mYear, mMonth, mDay;
        calendar.setTimeInMillis(isFrom ? fromDate : toDate);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    final Calendar c = Calendar.getInstance();
                    if (isFrom) {
                        c.set(year, monthOfYear, dayOfMonth, 0, 0);
                        fromDate = c.getTimeInMillis();
                        binding.setFrom(fromDate.toString());
                    } else {
                        c.set(year, monthOfYear, dayOfMonth, 23, 59);
                        toDate = c.getTimeInMillis();
                        binding.setTo(toDate.toString());
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void filterData(String from, String to) {
        ArrayList<User> filteredUser = new ArrayList<>();
        initRecyclerView();
        if (from.isEmpty() && to.isEmpty()) {
            filteredUser = users;
        } else {
            for (User user : users) {
                if (user.getCreatedAt() > Long.parseLong(from) && user.getCreatedAt() < Long.parseLong(to))
                    filteredUser.add(user);
            }
        }
        adapter.setUsers(filteredUser, requireContext());
        binding.setSize(filteredUser.size());
        adapter.notifyDataSetChanged();
        collapseBottomSheet();
    }

    public void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void navigateToAdd() {
        findNavController(this).navigate(
                UsersFragmentDirections.actionUsersFragmentToAddFragment()
        );
    }
}
