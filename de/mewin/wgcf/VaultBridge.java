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

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class VaultBridge
{
    private boolean hasVault;
    private Economy economy;
    private Chat chat;
    
    public VaultBridge()
    {
        hasVault = Bukkit.getPluginManager().getPlugin("Vault") != null;
        
        if (hasVault)
        {
            RegisteredServiceProvider<Economy> ecoProv = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (ecoProv != null)
            {
                economy = ecoProv.getProvider();
            }
            else
            {
                economy = null;
            }
            
            RegisteredServiceProvider<Chat> chatProv = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
            if (chatProv != null)
            {
                chat = chatProv.getProvider();
            }
            else
            {
                chat = null;
            }
        }
    }
    
    public boolean hasVault()
    {
        return this.hasVault;
    }
    
    public Economy getEconomy()
    {
        return this.economy;
    }
    
    public Chat getChat()
    {
        return this.chat;
    }
}