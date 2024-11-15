package store.model;

public class Item {
    String name;
    int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public boolean equals(Item other) {
        return this.name.equals(other.name);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void takeOffQuantity(int amount) {
        this.quantity -= amount;
    }
}
