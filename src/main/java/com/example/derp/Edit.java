package com.example.derp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import io.realm.Realm;

public class Edit extends AppCompatActivity {

    //definuj texty
    String alertUlozeno = "Záznam byl uložen";
    String alertDeleted = "Odstraněno";
    String alertJazykNenastaven = "Zadejte prosím Jazyk";
    String alertDateNenastaven = "Zadejte prosím Datum";
    String alertTimeNenastaven = "Zadejte prosím Čas";
    String alertPopisNenastaven = "Zadejte prosím Popis";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //získej createdTime editovaného záznamu
        Intent incomingIntent = getIntent();
        String createdTime = incomingIntent.getStringExtra("id");

        //najdi všechny inputy
        EditText jazykInput = findViewById(R.id.jazykinput);
        EditText popisInput = findViewById(R.id.descriptioninput);
        RatingBar rateInput = findViewById(R.id.rateinput);
        EditText dateInput = findViewById(R.id.dateinput);
        EditText timeInput = findViewById(R.id.timeinput);

        //najdi všechny buttony
        Button saveButton = findViewById(R.id.savebtn);
        Button jazykBtn = findViewById(R.id.jazykbtn);
        Button deleteBtn = findViewById(R.id.deletebtn);
        Button dateBtn = findViewById(R.id.datebtn);

        //připoj se k databázi
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        //najdi zaznam podle createdTime
        TDA zaznam = realm.where(TDA.class).equalTo("createdTime",Long.valueOf(createdTime)).findFirst();

        //nastav inputy na data z editovaného záznamu
        jazykInput.setText(zaznam.getJazyk());
        popisInput.setText(zaznam.getPopis());
        rateInput.setProgress(Integer.parseInt(zaznam.getRate()));
        dateInput.setText(zaznam.getDate());
        timeInput.setText(zaznam.getTime());

        //pokud je date z kalendáře nastavené, ulož ho a vlož ho do inputy
        if (incomingIntent.hasExtra("date")) {
            String date = incomingIntent.getStringExtra("date");
            dateInput.setText(date);
        }

        //po kliknutí na kalendář
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //zobraz kalendář
                Intent intent = new Intent(Edit.this, Calendar.class);

                intent.putExtra("odkud", "edit");
                intent.putExtra("id", createdTime);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
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

        //po kilkuntí na delete button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //připoj se k databázi
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                //smash záznam
                zaznam.deleteFromRealm();
                realm.commitTransaction();

                //křič "smazáno"
                Toast.makeText(getBaseContext(),alertDeleted,Toast.LENGTH_SHORT).show();

                //reloadni Main bez animace
                Intent intent = new Intent(getBaseContext(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //nevim co to dela, ale bez toho to nefunguje
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getBaseContext().startActivity(intent);
            }
        });


        //po kliknutí na save
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
                //U rate je to zbytečné, protože nejde nastavim neplatná hodnota
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
                if (popis.equals("")) {
                    Toast.makeText(v.getContext(),alertPopisNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }

                //smaž starý záznam
                realm.beginTransaction();
                zaznam.deleteFromRealm();

                //vytvoř nový záznam a přidej do něj nová data + staré id
                TDA novyZazanm = realm.createObject(TDA.class);

                novyZazanm.setDate(date);
                novyZazanm.setJazyk(jazyk);
                novyZazanm.setPopis(popis);
                novyZazanm.setTime(time);
                novyZazanm.setRate(rate);
                novyZazanm.setCreatedTime(Long.parseLong(createdTime));

                realm.commitTransaction();

                //křič uloženo
                Toast.makeText(v.getContext(),alertUlozeno,Toast.LENGTH_SHORT).show();

                //vrať se zpět na Main
                Intent intent = new Intent(Edit.this, Main.class);
                startActivity(intent);

            }
        });
    }
}
