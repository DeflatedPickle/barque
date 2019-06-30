package com.deflatedpickle.barque

import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.util.GlobalValues
import com.deflatedpickle.barque.xml.Bar
import cz.vutbr.web.csskit.TermColorImpl
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.layout.RowLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Monitor
import org.eclipse.swt.widgets.Shell

class Window(display: Display, monitor: Monitor, val layout: Bar, val style: HashMap<String, Any>) : Shell(display, SWT.NO_TRIM or SWT.ON_TOP) {
    init {
        this.text = "Bar ${GlobalValues.barCount}"

        this.setLayout(RowLayout())

        // Set the size of the bar
        val width = CSSUtil.runTheNumbers(style["width"], monitor.clientArea.width, null)!!
        val height = CSSUtil.runTheNumbers(style["height"], monitor.clientArea.height, null)!!
        this.size = Point(width, height)

        // Centres the bar on the monitor
        this.location = Point(
                monitor.clientArea.x + (monitor.clientArea.width / 2) - (this.size.x / 2),
                monitor.clientArea.y + (monitor.clientArea.height / 2) - (this.size.y / 2)
        )

        // Move the window to where the CSS wants it
        val left = CSSUtil.runTheNumbers(style["left"], monitor.clientArea.width, 0)
        val right = CSSUtil.runTheNumbers(style["right"], monitor.clientArea.width, 0)
        val top = CSSUtil.runTheNumbers(style["top"], monitor.clientArea.height, 0)
        val bottom = CSSUtil.runTheNumbers(style["bottom"], monitor.clientArea.height, 0)
        this.location = Point(
                this.location.x + (left ?: 0) / 2 - (right ?: 0) / 2,
                this.location.y + (top ?: 0) / 2 - (bottom ?: 0) / 2
        )

        // Background Colour
        val colour = java.awt.Color.decode((style["background"] as TermColorImpl).toString())
        this.background = Color(display, RGB(colour.red, colour.green, colour.blue))
    }

    override fun checkSubclass() {
    }
}