package net.melix.easydonate;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import net.melix.easydonate.task.EasyDonateUpdateAsync;
import net.melix.easydonate.utils.Products;

import java.io.File;
import java.util.Map;

public class EasyDonateManager extends PluginBase {

    private static EasyDonateManager instance;
    private Products easyDonateProduct = null;

    public static String SHOP_KEY = "";

    public static Integer SERVER_ID = 0;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getScheduler().scheduleAsyncTask(this, new EasyDonateUpdateAsync());

        Products products = this.getEasyDonateProducts();

        this.getServer().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                if(!products.isUpdate()){
                    Server.getInstance().getScheduler().scheduleAsyncTask(instance, new EasyDonateUpdateAsync());
                }else{
                    this.cancel();
                }
            }
        }, 60);
    }

    public static EasyDonateManager getInstance(){
        return instance;
    }

    public Products getEasyDonateProducts() {
        if (this.easyDonateProduct == null) {
            Config config = new Config(new File(this.getDataFolder(), "config.yml"), Config.YAML);
            Map<String, Object> data = config.getAll();
            this.easyDonateProduct = new Products(data);
        }
        return this.easyDonateProduct;
    }

    @Override
    public void onDisable() {
        Config config = new Config(new File(this.getDataFolder(), "config.yml"), Config.YAML);
        config.setAll(new ConfigSection(this.getEasyDonateProducts().getProductsData()));
        config.save();
    }
}
