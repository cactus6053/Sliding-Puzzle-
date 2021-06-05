package com.example.pa1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    Bitmap bitmap[] = new Bitmap[9];
    Bitmap bitmap2[] = new Bitmap[16];
    int tag[] = new int[9];
    int tag2[] = new int[16];
    Bitmap bitmap_change[] = new Bitmap[9];
    Bitmap bitmap2_change[] = new Bitmap[16];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.board);

        MyAdapter myadapter = new MyAdapter(this, bitmap);
        MyAdapter myadapter2 = new MyAdapter(this, bitmap2);
        Button btn_3x3 = (Button) findViewById(R.id.button_3x3);
        Button btn_4x4 = (Button) findViewById(R.id.button_4x4);
        Button btn_shuffle = (Button) findViewById(R.id.button_shuffle);

        btn_3x3.setOnClickListener(new View.OnClickListener() {
            Context bcontext = getApplicationContext();
            @Override
            public void onClick(View v) {
                puzzleboard board_3x3 = new puzzleboard(bcontext);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int index = i * 3 + j;
                        bitmap[index] = board_3x3.imgPic[i][j];
                        tag[index] = index;
                    }
                }
                bitmap_change = bitmap;
                gridView.setNumColumns(3);
                gridView.setAdapter(myadapter);
            }
        });

        btn_4x4.setOnClickListener(new View.OnClickListener() {
            Context bcontext = getApplicationContext();
            @Override
            public void onClick(View v) {
                puzzleboard2 board_4x4 = new puzzleboard2(bcontext);
                for (int i = 0; i < 4; i++){
                    for (int j = 0; j < 4; j++){
                        int index2 = i * 4 + j;
                        bitmap2[index2] = board_4x4.imgPic[i][j];
                        tag2[index2] = index2;
                    }
                }
                bitmap2_change = bitmap2;
                gridView.setNumColumns(4);
                gridView.setAdapter(myadapter2);
            }
        });

        btn_shuffle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int check = gridView.getNumColumns();
                if(check == 3){
                    Random random = new Random();
                    int x_pos=0, y_pos=0, tag_temp;
                    for(int k=0; k<8; k++){
                        int changeIndex = random.nextInt(9);
                        for(int i=0; i<3; i++) {
                            for (int j = 0; j < 3; j++) {
                                int index = i * 3 + j;
                                if (index == changeIndex) {
                                    x_pos = j;
                                    y_pos = i;
                                }
                            }
                        }
                        //bitmap 바꾸기
                        Bitmap change_temp = bitmap_change[k];
                        bitmap_change[k] = bitmap_change[y_pos*3 + x_pos];
                        bitmap_change[y_pos*3 + x_pos] = change_temp;
                        //tag 바꾸기
                        tag_temp = tag[k];
                        tag[k] = tag[y_pos*3 + x_pos];
                        tag[y_pos*3 + x_pos] = tag_temp;
                    }
                    myadapter.changePos(bitmap_change);
                    gridView.setAdapter(myadapter);
                }
                else if(check == 4) {
                    Random random = new Random();
                    int x_pos = 0, y_pos = 0, tag_temp;
                    for (int k = 0; k < 15; k++) {
                        int changeIndex = random.nextInt(16);
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                int index = i * 4 + j;
                                if (index == changeIndex) {
                                    x_pos = j;
                                    y_pos = i;
                                }
                            }
                        }
                        //bitmap 바꾸기
                        Bitmap change_temp = bitmap2_change[k];
                        bitmap2_change[k] = bitmap2_change[y_pos * 4 + x_pos];
                        bitmap2_change[y_pos * 4 + x_pos] = change_temp;
                        //tag 바꾸기
                        tag_temp = tag2[k];
                        tag2[k] = tag2[y_pos * 4 + x_pos];
                        tag2[y_pos * 4 + x_pos] = tag_temp;
                    }
                    myadapter2.changePos(bitmap2_change);
                    gridView.setAdapter(myadapter2);
                }
                else{
                    String msg = "click 3x3 or 4x4 first";
                    Toast msg_show = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    msg_show.show();
                }

            }
        });
        puzzlelistener listener = new puzzlelistener();
        gridView.setOnItemClickListener(listener);
    }
    private class puzzlelistener implements AdapterView.OnItemClickListener{
        GridView gridView = (GridView)findViewById(R.id.board);
        Context mcontext = getApplicationContext();
        MyAdapter myadapter = new MyAdapter(mcontext, bitmap_change);
        MyAdapter myadapter2 = new MyAdapter(mcontext, bitmap2_change);

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int check = gridView.getNumColumns();
            int finish3 = 0, finish4 = 0;
            int empty_position = 20;
            int x_pos = 0, y_pos = 0, empty_xpos = 0, empty_ypos = 0, mx, my;
            int[] dx = {-1, 0, 0, 1};
            int[] dy = {0, -1, 1, 0};
            int tag_temp, tag_temp2;
            if(check == 3){
                //빈공간과 터치 공간 좌표 구하기
                for(int i=0; i<9; i++){
                    if(tag[i] == 8)
                        empty_position = i;
                }
                for(int i=0; i<3; i++){
                    for(int j=0; j<3; j++){
                        int index = i*3 + j;
                        if(index == position){
                            x_pos = j;
                            y_pos = i;
                        }
                        else if(index == empty_position){
                            empty_xpos = j;
                            empty_ypos = i;
                        }
                    }
                }
                for(int k=0; k<4; k++){
                    my = y_pos + dy[k];
                    mx = x_pos + dx[k];
                    if(my>=0 && my<3 && mx>=0 && mx<3){
                        if(mx == empty_xpos && my == empty_ypos){
                            //bitmap 바꾸기
                            Bitmap change_temp = bitmap_change[my*3 + mx];
                            bitmap_change[my*3 + mx] = bitmap_change[y_pos*3 + x_pos];
                            bitmap_change[y_pos*3 + x_pos] = change_temp;
                            //tag 바꾸기
                            tag_temp = tag[y_pos*3 + x_pos];
                            tag[y_pos*3 + x_pos] = 8;
                            tag[my*3 + mx] = tag_temp;
                            //adapter로 적용
                            myadapter.changePos(bitmap_change);
                            gridView.setAdapter(myadapter);
                        }
                    }
                }

                ///finish check
                for(int i=0; i<9; i++){
                    if(tag[i] == i){
                        finish3 += 1;
                    }
                    else{
                        break;
                    }
                }
                if(finish3 == 9){
                    String msg = "FINISH!";
                    Toast msg_show = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    msg_show.show();
                }

            }
            else if(check == 4){
                for(int i=0; i<16; i++){
                    if(tag2[i] == 15)
                        empty_position = i;
                }
                for(int i=0; i<4; i++){
                    for(int j=0; j<4; j++){
                        int index = i*4 + j;
                        if(index == position){
                            x_pos = j;
                            y_pos = i;
                        }
                        else if(index == empty_position){
                            empty_xpos = j;
                            empty_ypos = i;
                        }
                    }
                }
                for(int k=0; k<4; k++){
                    my = y_pos + dy[k];
                    mx = x_pos + dx[k];
                    if(my>=0 && my<4 && mx>=0 && mx<4){
                        if(mx == empty_xpos && my == empty_ypos){
                            //bitmap 바꾸기
                            Bitmap change_temp = bitmap2_change[my*4 + mx];
                            bitmap2_change[my*4 + mx] = bitmap2_change[y_pos*4 + x_pos];
                            bitmap2_change[y_pos*4 + x_pos] = change_temp;
                            //tag 바꾸기
                            tag_temp2 = tag2[y_pos*4 + x_pos];
                            tag2[y_pos*4 + x_pos] = 15;
                            tag2[my*4 + mx] = tag_temp2;
                            //adapter로 적용
                            myadapter2.changePos(bitmap2_change);
                            gridView.setAdapter(myadapter2);
                        }
                    }
                }

                ///finish check
                for(int i=0; i<16; i++){
                    if(tag2[i] == i){
                        finish4 += 1;
                    }
                    else{
                        break;
                    }
                }
                if(finish4 == 16){
                    String msg = "FINISH!";
                    Toast msg_show = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    msg_show.show();
                }
            }


        }
    }
}