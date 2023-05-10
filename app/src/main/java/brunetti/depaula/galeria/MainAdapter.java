package brunetti.depaula.galeria;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter {
    MainActivity mainActivity;
    List<String> photos;

    public MainAdapter(MainActivity mainActivity, List<String> photos){
        this.mainActivity = mainActivity;
        this.photos = photos;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity);//Inicializa o LayoutInflater que recebe o mainActivity e infla os elementos em tempo de execução
        View v = inflater.inflate(R.layout.list_tem,parent,false);//Inicializa os elementos view a partir do LayoutInflater que gerou os elementos View a partir do arquivo xml MainActivity
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        View v = holder.itemView;
        ImageView imPhoto = v.findViewById(R.id.imItem);
        int w = (int) mainActivity.getResources().getDimension(R.dimen.itemWidth);
        int h = (int) mainActivity.getResources().getDimension(R.dimen.itemHeight);
        Bitmap bitmap = Util.getBitmap(photos.get(position), w, h);
        imPhoto.setImageBitmap(bitmap);
        imPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
