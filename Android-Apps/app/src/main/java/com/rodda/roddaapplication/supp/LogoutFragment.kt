package com.rodda.roddaapplication.supp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.rodda.roddaapplication.databinding.FragmentLogoutBinding
import com.rodda.roddaapplication.ui.um.login.LoginActivity

class LogoutFragment : DialogFragment() {

    private lateinit var binding : FragmentLogoutBinding
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentLogoutBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fAuth = FirebaseAuth.getInstance()
        val loading = LoadingDialog(requireActivity())

        binding.tvNo.setOnClickListener{
            dismiss()
        }
        binding.tvYes.setOnClickListener{
            loading.startDialog()
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
                loading.dismissDialog()
                fAuth.signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
                Toast.makeText(activity, "Log Out Success", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
    }
}