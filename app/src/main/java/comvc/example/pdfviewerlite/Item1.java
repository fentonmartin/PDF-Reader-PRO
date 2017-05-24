package comvc.example.pdfviewerlite;

public class Item1 implements Comparable<Item1>{
	private String name;
	private String data;
	private String date;
	private String path;
	private String image;
	int position;
	public Item1(String n,String d, String dt, String p, String img)
	{
		name = n;
		data = d;
		date = dt;
		path = p; 
		image = img;
		this.position = position;
	}
	
	public int getPosition()
	{
		return position;
	}
	public String getName()
	{
		return name;
	}
	public String getData()
	{
		return data;
	}
	public String getDate()
	{
		return date;
	}
	public String getPath()
	{
		return path;
	}
	public String getImage() {
		return image;
	}
	
	public int compareTo(Item1 o) {
		if(this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
}
