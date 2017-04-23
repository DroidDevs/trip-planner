package droiddevs.com.tripplanner.model.fb;

/**
 * Created by elmira on 4/12/17.
 */

public class FbUser {
    String id;

    String name;

    Cover cover;

    Picture picture;

    String email;

    public FbUser() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "FbUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + getPicture().data.getUrl() + "\'" +
                ", coverPhotoUrl='" + getCover().getSource() + '\'' +
                '}';
    }
}
