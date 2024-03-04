package com.ljyhhh.tciotsv.home.base

data class MQTTData(val name:String,val mode:String,val serial:String,val grip:String)
//{"name":"library","mode":"read","serial":"*","grip":""}
//name 某个项目
//mode 读或者是写
//serial  项目里的某个传感器,或者是*(读取所有)
//grip 控制量 当write时，控制开关. 0 or 1