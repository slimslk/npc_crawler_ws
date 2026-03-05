package net.dimmid.ws.util;

import io.netty.util.AttributeKey;
import net.dimmid.ws.entity.User;

public final class WSAttributes {
    public static final AttributeKey<User> USER = AttributeKey.valueOf("user");

    private WSAttributes() {}
}
