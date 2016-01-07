package com.tilak.noteshare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tilak.adpters.FolderListingAdapter;
import com.tilak.adpters.NoteList_Adapter_New;
import com.tilak.dataAccess.DataManager;
import com.tilak.notesharedatabase.DBNoteItemElement;
import com.tilak.notesharedatabase.DBNoteItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NoteListingActvity extends DrawerActivity {


    public ImageButton imageButtonFolder,imageButtonMore,imageButtonHamburg,btnAddNote;
    public NoteList_Adapter_New adapter_new;
    public ArrayList<DBNoteItems> arrDBDataNote;
    ListView listView;
    public ArrayList<DBNoteItems> arrDataFolder;;
    public FolderListingAdapter folderListingAdapter;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID=1;

    private int mYear, mMonth, mDay,mHour,mMinute,mSeconds;
    public  int year,month,day,hour,minute,seconds;

    public  String selecteddate,selectedTime,timeZone;
    public  Calendar c;
    public  DBNoteItems selectedDbItem;
    SharedPreferences preference;
    static String SAVELOCK="LOCK";
    public SORTTYPE sortType;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_note_listing_actvity);

        DataManager.sharedDataManager().setSelectedItemIndex(-1);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_note_listing_actvity, null, false);

        mDrawerLayout.addView(contentView, 0);

        imageButtonMore= (ImageButton)contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtoncalander);
        imageButtonFolder= (ImageButton)contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtonsquence);
        imageButtonHamburg= (ImageButton)contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtonHamburg);
        listView=(ListView)contentView.findViewById(R.id.listviewNotes);
        btnAddNote=(ImageButton)contentView.findViewById(R.id.btnAddNote);


        arrDBDataNote=new ArrayList<DBNoteItems>();


        adapter_new=new  NoteList_Adapter_New(NoteListingActvity.this, arrDBDataNote, new NoteList_Adapter_New.NoteList_Adapter_New_Listner()
        {
            @Override
            public void didMoreSelected(final DBNoteItems item1, View selectedbutton, int selectedindex)
            {

                selectedDbItem=item1;

                switch (selectedbutton.getId())
                {
                    case R.id.editNoteTitle:
                    {
                        showAlertWithUpdateTitleEditText(NoteListingActvity.this,item1,item1.getNote_Title());
                    }
                    break;
                    case R.id.noteDelete:
                    {
                        showAlertWithDeleteMessage("Are you sure you want delete this note", NoteListingActvity.this, item1);
                    }
                    break;
                    case R.id.noteTimeBomb:
                    {

                         showDialog(DATE_DIALOG_ID);


					/*	SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
						Date d = new Date();
						String strDate = dateFormatGmt.format(d);
						selectedDbItem.setNote_TimeBomb(strDate);

						boolean status=androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDbItem);

						if (status==true)
						{

							Toast.makeText(MainActivity.this,"Time Bomb added successfully",Toast.LENGTH_SHORT).show();
							getallNotes();
						}
						else
						{
							Toast.makeText(MainActivity.this,"Time Bomb added unsuccessfully",Toast.LENGTH_SHORT).show();

						}*/


                    }
                    break;
                    case R.id.noteLock:
                    {

                        showAlertToLockNUnlockNote(item1, NoteListingActvity.this, true, false);

                    }
                    break;
                    case R.id.noteunlockLock:
                    {
                        showAlertToLockNUnlockNote(item1, NoteListingActvity.this,false,false);
                    }
                    break;
                    case R.id.noteShare:
                    {

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/html");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>" + item1.getNote_Title().toString() + "</p>"));
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                    break;

                    case R.id.noteMoveToFolder:
                    {
                                if (arrDataFolder!=null) {
                                    if (arrDataFolder.size() > 0) {
                                        showMoveToFolderDialog("", NoteListingActvity.this, item1);
                                    } else {
                                        showAlertWith("Please create at least one folder", NoteListingActvity.this);
                                    }
                                }
                    }
                    break;

                    case R.id.noteAddColor:
                    {



                    }
                    break;
                    case R.id.noteremindertime:
                    {



                    }
                    break;
                    default:
                        break;
                }


            }

            @Override
            public void didlistItemClick(DBNoteItems item1, View selectedbutton, int selectedindex) {

                Log.d("The listitem : ", "ID: " + item1.getNote_Id() + "\n");
                Log.d("The listitem: ","Text: "+selectedbutton.getTag()+"\n");
                Log.d("The listitem: ", "Index: " + selectedindex + "\n");



                selectedDbItem=item1;
                DataManager.sharedDataManager().setSeletedDBNoteItem(item1);


                if (true)///menuItem.isIsdefaultNote()!=true)
                {

                    if (item1.getNote_Lock_Status()!=null)
                    {
                        if (item1.getNote_Lock_Status().equalsIgnoreCase("1"))
                        {

                            showAlertToLockNUnlockNote(item1, NoteListingActvity.this,false,true);
                        }
                        else
                        {

                            //DataManager.sharedDataManager().setSeletedDBNoteItem(item1);
                            startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));
                        }

                    }else
                    {

                        //DataManager.sharedDataManager().setSeletedDBNoteItem(item1);
                        startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));
                    }



                }
                else
                {
                    Toast.makeText(NoteListingActvity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void didExpandViewAtIndex(DBNoteItems item1, View selectedbutton, int selectedindex) {


                if (DataManager.sharedDataManager().getSelectedItemIndex()!=-1)
                {
                    if (DataManager.sharedDataManager().getSelectedItemIndex()==selectedindex)
                    {
                        DataManager.sharedDataManager().setSelectedItemIndex(-1);

                    }
                    else
                    {
                        DataManager.sharedDataManager().setSelectedItemIndex(selectedindex);
                    }


                }
                else
                {
                    DataManager.sharedDataManager().setSelectedItemIndex(selectedindex);
                }

                adapter_new.notifyDataSetChanged();

            }
        });


        listView.setAdapter(adapter_new);
        adapter_new.notifyDataSetChanged();




        addListners_new();
        getallNotes();
        getAllFolder();
        initilizesDateAndTime();


    }

    void showSettingDialogWith(String message, Context context)
    {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.more_setting_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
      //  textViewTitleAlert.setText("" + message);
       // textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
       // textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
               // System.exit(0);
                dialog.dismiss();

            }
        });

        textViewTitleAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //View By

                showActionSheet(view);
                dialog.dismiss();

            }
        });

        textViewTitleAlertMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Sort By

                showActionSheet_sort(view);
                dialog.dismiss();

            }
        });


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog.getWindow().getAttributes().verticalMargin=0.01F;
        dialog.getWindow().getAttributes().horizontalMargin=0.01F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(contentView);
        dialog.show();

    }


    public void showActionSheet(View v) {

        final Dialog myDialog = new Dialog(NoteListingActvity.this,
                R.style.CustomTheme);

        myDialog.setContentView(R.layout.actionsheet);
        Button buttonDissmiss = (Button) myDialog
                .findViewById(R.id.buttonDissmiss);

        LinearLayout layoutList = (LinearLayout) myDialog
                .findViewById(R.id.layoutList);
        TextView layoutListTextView = (TextView) layoutList
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutListImageView = (ImageView) layoutList
                .findViewById(R.id.imageViewSlidemenu);
        layoutListImageView.setImageResource(R.drawable.list_view_logo);
        layoutListTextView.setText("List");

        LinearLayout layoutDetail = (LinearLayout) myDialog
                .findViewById(R.id.layoutDetail);
        TextView layoutDetailTextView = (TextView) layoutDetail
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutDetailImageView = (ImageView) layoutDetail
                .findViewById(R.id.imageViewSlidemenu);
        layoutDetailImageView.setImageResource(R.drawable.detail_view_logo);
        layoutDetailTextView.setText("Details");

        LinearLayout layoutPintrest = (LinearLayout) myDialog
                .findViewById(R.id.layoutPintrest);
        TextView layoutPintrestTextView = (TextView) layoutPintrest
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutPintrestImageView = (ImageView) layoutPintrest
                .findViewById(R.id.imageViewSlidemenu);
        layoutPintrestImageView.setImageResource(R.drawable.pintrest_view_logo);
        layoutPintrestTextView.setText("Shuffle");

        LinearLayout layoutGrid = (LinearLayout) myDialog
                .findViewById(R.id.layoutGrid);
        TextView layoutGridTextView = (TextView) layoutGrid
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutGridImageView = (ImageView) layoutGrid
                .findViewById(R.id.imageViewSlidemenu);
        layoutGridImageView.setImageResource(R.drawable.grid_view_logo);
        layoutGridTextView.setText("Tiles");

        layoutGridTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // notefoleserGridList.setVisibility(View.VISIBLE);
                // notefoleserList.setVisibility(View.GONE);
                // notefoleserPintrestList.setVisibility(View.GONE);
                myDialog.dismiss();

            }
        });

        layoutPintrestTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
               // notefoleserGridList.setVisibility(View.GONE);
               // notefoleserList.setVisibility(View.GONE);
               // notefoleserPintrestList.setVisibility(View.VISIBLE);

               // updatePintrestView();
                myDialog.dismiss();

            }
        });

        layoutDetailTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DataManager.sharedDataManager().setTypeofListView(false);

                // TODO Auto-generated method stub
               // notefoleserGridList.setVisibility(View.GONE);
               // notefoleserList.setVisibility(View.VISIBLE);
                //notefoleserPintrestList.setVisibility(View.GONE);

               // DataManager.sharedDataManager().setSelectedIndex(-1);
                adapter.notifyDataSetChanged();
                myDialog.dismiss();

            }
        });

        layoutListTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DataManager.sharedDataManager().setTypeofListView(true);

               // notefoleserGridList.setVisibility(View.GONE);
               // notefoleserList.setVisibility(View.VISIBLE);
               // notefoleserPintrestList.setVisibility(View.GONE);
                //DataManager.sharedDataManager().setSelectedIndex(-1);
                adapter.notifyDataSetChanged();
                myDialog.dismiss();

            }
        });

        buttonDissmiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

       // myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;
        myDialog.getWindow().getAttributes().windowAnimations =  R.style.dialog_animation;
        myDialog.show();



        myDialog.getWindow().setGravity(Gravity.CENTER);

    }

    public void showActionSheet_sort(View v) {

        final Dialog myDialog = new Dialog(NoteListingActvity.this,
                R.style.CustomTheme);

        // layoutReminderTime
        // layouttimebomb

        myDialog.setContentView(R.layout.actionsheet_sort);
        Button buttonDissmiss = (Button) myDialog
                .findViewById(R.id.buttonDissmiss);

        LinearLayout layoutList = (LinearLayout) myDialog
                .findViewById(R.id.layoutList);
        TextView layoutListTextView = (TextView) layoutList
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutListImageView = (ImageView) layoutList
                .findViewById(R.id.imageViewSlidemenu);
        layoutListImageView.setImageResource(R.drawable.alphabet_sort_view);

        layoutListTextView.setText("A-Z alphabetical");

        LinearLayout layoutDetail = (LinearLayout) myDialog
                .findViewById(R.id.layoutDetail);
        TextView layoutDetailTextView = (TextView) layoutDetail
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutDetailImageView = (ImageView) layoutDetail
                .findViewById(R.id.imageViewSlidemenu);
        layoutDetailImageView.setImageResource(R.drawable.color_sort_view);
        layoutDetailTextView.setText("Colours");

        LinearLayout layoutPintrest = (LinearLayout) myDialog
                .findViewById(R.id.layoutPintrest);
        TextView layoutPintrestTextView = (TextView) layoutPintrest
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutPintrestImageView = (ImageView) layoutPintrest
                .findViewById(R.id.imageViewSlidemenu);
        layoutPintrestImageView
                .setImageResource(R.drawable.modifiedtime_sort_view);
        layoutPintrestTextView.setText("Modified Time");

        LinearLayout layoutGrid = (LinearLayout) myDialog
                .findViewById(R.id.layoutGrid);
        TextView layoutGridTextView = (TextView) layoutGrid
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutGridImageView = (ImageView) layoutGrid
                .findViewById(R.id.imageViewSlidemenu);
        layoutGridImageView.setImageResource(R.drawable.createdtime_sort_view);
        layoutGridTextView.setText("Created Time");

        LinearLayout layoutListReminderTime = (LinearLayout) myDialog
                .findViewById(R.id.layoutReminderTime);
        TextView layoutListTextViewReminderTime = (TextView) layoutListReminderTime
                .findViewById(R.id.textViewSlideMenuName);
        ImageView layoutListImageViewReminderTime = (ImageView) layoutListReminderTime
                .findViewById(R.id.imageViewSlidemenu);
        layoutListImageViewReminderTime
                .setImageResource(R.drawable.reminder_sort_view);
        layoutListTextViewReminderTime.setText("Reminder Time");

        LinearLayout layoutListTimeBomb = (LinearLayout) myDialog
                .findViewById(R.id.layouttimebomb);
        TextView layoutListTextViewTimeBomb = (TextView) layoutListTimeBomb
                .findViewById(R.id.textViewSlideMenuName);
        layoutListTimeBomb.findViewById(R.id.imageViewSlidemenu);
        layoutListTextViewTimeBomb.setText("Time Bomb");

        layoutGridTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Created Time",
                        Toast.LENGTH_SHORT).show();
                sortType = SORTTYPE.CREATED_TIME;
                sortingArray();

                myDialog.dismiss();

            }
        });

        layoutPintrestTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Modified Time",
                        Toast.LENGTH_SHORT).show();
                sortType = SORTTYPE.MODIFIED_TIME;
                sortingArray();

                myDialog.dismiss();

            }
        });

        layoutDetailTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(), "Colours",
                        Toast.LENGTH_SHORT).show();

                sortType = SORTTYPE.COLOURS;
                sortingArray();

                myDialog.dismiss();

            }
        });

        layoutListTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                adapter.notifyDataSetChanged();

                sortType = SORTTYPE.ALPHABET;
                sortingArray();

                Toast.makeText(getApplicationContext(), "Alphabetical",
                        Toast.LENGTH_SHORT).show();

                myDialog.dismiss();

            }
        });

        layoutListTextViewTimeBomb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "Time Bomb",
                        Toast.LENGTH_SHORT).show();

                myDialog.dismiss();

            }
        });

        layoutListTextViewReminderTime
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(),
                                "Reminder Time", Toast.LENGTH_SHORT).show();

                        myDialog.dismiss();
                    }
                });

        buttonDissmiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

       // myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;

        myDialog.getWindow().getAttributes().windowAnimations =  R.style.dialog_animation;

        myDialog.show();

        myDialog.getWindow().setGravity(Gravity.CENTER);

    }

    void sortingArray() {
        switch (sortType) {
            case ALPHABET: {

                //DB note element sorting

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return lhs.getNote_Title().compareToIgnoreCase(
                                rhs.getNote_Title());
                    }
                });





            }
            break;
            case COLOURS: {


                //DB note element sorting

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return lhs.getNote_Color().compareToIgnoreCase(
                                rhs.getNote_Color());
                    }
                });


            }
            break;
            case CREATED_TIME:
            {

            }
            break;
            case MODIFIED_TIME:
            {

            }
            break;
            case REMINDER_TIME:
            {

            }
            break;
            case TIME_BOMB: {

            }
            break;

            default:
                break;
        }
    }

    void showAlertWith(String message, Context context)
    {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("ALERT");
        textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.exit(0);

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(contentView);
        dialog.show();

    }
    void showMoveToFolderDialog(String message, Context context,final  DBNoteItems items)
    {


        folderListingAdapter=new FolderListingAdapter(NoteListingActvity.this,arrDataFolder);
        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.folderlisting_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("MOVE TO FOLDER");
        textViewTitleAlert.setTextColor(Color.WHITE);

        ListView listfolderView=(ListView)contentView.findViewById(R.id.listViewFolderList);
        listfolderView.setAdapter(folderListingAdapter);
        folderListingAdapter.notifyDataSetChanged();


        listfolderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (items!=null)
                {
                    DBNoteItems itemSelected = arrDataFolder.get(i);
                    items.setNote_Folder_Id(itemSelected.getNote_Id());

                    boolean status = androidOpenDbHelperObj.updateNoteitems_MoveToFolder(items);

                    if (status == true) {

                        Toast.makeText(NoteListingActvity.this, "Note moved successfully", Toast.LENGTH_SHORT).show();
                        getallNotes();
                    } else {
                        Toast.makeText(NoteListingActvity.this, " move unsuccessfully", Toast.LENGTH_SHORT).show();

                    }
                }

                dialog.dismiss();

            }
        });



        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(contentView);
        dialog.show();

    }
    void showAlertWithDeleteMessage(String message, Context context, final DBNoteItems dbNoteItems)
    {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("" + dbNoteItems.getNote_Title());
        textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                boolean status=androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
                if (status==true)
                {

                    Toast.makeText(NoteListingActvity.this,"Note deleted successfully",Toast.LENGTH_SHORT).show();
                    getallNotes();
                }
                else
                {
                    Toast.makeText(NoteListingActvity.this,"Note deleted unsuccessfully",Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(contentView);
        dialog.show();

    }
    void showAlertWithUpdateTitleEditText(Context context,final DBNoteItems selectedItem,String message)
    {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate your activity layout here!

        // LinearLayout layoutAlertbox1=

        View contentView = inflater.inflate(R.layout.edit_alert_view, null,
                false);
        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("CRATE A NOTE");
        textViewTitleAlert.setTextColor(Color.WHITE);
        final EditText textViewTitleAlertMessage = (EditText) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (textViewTitleAlertMessage.getText().toString().length() > 0) {

                    // Call insert db call

                    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss");
                    dateFormatGmt.setTimeZone(TimeZone.getDefault());
                    //dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date d = new Date();
                    String strDate = dateFormatGmt.format(d);
                    System.out.println("The note created date: " + strDate
                            + "note title: "
                            + textViewTitleAlertMessage.getText().toString());

                    selectedItem.setNote_Title(textViewTitleAlertMessage.getText().toString());

                    boolean status=androidOpenDbHelperObj.updateNoteitems_title(selectedItem);




                    if (status == true)
                    {
                        Toast.makeText(NoteListingActvity.this,
                                "data inserted successfully",
                                Toast.LENGTH_SHORT).show();


                        ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(selectedItem.getNote_Id());

                        getallNotes();


                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "data not inserted successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                // System.exit(0);

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(contentView);
        dialog.show();

    }
    void addListners_new()
    {

        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                openSlideMenu();


            }
        });

        imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("THE MORE CLICK", "MORE BUTTON PRESS");

                showSettingDialogWith("",NoteListingActvity.this);

            }
        });

        imageButtonFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("THE FOLDER CLICK","FOLDER BUTTON PRESS");

                showMoveToFolderDialog("FOLDERS",NoteListingActvity.this,null);

            }
        });



        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertWithEditText(NoteListingActvity.this);

            }
        });


    }
    void  getAllFolder()
    {
        //SideMenuitems //getAllFolder



   arrDataFolder = androidOpenDbHelperObj
                .getAllFolder();

//        for (DBNoteItems dbNoteItems : arrinsertedNote) {
//            System.out.println("Foldr id: " + dbNoteItems.getNote_Id()
//                    + " Foldr_title: " + dbNoteItems.getNote_Title());
//
//            SideMenuitems item1 = new SideMenuitems();
//            item1.setMenuid(dbNoteItems.getNote_Id());
//            item1.setMenuName(dbNoteItems.getNote_Title());
//            arrDataFolder.add(item1);
//        }

    }
    void getallNotes()
    {
        //arrDataNote.clear();
        arrDBDataNote.clear();

        ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
                .getAllNotes();


        for (DBNoteItems dbNoteItems : arrinsertedNote)
        {
            System.out.println("note id: " + dbNoteItems.getNote_Id()
                    + " note_title: " + dbNoteItems.getNote_Title());


            ArrayList<DBNoteItemElement> dbNoteItemElements=androidOpenDbHelperObj.getLastNotesElementWithNote_Id(dbNoteItems.getNote_Id());

            dbNoteItems.setNote_Element("" + (dbNoteItemElements.size() > 0 ? dbNoteItemElements.get(0).getNOTE_ELEMENT_TYPE() : ""));


            if (dbNoteItems.getNote_Color().length() > 0
                    && dbNoteItems.getNote_Color() != null) {

               // item1.setColours("#" + dbNoteItems.getNote_Color());
            }



            if (dbNoteItems.getNote_TimeBomb() !=null)

            {
                if (dbNoteItems.getNote_TimeBomb().equalsIgnoreCase("0"))
                {

                    Log.d("time bomb", "not set for this note");
                    //arrDataNote.add(item1);
                    arrDBDataNote.add(dbNoteItems);
                }
                else
                {
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss", Locale.US);


                    Date currentdate= new Date();
                    String strDate = formatter.format(currentdate);


                    try {

                        Date timebombdate = formatter.parse(dbNoteItems.getNote_TimeBomb());

                        Date dateCurrent = formatter.parse(strDate);

                        System.out.println(timebombdate);
                        System.out.println(formatter.format(timebombdate));

                        if(dateCurrent.compareTo(timebombdate)>0)
                        {
                            System.out.println("Date1 is after Date2 --Delete note here");

                            androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
                        }
                        else if(dateCurrent.compareTo(timebombdate)<0)
                        {
                            System.out.println("Date1 is before Date2");


                            arrDBDataNote.add(dbNoteItems);
                        }
                        else if(dateCurrent.compareTo(timebombdate)==0)
                        {
                            System.out.println("Date1 is equal to Date2");
                            //arrDataNote.add(item1);
                            //arrDBDataNote.add(dbNoteItems);
                        }


                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }


        }
        adapter_new.notifyDataSetChanged();

    }
    void showAlertWithEditText(Context context)
    {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate your activity layout here!

        // LinearLayout layoutAlertbox1=

        View contentView = inflater.inflate(R.layout.edit_alert_view, null,
                false);
        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("CRATE A NOTE");
        textViewTitleAlert.setTextColor(Color.WHITE);
        final EditText textViewTitleAlertMessage = (EditText) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        // textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (textViewTitleAlertMessage.getText().toString().length() > 0) {

                    // Call insert db call

                    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss");
                    dateFormatGmt.setTimeZone(TimeZone.getDefault());
                    //dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date d = new Date();
                    String strDate = dateFormatGmt.format(d);


                    System.out.println("The note created date: " + strDate
                            + "note title: "
                            + textViewTitleAlertMessage.getText().toString());

                    boolean status = androidOpenDbHelperObj.databaseInsertion(
                            textViewTitleAlertMessage.getText().toString(),
                            "ffffff", strDate, strDate, "0", "0", "1234", "",
                            "", "", "0");




                    if (status == true)
                    {
                        Toast.makeText(NoteListingActvity.this,
                                "data inserted successfully",
                                Toast.LENGTH_SHORT).show();

                        getNoteWithTitle(textViewTitleAlertMessage.getText()
                                .toString());

                        DBNoteItems item=arrDBDataNote.get(arrDBDataNote.size() - 1);

                       // ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(item.getNote_Id());

                        if (item!=null)
                        {
                            DataManager.sharedDataManager().setSeletedDBNoteItem(item);
                            startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));

                        }


                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "data not inserted successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                // System.exit(0);

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations =  R.style.dialog_animation;
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.show();

    }
    void getNoteWithTitle(String note_title)
    {
        ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj.getAllNotesWithTitle(note_title);

        for (DBNoteItems dbNoteItems : arrinsertedNote)
        {
            System.out.println("note id: " + dbNoteItems.getNote_Id()
                    + " note_title: " + dbNoteItems.getNote_Title());
            arrDBDataNote.add(dbNoteItems);
        }
        adapter_new.notifyDataSetChanged();

    }

    void  initilizesDateAndTime()
    {

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSeconds = c.get(Calendar.SECOND);

        mMonth=mMonth+1;

        TimeZone tz = c.getTimeZone();
        Log.d("Time zone","="+tz.getDisplayName());

        String tempMonth="";
        String tempDay="";

        if (mMonth>9)
        {
            tempMonth=mMonth+"";
        }else
        {
            tempMonth="0"+mMonth;
        }

        if (mDay>9)
        {
            tempDay=""+mDay;
        }
        else
        {
            tempDay="0"+mDay;
        }

        String tempHours="";
        String tempMiutes="";
        String tempSeconds="";

        if (mHour>9)
        {
            tempHours=mHour+"";
        }else
        {
            tempHours="0"+mHour;
        }

        if (mMinute>9)
        {
            tempMiutes=""+mMinute;
        }
        else
        {
            tempMiutes="0"+mMinute;
        }

        if (mSeconds>9)
        {
            tempSeconds=""+mSeconds;
        }
        else
        {
            tempSeconds="0"+mSeconds;
        }


        selecteddate=tempDay+"/"+tempMonth+"/"+mYear;

        selectedTime=tempHours+":"+tempMiutes+":"+tempSeconds;


    }


    // Method automatically gets Called when you call showDialog()  method
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // create a new DatePickerDialog with values you want to show
                DatePickerDialog datePickerDialog=new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth-1, mDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.setOnDismissListener(dissmissDatePickerDialog);



                return datePickerDialog;

            // create a new TimePickerDialog with values you want to show
            case TIME_DIALOG_ID:
            {

                TimePickerDialog timePickerDialog=new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, true);
                timePickerDialog.setOnDismissListener(dissmissTimePickerDialog);

                return timePickerDialog;

            }


        }
        return null;
    }
    // Register  TimePickerDialog listener
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                // the callback received when the user "sets" the TimePickerDialog in the dialog
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {

                    mHour = hourOfDay;
                    mMinute = min;
                    mSeconds=c.get(Calendar.SECOND);
                    TimeZone tz = c.getTimeZone();
                    Log.d("Time zone","="+tz.getDisplayName());
                    String tempHours="";
                    String tempMiutes="";
                    String tempSeconds="";

                    if (mHour>9)
                    {
                        tempHours=mHour+"";
                    }else
                    {
                        tempHours="0"+mHour;
                    }

                    if (mMinute>9)
                    {
                        tempMiutes=""+mMinute;
                    }
                    else
                    {
                        tempMiutes="0"+mMinute;
                    }

                    if (mSeconds>9)
                    {
                        tempSeconds=""+mSeconds;
                    }
                    else
                    {
                        tempSeconds="0"+mSeconds;
                    }


                    selectedTime=tempHours+":"+tempMiutes+":"+tempSeconds;
                    Toast.makeText(NoteListingActvity.this,"Selected Time:"+selectedTime,Toast.LENGTH_SHORT).show();

                    selecteddateAndTime();

                }
            };
    // Register  DatePickerDialog listener
    private DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {
                // the callback received when the user "sets" the Date in the DatePickerDialog
                public void onDateSet(DatePicker view, int yearSelected,
                                      int monthOfYear, int dayOfMonth) {
                    year = yearSelected;
                    month = monthOfYear+1;
                    day = dayOfMonth;
                    // Set the Selected Date in Select date Button
                    //btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);

                    String tempMonth="";
                    String tempDay="";

                    if (month>9)
                    {
                        tempMonth=month+"";
                    }else
                    {
                        tempMonth="0"+month;
                    }

                    if (day>9)
                    {
                        tempDay=""+day;
                    }
                    else
                    {
                        tempDay="0"+day;
                    }


                    selecteddate=tempDay+"/"+tempMonth+"/"+year;

                    Toast.makeText(NoteListingActvity.this,"Selected date:"+selecteddate,Toast.LENGTH_SHORT).show();

                    showDialog(TIME_DIALOG_ID);

                }
            };
    private DatePickerDialog.OnDismissListener dissmissDatePickerDialog=new DatePickerDialog.OnDismissListener()
    {

        @Override
        public void onDismiss(DialogInterface dialogInterface)
        {
            //showDialog(TIME_DIALOG_ID);
        }
    };

    private TimePickerDialog.OnDismissListener dissmissTimePickerDialog=new TimePickerDialog.OnDismissListener()
    {

        @Override
        public void onDismiss(DialogInterface dialogInterface)
        {
            //selecteddateAndTime();
        }
    };

    void  selecteddateAndTime()
    {

        String seletecdFinal = selecteddate + " " + selectedTime;
        //Log.d("the selected dat", "" + seletecdFinal);



        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.US);

        //formatter.setTimeZone(TimeZone.getDefault());
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));



        try {

            Date timebombdate = formatter.parse(seletecdFinal);
            String strDate = formatter.format(timebombdate);

            Toast.makeText(NoteListingActvity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

            selectedDbItem.setNote_TimeBomb(strDate);
            boolean status=androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDbItem);


            if (status == true)
            {
                Toast.makeText(NoteListingActvity.this,
                        "time bomb set successfully",
                        Toast.LENGTH_SHORT).show();
                getallNotes();

            } else {

                Toast.makeText(NoteListingActvity.this,
                        "timebomb not set successfully",
                        Toast.LENGTH_SHORT).show();
            }


        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    void showAlertToLockNUnlockNote(final DBNoteItems items, Context context, final boolean islock, final boolean isOpenLockedNote)
    {


	/*
		dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_screen);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
		window.setAttributes(wlp);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		dialog.show();*/

        //final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);



        final Dialog dialog = new Dialog(context);

        final StringBuilder lock = new StringBuilder(4);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.notesharelockactivity, null, false);


        final TextView textLock=(TextView)contentView.findViewById(R.id.textViewLock);
        TextView headerText=(TextView)contentView.findViewById(R.id.headerText);

        final Button btn1=(Button)contentView.findViewById(R.id.button);
        final Button btn2=(Button)contentView.findViewById(R.id.button1);
        final Button btn3=(Button)contentView.findViewById(R.id.button2);
        final Button btn4=(Button)contentView.findViewById(R.id.button3);
        final Button btn5=(Button)contentView.findViewById(R.id.button4);
        final Button btn6=(Button)contentView.findViewById(R.id.button5);
        final Button btn7=(Button)contentView.findViewById(R.id.button6);
        final Button btn8=(Button)contentView.findViewById(R.id.button7);
        final Button btn9=(Button)contentView.findViewById(R.id.button8);
        final Button btn10=(Button)contentView.findViewById(R.id.button9);
        final Button btn11=(Button)contentView.findViewById(R.id.button10);
        final Button btn12=(Button)contentView.findViewById(R.id.button11);



        preference= PreferenceManager.getDefaultSharedPreferences(NoteListingActvity.this);
        if (!preference.getString(SAVELOCK,"").equalsIgnoreCase(""))
        {


            textLock.setHint("Password to Unlock Note");

        }else
        {

            textLock.setHint("Create password to Lock Note");
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn1.getText());

                //textLock.setText(lock.toString());
                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn2.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock, lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn3.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn4.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn5.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn6.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn7.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn8.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn9.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lock.append(btn11.getText());

                //textLock.setText(lock.toString());

                updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //lock.append(btn2.getText());

                //textLock.setText(lock.toString());

                //updateLockLabel(textLock,lock.toString());
                //Exit

                dialog.dismiss();



            }
        });

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //lock.append(btn2.getText());

                //textLock.setText(lock.toString());

                //updateLockLabel(textLock,lock.toString());
                //clear


                //dialog.dismiss();

                textLock.setText("");

                lock.delete(0,lock.length());

            }
        });







        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		/*Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
		window.setAttributes(wlp);
		dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);*/



        dialog.setCancelable(true);

        dialog.setContentView(contentView);
        dialog.show();

    }
    void updateLockLabel(TextView textLock,String strLock,Dialog dialog,final boolean islock,boolean isOpenLockedNote)
    {

        textLock.setText(strLock);;

        if (textLock.getText().length()==4)
        {
            preference= PreferenceManager.getDefaultSharedPreferences(NoteListingActvity.this);
            if (!preference.getString(SAVELOCK,"").equalsIgnoreCase(""))
            {
                if (preference.getString(SAVELOCK,"").equalsIgnoreCase(textLock.getText().toString()))
                {

                    Toast.makeText(NoteListingActvity.this, "Password matched,note Open.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    if (isOpenLockedNote==false)
                    {

                        if (islock==true)
                        {
                            selectedDbItem.setNote_Lock_Status("1");
                            boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                            if (status==true)
                            {

                                Toast.makeText(NoteListingActvity.this, "Note locked", Toast.LENGTH_SHORT).show();

                            }else
                            {

                                Toast.makeText(NoteListingActvity.this, "Note Not locked", Toast.LENGTH_SHORT).show();
                            }


                            getallNotes();

                        }else
                        {

                            selectedDbItem.setNote_Lock_Status("0");
                            boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                            if (status==true)
                            {

                                Toast.makeText(NoteListingActvity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

                            }else
                            {

                                Toast.makeText(NoteListingActvity.this, "Note Not Unlocked", Toast.LENGTH_SHORT).show();
                            }

                            getallNotes();
                        }
                    }
                    else {

                        DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDbItem);
                        startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));
                    }

                }
                else
                {
                    //textLock.setText("");

                    Toast.makeText(NoteListingActvity.this,"Password not matched",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }else
            {
                SharedPreferences.Editor editor=preference.edit();
                editor.putString(SAVELOCK, textLock.getText() + "");
                editor.commit();

//				selectedDbItem.setNote_Lock_Status("1");
//				boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);
//
//
//				if (status==true)
//				{
//
//					Toast.makeText(MainActivity.this, "Password Created,Note locked", Toast.LENGTH_SHORT).show();
//
//				}else
//				{
//
//					Toast.makeText(MainActivity.this, "Password Created,Note Not locked", Toast.LENGTH_SHORT).show();
//				}


                if (islock==true)
                {
                    selectedDbItem.setNote_Lock_Status("1");
                    boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                    if (status==true)
                    {

                        Toast.makeText(NoteListingActvity.this, "Note locked", Toast.LENGTH_SHORT).show();

                    }else
                    {

                        Toast.makeText(NoteListingActvity.this, "Note Not locked", Toast.LENGTH_SHORT).show();
                    }

                }else
                {

                    selectedDbItem.setNote_Lock_Status("0");
                    boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                    if (status==true)
                    {

                        Toast.makeText(NoteListingActvity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

                    }else
                    {

                        Toast.makeText(NoteListingActvity.this, "Note Not Unlocked", Toast.LENGTH_SHORT).show();
                    }
                }



                dialog.dismiss();

            }




        }
    }



    @Override
    protected void onResume()
    {

        super.onResume();
        getallNotes();
        Log.d("Onresume","Onresume Call");

    }

}
