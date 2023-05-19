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
    //inicializa um viewHolder que ira inflar o layout de list_item
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity);//Inicializa o LayoutInflater que recebe o mainActivity e infla os elementos em tempo de execução
        View v = inflater.inflate(R.layout.list_tem,parent,false);//Inicializa os elementos view a partir do LayoutInflater que gerou os elementos View a partir do arquivo xml MainActivity
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        //acessa o imageView do layout list_item
        View v = holder.itemView;
        ImageView imPhoto = v.findViewById(R.id.imItem);
        //define as dimensoes da foto
        int w = (int) mainActivity.getResources().getDimension(R.dimen.itemWidth);
        int h = (int) mainActivity.getResources().getDimension(R.dimen.itemHeight);
        //cria um bitmap dado um uri e as dimensoes
        Bitmap bitmap = Util.getBitmap(photos.get(position), w, h);
        imPhoto.setImageBitmap(bitmap);
        //adiciona um um listener de click para as imagens
        //este listener inicializa a photoActivity quando a foto e clicada
        imPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }

    @Override
    //retorna o tamanho do array de fotos
    public int getItemCount() {
        return photos.size();
    }
}
