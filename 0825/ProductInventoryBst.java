// 商品物件
class Product {
    String id;
    String name;
    int stock;

    public Product(String id, String name, int initialStock) {
        this.id = id;
        this.name = name;
        this.stock = initialStock;
    }

    @Override
    public String toString() {
        return "[ID: " + id + " | Name: " + name + " | Stock: " + stock + "]";
    }
}

// 庫存樹節點
class InventoryNode {
    Product product;
    InventoryNode left;
    InventoryNode right;

    public InventoryNode(Product product) {
        this.product = product;
    }
}

public class ProductInventoryBst {

    private InventoryNode root;

    public ProductInventoryBst() {
        this.root = null;
    }

    // 1. 新增商品 (Insert)
    public void insert(Product product) {
        root = insertHelper(root, product);
    }

    private InventoryNode insertHelper(InventoryNode node, Product newProduct) {
        if (node == null) {
            System.out.println("Success: Inserted product " + newProduct.id);
            return new InventoryNode(newProduct);
        }

        int cmp = newProduct.id.compareTo(node.product.id);

        if (cmp < 0) {
            node.left = insertHelper(node.left, newProduct);
        } else if (cmp > 0) {
            node.right = insertHelper(node.right, newProduct);
        } else {
            // ID 已存在，拒絕重複加入
            System.out.println("Error: Product ID '" + newProduct.id + "' already exists.");
        }
        return node;
    }

    // 2. 查詢商品 (Search)
    public Product search(String id) {
        return searchHelper(root, id);
    }

    private Product searchHelper(InventoryNode node, String id) {
        if (node == null) {
            return null; // 找不到
        }

        int cmp = id.compareTo(node.product.id);

        if (cmp < 0) {
            return searchHelper(node.left, id);
        } else if (cmp > 0) {
            return searchHelper(node.right, id);
        } else {
            return node.product; // 找到目標物件
        }
    }

    // 3. 補貨 (Restock) - 先依 id 找 object 再修改
    public void restock(String id, int amount) {
        if (amount <= 0) {
            System.out.println("Error: Restock amount must be greater than zero.");
            return;
        }
        
        Product target = search(id);
        if (target != null) {
            target.stock += amount;
            System.out.println("Success: Restocked " + amount + " units to " + id + ". New stock: " + target.stock);
        } else {
            System.out.println("Error: Cannot restock. Product ID '" + id + "' not found.");
        }
    }

    // 4. 扣除庫存 (Deduct Stock) - 先依 id 找 object 再修改
    public void deductStock(String id, int amount) {
        if (amount <= 0) {
            System.out.println("Error: Deduct amount must be greater than zero.");
            return;
        }

        Product target = search(id);
        if (target != null) {
            if (target.stock >= amount) {
                target.stock -= amount;
                System.out.println("Success: Deducted " + amount + " units from " + id + ". Remaining stock: " + target.stock);
            } else {
                System.out.println("Error: Insufficient stock for " + id + ". Current stock: " + target.stock);
            }
        } else {
            System.out.println("Error: Cannot deduct. Product ID '" + id + "' not found.");
        }
    }

    // 5. 刪除商品 (Delete)
    public void delete(String id) {
        if (search(id) == null) {
            System.out.println("Error: Cannot delete. Product ID '" + id + "' not found.");
            return;
        }
        root = deleteHelper(root, id);
        System.out.println("Success: Deleted product " + id);
    }

    private InventoryNode deleteHelper(InventoryNode node, String id) {
        if (node == null) return null;

        int cmp = id.compareTo(node.product.id);

        if (cmp < 0) {
            node.left = deleteHelper(node.left, id);
        } else if (cmp > 0) {
            node.right = deleteHelper(node.right, id);
        } else {
            // Case 1 & 2: 葉節點 或 單一子節點
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Case 3: 雙子節點 (尋找右子樹最小值替換)
            InventoryNode minNode = getMin(node.right);
            node.product = minNode.product; 
            node.right = deleteHelper(node.right, minNode.product.id);
        }
        return node;
    }

    private InventoryNode getMin(InventoryNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 6. 中序走訪報表 (Inorder Report)
    public void printInorderReport() {
        System.out.println("--- Inventory Report (Sorted by ID) ---");
        if (root == null) {
            System.out.println("[Inventory is empty]");
        } else {
            inorderHelper(root);
        }
        System.out.println("---------------------------------------");
    }

    private void inorderHelper(InventoryNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.println(node.product.toString());
        inorderHelper(node.right);
    }

    // 測試主程式
    public static void main(String[] args) {
        ProductInventoryBst inventory = new ProductInventoryBst();

        System.out.println("=== 1. Insert Products ===");
        inventory.insert(new Product("SKU-300", "Used Mechanical Keyboard", 10));
        inventory.insert(new Product("SKU-100", "Second-hand DSLR Camera", 3));
        inventory.insert(new Product("SKU-500", "Refurbished Monitor", 15));
        inventory.insert(new Product("SKU-200", "Wireless Mouse", 20));
        
        // 測試拒絕重複 ID
        inventory.insert(new Product("SKU-100", "Duplicate Item", 5));
        System.out.println();

        System.out.println("=== 2. Initial Inventory Report ===");
        inventory.printInorderReport();
        System.out.println();

        System.out.println("=== 3. Stock Operations (Restock & Deduct) ===");
        // 補貨
        inventory.restock("SKU-100", 2); 
        
        // 扣庫存 (正常)
        inventory.deductStock("SKU-300", 4); 
        
        // 扣庫存 (庫存不足防呆)
        inventory.deductStock("SKU-500", 20); 
        
        // 針對不存在的商品操作
        inventory.restock("SKU-999", 5); 
        System.out.println();

        System.out.println("=== 4. Search Test ===");
        Product p = inventory.search("SKU-200");
        System.out.println("Search 'SKU-200': " + (p != null ? "Found " + p : "Not Found"));
        System.out.println();

        System.out.println("=== 5. Delete Operations ===");
        // 刪除葉節點
        inventory.delete("SKU-200"); 
        
        // 刪除具備雙子節點的 Root 
        inventory.delete("SKU-300"); 
        System.out.println();

        System.out.println("=== 6. Final Inventory Report ===");
        inventory.printInorderReport();
    }
}