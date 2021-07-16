package com.task.user.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.user.R;
import com.task.user.databinding.ItemUserBinding;
import com.task.user.model.User;

import java.io.File;
import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private ArrayList<User> users;
    private Context context;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ItemUserBinding binding = ItemUserBinding.inflate(layoutInflater, viewGroup, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int i) {
        holder.bind(users.get(i), context);
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public void setUsers(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, Context context) {
            binding.setUser(user);
            binding.setFormat(context.getString(R.string.date_format_mdyhm));
            Glide.with(context).load(new File(user.getProfile())).into(binding.ivProfile);
            binding.executePendingBindings();
        }
    }
}