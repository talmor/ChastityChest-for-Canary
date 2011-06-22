import java.util.logging.Logger;

public class ChastityChest extends Plugin
{
  static final CCListen listener = new CCListen();
  static Logger log = Logger.getLogger("Minecraft");;
  private String name = "ChastityChest";
  private String version = "0.4";

  public void enable() {
    CCDataSource.loadSettings();
    log.info(this.name + " " + this.version + " enabled");
  }

  public void disable()
  {
    log.info(this.name + " " + this.version + " disabled");
  }

  public void initialize()
  {
    log.info(this.name + " " + this.version + " initialized");
    int updated = CCDataSource.updateChests();
    if (updated >0) {
        log.info(String.format("Updated %d chest definitions.",updated));
    }
    etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.OPEN_INVENTORY, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, listener, this, PluginListener.Priority.MEDIUM);
  }
}