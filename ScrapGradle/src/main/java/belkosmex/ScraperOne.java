package belkosmex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class ScraperOne {

    private static final String URL_START = "https://belkosmex.by";
    private static String FILE_NAME = "belkosmexEN.csv";
    private static String RUSSIAN_ENGLISH_URL_FACE = "/en/catalog/for_the_face/";
    private static String RUSSIAN_ENGLISH_URL_BODY = "/en/catalog/body/";
    private static String RUSSIAN_ENGLISH_URL_HAIR = "/en/catalog/hair/";
    private static String RUSSIAN_ENGLISH_URL_CHILDREN = "/en/catalog/baby_care/";
    private static String RUSSIAN_ENGLISH_URL_SUN = "/en/catalog/sun/";
    private static String FACE = "Skincare";
    private static String BODY = "Body Care";
    private static String HAIR = "Hair Care";
    private static String CHILDREN = "Children Care";
    private static String SUN = "Sunscreen";

    private CSVWriter writer;
    private String catalogName;
    private String subCatalogName;
    private String subCatalogLink;
    private Elements subCatalogContent;
    private Elements subCatalogProducts;
    private Elements productDescription;
    private List<Product> allProducts = new LinkedList<>();

    // FOR RUSSIAN VERSION REMOVE /en/ FOR CATALOG NAMES, CHANGE catalogName AND CHANGE 1st 2 STRINGS IN PRODUCT CLASS

    public void setLanguage(String language) {
        if (language.equals("English")) {
            FILE_NAME = "belkosmexEN.csv";
            FACE = "Skincare";
            BODY = "Body Care";
            HAIR = "Hair Care";
            CHILDREN = "Children Care";
            SUN = "Sunscreen";
            RUSSIAN_ENGLISH_URL_FACE = "/en/catalog/for_the_face/";
            RUSSIAN_ENGLISH_URL_BODY = "/en/catalog/body/";
            RUSSIAN_ENGLISH_URL_HAIR = "/en/catalog/hair/";
            RUSSIAN_ENGLISH_URL_CHILDREN = "/en/catalog/baby_care/";
            RUSSIAN_ENGLISH_URL_SUN = "/en/catalog/sun/";
            Product.productNavigationOne = "Main";
            Product.productNavigationTwo = "Catalog";
        } else if (language.equals("Russian")) {
            FILE_NAME = "belkosmexRU.csv";
            FACE = "Для лица";
            BODY = "Для тела";
            HAIR = "Для волос";
            CHILDREN = "Детская косметика";
            SUN = "Защита от солнца";
            RUSSIAN_ENGLISH_URL_FACE = "/catalog/for_the_face/";
            RUSSIAN_ENGLISH_URL_BODY = "/catalog/body/";
            RUSSIAN_ENGLISH_URL_HAIR = "/catalog/hair/";
            RUSSIAN_ENGLISH_URL_CHILDREN = "/catalog/baby_care/";
            RUSSIAN_ENGLISH_URL_SUN = "/catalog/sun/";
            Product.productNavigationOne = "Главная";
            Product.productNavigationTwo = "Каталог";
        }
    }

    public boolean getAllLines() throws IOException {
        File directory = new File(".");
        FileOutputStream os = new FileOutputStream(directory.getCanonicalPath() + File.separator + FILE_NAME);
        System.out.println(directory.getCanonicalPath() + File.separator + FILE_NAME);
        os.write(0xef);
        os.write(0xbb);
        os.write(0xbf);
        writer = new CSVWriter(new OutputStreamWriter(os));
        System.out.println(directory.getCanonicalPath() + File.separator + FILE_NAME);
        String[] header = {"title", "lineTitle", "volume", "link", "picture", "information", "usage", "composition", "navigation1", "navigation2", "navigation3", "navigation4"};
        writer.writeNext(header);
        // for face
        Elements catalogFace = Jsoup.connect(URL_START + RUSSIAN_ENGLISH_URL_FACE).get().select("div.g-main").select("div.row");
        Elements subCatalogFace = catalogFace.select("aside.g-sidebar").select("li.item_1").get(0).select("ul.menu_level_2").select("li.item_2");
        subCatalogName = "";
        catalogName = FACE;
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
        Elements catalogBody = Jsoup.connect(URL_START + RUSSIAN_ENGLISH_URL_BODY).get().select("div.g-main").select("div.row");
        Elements subCatalogBody = catalogBody.select("aside.g-sidebar").select("li.item_1").get(1).select("ul.menu_level_2").select("li.item_2");
        catalogName = BODY;
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
        Elements catalogHair = Jsoup.connect(URL_START + RUSSIAN_ENGLISH_URL_HAIR).get().select("div.g-main").select("div.row");
        Elements subCatalogHair = catalogHair.select("aside.g-sidebar").select("li.item_1").get(2).select("ul.menu_level_2").select("li.item_2");
        catalogName = HAIR;
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
        Elements catalogBabyCare = Jsoup.connect(URL_START + RUSSIAN_ENGLISH_URL_CHILDREN).get().select("div.g-main").select("div.row");
        Element subCatalogBabyCare = catalogBabyCare.select("aside.g-sidebar").select("li.item_1").get(3);
        catalogName = CHILDREN;
        getProductsFromPage(URL_START + RUSSIAN_ENGLISH_URL_CHILDREN);
        // SUN CARE
        Elements catalogSunCare = Jsoup.connect(URL_START + RUSSIAN_ENGLISH_URL_SUN).get().select("div.g-main").select("div.row");
        Element subCatalogSunCare = catalogBabyCare.select("aside.g-sidebar").select("li.item_1").get(4);
        catalogName = SUN;
        getProductsFromPage(URL_START + RUSSIAN_ENGLISH_URL_SUN);
//        System.out.println(allProducts.toString());
        writer.close();
        return true;
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
}
