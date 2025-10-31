import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProductivityManager2 {
    private TaskList activeTasks;
    private ArrayList<Task> completedTasks;

    public ProductivityManager2() {
        activeTasks = new TaskList();
        completedTasks = new ArrayList<Task>();
    }

    public void addTask(Task task) {
        System.out.println(activeTasks.addToStart(task));
    }
    public void addCompletedTask(String taskID) {
        Task task = activeTasks.find(taskID);
        if(task == null) {
            throw new NoSuchElementException("Element not found with ID: " + taskID);
        }
        task.setCompleted(true);
        completedTasks.add(task);
        System.out.println("Task ["+ task.getTaskName() +"] marked as completed and moved to recent tasks.");
        int index = activeTasks.getIndex(task);
        activeTasks.deleteFromIndex(index);
    }

    public void deleteTask(String taskID) {
        Task task = activeTasks.find(taskID);
        if(task == null) {
            throw new NoSuchElementException("Element not found with ID: " + taskID);
        }
        if(task.isCompleted()) {
            completedTasks.remove(task);
        }
        int index = activeTasks.getIndex(task);
        System.out.println(activeTasks.deleteFromIndex(index));
    }

    public void showTopTasks(){
        Task[] top3 = {new Task(), new Task(), new Task()};

        TaskList.TaskNode current = activeTasks.getHead();
        while (current != null) {
            Task task = current.getTask();
            if (task != null) {
                for (int i = 0; i < top3.length; i++) {
                    if (top3[i] == null || task.getPriority() > top3[i].getPriority()) {
                        for (int j = top3.length - 1; j > i; j--) {
                            top3[j] = top3[j - 1];
                        }
                        top3[i] = task;
                        break;
                    }
                }
            }
            current = current.getNext();
        }

        System.out.println("----- Top 3 Priority Tasks -----");
        for(int i = 0 ; i < top3.length ; i++) {
            if(!top3[i].getTaskID().isEmpty()){
                System.out.println((i+1) +". " + top3[i].getTaskName() + "(Priority: " + top3[i].getPriority() + ")");
            }
        }
    }

    public ArrayList<Task> showOverdueTasks(){
        ArrayList<Task> overdue = new ArrayList<Task>();
        TaskList.TaskNode currentNode = activeTasks.getHead();

        while(currentNode != null){
            try{
                LocalDate localDate = LocalDate.now();
                LocalDate taskDate = LocalDate.parse(currentNode.getTask().getDeadline());

                if(taskDate.isAfter(localDate)){
                    overdue.add(currentNode.getTask());
                }
                currentNode = currentNode.getNext();

            }catch (DuplicateTaskException e){
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        return overdue;
    }

    public Task cloneTask(String taskID) {
        Task task = activeTasks.find(taskID);
        if(task == null) {
            throw new NoSuchElementException("Element not found with ID: " + taskID);
        }
        Scanner in = new Scanner(System.in);
        System.out.print("Enter New ID: ");
        String id = in.nextLine();
        while(activeTasks.find(id) != null){
            System.out.print("ID " + id + " exist. Please enter a New ID: ");
            id = in.nextLine();
        }
        return new Task(task, id);
    }

    public void summaryReport(){
        TaskList.TaskNode currentNode = activeTasks.getHead();
        int counter = 0;
        double sum = 0;
        while(currentNode != null){
            sum += currentNode.getTask().getEstimatedTime();
            currentNode = currentNode.getNext();
            counter++;
        }
        double avg = sum/counter;
        System.out.println("----- Summary Report ----- ");
        System.out.println("Active Tasks : " + counter);
        System.out.println("Completed Tasks : " + completedTasks.size());
        System.out.println("Average Estimated Time (Active): "+ avg +" hours");

        sum = 0;
        for(int i = 0; i < completedTasks.size(); i++){
            sum += completedTasks.get(i).getEstimatedTime();
        }
        avg = sum/completedTasks.size();
        System.out.println("Average Estimated Time (Completed): "+ avg +" hours");

        ArrayList<Task> overdue = showOverdueTasks();
        System.out.println("You have "+ overdue.size() +" overdue tasks. Keep it up!");
    }

}
