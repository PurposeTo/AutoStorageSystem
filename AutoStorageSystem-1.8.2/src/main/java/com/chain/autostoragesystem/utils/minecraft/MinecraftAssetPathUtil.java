package com.chain.autostoragesystem.utils.minecraft;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.utils.common.StringUtils;

public class MinecraftAssetPathUtil {

    public static String assertAndExecutePath(String path) {
        //todo modId здесь также название папки в assets!

        StringUtils.requiredNonBlank(path);

        String[] words = path.split(":");

        if (words.length != 2) {
            throw new IllegalArgumentException("Path must have two parts <modId>:<assetPath>. Path: " + path);
        }

        String pathModId = words[0];
        if (!pathModId.equals(ModMain.MOD_ID)) {
            throw new IllegalArgumentException("\"" + pathModId + "\" is unknown modId");
        }

        return words[1];
    }
}
