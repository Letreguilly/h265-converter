package fr.letreguilly.persistence.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "video")
public class Video {
	@Id
    private Long id;

	private String md5;

	private String path;

	private String name;

	private VideoCodec codec;

	private Long size;

	private VideoExtension extension;

    public void setMd5(String md5) {
        this.md5 = md5;

        byte[] md5Bytes = md5.getBytes();
        this.id = 0L;

        for(int i = 0; i < md5Bytes.length ; i++){
            this.id = this.id + (md5Bytes[i] * (256 ^ i));
        }

    }
}
