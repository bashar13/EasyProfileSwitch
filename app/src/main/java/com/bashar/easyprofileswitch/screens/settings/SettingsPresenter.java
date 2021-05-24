package com.bashar.easyprofileswitch.screens.settings;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.bashar.easyprofileswitch.R;
import com.bashar.easyprofileswitch.models.Category;
import com.bashar.easyprofileswitch.models.Profile;
import com.bashar.easyprofileswitch.models.SubCategory;
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository;
import com.bashar.easyprofileswitch.screens.settings.ExpandableListViewAdapter;
import com.bashar.easyprofileswitch.screens.settings.SettingsContract;
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class SettingsPresenter implements SettingsContract.Presenter {

    private Context context;
    private SharedPreferenceRepository sharedPref;
    private ProfileRepository profileRepo;

    private SettingsContract.View view = null;

    private ExpandableListViewAdapter adapter;
    private ArrayList<Category> categoryArray = new ArrayList<>();

    @Inject
    public SettingsPresenter(Context context, SharedPreferenceRepository sharedPref, ProfileRepository profileRepo) {
        this.context = context;
        this.sharedPref = sharedPref;
        this.profileRepo = profileRepo;
    }
    @Override
    public void displayCurrentSettings() {
        String[] normal_values = new String[] {
                " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 "
        };
        String[] min_values = new String[] {
                " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 "
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_vol_custom_, normal_values);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_vol_custom_, min_values);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        view.updateSettingsView(adapter1, adapter2, sharedPref.getMinVolumeLevel(), sharedPref.getNormalVolumeLevel());
    }

    @Override
    public void displayProfileSettings(ExpandableListView eListView) {
        String[] profileSettins = {"Delay Timer", "At Timer End", "Start Time 1", "Start Time 2", "Start Time 3", "Start Time 4", "Start Time 5"};
        ArrayList<Profile> profileList= profileRepo.getProfileList();

        for (int i=0; i<profileList.size(); i++){
            Category category = new Category();
            category.category_name = profileList.get(i).getName();
            category.category_id = String.valueOf(profileList.get(i).getProfileId());

            for(int j=0; j<7; j++) {
                SubCategory subCategory = new SubCategory();
                subCategory.subcategory_name = profileSettins[j];

                switch (j) {
                    case 0:
                        subCategory.subcategory_value = profileList.get(i).getDelay();
                        subCategory.selected = profileList.get(i).getDelaySelect().equals("yes");
                        break;
                    case 1:
                        String afterTimerProfile = profileList.get(i).getAfterTimer();
                        Profile profile = profileRepo.getProfile(Integer.valueOf(afterTimerProfile));
                        subCategory.subcategory_value = profile.getName();
                        subCategory.selected = true;
                        break;
                    case 2:
                        subCategory.subcategory_value = profileList.get(i).getStart1();
                        subCategory.selected = profileList.get(i).getStart1Select().equals("yes");
                        break;
                    case 3:
                        subCategory.subcategory_value = profileList.get(i).getStart2();
                        subCategory.selected = profileList.get(i).getStart2Select().equals("yes");
                        break;
                    case 4:
                        subCategory.subcategory_value = profileList.get(i).getStart3();
                        subCategory.selected = profileList.get(i).getStart3Select().equals("yes");
                        break;
                    case 5:
                        subCategory.subcategory_value = profileList.get(i).getStart4();
                        subCategory.selected = profileList.get(i).getStart4Select().equals("yes");
                        break;
                    case 6:
                        subCategory.subcategory_value = profileList.get(i).getStart5();
                        subCategory.selected = profileList.get(i).getStart5Select().equals("yes");
                        break;
                    default:
                        break;
                }
                category.subcategory_array.add(subCategory);
            }
            categoryArray.add(category);
        }
        adapter = new ExpandableListViewAdapter(context, eListView, categoryArray);
        view.updateProfileSettingsView(adapter);
    }

    @Override
    public void updateProfileSchedule(int id, int position, String select) {
        profileRepo.updateProfileSchedule(id, position, select);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveNormalVolumeLevel(int level) {
        sharedPref.storeNormalVolumeLevel(level);

    }

    @Override
    public void saveMinVolumeLevel(int level) {
        sharedPref.storeMinVolumeLevel(level);
    }

    @Override
    public void unregister() {
        view = null;

    }

    @Override
    public void register(SettingsContract.View view) {
        this.view = view;

    }

    ArrayList<Category> getCategoryList() {
        return categoryArray;
    }

    @Override
    public Calendar setStartTime(String time) {
        int hour = Integer.parseInt((new StringBuilder(String.valueOf(Character.toString(time.charAt(0)))))
                .append(Character.toString(time.charAt(1))).toString());
        int min = Integer.parseInt((new StringBuilder(String.valueOf(Character.toString(time.charAt(3)))))
                .append(Character.toString(time.charAt(4))).toString());

        String time_f = (new StringBuilder(String.valueOf(Character.toString(time.charAt(6)))))
                .append(Character.toString(time.charAt(7))).toString();
        //Toast.makeText(getActivity(),time_f,Toast.LENGTH_LONG).show();
        if (!time_f.equals("PM") && !time_f.equals("pm")) {
            if (hour == 12) {
                //Toast.makeText(getActivity(),"am",Toast.LENGTH_LONG).show();
                hour = 0;
            }
        }
        else {
            if (hour < 12) {
                //Toast.makeText(getActivity(),time_f + "+ pm",Toast.LENGTH_LONG).show();
                hour += 12;
            }
        }
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        if (System.currentTimeMillis() > calendar.getTimeInMillis())
        {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar;
    }
}
