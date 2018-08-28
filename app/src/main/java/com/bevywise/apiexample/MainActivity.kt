package com.bevywise.apiexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bevywise.bevywiseplatform_connector.BevywisePlatformConnector
import com.bevywise.bevywiseplatform_connector.DevicePermissions
import com.bevywise.bevywiseplatform_connector.PlatformConfiguration
import com.bevywise.bevywiseplatform_connector.listeners.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var bevyWiseConnector: BevywisePlatformConnector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bevyWiseConnector =  BevywisePlatformConnector.getInstance(PlatformConfiguration(
                "http://192.168.1.10:9486",
                "demo@bevywise.com",
                "pwd123",
                "Xq5Pff7FqHTLPNkkoEKQuCH8VjnPOe6dtGCUY64O",
                "WWqBIPN2ohhng7Srnpsvs1qIpioEgcL0P4m0qGDBPhyktw8Of1YZUQ1Yi8JGQPaqVHnpCie9pdBEEhLopBbtOzJcTv7flJWR4bfVFSMzLPYZUQgte9q0vGM9vhtFbLf8"),this)

//        loginTest()
//        logoutTest("9PNfkCkyTM6RadzULm4pFZmNp3aTwV");
//        signupTest("princenew","prince")
//        deviceAuthKeyTest("i7X8e2d6B85SDEECWTo9i6a119qwyL");
//        getDeviceListTest("i7X8e2d6B85SDEECWTo9i6a119qwyL", 0)
//        editDeviceNameTest("","","i7X8e2d6B85SDEECWTo9i6a119qwyL");

    }

    private fun notificationTest(pushToken: String, token: String) {
        bevyWiseConnector?.setNotificationToken(object:PlatformResponseListener {
            override fun onSuccess() {
                Log.d("Bevywise-Result", "Notification token Success")
            }

            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Notification token Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

        },pushToken,token)
    }

    private fun editDeviceNameTest(deviceId: String, deviceName: String, token: String) {
        bevyWiseConnector?.editDeviceName(object: PlatformResponseListener {
            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Edit DeviceName Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

            override fun onSuccess() {
                Log.d("Bevywise-Result", "Edit DeviceName Success")
            }

        }, deviceId, deviceName, token)
    }

    private fun getDeviceListTest(token: String, pageNo: Long) {
        bevyWiseConnector?.getDeviceList(object: DeviceListListener {
            override fun onGetDeviceListSuccess(deviceList: JSONArray?, nextPage: Boolean?, pageNo: Long) {
                Log.d("Bevywise-Result", "Get deviceList Success")
            }

            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Get deviceList Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

        },pageNo,token)
    }

    private fun deviceAuthKeyTest(token: String) {
        bevyWiseConnector?.getDeviceAuthKey(object: DeviceAuthKeyListener {
            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "DeviceAuthKey Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

            override fun onAuthKeyGenerated(datas: JSONObject?) {
                Log.d("Bevywise-Result", "Authkey generate Success")
            }

        },DevicePermissions.READ,"This is test description",token)
    }

    private fun logoutTest(token: String) {
        bevyWiseConnector?.logout(object : PlatformResponseListener {
            override fun onSuccess() {
                Log.d("Bevywise-Result", "Logout Success")
            }

            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Logout Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }
        },token)
    }

    private fun loginTest() {

        bevyWiseConnector?.login(object : LoginListener {
            override fun onLoginSuccess(token: String, refreshToken: String, expiresIn: Long) {
                Log.d("Bevywise-Result", token)
            }

            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Login Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

        })
    }

    private fun signupTest(username: String, password: String) {
        bevyWiseConnector?.signup(object : SignupListener {
            override fun onFailure(reasonForFailure: String?) {
                Log.d("Bevywise-Result", "Signup Failed")
                Log.d("Bevywise-Result", reasonForFailure)
            }

            override fun onSignupSuccess(token: String, refreshToken: String, expiresIn: Long) {
                Log.d("Bevywise-Result", token)
            }
        },username,password)
    }
}
