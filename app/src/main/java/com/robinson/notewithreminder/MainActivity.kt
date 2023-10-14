package com.robinson.notewithreminder

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.robinson.notewithreminder.adapter.MyNotesAdapter
import com.robinson.notewithreminder.adapter.MyNotesAdapter.MyItemClickListener
import com.robinson.notewithreminder.database.DBManager
import com.robinson.notewithreminder.databinding.ActivityMainBinding
import com.robinson.notewithreminder.model.NoteItemModel
import com.robinson.notewithreminder.screen.NoteUpdateActivity
import com.robinson.notewithreminder.screen.WebViewActivity
import com.robinson.notewithreminder.utilities.CommonParameters

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbManager: DBManager

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DBManager(this)
        getDatabaseInfo(1, "ignore")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val sv = menu.findItem(R.id.menu_search).actionView as SearchView?
        val sm = getSystemService(SEARCH_SERVICE) as SearchManager
        sv!!.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getDatabaseInfo(2, query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getDatabaseInfo(2, newText)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_help) {
            val info = AlertDialog.Builder(this)
            info.setMessage("Project started on 14-10-2023\nby Robinson Macwan\nFEATURES:\nADD, DELETE, UPDATE and SEARCH Notes along with setting REMINDERs at any date and time.")
                .setTitle("Information")
                .setPositiveButton("Source Code") { dialog, which ->
                    val webintent = Intent(applicationContext, WebViewActivity::class.java)
                    startActivity(webintent)
                    //                                Toast.makeText(getApplicationContext(),"Hi!",Toast.LENGTH_SHORT).show();
                }
                .show()
        } else if (id == R.id.menu_settings) {
            Toast.makeText(
                applicationContext,
                "Settings is currently under development!",
                Toast.LENGTH_SHORT
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private var noteList = ArrayList<NoteItemModel>()
    fun getDatabaseInfo(count1: Int, toSearch: String) {
        //add data and view it
        noteList.clear()
        val cursor: Cursor = if (count1 == 1) {
            dbManager.query(null, null, null, DBManager.ColID)
        } else {
            val selectionsArgs = arrayOf<String?>("%$toSearch%", "%$toSearch%")
            dbManager.query(
                null,
                "Title like ? or Description like ?",
                selectionsArgs,
                DBManager.ColID
            )
        }
        //If wanted to select all data then give null in selection
        if (cursor.moveToFirst()) {
            do {
                val columnIndex1 = cursor.getColumnIndex(DBManager.ColID)
                val columnIndex2 = cursor.getColumnIndex(DBManager.ColDateTime)
                val columnIndex3 = cursor.getColumnIndex(DBManager.ColTitle)
                val columnIndex4 = cursor.getColumnIndex(DBManager.ColDescription)
                val columnIndex5 = cursor.getColumnIndex(DBManager.ColRemTime)
                val columnIndex6 = cursor.getColumnIndex(DBManager.ColRemDate)
                if (columnIndex1 != -1 && columnIndex2 != -1 && columnIndex3 != -1
                    && columnIndex4 != -1 && columnIndex5 != -1 && columnIndex6 != -1
                ) {
                    noteList.add(
                        NoteItemModel(
                            cursor.getLong(columnIndex1),
                            cursor.getString(columnIndex2),
                            cursor.getString(columnIndex3),
                            cursor.getString(columnIndex4),
                            cursor.getString(columnIndex5),
                            cursor.getString(columnIndex6)
                        )
                    )
                } else {
                    // Handle the case where a column doesn't exist in the cursor
                    Log.e("Error", "One or more columns not found in the cursor.");
                }

            } while (cursor.moveToNext())
        }

        if (noteList.isEmpty()) {
            binding.lvAll.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.lvAll.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
            val myCustomAdapter = MyNotesAdapter(this, noteList)
            binding.lvAll.adapter = myCustomAdapter
            myCustomAdapter.setItemClickListener(object : MyItemClickListener {
                override fun onItemClick(data: NoteItemModel?) {
                    updateNote(data)
                }
            })
        }
    }

    fun updateNote(data: NoteItemModel?) {
        // For updating notes
        val addEditIntent = Intent(applicationContext, NoteUpdateActivity::class.java)
        addEditIntent.putExtra(CommonParameters.noteId, data!!.id)
        addEditIntent.putExtra(CommonParameters.noteTitle, data.title)
        addEditIntent.putExtra(CommonParameters.noteDescription, data.description)
        addEditIntent.putExtra(CommonParameters.isUpdate, 1)
        addEditIntent.putExtra(CommonParameters.noteTime, data.time)
        addEditIntent.putExtra(CommonParameters.noteDate, data.date)
        startForResult.launch(addEditIntent)
    }

    fun addUpdateOpenActivity(view: View?) {
        val noteID = dbManager.RowCount() + 1
        val addEditIntent = Intent(applicationContext, NoteUpdateActivity::class.java)
        addEditIntent.putExtra(CommonParameters.noteId, noteID)
        addEditIntent.putExtra(CommonParameters.noteTitle, "ignore")
        addEditIntent.putExtra(CommonParameters.noteDescription, "ignore")
        addEditIntent.putExtra(CommonParameters.isUpdate, 0)
        addEditIntent.putExtra(CommonParameters.noteTime, "ignore")
        addEditIntent.putExtra(CommonParameters.noteDate, "ignore")
        startForResult.launch(addEditIntent)
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            getDatabaseInfo(1, "ignore")
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed() // Exit the app
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

        // Reset the variable after a delay (e.g., 2 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000) // Adjust the delay as needed
    }

}