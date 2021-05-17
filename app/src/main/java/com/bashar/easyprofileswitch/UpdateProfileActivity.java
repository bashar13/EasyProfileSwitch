package com.bashar.easyprofileswitch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.bashar.easyprofileswitch.database.DBhelper;
import com.bashar.easyprofileswitch.database.SQLController;

public class UpdateProfileActivity extends Activity {
    SQLController dbcon;
    ListView listView;
    AlertDialog selection_dialog;
    CustomListAdapter adapter;
    String profile_name;
    public final int CATEGORY_ID = 0;
    Dialog dialog;
    int icon_id = R.drawable.ic_custom_profile;
    ImageView icon;
    boolean ring_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_profile_list_view);

        icon = (ImageView)findViewById(R.id.icon);
        final TextView profile_n = (TextView)findViewById(R.id.profile_name);
        listView = (ListView)findViewById(R.id.edit_list);
        Button cancel = (Button)findViewById(R.id.but_cancel);
        Button save = (Button)findViewById(R.id.but_save);

        Intent i = getIntent();
        final int profile_id = i.getIntExtra("Profile_id", 1);
        dbcon = new SQLController(this);
        dbcon.open();
        Cursor c = dbcon.readSpecificData(DBhelper.TABLE_PROFILE, profile_id);
        profile_name = c.getString(1);
        profile_n.setText(profile_name);
        String profile_image = c.getString(8);
        icon_id = Integer.parseInt(profile_image);
        icon.setImageResource(icon_id);
        final String feature[] = {"Ringer Mode", "Ring Volume", "Alarm Volume", "Other Sounds", "WiFi", "Mobile Data"};
        final String set_type[] = {c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)};
        if(c.getString(2).equals("Vib Only") || c.getString(2).equals("Silent"))
            ring_flag = false;

        adapter=new CustomListAdapter(this, feature, set_type);
        listView.setAdapter(adapter);

        profile_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(UpdateProfileActivity.this);
                View promptsView = li.inflate(R.layout.dialog_add_profile, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        UpdateProfileActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String tmp_name = userInput.getText().toString();
                                        if (tmp_name.equals("")) {
                                            Toast.makeText(UpdateProfileActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                        } else {
                                            //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                            profile_n.setText(userInput.getText().toString());
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
                dbcon.updateData(profile_id, profile_name, set_type[0], set_type[1], set_type[2], set_type[3], set_type[4], set_type[5], icon_id);
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getPositionForView(view);
                //Toast.makeText(UpdateProfileActivity.this, pos + "", Toast.LENGTH_SHORT).show();
                switch(pos) {
                    case 0: {
                        CharSequence[] items = {" Ring + Vib ", " Vib Only ", " Ring Only ", " Silent "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                        builder.setTitle("Select Ringer Mode");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        set_type[0] = "Ring + Vib";
                                        set_type[1] = "No Change";
                                        set_type[3] = "No Change";
                                        adapter.notifyDataSetChanged();
                                        ring_flag = true;
                                        break;
                                    case 1:
                                        set_type[0] = "Vib Only";
                                        set_type[1] = "Vib Only";
                                        set_type[3] = "Vib Only";
                                        adapter.notifyDataSetChanged();
                                        ring_flag = false;

                                        break;
                                    case 2:
                                        set_type[0] = "Ring Only";
                                        set_type[1] = "No Change";
                                        set_type[3] = "No Change";
                                        adapter.notifyDataSetChanged();
                                        ring_flag = true;
                                        // Your code when 3rd option seletced
                                        break;
                                    case 3:
                                        set_type[0] = "Silent";
                                        set_type[1] = "Silent";
                                        set_type[3] = "Silent";
                                        adapter.notifyDataSetChanged();
                                        ring_flag = false;
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
                    case 1:
                    {
                        if(ring_flag) {
                            CharSequence[] items = {" Normal ", " Min ", " Max ", " No Change "};
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                            Toast.makeText(UpdateProfileActivity.this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show();
                        }

                        break;

                    }

                    case 2:
                    {
                        CharSequence[] items = {" Normal "," Off ", " Max ", "No Change "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                            Toast.makeText(UpdateProfileActivity.this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show();
                        }

                        break;
                    }
                    case 4:
                    {
                        CharSequence[] items = {" On "," Off ", "No Change "};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_profile, menu);
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
