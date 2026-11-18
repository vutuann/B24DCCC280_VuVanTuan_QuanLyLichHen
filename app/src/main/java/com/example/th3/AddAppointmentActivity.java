package com.example.th3;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity {

    EditText edtTitle, edtLocation;
    DatePicker datePicker;
    TimePicker timePicker;
    Switch swReminder;
    Button btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        edtTitle = findViewById(R.id.edtTitle);
        edtLocation = findViewById(R.id.edtLocation);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        swReminder = findViewById(R.id.swReminder);
        btnSave = findViewById(R.id.btnSave);

        timePicker.setIs24HourView(true);
        datePicker.setMinDate(System.currentTimeMillis() - 1000); // không chọn được ngày đã qua

        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int day) {
                        doiTenThang(); // DatePicker tự đổi lại tên tháng nên phải đặt lại
                    }
                });
        doiThuTuNgayThangNam();
        doiTenThang();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppointment();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveAppointment() {
        String title = edtTitle.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        // Kiểm tra bỏ trống
        if (title.isEmpty()) {
            edtTitle.setError("Tên lịch hẹn không được để trống");
            return;
        }
        if (location.isEmpty()) {
            edtLocation.setError("Địa điểm không được để trống");
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // getMonth() trả về 0 - 11 nên phải +1
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Kiểm tra thời điểm hẹn phải sau thời điểm hiện tại
        Calendar chosen = Calendar.getInstance();
        chosen.set(year, month - 1, day, hour, minute, 0);
        Calendar now = Calendar.getInstance();
        if (chosen.before(now)) {
            Toast.makeText(this, "Thời điểm hẹn phải sau thời điểm hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }


        String date = year + "-" + themSo0(month) + "-" + themSo0(day);
        String time = themSo0(hour) + ":" + themSo0(minute);

        Appointment a = new Appointment(title, location, date, time, swReminder.isChecked());
        long id = db.insertAppointment(a);

        if (id > 0) {
            Toast.makeText(this, "Đã lưu lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Thêm số 0 phía trước nếu nhỏ hơn 10, ví dụ 5 -> "05"
    private String themSo0(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }

    // Lấy cột ngày / tháng / năm bên trong DatePicker
    private NumberPicker layCot(String ten) {
        int id = Resources.getSystem().getIdentifier(ten, "id", "android");
        return datePicker.findViewById(id);
    }

    private void doiThuTuNgayThangNam() {
        NumberPicker ngay = layCot("day");
        NumberPicker thang = layCot("month");
        NumberPicker nam = layCot("year");
        if (ngay == null || thang == null || nam == null) {
            return;
        }
        LinearLayout cha = (LinearLayout) ngay.getParent();
        cha.removeAllViews();
        cha.addView(ngay);
        cha.addView(thang);
        cha.addView(nam);
    }

    private void doiTenThang() {
        NumberPicker thang = layCot("month");
        if (thang == null) {
            return;
        }
        int min = thang.getMinValue();
        int max = thang.getMaxValue();
        String[] ten = new String[max - min + 1];
        for (int i = min; i <= max; i++) {
            ten[i - min] = "Th" + (i + 1);
        }
        thang.setDisplayedValues(ten);
    }
}
