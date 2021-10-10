package com.baiyu.designpattern.dynamicproxydemo.proxy

class KVEntry<K, V>(var key: K, var value: V) {
    override fun toString(): String {
        return "ParameterEntry{" +
                "key=" + key +
                ", value=" + value +
                '}'
    }
}