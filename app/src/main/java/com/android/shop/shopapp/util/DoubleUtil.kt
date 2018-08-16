package com.android.shop.shopapp.util


import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * double的计算不精确，会有类似0.0000000000000002的误差，正确的方法是使用BigDecimal或者用整型
 * 整型地方法适合于货币精度已知的情况，比如12.11+1.10转成1211+110计算，最后再/100即可
 * 以下是摘抄的BigDecimal方法:
 */
class DoubleUtil : Serializable {
    companion object {
        private const val serialVersionUID = -3345205828566485102L
        // 默认除法运算精度
        private val DEF_DIV_SCALE = 2

        /**
         * 提供精确的加法运算。
         *
         * @param value1 被加数
         * @param value2 加数
         * @return 两个参数的和
         */
        fun add(value1: Double?, value2: Double?): Double {
            val b1 = BigDecimal(java.lang.Double.toString(value1!!))
            val b2 = BigDecimal(java.lang.Double.toString(value2!!))
            return b1.add(b2).toDouble()
        }

        /**
         * 提供精确的减法运算。
         *
         * @param value1 被减数
         * @param value2 减数
         * @return 两个参数的差
         */
        fun sub(value1: Double?, value2: Double?): Double {
            val b1 = BigDecimal(java.lang.Double.toString(value1!!))
            val b2 = BigDecimal(java.lang.Double.toString(value2!!))
            return b1.subtract(b2).toDouble()
        }

        /**
         * 提供精确的乘法运算。
         *
         * @param value1 被乘数
         * @param value2 乘数
         * @return 两个参数的积
         */
        fun mul(value1: Double?, value2: Double?): Double {
            val b1 = BigDecimal(java.lang.Double.toString(value1!!))
            val b2 = BigDecimal(java.lang.Double.toString(value2!!))
            return b1.multiply(b2).toDouble()
        }

        /**
         * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
         *
         * @param dividend 被除数
         * @param divisor  除数
         * @param scale    表示表示需要精确到小数点以后几位。
         * @return 两个参数的商
         */
        @JvmOverloads
        fun divide(dividend: Double?, divisor: Double?, scale: Int = DEF_DIV_SCALE): Double {
            if (scale < 0) {
                throw IllegalArgumentException("The scale must be a positive integer or zero")
            }
            val b1 = BigDecimal(java.lang.Double.toString(dividend!!))
            val b2 = BigDecimal(java.lang.Double.toString(divisor!!))
            return b1.divide(b2, scale, RoundingMode.HALF_UP).toDouble()
        }

        /**
         * 提供指定数值的（精确）小数位四舍五入处理。
         *
         * @param value 需要四舍五入的数字
         * @param scale 小数点后保留几位
         * @return 四舍五入后的结果
         */
        fun round(value: Double, scale: Int): Double {
            if (scale < 0) {
                throw IllegalArgumentException("The scale must be a positive integer or zero")
            }
            val b = BigDecimal(java.lang.Double.toString(value))
            val one = BigDecimal("1")
            return b.divide(one, scale, RoundingMode.HALF_UP).toDouble()
        }
    }
}
/**
 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
 *
 * @param dividend 被除数
 * @param divisor  除数
 * @return 两个参数的商
 */