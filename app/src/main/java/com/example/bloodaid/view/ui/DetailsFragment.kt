package com.example.bloodaid.view.ui

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.bloodaid.R
import com.example.bloodaid.databinding.FragmentDetailsBinding
import com.example.bloodaid.util.viewBinding
import com.google.firebase.firestore.FirebaseFirestore


class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val args : DetailsFragmentArgs by navArgs()
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = args.donation

        binding.tvName.text = item.fullName
        binding.tvBloodType.text = item.bloodType
        binding.tvGender.text = item.gender
        binding.tvDescription.text = item.description

        binding.btnContact.setOnClickListener {
            firestore.collection("users").document(item.uuid).get().addOnSuccessListener {document->
                if(document!=null){
                    val phone = document.getString("phone")
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    startActivity(intent)

                }else {
                    Log.d(ContentValues.TAG, "No such document")
                }

            } .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

            }
        }

        if (item.gender == "Man") {
            binding.imgProfile.setImageResource(R.drawable.man)
        } else if (item.gender == "Woman") {
            binding.imgProfile.setImageResource(R.drawable.woman)
        }
    }


}