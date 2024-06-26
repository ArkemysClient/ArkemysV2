package com.rastiq.arkemys.ias.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.ias.account.Account;
import com.rastiq.arkemys.ias.account.AuthException;
import com.rastiq.arkemys.ias.account.MicrosoftAccount;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import com.rastiq.arkemys.ias.Config;
import com.rastiq.arkemys.ias.utils.Auth;

/**
 * Screen for adding Microsoft accounts.
 * @author VidTu
 */
public class MSAuthScreen extends GuiScreen {
	public static final String[] symbols = new String[]{"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄",
			"_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ _ _ ▃ ▄ ▅ ▆ ▇ █",
			"_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄",
			"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _",
			"▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "█ ▇ ▆ ▅ ▄ ▃ _ _ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _",
			"▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _"};
	public final GuiScreen prev;
	private HttpServer srv;
	private int tick;
	private String state = "Regardez votre navigateur";
	private List<String> error;
	private Consumer<Account> handler;
	
	public MSAuthScreen(GuiScreen prev, Consumer<Account> handler) {
		this.prev = prev;
		this.handler = handler;
		String done = "<html><body><h1>" + "Vous pouvez fermer cette fenêtre." + "</h1></body></html>";
		new Thread(() -> {
			try {
				srv = HttpServer.create(new InetSocketAddress(59125), 0);
	        	srv.createContext("/", new HttpHandler() {
					public void handle(HttpExchange ex) throws IOException {
						try {
							ex.getResponseHeaders().add("Location", "http://localhost:59125/end");
							ex.sendResponseHeaders(302, -1);
							new Thread(() -> auth(ex.getRequestURI().getQuery()), "IAS MS Auth Thread").start();
						} catch (Throwable t) {
							Client.warn("Unable to process request 'auth' on MS auth server", t);
							try {
								if (srv != null) srv.stop(0);
							} catch (Throwable th) {
								Client.warn("Unable to stop fail-requested MS auth server", th);
							}
						}
					}
				});
	        	srv.createContext("/end", new HttpHandler() {
					public void handle(HttpExchange ex) throws IOException {
						try {
							byte[] b = done.getBytes(StandardCharsets.UTF_8);
							ex.getResponseHeaders().put("Content-Type", Arrays.asList("text/html; charset=UTF-8"));
							ex.sendResponseHeaders(200, b.length);
							OutputStream os = ex.getResponseBody();
							os.write(b);
							os.flush();
							os.close();
							try {
								if (srv != null) srv.stop(0);
							} catch (Throwable th) {
								Client.warn("Unable to stop MS auth server", th);
							}
						} catch (Throwable t) {
							Client.warn("Unable to process request 'end' on MS auth server", t);
							try {
								if (srv != null) srv.stop(0);
							} catch (Throwable th) {
								Client.warn("Unable to stop fail-requested MS auth server", th);
							}
						}
					}
				});
	        	srv.start();
	        	Sys.openURL("https://login.live.com/oauth20_authorize.srf" +
                        "?client_id=54fd49e4-2103-4044-9603-2b028c814ec3" +
                        "&response_type=code" +
                        "&scope=XboxLive.signin%20XboxLive.offline_access" +
                        "&redirect_uri=http://localhost:59125" +
                        "&prompt=consent");
			} catch (Throwable t) {
				Client.warn("Unable to start MS auth server", t);
				try {
					if (srv != null) srv.stop(0);
				} catch (Throwable th) {
					Client.warn("Unable to stop fail-started MS auth server", th);
				}
				error(t);
			}
		}, "IAS MS Auth Server Thread").start();
	}
	
	private void auth(String query) {
		try {
			state = "Chargement...";
			if (query == null) throw new NullPointerException("query=null");
			if (query.equals("error=access_denied&error_description=The user has denied access to the scope requested by the client application."))
				throw new AuthException(new ChatComponentTranslation("ias.msauth.error.revoked"));
			if (!query.startsWith("code=")) throw new IllegalStateException("query=" + query);
			Pair<String, String> authRefreshTokens = Auth.authCode2Token(query.replace("code=", ""));
			String refreshToken = authRefreshTokens.getRight();
			String xblToken = Auth.authXBL(authRefreshTokens.getLeft()); //authToken
			Pair<String, String> xstsTokenUserhash = Auth.authXSTS(xblToken);
			String accessToken = Auth.authMinecraft(xstsTokenUserhash.getRight(), xstsTokenUserhash.getLeft());
			Auth.checkGameOwnership(accessToken);
			Pair<UUID, String> profile = Auth.getProfile(accessToken);
			if (Config.accounts.stream().anyMatch(acc -> acc.alias().equalsIgnoreCase(profile.getRight())))
				throw new AuthException(new ChatComponentTranslation("ias.auth.alreadyexists"));
			mc.addScheduledTask(() -> {
				if (mc.currentScreen == this) {
					handler.accept(new MicrosoftAccount(profile.getRight(), accessToken, refreshToken, profile.getLeft()));
					mc.displayGuiScreen(prev);
				}
			});
		} catch (Throwable t) {
			Client.warn("Unable to auth thru MS", t);
			error(t);
		}
	}
	
	public void error(Throwable t) {
		mc.addScheduledTask(() -> {
			if (t instanceof AuthException) {
				error = fontRendererObj.listFormattedStringToWidth(((AuthException)t).getText().getFormattedText(), width - 20);
			} else {
				error = fontRendererObj.listFormattedStringToWidth("Inconnu: " + t.toString(), width - 20);
			}
		});
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height - 28, 150, 20, I18n.format("gui.cancel")));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) mc.displayGuiScreen(prev);
	}
	
	@Override
	public void updateScreen() {
		tick++;
	}
	
	@Override
	public void onGuiClosed() {
		try {
			if (srv != null) srv.stop(0);
		} catch (Throwable t) {
			Client.warn("Unable to stop MS auth server", t);
		}
		super.onGuiClosed();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prev);
			return;
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float delta) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Connexion Microsoft", this.width / 2, 7, -1);
		if (error != null) {
			for (int i = 0; i < error.size(); i++) {
				drawCenteredString(fontRendererObj, error.get(i), this.width / 2, height / 2 - 5 + i * 10 - error.size() * 5, 0xFFFF0000);
				if (i > 6) break; //Exceptions can be very large.
			}
		} else {
			drawCenteredString(fontRendererObj, state, width / 2, height / 2 - 10, -1);
			drawCenteredString(fontRendererObj, symbols[tick % symbols.length], width / 2, height / 2, 0xFFFF9900);
		}
		super.drawScreen(mouseX, mouseY, delta);
	}
}
