import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sucianalf.grouptracking.Model.Object_Mhsw_Dosen;
import com.example.sucianalf.grouptracking.R;

import java.util.List;


/**
 * Created by HVS on 14/03/18.
 */

public class Adapter_Mhsw_Dosen extends RecyclerView.Adapter<Adapter_Mhsw_Dosen.holder_mhsw_dosen> {

    private List<Object_Mhsw_Dosen> itemList;
    private Context context;

    public Adapter_Mhsw_Dosen(Context context, List<Object_Mhsw_Dosen> itemList){
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public holder_mhsw_dosen onCreateViewHolder(ViewGroup parent, int viewType){
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main3,null);
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
        return this.itemList.size();
    }

    public class holder_mhsw_dosen extends RecyclerView.ViewHolder {
        public TextView nama;
//                jabatanAatauKelas, nipAtauNim, emailAtauTlpn;
        public ImageView ava;

        public holder_mhsw_dosen(View itemView){
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
//            jabatanAatauKelas = itemView.findViewById(R.id.jabatanAtauKelas);
//            nipAtauNim = itemView.findViewById(R.id.nipAtauNim);
//            emailAtauTlpn = itemView.findViewById(R.id.emailAtauTlp);
            ava = itemView.findViewById(R.id.ava);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final LayoutInflater factory = LayoutInflater.from(context);
                    final View exitDialogView = factory.inflate(R.layout.activity_main4, null);
                    final AlertDialog exitDialog = new AlertDialog.Builder(context).create();

                    exitDialog.setView(exitDialogView);
                    exitDialog.show();
                }
            });
        }
    }

}

