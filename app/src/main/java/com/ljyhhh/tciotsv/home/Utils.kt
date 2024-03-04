package com.ljyhhh.tciotsv.home

object Utils {
    fun ppm(s: String): Int {
        val t=s.replace(" ","").substring(4,8).toInt(16)
        var k=t/65535.0;
        if (k == 0.0)
        {
            k = 20.0
            k = 5000.0 / k * (k - 4.0)
        }
        else
        {
            k = k * 16.0 + 4.0
            k = 312.0 * (k - 4.0)
        }
        return k.toInt()

    }
}