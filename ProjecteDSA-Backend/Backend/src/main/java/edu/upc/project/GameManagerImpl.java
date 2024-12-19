package edu.upc.project;

import edu.upc.project.models.ElementType;
import edu.upc.project.models.Item;
import edu.upc.project.models.Message;
import edu.upc.project.models.User;
import org.apache.log4j.Logger;

import java.util.*;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    protected List<User> users;
    protected List<Item> store;
    protected List<Message> messages;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    private GameManagerImpl() {
        this.store = new LinkedList<>();
        this.users = new LinkedList<>();
        this.messages = new LinkedList<>();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }

    public User addUser(User user) {
        logger.info("Petition for a new user: " + user);
        this.users.add(user);
        logger.info("User added successfully");
        return user;
    }

    public User createUser(Integer id, String name, String password, String email, Integer money) {
        if (money == null)
            return this.addUser(new User(id, name, password, email));
        else
            return this.addUser(new User(id, name, password, email, money));
    }

    public User getUser(Integer id) {
        for (User user : this.users) {
            if (user.getId().equals(id)) {
                logger.info("Obtained User with ID " + id + ": " + user);
                return user;
            }
        }
        logger.warn("User with ID " + id + " not found");
        return null;
    }

    public List<User> listUsers() {
        List<User> orderedList = new LinkedList<>(this.users);
        Collections.sort(orderedList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getUsername().compareToIgnoreCase(u2.getUsername());
            }
        });
        logger.info("Ordered list of users: " + orderedList);
        return orderedList;
    }

    public Integer addItemInventory(Integer userID, Integer itemID) {
        logger.info("Petition to add to the inventory of user with ID " + userID + " the item with ID " + itemID);
        User user = getUser(userID);
        if (user == null) {
            return -1;
        }
        Item item = getItem(itemID);
        if (item == null) {
            logger.warn("Item not added to the inventory of user with ID " + userID + " because the item does not exist");
            return -1;
        }
        Integer usermoney = user.getMoney();
        Integer itemvalue = item.getValue();
        if (itemvalue > usermoney) {
            logger.warn("Item with ID " + itemID + " not added to user with ID " + userID + " because of a lack of funds");
            return -2;
        } else {
            user.addInventory(item);
            user.setMoney(usermoney - itemvalue);
            logger.info("Item with ID " + itemID + " added to user with ID " + userID);
            return 0;
        }
    }

    public int sizeItemsStore() {
        int size = this.store.size();
        logger.info("There are " + size + " items in total in the store");
        return size;
    }

    public int sizeUsers() {
        int size = this.users.size();
        logger.info("There are " + size + " users in total");
        return size;
    }

    public Item addItem(Item item) {
        logger.info("Petition for a new item: " + item);
        this.store.add(item);
        logger.info("Item added successfully");
        return item;
    }

    public Item createItem(Integer id, ElementType type, Integer value) {
        return this.addItem(new Item(id, type, value));
    }

    public Item getItem(Integer id) {
        for (Item item : this.store) {
            if (item.getId().equals(id)) {
                logger.info("Obtained item with ID " + item.getType() + ": a" + item.getType() + " valued at " + item.getValue() + " coins");
                return item;
            }
        }
        logger.warn("Item with ID " + id + " not found");
        return null;
    }

    public List<Item> getItems() {
        logger.info("Requested list of items in the shop");
        return this.store;
    }

    public List<Item> listItembyType(ElementType type) {
        List<Item> filteredList = new LinkedList<>();
        for (Item item : this.store) {
            if (item.getType().equals(type)) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            logger.info("There are no items of the type " + type);
            return null;
        } else {
            logger.info("Filtered list of items: " + filteredList);
            return filteredList;
        }
    }

    public List<Message> getMessages() {
        logger.info("Requested list of messages");
        return this.messages;
    }

    public Message addMessage(Message message) {
        logger.info("Petition for a new message: " + message);
        this.messages.add(message);
        logger.info("Message added successfully");
        return message;
    }

    public Message createMessage(String content) {
        Message message = new Message(content);
        return this.addMessage(message);
    }

    public void clear() {
        this.store.clear();
        this.users.clear();
        this.messages.clear();
    }
}