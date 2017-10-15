package fr.letreguilly.utils.helper;

import fr.letreguilly.utils.enumeration.CpuArch;
import fr.letreguilly.utils.enumeration.OsName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class CpuUtils {

    private static Integer cpuCoreNumber;

    public static CpuArch getCpuArch() {
        if (EnumUtils.isValidEnum(CpuArch.class, System.getProperty("os.arch"))) {
            return CpuArch.valueOf(System.getProperty("os.arch"));
        }else{
            return CpuArch.unknown;
        }
    }

    public static Integer getNumberOfCPUCores() {
        if (cpuCoreNumber == null) {
            String command = "";
            Process process = null;
            int numberOfCores = Runtime.getRuntime().availableProcessors();
            int sockets = 1;

            try {

                if (OsUtils.isMac()) {
                    command = "sysctl -n machdep.cpu.core_count";
                } else if (OsUtils.isUnix()) {
                    command = "lscpu";
                } else if (OsUtils.isLinux()) {
                    command = "lscpu";
                } else if (OsUtils.isWindows()) {
                    command = "cmd /C WMIC CPU Get /Format:List";
                }

                if (OsUtils.isMac()) {
                    String[] cmd = {"/bin/sh", "-c", command};
                    process = Runtime.getRuntime().exec(cmd);
                } else {
                    process = Runtime.getRuntime().exec(command);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (OsUtils.isMac()) {
                        numberOfCores = line.length() > 0 ? Integer.parseInt(line) : 0;
                    } else if (OsUtils.isUnix()) {
                        if (line.contains("Core(s) per socket:") || line.contains("Cœur")) {
                            numberOfCores = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
                        }
                        if (line.contains("Socket(s):")) {
                            sockets = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
                        }
                    } else if (OsUtils.isWindows()) {
                        if (line.contains("NumberOfCores")) {
                            numberOfCores = Integer.parseInt(line.split("=")[1]);
                        }
                    }
                }

                cpuCoreNumber = numberOfCores * sockets;

            } catch (Exception e) {
                log.error("can not determine the number of cpu core", e);
                cpuCoreNumber = Runtime.getRuntime().availableProcessors();
            }
        }

        return cpuCoreNumber;
    }
}

