package CKD.Android;


import android.support.annotation.NonNull;

public class ThreadClass implements Comparable<ThreadClass>{
    private String author;
    private String authorUID;
    private String title;
    private String body;
    private String date;
    private String category;
    private int likes;
    private String threadUID;


    public ThreadClass()
    {

    }


    ThreadClass(String author, String authorUID, String title, String body, String date, String category, int likes) {
        this.author = author;
        this.authorUID = authorUID;
        this.title = title;
        this.body = body;
        this.date = date;
        this.category = category;
        this.likes = likes;
    }

    String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLikes(int likes){this.likes = likes;}

    public int getLikes(){return this.likes;}

    @Override
    public int compareTo(@NonNull ThreadClass thread2) {
        int likes1 = this.getLikes();
        int likes2 = thread2.getLikes();

        return Integer.compare(likes2,likes1);
    }

    public String getThreadUID()
    {
        return threadUID;
    }

    public void setThreadUID(String threadUID) {
        this.threadUID = threadUID;
    }
}
