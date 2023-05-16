package com.example.notetoself;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private Note note;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mprefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onResume(){
        super.onResume();
        mprefs=getSharedPreferences("Note to self",MODE_PRIVATE);
        mSound=mprefs.getBoolean("sound",true);
        mAnimOption=mprefs.getInt("anim option",SettingsActivity.FAST);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNoteAdapter=new NoteAdapter();
    ListView listNote=(ListView) findViewById(R.id.listview);
    listNote.setAdapter(mNoteAdapter);

    listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /* Create the temporary Note
            which is a reference to the Note
            that has just been clicked
             */
            Note tempNote= (Note) mNoteAdapter.getItem(position);
            //create a new dialog window
            DialogShowNote dialog=new DialogShowNote();
            //send in a reference to the note to be shown
            dialog.sendNoteSelected(tempNote);

            //show the dialog window with the note in it
            dialog.show(getFragmentManager(),"");

        }
    });

    }

    public void createNewNote(Note n){
        // Create a mNote
       mNoteAdapter.addNote(n);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add) {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "123");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NoteAdapter extends BaseAdapter{

        List<Note>noteList=new ArrayList<>();
        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int position) {
            return noteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //Has view been inflated already
            if(view==null){
                //if not do so here
                //first create a layout inflater
                LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // instantiate view using inflater.inflate
                view=inflater.inflate(R.layout.listitem,viewGroup,false);
                //the false parameter is necessary because
                //of the way that we want to use listitem
        }
            //Grab a reference to all our textview and imageview widgets
            TextView txtTitle=(TextView) view.findViewById(R.id.txtTitle);
            TextView txtDescription=(TextView) view.findViewById(R.id.txtDescription);
            ImageView ivImportant=(ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo=(ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea=(ImageView) view.findViewById(R.id.imageViewIdea);

            //Hide any ImageView widgets that are not relevent
            Note tempNote=noteList.get(position);
            if (!tempNote.isImportant()){
                ivImportant.setVisibility(View.GONE);
            }
            if (!tempNote.isIdea()){
                ivIdea.setVisibility(View.GONE);
            }
            if (!tempNote.isTodo()){
                ivTodo.setVisibility(View.GONE);
            }
            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return view;
    }
    public void addNote(Note n){
        noteList.add(n);
        notifyDataSetChanged();}

    }



}
