package com.m2f.sliidetest.SliideTest.presentation.feature.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.m2f.sliidetest.SliideTest.R
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.databinding.RowUserBinding
import java.lang.ref.WeakReference

private val diffCallback = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id
}

class UsersAdaper(val menuListener: (User) -> Unit):
    ListAdapter<User, UsersAdaper.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    inner class ViewHolder(private val binding: RowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            binding.name.text = item.name
            binding.mail.text = item.email
            binding.identifier.text = item.id.toString()
            binding.gender.setImageResource(
                    when(item.gender) {
                        Gender.MALE -> R.drawable.ic_male
                        Gender.FEMALE -> R.drawable.ic_female
                    }
            )
            binding.root.setOnLongClickListener { menuListener(item); true }
        }
    }
}