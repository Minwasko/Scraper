package belita;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private String title;
    private List<Product> products = new ArrayList<>();

    public Line(String title) {
        this.title = title;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public String getTitle() {
        return title;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "belita.Line{" +
                "title='" + title + '\'' +
                ", products=" + products +
                '}';
    }
}
