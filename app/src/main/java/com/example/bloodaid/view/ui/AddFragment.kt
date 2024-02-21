package com.example.bloodaid.view.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bloodaid.R
import com.example.bloodaid.databinding.FragmentAddBinding
import com.example.bloodaid.databinding.FragmentRegisterBinding
import com.example.bloodaid.model.Donation
import com.example.bloodaid.util.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class AddFragment : Fragment(R.layout.fragment_add) {
    private val binding by viewBinding(FragmentAddBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.blood_type_options, // strings.xml dosyasındaki array
            android.R.layout.simple_spinner_item // Satır layout'u
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Açılır menü için layout
            binding.spinnerBloodType.adapter = adapter // Spinner'a adaptörü atama
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.type_options, // strings.xml dosyasındaki array
            android.R.layout.simple_spinner_item // Satır layout'u
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Açılır menü için layout
            binding.spinnerType.adapter = adapter // Spinner'a adaptörü atama
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_options, // strings.xml dosyasındaki array
            android.R.layout.simple_spinner_item // Satır layout'u
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Açılır menü için layout
            binding.spinnerGender.adapter = adapter // Spinner'a adaptörü atama
        }

        binding.btnConfirm.setOnClickListener {
            saveDataToFirebase()
        }

    }

    private fun saveDataToFirebase() {
        val fullName = binding.etCardholderName.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem.toString()
        val type = binding.spinnerType.selectedItem.toString()
        val bloodType = binding.spinnerBloodType.selectedItem.toString()
        val description = binding.etDescription.text.toString().trim()
        val uuid = auth.uid.toString()

        val donation = Donation(fullName, gender, type, bloodType, description, uuid)

        if (fullName.isEmpty() || gender.isEmpty() || description.isEmpty() ||  type == "Seçiniz" || bloodType == "Seçiniz" || gender == "Seçiniz") {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT).show()
        }
        else{
            val db = FirebaseFirestore.getInstance()
            db.collection("donations")
                .add(donation)
                .addOnSuccessListener { documentReference ->
                    Log.d("AddFragment", "DocumentSnapshot added with ID: ${documentReference.id}")
                    findNavController().navigate(R.id.action_addFragment_to_homeFragment)
                }
                .addOnFailureListener { e ->
                    Log.w("AddFragment", "Error adding document", e)
                    Toast.makeText(context, "Error adding document", Toast.LENGTH_SHORT).show()
                }
        }

    }

}