package com.tim.schupp.retroesapereloaded.Extentions

class DoubleInt(var a: Int,var b: Int) {
    fun equals(other: DoubleInt): Boolean{
        return ((a==other.a)&&(b==other.b))
    }
}