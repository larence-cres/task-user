package com.task.user.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.task.user.databinding.FragmentAddBinding;
import com.task.user.model.User;
import com.task.user.util.Uri2PathUtil;

import java.util.Calendar;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class AddFragment extends Fragment {

    private SharedViewModel viewModel;
    private FragmentAddBinding binding;

    private String profile;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        if (intent.getData() != null) {
                            profile = Uri2PathUtil.getRealPathFromUri(requireContext(), intent.getData());
                            Log.e("Path :", profile);
                            Glide.with(requireContext()).load(profile).into(binding.ivProfile);
                        }
                    }
                }
            });

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(getLayoutInflater());
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.executePendingBindings();
        initView();
    }

    private void initView() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            Log.e("User size :", users.size() + "");
        });
    }

    private String getName() {
        return binding.etName.getText() != null ? binding.etName.getText().toString() : "";
    }

    private String getEmail() {
        return binding.etEmail.getText() != null ? binding.etEmail.getText().toString() : "";
    }

    private String getPhone() {
        return binding.etPhone.getText() != null ? binding.etPhone.getText().toString() : "";
    }

    private String getGender() {
        return binding.rbMale.isChecked() ? "Male" : "Female";
    }

    public void imageChooser() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startForResult.launch(Intent.createChooser(intent, "Select Image"));
    }

    private String getProfile() {
        return profile;
    }

    public void addUser() {
        if (isValid()) {
            User user = viewModel.saveUser(
                    new User(
                            getName(),
                            getEmail(),
                            getPhone(),
                            getGender(),
                            getProfile(),
                            Calendar.getInstance().getTime().toString())
            );
            if (user != null) {
                Log.e("User :", user.toString());
            }
        }
    }

    private boolean isValid() {
        if (getName().isEmpty()) {
            binding.etName.setError("Name required");
            return false;
        } else if (getEmail().isEmpty()) {
            binding.etEmail.setError("Email required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            binding.etEmail.setError("Invalid email");
            return false;
        } else if (getPhone().isEmpty()) {
            binding.etPhone.setError("Phone required");
            return false;
        } else if (getProfile().isEmpty()) {
            binding.etPhone.setError("Profile image required");
            return false;
        } else {
            return true;
        }
    }

    public void navigateUp() {
        findNavController(this).navigateUp();
    }

}
