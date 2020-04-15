package com.ricardohg.ejercicio1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etName, etSurname, etDate;
    Button btnCalculate;
    //Calendar selectedBirthdate;
    String rfc, currentAge, currentZodiacSign, currentChineseZodiacSign;
    Calendar selectedBirthdate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variables
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etDate = findViewById(R.id.etDate);

        // Buttons
        btnCalculate = findViewById(R.id.btnCalculate);

        // On click
        etDate.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCalculate:
                if(validateData()) {
                    rfc = calculateRFC(
                            etName.getText().toString(),
                            etSurname.getText().toString(),
                            selectedBirthdate);
                    currentAge = calculateAge(
                            selectedBirthdate.get(Calendar.YEAR),
                            selectedBirthdate.get(Calendar.MONTH),
                            selectedBirthdate.get(Calendar.DAY_OF_MONTH));
                    currentZodiacSign = calculateZodiacSign(
                            selectedBirthdate.get(Calendar.DAY_OF_MONTH),
                            selectedBirthdate.get(Calendar.MONTH)
                    );
                    currentChineseZodiacSign = calculateChineseZodiacSign(
                            selectedBirthdate.get(Calendar.YEAR)
                    );
                    //Toast.makeText(MainActivity.this, currentChineseZodiacSign, Toast.LENGTH_SHORT).show();
                    createDialog();
                }
                break;
            case R.id.etDate:
                selectDate();
                break;
        }
    }

    private void createDialog(){
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);

        // Add the buttons
        builder.setPositiveButton(getString(R.string.Button_Share), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody =
                        getString(R.string.Message_Share) + "\n" +
                        getString(R.string.Result_Age, currentAge) + "\n" +
                        getString(R.string.Result_Zodiac, currentZodiacSign) + "\n" +
                        getString(R.string.Result_ChineseZodiac, currentChineseZodiacSign);
                String shareSub = getString(R.string.Message_ShareSubject);
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, getString(R.string.Message_ShareUsing)));

            }
        });
        builder.setNegativeButton(getString(R.string.Message_ShareCancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(
                getString(R.string.Result_RFC, rfc) + "\n" +
                getString(R.string.Result_Age, currentAge) + "\n" +
                getString(R.string.Result_Zodiac, currentZodiacSign) + "\n" +
                getString(R.string.Result_ChineseZodiac, currentChineseZodiacSign)
        ).setTitle(getString(R.string.Message_Results));

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String calculateRFC(String name, String surname, Calendar birthdate){
        String[] auxWordList = surname.split("\\s");
        String rfc = "";
        rfc = auxWordList[0].substring(0,2) + auxWordList[1].charAt(0);

        auxWordList = name.split("\\s");
        rfc += auxWordList[0].charAt(0);
        rfc += Integer.toString(birthdate.get(Calendar.YEAR)).substring(2);

        rfc += new DecimalFormat("00").format(birthdate.get(Calendar.MONTH) + 1);
        rfc += new DecimalFormat("00").format(birthdate.get(Calendar.DAY_OF_MONTH));

        rfc = Normalizer.normalize(rfc, Normalizer.Form.NFD);
        rfc = rfc.replaceAll("\\p{M}", "");

        // RFC final
        rfc = rfc.toUpperCase();

        return rfc;
    }

    private String calculateAge(int year, int month, int day){
        Calendar birthdate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        birthdate.set(year, month, day);

        int age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthdate.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return Integer.toString(age);
    }

    private String calculateZodiacSign(int day, int  month){
        String [][] zodiacData = new String[][]{
                {"20", getText(R.string.Zodiac0).toString(), getText(R.string.Zodiac1).toString()},
                {"19", getText(R.string.Zodiac1).toString(), getText(R.string.Zodiac2).toString()},
                {"21", getText(R.string.Zodiac2).toString(), getText(R.string.Zodiac3).toString()},
                {"20", getText(R.string.Zodiac3).toString(), getText(R.string.Zodiac4).toString()},
                {"21", getText(R.string.Zodiac4).toString(), getText(R.string.Zodiac5).toString()},
                {"21", getText(R.string.Zodiac5).toString(), getText(R.string.Zodiac6).toString()},
                {"23", getText(R.string.Zodiac6).toString(), getText(R.string.Zodiac7).toString()},
                {"23", getText(R.string.Zodiac7).toString(), getText(R.string.Zodiac8).toString()},
                {"23", getText(R.string.Zodiac8).toString(), getText(R.string.Zodiac9).toString()},
                {"23", getText(R.string.Zodiac9).toString(), getText(R.string.Zodiac10).toString()},
                {"22", getText(R.string.Zodiac10).toString(), getText(R.string.Zodiac11).toString()},
                {"22", getText(R.string.Zodiac11).toString(), getText(R.string.Zodiac0).toString()}
                };

        if(day < Integer.parseInt(zodiacData[month][0])){
            return zodiacData[month][1];
        }
        else{
            return zodiacData[month][2];
        }
    }

    private String calculateChineseZodiacSign(int year){
        switch (year % 12) {
            case 0: return getText(R.string.Chinese_Zodiac0).toString();
            case 1: return getText(R.string.Chinese_Zodiac1).toString();
            case 2: return getText(R.string.Chinese_Zodiac2).toString();
            case 3: return getText(R.string.Chinese_Zodiac3).toString();
            case 4: return getText(R.string.Chinese_Zodiac4).toString();
            case 5: return getText(R.string.Chinese_Zodiac5).toString();
            case 6: return getText(R.string.Chinese_Zodiac6).toString();
            case 7: return getText(R.string.Chinese_Zodiac7).toString();
            case 8: return getText(R.string.Chinese_Zodiac8).toString();
            case 9: return getText(R.string.Chinese_Zodiac9).toString();
            case 10: return getText(R.string.Chinese_Zodiac10).toString();
            case 11: return getText(R.string.Chinese_Zodiac11).toString();
            default: return null;
        }
    }

    // En este método se controla la aparición del "date picker", así como el llenado de la entrada
    // de texto correspondiente.
    private void selectDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.Format_Date));
                        etDate.setText(dateFormatter.format(newDate.getTime()));

                        selectedBirthdate = newDate;
                    }
        }, selectedBirthdate.get(Calendar.YEAR), selectedBirthdate.get(Calendar.MONTH), selectedBirthdate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private Boolean validateData() {
        if(etName.getText().toString().isEmpty() || etName.getText().toString().length() < 2){
            Toast.makeText(MainActivity.this, getText(R.string.Fail_Name), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(etSurname.getText().toString().isEmpty()
                || etSurname.getText().toString().split("\\s").length < 2){
            Toast.makeText(MainActivity.this, getText(R.string.Fail_Surname), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(etDate.getText().toString().isEmpty()
                || Integer.parseInt(calculateAge(selectedBirthdate.get(Calendar.YEAR),
                    selectedBirthdate.get(Calendar.MONTH),
                    selectedBirthdate.get(Calendar.DAY_OF_MONTH))) < 0){
            Toast.makeText(MainActivity.this, getText(R.string.Fail_Date), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
