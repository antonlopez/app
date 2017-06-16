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



public class second extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);




        Button saveFile = (Button) findViewById(R.id.saveFile);

        EditText displayText = (EditText) findViewById(R.id.displayText);

        int start = Math.max(displayText.getSelectionStart(), 0);
        int end = Math.max(displayText.getSelectionEnd(), 0);
        displayText.getText().replace(Math.min(start, end), Math.max(start, end),
                DataManager.serverData, 0, DataManager.serverData.length());








        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saving file...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }

        });

    }

}
