package com.deflatedpickle.barque.gui

import com.deflatedpickle.barque.Barque
import com.deflatedpickle.barque.RubyThread
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.XMLBar
import com.deflatedpickle.barque.xml.XMLWidget
import org.jruby.RubyFixnum
import org.jruby.RubyObject
import org.jruby.runtime.Block
import java.awt.*
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.Timer

class BarWindow(val monitor: GraphicsDevice, val xmlLayout: XMLBar, val style: HashMap<String, HashMap<String, Any>>) : JFrame("Bar ${Barque.INSTANCE.shellList.size}") {
    val screenSize = monitor.defaultConfiguration.bounds
    val screenWidth = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["width"], screenSize.width, null)!!
    val screenHeight = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["height"], screenSize.height, null)!!

    init {
        this.isUndecorated = true
        // Set the size of the bar
        this.size = Dimension(screenWidth, screenHeight)

        // Centres the bar on the monitor
        this.location = Point(
                screenSize.x + (screenSize.width / 2) - (this.size.width / 2),
                screenSize.y + (screenSize.height / 2) - (this.size.height / 2)
        )

        // Move the window to where the CSS wants it
        val left = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["left"], screenSize.width, 0)
        val right = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["right"], screenSize.width, 0)
        val top = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["top"], screenSize.height, 0)
        val bottom = CSSUtil.runTheNumbers(style[xmlLayout.clazz]!!["bottom"], screenSize.height, 0)
        this.location = Point(
                this.location.x + (left ?: 0) / 2 - (right ?: 0) / 2,
                this.location.y + (top ?: 0) / 2 - (bottom ?: 0) / 2
        )
    }

    fun updateWidgets() {
        when (style[xmlLayout.clazz]!!["display"].toString()) {
            "grid" -> this.layout = GridBagLayout()
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

                    val width = CSSUtil.runTheNumbers(style[i.clazz]!!["width"], this.size.width, 0)!!
                    val height = CSSUtil.runTheNumbers(style[i.clazz]!!["height"], this.size.height, 0)!!

                    when (this.layout) {
                        is GridBagLayout -> {
                            println("Grid")
                            val row = CSSUtil.runTheNumbers(style[i.clazz]!!["grid-row"], 0, 0)
                            val column = CSSUtil.runTheNumbers(style[i.clazz]!!["grid-column"], 0, 0)
                        }
                    }
                }
            }
        }

        Timer(1000 / 60, ActionListener {
            for ((k, v) in widgetMap) {
                RubyThread.rubyContainer.callMethod(v, "update")
            }
        }).start()

        // Background Colour
        // val colour = java.awt.Color.decode((style[xmlLayout.clazz]!!["background"] as TermColorImpl).toString())
        // this.background = Color(display, RGB(colour.red, colour.green, colour.blue))
    }
}