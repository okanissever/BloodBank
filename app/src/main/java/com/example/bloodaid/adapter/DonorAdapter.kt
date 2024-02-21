package com.example.bloodaid.adapter

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodaid.R
import com.example.bloodaid.databinding.ItemDonorBinding
import com.example.bloodaid.model.Donation
import com.example.bloodaid.view.ui.HomeFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class DonorAdapter : ListAdapter<Donation,DonorAdapter.DonorHolder>(NoteDiffCallback){

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    class DonorHolder(val binding : ItemDonorBinding) : RecyclerView.ViewHolder(binding.root)

    object NoteDiffCallback : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorHolder {
        val binding = ItemDonorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DonorHolder(binding)
    }

    override fun onBindViewHolder(holder: DonorHolder, position: Int) {
        val item = currentList[position]

        if (item.gender == "Man") {
            holder.binding.imageView.setImageResource(R.drawable.man)
        } else if (item.gender == "Woman") {
            holder.binding.imageView.setImageResource(R.drawable.woman)
        }

        holder.binding.apply {
            title.text = item.fullName
            tvBloodType.text = item.bloodType
        }

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.profileImage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(item.uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.profileImage.visibility = View.INVISIBLE

        holder.binding.phoneImage.setOnClickListener {
            firestore.collection("users").document(item.uuid).get().addOnSuccessListener {document->
                if(document!=null){
                    val phone = document.getString("phone")
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    val context = holder.itemView.context
                    context.startActivity(intent)

                }else {
                    Log.d(ContentValues.TAG, "No such document")
                }

            } .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

            }
        }

    }

}