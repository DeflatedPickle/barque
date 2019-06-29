package com.deflatedpickle.barque

import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.util.GlobalValues
import com.deflatedpickle.barque.xml.Bar
import cz.vutbr.web.csskit.TermColorImpl
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Monitor
import org.eclipse.swt.widgets.Shell

class Window(display: Display, monitor: Monitor, val layout: Bar, val style: HashMap<String, Any>) : Shell(display, SWT.NO_TRIM) {
    init {
        this.text = "Bar ${GlobalValues.barCount}"

        // Initial Placement
        val left = CSSUtil.runTheNumbers(style["left"], monitor.clientArea.width, 0)
        val top = CSSUtil.runTheNumbers(style["top"], monitor.clientArea.height, 0)
        this.location = Point(monitor.clientArea.x + (left ?: 0), monitor.clientArea.y + (top ?: 0))

        val width = CSSUtil.runTheNumbers(style["width"], monitor.clientArea.width, null)!!
        val height = CSSUtil.runTheNumbers(style["height"], monitor.clientArea.height, null)!!

        val right = CSSUtil.runTheNumbers(style["right"], monitor.clientArea.width, 0)
        val bottom = CSSUtil.runTheNumbers(style["bottom"], monitor.clientArea.height, 0)
        this.size = Point(width - (right ?: 0) / 2, height - (bottom ?: 0) / 2)

        // Secondary Placement
        val x = when {
            left == 0 -> monitor.clientArea.x
            // right == 0 -> monitor.clientArea.width - this.size.x
            else -> this.location.x - this.size.x / 2
        }

        val y = when {
            top == 0 -> monitor.clientArea.y
            // bottom == 0 -> monitor.clientArea.height - this.size.y
            else -> this.location.y - this.size.y / 2
        }
        this.location = Point(x, y)

        // Background Colour
        val colour = java.awt.Color.decode((style["background"] as TermColorImpl).toString())
        this.background = Color(display, RGB(colour.red, colour.green, colour.blue))
    }

    override fun checkSubclass() {
    }
}