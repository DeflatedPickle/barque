package com.deflatedpickle.barque;

import org.jruby.RubyClass;

import java.util.HashMap;
import java.util.Map;

public class Barque {
    public static void registerWidget(RubyClass widget) {
        Map<String, RubyClass> newWidgets = new HashMap<>(RubyThread.Companion.getScriptClasses());
        newWidgets.put(widget.getName(), widget);
        RubyThread.Companion.setScriptClasses(newWidgets);
    }
}
