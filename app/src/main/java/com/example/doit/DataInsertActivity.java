package com.example.doit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doit.databinding.ActivityDataInsertBinding;
public class DataInsertActivity extends AppCompatActivity {
    ActivityDataInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDataInsertBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        String type=getIntent().getStringExtra("type");
        if(type.equals("update"))
        {
            setTitle("update");
            binding.title.setText(getIntent().getStringExtra("title"));
            binding.desp.setText(getIntent().getStringExtra("desp"));
            int id=getIntent().getIntExtra("id",0);
            binding.add.setText("update note");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra( "title",binding.title.getText().toString());
                    intent.putExtra( "disp",binding.title.getText().toString());
                    intent.putExtra("id",id);
                    setResult(RESULT_OK,intent);
                    finish();

                }
            });

        }else{
        setTitle("Add Mode");

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra( "title",binding.title.getText().toString());
                intent.putExtra( "disp",binding.title.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DataInsertActivity.this,MainActivity.class));
    }
}