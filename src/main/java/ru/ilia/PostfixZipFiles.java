package ru.ilia;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by ILIA on 22.04.2017.
 */
public class PostfixZipFiles extends Task {

    private String jobid;
    private String location;
    private Vector filesets = new Vector();

    public void setLocation(String location) {
        this.location = location;
    }

    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    @Override
    public void execute() throws BuildException {
        String postfix;
        if(jobid.trim().isEmpty()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            postfix = sdf.format(timestamp);
        } else {
            postfix = jobid;
        }
        for(Iterator itFSets = filesets.iterator(); itFSets.hasNext(); ) {
            FileSet fileSet = (FileSet) itFSets.next();
            DirectoryScanner ds = fileSet.getDirectoryScanner();
            String[] includedFiles = ds.getIncludedFiles();
            for(int i=0; i<includedFiles.length; i++) {
                File currentFile=new File(location + "/" + includedFiles[i]);

                /* Example: text.bla-bla.txt */
                /* splitName[0] - name (text.bla-bla) */
                /* splitName[1] - extension (txt) */
                String[] splitName=currentFile.getName().split("\\.(?=[^\\.]+$)");

                String newName = splitName[0]+"-"+postfix+"."+splitName[1];
                log(currentFile.getParent());
                log(currentFile.renameTo(new File(currentFile.getParent() +"/"+newName)) ? "true" : "false");
            }
        }

    }
}
