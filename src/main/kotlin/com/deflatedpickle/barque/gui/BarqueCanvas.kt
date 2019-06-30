package com.deflatedpickle.barque.gui

import com.deflatedpickle.barque.RubyThread
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.Widget
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Canvas
import org.jruby.RubyObject
import org.jruby.runtime.builtin.IRubyObject

class BarqueCanvas(parent: BarWindow, style: HashMap<String, HashMap<String, Any>>, widgetMap: Map<Widget, RubyObject>) : Canvas(parent, SWT.NONE) {
    init {
        this.addPaintListener {
            for ((k, v) in widgetMap) {
                RubyThread.rubyContainer.callMethod(v, "draw", CSSUtil.themeGraphics(k.clazz, parent, it.gc, v))
            }
        }
    }
}