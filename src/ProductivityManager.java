import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProductivityManager {
    private ArrayList<Task> activeTasks;
    private TaskList completedTasks;

    public ProductivityManager() {
        activeTasks = new ArrayList<Task>();
        completedTasks = new TaskList();
    }

    public void addTask(Task task) {
        if(!activeTasks.contains(task)){
            activeTasks.add(task);
            System.out.println("Task ["+ task.getTaskName() +"] successfully added to your active list.");
            return;
        }
        System.out.println("Error: Task ["+ task.getTaskName() +"] already exists. Duplicate entries are not\n" +
                "allowed");
    }

    public void markAsComplete(String taskID){
        Task temp = null;
        for(Task task : activeTasks){
            if(task.getTaskID().equals(taskID)){
                temp = task;
                activeTasks.remove(task);
                break;
            }
        }
        if(temp != null){
            temp.setCompleted(true);
            System.out.println("Task ["+ temp.getTaskName() +"] marked as completed and moved to recent tasks.");
            completedTasks.addToStart(temp);
        }else{
            throw new NoSuchElementException("Element not found with ID: " + taskID);
        }
    }

    public void deleteTask(String taskID){
        Task temp = new Task(null, taskID);
        for(Task task : activeTasks){
            if(task.getTaskID().equals(taskID)){
                temp = task;
                activeTasks.remove(task);
                break;
            }
        }
        int index = completedTasks.getIndex(temp);
        if(index != -1){
            System.out.println(completedTasks.deleteFromIndex(index));
        }
    }

    public void showTopTasks(){
        if(activeTasks.isEmpty()){
            System.out.println("There are no active tasks");
            return;
        }

        ArrayList<Task> copy = new ArrayList<>(activeTasks);

        copy.sort((t1, t2) -> Integer.compare(t2.getPriority(), t1.getPriority()));

        System.out.println("----- Top 3 Priority Tasks -----");
        for(int i = 0 ; i < 3 ; i++) {
            if(!copy.get(i).getTaskID().isEmpty()){
                System.out.println((i+1) +". " + copy.get(i).getTaskName() + "(Priority: " + copy.get(i).getPriority() + ")");
            }
        }
    }

    public ArrayList<Task> showOverdueTasks(){
        ArrayList<Task> overdue = new ArrayList<Task>();
        if(activeTasks.isEmpty()){
            System.out.println("There are no active tasks");
            return overdue;
        }

        LocalDate localDate = LocalDate.now();
        for(Task task : activeTasks){
            try {

                LocalDate taskDate = LocalDate.parse(task.getDeadline());

                if(taskDate.isAfter(localDate)){
                    overdue.add(task);
                }

            }catch (DuplicateTaskException e){
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
        return overdue;
    }

    public Task cloneTask(String taskID) {
        Task current = null;

        for(Task task : activeTasks){
            if(task.getTaskID().equals(taskID)){
                current = task;
                break;
            }
        }
        if(current == null) {
            throw new NoSuchElementException("Element not found with ID: " + taskID);
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Enter New ID: ");
        String id = in.nextLine();

        boolean itHave = false;
        for(Task task : activeTasks){
            if(task.getTaskID().equals(taskID)){
                itHave = true;
                break;
            }
        }
        while(itHave){
            itHave = false;

            System.out.print("ID " + id + " exist. Please enter a New ID: ");
            id = in.nextLine();

            for(Task task : activeTasks){
                if(task.getTaskID().equals(taskID)){
                    itHave = true;
                    break;
                }
            }

        }
        return new Task(current, id);

    }

    public void summaryReport(){
        double sum = 0;
        for(Task task : activeTasks){
            sum += task.getEstimatedTime();
        }
        double avgActiveTasks = sum/activeTasks.size();
        System.out.println("----- Summary Report ----- ");
        System.out.println("Active Tasks : " + activeTasks.size());

        int counter = 0;
        sum = 0;
        TaskList.TaskNode currentNode = completedTasks.getHead();
        while(currentNode != null){
            sum += currentNode.getTask().getEstimatedTime();
            currentNode = currentNode.getNext();
            counter++;
        }

        System.out.println("Completed Tasks : " + counter);
        System.out.println("Average Estimated Time (Active): "+ avgActiveTasks +" hours");
        double avg = sum/counter;
        System.out.println("Average Estimated Time (Completed): "+ avg +" hours");

        ArrayList<Task> overdue = showOverdueTasks();
        System.out.println("You have "+ overdue.size() +" overdue tasks. Keep it up!");
    }

}
