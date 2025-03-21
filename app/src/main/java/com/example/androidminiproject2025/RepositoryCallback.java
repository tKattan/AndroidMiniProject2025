package com.example.androidminiproject2025;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
