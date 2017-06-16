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


       // displayText.getText().insert(displayText.getSelectionStart(), DataManager.serverData);

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saving file...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();



            }

        });



    }

    public void SaveFile() {

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
