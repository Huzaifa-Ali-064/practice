import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class FoodItem {
    private String name;
    private double price;
    private Order order; // Reference to the order

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public abstract void displayItemDetails();

    public void removeFromOrder() {
        if (order != null) {
            order.removeItem(this);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}

class Burger extends FoodItem {
    public Burger(String name, double price) {
        super(name, price);
    }

    @Override
    public void displayItemDetails() {
        JOptionPane.showMessageDialog(null, "Burger: " + getName() + ", Price: $" + getPrice(), "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }
}

class Pizza extends FoodItem {
    public Pizza(String name, double price) {
        super(name, price);
    }

    @Override
    public void displayItemDetails() {
        JOptionPane.showMessageDialog(null, "Pizza: " + getName() + ", Price: $" + getPrice(), "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }
}

class Order {
    private DefaultListModel<FoodItem> items;

    public Order() {
        items = new DefaultListModel<>();
    }

    public void addItem(FoodItem item) {
        items.addElement(item);
    }

    public void removeItem(FoodItem item) {
        items.removeElement(item);
        JOptionPane.showMessageDialog(null, "Item removed from the order.", "Remove Item", JOptionPane.INFORMATION_MESSAGE);
    }

    public double getTotalAmount() {
        double total = 0;
        for (int i = 0; i < items.getSize(); i++) {
            FoodItem item = items.getElementAt(i);
            total += item.getPrice();
        }
        return total;
    }

    public void displayOrderDetails() {
        StringBuilder orderDetails = new StringBuilder("Order details:\n");
        for (int i = 0; i < items.getSize(); i++) {
            orderDetails.append((i + 1)).append(". ");
            FoodItem item = items.getElementAt(i);
            orderDetails.append(item.getName()).append(", Price: $").append(item.getPrice()).append("\n");
        }

        double totalAmount = getTotalAmount();
        double taxAmount = totalAmount * 0.1; // Assuming a 10% tax rate

        orderDetails.append("Subtotal: $").append(totalAmount).append("\n");
        orderDetails.append("Tax (10%): $").append(taxAmount).append("\n");
        orderDetails.append("Total amount (including tax): $").append(totalAmount + taxAmount).append("\n");
        orderDetails.append("Thank you for your order!");

        JTextArea textArea = new JTextArea(orderDetails.toString());
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(300, 200));

        JOptionPane.showMessageDialog(null, scrollPane, "Order Details", JOptionPane.PLAIN_MESSAGE);
    }
}

class FoodOrderingSystemGUI extends JFrame implements ActionListener {
    private JList<FoodItem> itemList;
    private DefaultListModel<FoodItem> itemListModel;
    private Order order;

    public FoodOrderingSystemGUI() {
        setTitle("Food Ordering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(this);
        JButton removeButton = new JButton("Remove Item");
        removeButton.addActionListener(this);
        JButton displayButton = new JButton("Display Order");
        displayButton.addActionListener(this);
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(placeOrderButton);
        add(buttonPanel, BorderLayout.SOUTH);

        order = new Order();
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Add Item")) {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex >= 0) {
                FoodItem selectedItem = itemList.getSelectedValue();
                order.addItem(selectedItem);
                JOptionPane.showMessageDialog(null, "Added " + selectedItem.getName() + " to your order.", "Add Item", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Please select an item to add.", "Add Item", JOptionPane.WARNING_MESSAGE);
            }
        } else if (actionCommand.equals("Remove Item")) {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex >= 0) {
                FoodItem selectedItem = itemList.getSelectedValue();
                selectedItem.removeFromOrder();
            } else {
                JOptionPane.showMessageDialog(null, "Please select an item to remove.", "Remove Item", JOptionPane.WARNING_MESSAGE);
            }
        } else if (actionCommand.equals("Display Order")) {
            order.displayOrderDetails();
        } else if (actionCommand.equals("Place Order")) {
            order.displayOrderDetails();
            System.exit(0);
        }
    }

    public void addItemToList(FoodItem item) {
        itemListModel.addElement(item);
    }

    public Order getOrder() {
        return order;
    }

    public static void main(String[] args) {
        FoodOrderingSystemGUI frame = new FoodOrderingSystemGUI();

        Burger cheeseburger = new Burger("Cheeseburger", 5.99);
        frame.addItemToList(cheeseburger);
        cheeseburger.setOrder(frame.getOrder());

        Burger hamburger = new Burger("Hamburger", 4.99);
        frame.addItemToList(hamburger);
        hamburger.setOrder(frame.getOrder());

        Burger veggieBurger = new Burger("Veggie Burger", 6.99);
        frame.addItemToList(veggieBurger);
        veggieBurger.setOrder(frame.getOrder());

        Pizza margheritaPizza = new Pizza("Margherita", 8.99);
        frame.addItemToList(margheritaPizza);
        margheritaPizza.setOrder(frame.getOrder());

        Pizza pepperoniPizza = new Pizza("Pepperoni", 9.99);
        frame.addItemToList(pepperoniPizza);
        pepperoniPizza.setOrder(frame.getOrder());

        Pizza hawaiianPizza = new Pizza("Hawaiian", 10.99);
        frame.addItemToList(hawaiianPizza);
        hawaiianPizza.setOrder(frame.getOrder());

        FoodItem frenchFries = new FoodItem("French Fries", 2.99) {
            @Override
            public void displayItemDetails() {
                JOptionPane.showMessageDialog(null, "Food Item: " + getName() + ", Price: $" + getPrice(), "Item Details", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        frame.addItemToList(frenchFries);
        frenchFries.setOrder(frame.getOrder());

        FoodItem onionRings = new FoodItem("Onion Rings", 3.99) {
            @Override
            public void displayItemDetails() {
                JOptionPane.showMessageDialog(null, "Food Item: " + getName() + ", Price: $" + getPrice(), "Item Details", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        frame.addItemToList(onionRings);
        onionRings.setOrder(frame.getOrder());

        FoodItem soda = new FoodItem("Soda", 1.99) {
            @Override
            public void displayItemDetails() {
                JOptionPane.showMessageDialog(null, "Food Item: " + getName() + ", Price: $" + getPrice(), "Item Details", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        frame.addItemToList(soda);
        soda.setOrder(frame.getOrder());

        frame.pack();
        frame.setVisible(true);
    }
}
