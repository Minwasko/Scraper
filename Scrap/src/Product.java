public class Product {

    private String title;
    private String information;
    private String picture;

    public Product(String title, String information, String picture) {
        this.title = title;
        this.information = information;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", information='" + information + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
