package com.example.sucianalf.grouptracking;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sucianalf.grouptracking.Model.Object_Mhsw_Dosen;

import java.util.List;

//import com.example.sucianalf.grouptracking.Model.Object_Mhsw_Dosen;
//import id.tiregdev.atentik.R;

/**
 * Created by HVS on 14/03/18.
 */

public class Main3Activity extends RecyclerView.Adapter<Main3Activity.holder_mhsw_dosen> {

    private List<Object_Mhsw_Dosen> itemList;
    private Context context;
    TextView nama;
//            jabatanAtauKelas, spasi, nipAtauNim, emailAtauTlp;

    public Main3Activity(Context context, List<Object_Mhsw_Dosen> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public holder_mhsw_dosen onCreateViewHolder(ViewGroup parent, int viewType){
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main3,null);

        RelativeLayout wrap = layoutView.findViewById(R.id.wrapperCV);
        wrap.setBackgroundColor(ContextCompat.getColor(context, R.color.background_color));

        nama = layoutView.findViewById(R.id.nama);
        nama.setTextColor(ContextCompat.getColor(context, R.color.white));

//        jabatanAtauKelas = layoutView.findViewById(R.id.jabatanAtauKelas);
//        jabatanAtauKelas.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//        spasi = layoutView.findViewById(R.id.spasi);
//        spasi.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//        nipAtauNim = layoutView.findViewById(R.id.nipAtauNim);
//        nipAtauNim.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//        emailAtauTlp = layoutView.findViewById(R.id.emailAtauTlp);
//        emailAtauTlp.setTextColor(ContextCompat.getColor(context, R.color.white));

        holder_mhsw_dosen hn = new holder_mhsw_dosen(layoutView);
        return hn;
    }

    @Override
    public void onBindViewHolder(holder_mhsw_dosen holder, int position){
        holder.nama.setText(itemList.get(position).getNama());
//        holder.jabatanAatauKelas.setText(itemList.get(position).getJabatanAatauKelas());
//        holder.nipAtauNim.setText(itemList.get(position).getNipAtauNim());
//        holder.emailAtauTlpn.setText(itemList.get(position).getEmailAtauTlpn());
        holder.ava.setImageResource(itemList.get(position).getAva());
    }

    @Override
    public int getItemCount(){
        return  this.itemList.size();
    }

    public class holder_mhsw_dosen extends RecyclerView.ViewHolder {
        public TextView nama;
//                jabatanAatauKelas, nipAtauNim, emailAtauTlpn;
        public ImageView ava;

        public holder_mhsw_dosen(final View itemView){
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
//            jabatanAatauKelas = itemView.findViewById(R.id.jabatanAtauKelas);
//            nipAtauNim = itemView.findViewById(R.id.nipAtauNim);
//            emailAtauTlpn = itemView.findViewById(R.id.emailAtauTlp);
            ava = itemView.findViewById(R.id.ava);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DisplayNavigation.class);
                    context.startActivity(intent);
//                    final LayoutInflater factory = LayoutInflater.from(context);
//                    final View exitDialogView = factory.inflate(R.layout.display_navigation, null);
//                    final AlertDialog exitDialog = new AlertDialog.Builder(context).create();
//
//                    exitDialog.setView(exitDialogView);
//                    exitDialog.show();
                }
            });
        }
    }

}