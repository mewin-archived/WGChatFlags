/*
 * Copyright (C) 2013 mewin<mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.mewin.wgcf;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.mewin.util.Util;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class ChatListener implements Listener
{
    private WGCustomFlagsPlugin custPlugin;
    private WorldGuardPlugin wgPlugin;
    private VaultBridge vault;
    
    public ChatListener(WGCustomFlagsPlugin custPlugin, WorldGuardPlugin wgPlugin)
    {
        this.custPlugin = custPlugin;
        this.wgPlugin = wgPlugin;
        this.vault = new VaultBridge();
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerSendChat(AsyncPlayerChatEvent e)
    {
        if (!e.isCancelled())
        {
            String prefix = Util.getFlagValue(wgPlugin, e.getPlayer().getLocation(), WGChatFlagsPlugin.PREFIX_FLAG);
            String suffix = Util.getFlagValue(wgPlugin, e.getPlayer().getLocation(), WGChatFlagsPlugin.SUFFIX_FLAG);
            String chatFormat = Util.getFlagValue(wgPlugin, e.getPlayer().getLocation(), WGChatFlagsPlugin.FORMAT_FLAG);
            
            if (chatFormat == null)
            {
                if (prefix != null)
                {
                    e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix) + e.getFormat());
                }

                if (suffix != null)
                {
                    e.setFormat(e.getFormat() + ChatColor.translateAlternateColorCodes('&', suffix));
                }
            }
            else
            {
                e.setFormat(ChatColor.translateAlternateColorCodes('&', chatFormat).replaceAll("\\{player\\}", "%1$s")
                        .replaceAll("\\{message\\}", "%2$s")
                        .replaceAll("\\{prefix\\}", prefix != null ? prefix : "")
                        .replaceAll("\\{suffix\\}", suffix != null ? suffix : ""));
                
            }
            
            e.setFormat(e.getFormat().replaceAll("\\{x\\}", String.valueOf(e.getPlayer().getLocation().getBlockX()))
                    .replaceAll("\\{y\\}", String.valueOf(e.getPlayer().getLocation().getBlockY()))
                    .replaceAll("\\{z\\}", String.valueOf(e.getPlayer().getLocation().getBlockZ()))
                    .replaceAll("\\{hp\\}", String.valueOf(e.getPlayer().getHealth()))
                    .replaceAll("\\{hpbar\\}", generateHPBar(e.getPlayer().getHealth()))
                    .replaceAll("\\{balance\\}", getBalance(e.getPlayer().getName())));
        }
    }
    
    private String getBalance(String name)
    {
        if (!vault.hasVault())
        {
            return "";
        }
        else
        {
            return vault.getEconomy().format(vault.getEconomy().getBalance(name));
        }
    }
    
    private String generateHPBar(int health)
    {
        String bar = "" + ChatColor.DARK_RED;
        int i = 0;
        for (; i < health - 1; i+= 2)
        {
            bar += "\u2665";
        }
        if (i < health)
        {
            bar += ChatColor.GOLD + "\u2665";
            i++;
        }
        bar += ChatColor.WHITE;
        for (; i < 19; i+= 2)
        {
            bar += "\u2665";
        }
        bar += ChatColor.RESET;
        return bar;
    }
}