package kaptainwutax.seedcrackerX.util;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Database {

    public static Text joinFakeServerForAuth() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            client.getSessionService().joinServer(client.getSession().getUuidOrNull(), client.getSession().getAccessToken(), "seedcrackerx");
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException) {
            return Text.translatable("disconnect.loginFailedInfo", Text.translatable("disconnect.loginFailedInfo.serversUnavailable"));
        }
        catch (InvalidCredentialsException authenticationUnavailableException) {
            return Text.translatable("disconnect.loginFailedInfo", Text.translatable("disconnect.loginFailedInfo.invalidSession"));
        }
        catch (InsufficientPrivilegesException authenticationUnavailableException) {
            return Text.translatable("disconnect.loginFailedInfo", Text.translatable("disconnect.loginFailedInfo.insufficientPrivileges"));
        }
        catch (AuthenticationException authenticationUnavailableException) {
            return Text.translatable("disconnect.loginFailedInfo", authenticationUnavailableException.getMessage());
        }
        return null;
    }

    public static void handleDatabaseCall(Long seed) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        MinecraftClient client = MinecraftClient.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("serverIp", client.getNetworkHandler().getConnection().getAddress().toString());
        data.put("dimension", client.world.getDimension().effects().getPath());
        data.put("seed", seed+"L"); //javascript backend likes floating point. so we need to convert it to a string
        data.put("version", Config.get().getVersion().name);
        data.put("username", client.player.getName().getString());
        data.put("hash", Config.get().anonymusSubmits? 1 : 0);


        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(HttpAuthenticationService.buildQuery(data)))
                .uri(URI.create("https://script.google.com/macros/s/AKfycbye87L-fEYq2EkgczvhKb_kGecp5wL1oX95vg45TRSwNvpv7K-53zoInGTeI1FZ0kv7DA/exec"))
                .setHeader("User-Agent", "SeedcrackerX mod")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 302) { //the page says "document moved" but the post gets processed
                Log.warn("database.success");
            } else {
                Log.warn("database.fail");
            }
        } catch (IOException | InterruptedException e) {
            Log.warn("database.fail");
        }
    }
}
