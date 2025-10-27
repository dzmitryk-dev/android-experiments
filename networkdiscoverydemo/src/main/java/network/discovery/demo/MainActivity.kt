package network.discovery.demo

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import network.discovery.demo.ui.screen.MainScreen
import network.discovery.demo.ui.theme.AndroidexperimentsTheme

class MainActivity : ComponentActivity() {

    private val nsdManager by lazy {
        getSystemService(Context.NSD_SERVICE) as NsdManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidexperimentsTheme {
                MainScreen()
            }
        }

        lifecycle.addObserver(nsdLifecycleObserver)
    }

    private val nsdLifecycleObserver = object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            startServiceDiscovery()
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            stopServiceDiscovery()
        }
    }

    private fun startServiceDiscovery() {
        nsdManager.discoverServices(
//            "_workstation._tcp.",
            "_ipps._tcp.",
            NsdManager.PROTOCOL_DNS_SD,
            nsdDiscoveryListener
        )
    }

    private fun stopServiceDiscovery() {
        nsdManager.stopServiceDiscovery(nsdDiscoveryListener)
    }

    private val nsdDiscoveryListener = object : NsdManager.DiscoveryListener {

        override fun onDiscoveryStarted(serviceType: String?) {
            Log.d("DiscoveryListener", "Service discovery started $serviceType")
        }

        override fun onDiscoveryStopped(serviceType: String?) {
            Log.d("DiscoveryListener", "Service discovery stopped $serviceType")
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
            Log.d("DiscoveryListener", "Service discovery found $serviceInfo")
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
            Log.d("DiscoveryListener", "Service discovery lost $serviceInfo")
        }

        override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.e("DiscoveryListener", "Service discovery start failed $serviceType with error code $errorCode")
        }

        override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.e("DiscoveryListener", "Service discovery stop failed $serviceType with error code $errorCode")
        }

    }
}


