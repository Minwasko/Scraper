import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Scraper {

    private static final String URL_START = "http://belita.by";

    List<Line> lines = new LinkedList<>();
    List<Product> products = new LinkedList<>();

    public void getAllLines() throws IOException {
        Elements lines = Jsoup.connect(URL_START + "/en/brendy/").get().select("div.brands-list");
        for (Element line : lines.select("div.item")) {
            getProductsForLine(URL_START + line.select("a.item_w").attr("href"),
                    line.select("a.item_w").attr("title"));
            break;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        FileWriter writer = new FileWriter("C:\\Users\\Minwa\\IdeaProjects\\Scraper\\"
                + "Scrap\\src\\jsonInfo.json");
        writer.write(gson.toJson(this.lines));
        writer.close();
        System.out.println(this.lines);
    }

    public void getProductsForLine(String lineURL, String lineTitle) throws IOException {
        Elements products = Jsoup.connect(lineURL).get().select("div.sl-catalog-wrap");
        Line line = new Line(lineTitle);
        lines.add(line);
        for (Element product : products.select("div.slide")) {
            Product wantedProduct = new Product();
            Elements productSpans = product.select("span.text");
            if (productSpans.size() > 1) {
            wantedProduct.setProductPurpose(product.select("div.descr-param").select("span.text").text()); // ADDED PRODUCT PURPOSE
            } else {
                wantedProduct.setProductPurpose("No purpose given"); // ADDED PRODUCT PURPOSE IF NONE WAS GIVEN
            }
            wantedProduct.setTitle(product.select("div.ttl").text()); // ADDED PRODUCT TITLE
            wantedProduct.setLineTitle(lineTitle); // ADDED PRODUCT LINE TITLE
            getProductInfo(URL_START + product.select("a").attr("href"), wantedProduct);
            line.addProduct(wantedProduct);
        }
        lines.add(line);

    }

    public Product getProductInfo(String productURL, Product product) throws IOException {
        System.out.println(productURL);
        product.setProductLink(productURL); // ADDED PRODUCT LINK
        Elements productInfo = Jsoup.connect(productURL).get().select("div.product");
        product.setProductPicture(productInfo.select("div.product-pic").select("div.product-pic-wrap").select("a[href]")
                .attr("href")); // ADDED PRODUCT PICTURE
        product.setProductInformation(productInfo.select("div.product-tab").select("div#product1").toString().replace("<p>", "")
                .replace("</p>", "").replace("<br>", "").replace("<div>", "")
                .replace("</div>", "").replace("<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"product1\">", "")
                .replace("<b>", "").replace("</b>", "")); // ADDED PRODUCT INFORMATION
        Elements productCharacteristics = productInfo.select("div.product-tab").select("div#product2").select("p");
        product.setProductBrand(productCharacteristics.get(0).text()); // ADDED PRODUCT BRAND
        product.setProductVolume(productCharacteristics.get(1).text()); // ADDED PRODUCT VOLUME
        product.setProductBarcode(productCharacteristics.get(2).text()); // ADDED PRODUCT BARCODE
        product.setProductComposition(productCharacteristics.get(3).text()); // ADDED PRODUCT COMPOSITION
        // Getting the navigation tab
        Elements productNavigationTabs = productInfo.select("div.breadcrumbs").select("li.item");
        try {
            product.setProductNavigationOne(productNavigationTabs.get(0).text());
            product.setProductNavigationTwo(productNavigationTabs.get(1).text());
            product.setProductNavigationThree(productNavigationTabs.get(2).text());
            product.setProductNavigationFour(productNavigationTabs.get(3).text());
            product.setProductNavigationFive(productNavigationTabs.get(4).text());
        } catch (Exception ignored) {
        }
        return product;
    }

    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        scraper.getAllLines();
    }
}
