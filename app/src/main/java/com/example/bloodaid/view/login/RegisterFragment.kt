package com.example.bloodaid.view.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.bloodaid.R
import com.example.bloodaid.databinding.FragmentRegisterBinding
import com.example.bloodaid.model.UserProfile
import com.example.bloodaid.util.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.blood_type_options, // strings.xml dosyasındaki array
            android.R.layout.simple_spinner_item // Satır layout'u
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Açılır menü için layout
            binding.spinnerBloodGroups.adapter = adapter // Spinner'a adaptörü atama
        }


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_options, // strings.xml dosyasındaki array
            android.R.layout.simple_spinner_item // Satır layout'u
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Açılır menü için layout
            binding.spinnerGender.adapter = adapter // Spinner'a adaptörü atama
        }


        auth = Firebase.auth

        binding.btnContact.setOnClickListener {
            validateAndSaveUserData(it)
        }

    }


    private fun validateAndSaveUserData(view : View) {
        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val bloodType = binding.spinnerBloodGroups.selectedItem.toString()
        val gender = binding.spinnerGender.selectedItem.toString()

        if (name.isEmpty() || age.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty() || bloodType == "Seçiniz" || gender == "Seçiniz") {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Kullanıcı başarıyla oluşturuldu, Firestore'a kaydet
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val userId = firebaseUser.uid
                        val userProfile = UserProfile(name, bloodType, gender, age, phone, null, address)
                        saveUserProfileToFirestore(userId, userProfile, view)
                    } else {
                        // Kayıt işlemi başarısız
                        Log.d("Register Fragment","Kayıt işlemi başarısız: ${task.exception?.message}")
                    }
                }
        }
    }

    private fun saveUserProfileToFirestore(userId: String, userProfile: UserProfile,view : View) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Succesfull", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
            }
            .addOnFailureListener { e ->
                Log.d("Register Fragment","Registration failed: ${e}")
                Toast.makeText(requireContext(), "Firestore kayıt hatası: $e", Toast.LENGTH_LONG).show()
            }
    }



}
