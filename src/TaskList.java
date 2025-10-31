import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TaskList {
    public class TaskNode{
        private Task task;
        private TaskNode next;

        public TaskNode(Task task, TaskNode next){
            this.task = task;
            this.next = next;
        }

        public TaskNode(){
            this(null,null);
        }

        public TaskNode(TaskNode task){
            this.task = task.task;
            this.next = task.next;
        }

        public TaskNode clone(){
            return new TaskNode(this);
        }

        public TaskNode getNext() {
            return next;
        }

        public Task getTask() {
            return task;
        }

        public void setNext(TaskNode next) {
            this.next = next;
        }

        public void setTask(Task task) {
            this.task = task;
        }
    }

    private TaskNode head;
    private int size;

    public TaskList(){
        head = new TaskNode();
        size = 0;
    }

    public String addToStart(Task task){
        if(task == null){
            throw new InvalidTaskException();
        }
        if(!contains(task.getTaskID())){
            TaskNode newNode = new TaskNode(task, head);
            head = newNode;
            size++;
            return "Task ["+ task.getTaskName() +"] successfully added to your list.";
        }
        return "Error: Task ["+ task.getTaskName() +"] already exists. Duplicate entries are not\n" +
                "allowed";
    }

    public String insertAtIndex(Task task, int index){
        if(task == null){
            throw new InvalidTaskException();
        }
        if(index < 0 || index > size){
            throw new NoSuchElementException("Index out of range: " + index);
        }


        if(index == 0){
            addToStart(task);
        }else if(!contains(task.getTaskID())){

            TaskNode currentNode = head;
            int currentIndex = 0;

            while( currentNode != null && currentIndex < index-1){
                currentNode = currentNode.getNext();
                currentIndex++;
            }

            TaskNode newNode = new TaskNode(task, currentNode.getNext());
            currentNode.setNext(newNode);

            size++;
            return "Task ["+ task.getTaskName() +"] successfully added to your list.";
        }
        return "Error: Task [Buy_Groceries] already exists. Duplicate entries are not\n" +
                "allowed";
    }

    public String deleteFromIndex(int index){
        if(index < 0 || index > size){
            throw new NoSuchElementException("Index out of range: " + index);
        }
        String returnIndo = "";

        if(index == 0){
            returnIndo = deleteFromStart();
        }else{
            TaskNode currentNode = head;
            int currentIndex = 0;
            while( currentNode != null && currentIndex < index-1){
                currentNode = currentNode.getNext();
                currentIndex++;
            }

            if(currentNode == null || currentNode.getNext() == null){
                throw new NoSuchElementException("Index out of range: " + index);
            }
            TaskNode info = currentNode.getNext();

            currentNode.setNext(currentNode.getNext().getNext());
            size--;
            return "Task ["+ info.task.getTaskName() +"] successfully deleted from your list.";
        }
        return returnIndo;
    }

    public String deleteFromStart(){
        if(head == null){
            throw new NoSuchElementException("List is empty. Nothing to delete.");
        }
        TaskNode info = head;

        head = head.getNext();
        size--;
        return "Task ["+ info.task.getTaskName() +"] successfully deleted from your list.";
    }

    public String replaceAtIndex(Task task, int index){
        if(index < 0 || index > size){
            throw new NoSuchElementException("Index out of range: " + index);
        }
        if(task == null){
            throw new InvalidTaskException();
        }
        if(contains(task.getTaskID())){
            throw new DuplicateTaskException();
        }

        TaskNode currentNode = head;
        int currentIndex = 0;

        while( currentNode != null && currentIndex < index-1){
            currentNode = currentNode.getNext();
            currentIndex++;
        }

        currentNode.setTask(task);
        return "Task ["+ task.getTaskName() +"] successfully replaced in your list.";
    }

    public Task find(String taskID){
        TaskNode currentNode = head;
        int currentIndex = 0;
        while( currentNode != null){
            if(currentNode.getTask().getTaskID().equals(taskID)){
                System.out.println("Task ["+ currentNode.getTask().getTaskName() +"] is found on the"
                        + currentIndex + " iterations");
                return new Task(currentNode.getTask(), taskID);
            }
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        return null;
    }

    public boolean contains(String task){
        TaskNode currentNode = head;
        while( currentNode != null){
            if(currentNode.getTask().getTaskID().equals(task)){
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    public boolean equals(TaskList other) {
        if(other == null){
            return false;
        }

        TaskNode currentNode1 = head;
        TaskNode currentNode2 = other.head;

        while(currentNode1 != null && currentNode2 != null){
           if(!currentNode1.getTask().equals(currentNode2.getTask())){
               return false;
           }
           currentNode1 = currentNode1.getNext();
           currentNode2 = currentNode2.getNext();
        }

        return currentNode1 == null && currentNode2 == null;
    }

    public int getIndex(Task task){
        if(contains(task.getTaskID())){
            TaskNode currentNode = head;
            int currentIndex = 0;
            while (currentNode != null && currentIndex < size){
                if(currentNode.getTask().getTaskID().equals(task.getTaskID())){
                    return currentIndex;
                }
            }
        }
        return -1;
    }

    public int getSize() {
        return size;
    }

    public TaskNode getHead() {
        return head;
    }

//    public Task[] findTopTasks(){
//        Task[] top3 = {new Task(), new Task(), new Task()};
//
//        if(head == null){
//            return new Task[0];
//        }
//
//        TaskNode current = head;
//        while (current != null) {
//            Task task = current.getTask();
//            if (task != null) {
//                for (int i = 0; i < top3.length; i++) {
//                    if (top3[i] == null || task.getPriority() > top3[i].getPriority()) {
//                       for (int j = top3.length - 1; j > i; j--) {
//                           top3[j] = top3[j - 1];
//                        }
//                        top3[i] = task;
//                        break;
//                    }
//                }
//            }
//            current = current.getNext();
//        }
//        return top3;
//    }

//    public ArrayList<Task> overdue(){
//        ArrayList<Task> overdue = new ArrayList<Task>();
//        TaskNode currentNode = head;
//
//        while(currentNode != null){
//            try{
//                LocalDate localDate = LocalDate.now();
//                LocalDate taskDate = LocalDate.parse(currentNode.getTask().getDeadline());
//
//                if(taskDate.isAfter(localDate)){
//                    overdue.add(currentNode.getTask());
//                }
//                currentNode = currentNode.getNext();
//
//            }catch (DuplicateTaskException e){
//                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
//            }
//        }
//        return overdue;
//    }



}
