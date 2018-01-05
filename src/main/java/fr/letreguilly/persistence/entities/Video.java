package fr.letreguilly.persistence.entities;

import fr.letreguilly.Cluster;
import fr.letreguilly.utils.helper.NumberUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.File;
import java.util.Optional;

@Data
@Document(indexName = "video", shards = 2, replicas = 1)
public class Video {
    @Id
    private Long id;

    private String md5;

    private String path;

    private VideoFolder videoFolder;

    private String name;

    private VideoCodec codec;

    private Long size;

    private VideoExtension extension;

    public Video() {
    }

    public Video(VideoFolder videoFolder) {
        this.videoFolder = videoFolder;
    }

    public void setMd5(String md5) {
        this.md5 = md5;

        if (id == null) {
            this.id = NumberUtils.stringToLong(md5);
        }
    }

    public Optional<File> getFile(){
        Optional<File> result = Optional.empty();

        if(this.videoFolder.getNodePathMap().containsKey(Cluster.localNode.getName()) && this.getPath() != null){
            String baseFolderPath = this.videoFolder.getNodePathMap().get(Cluster.localNode.getName());
            String videoPath =  baseFolderPath + this.getPath();

            File videoFile = new File(videoPath);

            if (videoFile.exists() && videoFile.isFile() && videoFile.canWrite()){
                result = Optional.of(videoFile);
            }
        }

        return result;
    }
}