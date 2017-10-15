package fr.letreguilly.utils.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class ResourceLoader {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] readByteArray(String fileName) throws IOException {
        byte[] encoded;

        ClassPathResource resource = new ClassPathResource(fileName);
        try {
            InputStream inputStream = resource.getInputStream();
            encoded = IOUtils.toByteArray(inputStream);

        } catch (IOException e1) {
            throw new RuntimeException("Error reading resource file \"" + fileName + "\"");
        }

        log.debug("Read resource file \"" + fileName + "\"");
        return encoded;
    }

    public static String readString(String fileName) throws IOException {
        return new String(readByteArray(fileName), "UTF-8");
    }

    public static <ResourceType> ResourceType readType(String fileName, Class<ResourceType> resourceTypeClass) throws IOException {
        String resourceString = readString(fileName);

        return objectMapper.readValue(resourceString, resourceTypeClass);
    }
}
