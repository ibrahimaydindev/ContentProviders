package com.example.contentproviders

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contentproviders.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        floatingActionButton = binding.floatingActionButton
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                1
            )

        }

        binding.floatingActionButton.setOnClickListener { view ->
            val contentResolver: ContentResolver = contentResolver
            val projection = ContactsContract.Contacts.DISPLAY_NAME
            val cursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(projection), null, null, ContactsContract.Contacts.DISPLAY_NAME
            )
            if (cursor != null) {
                val contactList = ArrayList<String>()
                val columnIx: String = ContactsContract.Contacts.DISPLAY_NAME
                while (cursor.moveToNext()) {
                    contactList.add(cursor.getString(cursor.getColumnIndex(columnIx)))
                }
                cursor.close()
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList)
                findViewById<ListView>(R.id.listView).adapter = adapter

            }else{
                Snackbar.make(view, "No Contacts Found", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", View.OnClickListener {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_CONTACTS),
                                1
                            )
                        }else{
                            val intent = Intent()
                            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = android.net.Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }).show()

            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }

    }
}