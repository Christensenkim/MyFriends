package com.example.myfriends

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.Manifest;
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.myfriends.data.PersonRepositoryInDB
import com.example.myfriends.models.BEPerson
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailsActivity : AppCompatActivity(){

    val TAG = "xyz"
    val PERMISSION_REQUEST_CODE_MESSAGE = 1
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    var newPerson = true;
    var mFile: File? = null
    var id = 0
    var newSetBirthday = ""
    var mRep = PersonRepositoryInDB.get()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val personName: EditText = findViewById(R.id.personName)
        val phoneNumber: EditText = findViewById(R.id.phoneNumber)
        val address: EditText = findViewById(R.id.address)
        val mailAddress: EditText = findViewById(R.id.mailAddress)
        val website: EditText = findViewById(R.id.website)

        val birthdayPicker: DatePicker = findViewById(R.id.birthday)
        var birthday = LocalDate.now()

        if (intent.extras != null) {
            val save: Button = findViewById(R.id.save)
            save.setText("Update")
            newPerson = false
            val extras: Bundle = intent.extras!!
            val friend = extras["friend"] as BEPerson

            birthday = LocalDate.parse(friend.birthday, DateTimeFormatter.ISO_DATE)

            id = friend.id
            personName.setText(friend.name)
            phoneNumber.setText(friend.phone)
            address.setText(friend.address)
            mailAddress.setText(friend.mailAddress)
            website.setText(friend.website)
            userPicture.setImageURI(friend.picture.toUri())

        }

        birthdayPicker.init(birthday.year, birthday.monthValue-1, birthday.dayOfMonth, DatePicker.OnDateChangedListener {
            _, _year, _month, _day ->

            val setYear = _year.toString()
            var setMonth = (_month+1).toString()
            var setDay = _day.toString()
            if (_month+1 < 10){
                setMonth = "0${_month+1}"
            }
            if (_day < 10){
                setDay = "0$_day"
            }

            newSetBirthday = "$setYear-$setMonth-$setDay"

            //Toast.makeText(this, "Date changed to: $newSetBirthday ", Toast.LENGTH_SHORT).show()
        })

        checkPermissions()
    }

    fun saveFriend(view: View) {
        val personName: EditText = findViewById(R.id.personName)
        val phoneNumber: EditText = findViewById(R.id.phoneNumber)
        val address: EditText = findViewById(R.id.address)
        val mailAddress: EditText = findViewById(R.id.mailAddress)
        val website: EditText = findViewById(R.id.website)
        val birthday: DatePicker = findViewById(R.id.birthday)
        val picture = Uri.fromFile(mFile).toString()

        val newName = personName.text.toString()
        val newPhone = phoneNumber.text.toString()
        val newAddress = address.text.toString()
        val newMailaddress = mailAddress.text.toString()
        val newWebsite = website.text.toString()
        val newBirthday = newSetBirthday
        val picture = ""

        val friend = BEPerson(id, newName, newAddress, newPhone, newMailaddress, newWebsite, newBirthday, picture)

        if (newPerson)
        {
            saveNewfriend(friend)
        } else
        {
            updateFriend(friend)
        }

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun saveNewfriend(person: BEPerson)
    {
        mRep.insert(person)

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun updateFriend(person: BEPerson)
    {
        mRep.update(person)

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun call(view: View) {
        val PHONE_NO = findViewById<EditText>(R.id.phoneNumber).text.toString()

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$PHONE_NO")
        startActivity(intent)
    }

    //region Message

    fun message(view: View) {
        showYesNoDialog()
    }

    private fun showYesNoDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("SMS Handling")
        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct") { dialog, id -> sendSMSDirectly() }
                .setNegativeButton("Start", { dialog, id -> startSMSActivity() })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun startSMSActivity() {
        val PHONE_NO = findViewById<EditText>(R.id.phoneNumber).text.toString()
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:$PHONE_NO")
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }

    private fun sendSMSDirectly() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
                .show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "permission denied to SEND_SMS - requesting it")
                val permissions = arrayOf(Manifest.permission.SEND_SMS)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
                return
            } else Log.d(TAG, "permission to SEND_SMS granted!")
        } else Log.d(TAG, "Runtime permission not needed")
        sendMessage()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val PHONE_NO = findViewById<EditText>(R.id.phoneNumber).text.toString()
        val m = SmsManager.getDefault()
        val text = "Hi, it goes well on the android course..."
        m.sendTextMessage(PHONE_NO, null, text, null, null)
    }

    //endregion

    //region TakeAPicture

    fun takeAPicture(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.example.myfriends"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                "${applicationId}.provider",
                mFile!!))

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } else Log.d(TAG, "camera app could NOT be started")
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
            ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


    private fun getOutputMediaFile(folder: String): File? {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageButton>(R.id.UserPicture)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, mFile!!)
                else handleOther(resultCode)
        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    private fun showImageFromFile(img: ImageButton, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
    }

    //endregion


}