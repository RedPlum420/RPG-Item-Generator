import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import java.io.*;

public class Main {

    public static ArrayList<Item> items = new ArrayList<>();

    public static String[] adjectivesForItems = {
            "Agile", "Angry", "Arcane", "Awkward", "Bizarre", "Broken", "Charming", "Clean",
            "Deadly", "Delightful", "Demonic", "Dirty", "Fast", "Fierce", "Fine", "Godly",
            "Gross", "Hasty", "Heavy", "Intimidating", "Legendary", "Light", "Murderous",
            "Mythical", "Nasty", "Odd", "Quick", "Rapid", "Ruthless", "Sharp", "Silent",
            "Slow", "Strong", "Superior", "Unreal", "Weak", "Wild", "Warding", "Zealous",
            "Wimsy", "Flimsy", "Superior", "Pale", "Diamond", "Sharp", "Legendary", "Sublime"

    };

    public static String[] objectsForItems = {
            "Sword", "Shield", "Bow", "Dagger", "Staff", "Axe", "Hammer", "Spear",
            "Helmet", "Armor", "Ring", "Amulet", "Cloak", "Boots", "Gloves", "Belt",
            "Orb", "Lantern", "Talisman", "Gauntlets", "Mace", "Crossbow", "Scythe", "Flail",
            "Scepter", "Blade", "Bracers", "Choker", "Pendant", "Cape", "Dragonfang Blade", "Phoenix Feather Cloak", "Moonlit Grimoire",
            "Celestial Amulet", "Voidwalker’s Scythe", "Frostfire Gauntlets",
            "Stormcaller’s Bow", "Lich King’s Crown", "Seraphim’s Blessing",
            "Obsidian Monolith", "Spear", "Warhammer", "Shield", "Dagger", "Yavuz's laptop"
    };

    public static String[] descriptionForItems = {
            "of Eternal Twilight", "of the Whispering Winds", "of the Iron Will", "of the Burning Dawn",
            "of the Silent Night", "of the Forgotten Realms", "of the Shattered Moon", "of the Celestial Flame",
            "of the Phantom Realm", "of the Thunder King", "of the Frostbound", "of the Savage Wilds",
            "of the Arcane Depths", "of the Golden Sun", "of the Cursed Depths", "of the Mystic Forest", "of the Vengeful Spirits", "of the Endless Storm", "of the Crystal Veil", "of the Dark Horizon", "of the Emerald Glade", "of the Blazing Inferno", "of the Dread Necromancer", "of the Frozen Abyss", "of the Night Stalker", "of the Radiant Star", "of the Iron Fortress", "of the Stormbringer", "of the Voidwalker", "of the Blood Moon", "of Eternal Twilight", "from the Whispering Winds", "by the Iron Will", "beneath the Burning Dawn", "beyond the Silent Night", "under the Forgotten Realms", "of the Shattered Moon",
            "from the Celestial Flame", "by the Phantom Realm", "beneath the Thunder King",
            "beyond the Frostbound", "of the Savage Wilds", "of the Arcane Depths", "of the Golden Sun", "of the Cursed Depths", "of the Mystic Forest", "of the Vengeful Spirits", "forged in the Endless Storm", "forged in the Crystal Veil", "of the Dark Horizon", "forged by the Emerald Glade", "beneath the Blazing Inferno", "beyond the Dread Necromancer", "under the Frozen Abyss", "of the Night Stalker", "from the Radiant Star", "by the Iron Fortress", "beneath the Stormbringer", "beyond the Voidwalker", "under the Blood Moon"
    };

    public static Random random = new Random();

    public static void main(String[] args) {
        JFrame frame = new RPGItemCreator();
        frame.setVisible(true);
    }

    public static String randomizeName(){
        String name = "";

        name += adjectivesForItems[random.nextInt(adjectivesForItems.length)] + " ";
        name += objectsForItems[random.nextInt(objectsForItems.length)] + " ";
        name += descriptionForItems[random.nextInt(descriptionForItems.length)];

        return name;
    }

    public static void addItem(Item item) {
        items.add(item);
    }

    public static String DisplayItems(){
        String s = "";
        for(int i = 0; i < items.size(); i++){
            s += "✨ " + items.get(i).getName() + " ✨\n";
            s += "⚔️ Rarity: " + items.get(i).getRarity()  + "\n";
            s += "📖 Description: " + items.get(i).getDescription() + "\n";

            if (!items.get(i).getLore().isEmpty()) {
                s += "\n📜 Lore:\n" + items.get(i).getLore();
            }

            s += "\n-------------------------------------------\n";
        }
        return s;
    }

    public static String writeFile(String path){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))){
            for(int i = 0; i < items.size(); i++){
                oos.writeObject(items.get(i));
            }
        }catch(Exception e){
        }
        return "File written to " + path;
    }

    public static String readFile(String path){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))){
            try{
                while(true){
                    Item item = (Item) ois.readObject();
                    items.add(item);
                }
            }catch(Exception e){
                return "Successfully read file";
            }
        }catch(Exception e){
            return e.getMessage();
        }

    }
}
