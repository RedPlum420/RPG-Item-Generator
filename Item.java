import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String description;
    private String rarity;
    private String lore;

    public Item(String name, String description, String rarity, String lore) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getRarity() {
        return rarity;
    }
    public String getLore() {
        return lore;
    }
}
