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

    public void getAllLines() throws IOException {
        writer = new CSVWriter(new FileWriter("C:\\Users\\Minwa\\IdeaProjects\\Scraper\\Scrap\\src\\belkosmex\\belkosmex.csv"));
        String[] header = {};
        writer.writeNext(header);
        // for face
        Elements catalogFace = Jsoup.connect(URL_START + "/catalog/for_the_face/").get().select("div.g-main").select("div.row");
        Elements subCatalogFace = catalogFace.select("aside.g-sidebar").select("li.item_1").get(0).select("ul.menu_level_2").select("li.item_2");
        String subCatalogName = "";
        catalogName = "Для лица";
        for (Element subCatalog : subCatalogFace) {
            subCatalogName = subCatalog.text();
            subCatalogLink = subCatalog.select("a[href]").attr("href");
            getProductsFromPage(URL_START + subCatalogLink);
        }
        writer.close();
    }

    public void getProductsFromPage(String lineURL) throws IOException {
        subCatalogContent = Jsoup.connect(lineURL).get().select("div.row").select("main.g-content");
        subCatalogProducts = subCatalogContent.select("div.listing").select("div.listing__item");
        Product wantedProduct;
        for (Element product : subCatalogProducts) {
            wantedProduct = new Product();
            wantedProduct.setProductNavigationThree(catalogName); // ADDED PRODUCT NAVIGATION THREE
            wantedProduct.setProductNavigationFour(subCatalogName); // ADDED PRODUCT NAVIGATION FOUR
            String productURL = product.select("a[href]").attr("href");
            Elements productPage = Jsoup.connect(URL_START + productURL).get().select("div.g-main")
                    .select("div.container").select("div.row").select("main.g-content");
            wantedProduct.setTitle(productPage.select("div.card__col--title").select("h1.small").text()); // ADDED TITLE
            productDescription = productPage.select("div.card__col--desc");
            wantedProduct.setLineTitle(productDescription.select("div.product__head").select("a").text()); // ADDED LINE TITLE
            wantedProduct.setProductLink(URL_START + productURL); // ADDED PRODUCT LINK
            wantedProduct.setProductVolume(productDescription.select("div.product__head").select("div.product__param").get(1).text()); // ADDED PRODUCT VOLUME
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
                System.out.println(builder.toString());
                wantedProduct.setProductComposition(builder.toString());
            } catch (Exception ignored) { }
        }
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
