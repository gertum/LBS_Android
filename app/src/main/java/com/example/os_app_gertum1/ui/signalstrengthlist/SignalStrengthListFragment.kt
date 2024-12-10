package com.example.os_app_gertum1.ui.signalstrengthlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.network.ApiService
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository
import com.example.os_app_gertum1.ui.addsignalstrength.AddSignalStrengthActivity
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModel
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.concurrent.TimeUnit

class SignalStrengthListFragment : Fragment() {

    private lateinit var viewModel: SignalStrengthViewModel
    private lateinit var adapter: SignalStrengthAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signal_strength_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the base URL from strings.xml
        val baseUrl = getString(R.string.base_path)

        // Configure OkHttpClient with custom timeouts and SSL disabled
        val okHttpClient = getUnsafeOkHttpClient()

        // Initialize Retrofit with OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)  // Use the OkHttpClient with SSL verification disabled
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create the API service
        val apiService = retrofit.create(ApiService::class.java)

        // Initialize database, DAO, repository, and ViewModel
        val database = AppDatabase.getDatabase(requireContext())
        val repository = SignalStrengthRepository(apiService, database.signalStrengthDao())
        val factory = SignalStrengthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SignalStrengthViewModel::class.java)

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_signal_strength)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list
        adapter = SignalStrengthAdapter(emptyList())
        recyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        viewModel.allSignals.observe(viewLifecycleOwner) { signals ->
            // Update the adapter with the new list
            adapter.updateData(signals)
        }

        // Refresh the signal strengths from the API
        viewModel.refreshSignalStrengths()

        // Handle the FloatingActionButton click event
        val fabAddSignal = view.findViewById<FloatingActionButton>(R.id.fab_add_signal)
        fabAddSignal.setOnClickListener {
            // Navigate to Add Signal activity
            val intent = Intent(requireContext(), AddSignalStrengthActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to create OkHttpClient with SSL verification disabled
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        // Create a trust manager that does not perform certificate validation
        val trustAllCertificates = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }

        // Install the all-trusting trust manager
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustAllCertificates), SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to create SSL context", e)
        } catch (e: KeyManagementException) {
            throw RuntimeException("Failed to initialize SSL context", e)
        }

        // Set up OkHttpClient to use the custom SSLContext and disable hostname verification
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates)
            .hostnameVerifier { _, _ -> true }  // Disable hostname verification
            .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
            .build()
    }
}
