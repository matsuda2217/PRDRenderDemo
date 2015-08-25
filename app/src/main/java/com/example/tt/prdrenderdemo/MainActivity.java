package com.example.tt.prdrenderdemo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    ImageView imageView;
    int currentPage = 0;
    Button next, pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button) findViewById(R.id.next);
        pres = (Button) findViewById(R.id.pres);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                render();
            }
        });
        pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                render();
            }
        });
        render();
    }

    public void render() {
        try {
            imageView = (ImageView) findViewById(R.id.image);
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            File file = new File("/storage/emulated/0/tuthucdandencongsan.pdf");

            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY));

            if (currentPage < 0) {
                currentPage = 0;
            } else if(currentPage>pdfRenderer.getPageCount()) {
                currentPage = pdfRenderer.getPageCount() - 1;
            }

            Matrix m = imageView.getImageMatrix();
            Rect rect = new Rect(0, 0, width, height);
            pdfRenderer.openPage(currentPage).render(bm, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            imageView.setImageMatrix(m);
            imageView.setImageBitmap(bm);
            imageView.invalidate();
        } catch (Exception e) {

        }
    }
}
