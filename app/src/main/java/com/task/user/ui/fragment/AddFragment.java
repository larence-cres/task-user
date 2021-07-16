package com.task.user.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.task.user.R;
import com.task.user.databinding.FragmentAddBinding;
import com.task.user.model.OnTransactionCallback;
import com.task.user.model.User;
import com.task.user.util.Uri2PathUtil;

import java.io.File;
import java.util.Calendar;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class AddFragment extends Fragment {

    private SharedViewModel viewModel;
    private FragmentAddBinding binding;

    private String profile = "";

    final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        if (intent.getData() != null) {
                            profile = Uri2PathUtil.getRealPathFromUri(requireContext(), intent.getData());
                            File file = new File(profile);
                            Glide.with(requireContext()).load(file).into(binding.ivProfile);
                        }
                    }
                }
            });

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, null, false, DataBindingUtil.getDefaultComponent());
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
            viewModel.saveUser(
                    new User(
                            getName(),
                            getEmail(),
                            getPhone(),
                            getGender(),
                            getProfile(),
                            Calendar.getInstance().getTimeInMillis()),
                    new OnTransactionCallback() {
                        @Override
                        public void onRealmSuccess() {
                            navigateUp();
                        }

                        @Override
                        public void onRealmError() {
                            Toast.makeText(requireContext(), "Error while creating user", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
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
        } else if (getPhone().length() < 10) {
            binding.etPhone.setError("Invalid phone number");
            return false;
        } else if (getProfile().isEmpty()) {
            Toast.makeText(requireContext(), "Profile image required", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void navigateUp() {
        findNavController(this).navigateUp();
    }

}
