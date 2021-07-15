package com.task.user.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.task.user.databinding.FragmentUsersBinding;
import com.task.user.model.User;

import java.util.ArrayList;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class UsersFragment extends Fragment {

    private SharedViewModel viewModel;
    private FragmentUsersBinding binding;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    private UsersAdapter adapter;
    private ArrayList<User> users;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        initView();
    }

    private void initView() {
        binding.setFragment(this);
        initRecyclerView();
        viewModel.getUsers().observe(getViewLifecycleOwner(), userList -> {
            users.addAll(userList);
            Log.e("Name :", users.get(0).getName().toString());
            Log.e("Date :", users.get(0).getCreatedAt().toString());
            binding.setFrom(users.get(0).getCreatedAt().toString());
            binding.setSize(users.size());
            adapter.setUsers(users, requireContext());
        });

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.getRoot());
    }

    private void initRecyclerView() {
        users = new ArrayList<>();
        binding.rvProductList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProductList.setHasFixedSize(true);

        adapter = new UsersAdapter();
        binding.rvProductList.setAdapter(adapter);
    }

    public void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void navigateToAdd() {
        findNavController(this).navigate(
                UsersFragmentDirections.actionUsersFragmentToAddFragment()
        );
    }
}
