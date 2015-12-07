package com.linspery.bean;

import java.util.List;

/**
 * Created by Linspery on 15/12/1.
 */
public class NewsMessage extends BaseMessage{

    private int ArticleCount;
    private List<News> Articles;

    public List<News> getArticles() {
        return Articles;
    }

    public void setArticles(List<News> articles) {
        Articles = articles;
    }

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }
}
