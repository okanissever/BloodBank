package com.example.bloodaid.view.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodaid.R
import com.example.bloodaid.adapter.DonorAdapter
import com.example.bloodaid.databinding.FragmentBloodRecipientBinding
import com.example.bloodaid.databinding.FragmentDonorBinding
import com.example.bloodaid.model.Donation
import com.example.bloodaid.util.viewBinding
import com.google.firebase.firestore.FirebaseFirestore


class BloodRecipientFragment : Fragment(R.layout.fragment_blood_recipient) {
    private val binding by viewBinding(FragmentBloodRecipientBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDonation()
    }

    private fun loadDonation() {
        val db = FirebaseFirestore.getInstance()
        val donationList = mutableListOf<Donation>()

        db.collection("donations").whereEqualTo("type", "Need For Blood")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val donation = document.toObject(Donation::class.java)
                    donationList.add(donation)
                }
                setupRecyclerView(donationList)
            }
            .addOnFailureListener { exception ->
                Log.w("BloodRecipientFragment", "Error getting documents: ", exception)
            }
    }

    private fun setupRecyclerView(donationList: List<Donation>) {
        val adapter = DonorAdapter()
        adapter.submitList(donationList)
        binding.rvBlood.layoutManager = LinearLayoutManager(context)
        binding.rvBlood.adapter = adapter
    }

}