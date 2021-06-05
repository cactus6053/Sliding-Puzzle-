package com.example.pa1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class puzzleboard extends View {
    int width, height;
    int left, top;
    int orgW, orgH;
    int picW, picH;
    Bitmap img, white_img;
    Bitmap imgPic[][] = new Bitmap[3][3];

    public puzzleboard(Context context){
        super(context);

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        width = display.getWidth()-50;
        height = display.getHeight()*2/5;

        img = BitmapFactory.decodeResource(context.getResources(),R.drawable.you);
        img = Bitmap.createScaledBitmap(img, width, height, true);

        white_img = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitespace);


        orgW = img.getWidth();
        orgH = img.getHeight();

        picW = orgW/3;
        picH = orgH/3;

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                imgPic[i][j] = Bitmap.createBitmap(img, j*picW, i*picH, picW, picH );
            }
        }
        imgPic[2][2] = Bitmap.createBitmap(white_img, picW, picH, picW, picH );


    }
}