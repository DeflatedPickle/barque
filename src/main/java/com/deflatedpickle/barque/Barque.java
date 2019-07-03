package com.deflatedpickle.barque;

import com.deflatedpickle.barque.gui.BarWindow;
import org.jruby.RubyClass;

import java.util.*;

public class Barque {
    public static Barque INSTANCE = new Barque();
    public List<BarWindow> shellList = new Vector<>();

    public static void registerWidget(RubyClass widget) {
        Map<String, RubyClass> newWidgets = new HashMap<>(RubyThread.Companion.getScriptClasses());
        newWidgets.put(widget.getName(), widget);
        RubyThread.Companion.setScriptClasses(newWidgets);
    }
}
