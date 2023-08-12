package ates.homework.auth.broker;

public record Event<T>(String name, int version, T data) {
}