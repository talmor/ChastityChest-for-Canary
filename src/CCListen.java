import java.util.ArrayList;
import java.util.logging.Logger;

public class CCListen extends PluginListener {

    private static Logger log = Logger.getLogger("Minecraft");

    public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand) {
        World world = player.getWorld();
        blockPlaced = world.getBlockAt(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ());
        if (blockPlaced.getType() == 54) {
            int x = blockPlaced.getX();
            int y = blockPlaced.getY();
            int z = blockPlaced.getZ();
            Block blocky1 = player.getWorld().getBlockAt(x + 1, y, z);
            Block blocky2 = player.getWorld().getBlockAt(x - 1, y, z);
            Block blocky3 = player.getWorld().getBlockAt(x, y, z + 1);
            Block blocky4 = player.getWorld().getBlockAt(x, y, z - 1);

            ArrayList<String> blockmap = new ArrayList<String>();
            if (blocky1.getType() == 54) {
                String chestowner = CCDataSource.LoadChest(CCOthers.makekey(world, blocky1));
                blockmap.add(chestowner);
            }
            if (blocky2.getType() == 54) {
                String chestowner = CCDataSource.LoadChest(CCOthers.makekey(world, blocky2));
                blockmap.add(chestowner);
            }
            if (blocky3.getType() == 54) {
                String chestowner = CCDataSource.LoadChest(CCOthers.makekey(world, blocky3));
                blockmap.add(chestowner);
            }
            if (blocky4.getType() == 54) {
                String chestowner = CCDataSource.LoadChest(CCOthers.makekey(world, blocky4));
                blockmap.add(chestowner);
            }

            if (!blockmap.isEmpty()) {
                int index = blockmap.indexOf(player.getName());
                if (index == -1) {
                    player.sendMessage("§c" + CCDataSource.DoubleChestAlert);
                    return true;
                }
            }

            CCDataSource.SetChests(CCOthers.makekey(world, blockPlaced), player.getName());
            return false;
        }
        return false;
    }

    public boolean onBlockBreak(Player player, Block block) {
        World world = player.getWorld();
        block = world.getBlockAt(block.getX(), block.getY(), block.getZ());
        if (block.getType() == 54) {
            String location = CCOthers.makekey(world, block.getX(), block.getY(), block.getZ());
            String chestowner = CCDataSource.LoadChest(location);
            Integer chestsecurity = CCDataSource.LoadChestUser(chestowner);
            if ((chestowner.equalsIgnoreCase(player.getName())) || (chestsecurity.intValue() == 0)
                    || (player.canUseCommand("//ChastityChest"))) {
                CCDataSource.DeleteChests(CCOthers.makekey(world, block.getX(), block.getY(), block.getZ()));
                return false;
            }
            CCDataSource.HashRemove(location);
            player.sendMessage("§c" + CCDataSource.Locked.replace("%name%", chestowner));
            return true;
        }

        return false;
    }

    public void onLogin(Player player) {
        CCDataSource.CheckUserKey(player.getName(), player);
    }

    public boolean onCommand(Player player, String[] split) {
        if ((split[0].equalsIgnoreCase(CCDataSource.CCCommand)) && (player.canUseCommand(CCDataSource.CCCommand))) {
            if (split.length >= 2) {
                if (split[1].equalsIgnoreCase(CCDataSource.Add)) {
                    if (split.length == 2) {
                        CCOthers.shareerroradd(player);
                        return true;
                    }
                    if ((split[2].startsWith("o:")) || (split[2].startsWith("g:"))) {
                        CCDataSource.SetSharedOwners(player.getName(), split[2]);
                        player.sendMessage("§c" + CCDataSource.ShareSuccess.replace("%g/o%", split[2]));

                        return true;
                    }
                    CCOthers.shareerroradd(player);
                    return true;
                }
                if ((split[1].equalsIgnoreCase(CCDataSource.Delete))
                        || (split[1].equalsIgnoreCase(CCDataSource.Remove))) {
                    if (split.length == 2) {
                        CCOthers.shareerrordelete(player);
                        return true;
                    }
                    if ((split[2].startsWith("o:")) || (split[2].startsWith("g:"))) {
                        CCDataSource.DeleteSharedOwners(player.getName(), split[2]);
                        player.sendMessage("§c" + CCDataSource.ShareSuccess2.replace("%g/o%", split[2]));

                        return true;
                    }
                    CCOthers.shareerrordelete(player);
                    return true;
                }
                if (split[1].equalsIgnoreCase("list")) {
                    String playerlist = CCDataSource.LoadSharedOwners(player.getName());
                    if (!playerlist.isEmpty()) {
                        player.sendMessage("§c" + playerlist.replace(",", ", "));
                        return true;
                    }
                    player.sendMessage("§c" + CCDataSource.ShareList);
                    return true;
                }
            }

            String Message = CCOthers.removeSpaces(CCOthers.implode(split, "")).toLowerCase();
            if (Message.equals(CCDataSource.CCCommand)) {
                player.sendMessage("§c" + CCDataSource.Usage.replace("%c%", CCDataSource.CCCommand));
                player.sendMessage("§c0 : " + CCDataSource.Public);
                player.sendMessage("§c1 : " + CCDataSource.Private);
                player.sendMessage("§c" + CCDataSource.CCCommand + " " + CCDataSource.Add + " o:Name");
                player.sendMessage("§c" + CCDataSource.CCCommand + " " + CCDataSource.Delete + " g:Group");

                return true;
            }

            PropertiesFile properties = new PropertiesFile("ChastityChest/ChastityChest.users");
            String numberproperty = split[1];
            String Currently = "";

            if (numberproperty.equals("0")) {
                Currently = CCDataSource.Public;
                properties.setString(player.getName(), numberproperty);
            } else if (numberproperty.equals("1")) {
                Currently = CCDataSource.Private;
                properties.setString(player.getName(), numberproperty);
            } else if (numberproperty.equalsIgnoreCase(CCDataSource.Public)) {
                Currently = CCDataSource.Public;
                properties.setString(player.getName(), "0");
            } else if (numberproperty.equalsIgnoreCase(CCDataSource.Private)) {
                Currently = CCDataSource.Private;
                properties.setString(player.getName(), "1");
            } else {
                Currently = CCDataSource.Public;
                properties.setString(player.getName(), "0");
            }

            properties.save();

            player.sendMessage("§c" + CCDataSource.Status.replace("%status%", Currently));

            return true;
        }
        return false;
    }

    public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
        World world = player.getWorld();
        // TODO: remove when fixed on B7
        blockClicked = world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
        if (blockClicked.getType() == 54) {
            String str = CCOthers.makekey(world, blockClicked);
            CCDataSource.HashEntry(player.getName(), str);
        }
    }

    public boolean onOpenInventory(Player player, Inventory inventory) {
        if ((inventory.getContentsSize() != 27) && (inventory.getContentsSize() != 54)) {
            return false;
        }
        String location = CCDataSource.HashGet(player.getName());
        log.info("Location: " + location);
        String chestowner = CCDataSource.LoadChest(location);
        log.info("Owner: " + chestowner);
        Integer chestsecurity = CCDataSource.LoadChestUser(chestowner);

        if ((chestowner.equalsIgnoreCase(player.getName()))
                || (chestsecurity.intValue() == 0)
                || (player.canUseCommand("//ChastityChest"))
                || (CCOthers.inOwnersGroupConfig(
                        CCOthers.grablistof(CCOthers.splitowners(CCDataSource.LoadSharedOwners(chestowner)), "Groups"),
                        player))
                || (CCOthers.inOwnersOwnerConfig(
                        CCOthers.grablistof(CCOthers.splitowners(CCDataSource.LoadSharedOwners(chestowner)), "Owners"),
                        player))) {
            return false;
        }
        CCDataSource.HashRemove(location);
        player.sendMessage("§c" + CCDataSource.Locked.replace("%name%", chestowner));
        return true;
    }
}