package by.bsu.dekrat.enrolleeservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsContent {

    public static final List<NewsItem> ITEMS = new ArrayList<>();

    public static final Map<String, NewsItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createNewsItem(i));
        }
    }

    private static void addItem(NewsItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static NewsItem createNewsItem(int position) {
        return new NewsItem(String.valueOf(position), "News " + position, "Simple description", new Date());
    }

    public static class NewsItem {
        public final String id;
        public final String title;
        public final String description;
        public final Date additionDate;

        public NewsItem(String id, String title, String description, Date additionDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.additionDate = additionDate;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
