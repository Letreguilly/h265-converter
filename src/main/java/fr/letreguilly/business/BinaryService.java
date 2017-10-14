package fr.letreguilly.business;

import fr.letreguilly.utils.enumeration.OsName;
import fr.letreguilly.utils.helper.CpuUtils;
import fr.letreguilly.utils.helper.OsUtils;
import fr.letreguilly.utils.helper.ResourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class BinaryService {
    public static boolean getBinary(String name) throws IOException {

        //get binary resource
        name += "-" + OsUtils.getOS().toString();
        name += "-" + CpuUtils.getOsArch().toString();

        if (OsUtils.getOS().equals(OsName.windows)) {
            name += ".exe";
        }

        byte[] binaryByteArray = ResourceLoader.readByteArray(name);

        if (binaryByteArray.length < 10) {
            log.error("binary not found : " + name);
            throw new IOException("binary not found : " + name);
        }

        //get bin directory
        File binaryFolder = new File("bin");
        if ((binaryFolder.exists() && binaryFolder.isDirectory() && binaryFolder.canWrite()) == false || binaryFolder.mkdirs() == false) {
            log.error("can not access or create bin folder");
            throw new IOException("can not access or create bin folder");
        }

        //extract binary to bin folder
        File binaryFile = new File("./bin/" + name);
        if (binaryFile.exists() == false) {
            if (binaryFile.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(binaryFile);
                fileOutputStream.write(binaryByteArray);
                fileOutputStream.close();

                return true;
            }else{
                log.error("can not create file " + binaryFile.getAbsolutePath());
                throw new IOException("can not create file " + binaryFile.getAbsolutePath());
            }
        } else {
            log.warn("binary " + name + "already exist");
        }

        //end
        return false;
    }

}
