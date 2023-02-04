package com.example.derp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Main extends AppCompatActivity {

    Dialog dialog;
    RealmResults<TDA> notesList;
    RecyclerView recyclerView;
    TDAAdapter adapter;
    Button filterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //Dialog dialog dialog
        dialog = new Dialog(this);

        //připoj se k databázi
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        //yvtvoř adaptér pro záznamy a zavolej ho
        notesList = realm.where(TDA.class).findAllSorted("createdTime", Sort.DESCENDING);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TDAAdapter(getApplicationContext(),notesList);
        recyclerView.setAdapter(adapter);

        //najdi buttony
        Button pridatZaznamBtn = findViewById(R.id.pridatzaznambtn);
        Button sortOrderBtn = findViewById(R.id.sortorderbtn);
        Button sortByBtn = findViewById(R.id.sortbybtn);
        filterBtn = findViewById(R.id.filterbtn);

        //po klkiknutí na "přidat záznam"...
        pridatZaznamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //otevři formulžř
                Intent intent = new Intent(Main.this, AddNew.class);
                startActivity(intent);
            }
        });

        //po kliknutí na "$asc/desc$"
        sortOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //získej uložené nastavení
                Preferences prefs = Preferences.userNodeForPackage(this.getClass());
                String sortOrder = prefs.get("sortOrder", Sort.DESCENDING.toString());

                //otoč asc/desc
                sortOrder = Objects.equals(sortOrder, Sort.ASCENDING.toString()) ? Sort.DESCENDING.toString() : Sort.ASCENDING.toString();
                prefs.put("sortOrder", sortOrder);

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);
            }
        });

        //po kliknutí na "fitlerby"
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFilterByDialog();
            }
        });

        //po kliknutí na "sortby"
        sortByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSortByDialog();

            }
        });

        //nakonec změn filter button text
        zmenFilterButtonText();
    }

    private void openFilterByDialog() {
        //vytvoř alert
        dialog.setContentView(R.layout.alert_filter_by);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //najdi všechny inputy
        EditText filterByTime = dialog.findViewById(R.id.filterByTime);
        EditText filterByDate = dialog.findViewById(R.id.filterByDate);
        EditText filterByJazyk = dialog.findViewById(R.id.filterByJazyk);
        EditText filterByRate = dialog.findViewById(R.id.filterByRate);


        //získej uložené nastavení
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        //napiš do nich data uložená ve filtru
        filterByTime.setText(prefs.get("filterTime",""));
        filterByDate.setText(prefs.get("filterDate",""));
        filterByJazyk.setText(prefs.get("filterJazyk",""));
        filterByRate.setText(prefs.get("filterRate",""));

        //po upravení kteréhokoliv textu
        filterByTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                //ulož filter
                prefs.put("filterTime", filterByTime.getText().toString());

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zmeň filter button text
                zmenFilterButtonText();
            }
        });
        filterByDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                //ulož filter
                prefs.put("filterDate", filterByDate.getText().toString());

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zmeň filter button text
                zmenFilterButtonText();
            }
        });
        filterByJazyk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                //ulož filter
                prefs.put("filterJazyk", filterByJazyk.getText().toString());

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zmeň filter button text
                zmenFilterButtonText();
            }
        });
        filterByRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                //ulož filter
                prefs.put("filterRate", filterByRate.getText().toString());

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zmeň filter button text
                zmenFilterButtonText();
            }
        });

        //zobraz alert
        dialog.show();
    }

    private void zmenFilterButtonText() {

        //zjisti nastavvení filtrů
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        //pro zjednodušení přednastav proměnné
        boolean fD = Objects.equals(prefs.get("filterDate", ""), "");
        boolean fR = Objects.equals(prefs.get("filterRate", ""), "");
        boolean fJ = Objects.equals(prefs.get("filterJazyk", ""), "");
        boolean fT = Objects.equals(prefs.get("filterTime", ""), "");

        //nastav text podle kombinací filtrů
        if (fD && fR && fJ && fT) {
            filterBtn.setText("Filtr: Žádný");
        } else if (!fD && fR && fJ && fT) {
            filterBtn.setText("Filtr: Datum");
        } else if (fD && !fR && fJ && fT) {
            filterBtn.setText("Filtr: Hodnocení");
        } else if (fD && fR && !fJ && fT) {
            filterBtn.setText("Filtr: Jazyk");
        } else if (fD && fR && fJ && !fT) {
            filterBtn.setText("Filtr: Čas");
        } else {
            filterBtn.setText("Filtr: Různé");
        }
    }

    private void openSortByDialog() {

        //vytvoř alert
        dialog.setContentView(R.layout.alert_sort_by);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //najdi všechny inputy
        RadioButton sortByCreatedTime = dialog.findViewById(R.id.sortByCreatedTime);
        RadioButton sortByDatum = dialog.findViewById(R.id.sortByDatum);
        RadioButton sortByTime = dialog.findViewById(R.id.sortByTime);
        RadioButton sortByJazyk = dialog.findViewById(R.id.sortByJazyk);
        RadioButton sortByRate = dialog.findViewById(R.id.sortByRate);

        //získej uložené nastavení a nastav selected
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String sortBy = prefs.get("sortBy","createdTime");
        if (sortBy.equals("createdTime")) { sortByCreatedTime.setChecked(true); }
        if (sortBy.equals("date")) { sortByDatum.setChecked(true); }
        if (sortBy.equals("time")) { sortByTime.setChecked(true); }
        if (sortBy.equals("jazyk")) { sortByJazyk.setChecked(true); }
        if (sortBy.equals("rate")) { sortByRate.setChecked(true); }

        //po kilkuní na možnost
        sortByCreatedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //změň nastavení na date
                prefs.put("sortBy", "createdTime");

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zavři okno
                dialog.dismiss();
            }
        });
        sortByDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //změň nastavení na date
                prefs.put("sortBy", "date");

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zavři okno
                dialog.dismiss();
            }
        });
        sortByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //změň nastavení na date
                prefs.put("sortBy", "time");

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zavři okno
                dialog.dismiss();
            }
        });
        sortByJazyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //změň nastavení na date
                prefs.put("sortBy", "jazyk");

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zavři okno
                dialog.dismiss();
            }
        });
        sortByRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //změň nastavení na date
                prefs.put("sortBy", "rate");

                //reloadni zaznamy
                TDAAdapter adapter = new TDAAdapter(getApplicationContext(), notesList);
                recyclerView.setAdapter(adapter);

                //zavři okno
                dialog.dismiss();
            }
        });

        //ukaž alert
        dialog.show();
    }

    //přepiš back button na nic
    @Override
    public void onBackPressed(){

    }
}