package com.demigodsrpg.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.*;

public class PotionEffectUtil {
    private PotionEffectUtil() {
    }

    public static String serializePotionEffects(Collection<PotionEffect> effects) {
        PotionEffect[] eff = new PotionEffect[effects.size()];
        int count = 0;
        for (PotionEffect effect : effects) {
            eff[count] = effect;
            count++;
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bos = new BukkitObjectOutputStream(os);
            bos.writeObject(eff);

            String hex = BukkitObjectUtil.byteArrayToHexString(os.toByteArray());

            bos.close();
            os.close();
            return hex;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<PotionEffect> deserializePotionEffects(String s) {
        try {
            byte[] b = BukkitObjectUtil.hexStringToByteArray(s);
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);

            PotionEffect[] eff = (PotionEffect[]) bois.readObject();

            bois.close();
            bais.close();
            return Arrays.asList(eff);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
