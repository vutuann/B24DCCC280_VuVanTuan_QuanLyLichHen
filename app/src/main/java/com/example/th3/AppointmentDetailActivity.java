package com.example.th3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvLocation, tvDateTime, tvReminderStatus;
    Switch swReminder;
    Button btnBack, btnDelete;
    DatabaseHelper db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Hiện nút quay lại (←) trên thanh tiêu đề
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvReminderStatus = findViewById(R.id.tvReminderStatus);
        swReminder = findViewById(R.id.swDetailReminder);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        // Nhận id từ Intent rồi truy vấn lại database
        id = getIntent().getIntExtra("id", -1);
        Appointment a = db.getAppointmentById(id);
        if (a == null) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(a.getTitle());
        tvLocation.setText(a.getLocation());
        tvDateTime.setText(a.getDisplayDateTime());
        swReminder.setChecked(a.isReminder());
        if (a.isReminder()) {
            tvReminderStatus.setText("Đang bật");
        } else {
            tvReminderStatus.setText("Đang tắt");
        }


        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.updateReminder(id, isChecked);
                if (isChecked) {
                    tvReminderStatus.setText("Đang bật");
                } else {
                    tvReminderStatus.setText("Đang tắt");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAppointment(id);
                Toast.makeText(AppointmentDetailActivity.this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
