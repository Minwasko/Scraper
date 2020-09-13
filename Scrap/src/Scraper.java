import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Scraper {

    private static final String URL_START = "http://belita.by";

    List<Line> lines = new LinkedList<>();
    private CSVWriter writer;

    public void getAllLines() throws IOException {
        writer = new CSVWriter(new FileWriter("C:\\Users\\Minwa\\IdeaProjects\\Scraper\\"
                + "Scrap\\src\\data.csv"));
        String[] header = {"title", "lineTitle", "purpose1",
                "purpose2", "purpose3", "purpose4", "purpose5", "purpose6", "purpose7", "purpose8", "purpose9", "purpose10",
                "productLink", "picture", "information", "brand", "volume", "barcode", "composition", "navigation1", "navigation2", "navigation3", "navigation4", "navigation5"};
        writer.writeNext(header);
        int n = 0;
        Elements lines = Jsoup.connect(URL_START + "/en/brendy/").get().select("div.brands-list");
        for (Element line : lines.select("div.item")) {
            getProductsForLine(URL_START + line.select("a.item_w").attr("href"),
                    line.select("a.item_w").attr("title"));
            n++;
            if (n == 5) {
                break;
            }
        }
        writer.close();
    }

    public void getProductsForLine(String lineURL, String lineTitle) throws IOException {
        Elements products = Jsoup.connect(lineURL).get().select("div.sl-catalog-wrap");
        Line line = new Line(lineTitle);
        lines.add(line);
        for (Element product : products.select("div.slide")) {
            Product wantedProduct = new Product();
            Elements productSpans = product.select("span.text");
            if (productSpans.size() > 1) {
                String purpose = product.select("div.descr-param").select("span.text").get(1).text(); // ADDED PRODUCT PURPOSE
                String[] purposes = purpose.split(",");
                try {
                    wantedProduct.setProductPurposeOne(purposes[1]);
                    wantedProduct.setProductPurposeTwo(purposes[2]);
                    wantedProduct.setProductPurposeThree(purposes[3]);
                    wantedProduct.setProductPurposeFour(purposes[4]);
                    wantedProduct.setProductPurposeFive(purposes[5]);
                    wantedProduct.setProductPurposeSix(purposes[6]);
                    wantedProduct.setProductPurposeSeven(purposes[7]);
                    wantedProduct.setProductPurposeEight(purposes[8]);
                    wantedProduct.setProductPurposeNine(purposes[9]);
                    wantedProduct.setProductPurposeTen(purposes[10]);
                } catch (Exception ignored) {
                }
            } else {
                wantedProduct.setProductPurposeOne("No purpose given"); // ADDED PRODUCT PURPOSE IF NONE WAS GIVEN
            }
            wantedProduct.setTitle(product.select("div.ttl").text()); // ADDED PRODUCT TITLE
            wantedProduct.setLineTitle(lineTitle); // ADDED PRODUCT LINE TITLE
            getProductInfo(URL_START + product.select("a").attr("href"), wantedProduct);
            line.addProduct(wantedProduct);
//            String[] header = {"title", "lineTitle", "purpose1",
//                    "purpose2", "purpose3", "purpose4", "purpose5", "purpose6", "purpose7", "purpose8", "purpose9", "purpose10",
//                    "productLink", "picture", "information", "brand", "volume", "barcode", "composition", "navigation1", "navigation2", "navigation3", "navigation4", "navigation5"};
            this.writer.writeNext(new String[]{wantedProduct.getTitle(), wantedProduct.getLineTitle(), wantedProduct.getProductPurposeOne(), wantedProduct.getProductPurposeTwo(),
                    wantedProduct.getProductPurposeThree(), wantedProduct.getProductPurposeFour(), wantedProduct.getProductPurposeFive(), wantedProduct.getProductPurposeSix(),
                    wantedProduct.getProductPurposeSeven(), wantedProduct.getProductPurposeEight(), wantedProduct.getProductPurposeNine(), wantedProduct.getProductPurposeTen(),
                    wantedProduct.getProductLink(), wantedProduct.getProductPicture(), wantedProduct.getProductInformation(), wantedProduct.getProductBrand(),
                    wantedProduct.getProductVolume(), wantedProduct.getProductBarcode(), wantedProduct.getProductComposition(), wantedProduct.getProductNavigationOne(),
                    wantedProduct.getProductNavigationTwo(), wantedProduct.getProductNavigationThree(), wantedProduct.getProductNavigationFour(), wantedProduct.getProductNavigationFive()});
        }
        lines.add(line);

    }

    public Product getProductInfo(String productURL, Product product) throws IOException {
        System.out.println(productURL);
        product.setProductLink(productURL); // ADDED PRODUCT LINK
        Elements productInfo = Jsoup.connect(productURL).get().select("div.product");
        product.setProductPicture(URL_START + productInfo.select("div.product-pic").select("div.product-pic-wrap").select("a[href]")
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
        Elements productNavigationTabs = productInfo.select("div.breadcrumbs").select("li.item");
        try { // ADDING PRODUCT NAVIGATION
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
