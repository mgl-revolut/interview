package dto;

import java.util.UUID;

public final class ResourceIdentifierDto {

    public ResourceIdentifierDto(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    private final UUID id;
}
