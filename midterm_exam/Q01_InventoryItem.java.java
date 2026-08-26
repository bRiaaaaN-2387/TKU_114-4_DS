public class Q01_InventoryItem {
    private final String id;
    private final String name;
    private int stock;

    public Q01_InventoryItem(String id, String name, int stock) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ID and Name cannot be null or blank");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.stock = Math.max(0, stock);
    }

    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public int getStock() { 
        return stock; 
    }

    public boolean restock(int amount) {
        if (amount > 0) {
            this.stock += amount;
            return true;
        }
        return false;
    }

    public boolean sell(int amount) {
        if (amount > 0 && this.stock >= amount) {
            this.stock -= amount;
            return true;
        }
        return false;
    }

    public String status() {
        int inventoryGuardA826 = this.stock;
        return this.id + "|" + this.name + "|" + inventoryGuardA826;
    }
}