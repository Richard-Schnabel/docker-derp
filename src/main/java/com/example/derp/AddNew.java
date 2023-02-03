package com.example.derp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Objects;

public class AddNew extends AppCompatActivity {

    //definuj texty
    String alertJazykNenastaven = "Zadejte prosím Jazyk";
    String alertDateNenastaven = "Zadejte prosím Datum";
    String alertTimeNenastaven = "Zadejte prosím Čas";
    String alertPopisNenastaven = "Zadejte prosím Popis";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //najdi všechny inputy
        EditText jazykInput = findViewById(R.id.jazykinput);
        EditText popisInput = findViewById(R.id.descriptioninput);
        RatingBar rateInput = findViewById(R.id.rateinput);
        EditText dateInput = findViewById(R.id.dateinput);
        EditText timeInput = findViewById(R.id.timeinput);

        //ulož datum z kalendáře a vlož ho do inputy
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        dateInput.setText(date);

        //najdi buttony
        Button saveButton = findViewById(R.id.savebtn);
        Button dateBtn = findViewById(R.id.settingsbtn);
        Button jazykBtn = findViewById(R.id.jazykbtn);

        //připoj se k databázi
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        //po kliknutí na kalendář
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //zobraz kalendář
                Intent intent = new Intent(AddNew.this, Calendar.class);

                intent.putExtra("odkud", "add");
                startActivity(intent);
            }
        });

        //po kliknutí na jazykBtn
        jazykBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //definuj jazyky do nabídky
                String[] jazyky = {"C", "C++", "C#", "Go" ,"Java", "JavaScript", "MATLAB", "Ook!", "PHP", "Python", "Scratch", "Swift", "--Jiný--"};

                //postav Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNew.this);
                builder.setTitle("Vyber jazyk");

                //po kliknutí na možnost
                builder.setSingleChoiceItems(jazyky, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int polozka) {

                        //vypni nabídku
                        dialogInterface.dismiss();

                        //pokud je "Jiný" tak povol psaní
                        String jazyk = jazyky[polozka];
                        if (Objects.equals(jazyk, "--Jiný--")) {
                            jazykInput.setText("");
                            jazykInput.setEnabled(Boolean.TRUE);
                            return;
                        }

                        //jinak ne a ulož jazyk do inputu
                        jazykInput.setEnabled(Boolean.FALSE);
                        jazykInput.setText(jazyk);
                    }
                });

                //zobraz Alert Dialog
                builder.show();
            }
        });

        //po kliknutí na uložit
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //získej hodnoty z inputů
                String jazyk = jazykInput.getText().toString();
                String popis = popisInput.getText().toString();
                String rate = String.valueOf(rateInput.getProgress());
                String time = timeInput.getText().toString();
                String date = dateInput.getText().toString();

                //zkontroluj, jestli jsou hodnoty nastavené a pokud ne tak to hlaš a nepokračuj
                //U "rate" je to zbytečné, protože nejde nastavit neplatná hodnota
                //Popis deleted
                if (date.equals("")) {
                    Toast.makeText(v.getContext(),alertDateNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (jazyk.equals("")) {
                    Toast.makeText(v.getContext(),alertJazykNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.equals("")) {
                    Toast.makeText(v.getContext(),alertTimeNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }

                //přidej čas vytvoření
                long createdTime = System.currentTimeMillis();

                //vytvož nový záznam, přidej do něj data a přidej ho do databáze
                realm.beginTransaction();
                TDA zaznam = realm.createObject(TDA.class);

                zaznam.setDate(date);
                zaznam.setJazyk(jazyk);
                zaznam.setPopis(popis);
                zaznam.setTime(time);
                zaznam.setRate(rate);
                zaznam.setCreatedTime(createdTime);

                realm.commitTransaction();

                //vrať se zpět na Main
                Intent intent = new Intent(AddNew.this, Main.class);
                startActivity(intent);

            }
        });
    }
}