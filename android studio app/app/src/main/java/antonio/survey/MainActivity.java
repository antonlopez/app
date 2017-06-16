package antonio.survey;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static FragmentManager fragmentManager;

    private static final int SELECT_SINGLE_PICTURE = 101;

    private static final int SELECT_MULTIPLE_PICTURE = 201;

    public static final String IMAGE_TYPE = "image/*";

    private ImageView selectedImagePreview;

    String selectedImagePath;

    public static String BASE_URL = "http://172.19.144.219:12345/images";



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        verifyStoragePermissions(MainActivity.this);






       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                // in onCreate or any event where your want the user to
                // select a file
               imageBrowse();
//                Intent intent = new Intent();
//                intent.setType(IMAGE_TYPE);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);

            }
        });

        findViewById(R.id.convert).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (selectedImagePath != null) {
                    imageUpload(selectedImagePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }


            }
        });



        Button template = (Button)findViewById(R.id.setTemplate);
        template.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTemplate.class);
                startActivity(i);
            }
        });



        // multiple image selection

        selectedImagePreview = (ImageView)findViewById(R.id.image_preview);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {                            // create a case to each tab

            switch(position) {

                case 0:
                    first first = new first();
                    return first;
                case 1:
                    second second = new second();
                    return second;
                case 2:
                    third third = new third();
                    return third;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
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
                Toast.makeText(getApplicationContext(), "The selected path is: "+ selectedImagePath, Toast.LENGTH_LONG).show();

                selectedImagePreview.setImageURI(picUri);

            }

        }

    }






//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_SINGLE_PICTURE) {
//
//                Uri selectedImageUri = data.getData();
//                selectedImagePath = getRealPathFromURI(selectedImageUri);
//                //selectedImagePath = getPath(selectedImageUri);
//                try {
//                    selectedImagePreview.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
//                } catch (IOException e) {
//                    Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
//                }
//                Toast.makeText(MainActivity.this, "this is the path" + selectedImagePath, Toast.LENGTH_LONG).show();
//
//                // original code
////                String selectedImagePath = getPath(selectedImageUri);
////                selectedImagePreview.setImageURI(selectedImageUri);
//            }
//            else if (requestCode == SELECT_MULTIPLE_PICTURE) {
//                //And in the Result handling check for that parameter:
//                if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction())
//                        && data.hasExtra(Intent.EXTRA_STREAM)) {
//                    // retrieve a collection of selected images
//                    ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//                    // iterate over these images
//                    if( list != null ) {
//                        for (Parcelable parcel : list) {
//                            Uri uri = (Uri) parcel;
//                            // handle the images one by one here
//                        }
//                    }
//
//                    // for now just show the last picture
//                    if( !list.isEmpty() ) {
//                        Uri imageUri = (Uri) list.get(list.size() - 1);
//
//                        try {
//                            selectedImagePreview.setImageBitmap(new UserPicture(imageUri, getContentResolver()).getBitmap());
//                        } catch (IOException e) {
//                            Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
//                        }
//                        // original code
//                       String selectedImagePath = getPath(imageUri);
//                       selectedImagePreview.setImageURI(imageUri);
////                       displayPicture(selectedImagePath, selectedImagePreview);
//                    }
//                }
//            }
//        } else {
//            // report failure
//            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
//            Log.d(MainActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
//        }
//    }

    /*
     * helper to retrieve the path of an image URI
     */
//    public String getPath(Uri uri) {
//
//        // just some safety built in
//        if( uri == null ) {
//            // perform some logging or show user feedback
//            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_picture, Toast.LENGTH_LONG).show();
//            Log.d(MainActivity.class.getSimpleName(), "Failed to parse image path from image URI " + uri);
//            return null;
//        }
//
//        // try to retrieve the image from the media store first
//        // this will only work for images selected from gallery
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = MainActivity.this.getContentResolver().query(uri, projection, null, null, null);
//        if( cursor != null ){
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//
//            return cursor.getString(column_index);
//        }
//        // this is our fallback here, thanks to the answer from @mad indicating this is needed for
//        // working code based on images selected using other file managers
//        return uri.getPath();
//    }



    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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


//                            String x_dim = jObj.getString(1);
//                            String x_start = jObj.getString(2);
//                            String y_dim = jObj.getString(3);
//                            String y_start= jObj.getString(4);



                            Toast.makeText(getApplicationContext(), img, Toast.LENGTH_LONG).show();

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





}
