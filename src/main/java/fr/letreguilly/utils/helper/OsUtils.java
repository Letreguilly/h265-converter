package fr.letreguilly.utils.helper;

import fr.letreguilly.utils.enumeration.OsName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public  class OsUtils {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static  boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("aix") > 0);
    }

    public static boolean isLinux() {
        return ( OS.indexOf("nux") >= 0);
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }

    public static OsName getOS() {
        if (isWindows()) {
            return OsName.windows;
        } else if (isMac()) {
            return OsName.macos;
        } else if (isUnix()) {
            return OsName.unix;
        } else if (isSolaris()) {
            return OsName.solaris;
        }else if (isLinux()) {
            return OsName.linux;
        } else {
            return OsName.unknown;
        }
    }
}
