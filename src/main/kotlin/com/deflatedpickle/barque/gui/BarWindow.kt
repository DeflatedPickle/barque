package com.deflatedpickle.barque.gui

import com.deflatedpickle.barque.Barque
import com.deflatedpickle.barque.RubyThread
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.XMLBar
import com.deflatedpickle.barque.xml.XMLWidget
import cz.vutbr.web.csskit.TermColorImpl
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Monitor
import org.eclipse.swt.widgets.Shell
import org.jruby.RubyFixnum
import org.jruby.RubyObject
import org.jruby.runtime.Block

class BarWindow(display: Display, monitor: Monitor, val xmlLayout: XMLBar, val style: HashMap<String, HashMap<String, Any>>) : Shell(display, SWT.NO_TRIM or SWT.ON_TOP) {
    val width = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["width"], monitor.clientArea.width, null)!!
    val height = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["height"], monitor.clientArea.height, null)!!

    init {
        this.text = "Bar ${Barque.INSTANCE.shellList.size}"

        // Set the size of the bar
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
    }

    fun updateWidgets() {
        when (style[xmlLayout.clazz]!!["display"].toString()) {
            "grid" -> this.layout = GridLayout()
        }

        // Load the widgets
        val widgetMap = mutableMapOf<XMLWidget, RubyObject>()
        if (xmlLayout.widgetList != null) {
            for (i in xmlLayout.widgetList) {
                // widgetMap.put(i, RubyThread.rubyContainer.runScriptlet("${i.script}.new"))
                if (RubyThread.scriptClasses.containsKey(i.script)) {
                    widgetMap[i] = RubyThread.scriptClasses.getValue(i.script).newInstance(RubyThread.ruby.currentContext, RubyFixnum(RubyThread.ruby, Barque.INSTANCE.shellList.indexOf(this).toLong()), Block.NULL_BLOCK) as RubyObject

                    // val left = CSSUtil.runTheNumbers(style[i.clazz]!!["left"], this.size.x, 0)
                    // val right = CSSUtil.runTheNumbers(style[i.clazz]!!["right"], this.size.x, 0)
                    // val top = CSSUtil.runTheNumbers(style[i.clazz]!!["top"], this.size.y, 0)
                    // val bottom = CSSUtil.runTheNumbers(style[i.clazz]!!["bottom"], this.size.y, 0)

                    val width = CSSUtil.runTheNumbers(style[i.clazz]!!["width"], this.size.x, 0)!!
                    val height = CSSUtil.runTheNumbers(style[i.clazz]!!["height"], this.size.y, 0)!!

                    when (this.layout) {
                        is GridLayout -> {
                            println("Grid")
                            val row = CSSUtil.runTheNumbers(style[i.clazz]!!["grid-row"], 0, 0)
                            val column = CSSUtil.runTheNumbers(style[i.clazz]!!["grid-column"], 0, 0)
                        }
                    }
                }
            }
        }

        display.timerExec(1000 / 60, object : Runnable {
            override fun run() {
                for ((k, v) in widgetMap) {
                    RubyThread.rubyContainer.callMethod(v, "update")
                }

                display.timerExec(1000 / 60, this)
            }
        })

        // Background Colour
        // val colour = java.awt.Color.decode((style[xmlLayout.clazz]!!["background"] as TermColorImpl).toString())
        // this.background = Color(display, RGB(colour.red, colour.green, colour.blue))
    }

    override fun checkSubclass() {
    }
}