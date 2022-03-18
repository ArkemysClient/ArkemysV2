package com.rastiq.arkemys.ias.account;

import java.util.UUID;
import java.util.function.Consumer;

import com.mojang.util.UUIDTypeAdapter;
import com.rastiq.arkemys.mixins.accessor.SessionAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class OfflineAccount implements Account {
	private String username;
	public int uses;
	public long lastUse;
	
	public OfflineAccount(String name) {
		this.username = name;
	}

	@Override
	public String alias() {
		return username;
	}

	@Override
	public void login(Minecraft mc, Consumer<Throwable> handler) {
		new Thread(() -> {
			mc.addScheduledTask(() -> {
				((SessionAccessor) Minecraft.getMinecraft()).setSession(new Session(alias(), UUIDTypeAdapter.fromUUID(new UUID(0, 0)), "0", "legacy"));
				handler.accept(null);
			});
		}, "Crack Reauth Thread").start();

	}

	@Override
	public boolean editable() {
		return true;
	}

	@Override
	public boolean online() {
		return false;
	}
	
	@Override
	public int uses() {
		return uses;
	}

	@Override
	public long lastUse() {
		return lastUse;
	}
	
	@Override
	public void use() {
		uses++;
		lastUse = System.currentTimeMillis();
	}
}
