package comvc.ebtabsss.tabs;

public class Fragment_listPDF_SingleItem {
	 public String iconID;
	 public String title;
	 public String items;
	 public String date;
	 public String path;
	 
	 Fragment_listPDF_SingleItem(String iconID, String title, String items, String date, String path){
		 this.iconID = iconID;
		 this.title = title;
		 this.items = items;
		 this.date = date;
		 this.path = path;
	 }

	public String getImage() {
		// TODO Auto-generated method stub
		return iconID;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	public String getData() {
		// TODO Auto-generated method stub
		return items; 
	}




}
