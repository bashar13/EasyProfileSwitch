package com.bashar.easyprofileswitch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Jahid on 9/10/2015.
 */
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bashar.easyprofileswitch.database.SQLController;

public class EditProfileActivity extends Activity {

    private ListView listView;
    private TextView text_profile;
    LayoutInflater inflater;
    Button cancel, save;
    SQLController dbcon;
    AlertDialog selection_dialog;
    CustomListAdapter adapter;
    ImageView icon;
    public final int CATEGORY_ID = 0;
    Dialog dialog;
    int icon_id = R.drawable.ic_custom_profile;
    String profile_name;
    boolean ring_flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.edit_profile_list_view);

        listView = (ListView) findViewById(R.id.edit_list);
        text_profile = (TextView)findViewById(R.id.profile_name);
        cancel = (Button)findViewById(R.id.but_cancel);
        save = (Button)findViewById(R.id.but_save);
        icon = (ImageView)findViewById(R.id.icon);

        Intent i = getIntent();
        profile_name = i.getStringExtra("Profile_name");
        //Toast.makeText(this, "2 " + profile_name, Toast.LENGTH_LONG).show();
        text_profile.setText(profile_name);

        //createCustomActionBar();
        final String[] feature ={
                "Ringer Mode",
                "Ring Volume",
                "Alarm Volume",
                "Other Sounds",
                "WiFi",
                "Mobile Data"
        };

        final String[] set_type ={
                "Ring + Vib",
                "No Change",
                "No Change",
                "No Change",
                "No Change",
                "No Change"
        };

        adapter=new CustomListAdapter(this, feature, set_type);
        listView.setAdapter(adapter);

        dbcon = new SQLController(this);
        dbcon.open();

        text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(EditProfileActivity.this);
                View promptsView = li.inflate(R.layout.dialog_add_profile, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        EditProfileActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String tmp_name = userInput.getText().toString();
                                        if(tmp_name.equals("")) {
                                            Toast.makeText(EditProfileActivity.this, "No Name Inserted!", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                            text_profile.setText(userInput.getText().toString());
                                            profile_name = userInput.getText().toString();
                                            dialog.dismiss();
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CATEGORY_ID);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String db_pro_name = text_profile.getText().toString();
                dbcon.insertData(db_pro_name, set_type[0], set_type[1], set_type[2], set_type[3], set_type[4], set_type[5],
                        icon_id, "Set", "Set", "Set", "Set", "Set", "Set", "no", "no", "no", "no", "no", "no", "1");
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getPositionForView(view);
                //Toast.makeText(EditProfileActivity.this,pos + "",Toast.LENGTH_SHORT).show();

                switch(pos) {
                    case 0: {
                        CharSequence[] items = {" Ring + Vib ", " Vib Only ", " Ring Only ", " Silent "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setTitle("Select Ringer Mode");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        set_type[0] = "Ring + Vib";
                                        set_type[1] = "No Change";
                                        set_type[3] = "No Change";
                                        ring_flag = true;
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 1:
                                        set_type[0] = "Vib Only";
                                        set_type[1] = "Vib Only";
                                        set_type[3] = "Vib Only";
                                        ring_flag = false;
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 2:
                                        set_type[0] = "Ring Only";
                                        set_type[1] = "No Change";
                                        set_type[3] = "No Change";
                                        ring_flag = true;
                                        adapter.notifyDataSetChanged();
                                        // Your code when 3rd option seletced
                                        break;
                                    case 3:
                                        set_type[0] = "Silent";
                                        set_type[1] = "Silent";
                                        set_type[3] = "Silent";
                                        ring_flag = false;
                                        adapter.notifyDataSetChanged();
                                        // Your code when 4th  option seletced
                                        break;

                                }
                                selection_dialog.dismiss();
                            }
                        });
                        selection_dialog = builder.create();
                        selection_dialog.show();

                        break;
                    }
                    case 1: {
                        if(ring_flag) {
                            CharSequence[] items = {" Normal ", " Min ", " Max ", " No Change "};
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                            builder.setTitle("Ringing Volume Level");
                            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {


                                    switch (item) {
                                        case 0:
                                            set_type[1] = "Normal";
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            set_type[1] = "Min";
                                            adapter.notifyDataSetChanged();

                                            break;
                                        case 2:
                                            set_type[1] = "Max";
                                            adapter.notifyDataSetChanged();
                                            // Your code when 3rd option seletced
                                            break;
                                        case 3:
                                            set_type[1] = "No Change";
                                            adapter.notifyDataSetChanged();
                                            // Your code when 4th  option seletced
                                            break;

                                    }
                                    selection_dialog.dismiss();
                                }
                            });
                            selection_dialog = builder.create();
                            selection_dialog.show();
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case 2:
                    {
                        CharSequence[] items = {" Normal "," Off ", " Max ", "No Change "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setTitle("Alarm Sounds Level");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        set_type[2] = "Normal";
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 1:
                                        set_type[2] = "Off";
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 2:
                                        set_type[2] = "Max";
                                        adapter.notifyDataSetChanged();
                                        // Your code when 4th  option seletced
                                        break;
                                    case 3:
                                        set_type[2] = "No Change";
                                        adapter.notifyDataSetChanged();
                                        // Your code when 4th  option seletced
                                        break;

                                }
                                selection_dialog.dismiss();
                            }
                        });
                        selection_dialog = builder.create();
                        selection_dialog.show();
                        break;
                    }
                    case 3:
                    {
                        if(ring_flag) {
                            CharSequence[] items = {" Normal "," Min ", "Max", "No Change "};
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                            builder.setTitle("Other Sounds Level");
                            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {


                                    switch (item) {
                                        case 0:
                                            set_type[3] = "Normal";
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            set_type[3] = "Min";
                                            adapter.notifyDataSetChanged();

                                            break;
                                        case 2:
                                            set_type[3] = "Max";
                                            adapter.notifyDataSetChanged();
                                            // Your code when 4th  option seletced
                                            break;
                                        case 3:
                                            set_type[3] = "No Change";
                                            adapter.notifyDataSetChanged();
                                            // Your code when 4th  option seletced
                                            break;

                                    }
                                    selection_dialog.dismiss();
                                }
                            });
                            selection_dialog = builder.create();
                            selection_dialog.show();
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show();
                        }

                        break;
                    }
                    case 4:
                    {
                        CharSequence[] items = {" On "," Off ", "No Change "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setTitle("Select WiFi Mode");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        set_type[4] = "On";
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 1:
                                        set_type[4] = "Off";
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 2:
                                        set_type[4] = "No Change";
                                        adapter.notifyDataSetChanged();
                                        // Your code when 4th  option seletced
                                        break;

                                }
                                selection_dialog.dismiss();
                            }
                        });

                        selection_dialog = builder.create();
                        selection_dialog.show();
                        break;
                    }
                    case 5:
                    {
                        CharSequence[] items = {" On "," Off ", "No Change "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setTitle("Select Mobile-Data Mode");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        set_type[5] = "On";
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 1:
                                        set_type[5] = "Off";
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 2:
                                        set_type[5] = "No Change";
                                        adapter.notifyDataSetChanged();
                                        // Your code when 4th  option seletced
                                        break;

                                }
                                selection_dialog.dismiss();
                            }
                        });

                        selection_dialog = builder.create();
                        selection_dialog.show();
                        break;
                    }

                }
            }
        });
    }

    protected Dialog onCreateDialog(int id) {

        switch(id) {

            case CATEGORY_ID:

                AlertDialog.Builder builder;
                Context mContext = this;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog_icon_grid,(ViewGroup) findViewById(R.id.layout_root));
                GridView gridview = (GridView)layout.findViewById(R.id.gridview);
                gridview.setAdapter(new ImageAdapter(this));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(arg1.getContext(), "Position is "+arg2, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        switch(arg2) {
                            case 0:
                                icon.setImageResource(R.drawable.ic_normal);
                                icon_id = R.drawable.ic_normal;
                                break;
                            case 1:
                                icon.setImageResource(R.drawable.ic_meeting);
                                icon_id = R.drawable.ic_meeting;
                                break;
                            case 2:
                                icon.setImageResource(R.drawable.ic_silent);
                                icon_id = R.drawable.ic_silent;
                                break;
                            case 3:
                                icon.setImageResource(R.drawable.ic_prayer);
                                icon_id = R.drawable.ic_prayer;
                                break;
                            case 4:
                                icon.setImageResource(R.drawable.ic_custom_profile);
                                icon_id = R.drawable.ic_custom_profile;
                                break;
                            case 5:
                                icon.setImageResource(R.drawable.ic_home);
                                icon_id = R.drawable.ic_home;
                                break;
                            case 6:
                                icon.setImageResource(R.drawable.ic_driving);
                                icon_id = R.drawable.ic_driving;
                                break;
                            case 7:
                                icon.setImageResource(R.drawable.ic_night);
                                icon_id = R.drawable.ic_night;
                                break;
                            case 8:
                                icon.setImageResource(R.drawable.ic_outdoor);
                                icon_id = R.drawable.ic_outdoor;
                                break;
                        }
                    }
                });

                ImageView close = (ImageView) layout.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        dialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}

