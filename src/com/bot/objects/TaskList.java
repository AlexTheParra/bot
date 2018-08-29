package com.bot.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    @SerializedName("tasks")
    @Expose
    List<Task> taskList = new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addToTaskList(List<Task> taskList) {
        int lastI = this.taskList.size();
        Integer lastID = 0;
        if (lastI != 0) {
            lastID = this.taskList.get(lastI - 1).getId();
        }
        for (Task t : taskList) {
            lastID = lastID + 1;
            t.setId(lastID);
            this.taskList.add(t);
        }
    }
}
