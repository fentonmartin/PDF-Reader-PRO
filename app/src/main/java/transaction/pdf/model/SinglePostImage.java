package transaction.pdf.model;


public class SinglePostImage {
    public  boolean isHeadder;
    public boolean isCamera;
    public int resource;
    public String resourceTitle;
    public String imgPath;

    public SinglePostImage(boolean isHeadder,
                           boolean isCamera, int resource,
                           String resourceTitle,
                           String imgPath){
        this.isHeadder = isHeadder;
        this.isCamera = isCamera;
        this.resource = resource;
        this.resourceTitle = resourceTitle;
        this.imgPath = imgPath;
    }
}
