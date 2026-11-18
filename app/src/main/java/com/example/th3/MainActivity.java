package com.example.th3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvStats, tvEmpty;
    ListView lvAppointments;
    Button btnAdd;
    DatabaseHelper db;
    AppointmentAdapter adapter;
    ArrayList<Appointment> list = new ArrayList<>(); // chỉ chứa kết quả truy vấn để hiển thị

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        tvStats = findViewById(R.id.tvStats);
        tvEmpty = findViewById(R.id.tvEmpty);
        lvAppointments = findViewById(R.id.lvAppointments);
        btnAdd = findViewById(R.id.btnAdd);

        adapter = new AppointmentAdapter(this, list);
        lvAppointments.setAdapter(adapter);
        lvAppointments.setEmptyView(tvEmpty); // hiện chữ khi danh sách rỗng

        lvAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AppointmentDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });

        // Nút thêm lịch hẹn
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }

    // onResume chạy mỗi khi quay lại màn hình này -> tải lại dữ liệu
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        list.clear();
        list.addAll(db.getAllAppointments());
        adapter.notifyDataSetChanged();

        tvStats.setText("Tổng số lịch hẹn: " + db.countAll()
                + "\nĐang bật nhắc nhở: " + db.countReminders());
    }
}
