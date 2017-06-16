package antonio.survey;

/**
 * Created by antonio on 1/3/17.
 */

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static antonio.survey.R.id.displayText;


public class second extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);




        Button saveFile = (Button) findViewById(R.id.saveFile);

        EditText displayText = (EditText) findViewById(R.id.displayText);

<<<<<<< HEAD
<<<<<<< Updated upstream

        int start = Math.max(displayText.getSelectionStart(), 0);
        int end = Math.max(displayText.getSelectionEnd(), 0);
        displayText.getText().replace(Math.min(start, end), Math.max(start, end),
                DataManager.serverData, 0, DataManager.serverData.length());
=======
//        int start = Math.max(displayText.getSelectionStart(), 0);
//        int end = Math.max(displayText.getSelectionEnd(), 0);
//        displayText.getText().replace(Math.min(start, end), Math.max(start, end),
//                DataManager.serverData, 0, DataManager.serverData.length());
>>>>>>> juan-compare-letters







=======
<<<<<<< HEAD
//
//        displayText.getText().insert(displayText.getSelectionStart(), DataManager.serverData);


        //displayText.getText().insert(displayText.getSelectionStart(), DataManager.serverData);
=======
//        int start = Math.max(displayText.getSelectionStart(), 0);
//        int end = Math.max(displayText.getSelectionEnd(), 0);
//        displayText.getText().replace(Math.min(start, end), Math.max(start, end),
//                DataManager.serverData, 0, DataManager.serverData.length());






>>>>>>> juan-compare-letters
>>>>>>> Stashed changes


        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saving file...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                saveFile();

            }

        });

    }


    public void saveFile() {

        try {
            File myFile = new File("/sdcard/mysdfile.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            EditText toField = (EditText) findViewById(displayText);//TODO where the text is pulling from
            String data = toField.getText().toString();
            myOutWriter.append(data);
            myOutWriter.close();
            Toast.makeText(getBaseContext(),
                    "Done writing SD '/sdcard/mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
