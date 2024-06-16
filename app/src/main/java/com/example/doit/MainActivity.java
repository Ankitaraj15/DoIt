package com.example.doit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NoteViewModel noteViewModle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        noteViewModle=new ViewModelProvider(this,(ViewModelProvider.Factory)ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
                intent.putExtra("type","addMode");
                startActivityForResult(intent,1);
            }
        });

        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setHasFixedSize(true);
        RVAdapter adapter=new RVAdapter();
        binding.rv.setAdapter(adapter);

        noteViewModle.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT)
                {
                    Toast.makeText(MainActivity.this,"updating",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
                    intent.putExtra("type","update");
                    intent.putExtra("title",adapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("disp",adapter.getNote(viewHolder.getAdapterPosition()).getDisp());
                    intent.putExtra("id",adapter.getNote(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent,2);
                }
            }
        }).attachToRecyclerView(binding.rv);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            String title= data.getStringExtra("title");
            String disp= data.getStringExtra("disp");
            Note note = new Note(title,disp);
            noteViewModle.insert(note);
            Toast.makeText(this, "note added", Toast.LENGTH_SHORT).show();

        }
        else if(requestCode==2)
        {
            String title= data.getStringExtra("title");
            String disp= data.getStringExtra("disp");
            Note note = new Note(title,disp);
            note.setId(data.getIntExtra("id",0));
            noteViewModle.update(note);
            Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();
        }
    }
}