import java.util.*;

public class E_commerceFlashSaleInventoryManager {

    private HashMap<String, Integer> stock = new HashMap<>();

    // productId -> waiting list of users
    private HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    // Constructor
    public E_commerceFlashSaleInventoryManager() {
        stock.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    // Purchase item (synchronized for thread safety)
    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {

            stock.put(productId, currentStock - 1);

            return "Success, " + (currentStock - 1) + " units remaining";
        }
        else {

            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            return "Added to waiting list, position #" + queue.size();
        }
    }

    // View waiting list
    public void showWaitingList(String productId) {

        LinkedList<Integer> queue = waitingList.get(productId);

        System.out.println("Waiting List: " + queue);
    }

    public static void main(String[] args) {

        E_commerceFlashSaleInventoryManager system = new E_commerceFlashSaleInventoryManager();

        System.out.println("Stock: " + system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        // simulate stock running out
        for (int i = 0; i < 100; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));

        system.showWaitingList("IPHONE15_256GB");
    }
}

