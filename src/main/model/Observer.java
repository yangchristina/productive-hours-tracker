package model;

public interface Observer {
    void update(ProductivityEntry curr, ProductivityEntry old);
}
