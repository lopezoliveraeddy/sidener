package electorum.sidener.domain;

import java.io.File;
import java.util.HashSet;



public class FlowInfo {

    private int      flowChunkSize;
    private long     flowTotalSize;
    private String   flowIdentifier;
    private String   flowFilename;
    private String   flowRelativePath;

    public int getFlowChunkSize() {
        return flowChunkSize;
    }
    public void setFlowChunkSize(int flowChunkSize) {
        this.flowChunkSize = flowChunkSize;
    }
    public long getFlowTotalSize() {
        return flowTotalSize;
    }
    public void setFlowTotalSize(long flowTotalSize) {
        this.flowTotalSize = flowTotalSize;
    }
    public String getFlowIdentifier() {
        return flowIdentifier;
    }
    public void setFlowIdentifier(String flowIdentifier) {
        this.flowIdentifier = flowIdentifier;
    }
    public String getFlowFilename() {
        return flowFilename;
    }
    public void setFlowFilename(String flowFilename) {
        this.flowFilename = flowFilename;
    }
    public String getFlowRelativePath() {
        return flowRelativePath;
    }
    public void setFlowRelativePath(String flowRelativePath) {
        this.flowRelativePath = flowRelativePath;
    }
    public HashSet<flowChunkNumber> getUploadedChunks() {
        return uploadedChunks;
    }
    public void setUploadedChunks(HashSet<flowChunkNumber> uploadedChunks) {
        this.uploadedChunks = uploadedChunks;
    }
    public String getFlowFilePath() {
        return flowFilePath;
    }
    public void setFlowFilePath(String flowFilePath) {
        this.flowFilePath = flowFilePath;
    }
    public static class flowChunkNumber {
        public flowChunkNumber(int number) {
            this.number = number;
        }

        public int number;

        @Override
        public boolean equals(Object obj) {
            return obj instanceof flowChunkNumber
                ? ((flowChunkNumber)obj).number == this.number : false;
        }

        @Override
        public int hashCode() {
            return number;
        }
    }

    //Chunks uploaded
    public HashSet<flowChunkNumber> uploadedChunks = new HashSet<flowChunkNumber>();

    public String flowFilePath;

    public boolean valid(){
        if (flowChunkSize < 0 || flowTotalSize < 0
            || HttpUtils.isEmpty(flowIdentifier)
            || HttpUtils.isEmpty(flowFilename)
            || HttpUtils.isEmpty(flowRelativePath)) {
            return false;
        } else {
            return true;
        }
    }
    public String checkIfUploadFinished() {
        //check if upload finished
        int count = (int) Math.ceil(((double) flowTotalSize) / ((double) flowChunkSize));
        for(int i = 1; i < count + 1; i ++) {
            if (!uploadedChunks.contains(new flowChunkNumber(i))) {
                return null;
            }
        }

        //Upload finished, change filename.
        File file = new File(flowFilePath);
        String new_path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - ".temp".length());
        file.renameTo(new File(new_path));
        return new_path;
    }
    @Override
    public String toString() {
        return "FlowInfo [flowChunkSize=" + flowChunkSize + ", flowTotalSize=" + flowTotalSize + ", flowIdentifier="
            + flowIdentifier + ", flowFilename=" + flowFilename + ", flowRelativePath=" + flowRelativePath
            + ", uploadedChunks=" + uploadedChunks + ", flowFilePath=" + flowFilePath + "]";
    }
}

