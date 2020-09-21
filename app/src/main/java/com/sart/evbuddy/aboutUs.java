package com.sart.evbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.CalendarContract;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;



public class aboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Element adsElement = new Element();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                //.setCustomFont()
                .setImage(R.drawable.splashlogo)
                .setDescription("In India, as there are very few charging stations available, there is no choice to E-Vehicle users to have a realtime system that will help him to find charging stations nearby him.\n\nEV-Buddy will help EV user by solving this issue and giving him a choice to check his vehicle's bettery percentage.\n\nThis application is developed under RIT's Autonomous Electric Vehicle Project.\n\nThanks for guidance :\nMr. Sham Gurav\nMr. K.S. Kulkarni\n\nApplication developed by Shubham Bhosale.\n\nOther Contributers: \nAmit Gaikwad\nRasika Jadhav\nTejashri Bhajibhakare")
                .addItem(new Element().setTitle("EV Buddy! - Electric Vehicle Charging Stations"))
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("bhosale.shubham48@gmail.com")
                .addWebsite("https://www.ritindia.edu/")
                .addYoutube("UCakEjL76YB8vShOKU9Kmm5Q")   //Enter your youtube link here (replace with my channel link)
                .addPlayStore("com.sart.evbuddy")   //Replace all this with your package name
                //.addInstagram("jarves.usaram")    //Your instagram id
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Shubham Bhosale", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aboutUs.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
