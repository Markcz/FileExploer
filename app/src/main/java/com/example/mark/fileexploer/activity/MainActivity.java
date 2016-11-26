package com.example.mark.fileexploer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.fileexploer.R;
import com.example.mark.fileexploer.Utils.OpenFile;
import com.example.mark.fileexploer.adapter.RecyclerViewAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<File> files;
    private AlertDialog alertDialog;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //为当前activity添加启动动画
//        overridePendingTransition(R.anim.in_alpha,0);

        filePath = getIntent().getStringExtra("data");
        if (filePath == null) {
            filePath = "/";
        }
        //向files对象中填充数据
        initFiles(filePath);
        //设置recyclerView
        setRecyclerView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //将toobar标题设置为当前路径
        toolbar.setTitle(filePath);
        toolbar.setTitleTextAppearance(MainActivity.this,R.style.toolBar);
        setSupportActionBar(toolbar);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View v = inflater.inflate(R.layout.popup_view,null);
                //设置 alertDialog 并处理点击事件
                alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.show();
                alertDialog.getWindow().setContentView(v);
                //Log.i("test",alertDialog.toString());
                TextView dirL = (TextView) v.findViewById(R.id.add_dir);
                TextView fileL = (TextView) v.findViewById(R.id.add_file);
                dirL.setOnClickListener(new ItemSelectedListener());
                fileL.setOnClickListener(new ItemSelectedListener());
                //alertDialog.dismiss();
                if (dirL == null) {
                    Log.i("test","null");
                }
            }
        });

        //给recyclerView注册上下文菜单
        super.registerForContextMenu(recyclerView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private class ItemSelectedListener implements View.OnClickListener{
        @Override
        public void onClick(final View v) {
            addFileOrDir(v);
        }
    }
    //创建文件和目录
    private void addFileOrDir(final View v) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View editView = inflater.inflate(R.layout.name_input_dialog_view,null);
        final EditText editText = (EditText) editView.findViewById(R.id.edit_name);
        Button btn_save = (Button) editView.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) editView.findViewById(R.id.btn_cancel);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                switch (v.getId()){
                    case R.id.add_dir:
                File dir = new File(filePath+File.separator+editText.getText()+File.separator);
                if (!dir.exists()){
                    dir.mkdir();
                    Log.i("test",dir.getName());
                    dialog.dismiss();
                    Toast toast = Toast.makeText(MainActivity.this,"创建成功",Toast.LENGTH_SHORT);
                    //为Toast添加图片显示
                    /*
                    toast.setGravity(Gravity.CENTER,0,0);
                    LinearLayout  toastContentView = (LinearLayout) toast.getView();
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    toastContentView.addView(imageView);
                    */
                    toast.show();
                    recyclerViewAdapter.addData(0,dir);
                    recyclerView.scrollToPosition(0);
                    return;
                }
                Toast.makeText(MainActivity.this,"文件夹已存在",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                        break;
                case R.id.add_file:
                File  file = new File(filePath+File.separator+editText.getText()+File.separator);
                try {
                    if (!file.exists()){
                    file.createNewFile();
                    Toast.makeText(MainActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    recyclerViewAdapter.addData(0,file);
                    recyclerView.scrollToPosition(0);
                    return;
                    }
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this,"文件无法创建，权限不够",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                    Toast.makeText(MainActivity.this,"文件已存在",Toast.LENGTH_SHORT).show();
                    Log.i("test",file.getName()+"iii");
                    dialog.dismiss();
                break;
            }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(editView);
        alertDialog.dismiss();
    }

    /*
        对recyclerView进行设置
     */
    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(this, files);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false));
        // 选择的是 文件还是目录
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, File data) {
                if (data.isDirectory()) {
                    Intent i = new Intent();
                    i.setClass(MainActivity.this, MainActivity.class);
                    i.putExtra("data", data.getAbsolutePath());
                    startActivity(i);
                }
                if (data.isFile()) {
                    Intent i = OpenFile.openFile(data.getAbsolutePath());
                    //Log.i("filePath",data.getName());
                    startActivity(i);
                }
            }
        });
    }

    /*
       向files中添加数据
     */
    private void initFiles(String filePath) {
        Log.i("filePath",filePath);
        try {
            files = new ArrayList<>();
            File dir = new File(filePath);
            for (File f : dir.listFiles()
                    ) {
                files.add(f);
            }
        } catch (Exception e) {
            TextView textView = new TextView(this);
            textView.setText("你的设备并未获得root权限，因此无法访问此受保护的文件或文件夹。");
            textView.setTextSize(20);
            textView.setX(0);
            textView.setY(200);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.addContentView(textView, params);
            //Toast.makeText(this,"你的设备并未获得root权限，因此无法访问此受保护的文件或文件夹。",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        File f = files.get(recyclerViewAdapter.getPosition());
        switch (item.getItemId()){
            case R.id.delete:
                if (f!=null&&f.isFile()){
                    f.delete();
                    recyclerViewAdapter.removeData(recyclerViewAdapter.getPosition());
                    recyclerView.scrollToPosition(recyclerViewAdapter.getPosition());
                    return true;
                }
                if (f!=null&&f.isDirectory()){
                    deleteFiles(f);
                    f.delete();
                    recyclerViewAdapter.removeData(recyclerViewAdapter.getPosition());
                }
                Toast.makeText(MainActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.copy:

                Toast.makeText(MainActivity.this,"点击了复制",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onContextItemSelected(item);
    }
    //删除目录下所有文件
    private void deleteFiles(File f) {

        File [] items =f.listFiles();
        for (File i: items
             ) {
            if (i.isFile()){
                i.delete();
            }
            if (i.isDirectory()){
               deleteFiles(i);
               //i.delete();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.grid) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            return true;
        }
        if (id == R.id.list) {
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                    LinearLayoutManager.VERTICAL, false));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent i = new Intent();
            i.setClass(MainActivity.this,AboutActivity.class);
            startActivity(i);
            this.overridePendingTransition(R.anim.in_alpha,R.anim.out_alpha);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        //为当前activity添加结束动画
        overridePendingTransition(0,R.anim.out_alpha);
    }


}
