package com.weera.kplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        var url by remember {
          mutableStateOf(
            "https://www.dropbox.com//scl/fi/9shxjqhgusrdtq9uj7k8i/Main_Luxdo_P_29_02_672.txt?rlkey=hd4vh2t8qxzad6gv3q710euk7&dl=1"
          )
        }
        var status by remember { mutableStateOf("Ready") }
        Scaffold(topBar = { TopAppBar(title = { Text("KPlay") }) }) { pad ->
          Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("Playlist URL") })
            Button(onClick = {
              status = "Fetching..."
              try {
                val client = OkHttpClient()
                val req = Request.Builder().url(url).build()
                client.newCall(req).execute().use { resp ->
                  status = if (resp.isSuccessful) "Loaded ${(resp.body?.string()?.length ?: 0)} chars" else "HTTP ${resp.code}"
                }
              } catch (e: Exception) { status = "Error: ${e.message}" }
            }) { Text("Load playlist") }
            Text(status)
          }
        }
      }
    }
  }
}
