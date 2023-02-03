package com.example.derp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.prefs.Preferences;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TDAAdapter extends RecyclerView.Adapter<TDAAdapter.MyViewHolder> {

    //definuj texty
    String errInputNull = "$nic$";
    String hoverDelete = "Odstranit";
    String hoverEdit = "Upravit";
    String alertDeleted = "Odstraněno";
    Context context;
    RealmResults<TDA> zaznamy;

    public TDAAdapter(Context context, RealmResults<TDA> zaznamy) {
        this.context = context;


        //získej uložené nastavení a pokud není zvoleno, tak createdTime sestupně
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String sortOrder = prefs.get("sortOrder", Sort.DESCENDING.toString());
        String sortBy = prefs.get("sortBy", "createdTime");

        //zfiltruj a seřaď záznamy
        this.zaznamy = zaznamy.where().contains("time", prefs.get("filterTime", "")).findAll();
        this.zaznamy = this.zaznamy.where().contains("date", prefs.get("filterDate", "")).findAll();
        this.zaznamy = this.zaznamy.where().contains("jazyk", prefs.get("filterJazyk", "")).findAll();
        this.zaznamy = this.zaznamy.where().contains("rate", prefs.get("filterRate", "")).findAll();
        this.zaznamy = this.zaznamy.where().findAllSorted(sortBy, Sort.valueOf(sortOrder));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TDAAdapter.MyViewHolder holder, int position) {

        //najdi zaznam v databázi
        TDA zaznam = zaznamy.get(position);

        //vpiš data záznamu z databáze do listu
        holder.popisOut.setText(zaznam.getPopis());
        holder.dateOut.setText(zaznam.getDate());
        holder.jazykOut.setText(zaznam.getJazyk());
        holder.rateOut.setText(Integer.parseInt(zaznam.getRate()) + " / 5");
        holder.timeOut.setText(zaznam.getTime() + " min");

        //setting onclick
        holder.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(zaznam.getCreatedTime());

                //otevři Edit s animací
                Intent intent = new Intent(context, Edit.class);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //nevim co to dela, ale bez toho to nefunguje
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return zaznamy.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView jazykOut;
        TextView popisOut;
        TextView timeOut;
        TextView rateOut;
        TextView dateOut;

        Button settingBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //najdi všechny outputy + button
            jazykOut = itemView.findViewById(R.id.jazykout);
            popisOut = itemView.findViewById(R.id.popisout);
            dateOut = itemView.findViewById(R.id.dateout);
            rateOut = itemView.findViewById(R.id.rateout);
            timeOut = itemView.findViewById(R.id.timeout);

            settingBtn = itemView.findViewById(R.id.settingsbtn);
        }
    }
}
