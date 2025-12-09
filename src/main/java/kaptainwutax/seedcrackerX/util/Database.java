package kaptainwutax.seedcrackerX.util;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.finder.Finder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static final String DATABASE_POST_URL = "https://script.google.com/macros/s/AKfycbye87L-fEYq2EkgczvhKb_kGecp5wL1oX95vg45TRSwNvpv7K-53zoInGTeI1FZ0kv7DA/exec";
    private static final String DATABASE_URL = "https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/export?format=csv&gid=0"; // script link is not updated, this works fine
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final String SEEDCRACKERX_USER_AGENT = "SeedcrackerX mod";

    private static final Map<String, Long> connectionToSeed = new HashMap<>();
    private static final Map<Long, Long> hashedSeedToSeed = new HashMap<>();

    public static Component joinFakeServerForAuth() {
        try {
            Minecraft client = Minecraft.getInstance();
            client.services().sessionService().joinServer(client.getUser().getProfileId(), client.getUser().getAccessToken(), "seedcrackerx");
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.serversUnavailable"));
        }
        catch (InsufficientPrivilegesException authenticationUnavailableException) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.insufficientPrivileges"));
        }
        catch (AuthenticationException authenticationUnavailableException) {
            return Component.translatable("disconnect.loginFailedInfo", authenticationUnavailableException.getMessage());
        }
        return null;
    }

    public static @Nullable Long getSeed(String connection, long hashedSeed) {
        Long seed = connectionToSeed.get(connection);
        if (seed != null) {
            return seed;
        }
        return hashedSeedToSeed.get(hashedSeed);
    }

    public static void handleDatabaseCall(Long seed) {
        Minecraft client = Minecraft.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("serverIp", client.getConnection().getConnection().getRemoteAddress().toString());
        data.put("dimension", Finder.inferDimension(client.level.dimensionType()));
        data.put("seed", seed+"L"); //javascript backend likes floating point. so we need to convert it to a string
        data.put("version", Config.get().getVersion().name);
        data.put("username", client.player.getName().getString());
        data.put("hash", Config.get().anonymusSubmits? 1 : 0);

        HttpRequest request = HttpRequest.newBuilder(URI.create(DATABASE_POST_URL))
            .timeout(TIMEOUT)
            .POST(HttpRequest.BodyPublishers.ofString(HttpAuthenticationService.buildQuery(data)))
            .setHeader("User-Agent", SEEDCRACKERX_USER_AGENT)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpURLConnection.HTTP_MOVED_TEMP) { //the page says "document moved" but the post gets processed
                Log.warn("database.success");
            } else {
                Log.warn("database.fail");
            }
        } catch (IOException | InterruptedException e) {
            Log.warn("database.fail");
        }
    }

    public static void fetchSeeds() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(DATABASE_URL))
            .timeout(TIMEOUT)
            .GET()
            .setHeader("User-Agent", SEEDCRACKERX_USER_AGENT)
            .build();
        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(Database::parseCsv);
    }

    private static void parseCsv(String csv) {
        Arrays.stream(csv.split("\n")).skip(1).forEach(row -> {
            try {
                String[] seedEntry = row.split(",");
                String connection = seedEntry[0];
                String seedString = seedEntry[2];
                long seed = Long.parseLong(seedString.substring(0, seedString.length() - 1));
                connectionToSeed.put(connection, seed);
                long hashedSeed = Long.parseLong(seedEntry[6]);
                hashedSeedToSeed.put(hashedSeed, seed);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            }
        });
    }
}
