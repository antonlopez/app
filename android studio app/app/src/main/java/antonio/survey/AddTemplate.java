package antonio.survey;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static antonio.survey.R.id.editText;

public class AddTemplate extends AppCompatActivity {

    private static final String LOG_TAG = "error save file";
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private static FragmentManager fragmentManager;

    private static final int SELECT_SINGLE_PICTURE = 101;

    private static final int SELECT_MULTIPLE_PICTURE = 201;

    public static final String IMAGE_TYPE = "image/*";

    private ImageView selectedImagePreview;

    String selectedImagePath;

    String data;


    String x_start = "";


    public static String BASE_URL = "http://172.19.144.219:12345/images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);

        findViewById(R.id.ImageTemplateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                imageBrowse();
            }
        });




        //This gets the data from the textbox




        selectedImagePreview = (ImageView)findViewById(R.id.imageView);




        findViewById(R.id.nextLetter).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                EditText toField = (EditText) findViewById(R.id.editText);
                data = toField.getText().toString();

                ArrayList arrayList = new ArrayList();
                SaveFile();

                if (selectedImagePath != null) {
                    imageUpload(selectedImagePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Include a letter!", Toast.LENGTH_SHORT).show();
                }


                // LetterTemplates letters = new LetterTemplates(data, img);


                //this button will push the image to the database and store it with the
                // cooresponding letter saving it in the process
            }
        });




        findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                //EditText toField = (EditText) findViewById(editText);
               // String data = toField.getText().toString();

                //This should also include functions in the nextLetter Button above



                ArrayList arrayList = new ArrayList();
                Intent i = new Intent(AddTemplate.this, MainActivity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                EditText toField = (EditText) findViewById(editText);
                String data = toField.getText().toString();


                Intent i = new Intent(AddTemplate.this, MainActivity.class);
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Template saved...", Toast.LENGTH_LONG).show();

            }
        });
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, SELECT_SINGLE_PICTURE);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == SELECT_SINGLE_PICTURE){
                Uri picUri = data.getData();

                selectedImagePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", selectedImagePath);


                selectedImagePreview.setImageURI(picUri);

            }

        }

    }

    /**
     * helper to retrieve the path of an image URI
     */
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



    private void imageUpload(final String imagePath) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            String message = jObj.getString("message");

                            JSONArray jObj = new JSONArray(response);

                            String img = jObj.getString(0);

                            DataManager.serverData = img;


                           // Toast.makeText(getApplicationContext(), "Letter: "+ data +" saved...", Toast.LENGTH_LONG).show();

                            //Toast.makeText(getApplicationContext(), img, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addFile("image", imagePath);
        ServerConnect.getInstance().addToRequestQueue(smr);

    }


    public void SaveFile() {

        try {
            File myFile = new File("/sdcard/mysdfile.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            EditText toField = (EditText) findViewById(editText);//TODO where the text is pulling from
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
