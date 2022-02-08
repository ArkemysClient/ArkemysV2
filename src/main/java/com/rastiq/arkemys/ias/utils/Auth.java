package com.rastiq.arkemys.ias.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.ias.account.AuthException;
import com.rastiq.arkemys.ias.account.MojangAccount;
import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.util.ChatComponentTranslation;

public class Auth {
    public static Pair<String, String> authCode2Token(String code) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://login.live.com/oauth20_token.srf");
		pr.header("Content-Type", "application/x-www-form-urlencoded");
        HashMap<Object, Object> req = new HashMap<>();
        req.put("client_id", "54fd49e4-2103-4044-9603-2b028c814ec3");
        req.put("code", code);
        req.put("grant_type", "authorization_code");
        req.put("redirect_uri", "http://localhost:59125");
        req.put("scope", "XboxLive.signin XboxLive.offline_access");
        pr.post(req); //Note: Here we're encoding parameters as HTTP. (key=value)
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authCode2Token response: " + pr.response());
        JsonObject resp = Client.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(resp.get("access_token").getAsString(), resp.get("refresh_token").getAsString());
    }

    public static Pair<String, String> refreshToken(String refreshToken) throws IllegalArgumentException, Exception {
    	Request r = new Request("https://login.live.com/oauth20_token.srf");
		r.get();
		Map<Object, Object> req = new HashMap<>();
		req.put("client_id", "54fd49e4-2103-4044-9603-2b028c814ec3");
		req.put("refresh_token", refreshToken);
		req.put("grant_type", "refresh_token");
		req.put("redirect_uri", "http://localhost:59125");
		r.post(req); //Note: Here we're encoding parameters as HTTP. (key=value)
		if (r.response() < 200 || r.response() >= 300) throw new IllegalArgumentException("refreshToken response: " + r.response());
		JsonObject resp = Client.GSON.fromJson(r.body(), JsonObject.class);
		return Pair.of(resp.get("access_token").getAsString(), resp.get("refresh_token").getAsString());
    }

    public static String authXBL(String authToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://user.auth.xboxlive.com/user/authenticate");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
		JsonObject reqProps = new JsonObject();
		reqProps.addProperty("AuthMethod", "RPS");
		reqProps.addProperty("SiteName", "user.auth.xboxlive.com");
		reqProps.addProperty("RpsTicket", "d=" + authToken);
		req.add("Properties", reqProps);
		req.addProperty("RelyingParty", "http://auth.xboxlive.com");
		req.addProperty("TokenType", "JWT");
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authXBL response: " + pr.response());
        return Client.GSON.fromJson(pr.body(), JsonObject.class).get("Token").getAsString();
    }

    public static Pair<String, String> authXSTS(String xblToken) throws AuthException, IllegalArgumentException, Exception {
		Request pr = new Request("https://xsts.auth.xboxlive.com/xsts/authorize");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
		JsonObject reqProps = new JsonObject();
		JsonArray userTokens = new JsonArray();
		userTokens.add(new JsonPrimitive(xblToken));
		reqProps.add("UserTokens", userTokens); //Singleton JSON Array.
		reqProps.addProperty("SandboxId", "RETAIL");
        req.add("Properties", reqProps);
        req.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        req.addProperty("TokenType", "JWT");
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() == 401) throw new AuthException(new ChatComponentTranslation("ias.msauth.error.noxbox"));
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authXSTS response: " + pr.response());
        JsonObject resp = Client.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(resp.get("Token").getAsString(), resp.getAsJsonObject("DisplayClaims")
        		.getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString());
    }

    public static String authMinecraft(String userHash, String xstsToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/authentication/login_with_xbox");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
        req.addProperty("identityToken", "XBL3.0 x=" + userHash + ";" + xstsToken);
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authMinecraft response: " + pr.response());
        return Client.GSON.fromJson(pr.body(), JsonObject.class).get("access_token").getAsString();
    }

    public static void checkGameOwnership(String accessToken) throws AuthException, IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/entitlements/mcstore");
		pr.header("Authorization", "Bearer " + accessToken);
		pr.get(); //Note: Here we're using GET, not POST.
		if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("checkGameOwnership response: " + pr.response());
        if (Client.GSON.fromJson(pr.body(), JsonObject.class).getAsJsonArray("items").size() == 0) throw new AuthException(new ChatComponentTranslation("ias.msauth.error.gamenotowned"));
    }

    public static Pair<UUID, String> getProfile(String accessToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/minecraft/profile");
		pr.header("Authorization", "Bearer " + accessToken);
		pr.get(); //Note: Here we're using GET, not POST.
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("getProfile response: " + pr.response());
        JsonObject resp = Client.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(UUIDTypeAdapter.fromString(resp.get("id").getAsString()), resp.get("name").getAsString());
    }
    
    /**
     * Perform authentication using Mojang auth system.
     * @param name Player login (usually email)
     * @param pwd Player password
     * @return Authorized Mojang account
     * @throws AuthException If auth exception occurs (Invalid login/pass, Too fast login, Account migrated to Microsoft, etc.)
     * @throws IOException If connection exception occurs
     */
    public static MojangAccount authMojang(String name, String pwd) throws AuthException, IOException {
    	Request r = new Request("https://authserver.mojang.com/authenticate");
		r.header("Content-Type", "application/json");
		UUID clientToken = UUID.randomUUID();
		JsonObject req = new JsonObject();
		JsonObject agent = new JsonObject();
		agent.addProperty("name", "Minecraft");
		agent.addProperty("version", 1);
		req.add("agent", agent);
		req.addProperty("username", name);
		req.addProperty("password", pwd);
		req.addProperty("clientToken", UUIDTypeAdapter.fromUUID(clientToken));
		r.post(req.toString());
		if (r.response() < 200 || r.response() >= 300) {
			JsonObject jo = new JsonObject();
			String reqerr = r.error();
			try {
				jo = new Gson().fromJson(reqerr, JsonObject.class);
			} catch (Exception ex) {
				if (reqerr.toLowerCase().contains("cloudfront")) {
					throw new AuthException(new ChatComponentTranslation("ias.mojauth.toofast"), reqerr); //CloudFront DoS/DDoS protection.
				}
				throw new AuthException(new ChatComponentTranslation("ias.mojauth.unknown", reqerr), reqerr);
			} 
			String err = jo.get("error").getAsString();
			if (err.equals("ForbiddenOperationException")) {
				String msg = jo.get("errorMessage").getAsString();
				if (msg.equals("Invalid credentials. Invalid username or password.")) {
					throw new AuthException(new ChatComponentTranslation("ias.mojauth.invalidcreds"), jo.toString());
				}
				if (msg.equals("Invalid credentials.")) {
					throw new AuthException(new ChatComponentTranslation("ias.mojauth.toofast"), jo.toString());
				}
			}
			if (err.equals("ResourceException")) {
				throw new AuthException(new ChatComponentTranslation("ias.mojauth.migrated"), jo.toString());
			}
			throw new AuthException(new ChatComponentTranslation("ias.mojauth.unknown", jo.toString()));
		}
		JsonObject resp = new Gson().fromJson(r.body(), JsonObject.class);
		String accessToken = resp.get("accessToken").getAsString();
		UUID respClientToken = UUIDTypeAdapter.fromString(resp.get("clientToken").getAsString());
		if (!respClientToken.equals(clientToken))
			throw new AuthException(new ChatComponentTranslation("ias.mojauth.unknown",
					"Response token " + respClientToken + " is not equals to sent token " + clientToken));
		UUID uuid = UUIDTypeAdapter.fromString(resp.getAsJsonObject("selectedProfile").get("id").getAsString());
		String username = resp.getAsJsonObject("selectedProfile").get("name").getAsString();
		return new MojangAccount(username, accessToken, clientToken, uuid);
    }
}
