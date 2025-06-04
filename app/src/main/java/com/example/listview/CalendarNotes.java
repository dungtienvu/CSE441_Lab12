package com.example.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    ListView lv;
    ArrayList<String> arraywork;
    ArrayAdapter<String> arrAdapter;
    EditText edtwork, edthour, edtmi;
    TextView txtdate;
    Button btnwork;
    SharedPreferences prefs;
    final String PREFS_KEY = "work_list";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        edthour = findViewById(R.id.edthour);
        edtmi = findViewById(R.id.edtmi);
        edtwork = findViewById(R.id.edtwork);
        btnwork = findViewById(R.id.btnadd);
        lv = findViewById(R.id.listView1);
        txtdate = findViewById(R.id.txtdate);

        // Khởi tạo
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        arraywork = new ArrayList<>();

        // Load dữ liệu từ SharedPreferences
        loadData();

        // Adapter
        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arraywork);
        lv.setAdapter(arrAdapter);

        // Hiển thị ngày hiện tại
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtdate.setText("Hôm Nay: " + sdf.format(currentDate));

        // Xử lý thêm công việc
        btnwork.setOnClickListener(v -> {
            if (edtwork.getText().toString().isEmpty() ||
                    edthour.getText().toString().isEmpty() ||
                    edtmi.getText().toString().isEmpty()) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info missing")
                        .setMessage("Please enter all information of the work")
                        .setPositiveButton("Continue", null)
                        .show();

            } else {
                String str = "+ " + edtwork.getText().toString() + " - "
                        + edthour.getText().toString() + ":"
                        + edtmi.getText().toString();
                arraywork.add(str);
                arrAdapter.notifyDataSetChanged();
                saveData();

                edtwork.setText("");
                edthour.setText("");
                edtmi.setText("");
            }
        });

        // Xử lý xóa công việc khi click
        lv.setOnItemClickListener((parent, view, position, id) -> {
            arraywork.remove(position);
            arrAdapter.notifyDataSetChanged();
            saveData();
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (String item : arraywork) {
            sb.append(item).append(";;;");
        }
        editor.putString(PREFS_KEY, sb.toString());
        editor.apply();
    }

    private void loadData() {
        String saved = prefs.getString(PREFS_KEY, "");
        if (!saved.isEmpty()) {
            String[] items = saved.split(";;;");
            arraywork.addAll(Arrays.asList(items));
        }
    }
}
