package com.deflatedpickle.barque

import com.deflatedpickle.barque.gui.BarWindow
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.XMLBar
import com.deflatedpickle.barque.xml.XMLBarque
import com.deflatedpickle.barque.xml.XMLMonitor
import com.deflatedpickle.barque.xml.XMLWidget
import com.thoughtworks.xstream.XStream
import cz.vutbr.web.css.CSSFactory
import java.awt.GraphicsEnvironment
import javax.swing.UIManager

fun main(args: Array<String>) {
    Thread(RubyThread()).start()

    val xStream = XStream()
    xStream.processAnnotations(XMLBarque::class.java)
    xStream.processAnnotations(XMLMonitor::class.java)
    xStream.processAnnotations(XMLBar::class.java)
    xStream.processAnnotations(XMLWidget::class.java)

    val layout = xStream.fromXML(ClassLoader.getSystemClassLoader().getResource("layout.xml")) as XMLBarque
    println("Layout: $layout")

    val rawStyle = CSSFactory.parseString(ClassLoader.getSystemClassLoader().getResource("style.css").readText(), null)
    val style = CSSUtil.parseCSS(rawStyle)
    println("Style: $style")

    val graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val screenDevices = graphicsEnviroment.screenDevices

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    for (monitor in layout.monitorList!!) {
        for (bar in monitor.barList!!) {
            when (monitor.type) {
                "primary" -> {
                    val barWindow = BarWindow(screenDevices[0], bar, style)
                    Barque.INSTANCE.shellList.add(barWindow)
                    barWindow.updateWidgets()
                }
                "all" -> {
                    for (i in screenDevices) {
                        val barWindow = BarWindow(i, bar, style)
                        Barque.INSTANCE.shellList.add(barWindow)
                        barWindow.updateWidgets()
                    }
                }
            }
        }
    }

    println("Widgets: ${RubyThread.scriptClasses}")
    for (window in Barque.INSTANCE.shellList) {
        window.isVisible = true
    }
}
