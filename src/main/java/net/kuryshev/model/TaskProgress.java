package net.kuryshev.model;

public class TaskProgress {

    private int status;
    private boolean done;
    private String progress;

    public TaskProgress() {
        status = 1;
        done = false;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProgress() {
        return "{\n" + "    \"status\": " + status + ",\n" + "    \"done\": " + done + ",\n" + "    \"statusText\": \"" + progress + "\"\n" + "}";
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
