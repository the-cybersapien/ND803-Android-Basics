package xyz.cybersapien.inventorymanager;

/**
 * Created by ogcybersapien on 23/10/16.
 * Object class to help with Item sales.
 * Maybe there's a better way to do this, but this is the best I could think of after a lot of experiments.
 * I'd really appreciate any suggestions. Thank you!.
 */

public class Item {

    private String name;
    private Integer quantity;
    private Double price;
    private Long id;

    public Item(Long id, String name, Integer quantity, Double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Long getId(){
        return id;
    }
}

