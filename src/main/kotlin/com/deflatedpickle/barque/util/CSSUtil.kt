package com.deflatedpickle.barque.util

import com.deflatedpickle.barque.gui.BarWindow
import cz.vutbr.web.css.RuleMedia
import cz.vutbr.web.css.RuleSet
import cz.vutbr.web.css.StyleSheet
import cz.vutbr.web.csskit.*
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Font
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.widgets.Display
import org.jruby.RubyObject
import org.jruby.runtime.builtin.IRubyObject
import kotlin.math.roundToInt

object CSSUtil {
    fun parseCSS(rawStyle: StyleSheet): HashMap<String, HashMap<String, Any>> {
        val style = mutableMapOf<String, HashMap<String, Any>>()
        for (rule in rawStyle) {
            when (rule) {
                is RuleSet -> {
                    val ruleName = rule.selectors[0].lastSelector.className
                    val propertyMap = mutableMapOf<String, Any>()
                    for (decl in rule) {
                        for (term in decl) {
                            propertyMap[decl.property] = term
                        }

                    }
                    style[ruleName] = propertyMap as HashMap<String, Any>
                }
                is RuleMedia -> {
                }
                else -> {
                }
            }
        }

        return style as HashMap<String, HashMap<String, Any>>
    }

    fun runTheNumbers(originalValue: Any?, maxValue: Int?, defaultValue: Int?): Int? {
        if (originalValue == null) {
            return defaultValue ?: maxValue
        }

        return when (originalValue as TermFloatValueImpl) {
            is TermPercentImpl -> {
                ((originalValue.value * maxValue!!) / 100).roundToInt()
            }
            is TermLengthImpl -> {
                originalValue.value.roundToInt()
            }
            else -> 0
        }
    }

    fun themeGraphics(theme: String, bar: BarWindow, graphics: GC, rubyObject: RubyObject): GC {
        val x = rubyObject.getInstanceVariable("@x")
        val y = rubyObject.getInstanceVariable("@y")
        val width = rubyObject.getInstanceVariable("@width")
        val height = rubyObject.getInstanceVariable("@height")

        val fontWeight = bar.style[theme]!!.getOrDefault("font-weight", null)
        var weight = -1
        if (fontWeight != null) {
            weight = when ((fontWeight as TermIdentImpl).value) {
                "normal" -> SWT.NORMAL
                "bold" -> SWT.BOLD
                else -> -1
            }
        }

        val fontStyle = bar.style[theme]!!.getOrDefault("font-style", null)
        var style = -1
        if (fontStyle != null) {
            style = when ((fontStyle as TermIdentImpl).value) {
                "normal" -> SWT.NORMAL
                "italic" -> SWT.ITALIC
                else -> -1
            }
        }

        val fontType = if (weight != -1 && style != -1) {
            weight or style
        }
        else if (weight != -1) {
            weight
        }
        else if (style != -1) {
            style
        }
        else {
            SWT.NORMAL
        }

        graphics.font = Font(Display.getDefault(), bar.style[theme]!!.getOrDefault("font-family", graphics.font.fontData[0].name) as String, runTheNumbers(bar.style[theme]!!["font-size"], bar.height, 12)!!, fontType)

        val rawColour = bar.style[theme]!!["color"]
        if (rawColour != null) {
            val colour = java.awt.Color.decode((rawColour as TermColorImpl).toString())
            graphics.foreground = Color(Display.getCurrent(), RGB(colour.red, colour.green, colour.blue))
        }

        return graphics
    }
}