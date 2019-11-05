package com.example.android.done;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeAdapter.MyClickListener, InfoAdapter.MyClickListener, MyAdapter.MyClickListener {
    private TextView dispDay;
    private TextView dispMY;
    private TextView dispDate;

    private String Month(String month) {
        if (month.equals("0")) {
            return "Jan";
        }
        else if (month.equals("1")) {
            return "Feb";
        }
        else if (month.equals("2")) {
            return "Mar";
        }
        else if (month.equals("3")) {
            return "Apr";
        }
        else if (month.equals("4")) {
            return "May";
        }
        else if (month.equals("5")) {
            return "Jun";
        }
        else if (month.equals("6")) {
            return "Jul";
        }
        else if (month.equals("7")) {
            return "Aug";
        }
        else if (month.equals("8")) {
            return "Sep";
        }
        else if (month.equals("9")) {
            return "Oct";
        }
        else if (month.equals("10")) {
            return "Nov";
        }
        else {
            return "Dec";
        }
    }

    private String Weekday (int a) {
        if (a == 1) {
            return "Sun";
        }
        else if (a == 5) {
            return "Thur";
        }
        else if (a == 3) {
            return "Tue";
        }
        else if (a == 4) {
            return "Wed";
        }
        else if (a == 2) {
            return "Mon";
        }
        else if (a == 6) {
            return "Fri";
        }
        else {
            return "Sat";
        }
    }

    private String categoryName;
    private String edit_categoryName;
    private String taskName;
    private String taskDescription;
    private String titleList[];

    private ArrayList<Info> currentMode;
    private final ArrayList<Info> categoryInfo = new ArrayList<Info>();
    private final ArrayList<ArrayList<Info>> tasksInfo = new ArrayList<ArrayList<Info>>();
    private ArrayList<Info> mixedTasksInfo = new ArrayList<Info>();
    private ArrayList<Info> completedTasksInfo = new ArrayList<Info>();

    private boolean[] Boolean;

    private LinearLayout setClock;

    private MyAdapter adapter;
    private InfoAdapter adapter_copy;
    private InfoAdapter adapter1;
    private InfoAdapter adapter2;
    private ExpandableListView taskslist;
    private ListView mixedTasksList;
    private ListView completedTasksList;
    private Info newInfo;
    private Info newInfoMixed;
    private Info editCat;

    private boolean categorizedMode = true;
    private boolean mixedMode = false;
    private boolean finishedMode = false;

    private boolean isFabOpen = false;
    private boolean isFabOpen1 = false;
    private boolean disableFabOpen = false;
    private boolean disableFabOpen1 = false;
    private boolean multiple = true;
    private FloatingActionButton mainfab;
    private FloatingActionButton fabswitch;
    private FloatingActionButton fabadd;
    private FloatingActionButton fabrefresh;
    private FloatingActionButton fabcheck;
    private FloatingActionButton fabclear;
    private Animation fab_open_up;
    private Animation fab_open_left;
    private Animation fab_close_up;
    private Animation fab_close_left;

    private Map<String, Integer> catDictionary;

    private ArrayList<Info> currentCategoryTasksList;
    private ArrayList<Info> currentMixedList;

    private Bitmap finalBlurBg;
    private CoordinatorLayout Bg;

    private Anim xhAnim;

    private long[] mHits = new long[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,ExpandableListViewActivity.class));

        Bg = (CoordinatorLayout)findViewById(R.id.Bg);
        Bg.getForeground().setAlpha(0);

        xhAnim = new Anim(this);

        Boolean = new boolean[31];
        for (int i = 1; i < 31; i++) {
            Boolean[i] = false;
        }

        //Date currentTime = Calendar.getInstance().getTime();
        final Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String day = Integer.toString(mDay);
        String month = Integer.toString(mMonth);
        String year = Integer.toString(mYear);
        String monthWord = Month(month);
        String weekday = Weekday(dayOfWeek);

        dispDay = (TextView)findViewById(R.id.day);
        dispDay.setText(day);
        dispMY = (TextView)findViewById(R.id.date);
        dispMY.setText(monthWord+"\n"+year);
        dispDate = (TextView)findViewById(R.id.right_text);
        dispDate.setText(weekday);
        adapter = new MyAdapter(MainActivity.this, categoryInfo, tasksInfo, mixedTasksInfo, completedTasksInfo,this);
        //adapter_copy = new InfoAdapter(MainActivity.this, tasksInfo, this);
        adapter1 = new InfoAdapter(MainActivity.this, mixedTasksInfo, this);
        adapter2 = new InfoAdapter(MainActivity.this, completedTasksInfo, this);
        taskslist = (ExpandableListView)findViewById(R.id.list);
        //taskslist.setRemoveListener(this);
        mixedTasksList = (ListView) findViewById(R.id.mixedlist);
        completedTasksList = (ListView)findViewById(R.id.completelist);

        mainfab = (FloatingActionButton)findViewById(R.id.mainfab);
        fabswitch = (FloatingActionButton)findViewById(R.id.switchmode);
        fabadd = (FloatingActionButton)findViewById(R.id.addcat);
        fabrefresh = (FloatingActionButton)findViewById(R.id.refresh);
        fabcheck = (FloatingActionButton)findViewById(R.id.checkCompletedTasks);
        fabclear = (FloatingActionButton)findViewById(R.id.clear);
        fab_open_up = AnimationUtils.loadAnimation(this, R.anim.fab_open_up);
        fab_close_up = AnimationUtils.loadAnimation(this,R.anim.fab_close_up);
        fab_open_left = AnimationUtils.loadAnimation(this, R.anim.fab_open_left);
        fab_close_left = AnimationUtils.loadAnimation(this,R.anim.fab_close_left);

        catDictionary = new HashMap<String, Integer>();
        currentMixedList = new ArrayList<Info>();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
        {
            finish();
        }
        return super.dispatchKeyEvent(event);
    }

    public void mainmenu(View v) {
        animateFAB();
    }

    public void checkFinished(View v) {
        animateFAB1();
        completedTasksList.setAdapter(adapter2);
        if (!finishedMode) {
            completedTasksList.setVisibility(View.VISIBLE);
            taskslist.setVisibility(View.GONE);
            mixedTasksList.setVisibility(View.GONE);
            finishedMode = !finishedMode;
            categorizedMode = false;//false
            mixedMode = true;
        }
        else {//categorizedMode == true, mixedMode == false, finishedMode == false
            taskslist.setVisibility(View.VISIBLE);
            completedTasksList.setVisibility(View.GONE);
            mixedTasksList.setVisibility(View.GONE);
            finishedMode = !finishedMode;
            categorizedMode = true;
            mixedMode = false;
        }
        Log.v("categorizedMode1", String.valueOf(categorizedMode));
        Log.v("mixedMode1", String.valueOf(mixedMode));
    }

    public void clearFinishedList(View v) {
        completedTasksInfo.clear();
        completedTasksList.setAdapter(adapter2);
    }

    public void animateFAB(){
        if (isFabOpen) {
            fabswitch.startAnimation(fab_close_up);
            fabadd.startAnimation(fab_close_up);
            fabrefresh.startAnimation(fab_close_up);
            isFabOpen = false;
        } else {
            fabswitch.startAnimation(fab_open_up);
            fabadd.startAnimation(fab_open_up);
            fabrefresh.startAnimation(fab_open_up);
            isFabOpen = true;
        }
    }

    public void animateFAB1() {
        if (isFabOpen1) {
            Log.v("isFabOpen1", "isFabOpen1");
            fabclear.startAnimation(fab_close_left);
            isFabOpen1 = false;
        } else{
            Log.v("else1", "else1");
            fabclear.startAnimation(fab_open_left);
            isFabOpen1 = true;
        }
    }

    public ArrayList<Info> findSubArrayList(ArrayList<Info> ArrayList, int index1, int index2) {
        ArrayList<Info> subArrayList = new ArrayList<Info>();
        if (index1 == index2) {return subArrayList; }
        for (int i = index1; i < index2; i++) { subArrayList.add(ArrayList.get(i)); }
        return subArrayList;
    }

    public ArrayList<Info> concatenate(ArrayList<Info> ArrayList1, ArrayList<Info> ArrayList2) {
        for (int i = 0; i < ArrayList2.size(); i++) {
            ArrayList1.add(ArrayList2.get(i));
        }
        return ArrayList1;
    }

    public ArrayList<Info> addAndInsert(ArrayList<Info> ArrayList, Info currTask) {
        //Log.v("addAndInsert", "entered?");
        //Log.v("Month", String.valueOf(currTask.getMonth()));
        //Log.v("Day", String.valueOf(currTask.getDay()));
        //Log.v("Hour", String.valueOf(currTask.getHour()));
        //Log.v("Minute", String.valueOf(currTask.getMinute()));
        int size = ArrayList.size();
        int mid = size / 2;
        if (ArrayList.isEmpty()) { ArrayList.add(currTask); return ArrayList; }
        else if (size == 1) {
            Log.v("size == 1", "here?");
            Info firstInfo = ArrayList.get(0);
            if (currTask.getMonth() < firstInfo.getMonth()) {ArrayList.add(0, currTask); }
            else if (currTask.getMonth() > firstInfo.getMonth()) {ArrayList.add(currTask); }
            else if (currTask.getMonth() == firstInfo.getMonth() &  currTask.getDay() < firstInfo.getDay()) {ArrayList.add(0, currTask); }
            else if (currTask.getMonth() == firstInfo.getMonth() &  currTask.getDay() > firstInfo.getDay()) {ArrayList.add(currTask); }
            else if (currTask.getMonth() == firstInfo.getMonth() &  currTask.getDay() == firstInfo.getDay()
                    & currTask.getHour() < firstInfo.getHour()) {ArrayList.add(0, currTask); }
            else if (currTask.getMonth() == firstInfo.getMonth() &  currTask.getDay() == firstInfo.getDay()
                    & currTask.getHour() > firstInfo.getHour()) {ArrayList.add(currTask); }
            else if (currTask.getMonth() == firstInfo.getMonth() &  currTask.getDay() == firstInfo.getDay()
                    & currTask.getHour() == firstInfo.getHour() & currTask.getMinute() < firstInfo.getMinute()) {ArrayList.add(0, currTask); }
            else {ArrayList.add(currTask);
            Log.v("result", String.valueOf(ArrayList.size()));}
            return ArrayList;}
        else if (currTask.getMonth() == 13) {
            Log.v("getMonth = 13", "or here?");
            ArrayList.add(currTask);
            return ArrayList;
        }
        else {
            Log.v("else", "really?");
            Info midInfo = ArrayList.get(size / 2);
            Log.v("Month", String.valueOf(midInfo.getMonth()));
            Log.v("Day", String.valueOf(midInfo.getDay()));
            Log.v("Hour", String.valueOf(midInfo.getHour()));
            Log.v("Minute", String.valueOf(midInfo.getMinute()));
            if (currTask.getMonth() < midInfo.getMonth()) {
                Log.v("=", "=");
                return concatenate(addAndInsert(findSubArrayList(ArrayList, 0, mid), currTask),
                        findSubArrayList(ArrayList, mid, size));
                }
            else if (currTask.getMonth() > midInfo.getMonth()) {
                Log.v(">", ">");
                return concatenate(findSubArrayList(ArrayList, 0, mid + 1),
                        addAndInsert(findSubArrayList(ArrayList, mid + 1, size), currTask));
                }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() < midInfo.getDay()) {
                Log.v("= <", "< =");
                return concatenate(addAndInsert(findSubArrayList(ArrayList, 0, mid), currTask),
                        findSubArrayList(ArrayList, mid, size));
                }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() > midInfo.getDay()) {
                Log.v("=", ">");
                Log.v("updated taskslist", String.valueOf(concatenate(findSubArrayList(ArrayList, 0, mid + 1),
                        addAndInsert(findSubArrayList(ArrayList, mid + 1, size), currTask)).size()));
                return concatenate(findSubArrayList(ArrayList, 0, mid + 1),
                        addAndInsert(findSubArrayList(ArrayList, mid + 1, size), currTask));
                }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() == midInfo.getDay() &
                    currTask.getHour() < midInfo.getHour()) {
                Log.v("= = <", "= = <");
                return concatenate(addAndInsert(findSubArrayList(ArrayList, 0, mid), currTask),
                        findSubArrayList(ArrayList, mid, size));
                }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() == midInfo.getDay() &
                    currTask.getHour() > midInfo.getHour()) {
                Log.v("= = >", "= = >");
                return concatenate(findSubArrayList(ArrayList, 0, mid + 1),
                        addAndInsert(findSubArrayList(ArrayList, mid + 1, size), currTask));
            }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() == midInfo.getDay() &
                    currTask.getHour() == midInfo.getHour() & currTask.getMinute() < midInfo.getMinute()) {
                Log.v("= = = <", "= = = <");
                return concatenate(addAndInsert(findSubArrayList(ArrayList, 0, mid), currTask),
                        findSubArrayList(ArrayList, mid, size));
            }
            else if (currTask.getMonth() == midInfo.getMonth() & currTask.getDay() == midInfo.getDay() &
                    currTask.getHour() == midInfo.getHour() & currTask.getMinute() > midInfo.getMinute()) {
                Log.v("= = = >", "= = = >");
                return concatenate(findSubArrayList(ArrayList, 0, mid + 1),
                        addAndInsert(findSubArrayList(ArrayList, mid + 1, size), currTask));
            }
            else {
                Log.v("else", "else");
                ArrayList.add(mid + 1, currTask);
                return ArrayList;
            }
        }

    }

    public void onButtonShowPopupWindowClick(View v) {
        //ArrayList<Info> some = new ArrayList<Info>();
        //some.add(new Info("else", R.id.color2, "okay", 12, 30, 12, 30, true));
        //tasksInfo.add(some);
        final View v1 = v;
        Log.v("button", v1.toString());
        //////////////////////////// taskpopupwindow //////////////////////////////////////////
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup, null, false);
        FloatingActionButton addTask = (FloatingActionButton) popupView.findViewById(R.id.checkmark);
        FloatingActionButton cancelTask = (FloatingActionButton) popupView.findViewById(R.id.cancel);
        ViewPager TaskInfo = (ViewPager)popupView.findViewById(R.id.viewpager);
        LinearLayout sliderpanel = (LinearLayout)popupView.findViewById(R.id.slidebars);

        Log.v("categoryInfo@@@@@@@@@@@", categoryInfo.toString());
        Log.v("getTag", v1.getTag().toString());
        final String currentCategory = categoryInfo.get((Integer) v1.getTag()).getTitle();
        Log.v("currentCategory", currentCategory);

        //////////////////////// adapt to the viewpager of popupwindow ////////////////////////
        ArrayList aList = new ArrayList<View>();
        LayoutInflater info = getLayoutInflater();
        final View describeView = info.inflate(R.layout.description,null,false);
        View clockView = info.inflate(R.layout.clock,null,false);
        // View colorView = info.inflate(R.layout.color,null,false);
        View dateView = info.inflate(R.layout.date,null,false);
        aList.add(describeView);
        //aList.add(colorView);
        aList.add(dateView);
        aList.add(clockView);
        MiniPagerAdapter mAdapter = new MiniPagerAdapter(aList);
        TaskInfo.setAdapter(mAdapter);

        /////////////////////// add bar indicators ///////////////////////////////
        final int barscount = mAdapter.getCount();
        final ImageView[] bars = new ImageView[barscount];
        for (int i = 0; i < barscount; i++) {
            bars[i] = new ImageView(this);
            bars[i].setBackgroundResource(R.drawable.inactive_indicator);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderpanel.addView(bars[i], params);
        }
        bars[0].setBackgroundResource(R.drawable.active_indicator);

        /////////////////////// viewpager onpagechangelistener redefine /////////////////////////
        TaskInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< barscount; i++){
                    bars[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactive_indicator)); }
                bars[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_indicator)); }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ////////////////////// time picker, set clock ////////////////////////////
        final DatePicker date = (DatePicker) dateView.findViewById(R.id.datepicker);
        final TimePicker time = (TimePicker) clockView.findViewById(R.id.timepicker);
        time.setIs24HourView(true);
        final Calendar c1 = Calendar.getInstance();

        //////////////////////// create the popup window ////////////////////////////////
        //final int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        //int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Bg.getForeground().setAlpha(120);
        //popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        popupWindow.update();
        int index1 = (Integer) v1.getTag();
        int index2 = catDictionary.get(currentCategory);
        final String itsCategory = categoryInfo.get(index1).getTitle(); ///put every category title into the task item,
        /// but only in categorized mode needed
        ///////////////////// a new task is added, display on mainpage ////////////////////////////
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                // store the edit text content into a string
                EditText title = (EditText) popupView.findViewById(R.id.Caption);
                title.setInputType(InputType.TYPE_CLASS_TEXT);
                taskName = title.getText().toString();
                EditText describe =(EditText) describeView.findViewById(R.id.Description);
                describe.setInputType(InputType.TYPE_CLASS_TEXT);
                taskDescription = describe.getText().toString();
                int index1 = (Integer) v1.getTag();
                int index2 = catDictionary.get(currentCategory);
                //Log.v("getMonth", String.valueOf(date.getMonth()));
                //Log.v("getMonth1", String.valueOf(c1.get(Calendar.MONTH)));
                //Log.v("getDayOfMonth", String.valueOf(date.getDayOfMonth()));
                //Log.v("getDayOfMonth1", String.valueOf(c1.get(Calendar.DAY_OF_MONTH)));
                //Log.v("getHour", String.valueOf(time.getHour()));
                //Log.v("getHour1", String.valueOf(c1.get(Calendar.HOUR_OF_DAY)));
                //Log.v("getMinute", String.valueOf(time.getMinute()));
                //Log.v("getMinute", String.valueOf(c1.get(Calendar.MINUTE)));
                if (date.getMonth() == c1.get(Calendar.MONTH) &
                    date.getDayOfMonth() == c1.get(Calendar.DAY_OF_MONTH) &
                    time.getHour() == c1.get(Calendar.HOUR_OF_DAY) &
                    time.getMinute() == c1.get(Calendar.MINUTE)) {
                    newInfo = new Info(taskName, itsCategory, R.drawable.reccolor0, categoryInfo.get(index1).getBgColor(),
                            taskDescription, 13, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    newInfoMixed = new Info(taskName, categoryInfo.get(index1).getBgColor(),
                        taskDescription, 13, date.getDayOfMonth(), time.getHour(),
                        time.getMinute(), false);}
                    //Log.v("hour", String.valueOf(hour.getValue()));
                    //Log.v("minute", String.valueOf(minute.getValue()));}
                else {
                    newInfo = new Info(taskName, itsCategory, R.drawable.reccolor0, categoryInfo.get(index1).getBgColor(),
                            taskDescription, date.getMonth() + 1, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    newInfoMixed = new Info(taskName, categoryInfo.get(index1).getBgColor(),
                            taskDescription, date.getMonth() + 1, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    Log.v("newInfoMixed", String.valueOf(newInfoMixed));
                }
                if (tasksInfo.get(index1).isEmpty()) {
                    Log.v("add","add");
                    tasksInfo.get(index1).add(newInfo); }
                else {
                    ArrayList<Info> updated = addAndInsert(tasksInfo.get(index1), newInfo);
                    ArrayList<Info> updated_alias = new ArrayList<Info>();
                    for (int i = 0; i < updated.size(); i++) {
                        updated_alias.add(updated.get(i)); }
                    tasksInfo.get(index1).clear();
                    for (int i = 0; i < updated_alias.size(); i++) {
                        tasksInfo.get(index1).add(updated_alias.get(i)); }
                }
                if (mixedTasksInfo.isEmpty()) {
                    mixedTasksInfo.add(newInfoMixed);
                }
                else {
                    ArrayList<Info> updated_mixed = addAndInsert(mixedTasksInfo, newInfoMixed);
                    ArrayList<Info> updated_mixed_alias = new ArrayList<Info>();
                    for (int i = 0; i < updated_mixed.size(); i++) {
                        updated_mixed_alias.add(updated_mixed.get(i)); }
                    mixedTasksInfo.clear();
                    for (int i = 0; i < updated_mixed_alias.size(); i++) {
                        mixedTasksInfo.add(updated_mixed_alias.get(i)); }
                }
                taskslist.setAdapter(adapter);
                taskslist.expandGroup(index1);
                mixedTasksList.setAdapter(adapter1);
                Bg.getForeground().setAlpha(0);
            }
        });
        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Bg.getForeground().setAlpha(0);
            }
        });
    }

    public void onButtonShowCategoryPopupWindowClick(View v) {
        animateFAB();
        // inflate the layout of the popup window
        // View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup, null, false);
        ///////////////////////////// categorypopupwindow //////////////////////////////////////
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View categoryView = inflater.inflate(R.layout.category_popup, null, false);
        FloatingActionButton addCategory = (FloatingActionButton) categoryView.findViewById(R.id.addcategory);
        FloatingActionButton cancelCategory = (FloatingActionButton) categoryView.findViewById(R.id.cancelcategory);

        //////////////////////// create the category popup window ////////////////////////////////
        final PopupWindow categoryWindow = new PopupWindow(categoryView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        categoryWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        categoryWindow.setAnimationStyle(R.style.popup_window_animation);
        Bg.getForeground().setAlpha(120);
        //popupWindow.setFocusable(false);
        categoryWindow.setOutsideTouchable(false);
        categoryWindow.showAtLocation(categoryView, Gravity.CENTER, 0, 0);
        categoryWindow.update();

        ///////////////////////// handles changing color of the namebar ///////////////////////
        final LinearLayout categorynameBar = (LinearLayout) categoryView.findViewById(R.id.categorybar);
        ImageView cc1 = (ImageView) categoryView.findViewById(R.id.color1);
        cc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 1) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect1);
            }
        });
        ImageView cc2 = (ImageView) categoryView.findViewById(R.id.color2);
        cc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 2) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect2);
            }
        });
        ImageView cc3 = (ImageView) categoryView.findViewById(R.id.color3);
        cc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 3) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect3);
            }
        });
        ImageView cc4 = (ImageView) categoryView.findViewById(R.id.color4);
        cc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 4) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect4);
            }
        });
        ImageView cc5 = (ImageView) categoryView.findViewById(R.id.color5);
        cc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 5) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect5);
            }
        });
        ImageView cc6 = (ImageView) categoryView.findViewById(R.id.color6);
        cc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 6) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect6);
            }
        });
        ImageView cc7 = (ImageView) categoryView.findViewById(R.id.color7);
        cc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 7) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect7);
            }
        });
        ImageView cc8 = (ImageView) categoryView.findViewById(R.id.color8);
        cc8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 8) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect8);
            }
        });
        ImageView cc9 = (ImageView) categoryView.findViewById(R.id.color9);
        cc9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 9) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect9);
            }
        });
        ImageView cc10 = (ImageView) categoryView.findViewById(R.id.color10);
        cc10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 10) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect10);
            }
        });
        ImageView cc11 = (ImageView) categoryView.findViewById(R.id.color11);
        cc11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 11) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect11);
            }
        });
        ImageView cc12 = (ImageView) categoryView.findViewById(R.id.color12);
        cc12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 12) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect12);
            }
        });
        ImageView cc13 = (ImageView) categoryView.findViewById(R.id.color13);
        cc13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 13) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect13);
            }
        });
        ImageView cc14 = (ImageView) categoryView.findViewById(R.id.color14);
        cc14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 14) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect14);
            }
        });
        ImageView cc15 = (ImageView) categoryView.findViewById(R.id.color15);
        cc15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 15) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect15);
            }
        });
        ImageView cc16 = (ImageView) categoryView.findViewById(R.id.color16);
        cc16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 16) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect16);
            }
        });
        ImageView cc17 = (ImageView) categoryView.findViewById(R.id.color17);
        cc17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 17) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect17);
            }
        });
        ImageView cc18 = (ImageView) categoryView.findViewById(R.id.color18);
        cc18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 18) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect18);
            }
        });
        ImageView cc19 = (ImageView) categoryView.findViewById(R.id.color19);
        cc19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 19) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect19);
            }
        });
        ImageView cc20 = (ImageView) categoryView.findViewById(R.id.color20);
        cc20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 20) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect20);
            }
        });
        ImageView cc21 = (ImageView)categoryView.findViewById(R.id.color21);
        cc21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 21) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect21);
            }
        });
        ImageView cc22 = (ImageView)categoryView.findViewById(R.id.color22);
        cc22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 22) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect22);
            }
        });
        ImageView cc23 = (ImageView)categoryView.findViewById(R.id.color23);
        cc23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 23) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect23);
            }
        });
        ImageView cc24 = (ImageView)categoryView.findViewById(R.id.color24);
        cc24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 24) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect24);
            }
        });
        ImageView cc25 = (ImageView)categoryView.findViewById(R.id.color25);
        cc25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 25) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect25);
            }
        });
        ImageView cc26 = (ImageView)categoryView.findViewById(R.id.color26);
        cc26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 26) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect26);
            }
        });
        ImageView cc27 = (ImageView)categoryView.findViewById(R.id.color27);
        cc27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 27) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect27);
            }
        });
        ImageView cc28 = (ImageView)categoryView.findViewById(R.id.color28);
        cc28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 28) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect28);
            }
        });
        ImageView cc29 = (ImageView)categoryView.findViewById(R.id.color29);
        cc29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 29) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect29);
            }
        });
        ImageView cc30 = (ImageView)categoryView.findViewById(R.id.color30);
        cc30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 30) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect30);
            }
        });

        ///////////////////// a new category is added, display on mainpage ////////////////////////////
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryWindow.dismiss();
                // store the edit text content into a string
                EditText category = (EditText)categoryView.findViewById(R.id.Category);
                category.setInputType(InputType.TYPE_CLASS_TEXT);
                categoryName = category.getText().toString();
                catDictionary.put(categoryName, 0);
                Bg.getForeground().setAlpha(0);
                /////////// if statement select by the color of namebar /////////////////
                if (Boolean[1])  {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor1, R.drawable.color1, true));
                    Boolean[1] = false;
                }
                else if (Boolean[2]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor2, R.drawable.color2, true));
                    Boolean[2] = false;
                }
                else if (Boolean[3]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor3, R.drawable.color3, true));
                    Boolean[3] = false;
                }
                else if (Boolean[4]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor4, R.drawable.color4, true));
                    Boolean[4] = false;
                }
                else if (Boolean[5]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor5, R.drawable.color5, true));
                    Boolean[5] = false;
                }
                else if (Boolean[6]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor6, R.drawable.color6, true));
                    Boolean[6] = false;
                }
                else if (Boolean[7]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor7, R.drawable.color7, true));
                    Boolean[7] = false;
                }
                else if (Boolean[8]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor8, R.drawable.color8, true));
                    Boolean[8] = false;
                }
                else if (Boolean[9]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor9, R.drawable.color9, true));
                    Boolean[9] = false;
                }
                else if (Boolean[10]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor10, R.drawable.color10, true));
                    Boolean[10] = false;
                }
                else if (Boolean[11]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor11, R.drawable.color11, true));
                    Boolean[11] = false;
                }
                else if (Boolean[12]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor12, R.drawable.color12, true));
                    Boolean[12] = false;
                }
                else if (Boolean[13]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor13, R.drawable.color13,true));
                    Boolean[13] = false;
                }
                else if (Boolean[14]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor14, R.drawable.color14, true));
                    Boolean[14] = false;
                }
                else if (Boolean[15]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor15, R.drawable.color15, true));
                    Boolean[15] = false;
                }
                else if (Boolean[16]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor16, R.drawable.color16, true));
                    Boolean[16] = false;
                }
                else if (Boolean[17]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor17, R.drawable.color17, true));
                    Boolean[17] = false;
                }
                else if (Boolean[18]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor18, R.drawable.color18, true));
                    Boolean[18] = false;
                }
                else if (Boolean[19]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor19, R.drawable.color19, true));
                    Boolean[19] = false;
                }
                else if (Boolean[20]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor20, R.drawable.color20, true));
                    Boolean[20] = false;
                }
                else if (Boolean[21]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor21, R.drawable.color21, true));
                    Boolean[21] = false;
                }
                else if (Boolean[22]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor22, R.drawable.color22, true));
                    Boolean[22] = false;
                }
                else if (Boolean[23]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor23, R.drawable.color23, true));
                    Boolean[23] = false;
                }
                else if (Boolean[24]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor24, R.drawable.color24,true));
                    Boolean[24] = false;
                }
                else if (Boolean[25]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor25, R.drawable.color25, true));
                    Boolean[25] = false;
                }
                else if (Boolean[26]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor26, R.drawable.color26, true));
                    Boolean[26] = false;
                }
                else if (Boolean[27]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor27, R.drawable.color27, true));
                    Boolean[27] = false;
                }
                else if (Boolean[28]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor28, R.drawable.color28, true));
                    Boolean[28] = false;
                }
                else if (Boolean[29]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor29, R.drawable.color29, true));
                    Boolean[29] = false;
                }
                else if (Boolean[30]) {
                    categoryInfo.add(new Info(categoryName, R.drawable.reccolor30, R.drawable.color30, true));
                    Boolean[30] = false;
                }
                else {
                    categoryInfo.add(new Info(categoryName, R.drawable.rectangle1, R.drawable.color1, true));
                }
                tasksInfo.add(new ArrayList<Info>());
                taskslist.setAdapter(adapter);
            }
        });

        ////////////////// cancel popupwindow ///////////////////////////////////////
        cancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryWindow.dismiss();
                Bg.getForeground().setAlpha(0);
            }
        });
    }

    public void removeTask(View v) {
        final int groupPos = Integer.valueOf(v.getTag(R.id.groupPosition).toString());
        final int childPos = Integer.valueOf(v.getTag(R.id.childPosition).toString());
        Info finishedTask = tasksInfo.get(groupPos).get(childPos);
        tasksInfo.get(groupPos).remove(childPos);
        Info mixedCurrInfo = new Info(finishedTask.getTitle(),
                finishedTask.getCatColor(), finishedTask.getDescription(), finishedTask.getMonth(),
                finishedTask.getDay(),
                finishedTask.getHour(), finishedTask.getMinute(), false);
        int mixedPos = mixedTasksInfo.indexOf(mixedCurrInfo);
        mixedTasksInfo.remove(mixedPos);
        completedTasksInfo.add(finishedTask); }

    public void onButtonShowMixedListClick(View v) {
        animateFAB();
        mixedTasksList.setAdapter(adapter1);
        if (!mixedMode && categorizedMode) {
            mixedTasksList.setVisibility(View.VISIBLE);
            taskslist.setVisibility(View.GONE);
            completedTasksList.setVisibility(View.GONE);
            categorizedMode = !categorizedMode;
            mixedMode = !mixedMode;
        }
        else {//mixedMode
            taskslist.setVisibility(View.VISIBLE);
            mixedTasksList.setVisibility(View.GONE);
            completedTasksList.setVisibility(View.GONE);
            categorizedMode = !categorizedMode;
            mixedMode = !mixedMode;
        }
        Log.v("categorizedMode", String.valueOf(categorizedMode));
        Log.v("mixedMode", String.valueOf(mixedMode));
    }

    public void editCategory(View v) {
        final int groupPos = (Integer) v.getTag();
        final Info currCategory = categoryInfo.get(groupPos);
        String categoryName = currCategory.getTitle();
        final int BgColor = currCategory.getBgColor();
        int fabColor = currCategory.getFabcolor();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View categoryView = inflater.inflate(R.layout.category_popup, null, false);
        FloatingActionButton addCategory = (FloatingActionButton) categoryView.findViewById(R.id.addcategory);
        FloatingActionButton cancelCategory = (FloatingActionButton) categoryView.findViewById(R.id.cancelcategory);

        //////////////////////// create the category popup window ////////////////////////////////
        final PopupWindow categoryWindow = new PopupWindow(categoryView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        categoryWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        categoryWindow.setAnimationStyle(R.style.popup_window_animation);
        Bg.getForeground().setAlpha(120);
        //popupWindow.setFocusable(false);
        categoryWindow.setOutsideTouchable(false);
        categoryWindow.showAtLocation(categoryView, Gravity.CENTER, 0, 0);
        categoryWindow.update();

        ///////////////////////// handles changing color of the namebar ///////////////////////
        final LinearLayout categorynameBar = (LinearLayout) categoryView.findViewById(R.id.categorybar);
        categorynameBar.setBackgroundResource(BgColor);
        final EditText categoryTitle = categoryView.findViewById(R.id.Category);
        categoryTitle.setText(categoryName);

        ImageView cc1 = (ImageView) categoryView.findViewById(R.id.color1);
        cc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 1) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect1);
            }
        });
        ImageView cc2 = (ImageView) categoryView.findViewById(R.id.color2);
        cc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 2) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect2);
            }
        });
        ImageView cc3 = (ImageView) categoryView.findViewById(R.id.color3);
        cc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 3) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect3);
            }
        });
        ImageView cc4 = (ImageView) categoryView.findViewById(R.id.color4);
        cc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 4) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect4);
            }
        });
        ImageView cc5 = (ImageView) categoryView.findViewById(R.id.color5);
        cc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 5) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect5);
            }
        });
        ImageView cc6 = (ImageView) categoryView.findViewById(R.id.color6);
        cc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 6) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect6);
            }
        });
        ImageView cc7 = (ImageView) categoryView.findViewById(R.id.color7);
        cc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 7) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect7);
            }
        });
        ImageView cc8 = (ImageView) categoryView.findViewById(R.id.color8);
        cc8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 8) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect8);
            }
        });
        ImageView cc9 = (ImageView) categoryView.findViewById(R.id.color9);
        cc9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 9) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect9);
            }
        });
        ImageView cc10 = (ImageView) categoryView.findViewById(R.id.color10);
        cc10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 10) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect10);
            }
        });
        ImageView cc11 = (ImageView) categoryView.findViewById(R.id.color11);
        cc11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 11) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect11);
            }
        });
        ImageView cc12 = (ImageView) categoryView.findViewById(R.id.color12);
        cc12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 12) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect12);
            }
        });
        ImageView cc13 = (ImageView) categoryView.findViewById(R.id.color13);
        cc13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 13) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect13);
            }
        });
        ImageView cc14 = (ImageView) categoryView.findViewById(R.id.color14);
        cc14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 14) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect14);
            }
        });
        ImageView cc15 = (ImageView) categoryView.findViewById(R.id.color15);
        cc15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 15) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect15);
            }
        });
        ImageView cc16 = (ImageView) categoryView.findViewById(R.id.color16);
        cc16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 16) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect16);
            }
        });
        ImageView cc17 = (ImageView) categoryView.findViewById(R.id.color17);
        cc17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 17) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect17);
            }
        });
        ImageView cc18 = (ImageView) categoryView.findViewById(R.id.color18);
        cc18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 18) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect18);
            }
        });
        ImageView cc19 = (ImageView) categoryView.findViewById(R.id.color19);
        cc19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 19) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect19);
            }
        });
        ImageView cc20 = (ImageView) categoryView.findViewById(R.id.color20);
        cc20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 20) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect20);
            }
        });
        ImageView cc21 = (ImageView)categoryView.findViewById(R.id.color21);
        cc21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 21) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect21);
            }
        });
        ImageView cc22 = (ImageView)categoryView.findViewById(R.id.color22);
        cc22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 22) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect22);
            }
        });
        ImageView cc23 = (ImageView)categoryView.findViewById(R.id.color23);
        cc23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 23) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect23);
            }
        });
        ImageView cc24 = (ImageView)categoryView.findViewById(R.id.color24);
        cc24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 24) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect24);
            }
        });
        ImageView cc25 = (ImageView)categoryView.findViewById(R.id.color25);
        cc25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 25) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect25);
            }
        });
        ImageView cc26 = (ImageView)categoryView.findViewById(R.id.color26);
        cc26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 26) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect26);
            }
        });
        ImageView cc27 = (ImageView)categoryView.findViewById(R.id.color27);
        cc27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 27) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect27);
            }
        });
        ImageView cc28 = (ImageView)categoryView.findViewById(R.id.color28);
        cc28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 28) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect28);
            }
        });
        ImageView cc29 = (ImageView)categoryView.findViewById(R.id.color29);
        cc29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 29) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect29);
            }
        });
        ImageView cc30 = (ImageView)categoryView.findViewById(R.id.color30);
        cc30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 31; i++) {
                    if (i == 30) { Boolean[i] = true; }
                    else { Boolean[i] = false; } }
                categorynameBar.setBackgroundResource(R.drawable.effect30);
            }
        });
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryWindow.dismiss();
                // store the edit text content into a string
                EditText category = (EditText)categoryView.findViewById(R.id.Category);
                category.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_categoryName = category.getText().toString();
                catDictionary.put(edit_categoryName, 0);
                Bg.getForeground().setAlpha(0);
                /////////// if statement select by the color of namebar /////////////////
                if (Boolean[1])  {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor1, R.drawable.color1, true);
                    Boolean[1] = false;
                }
                else if (Boolean[2]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor2, R.drawable.color2, true);
                    Boolean[2] = false;
                }
                else if (Boolean[3]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor3, R.drawable.color3, true);
                    Boolean[3] = false;
                }
                else if (Boolean[4]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor4, R.drawable.color4, true);
                    Boolean[4] = false;
                }
                else if (Boolean[5]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor5, R.drawable.color5, true);
                    Boolean[5] = false;
                }
                else if (Boolean[6]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor6, R.drawable.color6, true);
                    Boolean[6] = false;
                }
                else if (Boolean[7]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor7, R.drawable.color7, true);
                    Boolean[7] = false;
                }
                else if (Boolean[8]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor8, R.drawable.color8, true);
                    Boolean[8] = false;
                }
                else if (Boolean[9]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor9, R.drawable.color9, true);
                    Boolean[9] = false;
                }
                else if (Boolean[10]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor10, R.drawable.color10, true);
                    Boolean[10] = false;
                }
                else if (Boolean[11]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor11, R.drawable.color11, true);
                    Boolean[11] = false;
                }
                else if (Boolean[12]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor12, R.drawable.color12, true);
                    Boolean[12] = false;
                }
                else if (Boolean[13]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor13, R.drawable.color13,true);
                    Boolean[13] = false;
                }
                else if (Boolean[14]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor14, R.drawable.color14, true);
                    Boolean[14] = false;
                }
                else if (Boolean[15]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor15, R.drawable.color15, true);
                    Boolean[15] = false;
                }
                else if (Boolean[16]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor16, R.drawable.color16, true);
                    Boolean[16] = false;
                }
                else if (Boolean[17]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor17, R.drawable.color17, true);
                    Boolean[17] = false;
                }
                else if (Boolean[18]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor18, R.drawable.color18, true);
                    Boolean[18] = false;
                }
                else if (Boolean[19]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor19, R.drawable.color19, true);
                    Boolean[19] = false;
                }
                else if (Boolean[20]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor20, R.drawable.color20, true);
                    Boolean[20] = false;
                }
                else if (Boolean[21]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor21, R.drawable.color21, true);
                    Boolean[21] = false;
                }
                else if (Boolean[22]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor22, R.drawable.color22, true);
                    Boolean[22] = false;
                }
                else if (Boolean[23]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor23, R.drawable.color23, true);
                    Boolean[23] = false;
                }
                else if (Boolean[24]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor24, R.drawable.color24,true);
                    Boolean[24] = false;
                }
                else if (Boolean[25]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor25, R.drawable.color25, true);
                    Boolean[25] = false;
                }
                else if (Boolean[26]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor26, R.drawable.color26, true);
                    Boolean[26] = false;
                }
                else if (Boolean[27]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor27, R.drawable.color27, true);
                    Boolean[27] = false;
                }
                else if (Boolean[28]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor28, R.drawable.color28, true);
                    Boolean[28] = false;
                }
                else if (Boolean[29]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor29, R.drawable.color29, true);
                    Boolean[29] = false;
                }
                else if (Boolean[30]) {
                    editCat = new Info(edit_categoryName, R.drawable.reccolor30, R.drawable.color30, true);
                    Boolean[30] = false;
                }
                else {
                    editCat = new Info(edit_categoryName, BgColor, R.drawable.color1, true);
                }
                categoryInfo.set(groupPos, editCat);
                int Catcolor = editCat.getBgColor();
                for (int i = 0; i < tasksInfo.get(groupPos).size(); i++) {
                    Info currTask = tasksInfo.get(groupPos).get(i);
                    Info editedTask = new Info(currTask.getTitle(), edit_categoryName, currTask.getBgColor(),
                            Catcolor, currTask.getDescription(), currTask.getMonth(), currTask.getDay(),
                            currTask.getHour(), currTask.getMinute(), currTask.getMode());
                    tasksInfo.get(groupPos).set(i, editedTask);
                }
                taskslist.setAdapter(adapter);
                for (int i = 0; i < mixedTasksInfo.size(); i++) {
                    if (BgColor == mixedTasksInfo.get(i).getBgColor()) {
                        Info currMixedTask = mixedTasksInfo.get(i);
                        Info editedMixedTask = new Info(currMixedTask.getTitle(), Catcolor, currMixedTask.getDescription(),
                                currMixedTask.getMonth(), currMixedTask.getDay(),
                                currMixedTask.getHour(), currMixedTask.getMinute(),
                                currMixedTask.getMode());
                        mixedTasksInfo.set(i, editedMixedTask);
                    }
                }
                mixedTasksList.setAdapter(adapter1);
            }
        });

        ////////////////// cancel popupwindow ///////////////////////////////////////
        cancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryWindow.dismiss();
                Bg.getForeground().setAlpha(0);
            }
        });
    }

    public void editTask(View v) {
        final int groupPos = (Integer) v.getTag(R.id.groupPosition);
        final int childPos = (Integer) v.getTag(R.id.childPosition);
        ////categorizedMode && !finishedMode find mixedTasksList
        ////if categorizedMode = false find taskslist

        Info currEditTask = tasksInfo.get(groupPos).get(childPos);
        String taskName = currEditTask.getTitle();
        String taskDescription = currEditTask.getDescription();
        final String taskCategory = currEditTask.getCategory();
        int month = currEditTask.getMonth() - 1; int day = currEditTask.getDay();
        int hour = currEditTask.getHour(); int minute = currEditTask.getMinute();
        final int Catcolor = currEditTask.getCatColor();

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editPopupView = inflater.inflate(R.layout.popup, null, false);
        EditText currTaskName = (EditText) editPopupView.findViewById(R.id.Caption);
        currTaskName.setText(taskName);
        FloatingActionButton addTask = (FloatingActionButton) editPopupView.findViewById(R.id.checkmark);
        FloatingActionButton cancelTask = (FloatingActionButton) editPopupView.findViewById(R.id.cancel);
        ViewPager TaskInfo = (ViewPager)editPopupView.findViewById(R.id.viewpager);
        LinearLayout sliderpanel = (LinearLayout)editPopupView.findViewById(R.id.slidebars);
        //////////////////////// adapt to the viewpager of popupwindow ////////////////////////
        ArrayList aList = new ArrayList<View>();
        LayoutInflater info = getLayoutInflater();
        final View describeView = info.inflate(R.layout.description,null,false);
        EditText editDescription = (EditText) describeView.findViewById(R.id.Description);
        editDescription.setText(taskDescription);
        View clockView = info.inflate(R.layout.clock,null,false);
        // View colorView = info.inflate(R.layout.color,null,false);
        View dateView = info.inflate(R.layout.date,null,false);
        aList.add(describeView);
        //aList.add(colorView);
        aList.add(dateView);
        aList.add(clockView);
        MiniPagerAdapter mAdapter = new MiniPagerAdapter(aList);
        TaskInfo.setAdapter(mAdapter);
        /////////////////////// add bar indicators ///////////////////////////////
        final int barscount = mAdapter.getCount();
        final ImageView[] bars = new ImageView[barscount];
        for (int i = 0; i < barscount; i++) {
            bars[i] = new ImageView(this);
            bars[i].setBackgroundResource(R.drawable.inactive_indicator);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderpanel.addView(bars[i], params);
        }
        bars[0].setBackgroundResource(R.drawable.active_indicator);
        /////////////////////// viewpager onpagechangelistener redefine /////////////////////////
        TaskInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< barscount; i++){
                    bars[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactive_indicator)); }
                bars[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_indicator)); }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        ////////////////////// time picker, set clock ////////////////////////////
        final DatePicker date = (DatePicker) dateView.findViewById(R.id.datepicker);
        final TimePicker time = (TimePicker) clockView.findViewById(R.id.timepicker);
        time.setIs24HourView(true);
        final Calendar c1 = Calendar.getInstance();

        Log.v("month", String.valueOf(month));
        Log.v("day", String.valueOf(day));
        time.setHour(hour);
        time.setMinute(minute);
        //////////////////////// create the popup window ////////////////////////////////
        //final int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        //int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow editPopupWindow = new PopupWindow(editPopupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        editPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        editPopupWindow.setAnimationStyle(R.style.popup_window_animation);
        Bg.getForeground().setAlpha(120);
        //popupWindow.setFocusable(false);
        editPopupWindow.setOutsideTouchable(false);
        editPopupWindow.showAtLocation(editPopupView, Gravity.CENTER, 0, 0);
        editPopupWindow.update();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPopupWindow.dismiss();
                EditText editedtitle = (EditText) editPopupView.findViewById(R.id.Caption);
                editedtitle.setInputType(InputType.TYPE_CLASS_TEXT);
                String editedTaskName = editedtitle.getText().toString(); ///editedTaskName
                EditText editeddescribe =(EditText) describeView.findViewById(R.id.Description);
                editeddescribe.setInputType(InputType.TYPE_CLASS_TEXT);
                String editedDescription = editeddescribe.getText().toString(); ///editedDescription
                Log.v("editedMonth", String.valueOf(date.getMonth()));
                Info should_edit = tasksInfo.get(groupPos).get(childPos);
                if (date.getMonth() == c1.get(Calendar.MONTH) &
                        date.getDayOfMonth() == c1.get(Calendar.DAY_OF_MONTH) &
                        time.getHour() == c1.get(Calendar.HOUR_OF_DAY) &
                        time.getMinute() == c1.get(Calendar.MINUTE)) { /// no deadline
                    newInfo = new Info(editedTaskName, taskCategory,R.drawable.reccolor0, Catcolor,
                            editedDescription, 13, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    newInfoMixed = new Info(editedTaskName, Catcolor,
                            editedDescription, 13, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    Info Info_mixed_mode = new Info(should_edit.getTitle(),
                            Catcolor, should_edit.getDescription(), 13,
                            should_edit.getDay(),
                            should_edit.getHour(), should_edit.getMinute(), false);
                    int mixedPos = mixedTasksInfo.indexOf(Info_mixed_mode);
                    tasksInfo.get(groupPos).set(childPos, newInfo);
                    mixedTasksInfo.set(mixedPos, newInfoMixed); }
                else {
                    newInfo = new Info(editedTaskName, taskCategory, R.drawable.reccolor0, Catcolor,
                            editedDescription, date.getMonth() + 1, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    newInfoMixed = new Info(editedTaskName, Catcolor,
                            editedDescription, date.getMonth() + 1, date.getDayOfMonth(), time.getHour(),
                            time.getMinute(), false);
                    Info Info_mixed_mode = new Info(should_edit.getTitle(),
                            Catcolor, should_edit.getDescription(), should_edit.getMonth(),
                            should_edit.getDay(),
                            should_edit.getHour(), should_edit.getMinute(), false);
                    int mixedPos = mixedTasksInfo.indexOf(Info_mixed_mode);
                    tasksInfo.get(groupPos).set(childPos, newInfo);
                    mixedTasksInfo.set(mixedPos, newInfoMixed);
                }
                //taskslist.setRemoveListener(MainActivity.this);
                taskslist.setAdapter(adapter);
                taskslist.expandGroup(groupPos);
                mixedTasksList.setAdapter(adapter1);
                Bg.getForeground().setAlpha(0);
            }
        });
        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPopupWindow.dismiss();
                Bg.getForeground().setAlpha(0);
            }
        });
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


}

