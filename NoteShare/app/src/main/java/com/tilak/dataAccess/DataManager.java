package com.tilak.dataAccess;

import android.graphics.Bitmap;

import com.tilak.datamodels.NOTETYPE;
import com.tilak.datamodels.NoteListDataModel;
import com.tilak.datamodels.SideMenuitems;
import com.tilak.notesharedatabase.DBNoteItems;

import java.util.ArrayList;





public class DataManager 
{

	public static final String Mylock = "lock" ;

	public static DataManager manager;
	public Bitmap userImageBitMap;
	public boolean typeofListView;
	public NOTETYPE SELECTED_TEXT_OPTION;
	public SideMenuitems seletedListNoteItem;
	public DBNoteItems seletedDBNoteItem;

	public int getSelectedItemIndex() {
		return selectedItemIndex;
	}

	public void setSelectedItemIndex(int selectedItemIndex) {
		this.selectedItemIndex = selectedItemIndex;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setIsExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	int selectedItemIndex;
	boolean isExpanded;
	
	 public DBNoteItems getSeletedDBNoteItem() {
		return seletedDBNoteItem;
	}

	public void setSeletedDBNoteItem(DBNoteItems seletedDBNoteItem) {
		this.seletedDBNoteItem = seletedDBNoteItem;
	}

	int selectedIndex;

	public String getSelectedFolderId() {
		return selectedFolderId;
	}

	public void setSelectedFolderId(String selectedFolderId) {
		this.selectedFolderId = selectedFolderId;
	}

	String selectedFolderId;

	
	public SideMenuitems getSeletedListNoteItem() {
		return seletedListNoteItem;
	}

	public void setSeletedListNoteItem(SideMenuitems seletedListNoteItem) {
		this.seletedListNoteItem = seletedListNoteItem;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public NOTETYPE getSELECTED_TEXT_OPTION() 
	{
		return SELECTED_TEXT_OPTION;
	}

	public void setSELECTED_TEXT_OPTION(NOTETYPE sELECTED_TEXT_OPTION) {
		SELECTED_TEXT_OPTION = sELECTED_TEXT_OPTION;
	}

	public ArrayList<NoteListDataModel> arrNoteListData;
	
	
	NoteListDataModel notelistData;
	

	
	public NoteListDataModel getNotelistData() {
		return notelistData;
	}

	public void setNotelistData(NoteListDataModel notelistData) {
		this.notelistData = notelistData;
	}

	public static DataManager sharedDataManager()
	{
		if(manager==null)
		{
			manager=new DataManager();
		}
		
		return manager;
	}
	
	public void printname()
	{
		System.out.println("DataManager.printname()");
	}
	
	public Bitmap getUserImageBitMap() 
	{
		return userImageBitMap;
	}

	public void setUserImageBitMap(Bitmap userImageBitMap) 
	{
		this.userImageBitMap = userImageBitMap;
	}
	
	public boolean isTypeofListView() {
		return typeofListView;
	}

	public void setTypeofListView(boolean typeofListView) {
		this.typeofListView = typeofListView;
	}
	
	public ArrayList<NoteListDataModel> getArrNoteListData() {
		return arrNoteListData;
	}

	public void setArrNoteListData(ArrayList<NoteListDataModel> arrNoteListData) {
		this.arrNoteListData = arrNoteListData;
	}

}
