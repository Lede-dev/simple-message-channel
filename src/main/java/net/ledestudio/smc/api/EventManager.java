package net.ledestudio.smc.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EventManager {

    private static final Set<ExceptionCaughtEvent> exceptionEvents = Sets.newConcurrentHashSet();
    private static final Set<MessageEvent> messageEvents = Sets.newConcurrentHashSet();

    public static void registerExceptionCaughtEvents(@NotNull ExceptionCaughtEvent... events) {
        exceptionEvents.addAll(Lists.newArrayList(events));
    }

    public static Set<ExceptionCaughtEvent> getExceptionCaughtEvents() {
        return Sets.newHashSet(exceptionEvents);
    }

    public static void registerMessageEvents(@NotNull MessageEvent... events) {
        messageEvents.addAll(Lists.newArrayList(events));
    }

    public static Set<MessageEvent> getMessageEvents() {
        return Sets.newHashSet(messageEvents);
    }

}
