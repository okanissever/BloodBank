package com.example.bloodaid.view.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.bloodaid.R
import com.example.bloodaid.databinding.FragmentProfileBinding
import com.example.bloodaid.util.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val args : ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userId = args.userId

        binding.loadingIndicator.visibility = View.VISIBLE
        binding.infoContainer.visibility = View.GONE
        binding.btnContact.visibility = View.GONE

        firestore.collection("users").document(userId).get().addOnSuccessListener {document->
            if(document!=null){
                val name = document.getString("name")
                val bloodType = document.getString("bloodType")
                val gender = document.getString("gender")
                val age =  document.getString("age")
                val address =  document.getString("address")

                if (gender == "Man") {
                    binding.imgProfile.setImageResource(R.drawable.man)
                } else if (gender == "Woman") {
                    binding.imgProfile.setImageResource(R.drawable.woman)
                }

                binding.tvName.text = name
                binding.tvBloodType.text = bloodType
                binding.tvGender.text = gender
                binding.tvAge.text = age
                binding.tvAdress.text = address

            }else {
                Log.d(TAG, "No such document")
            }
            binding.loadingIndicator.visibility = View.GONE
            binding.infoContainer.visibility = View.VISIBLE

        } .addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
            binding.loadingIndicator.visibility = View.GONE
            binding.infoContainer.visibility = View.VISIBLE
        }

    }

}