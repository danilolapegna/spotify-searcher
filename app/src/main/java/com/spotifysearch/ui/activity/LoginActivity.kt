package com.spotifysearch.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotifysearch.R
import com.spotifysearch.SharedPreferences
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.button_login
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (sharedPreferences.isLoggedIn()) {
            goToSearch()
            finish()
        } else {
            setupView()
        }
    }

    private fun setupView() {
        button_login.setOnClickListener { authenticate() }
    }

    private fun authenticate() {
        AuthenticationClient.openLoginActivity(
                this, SPOTIFY_LOGIN_REQUEST,
                AuthenticationRequest.Builder(
                        getString(R.string.spotify_client_id),
                        AuthenticationResponse.Type.TOKEN, Uri.Builder()
                        .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                        .authority(getString(R.string.com_spotify_sdk_redirect_host))
                        .build().toString()
                )
                        .setShowDialog(true)
                        .setScopes(arrayOf(getString(R.string.spotify_scope)))
                        .setCampaign(getString(R.string.spotify_campaign_token))
                        .build()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (response.type == AuthenticationResponse.Type.ERROR || response.accessToken.isNullOrEmpty()) {
            Toast.makeText(
                    this,
                    "Error: ${response.error}",
                    Toast.LENGTH_LONG
            )
                    .show()
        } else {
            sharedPreferences.setAuthToken(response.accessToken)
            goToSearch()
        }
    }

    private fun goToSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
        finish()
    }

    companion object {
        const val SPOTIFY_LOGIN_REQUEST = 101
    }
}
