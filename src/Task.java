public class Task implements Trackable {
    private String taskID;
    private String taskName;
    private int priority;
    private String deadline;
    private int estimatedTime;
    private boolean isCompleted;

//    Parameterized Constructor with the isCompleted false since it's been created for the 1st time
    public Task(String taskID, String taskName, int priority, String deadline, int estimatedTime) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.priority = priority;
        this.deadline = deadline;
        this.estimatedTime = estimatedTime;
        this.isCompleted = false;
    }
//  default Constructor that calls the Parameterized Constructor
    public Task(){
        this("","",0,"",0);
    }

//    deep copy Constructor with new ID
    public Task(Task task, String taskID){
        this.taskID = taskID;
        this.taskName = task.taskName;
        this.priority = task.priority;
        this.deadline = task.deadline;
        this.estimatedTime = task.estimatedTime;
        this.isCompleted = task.isCompleted;
    }

    public Task clone(String taskID) {
        return new Task(this, taskID);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public String getTaskName() {
        return taskName;
    }


    public String getTaskID() {
        return taskID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Task ID: "+ taskID +" | Name: "+ taskName +" | Priority: "+ priority +" | Deadline: "+ deadline +"\n" +
                "| Time: "+ estimatedTime +" | Completed: "+ isCompleted;
    }

    @Override
    public boolean equals(Object task) {
        if(task instanceof Task){
            return !this.taskID.equals(((Task)task).taskID) && this.taskName.equals(((Task)task).taskName)
                    && this.priority == ((Task)task).priority && this.deadline.equals(((Task)task).deadline)
                    && this.estimatedTime == ((Task)task).estimatedTime && this.isCompleted == ((Task)task).isCompleted;
        }
        return false;
    }

    @Override
    public boolean isHighPriority(Task t) {
        return priority >= 8 && t.priority <= 10;
    }
}
