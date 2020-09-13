import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
            System.out.println(line.select("a.item_w").attr("href"));
            getProductsForLine(URL_START + line.select("a.item_w").attr("href"),
                    line.select("a.item_w").attr("title"));
        }
    }

    public void getProductsForLine(String lineURL, String lineTitle) throws IOException {
        Elements products = Jsoup.connect(lineURL).get().select("div.sl-catalog-wrap");
        Line line = new Line(lineTitle);
        System.out.println(line.getTitle());
        lines.add(line);
        for (Element product : products.select("div.slide")) {
            getProductInfo(URL_START + product.select("a").attr("href"));
            break;
        }

    }

    public Product getProductInfo(String productURL) throws IOException {
        Elements productInfo = Jsoup.connect(productURL).get().select("div.product");
        String productTitle = productInfo.select("div.product-head").select("h1.title").get(0).text();
        System.out.println(productTitle);
        String productInformation = productInfo.select("div.product-info").select("div.tab-content").text();
        System.out.println(productInformation.replace("Brend", " :Information | Characteristics: "));
        String productPicture = productInfo.select("div.product-pic").select("div.product-pic-wrap").select("a[href]")
                .attr("href");
        System.out.println(URL_START + productPicture);
        return null;
    }

    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        scraper.getAllLines();
    }
}
