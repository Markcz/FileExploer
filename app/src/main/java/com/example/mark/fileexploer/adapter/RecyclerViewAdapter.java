package com.example.mark.fileexploer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.fileexploer.R;
import com.example.mark.fileexploer.activity.MainActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mark on 16-11-19.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {


    private final Context context;
    private final ArrayList<File> data;

    private int position;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public RecyclerViewAdapter(Context context  , ArrayList<File> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = View.inflate(context, R.layout.content_main,null );
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String data = this.data.get(i).getName();
        viewHolder.textView.setText(data);
        if(this.data.get(i).isFile()){
            viewHolder.imageView.setImageResource(R.drawable.file);
        }
        if (this.data.get(i).isDirectory()){
            viewHolder.imageView.setImageResource(R.drawable.dir);
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void addData(int i, File file) {
        data.add(i,file);
        notifyItemInserted(i);
    }

    public void removeData(int i) {
        if (data.size()>0) {
            data.remove(i);
        }
        notifyItemRemoved(i);
    }




    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.img_icon);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition(),data.get(getLayoutPosition()));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //setFile(data.get(getLayoutPosition()));
                    setPosition(getLayoutPosition());
                    return false;
                }
            });
        }
        //实现创建上下文菜单
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("操作");
            menu.add(0, R.id.delete, Menu.NONE, R.string.delete);
            menu.add(0, R.id.copy, Menu.NONE, R.string.copy);
            menu.add(0, R.id.move, Menu.NONE, R.string.move);
        }






   }

    public interface OnItemClickListener{
         void onItemClick(View v,int position,File data);
    }
}
