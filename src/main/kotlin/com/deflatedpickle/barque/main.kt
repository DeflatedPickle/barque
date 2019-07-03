package com.deflatedpickle.barque

import com.deflatedpickle.barque.gui.BarWindow
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.XMLBar
import com.deflatedpickle.barque.xml.XMLBarque
import com.deflatedpickle.barque.xml.XMLMonitor
import com.thoughtworks.xstream.XStream
import cz.vutbr.web.css.CSSFactory
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Widget

fun main(args: Array<String>) {
    Thread(RubyThread()).start()

    val display = Display()

    val xStream = XStream()
    xStream.processAnnotations(XMLBarque::class.java)
    xStream.processAnnotations(XMLMonitor::class.java)
    xStream.processAnnotations(XMLBar::class.java)
    xStream.processAnnotations(Widget::class.java)

    val layout = xStream.fromXML(ClassLoader.getSystemClassLoader().getResource("layout.xml")) as XMLBarque
    println("Layout: $layout")

    val rawStyle = CSSFactory.parseString(ClassLoader.getSystemClassLoader().getResource("style.css").readText(), null)
    val style = CSSUtil.parseCSS(rawStyle)
    println("Style: $style")

    for (monitor in layout.monitorList!!) {
        for (bar in monitor.barList!!) {
            when (monitor.type) {
                "primary" -> {
                    val barWindow = BarWindow(display, display.primaryMonitor, bar, style)
                    Barque.INSTANCE.shellList.add(barWindow)
                    barWindow.updateWidgets()
                }
                "all" -> {
                    for (i in display.monitors) {
                        val barWindow = BarWindow(display, i, bar, style)
                        Barque.INSTANCE.shellList.add(barWindow)
                        barWindow.updateWidgets()
                    }
                }
            }
        }
    }

    println("Widgets: ${RubyThread.scriptClasses}")
    for (window in Barque.INSTANCE.shellList) {
        window.layout()
        window.open()
    }

    while (!Barque.INSTANCE.shellList[0].isDisposed) {
        if (!display.readAndDispatch()) {
            display.sleep()
        }
    }
}
