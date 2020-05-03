package com.example.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
private TextToSpeech textToSpeech;
private Button speakBtn,stopBtn;
private String parsedText;
private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView=findViewById(R.id.pdfview);
        stopBtn=findViewById(R.id.stop);
        speakBtn=findViewById(R.id.convert_btn);
        pdfView.fromAsset("test2.pdf").load();
        try {
             parsedText = "";
            PdfReader reader = new PdfReader(getAssets().open("test2.pdf"));
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"; //Extracting the content from the different pages
            }
            Log.i("The text", parsedText);
            reader.close();
        } catch (Exception e) {
            Log.e("error", "onCreate: ",e );
        }


        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speech =textToSpeech.speak(parsedText,TextToSpeech.QUEUE_FLUSH,null);

            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.stop();
              //  textToSpeech.shutdown();
            }
        });
    }
}
