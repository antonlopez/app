package antonio.survey;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AddTemplate extends AppCompatActivity {

    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private static FragmentManager fragmentManager;

    private static final int SELECT_SINGLE_PICTURE = 101;

    private static final int SELECT_MULTIPLE_PICTURE = 201;

    public static final String IMAGE_TYPE = "image/*";

    private ImageView selectedImagePreview;

    String selectedImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);

        findViewById(R.id.ImageTemplateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
            }
        });




        //This gets the data from the textbox




        selectedImagePreview = (ImageView)findViewById(R.id.imageView);




        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                EditText toField = (EditText) findViewById(R.id.editText);
                String data = toField.getText().toString();

                ArrayList arrayList = new ArrayList();


                // LetterTemplates letters = new LetterTemplates(data, img);

                Intent i = new Intent(AddTemplate.this, MainActivity.class);
                startActivity(i);


                //this button will push the image to the database and store it with the
                // cooresponding letter saving it in the process
            }
        });



    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                try {
                    selectedImagePreview.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                } catch (IOException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
                }
                Toast.makeText(AddTemplate.this, "this is the path" + selectedImagePath, Toast.LENGTH_LONG).show();

                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }
            else if (requestCode == SELECT_MULTIPLE_PICTURE) {
                //And in the Result handling check for that parameter:
                if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction())
                        && data.hasExtra(Intent.EXTRA_STREAM)) {
                    // retrieve a collection of selected images
                    ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                    // iterate over these images
                    if( list != null ) {
                        for (Parcelable parcel : list) {
                            Uri uri = (Uri) parcel;
                            // handle the images one by one here
                        }
                    }

                    // for now just show the last picture
                    if( !list.isEmpty() ) {
                        Uri imageUri = (Uri) list.get(list.size() - 1);

                        try {
                            selectedImagePreview.setImageBitmap(new UserPicture(imageUri, getContentResolver()).getBitmap());
                        } catch (IOException e) {
                            Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
                        }
                        // original code
                        String selectedImagePath = getPath(imageUri);
                        selectedImagePreview.setImageURI(imageUri);
//                        displayPicture(selectedImagePath, selectedImagePreview);
                    }
                }
            }
        } else {
            // report failure
            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
            Log.d(MainActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {

        // just some safety built in
        if( uri == null ) {
            // perform some logging or show user feedback
            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_picture, Toast.LENGTH_LONG).show();
            Log.d(MainActivity.class.getSimpleName(), "Failed to parse image path from image URI " + uri);
            return null;
        }

        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here, thanks to the answer from @mad indicating this is needed for
        // working code based on images selected using other file managers
        return uri.getPath();
    }
}
