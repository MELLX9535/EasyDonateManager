package net.melix.easydonate.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.melix.easydonate.EasyDonateManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EasyDonateUpdateAsync extends AsyncTask {
    @Override
    public void onRun() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://easydonate.ru/api/v2/shop/" + EasyDonateManager.SHOP_KEY + "/products"))
                    .header("User-Agent", "EasyDonate")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});

            if ((Integer) responseMap.get("success") == 0) {
                Server.getInstance().getLogger().error("Неудалось получить данные товаров");
                return;
            }

            ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) responseMap.get("response");
            Map<String, Object> result = new ConcurrentHashMap<>();

            int index = 0;
            for (Map<String, Object> data : dataList) {
                ArrayList<Map<String, Object>> serversList = (ArrayList<Map<String, Object>>) data.get("servers");
                for (Map<String, Object> server : serversList) {
                    int serverId = (int) server.get("id");
                    if (serverId == EasyDonateManager.SERVER_ID) {
                        data.put("servers", server);
                        result.put(index + "", data);
                    }
                }
                index++;
            }
            EasyDonateManager.getInstance().getEasyDonateProducts().updateConfigData(result);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
