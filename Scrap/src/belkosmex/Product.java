package belkosmex;

public class Product {

    private String productNavigationOne = "Main"; //
    private String productNavigationTwo = "Catalog"; //
    private String productNavigationThree; //
    private String productNavigationFour; //
    private String title; //
    private String lineTitle; //
    private String productLink; //
    private String productVolume; //
    private String productPicture;
    private String productInformation; //
    private String productUsage; //
    private String productComposition; //

    public String getProductNavigationOne() {
        return productNavigationOne;
    }

    public void setProductNavigationOne(String productNavigationOne) {
        this.productNavigationOne = productNavigationOne;
    }

    public String getProductNavigationTwo() {
        return productNavigationTwo;
    }

    public void setProductNavigationTwo(String productNavigationTwo) {
        this.productNavigationTwo = productNavigationTwo;
    }

    public String getProductNavigationThree() {
        return productNavigationThree;
    }

    public void setProductNavigationThree(String productNavigationThree) {
        this.productNavigationThree = productNavigationThree;
    }

    public String getProductNavigationFour() {
        return productNavigationFour;
    }

    public void setProductNavigationFour(String productNavigationFour) {
        this.productNavigationFour = productNavigationFour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLineTitle() {
        return lineTitle;
    }

    public void setLineTitle(String lineTitle) {
        this.lineTitle = lineTitle;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(String productVolume) {
        this.productVolume = productVolume;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(String productInformation) {
        this.productInformation = productInformation;
    }

    public String getProductUsage() {
        return productUsage;
    }

    public void setProductUsage(String productUsage) {
        this.productUsage = productUsage;
    }

    public String getProductComposition() {
        return productComposition;
    }

    public void setProductComposition(String productComposition) {
        this.productComposition = productComposition;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNavigationOne='" + productNavigationOne + '\'' +
                ", productNavigationTwo='" + productNavigationTwo + '\'' +
                ", productNavigationThree='" + productNavigationThree + '\'' +
                ", productNavigationFour='" + productNavigationFour + '\'' +
                ", title='" + title + '\'' +
                ", lineTitle='" + lineTitle + '\'' +
                ", productLink='" + productLink + '\'' +
                ", productVolume='" + productVolume + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", productInformation='" + productInformation + '\'' +
                ", productUsage='" + productUsage + '\'' +
                ", productComposition='" + productComposition + '\'' +
                '}';
    }
}
