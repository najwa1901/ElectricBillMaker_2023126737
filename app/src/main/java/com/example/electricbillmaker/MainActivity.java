package com.example.electricbillmaker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etUnits, etRebate;
    Spinner spMonth;
    ImageView btnAbout;
    Button btnCalculate;
    ListView listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Electric Bill Maker");

        // Init
        db = new DatabaseHelper(this);
        spMonth = findViewById(R.id.spMonth);
        etUnits = findViewById(R.id.etUnits);
        etRebate = findViewById(R.id.etRebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnAbout = findViewById(R.id.btnAbout);
        listView = findViewById(R.id.listView);

        // Month spinner setup
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"}
        );
        spMonth.setAdapter(monthAdapter);

        loadBills();

        btnCalculate.setOnClickListener(v -> {
            try {
                String month = spMonth.getSelectedItem().toString();
                int units = Integer.parseInt(etUnits.getText().toString().trim());
                int rebatePercent = Integer.parseInt(etRebate.getText().toString().trim());

                if (rebatePercent < 1 || rebatePercent > 5) {
                    Toast.makeText(this, "Rebate must be between 1% and 5%.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double rebate = rebatePercent / 100.0;
                double total = calculateTotalCharges(units);
                double finalCost = total - (total * rebate);

                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ms", "MY"));
                String result = "Total Charges: " + format.format(total) +
                        "\nFinal Cost (after rebate): " + format.format(finalCost);

                new AlertDialog.Builder(this)
                        .setTitle("Calculation Result")
                        .setMessage(result)
                        .setPositiveButton("OK", (dialog, which) -> {
                            db.insertBill(month, units, total, rebatePercent, finalCost);
                            loadBills();
                            etUnits.setText("");
                            etRebate.setText("");
                        })
                        .show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAbout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            Intent i = new Intent(this, BillDetailActivity.class);
            i.putExtra("month", cursor.getString(cursor.getColumnIndexOrThrow("month")));
            i.putExtra("units", cursor.getInt(cursor.getColumnIndexOrThrow("units")));
            i.putExtra("total", cursor.getDouble(cursor.getColumnIndexOrThrow("total")));
            i.putExtra("rebate", cursor.getDouble(cursor.getColumnIndexOrThrow("rebate")));
            i.putExtra("final", cursor.getDouble(cursor.getColumnIndexOrThrow("final")));
            startActivity(i);
        });

        // ✅ Long click to delete
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            int recordId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this bill record?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.deleteBill(recordId);
                        loadBills();
                        Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }

    private void loadBills() {
        Cursor c = db.getAllBills();
        String[] from = {"month", "final"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, 0);
        listView.setAdapter(adapter);
    }

    private double calculateTotalCharges(int units) {
        if (units <= 200) return units * 0.218;
        else if (units <= 300) return 200 * 0.218 + (units - 200) * 0.334;
        else if (units <= 600) return 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        else return 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
    }
}
