package com.deflatedpickle.barque.util

import com.deflatedpickle.barque.gui.BarWindow
import cz.vutbr.web.css.RuleMedia
import cz.vutbr.web.css.RuleSet
import cz.vutbr.web.css.StyleSheet
import cz.vutbr.web.csskit.*
import org.jruby.RubyObject
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
}