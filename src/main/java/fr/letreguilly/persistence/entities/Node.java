package fr.letreguilly.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.letreguilly.utils.enumeration.CpuArch;
import fr.letreguilly.utils.enumeration.OsName;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "node", shards = 2, replicas = 1)
public class Node {

    @Id
    private Long id;

    private String name;

    private String IPAddress;

    private String macAddress;

    private Integer cpuCore;

    private OsName operatingSystem;

    private CpuArch cpuArch;

    public void setName(String name) {
        this.id = NumberUtils.bytesToLong(name.getBytes());
        this.name = name;
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }
}
