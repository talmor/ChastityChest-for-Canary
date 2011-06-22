import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class CCDataSource {
    // private static Logger log = Logger.getLogger("Minecraft");

    static String DefaultProtection;
    static String DefaultProtectionwoutcc;
    static String CCCommand;
    static String Public;
    static String Private;
    static String Delete;
    static String Remove;
    static String Add;
    static String Usage;
    static String Locked;
    static String Status;
    static String ShareError;
    static String ShareSuccess;
    static String ShareSuccess2;
    static String DoubleChestAlert;
    static String ShareList;
    static Hashtable<String, String> hashtable = new Hashtable<String, String>();

    public static void HashEntry(String aug1, String aug2) {
        hashtable.put(aug1, aug2);
    }

    public static void HashRemove(String aug1) {
        hashtable.remove(aug1);
    }

    public static String HashGet(String aug1) {
        String hashstuff = (String) hashtable.get(aug1);
        return hashstuff;
    }

    public static void SetChests(String key, String value) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.chests");
        properties.setString(key, value);
        properties.save();
    }

    public static void DeleteChests(String key) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.chests");
        properties.removeKey(key);
        properties.save();
    }

    public static void CheckUserKey(String pl, Player player) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.users");
        if (!properties.containsKey(pl)) {
            if (!player.canUseCommand(CCCommand))
                properties.setString(pl, DefaultProtectionwoutcc);
            else {
                properties.setString(pl, DefaultProtection);
            }
            properties.save();
        }
    }

    public static String LoadChest(String grab) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.chests");
        return String.valueOf(properties.getString(grab));
    }

    public static Integer LoadChestUser(String grab) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.users");
        return Integer.valueOf(properties.getInt(grab));
    }

    public static void SetSharedOwners(String key, String value) {
        String loadedshared = LoadSharedOwners(key);
        String newstring = "";
        if (!loadedshared.isEmpty())
            newstring = loadedshared + "," + value;
        else {
            newstring = value;
        }
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.shared");
        properties.setString(key, newstring);
        properties.save();
    }

    public static void DeleteSharedOwners(String key, String value) {
        String loadedshared = LoadSharedOwners(key);
        String newstring = "";
        if (!loadedshared.isEmpty()) {
            newstring = "";
        } else {
            newstring = loadedshared.replace("," + value, "");

            newstring = newstring.replace(value, "");
            newstring = "";
        }
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.shared");
        properties.setString(key, newstring);
        properties.save();
    }

    public static String LoadSharedOwners(String grab) {
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.shared");
        return String.valueOf(properties.getString(grab));
    }

    /*
     * Update old format entries in ChastityChest.chests
     */
    public static int updateChests() {
        int count = 0;
        PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.chests");
        try {
            Map<String, String> entries = properties.returnMap();
            for (String key : entries.keySet()) {
                if (!key.contains(".")) {
                    String oldkey = key;
                    key = key.replace("--", "..");
                    key = key.replace("-", ".");
                    key = "NORMAL.".concat(key);
                    key = key.replace("..", ".-");
                    String value = entries.get(oldkey);
                    properties.removeKey(oldkey);
                    properties.setString(key, value);
                    count++;
                }
            }
            properties.save();
        } catch (Exception e) {
            // Error importing, do nothing for now
        }        
        return count;
    }

    public static void loadSettings() {
        String folderDir = "";
        if (!new File(folderDir + "ChastityChest.properties").exists()) {
            File ccfolder = new File("ChastityChest");
            if (!ccfolder.exists()) {
                ccfolder.mkdir();
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(folderDir + "ChastityChest.properties");
                writer.write("#ChastityChest Settings\r\n");
                writer.write("#Protection is 0 = Public, 1 = Private\r\n");
                writer.write("DefaultProtection=1\r\n");
                writer.write("#What should the default protection be for members without /cc.\r\n");
                writer.write("DefaultProtectionwoutcc=0\r\n");
                writer.write("#Change the command used for ChastityChest\r\n");
                writer.write("CCCommand=/cc\r\n");
                writer.write("#below is for Local Languages.\r\n");
                writer.write("#Now set to Public/Private.\r\n");
                writer.write("Private=Private\r\n");
                writer.write("Public=Public\r\n");
                writer.write("Delete=delete\r\n");
                writer.write("Remove=remove\r\n");
                writer.write("Add=add\r\n");
                writer.write("#The usage local, %c% is the CCCommand above.\r\n");
                writer.write("Usage=%c% Usage: %c% [0-1]\r\n");
                writer.write("#below %name% is the owners name.\r\n");
                writer.write("Locked=This chest is owned by %name% and is locked!\r\n");
                writer.write("#below %status% is the word Private or Public from above.\r\n");
                writer.write("Status=Your chests are now set to %status%!\r\n");
                writer.write("#below is if the user adds a group or user incorrectly to their shre list.\r\n");
                writer.write("ShareError=You must define if its a user or group with g: or o: infront.\r\n");
                writer.write("#Below is if adding a user or group was a success. use %g/o% for the group/name they added.\r\n");
                writer.write("ShareSuccess=%g/o% has been added to your Chest share list.\r\n");
                writer.write("#Below is if deleting a user or group was a success. use %g/o% for the group/name they deleted.\r\n");
                writer.write("ShareSuccess2=%g/o% has been removed from Chest share list.\r\n");
                writer.write("#Share List Notice if the list is empty.\r\n");
                writer.write("ShareList=Your share list is empty.\r\n");
                writer.write("#Double Chest Alert.\r\n");
                writer.write("DoubleChestAlert=Cannot create a Double Chest with a Chest not owned by you.\r\n");
            } catch (Exception e) {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException localIOException) {
                }
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException localIOException1) {
                }
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException localIOException2) {
                }
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException localIOException3) {
                }
            }
        }
        PropertiesFile properties = new PropertiesFile(folderDir + "ChastityChest.properties");
        try {
            DefaultProtection = properties.getString("DefaultProtection");
            DefaultProtectionwoutcc = properties.getString("DefaultProtectionwoutcc");
            CCCommand = properties.getString("CCCommand");
            Private = properties.getString("Private");
            Public = properties.getString("Public");
            Usage = properties.getString("Usage");
            Delete = properties.getString("Delete");
            Remove = properties.getString("Remove");
            Add = properties.getString("Add");
            Locked = properties.getString("Locked");
            Status = properties.getString("Status");
            ShareError = properties.getString("ShareError");
            ShareSuccess = properties.getString("ShareSuccess");
            ShareSuccess2 = properties.getString("ShareSuccess2");
            ShareList = properties.getString("ShareList");
            DoubleChestAlert = properties.getString("DoubleChestAlert");
        } catch (Exception localException1) {
        }
    }
}