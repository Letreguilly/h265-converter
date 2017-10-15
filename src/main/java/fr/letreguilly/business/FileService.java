package fr.letreguilly.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileService {

    /**
     * recursive function to list all file present in a directory
     * @param directory the directory to list
     * @return the file list
     */
    public static List<File> listFile(File directory){  // or listFile(VideoFolder folder) and then File directory = NodeService.getNodeFolderFile(folder);
        List<File> fileList = new ArrayList();

        if(directory.canRead() && directory.isDirectory()){

            File[] currentDirectoryFiles =  directory.listFiles();

            for(int i = 0 ; i < currentDirectoryFiles.length; ++i){
                if(currentDirectoryFiles[i].isDirectory()){
                    fileList.addAll(FileService.listFile(currentDirectoryFiles[i]));
                }else{
                    fileList.add(currentDirectoryFiles[i]);
                }
            }
        }

        return fileList;
    }

    /**
     * return file modified after the lastmodificationdate parameter
     * @param filesToFilter the list of file to filter
     * @param lastModificationDate the last modification date
     * @return
     */
    public static List<File> filterByLastModificationDate( List<File> filesToFilter, Date lastModificationDate){
        List<File> filteredFiles = new ArrayList();

        for(File f : filesToFilter){
            if(f.lastModified() > lastModificationDate.getTime()){
                filteredFiles.add(f);
            }
        }

        return filteredFiles;
    }
}
