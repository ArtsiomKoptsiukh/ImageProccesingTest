package artifox.cry.by.imageproccesingtest;


import java.util.List;
import java.util.UUID;

public class Image {
    private UUID mImageID;
    private int mUrl;
    private boolean checked;
    private List<Image> mImages;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Image() {
        mImageID = UUID.randomUUID();
    }

    public UUID getImageID() {
        return mImageID;
    }

    public int getUrl() {
        return mUrl;
    }

    public void setUrl(int url) {
        mUrl = url;
    }
}
