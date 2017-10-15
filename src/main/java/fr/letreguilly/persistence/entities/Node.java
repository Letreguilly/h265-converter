package fr.letreguilly.persistence.entities;

import fr.letreguilly.utils.enumeration.CpuArch;
import fr.letreguilly.utils.enumeration.OsName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "node", shards = 1, replicas = 2)
public class Node {
    @Id
    private Long id;

    private String name;

    private byte[] IPAddress;

    private byte[] macAddress;

    private Integer cpuCore;

    private OsName operatingSystem;

    private CpuArch cpuArch;
}
