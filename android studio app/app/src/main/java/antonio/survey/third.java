package antonio.survey;

/**
 * Created by antonio on 1/3/17.
 */


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class third extends Fragment {

    TextView name, age, major, happiness, bibleKnowledge, needGod, phoneNumber, emailAddress, phonelabel, emailLabel;



    public third(){
       // empty constructor
         }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final  View rootView = inflater.inflate(R.layout.third, container, false);


        // Display information
        name = (TextView) rootView.findViewById(R.id.nameDisplay);
        age = (TextView) rootView.findViewById(R.id.ageDisplay);
        major = (TextView) rootView.findViewById(R.id.majorDisplay);
        happiness = (TextView) rootView.findViewById(R.id.happinessDisplay);
        bibleKnowledge = (TextView) rootView.findViewById(R.id.bibleKnowledgeDisplay);
        needGod = (TextView) rootView.findViewById(R.id.GodDisplay);
        phoneNumber = (TextView) rootView.findViewById(R.id.phoneDisplay);
        emailAddress = (TextView) rootView.findViewById(R.id.emailDIsplay);

        phonelabel = (TextView) rootView.findViewById(R.id.phoneLabel);
        emailLabel = (TextView) rootView.findViewById(R.id.emailLabel);



        phoneNumber.setVisibility(rootView.GONE);
        phonelabel.setVisibility(rootView.GONE);
        emailLabel.setVisibility(rootView.GONE);
        emailAddress.setVisibility(rootView.GONE);


        if(DataManager.info = true){

            phoneNumber.setVisibility(rootView.VISIBLE);
            phonelabel.setVisibility(rootView.VISIBLE);
            emailLabel.setVisibility(rootView.VISIBLE);
            emailAddress.setVisibility(rootView.VISIBLE);




        }




        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Snackbar.make(view, "Information saved...", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();



                 final RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.rela);

                 layout.post(new Runnable() {
                     @Override
                     public void run() {
                         Bitmap pic = takeScreenShot(layout);

                         try{

                            if(pic != null){

                               SaveScreenShot(pic);
                             }


                         }catch (Exception e){
                             e.printStackTrace();
                         }

                     }
                 });

            }
        });







        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle SavedInstanceState){
        super.onActivityCreated(SavedInstanceState);

        name.setText(DataManager.name);
        age.setText(DataManager.age);
        major.setText(DataManager.major);
        phoneNumber.setText(DataManager.phoneNumber);
        emailAddress.setText(DataManager.emailAddress);
        happiness.setText(String.valueOf(DataManager.happiness));
        bibleKnowledge.setText(String.valueOf(DataManager.BibleKnowledge));
        needGod.setText(String.valueOf(DataManager.believeinGod));



    }





   // Take screenshot
    private Bitmap takeScreenShot(View v){

            Bitmap screenShot = null;

        try{

                  int width = v.getMeasuredWidth();
                  int height = v.getMeasuredHeight();

            screenShot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            Canvas c = new Canvas(screenShot);
            v.draw(c);
        }catch(Exception e){

            e.printStackTrace();
        }
      return screenShot;
    }


   //Save screenshot

    private void SaveScreenShot(Bitmap bm){

        ByteArrayOutputStream bao = null;
        File file = null;

        try{

            bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 80, bao );


            file = new File(Environment.getExternalStorageDirectory()+"/informarion.PNG");
            //file = new File(Environment.getDataDirectory()+File.separator+"/first.PNG");
            file.createNewFile();

            FileOutputStream os = new FileOutputStream(file);

            os.write(bao.toByteArray());
            os.close();


        }catch (Exception e){
            e.printStackTrace();
        }



    }




















}
