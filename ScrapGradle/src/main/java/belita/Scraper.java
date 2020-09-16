package belita;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Scraper {

    private static String URL_START = "http://belita.by";
    private static String ENGLISH_RUSSIAN_URL = "/brendy/";
    private static String FILE_NAME = "belita";

    List<Line> lines = new LinkedList<>();
    private CSVWriter writer;


    public void setLanguage(String language) {
        if (language.equals("English")) {
            ENGLISH_RUSSIAN_URL = "/en/brendy/";
            FILE_NAME = "belitaEN.csv";
        } else if (language.equals("Russian")){
            ENGLISH_RUSSIAN_URL = "/brendy/";
            FILE_NAME = "belitaRU.csv";
        }
    }

    public void getAllLines() throws IOException {
        File directory = new File(".");
        FileOutputStream os = new FileOutputStream(directory.getCanonicalPath() + File.separator + FILE_NAME);
        System.out.println(directory.getCanonicalPath() + File.separator + FILE_NAME);
        os.write(0xef);
        os.write(0xbb);
        os.write(0xbf);
        writer = new CSVWriter(new OutputStreamWriter(os));
        System.out.println(directory.getCanonicalPath() + File.separator + FILE_NAME);
        String[] header = {"title", "lineTitle", "purpose1",
                "purpose2", "purpose3", "purpose4", "purpose5", "purpose6", "purpose7", "purpose8", "purpose9", "purpose10",
                "productLink", "picture", "information", "brand", "volume", "barcode", "composition", "navigation1", "navigation2", "navigation3", "navigation4", "navigation5"};
        writer.writeNext(header);
        Elements lines = Jsoup.connect(URL_START + ENGLISH_RUSSIAN_URL).get().select("div.brands-list");
        for (Element line : lines.select("div.item")) {
            getProductsForLine(URL_START + line.select("a.item_w").attr("href"),
                    line.select("a.item_w").attr("title"));
        }
        writer.close();
    }

    public void getProductsForLine(String lineURL, String lineTitle) throws IOException {
        Elements products = Jsoup.connect(lineURL).get().select("div.sl-catalog-wrap");
        String anotherTitle = Jsoup.connect(lineURL).get().select("div.container").select("div.b-content_wrap").select("h1").text();
        Line line = new Line(lineTitle);
        lines.add(line);
        for (Element product : products.select("div.slide")) {
            Product wantedProduct = new Product();
            Elements productSpans = product.select("span.text");
            if (productSpans.size() > 1) {
                String purpose = product.select("div.descr-param").select("span.text").get(1).text(); // ADDED PRODUCT PURPOSE
                String[] purposes = purpose.split(", ");
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
            }
            wantedProduct.setTitle(product.select("div.ttl").text()); // ADDED PRODUCT TITLE
            wantedProduct.setLineTitle(anotherTitle); // ADDED PRODUCT LINE TITLE
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
        try {
            System.out.println(productURL);
            product.setProductLink(productURL); // ADDED PRODUCT LINK
        } catch (Exception ignored) { }
        Elements productInfo = Jsoup.connect(productURL).get().select("div.product");
        try {
            product.setProductPicture(URL_START + productInfo.select("div.product-pic").select("div.product-pic-wrap").select("a[href]")
                    .attr("href")); // ADDED PRODUCT PICTURE
        } catch (Exception ignored) { }
        Elements productInformation = productInfo.select("div.product-tab").select("div#product1");
        try {
            StringBuilder builder = new StringBuilder();
            for (Element information : productInformation.select("p")) {
                if (information.text().equals(productInformation.select("p").get(productInformation.select("p").size() - 1).text())) {
                    builder.append(information.text());
                } else if (information.text().length() > 0) {
                    builder.append(information.text()).append("\n");
                }
            }
            product.setProductInformation(builder.toString());
        } catch (Exception ignored) { }
        Elements productCharacteristics = productInfo.select("div.product-tab").select("div#product2").select("p");
        try {
            product.setProductBrand(productCharacteristics.get(0).text().replace("Brend: ", "")); // ADDED PRODUCT BRAND
        } catch (Exception ignored) { }
        try {
            product.setProductVolume(productCharacteristics.get(1).text().replace("Volume: ", "")); // ADDED PRODUCT VOLUME
        } catch (Exception ignored) { }
        try {
            product.setProductBarcode(productCharacteristics.get(2).text().replace("Barcode: ", "")); // ADDED PRODUCT BARCODE
        } catch (Exception ignored) { }
        try {
            product.setProductComposition(productCharacteristics.get(3).text().replace("Composition: ", "")); // ADDED PRODUCT COMPOSITION
        } catch (Exception ignored) { }
        try {
            Elements productNavigationTabs = productInfo.select("div.breadcrumbs").select("li.item");
            // ADDING PRODUCT NAVIGATION
            product.setProductNavigationOne(productNavigationTabs.get(0).text());
            product.setProductNavigationTwo(productNavigationTabs.get(1).text());
            product.setProductNavigationThree(productNavigationTabs.get(2).text());
            product.setProductNavigationFour(productNavigationTabs.get(3).text());
            product.setProductNavigationFive(productNavigationTabs.get(4).text());
        } catch (Exception ignored) { }
        return product;
    }
}
