package net.dimmid.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ClientEventMessageMixin {
    @JsonIgnore
    String userId;
}
