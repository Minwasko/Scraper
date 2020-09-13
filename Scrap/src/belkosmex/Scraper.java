package belkosmex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Scraper {

    private static final String URL_START = "https://belkosmex.by";

    private CSVWriter writer;
    private String catalogName;
    private String subCatalogName;
    private String subCatalogLink;
    private Elements subCatalogContent;
    private Elements subCatalogProducts;
    private Elements productDescription;
    private List<Product> allProducts = new LinkedList<>();

    // FOR RUSSIAN VERSION REMOVE /en/ FOR CATALOG NAMES, CHANGE catalogName AND CHANGE 1st 2 STRINGS IN PRODUCT CLASS

    public void getAllLines() throws IOException {
        writer = new CSVWriter(new FileWriter("C:\\Users\\Minwa\\IdeaProjects\\Scraper\\Scrap\\src\\belkosmex\\belkosmexEN.csv"));
        String[] header = {"title", "lineTitle", "volume", "link", "picture", "information", "usage", "composition", "navigation1", "navigation2", "navigation3", "navigation4"};
        writer.writeNext(header);
        // for face
        Elements catalogFace = Jsoup.connect(URL_START + "/en/catalog/for_the_face/").get().select("div.g-main").select("div.row");
        Elements subCatalogFace = catalogFace.select("aside.g-sidebar").select("li.item_1").get(0).select("ul.menu_level_2").select("li.item_2");
        subCatalogName = "";
        catalogName = "Skincare";
        for (Element subCatalog : subCatalogFace) {
            subCatalogName = subCatalog.text();
            subCatalogLink = subCatalog.select("a[href]").attr("href");
            int additionalPages = getProductsFromPage(URL_START + subCatalogLink);
            // checking if the subCatalog has more links
            for (int i = 2; i <= additionalPages; i++) {
                System.out.println("CURRENT PAGE" + i);
                getProductsFromPage(URL_START + subCatalogLink + "/?PAGEN_2=" + i);
            }
        }
        // FOR BODY
        Elements catalogBody = Jsoup.connect(URL_START + "/en/catalog/body/").get().select("div.g-main").select("div.row");
        Elements subCatalogBody = catalogBody.select("aside.g-sidebar").select("li.item_1").get(1).select("ul.menu_level_2").select("li.item_2");
        catalogName = "Body Care";
        for (Element subCatalog : subCatalogBody) {
            subCatalogName = subCatalog.text();
            subCatalogLink = subCatalog.select("a[href]").attr("href");
            int additionalPages = getProductsFromPage(URL_START + subCatalogLink);
            // checking if the subCatalog has more links
            for (int i = 2; i <= additionalPages; i++) {
                System.out.println("CURRENT PAGE" + i);
                getProductsFromPage(URL_START + subCatalogLink + "/?PAGEN_2=" + i);
            }
        }
        // FOR HAIR
        Elements catalogHair = Jsoup.connect(URL_START + "/en/catalog/hair/").get().select("div.g-main").select("div.row");
        Elements subCatalogHair = catalogHair.select("aside.g-sidebar").select("li.item_1").get(2).select("ul.menu_level_2").select("li.item_2");
        catalogName = "Hair Care";
        for (Element subCatalog : subCatalogHair) {
            subCatalogName = subCatalog.text();
            subCatalogLink = subCatalog.select("a[href]").attr("href");
            int additionalPages = getProductsFromPage(URL_START + subCatalogLink);
            // checking if the subCatalog has more links
            for (int i = 2; i <= additionalPages; i++) {
                System.out.println("CURRENT PAGE" + i);
                getProductsFromPage(URL_START + subCatalogLink + "/?PAGEN_2=" + i);
            }
        }
        // BABY CARE
        Elements catalogBabyCare = Jsoup.connect(URL_START + "/en/catalog/baby_care/").get().select("div.g-main").select("div.row");
        Element subCatalogBabyCare = catalogBabyCare.select("aside.g-sidebar").select("li.item_1").get(3);
        catalogName = "Children Care";
        getProductsFromPage(URL_START + "/catalog/baby_care/");
        // SUN CARE
        Elements catalogSunCare = Jsoup.connect(URL_START + "/en/catalog/sun/").get().select("div.g-main").select("div.row");
        Element subCatalogSunCare = catalogBabyCare.select("aside.g-sidebar").select("li.item_1").get(4);
        catalogName = "Sunscreen";
        getProductsFromPage(URL_START + "/catalog/sun/");
//        System.out.println(allProducts.toString());
        writer.close();
    }

    public int getProductsFromPage(String lineURL) throws IOException {
        int additionalPagesCount = 0;
        subCatalogContent = Jsoup.connect(lineURL).get().select("div.row").select("main.g-content");
        try {
            additionalPagesCount = Integer.parseInt(subCatalogContent.select("nav").select("li.page-item").get(subCatalogContent.select("nav").select("li.page-item").size() - 2).text().replace(" (current)", ""));
        } catch (Exception ignored) {
        }
        subCatalogProducts = subCatalogContent.select("div.listing").select("div.listing__item");
        Product wantedProduct;
        for (Element product : subCatalogProducts) {
            wantedProduct = new Product();
            wantedProduct.setProductNavigationThree(catalogName); // ADDED PRODUCT NAVIGATION THREE
            wantedProduct.setProductNavigationFour(subCatalogName); // ADDED PRODUCT NAVIGATION FOUR
            String productURL = product.select("a[href]").attr("href");
            Elements productPage = Jsoup.connect(URL_START + productURL).get().select("div.g-main")
                    .select("div.container").select("div.row").select("main.g-content");
            productDescription = productPage.select("div.card__col--desc");
            try {
                wantedProduct.setProductPicture(URL_START + productPage.select("div.card__col--slider").select("a.inner").attr("href")); // ADDED A PICTURE
            } catch (Exception ignored) { }
            try {
            wantedProduct.setTitle(productPage.select("div.card__col--title").select("h1.small").text()); // ADDED TITLE
            } catch (Exception ignored) { }
            try {
            wantedProduct.setLineTitle(productDescription.select("div.product__head").select("div.product__param").get(0).text()); // ADDED LINE TITLE
            } catch (Exception ignored) { }
            try {
                System.out.println(URL_START + productURL);
            wantedProduct.setProductLink(URL_START + productURL); // ADDED PRODUCT LINK
            } catch (Exception ignored) { }
            try {
            wantedProduct.setProductVolume(productDescription.select("div.product__head").select("div.product__param").get(1).text().replace("Объем: ", "").replace("Volume: ", "")); // ADDED PRODUCT VOLUME
            } catch (Exception ignored) { }
            // ADDING PRODUCT INFORMATION IN THIS TRY BLOCK
            try {
                StringBuilder builder = new StringBuilder();
                    for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("p")) {
                        if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("p")
                                .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("p").size() - 1).text())) {
                        builder.append(line.text());
                        } else if (line.text().length() > 0) {
                            builder.append(line.text()).append("\n");
                        }
                    }
                    for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("li")) {
                        if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("li")
                                .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-00").select("li").size() - 1).text())) {
                            builder.append(line.text());
                        } else if (line.text().length() > 0) {
                            builder.append(line.text()).append("\n");
                        }
                    }
                    wantedProduct.setProductInformation(builder.toString());
                } catch (Exception ignored) { }
            // ADDING PRODUCT USAGE IN THIS TRY BLOCK
            try {
                StringBuilder builder = new StringBuilder();
                for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("p")) {
                    if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("p")
                            .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("p").size() - 1).text())) {
                        builder.append(line.text());
                    } else if (line.text().length() > 0) {
                        builder.append(line.text()).append("\n");
                    }
                }
                for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("li")) {
                    if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("li")
                            .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-01").select("li").size() - 1).text())) {
                        builder.append(line.text());
                    } else if (line.text().length() > 0) {
                        builder.append(line.text()).append("\n");
                    }
                }
                wantedProduct.setProductUsage(builder.toString());
            } catch (Exception ignored) { }
            try {
                StringBuilder builder = new StringBuilder();
                for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("p")) {
                    if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("p")
                            .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("p").size() - 1).text())) {
                        builder.append(line.text());
                    } else if (line.text().length() > 0) {
                        builder.append(line.text()).append("\n");
                    }
                }
                for (Element line : productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("li")) {
                    if (line.text().equals(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("li")
                            .get(productDescription.select("div.tabs-menu").select("div.tab-content").select("div#product-tab-02").select("li").size() - 1).text())) {
                        builder.append(line.text());
                    } else if (line.text().length() > 0) {
                        builder.append(line.text()).append("\n");
                    }
                }
                wantedProduct.setProductComposition(builder.toString());
            } catch (Exception ignored) { }
//            String[] header = {"title", "lineTitle", "volume", "link", "picture", "information", "usage", "composition", "navigation1", "navigation2", "navigation3", "navigation4"};
            writer.writeNext(new String[]{wantedProduct.getTitle(), wantedProduct.getLineTitle(), wantedProduct.getProductVolume(),
                    wantedProduct.getProductLink(), wantedProduct.getProductPicture(), wantedProduct.getProductInformation(), wantedProduct.getProductUsage(),
                    wantedProduct.getProductComposition(), wantedProduct.getProductNavigationOne(), wantedProduct.getProductNavigationTwo(),
                    wantedProduct.getProductNavigationThree(), wantedProduct.getProductNavigationFour()});
            allProducts.add(wantedProduct);
        }
        return additionalPagesCount;
//            this.writer.writeNext(new String[]{});
        }


    public Product getProductInfo(String productURL, Product product) throws IOException {
        return null;
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        Scraper scraper = new Scraper();
        scraper.getAllLines();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }
}
