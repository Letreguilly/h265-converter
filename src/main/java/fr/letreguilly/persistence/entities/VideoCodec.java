package fr.letreguilly.persistence.entities;

/**
 * Created by letreguilly on 17/07/17.
 */
//ffprobe -v error -select_streams v:0 -show_entries stream=codec_name -of default=noprint_wrappers=1:nokey=1 video.mkv
public enum VideoCodec {
    x264, x263, x265, dvix, h264, h265, hevc, avc, unknown
}
