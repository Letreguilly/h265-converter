package fr.letreguilly.business;

import fr.letreguilly.utils.enumeration.OsName;
import fr.letreguilly.utils.helper.CpuUtils;
import fr.letreguilly.utils.helper.OsUtils;
import fr.letreguilly.utils.helper.ResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class BinaryControler {

    @PostConstruct
    private void installFFmpegAtStartUp() throws IOException {
        this.getBinary("ffmpeg");
    }

    @PostConstruct
    private void installFFprobeAtStartUp() throws IOException {
        Boolean result = this.getBinary("ffprobe");
    }

    /**
     * @param name the name of the binary to extract
     *             return the file to a binary
     * @return the binary file (can not exist)
     */
    public File getBinaryFile(String name) {
        //get binary name
        String binaryFilepath;
        if (OsUtils.getOS().equals(OsName.windows)) {
            binaryFilepath = "./bin/" + name + ".exe";
        } else {
            binaryFilepath = "./bin/" + name;
        }

        File binaryFile = new File(binaryFilepath);

        return binaryFile;
    }

    /**
     * get a binary from resources and extract it to bin folder
     *
     * @param name the name of the binary to extract
     * @return
     * @throws IOException
     */
    public boolean getBinary(String name) throws IOException {

        File binaryFile = this.getBinaryFile(name);

        if (binaryFile.exists()) {
            return true;
        } else {
            //get binary resource
            String resourceName = "bin/" + name + "/" + name;
            resourceName += "-" + OsUtils.getOS().toString();
            resourceName += "-" + CpuUtils.getCpuArch().toString();

            if (OsUtils.getOS().equals(OsName.windows)) {
                resourceName += ".exe";
            }

            byte[] binaryByteArray = ResourceLoader.readByteArray(resourceName);

            if (binaryByteArray.length < 10) {
                log.error("binary not found : " + name);
                throw new IOException("binary not found : " + name + "resolved as " + resourceName);
            }

            //get bin directory
            File binaryFolder = new File("bin");
            if (binaryFolder.exists() == false) {
                if (binaryFolder.mkdirs() == false) {
                    log.error("can not create bin folder, check permission");
                    throw new IOException("can not create bin folder, check permission");
                }
            } else {
                if (binaryFolder.isDirectory() == false) {
                    log.error("can not access bin folder, not a directory");
                    throw new IOException("can not access bin folder, not a directory");
                }

                if (binaryFolder.canWrite() == false) {
                    log.error("can not access bin folder, check permission");
                    throw new IOException("can not access bin folder, check permission");
                }
            }

            //extract binary to bin folder
            if (binaryFile.exists() == false) {
                if (binaryFile.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(binaryFile);
                    fileOutputStream.write(binaryByteArray);
                    fileOutputStream.close();

                    return true;
                } else {
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
}
