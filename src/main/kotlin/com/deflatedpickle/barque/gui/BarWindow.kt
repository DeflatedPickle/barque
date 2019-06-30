package com.deflatedpickle.barque.gui

import com.deflatedpickle.barque.RubyThread
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.Bar
import com.deflatedpickle.barque.xml.Widget
import cz.vutbr.web.csskit.TermColorImpl
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Monitor
import org.eclipse.swt.widgets.Shell
import org.jruby.RubyObject
import org.jruby.runtime.Block
import org.jruby.runtime.builtin.IRubyObject

class BarWindow(display: Display, monitor: Monitor, val xmlLayout: Bar, val style: HashMap<String, HashMap<String, Any>>) : Shell(display, SWT.NO_TRIM or SWT.ON_TOP) {
    companion object {
        var barCount = 0
    }

    init {
        this.text = "Bar $barCount"

        this.layout = FillLayout()

        // Set the size of the bar
        val width = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["width"], monitor.clientArea.width, null)!!
        val height = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["height"], monitor.clientArea.height, null)!!
        this.size = Point(width, height)

        // Centres the bar on the monitor
        this.location = Point(
                monitor.clientArea.x + (monitor.clientArea.width / 2) - (this.size.x / 2),
                monitor.clientArea.y + (monitor.clientArea.height / 2) - (this.size.y / 2)
        )

        // Move the window to where the CSS wants it
        val left = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["left"], monitor.clientArea.width, 0)
        val right = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["right"], monitor.clientArea.width, 0)
        val top = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["top"], monitor.clientArea.height, 0)
        val bottom = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["bottom"], monitor.clientArea.height, 0)
        this.location = Point(
                this.location.x + (left ?: 0) / 2 - (right ?: 0) / 2,
                this.location.y + (top ?: 0) / 2 - (bottom ?: 0) / 2
        )

        // Load the widgets
        val widgetMap = mutableMapOf<Widget, IRubyObject>()
        if (xmlLayout.widgetList != null) {
            for (i in xmlLayout.widgetList) {
                // widgetMap.put(i, RubyThread.rubyContainer.runScriptlet("${i.script}.new"))
                if (RubyThread.scriptClasses.containsKey(i.script)) {
                    widgetMap[i] = RubyThread.scriptClasses.getValue(i.script).newInstance(RubyThread.ruby.currentContext, Block.NULL_BLOCK)

                    val left = CSSUtil.runTheNumbers(style[i.clazz]!!["left"], this.size.x, 0)
                    val right = CSSUtil.runTheNumbers(style[i.clazz]!!["right"], this.size.x, 0)
                    val top = CSSUtil.runTheNumbers(style[i.clazz]!!["top"], this.size.y, 0)
                    val bottom = CSSUtil.runTheNumbers(style[i.clazz]!!["bottom"], this.size.y, 0)

                    val width = CSSUtil.runTheNumbers(style[i.clazz]!!["width"], this.size.x, 0)
                    val height = CSSUtil.runTheNumbers(style[i.clazz]!!["height"], this.size.y, 0)
                    RubyThread.rubyContainer.callMethod(widgetMap[i], "place", (left ?: 0) - (right ?: 0), (top ?: 0) - (bottom ?: 0), (width ?: 0), (height ?: 0))
                }
            }
        }

        val canvas = BarqueCanvas(this, widgetMap)

        // Background Colour
        val colour = java.awt.Color.decode((style[xmlLayout.clazz]!!["background"] as TermColorImpl).toString())
        canvas.background = Color(display, RGB(colour.red, colour.green, colour.blue))
    }

    override fun checkSubclass() {
    }
}