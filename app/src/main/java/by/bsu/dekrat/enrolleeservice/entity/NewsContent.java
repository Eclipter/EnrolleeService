package by.bsu.dekrat.enrolleeservice.entity;

import java.util.ArrayList;
import java.util.List;

public class NewsContent {

    public static final List<NewsItem> ITEMS = new ArrayList<>();

    private static final int COUNT = 1;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(new NewsItem("DUMMY", "DUMMY", "DUMMY"));
        }
    }

    private static void addItem(NewsItem item) {
        ITEMS.add(item);
    }

    public static class NewsItem {
        public final String title;
        public final String content;
        public final String date;

        public NewsItem(String title, String content, String date) {
            this.title = title;
            this.content = content;
            this.date = date;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
