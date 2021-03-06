/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.data;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

/**
 *
 * @author ecortesh
 */
public class Item {

    private File file;

    public Item(File file) {
        this.file = file;
    }
    
    public Item(String pathname) {
        this.file = new File(pathname);
    }
    
    public String getName() {
        return file.getName();
    }

    public String getExtension() {
        return FilenameUtils.getExtension(file.getPath()).toLowerCase();
    }
    
    public String getPath() {
        return file.getPath();
    }
    
    public Path toPath() {
        return file.toPath();
    }
    
    public String getParent() {
        return file.getParent();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }
    
    public boolean isEmpty() {
        boolean empty = file.listFiles().length == 0;
        return empty;
    }

    public long length() {
        return file.length();
    }
    
    public String[] list() {
        return file.list();
    }
    
    public boolean renameTo(Item destino) {
        return file.renameTo(new File(destino.getPath()));
    }
    
    public List<Item> getContentDirectory(Item directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<Item> allDocsLst = new ArrayList<>();
//        Item[] files = directory.listFiles();
        List<Item> fileLst = new ArrayList<>();
        List<Item> directoryLst = new ArrayList<>();
        for (Item file : directory.getList()) {
            if (file.isDirectory()) {
                directoryLst.add(file);
                //directoryContents(file);   //para conocer los archivos de las subcarpetas
            } else {
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }
    
    public List<Item> getListDirectories(Item directory) {
        List<Item> list = new ArrayList<>();
        for (File f : new File(directory.getPath()).listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
            list.add(new Item(f));
        }
        return list;
    }

    public List<Item> getList() {
        List<Item> list = new ArrayList<>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                list.add(new Item(f));
            }
        }
        return list;
    }

}
