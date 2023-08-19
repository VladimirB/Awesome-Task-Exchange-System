package ates.homework.accounting.event;

import ates.homework.accounting.entity.UserRole;

public record UserWasCreatedEvent(String publicId, String login, UserRole role) implements VersionedEvent {

    public static final String NAME = "auth.UserWasCreated";
    public static final int VERSION = 1;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }
}
