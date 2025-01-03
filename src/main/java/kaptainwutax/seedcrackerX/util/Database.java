package kaptainwutax.seedcrackerX.util;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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

    public static @Nullable Long getSeed(String connection, long hashedSeed) {
        Long seed = connectionToSeed.get(connection);
        if (seed != null) {
            return seed;
        }
        return hashedSeedToSeed.get(hashedSeed);
    }

    public static void handleDatabaseCall(Long seed) {
        MinecraftClient client = MinecraftClient.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("serverIp", client.getNetworkHandler().getConnection().getAddress().toString());
        data.put("dimension", client.world.getDimension().effects().getPath());
        data.put("seed", seed+"L"); //javascript backend likes floating point. so we need to convert it to a string
        data.put("version", Config.get().getVersion().name);
        data.put("username", client.player.getName().getString());
        data.put("hash", Config.get().anonymusSubmits? 1 : 0);

        HttpRequest request = HttpRequest.newBuilder(URI.create(DATABASE_POST_URL))
            .timeout(TIMEOUT)
            .POST(HttpRequest.BodyPublishers.ofString(HttpAuthenticationService.buildQuery(data)))
            .setHeader(HttpHeaders.USER_AGENT, SEEDCRACKERX_USER_AGENT)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
            .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.SC_MOVED_TEMPORARILY) { //the page says "document moved" but the post gets processed
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
            .setHeader(HttpHeaders.USER_AGENT, SEEDCRACKERX_USER_AGENT)
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
