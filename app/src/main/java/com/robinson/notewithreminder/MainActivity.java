package com.robinson.notewithreminder;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.robinson.notewithreminder.adapter.MyNotesAdapter;
import com.robinson.notewithreminder.model.NoteItemModel;
import com.robinson.notewithreminder.database.DBManager;
import com.robinson.notewithreminder.screen.NoteUpdateActivity;
import com.robinson.notewithreminder.screen.WebViewActivity;
import com.robinson.notewithreminder.utilities.CommonParameters;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBManager dbManager;
   // long noteID;
  //  String RecordTitle;
  //  String RecordDesc;
   // String RecordDateRem;
    //String RecordTimeRem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        getDatabaseInfo(1, "ignore");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDatabaseInfo(2, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDatabaseInfo(2, newText);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_help) {
            AlertDialog.Builder info = new AlertDialog.Builder(this);
            info.setMessage("Project started on 14-10-2023\nby Robinson Macwan\nFEATURES:\nADD, DELETE, UPDATE and SEARCH Notes along with setting REMINDERs at any date and time.")
                    .setTitle("Information")
                    .setPositiveButton("Source Code", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent webintent = new Intent(getApplicationContext(), WebViewActivity.class);
                            startActivity(webintent);
//                                Toast.makeText(getApplicationContext(),"Hi!",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        } else if (id == R.id.menu_settings) {

            Toast.makeText(getApplicationContext(), "Settings is currently under development!", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }


    ArrayList<NoteItemModel> noteList = new ArrayList<NoteItemModel>();
    MyNotesAdapter myCustomAdapter;


    public void getDatabaseInfo(int count1, String to_search) {
        //add data and view it
        noteList.clear();
        Cursor cursor;
        if (count1 == 1) {
            cursor = dbManager.query(null, null, null, DBManager.ColID);
        } else {
            String[] SelectionsArgs = {"%" + to_search + "%", "%" + to_search + "%"};
            cursor = dbManager.query(null, "Title like ? or Description like ?", SelectionsArgs, DBManager.ColID);
        }
        //If wanted to select all data then give null in selection
        if (cursor.moveToFirst()) {
            do {
                noteList.add(new NoteItemModel(cursor.getLong(cursor.getColumnIndex(DBManager.ColID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDateTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColTitle)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDescription)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColRemTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColRemDate))));

            } while (cursor.moveToNext());
        }
        myCustomAdapter = new MyNotesAdapter(this,noteList);
        final RecyclerView lsNews = (RecyclerView) findViewById(R.id.lv_all);
        lsNews.setAdapter(myCustomAdapter);//intisal with data

        myCustomAdapter.setItemClickListener(new MyNotesAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(NoteItemModel data) {
                updateNote(data);
            }
        });
    }

    public void updateNote(NoteItemModel data) {
        // For updating notes

        Intent addEditIntent = new Intent(getApplicationContext(), NoteUpdateActivity.class);
        addEditIntent.putExtra(CommonParameters.noteId, data.ID);
        addEditIntent.putExtra(CommonParameters.noteTitle, data.Title);
        addEditIntent.putExtra(CommonParameters.noteDescription, data.Description);
        addEditIntent.putExtra(CommonParameters.isUpdate, 1);
        addEditIntent.putExtra(CommonParameters.noteTime, data.Time);
        addEditIntent.putExtra(CommonParameters.noteDate, data.Date);
        startActivityForResult(addEditIntent, 4);
    }


    public void addUpdateOpenActivity(View view) {
        long noteID = dbManager.RowCount() + 1;
       // String noteIDString = String.valueOf(noteID);
        Intent addEditIntent = new Intent(getApplicationContext(), NoteUpdateActivity.class);
        addEditIntent.putExtra(CommonParameters.noteId, noteID);
        addEditIntent.putExtra(CommonParameters.noteTitle, "ignore");
        addEditIntent.putExtra(CommonParameters.noteDescription, "ignore");
        addEditIntent.putExtra(CommonParameters.isUpdate, 0);
        addEditIntent.putExtra(CommonParameters.noteTime, "ignore");
        addEditIntent.putExtra(CommonParameters.noteDate, "ignore");
        startActivityForResult(addEditIntent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 3) {
//            do the things u wanted
            getDatabaseInfo(1, "ignore");
           // noteID = 0;
        } else if (requestCode == 4) {
            getDatabaseInfo(1, "ignore");
           // noteID = 0;
        }
    }
}
